package com.fiverr.application;

import android.app.Application;

public class PlayGroundHumer extends Application{
	
	public UserInfo info;

	public UserInfo getInfo() {
		return info;
	}

	public void setInfo(UserInfo info) {
		this.info = info;
	}
	

}
