package org.aa.social;

import it.sauronsoftware.base64.Base64;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.tomcat.dbcp.dbcp.DbcpException;
import org.neo4j.graphdb.Node;

public class SocialOperations {
	private final static String id = "id";
	
	
	
	public JSONObject validateUser(String email,String lat,String lon){
		JSONObject json = new JSONObject();
		if ((!lat.isEmpty())&& (!isvalidLat(lat))) {
			json.put("status", "fail");
			json.put("desc","Invalid Latitude value.It shouldbe between -90 and 90");
			return json;
		} else if ((!lat.isEmpty())&&(!isvalidLon(lon))) {
			json.put("status", "fail");
			json.put("desc","Invalid Longitude value.It shouldbe between -180 and 180");
			return json;
		
		} else if (!isValidEmail(email)) {
			json.put("status", "fail");
			json.put("desc", "Invalid email");
			return json;
		}
		
		SocialApp ap=new SocialApp();
		if(ap.validateType(id,email)){
			json.put("status", "fail");
			json.put("desc", "user already registerd");
			return json;
		}
		json.put("status", "success");
		return json;
	}
	
	public JSONObject convertUserNodetoJSON(Node user) {
		JSONObject json = new JSONObject();

		if (user != null) {
			try {
				
				json.put("name", user.getProperty("name"));
				json.put("address", user.getProperty("address"));
				json.put("phone", user.getProperty("phone"));
				json.put("web", user.getProperty("web"));				
				json.put("home_Town", user.getProperty("home_Town"));
				json.put("living_Town", user.getProperty("living_Town"));
				json.put("current_lat", user.getProperty("current_lat"));
				json.put("current_lon", user.getProperty("current_lon"));
				json.put("profile_picture", user.getProperty("profile_picture"));
				json.put("work_in", user.getProperty("work_in"));
				json.put("discription", user.getProperty("discription"));
				json.put("uid", user.getProperty("uid"));
				
				
				
			} catch (Exception e) {
				json.put("status", "fail");
				json.put("desc","exception occurs in converting secondarynode to JSONObject:"+e.getMessage());
				System.out.println("ccc="+e.getMessage());
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

public boolean isValidEmail(String email) {
	boolean ans = false;
	Pattern p = Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]+.[a-zA-Z]+");
	Matcher m = p.matcher(email);

	ans = m.matches();

	return ans;
}

public String getEncodedData(String S) {
	String result1 = null;

	result1 = Base64.encode(S).toString();

	return result1.toString();
}

public String getDecodedData(String S) {
	String result1 = null;
	if (org.apache.catalina.util.Base64.isBase64(S)) {
		result1 = Base64.decode(S).toString();
	}
	return result1.toString();
}

}
