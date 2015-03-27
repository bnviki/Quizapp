package com.medicine.vhquiz.data.entity;

public class DailyRecord {
	public int totalTests;
	public int avgScore;
	public int bestScore;
	
	public DailyRecord(int totalTests, int avgScore, int bestScore){
		this.totalTests = totalTests;
		this.avgScore = avgScore;
		this.bestScore = bestScore;
	}
	
	public DailyRecord(){
		this.totalTests = 0;
		this.avgScore = 0;
		this.bestScore = 0;
	}
}
