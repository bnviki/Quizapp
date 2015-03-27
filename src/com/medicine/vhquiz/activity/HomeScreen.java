package com.medicine.vhquiz.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.data.UserManager;
import com.medicine.vhquiz.data.entity.User;
import com.squareup.picasso.Picasso;

public class HomeScreen extends Activity{
	private static final int PROFILE_MENU_ITEM = Menu.FIRST;
	private static final int SETTINGS_MENU_ITEM = Menu.FIRST + 1;
	private static final int LOGOUT_MENU_ITEM = Menu.FIRST + 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_screen);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(actionBar.getDisplayOptions()
				| ActionBar.DISPLAY_SHOW_CUSTOM);
		ImageView imageView = new ImageView(actionBar.getThemedContext());
		imageView.setScaleType(ImageView.ScaleType.CENTER);

		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
				| Gravity.CENTER_VERTICAL);
		layoutParams.rightMargin = 40;
		imageView.setLayoutParams(layoutParams);
		actionBar.setCustomView(imageView);

		imageView.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent profileIntent = new Intent(HomeScreen.this, ProfileView.class);
				startActivity(profileIntent);
			}
		});
		
		User currentUser = UserManager.getInstance(this).getCurrentUser();
		if(currentUser != null)
			Picasso.with(this).load(currentUser.picture).error(R.drawable.guest).into(imageView);
		else 
			Picasso.with(this).load(R.drawable.guest).into(imageView);

		
		Button startQuizButton = (Button) findViewById(R.id.startQuizBtn);		
		startQuizButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent nextScreenIntent = new Intent(HomeScreen.this, CategorySelection.class);
				startActivity(nextScreenIntent);				
			}
		});
		
		Button settingsButton = (Button) findViewById(R.id.settingsBtn);		
		settingsButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent nextScreenIntent = new Intent(HomeScreen.this, Settings.class);
				startActivity(nextScreenIntent);				
			}
		});	

		Button statsButton = (Button) findViewById(R.id.statsBtn);		
		statsButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent nextScreenIntent = new Intent(HomeScreen.this, Statistics.class);
				startActivity(nextScreenIntent);				
			}
		});	

		Button feedbackButton = (Button) findViewById(R.id.feedbackBtn);		
		feedbackButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent nextScreenIntent = new Intent(HomeScreen.this, Feedback.class);
				startActivity(nextScreenIntent);				
			}
		});
		
		Button quitButton = (Button) findViewById(R.id.quitBtn);		
		quitButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});	


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int menuItemOrder = Menu.NONE;			

		menu.add(Menu.NONE, SETTINGS_MENU_ITEM,	menuItemOrder, "Settings");
		menu.add(Menu.NONE, LOGOUT_MENU_ITEM, menuItemOrder, "Log out");	
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case LOGOUT_MENU_ITEM: onLogoutOption();
		break;
		case SETTINGS_MENU_ITEM: Intent actIntent = new Intent(this, Settings.class);
		startActivity(actIntent);
		break;			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onLogoutOption(){
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
		AlertDialog dialog;

		builder.setMessage("You will loose your saved records. Are you sure you want to Logout?")
		.setTitle("Logout");

		builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				UserManager.getInstance(HomeScreen.this).logoutUser();				
				dialog.dismiss();
				Intent actIntent = new Intent(HomeScreen.this, MainActivity.class);
				startActivity(actIntent);
				finish();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {				
				dialog.dismiss();
			}
		});

		dialog = builder.create();
		dialog.show();
	}
}
