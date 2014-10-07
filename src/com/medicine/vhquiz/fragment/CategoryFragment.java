package com.medicine.vhquiz.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.activity.CategorySelection;
import com.medicine.vhquiz.activity.MainQuiz;
import com.medicine.vhquiz.adapter.MainCategoryAdapter;

public class CategoryFragment extends ListFragment {
	public static enum CategoryCode {MAIN, SUB, CAT};
	public ArrayAdapter<String> listAdapter;
	
	public CategoryCode category = CategoryCode.MAIN;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		/*if(savedInstanceState != null){
			int cat = savedInstanceState.getInt("category");
			category = CategoryCode.values()[cat];
		}*/
		
		getActivity().getActionBar().setTitle("Choose a category");
		
		Bundle bundle = this.getArguments();
		if(bundle != null){
		    int cat = bundle.getInt("category", 0);
		    category = CategoryCode.values()[cat];
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {       
        String[] mainCats = {"Anatomy", "Pharmacology", "Microbiology", "Pathology"};
        String[] subCats = {"Sub Category 1", "Sub Category 2", "Sub Category 3", "Sub Category 4", "Sub Category 5", "Sub Category 6"};
        String[] cats = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5", "Category 6"};
        if(category == CategoryCode.MAIN)
        	listAdapter = new MainCategoryAdapter(getActivity(), mainCats);
        else if(category == CategoryCode.SUB)
        	listAdapter = new ArrayAdapter<String>(getActivity(),
        			R.layout.category_item, subCats);
        else 
        	listAdapter = new ArrayAdapter<String>(getActivity(),
        			R.layout.category_item, cats);
        
        setListAdapter(listAdapter);        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	 @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);         
     }

     @Override
     public void onListItemClick(ListView l, View v, int position, long id) {
    	 if(category == CategoryCode.MAIN)
    		 ((CategorySelection)getActivity()).setNavigation(CategoryCode.SUB);
    	 else if(category == CategoryCode.SUB)
    		 ((CategorySelection)getActivity()).setNavigation(CategoryCode.CAT);
    	 else {
    		 Intent quizIntent = new Intent(getActivity(), MainQuiz.class);
    		 startActivity(quizIntent);    		 
    	 }
    		 
     }
}
