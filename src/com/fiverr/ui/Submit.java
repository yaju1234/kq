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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.helper.UserFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Submit extends Activity{
	private static final int PICK_IMAGE = 1;
	final Context context=this;
	private String image="",video="",avg_rate="0";
	private int kid_id=0;
	//private Typeface cTypeface;
	private EditText eQuote;
	private Button  btnImageUploader,btnVideoUploader,btnSubmit;
	private String kidsImagePath;
	
	public HttpEntity resEntity;
	
	public int imgflag = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Intent getKidIdIntent = getIntent();
		kid_id = getKidIdIntent.getExtras().getInt("kid_id");
		//cTypeface = Typeface.createFromAsset(getAssets(), "GochiHand-Regular.ttf");
		eQuote =(EditText) findViewById(R.id.editText1);
		btnImageUploader = (Button) findViewById(R.id.button1);
		btnVideoUploader = (Button) findViewById(R.id.button2);
		btnSubmit =(Button) findViewById(R.id.button3);
		//btnImageUploader.setTypeface(cTypeface);
		//btnImageUploader.setVisibility(View.GONE);
		//btnSubmit.setTypeface(cTypeface);
		//btnVideoUploader.setTypeface(cTypeface);
		btnVideoUploader.setVisibility(View.GONE);
		btnSubmit.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new PostQuote().execute(""+kid_id,eQuote.getText().toString().trim(),kidsImagePath,"0");
			}
		});
		
		
		btnImageUploader.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
		 
					// set title
					alertDialogBuilder.setTitle("Subbmit Post");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Select fle type")
						.setCancelable(false)
						.setPositiveButton("Image",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
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
								// if this button is clicked, just close
								// the dialog box and do nothing
								imgflag = 2;
								Intent intent = new Intent();
								intent.setType("video/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_IMAGE);
								dialog.cancel();
							}
						});
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
					}
				});
				/*
				// Intent open gallery
				
				try {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"Error",
							Toast.LENGTH_LONG).show();
				}
				
			*/}
		//});

		
	//}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		
		case PICK_IMAGE:
			if(resultCode == Activity.RESULT_OK){
				Uri selectedImageUri = data.getData();
				String filePath = null;
				
				try{
					// OI File Manager
					String filemanagerstring= selectedImageUri.getPath();
					Log.d("Filemanager String",filemanagerstring);
					//Media Gallery
					Log.d("Selected ImagePath",""+getPath(selectedImageUri));
						kidsImagePath =getPath(selectedImageUri);
					}
				catch(Exception e){
					
				}
			}
		}
		//super.onActivityResult(requestCode, resultCode, data);
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
		ProgressDialog pDialog;
		@Override
		protected String doInBackground(String... param) {
			// TODO Auto-generated method stub
			try {
			SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
			UserFunctions userfunction = new UserFunctions();
			/*Log.d("Data Tag", "parent_id:"+settings.getString("Parent_ID", "0").toString()+" Kid_ID:"+kid_id+" Quote:"+eQuote.getText().toString().trim()+" "+image+","+video+","+avg_rate);
			JSONObject  json = userfunction.insertQuote(settings.getString("Parent_ID", "0").toString(), ""+kid_id, eQuote.getText().toString().trim(), image, video, avg_rate);			
			String res = json.getString("success");
			if(Integer.parseInt(res)==1){
				
				//Toast.makeText(Submit.this, "Quote posted", Toast.LENGTH_LONG).show();
				finish();
				
			}
			*/
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();					
		   	HttpPost httpPost = new HttpPost("http://playgroundhumor.com/demo/webservice/mywebservice.php");
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);	
				Log.e("parent_id", settings.getString("Parent_ID", "0").toString());
					entity.addPart("tag", new StringBody("inser_Quote"));
					entity.addPart("parent_id", new StringBody(settings.getString("Parent_ID", "0").toString()));
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
					Log.e("TAG", "Request  "+httpPost.getRequestLine());
					HttpResponse response;
					response = httpClient.execute(httpPost);
					resEntity = response.getEntity();
				
				
	            final String response_str = EntityUtils.toString(resEntity);
	            Log.e("TAG","Response "+ response_str);
				return response_str;		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				pDialog.dismiss();
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(Submit.this);
			pDialog.setTitle("Talking To Server");
			pDialog.setMessage("Creating Quote");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			
			try {
				JSONObject json = new JSONObject(result);
				if(json.getInt("success") ==1){
					Toast.makeText(Submit.this, "post created successfully", Toast.LENGTH_LONG).show();	
					Submit.this.finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}
