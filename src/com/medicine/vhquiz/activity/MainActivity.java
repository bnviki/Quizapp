package com.medicine.vhquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.SignInButton;
import com.medicine.vhquiz.R;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button signInButton = (Button) findViewById(R.id.signin);
		signInButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {			
				Intent categoryIntent = new Intent(MainActivity.this, CategorySelection.class);
				startActivity(categoryIntent);				
			}
		});
		ImageButton fsignInButton = (ImageButton) findViewById(R.id.facebook_signin);
		fsignInButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {			
				Intent categoryIntent = new Intent(MainActivity.this, CategorySelection.class);
				startActivity(categoryIntent);				
			}
		});
		SignInButton gsignInButton = (SignInButton) findViewById(R.id.google_signin);
		gsignInButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {			
				Intent categoryIntent = new Intent(MainActivity.this, CategorySelection.class);
				startActivity(categoryIntent);				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
}
