package org.aa.geo;

import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.aa.admin.Developer;
import org.aa.admin.JDBCMySql;
import org.neo4j.graphdb.Node;



public class GeoOperations {
	private final String id = "id";
	private final String snid="snid";
	
	public JSONObject validatePrimaryNode(String postalcode, String name,
			String lat, String lon, String province, String district,
			String description) {

		JSONObject json = new JSONObject();
		if (!isvalidLat(lat)) {
			json.put("status", "fail");
			json.put("desc",
					"Invalid Latitude value.It shouldbe between -90 and 90");
			return json;
		} else if (!isvalidLon(lon)) {
			json.put("status", "fail");
			json.put("desc",
					"Invalid Longitude value.It shouldbe between -180 and 180");
			return json;
		}
		GeoApp ap1=new GeoApp();
		
		try {
			if(ap1.validateType(id, postalcode)){
				json.put("status", "fail");
				json.put("desc", "There's a primary node with this postal code");

				return json;
			}
			
			if (!ap1.validateType("province", province)) {
				json.put("status", "fail");
				json.put("desc", "Invalid Province");

				return json;
			}
			if (!ap1.validateType("district", district)) {
				json.put("status", "fail");
				json.put("desc", "Invalid District");
				//ap1.shutdown("from validatePrimaryNode");
				return json;
			}
			
			
		} catch (Exception e) {
			json.put("status", "fail");
			json.put("desc","exception occurs while validating province and district");
			return json;
		}

		//ap1.shutdown("from validatePrimaryNode");
		
		json.put("status", "success");
		return json;
	}
	
	
		public JSONObject convertPrimaryNodetoJSON(Node node) {
		JSONObject json = new JSONObject();

		
		if (node != null) {
			try {
				json.put("postalcode", node.getProperty("postalcode"));
				json.put("name", node.getProperty("name"));
				json.put("lat", node.getProperty("lat"));
				json.put("lon", node.getProperty("lon"));				
				json.put("province", node.getProperty("province"));
				json.put("district", node.getProperty("district"));
				json.put("description", node.getProperty("description"));
				
			} catch (Exception e) {
				json.put("status", "fail");
				json.put("desc","exception occurs in converting secondarynode to JSONObject:"+e.getMessage());
			}

		} else {
			json.put("status", "fail");
			json.put("desc", "empty node cannot convert to json");
		}

		return json;
	}
		public JSONObject validateSecondaryNode(String name, String postalcode,
				String type, String lat, String lon, String address, String phone,
				String email, String image, String website, String desc,
				String is_government) {
		/*	String[] types = { "Academic Center", "Bank", "Book Shop", "Bus Stand",
					"Cloths", "Cultural Place", "Electricity Board", "Food Shop",
					"Ground", "Hospital", "Hotel", "Medical Center",
					"Municipal Council", "Park", "police", "Post office",
					"Railway Station", "School", "Telecommunication Center",
					"Super Market", "University", "Mobile Tower", "Communication",
					"Restaurant", "Filling Station", "ATM" };*/

			JSONObject json = new JSONObject();
			if (!isvalidLat(lat)) {
				json.put("status", "fail");
				json.put("desc",
						"Invalid Latitude value.It shouldbe between -90 and 90");
				return json;
			} else if (!isvalidLon(lon)) {
				json.put("status", "fail");
				json.put("desc",
						"Invalid Longitude value.It shouldbe between -180 and 180");
				return json;
			} else if (!isValidPhone(phone)) {
				json.put("status", "fail");
				json.put("desc", "Invalid phone number");
				return json;
			} else if (!isValidEmail(email)) {
				json.put("status", "fail");
				json.put("desc", "Invalid email");
				return json;
			} else if (!isValidWeb(website)) {
				json.put("status", "fail");
				json.put("desc", "Invalid web");
				return json;
			}

			try {
				boolean x = Boolean.parseBoolean(is_government);
			} catch (Exception e) {
				json.put("status", "fail");
				json.put("desc", "need boolean value for is_government");
				return json;
			}

			try {
				GeoApp a2=new GeoApp();

				if (!a2.validateType("id", postalcode)) {
					json.put("status", "fail");
					json.put("desc", "Invalid Postal Code");					
					return json;
				}
				
			} catch (Exception e) {
				System.out.println("i'm herereerer");
			}

			boolean x = isValidSecondaryNodeType(type);
			/*for (int i = 0; i < types.length; i++) {
				if (types[i].equals(type)) {
					x = true;
					break;
				}
			}*/
			if (x == false) {
				json.put("status", "fail");
				json.put("desc", "Please check the Secondary Node Type");
				return json;
			}

			json.put("status", "success");
			return json;
		}
		
		public boolean isValidSecondaryNodeType(String type){
			/*String[] types = { "Academic Center", "Bank", "Book Shop", "Bus Stand",
					"Cloths", "Cultural Place", "Electricity Board", "Food Shop",
					"Ground", "Hospital", "Hotel", "Medical Center",
					"Municipal Council", "Park", "police", "Post office",
					"Railway Station", "School", "Telecommunication Center",
					"Super Market", "University", "Mobile Tower", "Communication",
					"Restaurant", "Filling Station", "ATM" };
			*/
			//String[] types=null;
			
			//JDBCMySql c=new JDBCMySql();
			//JSON
			try{
				

				Developer a=new Developer();
				
			
			String[] types = jsonToStringArray(a.getAllPlaceTypes());
			
		

			
			
			boolean x = false;
			for (int i = 0; i < types.length; i++) {
				if (types[i].equals(type)) {				
					x = true;
					break;
				}
			}
			return x;
			
			}catch(Exception e){
				
				System.out.println("cannot load all types from db.");
				return false;
			}
		}
		
		public String[] jsonToStringArray(JSONObject json){
			try{			

			
				 String []arr=new String[json.size()];	

				String x=json.toString();
				
				x = x.replaceAll("\"", "");
				x = x.replaceAll("\\{", "");
				x = x.replaceAll("\\}", "");
				
				
				int i=0;
				StringTokenizer st1 = new StringTokenizer(x,",");
				  while(st1.hasMoreTokens()){
					  
					  StringTokenizer st2 = new StringTokenizer(st1.nextToken(),":,");
					  while(st2.hasMoreTokens()){
						  st2.nextElement();  // if you want to get numbers like 1,2,3.. in json output use this
					    arr[i]=st2.nextToken();
					    i++;
					  }
					 
				  }
				  System.out.println("______________-");
				  
				  for( i=0;i<arr.length;i++){
					  System.out.println(arr[i]);
				  }
				 
	
			
			return arr;
			}
			catch(Exception e){
				return null;
			}
			
			
		
		}
	public JSONObject convertSecondaryNodetoJSON(Node node) {
		JSONObject json = new JSONObject();

		if (node != null) {
			try {
				json.put("name", node.getProperty("name"));
				json.put("postalcode", node.getProperty("postalcode"));
				json.put("type", node.getProperty("type"));
				json.put("lat", node.getProperty("lat"));
				json.put("lon", node.getProperty("lon"));
				json.put("address", node.getProperty("address"));
				json.put("phone", node.getProperty("phone"));
				json.put("email", node.getProperty("email"));
				json.put("image", node.getProperty("image"));
				json.put("website", node.getProperty("website"));
				json.put("description", node.getProperty("description"));
				json.put("is_government", node.getProperty("is_government"));
				json.put("snid", node.getProperty("snid"));
			} catch (Exception e) {
				json.put("status", "fail");
				json.put("desc","exception occurs in converting secondarynode to JSONObject:"+e.getMessage());
			}

		} else {
			json.put("status", "fail");
			json.put("desc", "empty node cannot convert to json");
		}

		return json;
	}
	
	public boolean isvalidLat(String lat) {
		boolean ans = false;
		try {
			double latitude = Double.parseDouble(lat);
			if (latitude >= -90 && latitude <= 90) {
				ans = true;
			}
		} catch (Exception e) {
		}
		return ans;
	}

	public boolean isvalidLon(String lon) {
		boolean ans = false;
		try {
			double longitude = Double.parseDouble(lon);
			if (longitude >= -180 && longitude <= 180) {
				ans = true;
			}
		} catch (Exception e) {
		}
		return ans;
	}

	public boolean isValidPhone(String phone) {
		boolean ans = false;
		if (phone.length() == 10) {
			try {
				int n = Integer.parseInt(phone);
				ans = true;
			} catch (Exception e) {
			}
		} else if (phone.length() == 13) {
			String x = phone.substring(0, 3);
			if (x.substring(0, 1).equals("+") && x.substring(1, 3).equals("94")) {
				String X2 = phone.substring(3, phone.length());
				try {
					int nn = Integer.parseInt(X2);
					ans = true;
				} catch (Exception e) {
				}

			}
		} else if (phone.length() == 14) {
			String x1 = phone.substring(0, 4);
			String x2 = phone.substring(4, phone.length());
			if (x1.equals("0094")) {
				try {
					int nn = Integer.parseInt(x2);
					ans = true;
				} catch (Exception e) {
				}
			}
		}

		return ans;
	}

	public boolean isValidEmail(String email) {
		boolean ans = false;
		Pattern p = Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]+.[a-zA-Z]+");
		Matcher m = p.matcher(email);

		ans = m.matches();

		return ans;
	}

	public boolean isValidWeb(String web) {
		boolean ans = false;
		try {
			web = "http://" + web;
			URL url = new URL(web);
			ans = true;
		} catch (Exception e) {
		}
		return ans;
	}

}
