package com.fiverr.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.ui.R;
import com.fiverr.helper.UserFunctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private static String parent_id;
	ProgressDialog pDialog;
	final Context mContext =this;
	//private Typeface cTypeface;
	private Button Login, Register;
	private EditText email, password;
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		setContentView(R.layout.activity_login);
		/*cTypeface = Typeface.createFromAsset(getAssets(),
				"GochiHand-Regular.ttf");*/
		Login = (Button) findViewById(R.id.button1);
		Register = (Button) findViewById(R.id.button2);
		email = (EditText) findViewById(R.id.et_username);
		password = (EditText) findViewById(R.id.et_password);
		/*Login.setTypeface(cTypeface);
		Register.setTypeface(cTypeface);*/

		Register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Login.this, RegisterUser.class);
				startActivity(i);
			}
		});
		Login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isOnline()){
					if(isFieldValid()){
						new LoginUser().execute();
					}
				}
				
				else{
					Toast.makeText(mContext, "No Internet connection", Toast.LENGTH_LONG).show();
				}
			}
		});

	}
	private boolean isOnline()  {
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    return (ni != null && ni.isConnected());
	}
	
	public void ShowDashBoard(Context mcontext){
		Intent i = new Intent(mcontext,Menu.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		/*finish();*/
	}
	
	public void savePreference(String parentID){
		
		SharedPreferences settings =getSharedPreferences("MYPREFS", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("Parent_ID", parentID);
		editor.commit();
	}
	
	public void showToast(String message){
		
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	private class LoginUser extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserFunctions userfunction = new UserFunctions();
			final JSONObject json=userfunction.checkUser(email.getText().toString().trim(), password.getText().toString().trim());
			
			// check for login response
			try{
				if(json.getString(KEY_SUCCESS)!=null){
					//Log.e("JSON", json.toString());
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res)==1){
						parent_id = json.getString("data");
						savePreference(parent_id);
						ShowDashBoard(mContext);
						finish();
					}
					else{
						
						runOnUiThread(new Runnable() {
							public void run() {
								
								try {
									Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								/*
								//
								
								AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
					            builder1.setMessage("Login Error");
					            builder1.setCancelable(true);
					            builder1.setPositiveButton("Try Again",
					                    new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int id) {
					                    dialog.cancel();
					                    email.setText("");
					                    password.setText("");
					                }
					            });
					            builder1.setNegativeButton("Exit",
					                    new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int id) {
					                    dialog.cancel();
					                    finish();
					                }
					            });

					            AlertDialog alert11 = builder1.create();
					            alert11.show();
							*/}
						});
						
						
					}
						
				}
			}catch(Exception e){
				Log.e("JSON", e.toString());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setTitle("Talking To Server");
			pDialog.setMessage("Checking Credential");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
			
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}
	}
	
	public boolean isFieldValid(){
		boolean flag = true;
		if(!(email.getText().toString().trim().length()>0)){
			email.setError("Please enter email");
			flag  = false;
		}else if(!(password.getText().toString().trim().length()>0)){
			password.setError("Please enter password");
			flag  = false;
		}
		
		return flag;
		
	}
	}

