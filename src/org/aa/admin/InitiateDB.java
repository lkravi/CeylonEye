package org.aa.admin;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;


public class InitiateDB {
	
	private final static String id = "id";
	
	private static   GraphDatabaseService db=null;
	private static  IndexService indexService=null;
	private final static String Province = "province";
	private final static String District = "district";	
	private final static String id_for_secondarynodes="id_for_secondarynodes";
	private final static String id_for_usernodes="id_for_usernodes";
	
	
	public void shutdown(){
		indexService.shutdown();
		db.shutdown();
	}
	public InitiateDB() {

		if(db==null){
				Connect c=new Connect("InitiateDB");
				db=c.getConnection();
		
				if(indexService==null){				
					indexService=c.getIndexService();
				}
		}
	}
	public InitiateDB(GraphDatabaseService db,IndexService indexService) {
		if(db==null){
			InitiateDB.db=db;
		}
		if(indexService==null){
			this.indexService=indexService;
		}
	}
	public static void main(String[] args) {
		
		AdminApp a=new AdminApp();
		new InitiateDB();
		System.out.println("creating social node");
		
		if (!(a.validateType(id, "social"))) {
			Transaction  tx1 = db.beginTx();
			try {
				
				
				
				Node Social = a.createAndIndexNode(id,"social");
				db.getReferenceNode().createRelationshipTo(Social, admin_rel.SOCIAL);
				
				tx1.success();
			} 
			catch(Exception e){
				System.out.println("error ="+e.getMessage());
			}
			finally {
				tx1.finish();
			}
		}
		//System.out.println("creating logged users node");
		// To create Logged users node and connect it to Reference Node
		// ..................................................

		/*if (!(a.validateType(id, "logged_users"))) {
			Transaction tx5 =db.beginTx();
			try {

				Node logged_users_root = a.createAndIndexNode(id, "logged_users");
				db.getReferenceNode().createRelationshipTo(logged_users_root,admin_rel.LOGGED_USERS);
				tx5.success();
			} finally {
				tx5.finish();
			}
		}
		*/
		
		System.out.println("creating geo node");
		// .......................................................
		// To create Geo Node

		if (!(a.validateType(id, "geo"))) {
			Transaction tx5 = db.beginTx();
			try {

				Node geo = a.createAndIndexNode(id, "geo");
				db.getReferenceNode().createRelationshipTo(geo, admin_rel.GEO);
				tx5.success();
			} finally {
				tx5.finish();
			}
		}

		System.out.println("creating provinces");
		// ........................................
		// To create Provinces

		String provinces[] = { "central", "eastern", "north central",
				"northern", "north western", "sabaragamuwa", "southern", "uva",
				"western" };
		Node Parent = a.indexToNode(id, "geo");
		for (int i = 0; i < provinces.length; i++) {

			if (!(a.validateType(Province, provinces[i]))) {
				Transaction tx5 = db.beginTx();
				try {

					Node province = a.createAndIndexNode(Province, provinces[i]);
					Parent.createRelationshipTo(province, admin_rel.PROVINCE);
					System.out.println(provinces[i]+" craeted");
					tx5.success();
				} finally {
					tx5.finish();
				}
			}
			
			// ..........................................
		}
		
		System.out.println("creating districs.......................");
		// To create Districs

		System.out.println("creating districts");
		String districs[][] = {
				{ "kandy", "matale", "nuwara eliya" },
				{ "ampara", "batticalo", "trincomalee" },
				{ "anuradhapura", "polonnaruwa" },
				{ "jaffna", "kilinochchi", "mannar", "mullaitivu", "vavuniya" },
				{ "puttalam", "kurunegala" }, { "ratnapura", "kegalle" },
				{ "matara", "galle", "hambantota" },
				{ "moneragala", "badulla" },
				{ "colombo", "gampaha", "kalutara" } };

		for (int a1 = 0; a1 < districs.length; a1++) {

			Node pro = a.indexToNode(Province, provinces[a1]);
			//System.out.println("-->>>>>loop =" + a1);
			for (int b1 = 0; b1 < districs[a1].length; b1++) {
				if (!(a.validateType(District, districs[a1][b1]))) {
					Transaction tx5 = db.beginTx();
					try {
												 
						Node district = a.createAndIndexNode(District,districs[a1][b1]);						
						pro.createRelationshipTo(district, admin_rel.DISTRICT);
						tx5.success();
						System.out.println(districs[a1][b1]+" created");
						
					} finally {
						tx5.finish();
					}
				}
			}
		}
				
		

		if (!(a.validateType(id, "id_for_secondarynodes"))) {
			Transaction  tx4 = db.beginTx();
			try {				
				
				Node node = a.createAndIndexNode(id,id_for_secondarynodes);	
				node.setProperty("val", "100");
				
				tx4.success();
			} 
			catch(Exception e){
				System.out.println("error ="+e.getMessage());
			}
			finally {
				tx4.finish();
			}
		}
		
		if (!(a.validateType(id, "id_for_usernodes"))) {
			Transaction  tx5 = db.beginTx();
			try {				
				
				Node node = a.createAndIndexNode(id,id_for_usernodes);	
				node.setProperty("val", "100");
				
				tx5.success();
			} 
			catch(Exception e){
				System.out.println("error ="+e.getMessage());
			}
			finally {
				tx5.finish();
			}
		}
		
		a.shutdown("nad1");
		//b.shutdown("nad2");
	}
	
	public void shutdown(String s){
		System.out.println("shut down by"+s);
		indexService.shutdown();
		db.shutdown();
	}

}
