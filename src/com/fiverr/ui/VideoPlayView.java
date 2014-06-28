package com.fiverr.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoPlayView extends Activity{
	String url;
	private VideoView vv;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		vv = (VideoView)findViewById(R.id.videoView1);
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			url = bundle.getString("url");
		}
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.VISIBLE);
		vv.setVideoURI(Uri.parse((url)));
        MediaController mc = new MediaController(VideoPlayView.this);
        vv.setMediaController(mc);
        vv.requestFocus();
        mc.show();
		vv.start();
		vv.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				
				progressBar.setVisibility(View.GONE);
				
			}
		});
	}
	

}
