package com.medicine.vhquiz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.medicine.vhquiz.R;

public class Settings extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		SharedPreferences prefs = getSharedPreferences("medsplash", Context.MODE_PRIVATE);
		
		RadioGroup quizMode = (RadioGroup) findViewById(R.id.radioGroup1);
		if(prefs.getBoolean("showAnsAtEnd", false))
			quizMode.check(R.id.radio1);
		else
			quizMode.check(R.id.radio0);
		
		quizMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {			
				SharedPreferences prefs = getSharedPreferences("medsplash", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				if(checkedId == R.id.radio0)
					editor.putBoolean("showAnsAtEnd", false);
				else 
					editor.putBoolean("showAnsAtEnd", true);
				editor.commit();
			}
		});
	}
}
