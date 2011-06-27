package org.aa.geo;

import net.sf.json.JSONObject;

import org.aa.admin.Connect;
import org.aa.admin.admin_rel;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.index.IndexService;





public class GeoApp {
	private final String id = "id";
	private final String Province = "province";
	private final String District = "district";
	private final String snid="snid";
	
	private final static String id_for_secondarynodes="id_for_secondarynodes";
	
	private static GraphDatabaseService db=null;
	private static  IndexService indexService=null;	
	
	
	
	public GeoApp() {

	if(db==null){
		Connect c=new Connect("GeoApp");
		db=c.getConnection();
	
		if(indexService==null){
			
			indexService=c.getIndexService();
		}
		}
	}
	public GeoApp(GraphDatabaseService db,IndexService indexService) {
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

	
	//***************************************************************************
	
	public JSONObject addPrimaryNode(PrimaryNode t) {
		System.out.println("start creating a a primary node");
		JSONObject json = new JSONObject();
		
		Transaction tx=db.beginTx();		
		System.out.println("test1111");
		try {
			Node primarynode = createAndIndexNode(id,t.getPostalcode());
			//System.out.println("xxxxxxxxxxxxxxhere");
			primarynode.setProperty("postalcode", t.getPostalcode());
			primarynode.setProperty("name", t.getName());
			primarynode.setProperty("lat", t.getlat());
			primarynode.setProperty("lon", t.getLon());
			primarynode.setProperty("province", t.getProvince());
			primarynode.setProperty("district", t.getDistrict());
			primarynode.setProperty("description", t.getDescription());
			primarynode.setProperty("node_type", t.getNodeType());
			
			Node parent = indexToNode(District, t.getDistrict());
			if(parent!=null){
					parent.createRelationshipTo(primarynode, geo_rel.PRIMARY_NODE);
			}
			else{
				json.put("status", "fail");
				json.put("desc", "Cannot find the parent node of this Primary Node");
			}
			GeoOperations op =new GeoOperations();
			json=op.convertPrimaryNodetoJSON(primarynode);
			
			/*if(json.get("status").equals("fail")){
				return json;
			}*/
			
			json.put("status", "success");
			json.put("desc", "Town created sucessfully");
			tx.success();
		} 
		catch (Exception e) {

			json.put("status", "fail");
			json.put("desc", "exception occurs while creating a user"+e.getMessage());

		}
		finally{
			tx.finish();
		}

		return json;
	}
	
	public JSONObject addSecondaryNode(SecondaryNode n) {
		System.out.println("start creating a secondary node");
		JSONObject json = new JSONObject();
		
		Transaction tx2=db.beginTx();		
		Node n2;
		try {
			 n2=indexToNode("id",id_for_secondarynodes);
			int val = 0;
			if(n2!=null){
				 val=Integer.parseInt(n2.getProperty("val").toString());
				 val++;
				
			}
			else{
				json.put("status", "fail");
				json.put("desc", "Id generation error");
				return json;
			}
			
			
			Node town = createAndIndexNode(snid,Integer.toString(val));
			n2.setProperty("val", Integer.toString(val)); //store the current issued number in the node
			town.setProperty(snid, Integer.toString(val));
			
			town.setProperty("name", n.getName());
			town.setProperty("postalcode", n.getPostalCode());
			town.setProperty("type", n.getType());
			town.setProperty("lat", n.getLat());
			town.setProperty("lon", n.getLon());

			//System.out.println("s111");

			town.setProperty("address", n.getAddress());
			town.setProperty("phone", n.getphone());
			town.setProperty("email", n.getEmail());
			town.setProperty("image", n.getImage());

			//System.out.println("s22");

			town.setProperty("website", n.getWebsite());
			town.setProperty("description", n.getDescription());
			town.setProperty("is_government", n.IsGovernment());
			town.setProperty("node_type", n.getNodeType());

			//System.out.println("s333");

			Node parent = indexToNode(id, n.getPostalCode());
			//System.out.println("s4444");
			parent.createRelationshipTo(town, geo_rel.SECONDARY_NODE);
			//System.out.println("s55");
			System.out.println(town + "secondary node created");
			//System.out.println("1111");

			GeoOperations op1 = new GeoOperations();

			//System.out.println("2222");
			json = op1.convertSecondaryNodetoJSON(town);
			
			
			//System.out.println("3333");

			json.put("status", "success");
			json.put("desc", "Secondary Node added sucessfully");

			//System.out.println("4444");
			tx2.success();

		} catch (Exception e) {
			//System.out.println("55");
			json.put("status", "fail");
			json.put("desc", "exception occurs while creating a Secondary Node:"+e.getMessage());

		}
		finally{
			tx2.finish();
		}

		return json;

	}
	public Node getTownByName(String name) {
	
		
		Transaction tx =db.beginTx();
		
		Node result2 = null;
		Node ss;
		try {
			ss = indexToNode(id, "geo");
			//System.out.println("x22222");

			//System.out.println("1111");
			Traverser traverser1 = ss.traverse(Order.BREADTH_FIRST,
					StopEvaluator.END_OF_GRAPH,
					ReturnableEvaluator.ALL_BUT_START_NODE, admin_rel.PROVINCE,
					Direction.OUTGOING);

			//System.out.println("x33333");
			outerloop: for (Node f : traverser1) {
				Traverser traverser2 = f.traverse(Order.BREADTH_FIRST,
						StopEvaluator.END_OF_GRAPH,
						ReturnableEvaluator.ALL_BUT_START_NODE, admin_rel.DISTRICT,
						Direction.OUTGOING);

				for (Node f2 : traverser2) {
					Traverser traverser3 = f2.traverse(Order.BREADTH_FIRST,
							StopEvaluator.END_OF_GRAPH,
							ReturnableEvaluator.ALL_BUT_START_NODE,
							geo_rel.PRIMARY_NODE, Direction.OUTGOING);
					for (Node f3 : traverser3) {
						if (f3.getProperty("name").equals(name)) {

							//System.out.println("here="+f3);
							//System.out.println("xxxs="+f3.getProperty("postalcode"));
							result2 = f3;
							break outerloop;
						}
					}
				}

			}
			tx.success();

		} finally {
			tx.finish();
			

		}
	
		return result2;

	}

	
	
	
}
