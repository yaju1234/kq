package com.fiverr.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fiverr.constant.Constant;
import com.fiverr.ui.BaseAcivity;

public class UserInfo {	
	private BaseAcivity base;
	public SharedPreferences sharedPreferences;
	public int useid = 0;
	public UserInfo(BaseAcivity b){
		base = b;
		sharedPreferences = base.getSharedPreferences(Constant.VALUE.MYPREFS.name(), Context.MODE_PRIVATE);
		useid  = sharedPreferences.getInt(Constant.VALUE.USER_ID.name(), useid);
	}			
	public void setUsrId(int useid){
		this.useid = useid;
		Log.e("useid", ""+useid);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(Constant.VALUE.USER_ID.name(), useid);
		editor.commit();
	}

}
