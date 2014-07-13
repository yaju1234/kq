package com.fiverr.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONObject;
import com.fiverr.ui.R;
import com.fiverr.helper.UserFunctions;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class RegisterUser extends BaseAcivity {	
	
	private String Gender="m",Country;
	private Spinner country;
	public ProgressDialog pDialog;
	private EditText username,email,password,state,city,phone;
	private Button btn_submit;
	private static String KEY_SUCCESS = "success";
    private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public List<String> list = new ArrayList<String>();
    private int pos;
    
	
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
		btn_submit = (Button) findViewById(R.id.btn_submit);
		
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
		country.setOnItemSelectedListener(this);
		btn_submit.setOnClickListener(this);
		
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
			showToast("Please Choose a Country");
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

class CreateUser extends AsyncTask<String, String, String>{
	boolean flag = false;	
	protected void onPreExecute() {
		super.onPreExecute();
		showProgressDailog();	
	}	
	protected String doInBackground(String... arg0) {
		try{		
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
			if(json.getInt(KEY_SUCCESS)== 1){
				flag = true;
				finish();
			}
			res = json.getString("message");
			}
		return res;	
		}catch(Exception e){
			e.printStackTrace();
			return null;	
		}		
	}
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		dissmissProgressDialog();
		if(flag){
			showToast("Thank you for your registration. A activation mail will be sent");
		}else{
			showToast(result);
			}
		}

	}


	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btn_submit:
		if(isNetworkAvailable()){
			if(registrationValidation(username.getText().toString().trim(),password.getText().toString().trim(),email.getText().toString().trim(),
					phone.getText().toString().trim(),Gender,Country,state.getText().toString().trim(),city.getText().toString().trim())){
				new CreateUser().execute();
			}	
			
		}else{
			showToast("No Internet connection");
			}
		break;

	}
	}
	
	public void onItemSelected(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
		Country = list.get(arg2);
	}	
}

