package com.fiverr.ui;

import com.fiverr.helper.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;


public class ImageDetailScreen extends Activity{

	private String url = null;
	private ImageLoader imageloader;
	private ImageView iv_kid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail);
		imageloader = new ImageLoader(getApplicationContext());
		
		url = getIntent().getExtras().getString("url");
		iv_kid = (ImageView)findViewById(R.id.iv_kid);
		
		imageloader.DisplayImage(url, iv_kid);
	}
}
