package org.aa.social;

import net.sf.json.JSONObject;

import org.aa.admin.Connect;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;

public class SocialApp {
	private final String id = "id";
	
	private static GraphDatabaseService db=null;
	private static  IndexService indexService=null;	
	private final static String id_for_usernodes="id_for_usernodes";
	private final static String uid="uid";
	private final String snid="snid";
	
	public SocialApp() {

		if(db==null){
			Connect c=new Connect("social App");
			db=c.getConnection();
		
			if(indexService==null){
				
				indexService=c.getIndexService();
			}
			}
		}
		public SocialApp(GraphDatabaseService db,IndexService indexService) {
			if(db==null){
				this.db=db;
			}
			if(indexService==null){
				this.indexService=indexService;
			}
		}
		public Node createAndIndexNode(String index_name, final String index_value) {
			
			System.out.println("index_value="+index_value);
			if(db==null){
				System.out.println("empty db");
			}
			Node node = db.createNode();
			try{
						
			indexService.index(node, index_name, index_value);
			}
			catch(Exception e){
				System.out.println("exception in createAndIndexNode function");
			}
			
			return node;
		}
			public Node indexToNode(String index_name, final String index_value){
		
		
			Node user = null;
			try{
				user = indexService.getSingleNode(index_name, index_value);		
			}
			catch(Exception e){
				System.out.println("exception in indexToNode function");
			}		
			
			return user;

		}
		public boolean validateType(String index_name, final String index_value) {

			if (indexToNode(index_name, index_value) == null) {
				return false;
			}
			return true;
		}
		
		public void shutdown(String s){
			System.out.println("shutdown ->"+s);
			indexService.shutdown();
			db.shutdown();
		}

		public Relationship createRelationshipTo(Node source,Node destination,RelationshipType type){
			
			Transaction tx1=db.beginTx();
			Relationship r = null;
			try{
				
				 r=source.createRelationshipTo(destination,type);
				
			}
			catch(Exception e){
				
				tx1.success();
			}
			finally{
				tx1.finish();
				
			}
			return r;
		}
		
		//***************************************************************************
		
		public JSONObject addUser(final User usr) {
			System.out.println("start creating a user");
			JSONObject json = new JSONObject();			
		
			Transaction tx2=db.beginTx();		
			Node n2;
			try {
				 n2=indexToNode("id",id_for_usernodes);
				int val = 0;
				if(n2!=null){
					 val=Integer.parseInt(n2.getProperty("val").toString());
					 val++;
					 System.out.println("x=="+val);
					
				}
				else{
					json.put("status", "fail");
					json.put("desc", "Id generation error");
					return json;
				}
				
				if (!validateType(id, usr.getEmail())) {
					System.out.println("start ..........");
					
					
					Node user = createAndIndexNode(id,usr.getEmail());
					n2.setProperty("val", Integer.toString(val)); //store the current issued number in the node
					
					indexService.index(user, uid, val);// creating an index on uid also.there for we cans search on both email and uid.easily.
					//we create two indexes with users.that is for email and uid.
					
					user.setProperty(uid, Integer.toString(val));	
					
					
					System.out.println("1111");
					
					user.setProperty(uid, Integer.toString(val));
					user.setProperty("email", usr.getEmail());					
					user.setProperty("password", usr.getPassWord());				
					user.setProperty("name", usr.getName());
					user.setProperty("address", usr.getAddress());
					user.setProperty("phone", usr.getPhone());
					user.setProperty("web", usr.getWeb());
					
					
					System.out.println("2222");
					
					user.setProperty("home_Town", usr.getHomeTown());
					user.setProperty("living_Town", usr.getLivingTown());
					
					System.out.println("x111");
					
					user.setProperty("current_lat", usr.getCurrentLat());
					user.setProperty("current_lon", usr.getCurrentLon());
					
					user.setProperty("profile_picture", usr.getProfilePic());
					
					System.out.println("x2222");
					
					user.setProperty("work_in", usr.getWorksIn());
					System.out.println("xxx");
					user.setProperty("discription", usr.getDescription());
					
					System.out.println("333");
					
					Node parent = indexToNode(id,"social");
					SocialOperations op1=new SocialOperations();
					
					json=op1.convertUserNodetoJSON(user);					
					
					setGeoLocations(user,usr.getHomeTown(),usr.getLivingTown(),usr.getWorksIn());
					
					parent.createRelationshipTo(user, social_rel.USER);					
					System.out.println("4444");

					json.put("status", "success");
					json.put("desc", "user created sucessfully");
					System.out.println("user created");
					tx2.success();
				}

					else {
					json.put("status", "fail");
					json.put("desc", "user already exist");
					}
				} 
				catch (Exception e) {

				json.put("status", "fail");
				json.put("desc", "exception occurs while creating a user");

				}
				finally{
					tx2.finish();
				}

			return json;
		
		
	
}
public void setGeoLocations(Node user,String postalcode_hometown,String postalcode_living_town,String id_of_works_in){


			if(!postalcode_hometown.isEmpty()){
				Transaction tx=db.beginTx();
				try{
					if(validateType(id, postalcode_hometown)){
						Node homeTown=indexToNode(id, postalcode_hometown);
						createRelationshipTo(user, homeTown, social_rel.HOME_TOWN);
						
					}
				}
				finally{
					tx.finish();
				}
			}
			//>>>>>>
			if(!postalcode_living_town.isEmpty()){
				Transaction tx=db.beginTx();
				try{
					if(validateType(id, postalcode_hometown)){
						Node living_town=indexToNode(id, postalcode_living_town);
						createRelationshipTo(user, living_town, social_rel.LIVES_IN);
						
					}
				}
				finally{
					tx.finish();
				}
			}
			
			//>>>>>>
			if(!id_of_works_in.isEmpty()){
				Transaction tx=db.beginTx();
				try{
					if(validateType(snid, id_of_works_in)){
						Node work_place=indexToNode(id, postalcode_living_town);
						createRelationshipTo(user, work_place, social_rel.WORKS_IN);
						
					}
				}
				finally{
					tx.finish();
				}
			}
			
		}
}

		

