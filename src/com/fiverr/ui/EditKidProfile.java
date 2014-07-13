package com.fiverr.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fiverr.helper.Constant;
import com.fiverr.helper.ImageLoader;
import com.fiverr.helper.UserFunctions;
import com.fiverr.model.Kid;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class EditKidProfile extends BaseAcivity {
	private ArrayList<Kid> KidArray = new ArrayList<Kid>();
	private Kid kid;
	private String KidID;
	private static final int PICK_IMAGE = 1;
	private EditText KidName, NickName, Age, About;
	private ImageView iv_profile_pic;
	private String Sex="m";
	private Button btnUploadImage, btnSubmit;
	private String profileImagePath= "";
	private ImageLoader imgLoader;
	public HttpEntity resEntity;
	private String is_image_change = "0";
	private RadioButton male,female;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kid_profile_edit);
		Intent intent = getIntent();
		KidID = intent.getIntExtra("kid_id", 0)+"".trim();
		
		KidName = (EditText) findViewById(R.id.editText1);
		NickName = (EditText) findViewById(R.id.editText2);
		Age = (EditText) findViewById(R.id.editText3);
		About = (EditText) findViewById(R.id.editText4);
		iv_profile_pic=(ImageView)findViewById(R.id.iv_profile_pic);
		btnUploadImage = (Button) findViewById(R.id.button1);
		male = (RadioButton)findViewById(R.id.radio0);
		female = (RadioButton)findViewById(R.id.radio1);
		imgLoader = new ImageLoader(getApplicationContext());		
		new getKidProfileData().execute();		
		btnUploadImage.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);		
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			try {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
			} catch (Exception e) {
				showToast("Error");
			}
			break;
			
		case R.id.button2:
			new UpdateKidProfile().execute();
			break;

		default:
			break;
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				try {					
					profileImagePath = getPath(selectedImageUri);
					File imgFile = new  File(profileImagePath);
					if(imgFile.exists()){
					    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					    iv_profile_pic.setImageBitmap(myBitmap);
					    is_image_change = "1";
					}
				} catch (Exception e) {	}
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
	
	class getKidProfileData extends AsyncTask<String, String, String>{
		JSONArray jArray;
		JSONObject json;			
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDailog();
		}		
		protected String doInBackground(String... params) {
			UserFunctions userfunction = new UserFunctions();
			json =userfunction.getKidDetails(KidID);
			try {
				if(json.getString("success")!=null){
					
					if(Integer.parseInt(json.getString("success"))==0){
						Log.d("JSON", json.getString("message"));
					}else {
						jArray = json.getJSONArray("data");
						for(int i=0;i<jArray.length();i++){
							JSONObject jOBJ = jArray.getJSONObject(i);
							kid = new Kid();
							kid.setKid_ID(jOBJ.getInt("id"));
							kid.setName(jOBJ.getString("name"));
							kid.setNick_Name(jOBJ.getString("nick_name"));
							kid.setParen_ID(jOBJ.getInt("parent_id"));
							kid.setAbout(jOBJ.getString("about"));
							kid.setGender(jOBJ.getString("gender"));
							kid.setAge(jOBJ.getString("age"));
							kid.setImage(jOBJ.getString("image"));
							KidArray.add(kid);
						}
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}			
			return null;
		}	
	
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		KidName.setText(KidArray.get(0).getName());
		NickName.setText(KidArray.get(0).getNick_Name());
		Age.setText(KidArray.get(0).getAge());
		About.setText(KidArray.get(0).getAbout());
		if(KidArray.get(0).getGender().equals("m")){
			male.setChecked(true);
			female.setChecked(false);
		}else{
			female.setChecked(true);
			male.setChecked(false);
		}
		imgLoader.DisplayImage("http://playgroundhumor.com/demo"+KidArray.get(0).getImage(),  iv_profile_pic);
		dissmissProgressDialog();
	}
}
	
	 class UpdateKidProfile extends AsyncTask<String, String, String> {			
			protected String doInBackground(String... param) {				
				try {
				SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
					HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://playgroundhumor.com/demo/webservice/mywebservice.php");
					MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);	
					Log.e("parent_id", settings.getString("Parent_ID", "0").toString());
						entity.addPart("tag", new StringBody("update_kid_profile"));
						entity.addPart("parent_id", new StringBody(""+KidArray.get(0).getParen_ID()));					   
						entity.addPart("kids_id", new StringBody(""+KidArray.get(0).getKid_ID()));						
						entity.addPart("name", new StringBody(KidName.getText().toString()));						
						entity.addPart("nick_name", new StringBody(NickName.getText().toString()));						
						entity.addPart("sex", new StringBody(Sex));					
						entity.addPart("age", new StringBody(Age.getText().toString()));						
						entity.addPart("about", new StringBody(About.getText().toString()));						
						entity.addPart("is_image_change", new StringBody(is_image_change));						if(is_image_change.equals("1")){							
							entity.addPart("file", new FileBody(new File(profileImagePath)));
						}						
						httpPost.setEntity(entity);						
						HttpResponse response;
						response = httpClient.execute(httpPost);
						resEntity = response.getEntity();
						final String response_str = EntityUtils.toString(resEntity);
			            Log.e("TAG","Response "+ response_str);
						return response_str;				
						} catch (Exception e) {
					e.printStackTrace();
					return null;
				}				
			}		
			protected void onPreExecute() {
				super.onPreExecute();				
				showProgressDailog();
			}			
			protected void onPostExecute(String result) {
				dissmissProgressDialog();				
				try {
					JSONObject json = new JSONObject(result);
					if(json.getInt("success") ==1){
						Toast.makeText(EditKidProfile.this, "Profile Edited successfully", Toast.LENGTH_LONG).show();
						finish();
						Constant.mEditKidFlag = true ;
					}else{
						Toast.makeText(EditKidProfile.this, "Profile not Edited, please try again", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}	
}
