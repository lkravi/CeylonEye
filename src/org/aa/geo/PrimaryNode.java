package org.aa.geo;

public class PrimaryNode {

	private String postalcode;
	private String name;
	private String lat;
	private String lon;
	private String province;
	private String district;
	private String description;
	private String node_type;
	
	public PrimaryNode(String postalcode,String name,
			String lat,String lon,String province,String district,String description){
		this.postalcode=postalcode;
		this.name=name;
		this.lat=lat;
		this.lon=lon;
		this.district=district;
		this.province=province;
		this.description=description;
		this.node_type="primary_node";
	}
	
	public String getPostalcode(){
		return this.postalcode;
	}
	public String getName(){
		return this.name;
	}
	public String getlat(){
		return this.lat;
	}
	public String getLon(){
		return this.lon;
	}
	public String getDescription(){
		return this.description;
		
	}
	public String getProvince(){
		return this.province;
	}
	public String getDistrict(){
		return this.district;
	}
	public String getNodeType(){
		return node_type;
	}
}
