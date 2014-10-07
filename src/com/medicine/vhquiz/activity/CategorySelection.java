package com.medicine.vhquiz.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.fragment.CategoryFragment;
import com.medicine.vhquiz.fragment.CategoryFragment.CategoryCode;

public class CategorySelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.category_selector);
		setNavigation(CategoryCode.MAIN);
	}
	
	public void setNavigation(CategoryCode cat){
		CategoryFragment mainCategory = new CategoryFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("category", cat.ordinal());
		mainCategory.setArguments(bundle);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, mainCategory);
		if(cat != CategoryCode.MAIN)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
}
