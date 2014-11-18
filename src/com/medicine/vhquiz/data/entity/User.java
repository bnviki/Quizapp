package com.medicine.vhquiz.data.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	public String displayName;			
	public String email;
	public String picture;
	public String id;
	public String provider;
	
	public User(String id, String displayname, String email, String picture, String provider){
		this.id = id;
		this.displayName = displayname;		
		this.picture = picture;				
		this.email = email;	
		this.provider = provider;
	}
	
	public User(String jsonValue){
		try {
			JSONObject jObject = new JSONObject(jsonValue);			
			picture = getField("picture", jObject);
			id = getField("_id", jObject);				
			displayName = getField("displayname", jObject);
			email = getField("email", jObject);										
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getField(String name, JSONObject obj){
		try{
			return obj.getString(name);			
		} catch (Exception e){
			return "";
		}
	}
	
	public User(JSONObject jObject){
		try{			
			id = getField("_id", jObject);			
			displayName = getField("displayname", jObject);
			picture = getField("picture", jObject);
			email = getField("email", jObject);		
		} catch (Exception e) {		
			e.printStackTrace();
		}
	}
	
	public String toJSON(boolean removeId){
		JSONObject jObj = new JSONObject();
		
		try {
			if(!removeId)
				jObj.put("_id", id);		
			
			jObj.put("displayname", displayName);
			jObj.put("email", email);			
			jObj.put("picture", picture);			
			return jObj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
