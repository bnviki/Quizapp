package com.medicine.vhquiz.activity;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.data.QuizManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Instruction extends Activity{
	String catCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		catCode = getIntent().getExtras().getString("catcode");
		if(catCode == null || catCode == ""){
			finish();
			return;
		}
		
		setContentView(R.layout.instructions);
		
		String instructions = "1. Select the best answer for every question.\n2. Unanswered questions"
				+ "are considered wrong\n3.Correct answer is shown after answering a question";
		
		TextView ins = (TextView) findViewById(R.id.instructionsText);
		ins.setText(instructions);
		getActionBar().setTitle("Instructions");
		
		Button start = (Button) findViewById(R.id.startQuiz);
		start.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent quizIntent = new Intent(Instruction.this, MainQuiz.class);
				quizIntent.putExtra("catcode", catCode);
	    		startActivity(quizIntent);	
	    		finish();
			}
		});
		
		new FetchQuestions().execute();
	}
	
	private class FetchQuestions extends AsyncTask<String, String, Boolean> {
		private ProgressDialog pDialog = new ProgressDialog(Instruction.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();			
			//pDialog = new ProgressDialog(Dashboard.this.getApplicationContext());			
			pDialog.setMessage("loading quiz...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			QuizManager.getInstance(getApplicationContext()).fetchQuestions(catCode);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean state) {
			pDialog.dismiss();	
			if(QuizManager.getInstance(getApplicationContext()).getQuestions().size() <= 0){
				Toast.makeText(Instruction.this, "No questions in this category", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}
}
