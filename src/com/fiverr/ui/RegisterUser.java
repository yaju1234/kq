package com.fiverr.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.fiverr.ui.R;
import com.fiverr.helper.UserFunctions;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterUser extends BaseView {
	
	final Context mcontext = this;
	private String Gender="m",Country;
	private Spinner country;
	public ProgressDialog pDialog;
	private EditText username,email,password,state,city,phone;
	private Button Submit;
	 private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    List<String> list = new ArrayList<String>();
    int pos;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeruser);
		username=(EditText) findViewById(R.id.etusername);
		password = (EditText) findViewById(R.id.etpassword);
		email =(EditText) findViewById(R.id.etemail);
		country = (Spinner) findViewById(R.id.spinnerCountry);
		city =(EditText) findViewById(R.id.etcity);
		state =(EditText) findViewById(R.id.etstate);
		phone = (EditText) findViewById(R.id.etphone);
		Submit = (Button) findViewById(R.id.btnsbmt);
		
		String[] regionsArray = getResources().getStringArray(R.array.country_arrays);
		for(int i=0; i<regionsArray.length; i++){
			list.add(regionsArray[i]);
			if(regionsArray[i].equalsIgnoreCase("United States")){
				pos = i;
			}
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegisterUser.this, android.R.layout.simple_spinner_item,list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		country.setAdapter(dataAdapter);
		country.setSelection(pos);
		Country = list.get(pos);
		
		country.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				Country = list.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		Submit.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					
				
				if(isOnline()){
					if(registrationValidation(username.getText().toString().trim(),password.getText().toString().trim(),email.getText().toString().trim(),
							phone.getText().toString().trim(),Gender,Country,state.getText().toString().trim(),city.getText().toString().trim())){
						new CreateUser().execute();
					}
					
					
				}else{
						Toast.makeText(mcontext, "No Internet connection", Toast.LENGTH_LONG).show();
					}
				
			}
		});
		
		
	}
	
	public boolean registrationValidation(String st_username, String st_password,
			String st_email, String st_phone, String st_Gender, String st_Country,String st_state, String st_city) {
		if(st_username.length()==0){
			username.setError("Please Enter User Name");
			username.requestFocus();
			return false;
		}else if(st_password.length()==0){
			password.setError("Please Enter Your Password");
			password.requestFocus();
			return false;
		}else if(st_password.length()<5){
			password.setError("Please Enter at least 5 Digit Password");
			password.requestFocus();
			return false;
		}else if(st_email.length()==0){
			email.setError("Please Enter Your Email");
			email.requestFocus();
			return false;
		}else if(!isValidEmail(st_email)){
			email.setError("Please enter Valid Email");
			email.setSelection(st_email.length());
			return false;
		}else if(st_phone.length()==0){
			phone.setError("Please Enter Your Phone No");
			phone.requestFocus();
			return false;
		}else if(st_phone.length()<10){
			phone.setError("Please enter 10 Digit Ph. No");
			phone.setSelection(st_phone.length());
			return false;
		}else if(st_Country.length()==0){
			Toast.makeText(mcontext, "Please Choose a Country", Toast.LENGTH_LONG).show();
			return false;
		}else if(st_state.length()==0){
			state.setError("Please Enter Your State");
			state.requestFocus();
			return false;
		}else if(st_city.length()==0){
			city.setError("Please Enter Your City");
			city.requestFocus();
			return false;
		}
		return true;
	}
	public  boolean isValidEmail(String email) {		
		return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
	}	
	
	
	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
switch (view.getId()) {
		case R.id.radio0:
			if (checked)
				Gender = "m";
			break;
		case R.id.radio1:
			if (checked)
				Gender = "f";
			break;
		}
	}

	private boolean isOnline()  {
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    return (ni != null && ni.isConnected());
	}


class CreateUser extends AsyncTask<String, String, String>{

	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		showProgressDailog();
	
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		try{
		Log.d("TEST", Gender +" "+Country);
		String name =username.getText().toString().trim();
		String pemail = email.getText().toString().trim();
		String ppassword = password.getText().toString().trim();
		String pphone=phone.getText().toString().trim();
		String pcountry =Country;
		String pstate =state.getText().toString().trim();
		String pcity = city.getText().toString().trim();
		UserFunctions userfunction = new UserFunctions();
		JSONObject json= userfunction.registerUser(name, pemail, ppassword, pphone,Gender, pcountry, pstate,pcity);
		String res = null ;
		if(json!=null){
			if(json.getString(KEY_SUCCESS)!=null){
				Log.d("JSON", json.toString());
				res = json.getString(KEY_SUCCESS);
				if(Integer.parseInt(res)==1){
					if(pDialog.isShowing())
						pDialog.dismiss();
					finish();
				}
				else{
					res = json.getString("message");
				
				}
					
			}	
		}
		return res;	
		}catch(Exception e){
			e.printStackTrace();
			return null;	
		}
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		dissmissProgressDialog();
		if(Integer.parseInt(result)==1){
			Toast.makeText(mcontext, "Thank you for your registration. A activation mail will be sent", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
		}
	}

	}
}

