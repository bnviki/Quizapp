package com.medicine.vhquiz;

import android.app.Application;

public class QuizApp extends Application {

	@Override
	public void onCreate() {
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Comfortaa Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
	}
}