package com.fiverr.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.ui.R;
import com.fiverr.helper.UserFunctions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends BaseView{

	private static String parent_id;
	final Context mContext =this;
	private Button Login, Register;
	private EditText email, password;
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Login = (Button) findViewById(R.id.btn_login);
		Register = (Button) findViewById(R.id.btn_sign_up);
		email = (EditText) findViewById(R.id.et_username);
		password = (EditText) findViewById(R.id.et_password);
		
		Register.setOnClickListener(this);
		Login.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_login:

			if(isOnline()){
				if(isFieldValid()){
					new LoginUser().execute();
				}
			}
			
			else{
				Toast.makeText(mContext, "No Internet connection", Toast.LENGTH_LONG).show();
			}
		
			break;
		case R.id.btn_sign_up:
			Intent i = new Intent(Login.this, RegisterUser.class);
			startActivity(i);
			break;

		default:
			break;
		}
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
			UserFunctions userfunction = new UserFunctions();
			final JSONObject json=userfunction.checkUser(email.getText().toString().trim(), password.getText().toString().trim());
			
			try{
				if(json.getString(KEY_SUCCESS)!=null){
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
									e.printStackTrace();
								}
								
								}
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
			super.onPreExecute();
			showProgressDailog();
		}
			
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dissmissProgressDialog();
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

