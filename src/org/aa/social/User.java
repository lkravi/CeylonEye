package org.aa.social;

public class User {

	
	private String email;
	private String password;   
	private String name;
	private String address;
	private String phone;
	private String web;
	private String home_Town;  //postal code of home_town
	private String living_Town;  //postal code of living town
	private String current_lat;
	private String current_lon;
	private String profile_picture;
	private String work_in;   //this is an ID of a secondarynode where the user works in.eg:-100
	private String discription;

	
	public User(String email,String pw,String name,String add,
			String phone,String web,String hometown,String LivingTown,
			String Current_lat,String Current_lon,String Profile_picture,
			String Work_in,String Discription
			){
		
		this.email=email;
		this.password=pw;
		this.name=name;
		this.address=add;
		this.phone=phone;
		this.web=web;
		this.home_Town=hometown;
		this.living_Town=LivingTown;
		this.current_lat=Current_lat;
		this.current_lon=Current_lon;
		this.profile_picture=Profile_picture;
		this.work_in=Work_in;
		this.discription=Discription;
		
	
	}
	
	public User(String email,String pw){
		
		this(email,pw,"","","","","","","","","","","");
	}
	
	public String getEmail(){
		return this.email;
	}
	public String getPassWord(){
		return this.password;
	}
	public String getName(){
		return this.name;
	}
	public String getAddress(){
		return this.address;
	}
	public String getPhone(){
		return this.phone;
	}
	public String getWeb(){
		return this.web;
	}
	
	public String getHomeTown(){
		return this.home_Town;
	}
	public String getLivingTown(){
		return this.living_Town;
		
	}
	public String getCurrentLat() {
		return this.current_lat;
		
	}
	public String getCurrentLon(){
		return this.current_lon;
	}
	public String getProfilePic(){
		
		return this.profile_picture;
	}
	public String getWorksIn(){
		return this.work_in;
	}
	public String getDescription(){
		return this.discription;
	}
}
