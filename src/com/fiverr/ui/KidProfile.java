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
import com.fiverr.helper.Constant;
import com.fiverr.helper.UserFunctions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
public class KidProfile extends BaseAcivity {
	
	private static final int PICK_IMAGE = 1;
	private String Sex="m",Imagename="";
	private EditText KidName, NickName, Age, About;
	private Button btnUploadImage, btnSubmit;
	
	final Context mContext=this;
	private String profileImagePath;
	ProgressDialog pDialog;
	public HttpEntity resEntity;
	private ImageView imgKid;	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kid_profile);	
		KidName = (EditText) findViewById(R.id.editText1);
		NickName = (EditText) findViewById(R.id.editText2);
		Age = (EditText) findViewById(R.id.editText3);
		About = (EditText) findViewById(R.id.editText4);		
		btnUploadImage = (Button) findViewById(R.id.button1);
		imgKid = (ImageView)findViewById(R.id.imgKid);;
		
		btnUploadImage.setOnClickListener(new OnClickListener() {		
			public void onClick(View arg0) {				
				try {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
				} catch (Exception e) {
					
					Toast.makeText(getApplicationContext(),	"Error",Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		btnSubmit = (Button) findViewById(R.id.button2);		
		btnSubmit.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				if(isNetworkAvailable()){
						if(isValid()){
					new ImageUploadTask().execute(""+app.getInfo().useid, KidName.getText().toString().trim(),NickName.getText().toString().trim(), Sex, Age.getText().toString().trim(), About.getText().toString().trim(),profileImagePath);
					}
					}else{
					
					Toast.makeText(mContext, "No Internet connection", Toast.LENGTH_LONG).show();
				}

			}
		});
		
	}	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		switch(requestCode){		
		case PICK_IMAGE:
			if(resultCode == Activity.RESULT_OK){
				Uri selectedImageUri = data.getData();			
				try{			
						profileImagePath =getPath(selectedImageUri);						
						File imgFile = new  File(profileImagePath);
						if(imgFile.exists()){
						    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
						    imgKid.setImageBitmap(myBitmap);
						}
					}
				catch(Exception e){
					
				}
			}
		}
		
	}	
	 public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	       Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
		case R.id.radio0:
			if (checked)
				Sex = "m";
			break;
		case R.id.radio1:
			if (checked)
				Sex = "f";
			break;
		}
	}
	

	 class CreateKidProfile extends AsyncTask<String, String, String> {		
		protected String doInBackground(String... arg0) {			
			UserFunctions userFunction = new UserFunctions();
			JSONObject json= userFunction.createKid(""+app.getInfo().useid, KidName.getText().toString().trim(),
					NickName.getText().toString().trim(), Sex, Age.getText().toString().trim(), About.getText().toString().trim(),Imagename);
			
			try {
				if(json.getString("success")!=null){					
					if(Integer.parseInt(json.getString("success"))==0){	}else {
						runOnUiThread(new Runnable() {
							public void run() {
								AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
					            builder1.setMessage("Submition successful");
					            builder1.setCancelable(true);
					            builder1.setPositiveButton("OK",
					                    new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int id) {
					                    dialog.cancel();
					                    finish();
					                }
					            });
					           

					            AlertDialog alert11 = builder1.create();
					            alert11.show();
							}
						});
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}	
		protected void onPreExecute() {
			super.onPreExecute();			
			pDialog = new ProgressDialog(mContext);
			pDialog.setTitle("Talking To Server");
			pDialog.setMessage("Creating Kid Profile");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}	
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);
			pDialog.dismiss();
		}
	}	
	class ImageUploadTask extends AsyncTask<String, Void, String> {		
		protected String doInBackground(String... param) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
			 	HttpPost httpPost = new HttpPost("http://playgroundhumor.com/demo/webservice/mywebservice.php");
					MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);					
					    entity.addPart("tag", new StringBody("inser_kid_profile"));
						entity.addPart("parent_id", new StringBody(param[0]));					  
						entity.addPart("name", new StringBody(param[1]));						
						entity.addPart("nick_name", new StringBody(param[2]));						
						entity.addPart("sex", new StringBody(param[3]));						
						entity.addPart("age", new StringBody(param[4]));					
						entity.addPart("about", new StringBody(param[5]));						
						entity.addPart("file", new FileBody(new File(param[6])));				
					httpPost.setEntity(entity);
					Log.e("TAG", "Request  "+httpPost.getRequestLine());
					HttpResponse response;
					response = httpClient.execute(httpPost);
					resEntity = response.getEntity();				
	            final String response_str = EntityUtils.toString(resEntity);
	           return response_str;
			} catch (Exception e) {	
				pDialog.dismiss();
				return null;
			}
		}
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setTitle("Creating Kid Profile");
			pDialog.setMessage("Please Wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}		
		protected void onPostExecute(String sResponse) {				
		if(sResponse!=null){
			pDialog.dismiss();			
			try {
				JSONObject json = new JSONObject(sResponse);
				if(json.getInt("success") ==1){
					Toast.makeText(KidProfile.this, "Profile created successfully", Toast.LENGTH_LONG).show();
					Constant.mEditKidFlag = true ;
					KidProfile.this.finish();
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
			}
	}
 
 public boolean isValid(){
	 boolean flag = true;
	 if(!(KidName.getText().toString().trim().length()>0)){
		 KidName.setError("please enter kidname");
		 flag = false;
	 }else if(!(NickName.getText().toString().trim().length()>0)){
		 NickName.setError("please enter NickName");
		 flag = false;
	 }else if(!(Age.getText().toString().trim().length()>0)){
		 Age.setError("please enter Age");
		 flag = false;
	 }else if(!(About.getText().toString().trim().length()>0)){
		 About.setError("please enter About kid");
		 flag = false;
	 }else if(!(profileImagePath!=null)){
		 Toast.makeText(KidProfile.this, "Please select a profile image", Toast.LENGTH_LONG).show();
		 flag = false;
	 }
	 return flag;
 }


}
