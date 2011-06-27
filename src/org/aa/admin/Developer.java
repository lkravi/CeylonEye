package org.aa.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("/developer")
public class Developer {
	private String Key="key";
	String dbtime;
	//=============01) Method adminAuth()=========================================================
	//User name and Password must be in base64 encoded format
	@POST
	@Path("admin/auth/{username}/{password}")
	@Produces("application/json")
	public JSONObject adminAuth(
							@PathParam("username") String username,
							@PathParam("password") String password,
							@Context HttpServletRequest req)
	{
		
		AdminOperations op1=new AdminOperations();
		JSONObject result=new JSONObject();
		String un="",pw="",query,pass="",token;
		
		
		if(org.apache.catalina.util.Base64.isBase64(username)&&org.apache.catalina.util.Base64.isBase64(password))
		{
			un=op1.getDecodedData(username);
			pw=op1.getDecodedData(password);
			
			query="select password from admin where username='"+un+"'";
			
			try {
				JDBCMySql mysql=new JDBCMySql();
				
				
				Connection con=mysql.getCon();	
				
				Statement stmt = con.createStatement();
				
				ResultSet rs = stmt.executeQuery(query);
				
				while (rs.next()) {
				pass = rs.getString(1);
				} 
				
				
				if(pass.isEmpty())
				{
					result.put("status", "fail");
					result.put("code", "e100");
					result.put("desc", "Incorect username");
					con.close();
					return result;
				
				}
				
				if(pw.equals(pass))
				{
					if(mysql.validateAdminSession(un)){
						result.put("status", "fail");
						result.put("desc", "you need to close the previous session.");						
						return result;
					}
					token=op1.getRandomNumber();				
					HttpSession session=req.getSession();					
					session.setAttribute(Key, token);	
					
					result.put("status", "success");
					result.put("token", token);
					result.put("desc", "Admin Authenticated sucsessfully");
					
					String sql="insert into Adminsession values('"+un+"','"+pw+"','"+token+"')";
					mysql.storeSession(sql);
					
					con.close();
					return result;
				}
				else
				{
					result.put("status", "fail");
					result.put("code","e101");
					result.put("desc", "Incorect password");
					con.close();
					return result;
				}

				} //end try

				

				catch(Exception e) {
					result.put("status", "fail");
					result.put("code", "e103");
					result.put("desc", "sql exception");
					e.printStackTrace();
				return result;
				}
			
			
		}
		else
		{
			result.put("status", "fail");
			result.put("code", "e102");
			result.put("desc", "user name or password is not in Base64 format");
			return result;
		}
	
		
		
	}
	//=====================EOF adminAuth()=============================================================
	
	@POST
	@Path("admin/clearSession/{username}/{password}")
	@Produces("application/json")
	public JSONObject clearSession(
							@PathParam("username") String username,
							@PathParam("password") String password,
							@Context HttpServletRequest req)
	{
		
		AdminOperations op1=new AdminOperations();
		JSONObject result=new JSONObject();
		String un="",pw="",query,pass="",token;
		
		
		if(org.apache.catalina.util.Base64.isBase64(username)&&org.apache.catalina.util.Base64.isBase64(password))
		{
			un=op1.getDecodedData(username);
			pw=op1.getDecodedData(password);
			
			query="select * from adminsession where username='"+un+"' and password='"+pw+"'";
			
			try {
				JDBCMySql mysql=new JDBCMySql();				
				
			ResultSet rs=mysql.queryToDB(query);
				
				if(rs.next()){
					String sql="delete from adminsession where username='"+un+"' and password='"+pw+"'";
					mysql.updateDB(sql);
					result.put("status", "success");					
					result.put("desc", "Admin session cleared sucsessfully");
					
				}
				else{
					result.put("status", "fail");					
					result.put("desc", "there's no Admin session for this credentials");
					
				}
			}
			catch(Exception e){
				result.put("status", "fail");
				result.put("code", "e103");
				result.put("desc", "sql exception");
				e.printStackTrace();
			return result;
			}
		}
			else
			{
				result.put("status", "fail");
				result.put("code", "e102");
				result.put("desc", "user name or password is not in Base64 format");
				return result;
			}
		
		
		return result;
			}
	//=====================02) AddplaceType()==========================================================
	@POST
	@Path("addPlaceType/{type}/{adminToken}")
	@Produces("application/json")
	public JSONObject addPlaceType(
			@PathParam("type") String type,
			@PathParam("adminToken") String adminToken,
			@Context HttpServletRequest req)
	{
		JSONObject result=new JSONObject();
		String query;
		int rs;
		AdminOperations op1=new AdminOperations();
		
		
		if(!op1.isValidUserSession(req,adminToken)){
			result.put("status", "fail");
			result.put("desc", "Invalid Session");			
			return result;			
		}
		else
		{
			query="INSERT INTO secondaryTypes(type) VALUES('"+type+"')";
			
			try {
					
				JDBCMySql mysql=new JDBCMySql();
				Connection con = mysql.getCon();
				Statement stmt = con.createStatement();
				rs=stmt.executeUpdate(query);
				
				System.out.println(rs);
				if(rs==1)
				{
					result.put("status", "success");
					result.put("desc", "Type added succesfully");
					return result;
				}
				else
				{
					result.put("status", "fail");
					result.put("desc", "MYSQL execution error");
					return result;
				}
				
				

				} //end try

				catch(Exception e) {
					result.put("status", "fail");
					result.put("desc", "MYSQL execution error");
				}

				
		}
		
		return result;
	}//=====================EOF AddplaceType()==========================================================	
	
	
	//=====================03) getAllPlaceTypes()======================================================
	@GET
	@Path("getAllPlaceTypes")
	@Produces("application/json")
	public JSONObject getAllPlaceTypes()
	{
		String query = "Select * FROM secondarytypes";
		JSONArray json=new JSONArray();
		JSONObject result=new JSONObject();
		
		try {

			JDBCMySql mysql=new JDBCMySql();
			Connection con = mysql.getCon();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
			dbtime = rs.getString(2);
			result.put(rs.getString(1), rs.getString(2));
			
			
			//System.out.println(dbtime);
			} 
			
			//end while

			con.close();
			} //end try

			catch(Exception e) {
				result.put("status", "fail");
				result.put("desc", "MYSQL execution error");
				
			}

			return result;
		
		
	}//=====================EOF getAllPlaceTypes()======================================================

	
}
