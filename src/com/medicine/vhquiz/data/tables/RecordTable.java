package com.medicine.vhquiz.data.tables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.medicine.vhquiz.data.DatabaseManager;
import com.medicine.vhquiz.data.entity.CategoryItem;
import com.medicine.vhquiz.data.entity.DailyRecord;

public class RecordTable implements DatabaseTable{
	
	private DatabaseManager databaseManager;
	private static RecordTable instance;
	private Context context;
	
	public static final String TABLE_NAME = "records";
	public static final String RECORD_TIME = "record_time";
	public static final String CATEGORY = "category";
	public static final String TOTAL_ANSWERS = "total";
	public static final String CORRECT_ANSWERS = "correct";
	public static final String BEST_SEQUENCE = "best_sequence";	

	public static RecordTable getInstance(Context context) {
		if(instance == null){
			instance = new RecordTable(DatabaseManager.getInstance(context), context);
			DatabaseManager.getInstance(context).addTable(instance);
		}
		return instance;
	}

	private RecordTable(DatabaseManager databaseManager, Context context) {
		this.databaseManager = databaseManager;
		this.context = context;
	} 
	
	@Override
	public void create(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + RECORD_TIME
				+ " INTEGER PRIMARY KEY," + CATEGORY + " TINYTEXT,"
				+ TOTAL_ANSWERS + " INTEGER," + CORRECT_ANSWERS + " INTEGER,"
				+ BEST_SEQUENCE + " INTEGER" + ");";
		DatabaseManager.execSQL(db, sql);
		
	}

	@Override
	public void migrate(SQLiteDatabase db, int toVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		SQLiteDatabase db = databaseManager.getWritableDatabase();
		DatabaseManager.dropTable(db, TABLE_NAME);
		create(db);		
	}
	
	public boolean saveRecord(long recordTime, String category, int total, int correct, int best){
		ContentValues values = new ContentValues();
		values.put(RECORD_TIME, recordTime);
		values.put(CATEGORY, category);
		values.put(TOTAL_ANSWERS, total);
		values.put(CORRECT_ANSWERS, correct);
		values.put(BEST_SEQUENCE, best);
		
		long res = databaseManager.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		if(res == -1)
			return false;
		return true;
	}
	
	public DailyRecord getTodaysRecord(){
		DailyRecord rec = new DailyRecord();		
		Calendar c = Calendar.getInstance();
		//c.add(Calendar.DAY_OF_YEAR, -10);
		int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = c.get(Calendar.DATE);
	    c.set(year, month, day, 0, 0, 0);
		
		String[] args = {"" + c.getTimeInMillis()};
		Cursor cursor = databaseManager.getReadableDatabase().query(TABLE_NAME, null, RECORD_TIME + " > ?", args, null,
				null, null);	
		try {
			if (cursor.moveToFirst()) {
				do {
					rec.totalTests++;
					int total = cursor.getInt(cursor.getColumnIndex(TOTAL_ANSWERS));
					int correct = cursor.getInt(cursor.getColumnIndex(CORRECT_ANSWERS));
					double avg = (correct/(1.0 * total))*100;
					if(avg > rec.bestScore)
						rec.bestScore = (int)avg;
					rec.avgScore += avg;
				} while (cursor.moveToNext());
				rec.avgScore = rec.avgScore/rec.totalTests;
			}
		} finally {
			cursor.close();
		}
		return rec;
	}
	
	public ArrayList<DailyRecord> getLast10DaysRecords(){
		ArrayList<DailyRecord> recs = new ArrayList<DailyRecord>();		
		Calendar startDay = Calendar.getInstance();
		int year = startDay.get(Calendar.YEAR);
	    int month = startDay.get(Calendar.MONTH);
	    int day = startDay.get(Calendar.DATE);
	    startDay.set(year, month, day, 0, 0, 0);
		startDay.add(Calendar.DATE, -10);
		
		for(int i=0;i<10; i++){
			DailyRecord rec = new DailyRecord();
			recs.add(rec);
		}
		
		String[] args = {"" + startDay.getTimeInMillis()};
		Cursor cursor = databaseManager.getReadableDatabase().query(TABLE_NAME, null, RECORD_TIME + " > ?", args, null,
				null, null);	
		try {
			if (cursor.moveToFirst()) {
				do {
					long recTime = cursor.getLong(cursor.getColumnIndex(RECORD_TIME));
					Calendar recDate = Calendar.getInstance();
					recDate.setTimeInMillis(recTime);
					int index = recDate.get(Calendar.DAY_OF_YEAR) - startDay.get(Calendar.DAY_OF_YEAR) - 1;
					DailyRecord rec = recs.get(index); 
					rec.totalTests++;
					int total = cursor.getInt(cursor.getColumnIndex(TOTAL_ANSWERS));
					int correct = cursor.getInt(cursor.getColumnIndex(CORRECT_ANSWERS));
					double avg = (correct/(1.0 * total))*100;
					rec.avgScore = (int)((rec.avgScore + avg)/2);
				} while (cursor.moveToNext());				
			}
		} finally {
			cursor.close();
		}
		return recs;
	}
	
	public ArrayList<DailyRecord> getCategoryLast10DaysRecords(List<CategoryItem> cats){
		ArrayList<DailyRecord> recs = new ArrayList<DailyRecord>();		
		Calendar startDay = Calendar.getInstance();
		int year = startDay.get(Calendar.YEAR);
	    int month = startDay.get(Calendar.MONTH);
	    int day = startDay.get(Calendar.DATE);
	    startDay.set(year, month, day, 0, 0, 0);
		startDay.add(Calendar.DATE, -10);
		
		for(int i=0;i<10; i++){
			DailyRecord rec = new DailyRecord();
			recs.add(rec);
		}
		
		String whereClause = RECORD_TIME + " > " + startDay.getTimeInMillis() + " AND " + CATEGORY + " IN (";
		int i=0;
		for(i=0; i<cats.size(); i++){
			whereClause += cats.get(i).id + ",";
		}
		whereClause = whereClause.substring(0, whereClause.length() - 2);
		whereClause += ")";			
		
		String[] args = {"" + startDay.getTimeInMillis()};
		Cursor cursor = databaseManager.getReadableDatabase().query(TABLE_NAME, null, whereClause, null, null,
				null, null);	
		try {
			if (cursor.moveToFirst()) {
				do {
					long recTime = cursor.getLong(cursor.getColumnIndex(RECORD_TIME));
					Calendar recDate = Calendar.getInstance();
					recDate.setTimeInMillis(recTime);
					int index = recDate.get(Calendar.DAY_OF_YEAR) - startDay.get(Calendar.DAY_OF_YEAR) - 1;
					DailyRecord rec = recs.get(index); 
					rec.totalTests++;
					int total = cursor.getInt(cursor.getColumnIndex(TOTAL_ANSWERS));
					int correct = cursor.getInt(cursor.getColumnIndex(CORRECT_ANSWERS));
					double avg = (correct/(1.0 * total))*100;
					rec.avgScore = (int)((rec.avgScore + avg)/2);
				} while (cursor.moveToNext());				
			}
		} finally {
			cursor.close();
		}
		return recs;
	}
	
	public ArrayList<DailyRecord> getCategoryLast10DaysMistakeRecords(List<CategoryItem> cats){
		ArrayList<DailyRecord> recs = new ArrayList<DailyRecord>();		
		Calendar startDay = Calendar.getInstance();
		int year = startDay.get(Calendar.YEAR);
	    int month = startDay.get(Calendar.MONTH);
	    int day = startDay.get(Calendar.DATE);
	    startDay.set(year, month, day, 0, 0, 0);
		startDay.add(Calendar.DATE, -10);
		
		for(int i=0;i<10; i++){
			DailyRecord rec = new DailyRecord();
			recs.add(rec);
		}
		
		String whereClause = RECORD_TIME + " > " + startDay.getTimeInMillis() + " AND " + CATEGORY + " IN (";
		int i=0;
		for(i=0; i<cats.size(); i++){
			whereClause += cats.get(i).id + ",";
		}
		whereClause = whereClause.substring(0, whereClause.length() - 2);
		whereClause += ")";			
		
		String[] args = {"" + startDay.getTimeInMillis()};
		Cursor cursor = databaseManager.getReadableDatabase().query(TABLE_NAME, null, whereClause, null, null,
				null, null);	
		try {
			if (cursor.moveToFirst()) {
				do {
					long recTime = cursor.getLong(cursor.getColumnIndex(RECORD_TIME));
					Calendar recDate = Calendar.getInstance();
					recDate.setTimeInMillis(recTime);
					int index = recDate.get(Calendar.DAY_OF_YEAR) - startDay.get(Calendar.DAY_OF_YEAR) - 1;
					DailyRecord rec = recs.get(index); 
					rec.totalTests++;
					int total = cursor.getInt(cursor.getColumnIndex(TOTAL_ANSWERS));
					int correct = cursor.getInt(cursor.getColumnIndex(CORRECT_ANSWERS));
					double avg = ((total - correct)/(1.0 * total))*100;
					rec.avgScore = (int)((rec.avgScore + avg)/2);
				} while (cursor.moveToNext());				
			}
		} finally {
			cursor.close();
		}
		return recs;
	}

}
