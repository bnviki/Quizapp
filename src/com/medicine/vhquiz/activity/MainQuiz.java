package com.medicine.vhquiz.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.medicine.vhquiz.data.QuizManager;
import com.medicine.vhquiz.utils.RestClient;
import com.medicine.vhquiz.utils.RestClient.RequestMethod;
import com.medicine.vhquiz.utils.RestResponse;
import com.nicolatesser.androidquiztemplate.activity.QuizActivity;
import com.nicolatesser.androidquiztemplate.activity.SessionUtils;
import com.nicolatesser.androidquiztemplate.quiz.Answer;
import com.nicolatesser.androidquiztemplate.quiz.GameHolder;
import com.nicolatesser.androidquiztemplate.quiz.Question;
import com.nicolatesser.androidquiztemplate.quiz.QuestionDatabase;
import com.nicolatesser.androidquiztemplate.quiz.Session;
import com.nicolatesser.androidquiztemplate.quiz.TriviaGame;

public class MainQuiz extends QuizActivity {
	RestClient restClient;
	public String catCode;
	public List<Question> questions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		catCode = getIntent().getExtras().getString("catcode");
		if(catCode == null || catCode == ""){
			finish();
			return;
		}
			
		questions = new ArrayList<Question>();
		restClient = RestClient.getInstance();		
		getActionBar().setTitle("Quiz");
		
		QuestionDatabase qd = QuestionDatabase.getInstance();		
		qd.prepare(QuizManager.getInstance(this).getQuestions());
		//setContentView(R.layout.quiz);
		SessionUtils.setSession(MainQuiz.this, new Session());   //create an empty session   
		List<String> categories = Arrays.asList("category1");
		TriviaGame game = new TriviaGame(QuestionDatabase.getInstance(), categories); 
		GameHolder.setInstance(game);
		loadRecord();   
		displayNextQuestion();	
	}

	public List<Question> getQuestions(){
		ArrayList<Question> ques = new ArrayList<Question>();    	
		String questionText = "Which nerves does not arise form the medulla?";
		List<Answer> answers = new LinkedList<Answer>();
		answers.add(new Answer("Facial nerve nucleus", true));
		answers.add(new Answer("Dorsal horn of gray matter", false));
		answers.add(new Answer("Glossopharyngeal", false));
		answers.add(new Answer("Vagus", false));
		List<String> categories = Arrays.asList("category1");
		Question question = new Question(questionText, answers, categories );    	   	
		ques.add(question);
		String questionText1 = "Purkinje cells from the cerebellum end in?";
		List<Answer> answers1 = new LinkedList<Answer>();
		answers1.add(new Answer("Cerebellar nuclie", true));
		answers1.add(new Answer("cerebral cortex", false));
		answers1.add(new Answer("Cranial nerve nuclie", false));
		answers1.add(new Answer("extrapyramidal system", false));    	
		Question question1 = new Question(questionText1, answers1, categories );    	   	
		ques.add(question1);
		String questionText3 = "Which of the following nuclei belongs to the general visceral effrent column?";
		List<Answer> answers3 = new LinkedList<Answer>();
		answers3.add(new Answer("Dorsal Nucleus of vagus", true));
		answers3.add(new Answer("Nucleus ambiguous", false));
		answers3.add(new Answer("Facial nerve nucleus", false));
		answers3.add(new Answer("Trigeminal nerrve nucleus", false));    	
		Question question3 = new Question(questionText3, answers3, categories );    	   	
		ques.add(question3);
		String questionText4 = "Superior oblique muscle is supplied by?";
		List<Answer> answers4 = new LinkedList<Answer>();
		answers4.add(new Answer("Occulomotor nerve", false));
		answers4.add(new Answer("Trochelar nerve", true));
		answers4.add(new Answer("Trigeminal nerve", false));
		answers4.add(new Answer("Abducent nerve", false));    	
		Question question4 = new Question(questionText4, answers4, categories );    	   	
		ques.add(question4);
		String questionText5 = "These ventral spinal rootlets are more prone to injury during decompressive operation because they are shorter and exit in more horizontal direction?";
		List<Answer> answers5 = new LinkedList<Answer>();
		answers5.add(new Answer("C7", false));
		answers5.add(new Answer("T1", false));
		answers5.add(new Answer("C6", false));
		answers5.add(new Answer("C5", true));    	
		Question question5 = new Question(questionText5, answers5, categories );    	   	
		ques.add(question5);
		return ques;
	}	

}
