package com.medicine.vhquiz.activity;

import com.medicine.vhquiz.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		SharedPreferences prefs = getSharedPreferences("medsplash", Context.MODE_PRIVATE);
		
		Switch ansAtEndButton = (Switch) findViewById(R.id.switch1);
		ansAtEndButton.setChecked(prefs.getBoolean("showAnsAtEnd", false));
		ansAtEndButton.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {			
				SharedPreferences prefs = getSharedPreferences("medsplash", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean("showAnsAtEnd", arg1);
				editor.commit();
			}
		});
	}
}
