package org.aa.admin;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;

 
public class AdminApp {
	
	
	
	private  GraphDatabaseService db=null;
	private  IndexService indexService=null;
	
	
	public void shutdown(String s){
		System.out.println("shut down by"+s);
		indexService.shutdown();
		db.shutdown();
	}
	
	public AdminApp() {

	if(db==null){
		Connect c=new Connect("AdminApp");
		db=c.getConnection();
	
		if(indexService==null){
			
			indexService=c.getIndexService();
		}
	}
	}
	public AdminApp(GraphDatabaseService db,IndexService indexService) {
		if(db==null){
			this.db=db;
		}
		if(indexService==null){
			this.indexService=indexService;
		}
	}
		

	public Node createAndIndexNode(String index_name, final String index_value) {
	
		
		Node node = null;
		try{
		 node = db.createNode();		
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

	
}
