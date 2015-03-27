package com.medicine.vhquiz.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.medicine.vhquiz.R;
import com.medicine.vhquiz.adapter.CategorySpinnerAdapter;
import com.medicine.vhquiz.data.QuizManager;
import com.medicine.vhquiz.data.entity.CategoryItem;
import com.medicine.vhquiz.data.entity.DailyRecord;
import com.medicine.vhquiz.data.tables.RecordTable;

public class Statistics extends Activity implements OnItemSelectedListener{
	public TextView avgScoreView;
	public TextView totalTestsView;
	public TextView bestScoreView;
	public GraphView graph;
	public GraphView graph1;
	public GraphView graph2;
	public Spinner catList;
	public CategorySpinnerAdapter adapter;
	public QuizManager quizManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);
		
		avgScoreView = (TextView) findViewById(R.id.avgScore);
		totalTestsView = (TextView) findViewById(R.id.totalTests);
		bestScoreView = (TextView) findViewById(R.id.bestScore);

		graph = (GraphView) findViewById(R.id.graph);
		GridLabelRenderer gd = graph.getGridLabelRenderer();
		gd.setGridColor(getResources().getColor(android.R.color.white));
		gd.setHorizontalAxisTitle("Days");
		gd.setVerticalAxisTitle("avg score");
		gd.setHorizontalAxisTitleColor(getResources().getColor(android.R.color.white));
		gd.setVerticalAxisTitleColor(getResources().getColor(android.R.color.white));
		gd.setHorizontalLabelsColor(getResources().getColor(android.R.color.white));
		gd.setVerticalLabelsColor(getResources().getColor(android.R.color.white));
		graph.getViewport().setXAxisBoundsManual(true);
		graph.getViewport().setMaxX(10);
		graph.getViewport().setMinX(0);
		
		graph1 = (GraphView) findViewById(R.id.graph1);
		GridLabelRenderer gd1 = graph1.getGridLabelRenderer();
		gd1.setGridColor(getResources().getColor(android.R.color.white));
		gd1.setHorizontalAxisTitle("Days");
		gd1.setVerticalAxisTitle("avg score");
		gd1.setHorizontalAxisTitleColor(getResources().getColor(android.R.color.white));
		gd1.setVerticalAxisTitleColor(getResources().getColor(android.R.color.white));
		gd1.setHorizontalLabelsColor(getResources().getColor(android.R.color.white));
		gd1.setVerticalLabelsColor(getResources().getColor(android.R.color.white));
		graph1.getViewport().setXAxisBoundsManual(true);
		graph1.getViewport().setMaxX(10);
		graph1.getViewport().setMinX(0);
		
		graph2 = (GraphView) findViewById(R.id.graph2);
		GridLabelRenderer gd2 = graph2.getGridLabelRenderer();
		gd2.setGridColor(getResources().getColor(android.R.color.white));
		gd2.setHorizontalAxisTitle("Days");
		gd2.setVerticalAxisTitle("avg score");
		gd2.setHorizontalAxisTitleColor(getResources().getColor(android.R.color.white));
		gd2.setVerticalAxisTitleColor(getResources().getColor(android.R.color.white));
		gd2.setHorizontalLabelsColor(getResources().getColor(android.R.color.white));
		gd2.setVerticalLabelsColor(getResources().getColor(android.R.color.white));
		graph2.getViewport().setXAxisBoundsManual(true);
		graph2.getViewport().setMaxX(10);
		graph2.getViewport().setMinX(0);
		
		catList = (Spinner) findViewById(R.id.catList);
		quizManager = QuizManager.getInstance(this);		
		adapter = new CategorySpinnerAdapter(this, quizManager.mainCats);	
		catList.setAdapter(adapter);
		catList.setOnItemSelectedListener(this);
		
		new ShowStats().execute();
	}
	
	private class ShowStats extends AsyncTask<String, String, DailyRecord> {		
		private ArrayList<DailyRecord> recs;
		private ArrayList<DailyRecord> catRecs;
		private ProgressDialog pDialog = new ProgressDialog(Statistics.this);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("loading ...");
 			pDialog.setIndeterminate(false);
 			pDialog.setCancelable(false);
 			pDialog.show();
		}

		@Override
		protected DailyRecord doInBackground(String... params) {		
			quizManager.initCats();
			DailyRecord rec = RecordTable.getInstance(Statistics.this).getTodaysRecord();
			recs = RecordTable.getInstance(Statistics.this).getLast10DaysRecords();
			return rec;
		}

		@Override
		protected void onPostExecute(DailyRecord rec) {	
			pDialog.dismiss();
			avgScoreView.setText("" + rec.avgScore);
			totalTestsView.setText("" + rec.totalTests);
			bestScoreView.setText("" + rec.bestScore);
			
			DataPoint[] dp = new DataPoint[10];
			for(int i=0; i<10; i++){
				dp[i] = new DataPoint(i, recs.get(i).avgScore);
			}			
						
			//adapter.addAll(quizManager.mainCats);
			adapter.notifyDataSetChanged();
			
			LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dp);
			graph.addSeries(series2);			
			series2.setColor(getResources().getColor(android.R.color.holo_orange_dark));
		}
	}
	
	private class ShowCatStats extends AsyncTask<String, String, DailyRecord> {		
		private ArrayList<DailyRecord> recs;
		private ArrayList<DailyRecord> mistakeRecs;
		private ProgressDialog pDialog = new ProgressDialog(Statistics.this);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("loading ...");
 			pDialog.setIndeterminate(false);
 			pDialog.setCancelable(false);
 			pDialog.show();
		}

		@Override
		protected DailyRecord doInBackground(String... params) {	
			List<CategoryItem> cats = quizManager.getSubCategory(params[0]);			
			recs = RecordTable.getInstance(Statistics.this).getCategoryLast10DaysRecords(cats);
			mistakeRecs = RecordTable.getInstance(Statistics.this).getCategoryLast10DaysMistakeRecords(cats);
			return null;
		}

		@Override
		protected void onPostExecute(DailyRecord rec) {	
			pDialog.dismiss();	
			graph1.removeAllSeries();
			graph2.removeAllSeries();
			DataPoint[] dp = new DataPoint[10];
			for(int i=0; i<10; i++){
				dp[i] = new DataPoint(i, recs.get(i).avgScore);
			}
			
			LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dp);			
			graph1.addSeries(series2);
			
			DataPoint[] dp1 = new DataPoint[10];
			for(int i=0; i<10; i++){
				dp1[i] = new DataPoint(i, mistakeRecs.get(i).avgScore);
			}
			
			LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(dp1);			
			graph2.addSeries(series3);
			series3.setColor(getResources().getColor(android.R.color.holo_orange_dark));
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		CategoryItem cat = quizManager.mainCats.get(position);
		new ShowCatStats().execute(cat.id);		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
