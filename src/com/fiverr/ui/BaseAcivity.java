package com.fiverr.ui;

import com.fiverr.application.PlayGroundHumer;
import com.fiverr.application.UserInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Toast;

public class BaseAcivity extends Activity implements OnClickListener,OnItemSelectedListener {
	private ProgressDialog pDialog;
	private InputMethodManager imm = null;
	public boolean flag = false;
	public PlayGroundHumer app;	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!flag){
			app = (PlayGroundHumer) getApplication();
			app.setInfo(new UserInfo(this));
		}
	}
	public void showProgressDailog() {
		runOnUiThread(new Runnable() {		
			public void run() {
				pDialog = new ProgressDialog(BaseAcivity.this);
				pDialog.setTitle("Talking To Server");
				pDialog.setMessage("Checking Credential");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();

			}
		});
	}
	public void dissmissProgressDialog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (pDialog.isShowing()) {
					pDialog.dismiss();
				}

			}
		});
	}
	public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork!=null) {
        	return true;
        }
        return false;
    }		
	public void hideKeyBoard(EditText et){
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}	
	public void showKeyBoard() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
	public void onClick(View v) {}	
	public void showToast(String msg){
		Toast.makeText(BaseAcivity.this, msg, Toast.LENGTH_LONG).show();
	}
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {}
	public void onNothingSelected(AdapterView<?> arg0) {}	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
