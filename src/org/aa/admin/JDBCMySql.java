/*
 * THIS Class contain following methods
 * 
 * 01) public JSONObject adminAuth() : this will authenticate administrators and issue token for current session
 * 02) public JSONObject addPlaceType() : this will add place types for system this method need administrator privileges
 * 03) public JSONArray getAllPlaceTypes() : this will return all place types in the system (secondary node types)
 * 
 */

package org.aa.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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



//=============Root path for all functionalities in this class====================================

public class JDBCMySql {
	private String Key="key";
	
	//=============MY SQL DATABASE Configuration==================================================
	/*=======
	 * Warning ! : Check this settings and verify before run tomcat Server !
	 * 
	 */
	
	String dbUrl = "jdbc:mysql://localhost/CeylonEye"; //database url string
	String dbClass = "com.mysql.jdbc.Driver";
	String dbusername = "root"; //database access user name
	String dbpassword = ""; //database access password
	Connection con;
	
	//=============EOF MYSQL Configuration========================================================
	
	public Connection getCon(){
		return this.con;
		
	}
	public void closeCon(){
		try {
			this.con.close();
		} catch (SQLException e) {
			
		}
	}
	public JDBCMySql(){
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 this.con = DriverManager.getConnection (dbUrl,dbusername,dbpassword);
			
		} catch (Exception e) {
			
		}
		
	}
	/*public static void main(String[] args) {
		JDBCMySql c=new JDBCMySql();
		String sql="insert into Adminsession values('aa','bb','cc')";
		c.storeSession(sql);
	}*/
	public boolean storeSession(String sql){
		boolean result=false;
		try {
			Statement stm=con.createStatement();
			int x=stm.executeUpdate(sql);
			if(x>0){
				result=true;
			}
			
		} catch (SQLException e) {
			
		}
		return result;
	}
	//*************
	public ResultSet queryToDB(String sql){
		
		try {
			Statement stm2=con.createStatement();
		ResultSet r = stm2.executeQuery(sql);
		return r;
			
		} catch (SQLException e) {
			
		}
		return null;
	}
	
	//+++++
	
public int updateDB(String sql){
	int x=0;
		try {
			Statement stm2=con.createStatement();
		 x=stm2.executeUpdate(sql);
		
			
		} catch (SQLException e) {
			
		}
		return x;
	}
	
	public boolean validateAdminSession(String username){
		boolean result=false;
		
		String sql="select * from adminsession where username='"+username+"'";
		ResultSet r=queryToDB(sql);
		
		try {
			if(r.next()){
				result=true;
			}
		} catch (SQLException e) {
			
		}
		return result;
	}
	
	public void clearAdminSession(String un,String pw){
		
	}
	
}//EOF JDBCMySql class
