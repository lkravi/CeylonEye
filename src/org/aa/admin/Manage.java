package org.aa.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sf.json.JSONObject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

@Path("/manage")
public  class Manage {
	private final String id = "id";
	
	@GET
	@Path("token/{un}/{pw}")
	@Produces("application/json")
	public JSONObject getToken(@PathParam("un") String em,@PathParam("pw")String pw,@Context HttpServletRequest req){
		AdminApp a1=new AdminApp();
		AdminOperations op1=new AdminOperations();
		String email,password;
	
		
		if(org.apache.catalina.util.Base64.isBase64(em)&&org.apache.catalina.util.Base64.isBase64(pw)){
		
			
			try{
				email=op1.getDecodedData(em);			
				password=op1.getDecodedData(pw);
				
				
			}catch(Exception e){
				JSONObject json=new JSONObject();
				json.put("status", "fail");
				json.put("desc", "cannot decode email or password:"+e.getMessage());
				return json;
			}
		
		}
		else{
			JSONObject json=new JSONObject();
			json.put("status", "fail");
			json.put("desc", "user name or password is not in Base64 format");
			return json;
		}
		
		
		JSONObject json=new JSONObject();
		
		try{			
			
				Node user=a1.indexToNode(id, email);			
				if(user!=null&&user.getProperty("password").toString().equals(password)){	
					System.out.println("user="+user);
					/*if(a1.validateType("sessionid",email)){
						json.put("status", "fail");
						json.put("desc", "you need to close the previous session.");
						System.out.println("x222");
						return json;
					}*/
				
					String	 token=op1.getRandomNumber();				
					HttpSession session=req.getSession();					
					session.setAttribute("key", token);				
				
					System.out.println("tok="+token);
					json.put("token", token);
					json.put("status", "success");
					json.put("desc","token created");
				
				}
				else{
				
					json.put("status", "fail");
					json.put("desc", "invalid authentication");
				}
		
			}
			catch(Exception e){
				json.put("status", "fail");
				json.put("desc", "Exception while a token:"+e.getMessage());
			}
		
		return json;
		
	}
	
	
}
