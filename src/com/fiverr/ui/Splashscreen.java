package com.fiverr.ui;

import android.content.Intent;
import android.os.Bundle;

public class Splashscreen extends BaseAcivity{

	private int TIME_OUT=1000;	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_splashscreen);
		 doWait();
	}
	
	public void doWait(){
		Thread t = new Thread(){			
			public void run() {
			   try {
				Thread.sleep(TIME_OUT);
				Intent menuIntent = new Intent(Splashscreen.this,Dashboard.class);
				startActivity(menuIntent);
				finish();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		};
		t.start();
	}
}

