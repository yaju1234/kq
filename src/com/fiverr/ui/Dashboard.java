package com.fiverr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dashboard extends BaseAcivity{

	private LinearLayout ll_browse,ll_my_favorite,ll_kids_profile,ll_kids_posts,ll_submit,ll_signin;
	private TextView tv_signin;
	private ImageView iv_signin;	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);		
		ll_browse = (LinearLayout) findViewById(R.id.ll_browse);
		ll_my_favorite = (LinearLayout) findViewById(R.id.ll_my_favorite);
		ll_kids_profile = (LinearLayout) findViewById(R.id.ll_kids_profile);
		ll_kids_posts = (LinearLayout) findViewById(R.id.ll_kids_posts);
		ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
		ll_signin =(LinearLayout)findViewById(R.id.ll_signin);		
		tv_signin = (TextView)findViewById(R.id.tv_signin);
		iv_signin = (ImageView)findViewById(R.id.iv_signin);		
		if(app.info.useid == 0){
			iv_signin.setImageResource(R.drawable.signin_icon);
			tv_signin.setText("Sign In");
		}else {
			iv_signin.setImageResource(R.drawable.logout_icon);
			tv_signin.setText("Sign Out");
		}		
		ll_browse.setOnClickListener(this);
		ll_my_favorite.setOnClickListener(this);
		ll_kids_profile.setOnClickListener(this);
		ll_kids_posts.setOnClickListener(this);
		ll_submit.setOnClickListener(this);
		ll_signin.setOnClickListener(this);		
	}	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_browse:
			if(app.info.useid == 0){			
				showToast("you have to Login first");
			}else{
			Intent browseQuotes = new Intent(Dashboard.this,AllQuote.class);
			browseQuotes.putExtra("test", app.info.useid);
			browseQuotes.putExtra("quote_type", "fav");
			startActivity(browseQuotes);
			}
			break;
		case R.id.ll_my_favorite:
			if(app.info.useid == 0){
				showToast("you have to Login first");
			}else{
			Intent browseQuotes = new Intent(Dashboard.this,AllQuote.class);
			browseQuotes.putExtra("test", app.info.useid);
			browseQuotes.putExtra("quote_type", "fav");
			startActivity(browseQuotes);
			}
			break;
		case R.id.ll_kids_profile:
			if(app.info.useid == 0){
				showToast("you have to Login first");					
			}else{
			Intent intent = new Intent(Dashboard.this,KidList.class);
			intent.putExtra("type", "create");
			startActivity(intent);
			}
			break;			
		case R.id.ll_kids_posts:
			if(app.info.useid == 0){
				showToast("you have to Login first");
			}else{
				Intent browseQuotes = new Intent(Dashboard.this,AllQuote.class);
				browseQuotes.putExtra("test",app.info.useid);
				browseQuotes.putExtra("quote_type", "kid_qoute");
				startActivity(browseQuotes);
			}	
			break;
		case R.id.ll_submit:
			if(app.info.useid == 0){				
				showToast("you have to Login first");						
			}else{
			Intent submitQuotes = new Intent(Dashboard.this,KidList.class);
			submitQuotes.putExtra("type", "submit");
			startActivity(submitQuotes);
			}
			break;			
		case R.id.ll_signin:
			if(app.info.useid == 0){
				Intent loginIntent = new Intent(Dashboard.this,LoginAcivity.class);//
				startActivity(loginIntent);
			}else{
				app.info.setUsrId(0);
				finish();
			}
			break;
		}
	}
}
