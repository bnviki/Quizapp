package com.medicine.vhquiz.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.data.UserManager;
import com.medicine.vhquiz.data.entity.User;
import com.squareup.picasso.Picasso;

public class ProfileView extends Activity{
	private TextView displayNameView;
	private ImageView profileImageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		User currentUser = UserManager.getInstance(this).getCurrentUser();
		if(currentUser == null){
			finish();
			return;
		}	

		setContentView(R.layout.profile);
		displayNameView = (TextView) findViewById(R.id.displayName);
		profileImageView = (ImageView) findViewById(R.id.profileImage);
		
		displayNameView.setText(currentUser.displayName);
			
		Picasso.with(this).load(currentUser.picture).into(profileImageView);
		
		
	}

}
