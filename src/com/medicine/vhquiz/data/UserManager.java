package com.medicine.vhquiz.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
			User newUser = new User(res.response);
			newUser.picture = "dummy";
			return loginUser(newUser);
		} 
		return false;
	}
	
	public boolean signupUser(String username, String password, String email){
		String url = "";
		url = "/quiz/restserver/index.php/medquiz/signup/username/" + username + "/password/" + password + "/email/" + email;		
		RestResponse res = restClient.execute(url, RequestMethod.GET, null, null);    	

		if(res.responseCode == 200){	
			User newUser = new User("", username, email, "dummy", "");
			return loginUser(newUser);
		} 
		return false;
	}
	
	public boolean sendFeedback(String msg){
		User current = getCurrentUser(); 
		if(current == null)
			return false;		
		String url = "";
		try {
			url = "/quiz/restserver/index.php/medquiz/feedback/username/" + URLEncoder.encode(current.displayName, "utf-8") + "/email/" + URLEncoder.encode(current.email, "utf-8") + "/message/" + URLEncoder.encode(msg, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		RestResponse res = restClient.execute(url, RequestMethod.GET, null, null);    	

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
