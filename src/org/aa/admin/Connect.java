package org.aa.admin;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.kernel.EmbeddedGraphDatabase;


public class Connect {
	private  static String DB_PATH="c:/DB2/neo4j-Db4";
	private static GraphDatabaseService db=new EmbeddedGraphDatabase(DB_PATH);;
	private static IndexService   indexService=new LuceneIndexService(db);
	private static int count=0;
	
	
	
	
	public Connect(String x) { 
		   count++;
		// if(count==1){
	       registerShutdownHook(db);
	       //System.out.println("db created=="+x);
		// }
	       
	}
	public IndexService getIndexService(){
		return indexService;
	}
	
	public GraphDatabaseService getConnection(){
		
		return db;
	}
	
	
	 public void shutdown(String x){
		    indexService.shutdown();
		    db.shutdown();
		   // System.out.println("db shutdowned by"+x);
	 }
	 
	 
	private  void registerShutdownHook(final GraphDatabaseService db){
	    Runtime.getRuntime().addShutdownHook(new Thread()
	    {
	    @Override
	    public void run(){
	    	shutdown("matchene");
	    } });
	    }
	
	//public static void main(String[] args) {
		//DBConnect c=new DBConnect();
		//c.shutdown();
		
		//DBConnect c2=new DBConnect();
		//c2.shutdown();
		
	//}
	
	/*public static void main(String[] args) {
		DBConnect a=new DBConnect();
		GraphDatabaseService x = a.getConnection();
		Transaction tx=x.beginTx();
		try{
			A a1=new A();
			//a1.test();
			
			Node n3=x.createNode();
			n3.setProperty("id", "100");
			Node n4=x.createNode();
			n3.createRelationshipTo(n4, myrel.KNOWS);
			n4.setProperty("id", "200");
			tx.success();
		}
		catch(Exception e){
			
		}
		finally{
			tx.finish();
			x.shutdown();
		}
		
	}*/
	
	
}
