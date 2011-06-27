package org.aa.social;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.sf.json.JSONObject;

import org.aa.geo.GeoApp;



@Path("/socialservices")
public class SocialServices {
	// >>>>>>>>>>>>>>>>>>>>>>>To create a user Account>>>>>>>>>>>>>>>>>>>>>>>
		
		@POST
		@Path("create/{email}/{password}/{name}/{address}/{phone}/{web}/{home_town}/{living_town}/{current_lat}/{current_lon}/" +
				"{profile_pic}/{works_in}/{description}")
		@Produces("application/json")
		//@Produces({"application/json", "application/xml"})
		public JSONObject createUser(
				@PathParam("email") String em,
				@PathParam("password") String pw,
				@PathParam("name") String name,
				@PathParam("address") String address,
				@PathParam("phone") String phone,
				@PathParam("web") String web,
				@PathParam("home_town") String home_town,
				@PathParam("living_town") String living_town,
				@PathParam("current_lat") String current_lat,
				@PathParam("current_lon") String current_lon,
				@PathParam("profile_pic") String profile_pic,
				@PathParam("works_in") String works_in,
				@PathParam("description") String description)
		{
			
			SocialOperations op=new SocialOperations();
			String email = null;
			String password=null;
			try{
				email=op.getDecodedData(em);			
				password=op.getDecodedData(pw);
				
				
			}catch(Exception e){
				JSONObject json=new JSONObject();
				json.put("status", "fail");
				json.put("desc", "cannot decode email or password:"+e.getMessage());
				return json;
			}
				
			JSONObject x = op.validateUser(email, current_lat, current_lon);			
	
			if(!x.get("status").equals("success")){				
				return x;
			}
			
			
			User user=new User(email,password,name,address,phone,web,home_town,living_town,current_lat,
			current_lon,profile_pic,works_in,description);
				
			JSONObject result=new JSONObject();
			SocialApp m=new SocialApp();	
				
				
			try{
					
				JSONObject r=m.addUser(user);						
				result=r;
					
			}
			catch(Exception e){
				JSONObject json = new JSONObject();
				json.put("status", "fail");
				json.put("desc", "exception occurs while creating a user");
				result=json;
			}
			
			
			return result;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		//_________________________________________________________________
		@POST
		@Path("/a")
		public void shutdown(){
			GeoApp aa=new GeoApp();
			aa.shutdown("from geoservices");
		}
		
}
