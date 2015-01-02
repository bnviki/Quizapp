package com.medicine.vhquiz.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.medicine.vhquiz.data.entity.CategoryItem;
import com.medicine.vhquiz.utils.RestClient;
import com.medicine.vhquiz.utils.RestClient.RequestMethod;
import com.medicine.vhquiz.utils.RestResponse;
import com.nicolatesser.androidquiztemplate.quiz.Answer;
import com.nicolatesser.androidquiztemplate.quiz.Question;

public class QuizManager {
	private static QuizManager instance;
	private Context context;
	public List<CategoryItem> mainCats;
	public List<CategoryItem> subCats;
	private RestClient restClient;
	public List<Question> questions;
	
	public static QuizManager getInstance(Context context) {
		if(instance == null)
			instance = new QuizManager(context.getApplicationContext());
		return instance;
	}
	
	private QuizManager(Context context){
		this.context = context;
		restClient = RestClient.getInstance();
		mainCats = new ArrayList<CategoryItem>();
		subCats = new ArrayList<CategoryItem>();
	}
	
	public void initCats(){
		if(mainCats.size() > 0 && subCats.size() > 0){
			return;
		}
		RestResponse res = restClient.execute("/quiz/restserver/index.php/medquiz/subject", RequestMethod.GET, null, null);    	

		if(res.responseCode == 200){		
			try{
				JSONArray jArray = new JSONArray(res.response);
				if(jArray.length() > 0){
					try{
						for(int i=0; i < jArray.length(); i++){						
							JSONObject obj = jArray.getJSONObject(i);
							CategoryItem cat = new CategoryItem(CategoryItem.MAIN_CATEGORY, obj);
							if(!cat.name.equals(""))
								mainCats.add(cat);
						}	
					} catch(Exception e){
						
					}
				}
				
			} catch(Exception e){
				
			}
		} else {
			Log.e("quiz", "failed to fetch cats");
		}
		
		res = restClient.execute("/quiz/restserver/index.php/medquiz/quizcat", RequestMethod.GET, null, null);    	

		if(res.responseCode == 200){		
			try{
				JSONArray jArray = new JSONArray(res.response);
				if(jArray.length() > 0){
					try{
						for(int i=0; i < jArray.length(); i++){						
							JSONObject obj = jArray.getJSONObject(i);
							CategoryItem cat = new CategoryItem(CategoryItem.SUB_CATEGORY, obj);
							subCats.add(cat);
						}	
					} catch(Exception e){						
					}
				}				
			} catch(Exception e){				
			}
		} else {
			Log.e("quiz", "failed to fetch sub cats");
		}
	}	
	
	public List<CategoryItem> getSubCategory(String mainCat){
		ArrayList<CategoryItem> cats = new ArrayList<CategoryItem>();
		for(int i=0; i<subCats.size();i++){
			CategoryItem cat = subCats.get(i); 
			if(cat.parent.equals(mainCat)){
				cats.add(cat);
			}
		}
		return cats;
	}
	
	public List<Question> getQuestions(){
		return questions;
	}
	
	public List<Question> fetchQuestions(String catCode){
		ArrayList<Question> questions = new ArrayList<Question>();
		String url = "/quiz/restserver/index.php/quiztest/user/id/" + catCode;
		RestResponse res = restClient.execute(url, RequestMethod.GET, null, null);    	

		if(res.responseCode == 200){		
			try{
				JSONArray jArray = new JSONArray(res.response);
				if(jArray.length() > 0){
					try{
						for(int i=0; i < jArray.length(); i++){						
							JSONObject obj = jArray.getJSONObject(i);
							String questionText = obj.getString("question_text");
							questionText = questionText.replace("<b>", "");
							questionText = questionText.replace("</b>", "");
							String[] ans = obj.getString("answer_text").split(",");
							String[] types = obj.getString("answer_correct").split(",");
							List<Answer> answers = new LinkedList<Answer>();
							for(int j=0; j < ans.length; j++){
								answers.add(new Answer(ans[j], types[j].equals("Correct")));
							}
							List<String> categories = Arrays.asList("category1");
							Question question = new Question(questionText, answers, categories );
							questions.add(question);
						}	
					} catch(Exception e){
						
					}
				}
				
			} catch(Exception e){
				
			}
		} else {
			Log.e("quiz", "failed to fetch questions");
		}
		this.questions = questions;
		return questions;
	}
	
}
