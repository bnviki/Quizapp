package com.medicine.vhquiz.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.medicine.vhquiz.fragment.CategoryFragment;
import com.medicine.vhquiz.fragment.CategoryFragment.CategoryCode;
import com.squareup.picasso.Picasso;

public class CategorySelection extends Activity {
	private static final int PROFILE_MENU_ITEM = Menu.FIRST;
	private static final int SETTINGS_MENU_ITEM = Menu.FIRST + 1;
	private static final int LOGOUT_MENU_ITEM = Menu.FIRST + 2;		
	
	public Button backBtn;
	public String catSelected = "";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.category_selector);
		setNavigation(CategoryCode.MAIN);

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
				Intent profileIntent = new Intent(CategorySelection.this, ProfileView.class);
				startActivity(profileIntent);
			}
		});
		
		backBtn = (Button) findViewById(R.id.backButton);
		backBtn.setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});		

		User currentUser = UserManager.getInstance(this).getCurrentUser();
		if(currentUser != null)
			Picasso.with(this).load(currentUser.picture).error(R.drawable.guest).into(imageView);
		else 
			Picasso.with(this).load(R.drawable.guest).into(imageView);
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
    }

	public void setNavigation(CategoryCode cat){
		CategoryFragment mainCategory = new CategoryFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("category", cat.ordinal());
		bundle.putString("catcode", catSelected);
		mainCategory.setArguments(bundle);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mainCategory);
		if(cat != CategoryCode.MAIN)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
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
		AlertDialog.Builder builder = new AlertDialog.Builder(CategorySelection.this);
		AlertDialog dialog;

		builder.setMessage("You will loose your saved records. Are you sure you want to Logout?")
		.setTitle("Logout");

		builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				UserManager.getInstance(CategorySelection.this).logoutUser();				
				dialog.dismiss();
				Intent actIntent = new Intent(CategorySelection.this, MainActivity.class);
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
