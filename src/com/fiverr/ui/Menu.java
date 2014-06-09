package com.fiverr.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends Activity{

	private LinearLayout ll_browse,ll_favourite,ll_kid_profile,ll_kid_qoute,ll_submit_qoute,ll_login;
	private SharedPreferences settings ;
	
	private TextView tv_signin;
	private ImageView iv_signin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		ll_browse = (LinearLayout) findViewById(R.id.ll_Browse);
		ll_favourite = (LinearLayout) findViewById(R.id.ll_favourite);
		ll_kid_profile = (LinearLayout) findViewById(R.id.ll_kids_profile);
		ll_kid_qoute = (LinearLayout) findViewById(R.id.ll_kids_qoute);
		ll_submit_qoute = (LinearLayout) findViewById(R.id.ll_submit_qoute);
		ll_login =(LinearLayout)findViewById(R.id.ll_signin);
		
		tv_signin = (TextView)findViewById(R.id.tv_signin);
		iv_signin = (ImageView)findViewById(R.id.iv_signin);
		
		
		settings = getSharedPreferences("MYPREFS", 0);
		Log.d("Parent_ID_STRING",settings.getString("Parent_ID", "0"));
		if(settings.getString("Parent_ID", "0").equals("0")){
			iv_signin.setImageResource(R.drawable.signin_icon);
			tv_signin.setText("Sign In");
		}else {
			iv_signin.setImageResource(R.drawable.logout_icon);
			tv_signin.setText("Sign Out");
		}
		
		ll_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(settings.getString("Parent_ID", "0").equals("0")){
					Intent loginIntent = new Intent(Menu.this,Login.class);//
					startActivity(loginIntent);
				}else{
					SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("Parent_ID", "0");
					editor.commit();
					finish();
				}
			}
		});
		
		ll_kid_profile.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(settings.getString("Parent_ID", "0").equals("0")){
					Toast.makeText(getApplicationContext(),
							"you have to Login first",
							Toast.LENGTH_LONG).show();
				}else{
				//Intent intent = new Intent(Menu.this,KidProfile.class);
				Intent intent = new Intent(Menu.this,KidList.class);
				intent.putExtra("type", "create");
				startActivity(intent);
				}
			}
		});
		
		ll_submit_qoute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(settings.getString("Parent_ID", "0").equals("0")){
					Toast.makeText(getApplicationContext(),
							"you have to Login first",
							Toast.LENGTH_LONG).show();
				}else{
				Intent submitQuotes = new Intent(Menu.this,KidList.class);
				submitQuotes.putExtra("type", "submit");
				startActivity(submitQuotes);
				}
			}
		});
		ll_browse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent browseQuotes = new Intent(Menu.this,AllQuote.class);
				browseQuotes.putExtra("test", "0");
				browseQuotes.putExtra("quote_type", "all");
				startActivity(browseQuotes);
			}
		});
		 ll_favourite.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(settings.getString("Parent_ID", "0").equals("0")){
						Toast.makeText(getApplicationContext(),"you have to Login first",Toast.LENGTH_LONG).show();
					}else{
					Intent browseQuotes = new Intent(Menu.this,AllQuote.class);
					SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
					browseQuotes.putExtra("test", settings.getString("Parent_ID", "0"));
					browseQuotes.putExtra("quote_type", "fav");
					startActivity(browseQuotes);
					}
				}
			});
		 ll_kid_qoute.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(settings.getString("Parent_ID", "0").equals("0")){
						Toast.makeText(getApplicationContext(),	"you have to Login first",Toast.LENGTH_LONG).show();
					}else{
						Intent browseQuotes = new Intent(Menu.this,AllQuote.class);
						SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
						browseQuotes.putExtra("test", settings.getString("Parent_ID", "0"));
						browseQuotes.putExtra("quote_type", "kid_qoute");
						startActivity(browseQuotes);
					}					
				}
			});		
	}
}
