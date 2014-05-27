package com.fiverr.ui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.helper.ImageLoader;
import com.fiverr.helper.UserFunctions;
import com.fiverr.model.Kid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class EditKidProfile extends Activity {
	private List<Kid> KidArray;
	private Kid kid;
	private String KidID;
	private static final int PICK_IMAGE = 1;
	private Typeface cTypeFace;
	private EditText KidName, NickName, Age, About;
	private ImageView iv_profile_pic;
	private String Sex="m",Imagename="";
	private Button btnUploadImage, btnSubmit;
	private String profileImagePath= "";
	private int serverResponseCode;
	private String uploadServerURL = "http://playgroundhumor.com/demo/webservice/image_upload.php";
	final Context mContext = this;
	ProgressDialog pDialog;
	private ImageLoader imgLoader;
	public HttpEntity resEntity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kid_profile_edit);
		Intent intent = getIntent();
		KidID = intent.getIntExtra("kid_id", 0)+"".trim();
		KidArray = new ArrayList<Kid>();
		cTypeFace = Typeface.createFromAsset(getAssets(),
				"GochiHand-Regular.ttf");
		KidName = (EditText) findViewById(R.id.editText1);
		NickName = (EditText) findViewById(R.id.editText2);
		Age = (EditText) findViewById(R.id.editText3);
		About = (EditText) findViewById(R.id.editText4);
		iv_profile_pic=(ImageView)findViewById(R.id.iv_profile_pic);
		btnUploadImage = (Button) findViewById(R.id.button1);
		btnUploadImage.setTypeface(cTypeFace);
		//btnUploadImage.setVisibility(View.GONE);
		imgLoader = new ImageLoader(getApplicationContext());
		btnUploadImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
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
				
			}
		});
		btnSubmit = (Button) findViewById(R.id.button2);
		btnSubmit.setTypeface(cTypeFace);
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new UpdateKidProfile().execute();
				
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new getKidProfileData().execute();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {

		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				String filePath = null;

				try {
					// OI File Manager
					String filemanagerstring = selectedImageUri.getPath();
					Log.d("Filemanager String", filemanagerstring);
					// Media Gallery
					Log.d("Selected ImagePath", "" + getPath(selectedImageUri));
					profileImagePath = getPath(selectedImageUri);
				} catch (Exception e) {

				}
			}
		}
		// super.onActivityResult(requestCode, resultCode, data);
	}
	 public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        @SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }
	/*public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public int uploadFile(String sourcFileURI) {

		String filename = sourcFileURI;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineend = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourcFileURI);

		if (!sourceFile.isFile()) {
			// Do output
			Log.e("Upload File", "File doesn not exist");
			return 0;
		} else {

			try {
				// open a URL connection to the service url
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(uploadServerURL);

				// Open a HTTP connecion to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow iNput
				conn.setDoOutput(true); // allow output
				conn.setUseCaches(false); // Dont' use cache
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Cotent-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", filename);
				conn.setRequestProperty("tag", "profileImage");
				conn.setRequestProperty("file_path",
						"http://playgroundhumor.com/demo/webservice/user_images/myandroid42/images");
				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineend);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ filename + "\"" + lineend);
				dos.writeBytes(lineend);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data neccesary after file data
				dos.writeBytes(lineend);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineend);

				// response from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverMessage = conn.getResponseMessage();
				Log.i("uplodFile", "HTTP Response is :" + serverResponseCode
						+ ":" + serverMessage);

				if (serverResponseCode == 200) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							// Toast.makeText(KidProfile.this,
							// "File Upload Complete.",
							// Toast.LENGTH_SHORT).show();
						}
					});
				}

				// close the streams//
				fileInputStream.close();
				dos.flush();
				dos.close();
			} catch (Exception ex) {

			} finally {
			}
			return 1;
		}
	}*/

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.radio0:
			if (checked)
				// Sex Male
				Sex = "m";
			break;
		case R.id.radio1:
			if (checked)
				// Sex Female
				Sex = "f";
			break;
		}
	}
	
	private boolean isOnline()  {
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    return (ni != null && ni.isConnected());
	}
	
	class getKidProfileData extends AsyncTask<String, String, String>{
		JSONArray jArray;
		JSONObject json;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setTitle("Talking To Server");
			pDialog.setMessage("Searching Kid Detail");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		KidName.setText(KidArray.get(0).getName());
		NickName.setText(KidArray.get(0).getNick_Name());
		Age.setText(KidArray.get(0).getAge());
		About.setText(KidArray.get(0).getAbout());
		imgLoader.DisplayImage("http://playgroundhumor.com/demo"+KidArray.get(0).getImage(),  iv_profile_pic);
		pDialog.dismiss();
	}
	}
	
	////////////////////////////////////////////////////////////
	 class UpdateKidProfile extends AsyncTask<String, String, String> {

			@Override
			protected String doInBackground(String... param) {
				// TODO Auto-generated method stub
				try {
				UserFunctions userFunction = new UserFunctions();
				SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
				Log.d("Parent_ID_STRING",settings.getString("Parent_ID", "0"));
				Log.d("Kid ID + Parent ID",""+KidArray.get(0).getKid_ID()+"++"+KidArray.get(0).getParen_ID());
				//JSONObject json= userFunction.editKidProfile(KidArray.get(0).getKid_ID()+"".trim(), KidArray.get(0).getParen_ID()+"".trim(), KidName.getText().toString(), NickName.getText().toString(), Sex, Age.getText().toString(), About.getText().toString(), Imagename);
				//
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();					
			   	HttpPost httpPost = new HttpPost("http://playgroundhumor.com/demo/webservice/mywebservice.php");
					MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);	
					Log.e("parent_id", settings.getString("Parent_ID", "0").toString());
						entity.addPart("tag", new StringBody("update_kid_profile"));
						entity.addPart("parent_id", new StringBody(""+KidArray.get(0).getParen_ID()));
					    Log.e("kid_id", ""+KidArray.get(0).getKid_ID());
						entity.addPart("kids_id", new StringBody(""+KidArray.get(0).getKid_ID()));
						Log.e("name", KidName.getText().toString());
						entity.addPart("name", new StringBody(KidName.getText().toString()));
						Log.e("nick_name", NickName.getText().toString());
						entity.addPart("nick_name", new StringBody(NickName.getText().toString()));	
						Log.e("sex", Sex);
						entity.addPart("sex", new StringBody(Sex));
						Log.e("age", Age.getText().toString());
						entity.addPart("avg_rate", new StringBody(Age.getText().toString()));
						Log.e("about", About.getText().toString());
						entity.addPart("about", new StringBody(About.getText().toString()));
						Log.e("image", profileImagePath);
						entity.addPart("image", new FileBody(new File(profileImagePath)));
						httpPost.setEntity(entity);
						Log.e("TAG", "Request  "+httpPost.getRequestLine());
						
						HttpResponse response;
						response = httpClient.execute(httpPost);
						resEntity = response.getEntity();
				//uploadFile(profileImagePath);

				//check for response
				/*
					if(json.getString("success")!=null){
						
						if(Integer.parseInt(json.getString("success"))==0){
							Log.d("JSON", json.getString("message"));
						}else {
							runOnUiThread(new Runnable() {
								public void run() {
									//
									
									AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
						            builder1.setMessage("Profile Updated");
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
					
				*/
						} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
				pDialog = new ProgressDialog(mContext);
				pDialog.setTitle("Talking To Server");
				pDialog.setMessage("Updating Kid Profile");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			@Override
			protected void onPostExecute(String result) {
				pDialog.dismiss();
				
				try {
					JSONObject json = new JSONObject(result);
					if(json.getInt("success") ==1){
						Toast.makeText(EditKidProfile.this, "Profile Edited successfully", Toast.LENGTH_LONG).show();					
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	
}
