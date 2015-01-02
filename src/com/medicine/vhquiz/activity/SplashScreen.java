package com.medicine.vhquiz.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.medicine.vhquiz.R;

public class SplashScreen extends Activity {
	private Button nextBtn; 
	private ImageView background;
	private int currentScreen = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		getActionBar().hide();
		
		background = (ImageView) findViewById(R.id.back);
		nextBtn = (Button) findViewById(R.id.nextBtn);		
		nextBtn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {		
				if(currentScreen == 1)
					background.setImageResource(R.drawable.splash2);
				else if(currentScreen == 2){
					background.setImageResource(R.drawable.splash3);
					((Button)arg0).setText("Done");
				}
				else 
					finish();
				currentScreen++;
			}
		});
	}
}
