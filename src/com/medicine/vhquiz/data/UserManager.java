package com.medicine.vhquiz.data;

import org.brickred.socialauth.android.SocialAuthAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.medicine.vhquiz.data.entity.User;
import com.medicine.vhquiz.utils.RestClient;
import com.medicine.vhquiz.utils.RestResponse;
import com.medicine.vhquiz.utils.RestClient.RequestMethod;

public class UserManager {
	private User currentUser;
	private static UserManager instance;
	private Context context;
	public static SharedPreferences prefs;
	private static final String PREFERENCE_FILE_NAME = "userdetails";	
	private static SocialAuthAdapter adapter;
	public RestClient restClient;
	
	public static UserManager getInstance(Context context) {
		if(instance == null)
			instance = new UserManager(context.getApplicationContext());
		return instance;
	}

	private UserManager(Context context){
		this.context = context;		
		prefs = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		restClient = RestClient.getInstance();
	}
	
	public void setAdapter(SocialAuthAdapter adapter){
		this.adapter = adapter;
	}
	
	public SocialAuthAdapter getAdapter(){
		return adapter;
	}
	
	public boolean remoteLogin(String username, String password){
		String url = "/quiz/restserver/index.php/medquiz/login/user/" + username + "/pass/" + password;
		String encodedUri = Uri.parse(url).toString();
		RestResponse res = restClient.execute(encodedUri, RequestMethod.GET, null, null);    	

		if(res.responseCode == 200){	
			return true;
		} 
		return false;
	}
	
	public boolean loginUser(User user){
		currentUser = user;
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("provider", user.provider);
		editor.putString("displayname", user.displayName);
		editor.putString("email", user.email);
		editor.putString("picture", user.picture);
		editor.putString("id", user.id);
		editor.commit();
		return true;
	}
	
	public User checkForUser(){
		String userId = prefs.getString("id", "");
		if(userId == "")
			return null;
		else {
			User user = new User(userId, prefs.getString("displayname", ""), prefs.getString("email", ""),
					prefs.getString("picture", ""), prefs.getString("provider", ""));
			currentUser = user;
			return user;
		}
			
	}
	
	public User getCurrentUser(){
		if(currentUser == null){
			checkForUser();
		}
		return currentUser;
	}
	
	public boolean logoutUser(){
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove("provider");
		editor.remove("displayname");
		editor.remove("email");
		editor.remove("picture");
		editor.remove("id");
		editor.commit();
		currentUser = null;
		return true;
	}
	
}
