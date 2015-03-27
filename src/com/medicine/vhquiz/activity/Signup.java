package com.medicine.vhquiz.activity;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.data.UserManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends Activity {
	private EditText userNameView;
	private EditText passwordView;
	private EditText emailView;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.signup);
		
		userNameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);
		emailView = (EditText) findViewById(R.id.email);
		
		Button signupButton = (Button) findViewById(R.id.signup);		
		signupButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String username = userNameView.getText().toString().trim();
				String password = passwordView.getText().toString().trim();
				String email = emailView.getText().toString().trim();
				if(!username.equals("") && !password.equals("") && !email.equals(""))
					new SignupTask().execute(username, password, email);
				else 
					Toast.makeText(Signup.this, "Please provide all the details", Toast.LENGTH_SHORT).show();
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.cancel);		
		cancelButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public void startHomeScreen(){
		Intent homeScreenIntent = new Intent(Signup.this, HomeScreen.class);
		startActivity(homeScreenIntent);
		finish();
	}
	
	private class SignupTask extends AsyncTask<String, String, Boolean> {
 		private ProgressDialog pDialog = new ProgressDialog(Signup.this);

 		@Override
 		protected void onPreExecute() {
 			super.onPreExecute();			
 			//pDialog = new ProgressDialog(Dashboard.this.getApplicationContext());			
 			pDialog.setMessage("signing up ...");
 			pDialog.setIndeterminate(false);
 			pDialog.setCancelable(false);
 			pDialog.show();
 		}

 		@Override
 		protected Boolean doInBackground(String... params) { 			
 			return UserManager.getInstance(Signup.this).signupUser(params[0], params[1], params[2]);
 		}

 		@Override
 		protected void onPostExecute(Boolean state) {
 			pDialog.dismiss();		
 			if(state){
 				startHomeScreen(); 				
 			} else {
 				Toast.makeText(Signup.this, "Incorrect data", Toast.LENGTH_SHORT).show();
 			}
 		}
 	}
}
