package org.aa.geo;

import net.sf.json.JSONObject;

public class SecondaryNode {
private String name;
private String postalcode;
private String type;
private String lat;
private String lon;
private String address;
private String phone;
private String email;
private String image;
private String website;
private String description;
private boolean is_government;
private String node_type;


	public 	SecondaryNode(String name,String postalcode,String type,String lat,String lon,String address,
		String phone,String email,String image,String website,String desc,boolean is_government){
	
	this.name=name;
	this.postalcode=postalcode;
	this.type=type;
	this.lat=lat;
	this.lon=lon;
	this.address=address;
	this.phone=phone;
	this.email=email;
	this.image=image;
	this.website=website;
	this.description=desc;
	this.is_government=is_government;
	this.node_type="secondary_node";
		
	}
	public String getName(){
		return this.name;
	}
	public String getPostalCode(){
		return this.postalcode;
	}
	public String getType(){
		return this.type;
	}
	
	public String getLat(){
		return this.lat;
	}
	public String getLon(){
		return this.lon;
	}
	public String getAddress(){
		return this.address;
	}
	public String getphone(){
		return this.phone;
	}
	
	public String getEmail(){
		return this.email;
	}
	public String getImage(){
		return this.image;
	}
	public String getWebsite(){
		return this.website;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean IsGovernment(){		
		return this.is_government;
	}
	
	public String getNodeType(){
		return node_type;
	}
	
}
