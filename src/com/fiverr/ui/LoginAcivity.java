package com.fiverr.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.ui.R;
import com.fiverr.constant.Constant;
import com.fiverr.helper.UserFunctions;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginAcivity extends BaseAcivity{
	private  int parent_id;
	private Button btn_login, btn_sign_up;
	private EditText et_username, et_password;
	private static String KEY_SUCCESS = "success";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);		
		btn_sign_up.setOnClickListener(this);
		btn_login.setOnClickListener(this);
	}	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if(isNetworkAvailable()){
				if(isFieldValid()){
					new LoginUser().execute();
				}
			}else{
				showToast(Constant.NO_NETWORK_MSG);				
			}		
			break;
		case R.id.btn_sign_up:
			Intent i = new Intent(LoginAcivity.this, RegisterUser.class);
			startActivity(i);
			break;		
		}
	}		
	private class LoginUser extends AsyncTask<String, String, String> {
	
		protected String doInBackground(String... params) {
			UserFunctions userfunction = new UserFunctions();
			final JSONObject json=userfunction.checkUser(et_username.getText().toString().trim(), et_password.getText().toString().trim());
			try{
				if(json.getString(KEY_SUCCESS)!=null){
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res)==1){
						parent_id = Integer.parseInt(json.getString("data"));
						Log.e("parent_id", ""+parent_id);
						app.getInfo().setUsrId(parent_id);
						Intent i = new Intent(LoginAcivity.this,Dashboard.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);
						finish();
					}
					else{						
						runOnUiThread(new Runnable() {
							public void run() {								
								try {
									showToast(json.getString("message"));									
								} catch (JSONException e) {
									e.printStackTrace();
								}								
								}
						});						
					}						
				}
			}catch(Exception e){}
			return null;
		}		
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDailog();
		}		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dissmissProgressDialog();
		}
	}	
	public boolean isFieldValid(){
		boolean flag = true;
		if(!(et_username.getText().toString().trim().length()>0)){
			et_username.setError("Please enter email");
			flag  = false;
		}else if(!(et_password.getText().toString().trim().length()>0)){
			et_password.setError("Please enter password");
			flag  = false;
		}		
		return flag;
		
		}
	}

