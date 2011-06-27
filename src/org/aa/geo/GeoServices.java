package org.aa.geo;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;
@Path("/geoservices")
public class GeoServices {
	private final String snid="snid";
	
	@POST
	@Path("primarynode/{postalcode}/{name}/{lat}/{lon}/{province}/{district}/{description}")
	@Produces("application/json")
	
	public JSONObject createPrimaryNode(
			@PathParam("postalcode") String postalcode,
			@PathParam("name") String name,
			@PathParam("lat") String lat,
			@PathParam("lon") String lon,
			@PathParam("province") String province,
			@PathParam("district") String district,
			@PathParam("description") String description)
			
	{
		
		GeoOperations op=new GeoOperations();	
		JSONObject x=op.validatePrimaryNode(postalcode, name, lat, lon, province, district, description);
		System.out.println("x11111");
		if(!x.get("status").equals("success")){
			
			return x;
		}
		System.out.println("x22222");
		JSONObject result=new JSONObject();
		GeoApp a=new GeoApp();
		try{
		PrimaryNode town=new PrimaryNode(postalcode,name,lat,lon,province,district,description);
		
		System.out.println("1111111");
		result=a.addPrimaryNode(town);
		//a.shutdown("createPrimaryNode");
		
		}
		
		catch(Exception e){
			JSONObject json = new JSONObject();
			json.put("status", "fail");
			json.put("desc", "exception occurs:"+e.getMessage());
			result=json;
			
		}
		
	
		return result;
		
	}
	
	@POST
	@Path("secondarynode/{name}/{postalcode}/{type}/{lat}/{lon}/{address}/{phone}/{email}/{image}/{website}/{description}/{is_government}")
	@Produces("application/json")
	
	public JSONObject createSecondaryNode(			
			@PathParam("name") String name,
			@PathParam("postalcode") String postalcode,
			@PathParam("type") String type,
			@PathParam("lat") String lat,
			@PathParam("lon") String lon,
			@PathParam("address") String address,
			@PathParam("phone") String phone,
			@PathParam("email") String email,			
			@PathParam("image") String image,
			@PathParam("website") String website,
			@PathParam("description") String description,
			@PathParam("is_government") String is_government)
			
			
	{
		
		GeoOperations op=new GeoOperations();
		JSONObject x1=op.validateSecondaryNode(name,postalcode,type,lat, lon, address, phone, email, image, website, description, is_government);
		if(!x1.get("status").equals("success")){
			
			return x1;
		}
		
		SecondaryNode node=new SecondaryNode(name,postalcode,type,lat,lon,address,phone,email,image,website,description,Boolean.parseBoolean(is_government));		
		JSONObject result=new JSONObject();	
		
		GeoApp app=new GeoApp();
		
		try{
		result=app.addSecondaryNode(node);
		
		}
		catch(Exception e){
			JSONObject json = new JSONObject();
			json.put("status", "fail");
			json.put("desc", "exception occurs:"+e.getMessage());
			result=json;
			
		}
		
		return result;		
		
		
	}
	
	
	@POST
	@Path("customsearch/{town}/{type}")
	@Produces("application/json")
	public JSONArray customsearch(@PathParam("town") String town,
			@PathParam("type") String type){
		
	//System.out.println("11111111");
	GeoApp ap1=new GeoApp();
	GeoOperations op2=new GeoOperations();
	//System.out.println("22222222");
	
	
	JSONObject json=new JSONObject();
	JSONArray json_array=new JSONArray();
	if(!op2.isValidSecondaryNodeType(type)){
		
			json.put("status", "fail");
			json.put("desc", "Please check the  Type");
			
			json_array.add(json);
			return json_array;
			
	}
	
	try{
		Node n = null;
		try{
			n=ap1.getTownByName(town);
		 
			json.put("status", "fail");
			json.put("desc", "Invalid town name.function returns null node for this name");
		}
		catch(Exception e){
			json.put("status", "fail");
			json.put("desc", "exception occurs converting town name to node");
		}
		//System.out.println("444444");
		if(n!=null){
			Traverser traverser1 = n.traverse(
					Order.BREADTH_FIRST,
					StopEvaluator.END_OF_GRAPH,ReturnableEvaluator.ALL_BUT_START_NODE,geo_rel.SECONDARY_NODE,
					Direction.OUTGOING );						
					
					JSONObject ans;
					//System.out.println("pc=="+n.getProperty("postalcode"));
					//System.out.println("5555");
			for ( Node f : traverser1)
			{
						if(f.getProperty("type").equals(type)){
							ans=op2.convertSecondaryNodetoJSON(f);
							json_array.add(ans);
						}
						//System.out.println("f===="+f);
			}
			
			
			
			return json_array;
		}else{
			json.put("status", "fail");
			json.put("desc", "There is no node with this name");
		}
	}
	catch(Exception e){
		json.put("status", "fail");
		json.put("desc", "Exception occured:"+e.getMessage());
	}
	
	json_array.add(json);
	return json_array;
		
	}
	
	
	@POST
	@Path("getsecondarynode/{sec_node}")
	@Produces("application/json")
	public JSONObject test(@PathParam("sec_node") String id_val){
		
		GeoApp geoapp=new GeoApp();
		GeoOperations operations=new GeoOperations();
		JSONObject json=new JSONObject();
		try{
			
		Node n=geoapp.indexToNode(snid, id_val);
		
		
		
		if(n!=null){
			if(!n.getProperty("node_type").equals("secondary_node")){
				json.put("status", "fail");
				json.put("desc", "There's no secondary node with this name");
			}
			else{
			json=operations.convertSecondaryNodetoJSON(n);
			
			}
		}
		else{
			json.put("status", "fail");
			json.put("desc", "There's no secondary node with this name");
		}
		
		
		}
		catch(Exception e){
			json.put("status", "fail");
			json.put("desc", e.getMessage());
		}
		
		return json;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//_________________________________________________________________
	@POST
	@Path("/a")
	public void shutdown(){
		GeoApp aa=new GeoApp();
		aa.shutdown("from geoservices");
	}
}
