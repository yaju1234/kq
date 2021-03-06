package com.fiverr.ui;


import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;

public class Submit extends BaseAcivity{
	private static final int PICK_IMAGE = 1;
	final Context context=this;
	private int kid_id=0;
	private EditText eQuote;
	private Button  btnVideoUploader,btnSubmit;
	private String kidsImagePath;	
	public HttpEntity resEntity;
	private CustomVideoView customVideoView;
	private ImageView imgKid;	
	public int imgflag = 0;
	public boolean flag = false; 	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Intent getKidIdIntent = getIntent();
		kid_id = getKidIdIntent.getExtras().getInt("kid_id");
		eQuote =(EditText) findViewById(R.id.editText1);
		btnVideoUploader = (Button) findViewById(R.id.button2);
		btnSubmit =(Button) findViewById(R.id.button3);
		customVideoView = (CustomVideoView)findViewById(R.id.videoQuote);
		imgKid = (ImageView)findViewById(R.id.imgKid);
		btnSubmit.setOnClickListener( new OnClickListener() {			
			
			public void onClick(View arg0) {
			
				if(eQuote.getText().toString().trim().length()>0){
					new PostQuote().execute(""+kid_id,eQuote.getText().toString().trim(),kidsImagePath,"0");	
				}else{
					showToast("Please enter Post");
				}
				
			}
		});
		
		
		btnVideoUploader.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);		 
				alertDialogBuilder.setTitle("Subbmit Post");					
					alertDialogBuilder
						.setMessage("Select fle type")
						.setCancelable(true)
						.setPositiveButton("Image",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {								
								imgflag = 1;
								Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
								dialog.cancel();
							}
						  })
						.setNegativeButton("Video",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								imgflag = 2;
								Intent intent = new Intent();
								intent.setType("video/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_IMAGE);
								dialog.cancel();
							}
						});	 
						
						AlertDialog alertDialog = alertDialogBuilder.create();						
						alertDialog.show();
					}
				});
				}		
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		
		case PICK_IMAGE:
			if(resultCode == Activity.RESULT_OK){
				Uri selectedImageUri = data.getData();				
				try{								
						kidsImagePath =getPath(selectedImageUri);
						File imgFile = new  File(kidsImagePath);
						if(imgflag == 1){
							customVideoView.setVisibility(View.GONE);
							imgKid.setVisibility(View.VISIBLE);
							btnVideoUploader.setVisibility(View.GONE);
							if(imgFile.exists()){
							    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
							    imgKid.setImageBitmap(myBitmap);
							}
						}else{
							customVideoView.setVisibility(View.VISIBLE);
							imgKid.setVisibility(View.GONE);
							btnVideoUploader.setVisibility(View.GONE);
							customVideoView.setVideoURI(Uri.parse((kidsImagePath)));
					        MediaController mc = new MediaController(Submit.this);
					        customVideoView.setMediaController(mc);
					        customVideoView.requestFocus();
					        mc.show();
					        customVideoView.seekTo(100);
						}
					}
				catch(Exception e){
					
				}
			}
		}
		
	}	
	 public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        @SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }
	class PostQuote extends AsyncTask<String, String, String>{	
		protected String doInBackground(String... param) {		
			try {
			HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://playgroundhumor.com/demo/webservice/mywebservice.php");
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);					
					entity.addPart("tag", new StringBody("inser_Quote"));
					entity.addPart("parent_id", new StringBody(""+app.info.useid));
					entity.addPart("kid_id", new StringBody(param[0]));
					entity.addPart("quote", new StringBody(param[1]));
					if(imgflag == 1){
						entity.addPart("file", new FileBody(new File(param[2])));
						entity.addPart("flaImg", new StringBody("1"));
					}					
					if(imgflag == 2){
						entity.addPart("file", new FileBody(new File(param[2])));
						entity.addPart("flaImg", new StringBody("2"));
					}						
					entity.addPart("avg_rate", new StringBody(param[3]));
					httpPost.setEntity(entity);
					HttpResponse response;
					response = httpClient.execute(httpPost);
					resEntity = response.getEntity();				
	            final String response_str = EntityUtils.toString(resEntity);
	            return response_str;		
			} catch (Exception e) {				
				dissmissProgressDialog();
				e.printStackTrace();
				return null;
			}
		}		
		protected void onPreExecute() {
			showProgressDailog();
			super.onPreExecute();
		}		
		protected void onPostExecute(String result) {
			dissmissProgressDialog();			
			try {
				JSONObject json = new JSONObject(result);
				if(json.getInt("success") ==1){
					showToast("successfully upload ..waiting for admin approval");	
					Submit.this.finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}
