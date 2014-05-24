package com.fiverr.ui;

import com.fiverr.ui.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends Activity{

	private int TIME_OUT=1000;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		mContext = this.getApplicationContext();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				/*// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
				String temp=settings.getString("Parent_ID", "0");
				if(temp=="0"){
				Intent loginIntent = new Intent(Splashscreen.this,Login.class);//
				startActivity(loginIntent);
				finish();
				}else{*/
					Intent menuIntent = new Intent(Splashscreen.this,Menu.class);//
					startActivity(menuIntent);
					finish();
				//}
			}
		}, TIME_OUT);
	}
}

