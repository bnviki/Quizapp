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

public class Feedback extends Activity{
	private EditText feedbackText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.feedback);
		
		feedbackText = (EditText) findViewById(R.id.feedbackText);
		
		Button statsButton = (Button) findViewById(R.id.sendBtn);		
		statsButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String feedback = feedbackText.getText().toString().trim();
				new SignupTask().execute(feedback);				
			}
		});	

	}
	
	private class SignupTask extends AsyncTask<String, String, Boolean> {
 		private ProgressDialog pDialog = new ProgressDialog(Feedback.this);

 		@Override
 		protected void onPreExecute() {
 			super.onPreExecute();			
 			//pDialog = new ProgressDialog(Dashboard.this.getApplicationContext());			
 			pDialog.setMessage("sending...");
 			pDialog.setIndeterminate(false);
 			pDialog.setCancelable(false);
 			pDialog.show();
 		}

 		@Override
 		protected Boolean doInBackground(String... params) { 			
 			return UserManager.getInstance(Feedback.this).sendFeedback((params[0]));
 		}

 		@Override
 		protected void onPostExecute(Boolean state) {
 			pDialog.dismiss();		
 			if(state){
 				finish();
 			} else {
 				Toast.makeText(Feedback.this, "Error sending feedback", Toast.LENGTH_SHORT).show();
 			}
 		}
 	}

}
