package com.medicine.vhquiz.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.medicine.vhquiz.R;
import com.medicine.vhquiz.activity.CategorySelection;
import com.medicine.vhquiz.activity.Instruction;
import com.medicine.vhquiz.activity.MainQuiz;
import com.medicine.vhquiz.adapter.MainCategoryAdapter;
import com.medicine.vhquiz.data.QuizManager;
import com.medicine.vhquiz.data.entity.CategoryItem;

public class CategoryFragment extends ListFragment {
	public static enum CategoryCode {MAIN, SUB, CAT};
	public ArrayAdapter<CategoryItem> listAdapter;
	
	public CategoryCode category = CategoryCode.MAIN;
	public List<CategoryItem> categories;
	public List<CategoryItem> mainCats;
	public List<CategoryItem> subCats;
	public QuizManager quizManager;
	public String mainCategoryCode;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		/*if(savedInstanceState != null){
			int cat = savedInstanceState.getInt("category");
			category = CategoryCode.values()[cat];
		}*/
		
		getActivity().getActionBar().setTitle("Choose a category");
		quizManager = QuizManager.getInstance(getActivity());
		
		mainCats = new ArrayList<CategoryItem>();
		subCats = new ArrayList<CategoryItem>();
		categories = new ArrayList<CategoryItem>();
		
		Bundle bundle = this.getArguments();
		if(bundle != null){
		    int cat = bundle.getInt("category", 0);		    
		    category = CategoryCode.values()[cat];
		    if(category == CategoryCode.SUB)
		    	mainCategoryCode = bundle.getString("catcode", "");
		}
		new FetchCats().execute();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {       
        //String[] mainCats = {"Anatomy", "Pharmacology", "Microbiology", "Pathology"};
        //String[] subCats = {"Sub Category 1", "Sub Category 2", "Sub Category 3", "Sub Category 4", "Sub Category 5", "Sub Category 6"};
        //String[] cats = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5", "Category 6"};
        //listAdapter = new ArrayAdapter<String>(getActivity(),
    	//		R.layout.category_item, categories);
		
		listAdapter = new MainCategoryAdapter(getActivity(), categories);
        setListAdapter(listAdapter);
        
        if(category == CategoryCode.MAIN){
        	((CategorySelection)getActivity()).backBtn.setVisibility(View.GONE);
        	//listAdapter = new MainCategoryAdapter(getActivity(), mainCats);        	
        }
        else if(category == CategoryCode.SUB){
        	((CategorySelection)getActivity()).backBtn.setVisibility(View.VISIBLE);        	
        }
        else {
        	((CategorySelection)getActivity()).backBtn.setVisibility(View.VISIBLE);        	
        }       
                
        return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	 @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);         
     }

     @Override
     public void onListItemClick(ListView l, View v, int position, long id) {
    	 if(category == CategoryCode.MAIN){
    		 CategoryItem cat = categories.get(position);
    		 ((CategorySelection)getActivity()).catSelected = cat.id;
    		 ((CategorySelection)getActivity()).setNavigation(CategoryCode.SUB);
    	 }
    	 //else if(category == CategoryCode.SUB)
    	//	 ((CategorySelection)getActivity()).setNavigation(CategoryCode.CAT);
    	 else {
    		 CategoryItem cat = categories.get(position);
    		 Intent quizIntent = new Intent(getActivity(), Instruction.class);
    		 quizIntent.putExtra("catcode", cat.id);
    		 startActivity(quizIntent);    		 
    	 }    		 
     }
     
     private class FetchCats extends AsyncTask<String, String, Boolean> {
 		private ProgressDialog pDialog = new ProgressDialog(getActivity());

 		@Override
 		protected void onPreExecute() {
 			super.onPreExecute();			
 			//pDialog = new ProgressDialog(Dashboard.this.getApplicationContext());			
 			pDialog.setMessage("loading ...");
 			pDialog.setIndeterminate(false);
 			pDialog.setCancelable(false);
 			pDialog.show();
 		}

 		@Override
 		protected Boolean doInBackground(String... params) {
 			quizManager.initCats();
 			return true;
 		}

 		@Override
 		protected void onPostExecute(Boolean state) {
 			pDialog.dismiss();			
 			categories.clear();
 			if(category == CategoryCode.MAIN){
 				categories.addAll(quizManager.mainCats);
 			} else if(category == CategoryCode.SUB){
 				categories.addAll(quizManager.getSubCategory(mainCategoryCode)); 				
 			}
 			listAdapter.notifyDataSetChanged();
 		}
 	}
}
