package com.medicine.vhquiz.activity;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.adapter.CustomAdapter;
import com.medicine.vhquiz.data.UserManager;
import com.medicine.vhquiz.data.entity.User;

public class MainActivity extends ActionBarActivity {
	// SocialAuth Components
	private static SocialAuthAdapter adapter;

	// Android Components
	private ListView listview;	
	private String providerName;
	private EditText userNameView;
	private EditText passwordView;
	public CustomAdapter listAdapter;

	public static int pos;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		userNameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);
		
		Button signInButton = (Button) findViewById(R.id.signin);		
		signInButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String username = userNameView.getText().toString().trim();
				String password = passwordView.getText().toString().trim();
				if(!username.equals("") && !password.equals(""))
					new LoginUser().execute(username, password);
			}
		});	
		
		if(UserManager.getInstance(this).checkForUser() != null){
			Intent categoryIntent = new Intent(MainActivity.this, CategorySelection.class);
			startActivity(categoryIntent);
			finish();
		}

		adapter = new SocialAuthAdapter(new ResponseListener());
		
		listview = (ListView) findViewById(R.id.listview);
		listAdapter = new CustomAdapter(this, adapter);
		listview.setAdapter(listAdapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainActivity.pos = position;

				if (listAdapter.providers[position].equals(Provider.GOOGLEPLUS))
					adapter.addCallBack(Provider.GOOGLEPLUS, "http://www.eventfriendly.com/success");
				
				// This method will enable the selected provider
				adapter.authorize(MainActivity.this, listAdapter.providers[position]);
			}
		});	
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	public static void hideSoftKeyboard (Activity activity, View view) 
	{
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private final class ResponseListener implements DialogListener {

		@Override
		public void onComplete(Bundle values) {

			Log.d("Custom-UI", "Successful");

			// Changing Sign In Text to Sign Out
			View v = listview.getChildAt(pos - listview.getFirstVisiblePosition());
			TextView pText = (TextView) v.findViewById(R.id.signstatus);
			pText.setText("Sign Out");

			// Get the provider
			providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("Custom-UI", "providername = " + providerName);

			Toast.makeText(MainActivity.this, providerName + " connected", Toast.LENGTH_SHORT).show();
			adapter.getUserProfileAsync(new ProfileDataListener());
		}

		@Override
		public void onError(SocialAuthError error) {
			Log.d("Custom-UI", "Error");
			error.printStackTrace();
		}

		@Override
		public void onCancel() {
			Log.d("Custom-UI", "Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Custom-UI", "Dialog Closed by pressing Back Key");
		}
	}

	// To receive the profile response after authentication
	private final class ProfileDataListener implements SocialAuthListener<Profile> {

		@Override
		public void onExecute(String provider, Profile t) {
			Log.d("VHQuiz", "Receiving Data");			
			String picture = t.getProfileImageURL();
			picture = picture.replace("http:", "https:");
			
			User newUser = new User(t.getValidatedId(), t.getFullName(), t.getEmail(), picture, t.getProviderId());
			if(UserManager.getInstance(MainActivity.this).loginUser(newUser)){
				Intent categoryIntent = new Intent(MainActivity.this, CategorySelection.class);
				startActivity(categoryIntent);
				finish();
			}
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}
	
	private class LoginUser extends AsyncTask<String, String, Boolean> {
 		private ProgressDialog pDialog = new ProgressDialog(MainActivity.this);

 		@Override
 		protected void onPreExecute() {
 			super.onPreExecute();			
 			//pDialog = new ProgressDialog(Dashboard.this.getApplicationContext());			
 			pDialog.setMessage("loading ...");
 			pDialog.setIndeterminate(false);
 			pDialog.setCancelable(false);
 			pDialog.show();
 		}

 		@Override
 		protected Boolean doInBackground(String... params) { 			
 			return UserManager.getInstance(MainActivity.this).remoteLogin(params[0], params[1]);
 		}

 		@Override
 		protected void onPostExecute(Boolean state) {
 			pDialog.dismiss();		
 			if(state){
 				Intent categoryIntent = new Intent(MainActivity.this, CategorySelection.class);
				startActivity(categoryIntent);
				finish();
 			} else {
 				Toast.makeText(MainActivity.this, "Username and password incorrect", Toast.LENGTH_SHORT).show();
 			}
 		}
 	}
}
