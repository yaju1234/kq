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
public class KidProfile extends Activity {
	
	private static final int PICK_IMAGE = 1;
	private String Sex="m",Imagename="";
	private EditText KidName, NickName, Age, About;
	private Button btnUploadImage, btnSubmit;
	
	//private Typeface cTypeFace;
	private String Parent_ID;private int serverResponseCode;
	private String uploadServerURL="http://playgroundhumor.com/demo/webservice/image_upload.php";
	final Context mContext=this;
	private String profileImagePath;
	ProgressDialog pDialog;
	public HttpEntity resEntity;
private ImageView imgKid;
	// private RadioGroup radioGropup;
	// private RadioButton mRadioBtn,fRadioBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kid_profile);
	/*	cTypeFace = Typeface.createFromAsset(getAssets(),
				"GochiHand-Regular.ttf");*/
		KidName = (EditText) findViewById(R.id.editText1);
		NickName = (EditText) findViewById(R.id.editText2);
		Age = (EditText) findViewById(R.id.editText3);
		About = (EditText) findViewById(R.id.editText4);
		// radioGropup = (RadioGroup) findViewById(R.id.radioGroup1);
		btnUploadImage = (Button) findViewById(R.id.button1);
		imgKid = (ImageView)findViewById(R.id.imgKid);;
		
		//btnUploadImage.setTypeface(cTypeFace);
		//-------------------------Setting Visibility----------------------------//
		//btnUploadImage.setVisibility(View.GONE);
		// code for opening gallery to choose image file
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
		//btnSubmit.setTypeface(cTypeFace); 
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isOnline()){
					//new CreateKidProfile().execute();
					//UserFunctions userFunction = new UserFunctions();
					SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
					Log.d("Parent_ID_STRING1111",settings.getString("Parent_ID", "0"));
					//JSONObject json= userFunction.createKid(settings.getString("Parent_ID", "0"), KidName.getText().toString().trim(),
						//	NickName.getText().toString().trim(), Sex, Age.getText().toString().trim(), About.getText().toString().trim(),Imagename);
					//
					if(isValid()){
					new ImageUploadTask().execute(settings.getString("Parent_ID", "0"), KidName.getText().toString().trim(),NickName.getText().toString().trim(), Sex, Age.getText().toString().trim(), About.getText().toString().trim(),profileImagePath);
					}
					}else{
					
					Toast.makeText(mContext, "No Internet connection", Toast.LENGTH_LONG).show();
				}

			}
		});
		
	}
	
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
						profileImagePath =getPath(selectedImageUri);
						
						File imgFile = new  File(profileImagePath);
						if(imgFile.exists()){
						    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
						    //Drawable d = new BitmapDrawable(getResources(), myBitmap);
						    imgKid.setImageBitmap(myBitmap);

						}
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
	 
	 //*************************************************************//
	 //Upload Method
	 
	/* public int uploadFile(String sourcFileURI){
		 
		 String filename = sourcFileURI;
		 HttpURLConnection conn = null;
		 DataOutputStream dos = null;
		 String lineend = "\r\n";
		 String twoHyphens ="--";
		 String boundary ="*****";
		 int bytesRead, bytesAvailable ,bufferSize;
		 byte[] buffer;
		 int maxBufferSize = 1 * 1024 * 1024;
		 File sourceFile = new File(sourcFileURI);
		 
		 if(!sourceFile.isFile()){
			 //Do output
			 Log.e("Upload File", "File doesn not exist");
			 return 0;
		 }else {
			 
			 try{
				 //open a URL connection to the service url
				 FileInputStream fileInputStream = new FileInputStream(sourceFile);
				 URL url = new URL(uploadServerURL);
				 
				 // Open  a HTTP connecion to the URL
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
				//conn.setRequestProperty("tag", "profileImage");
				//conn.setRequestProperty("file_path", "http://playgroundhumor.com/demo/webservice/user_images/myandroid42/images");
				dos = new DataOutputStream(conn.getOutputStream());
				
				dos.writeBytes(twoHyphens + boundary + lineend);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+filename+"\""+lineend);
				dos.writeBytes(lineend);
				
				// create a buffer of maximum size
				bytesAvailable =fileInputStream.available();
				
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer,0,bufferSize);
				
				while(bytesRead > 0){
					dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				
				// send multipart form data neccesary after file data
				dos.writeBytes(lineend);
				dos.writeBytes(twoHyphens+boundary+twoHyphens+lineend);
				
				// response from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverMessage= conn.getResponseMessage();
				Log.i("uplodFile","HTTP Response is :"+serverResponseCode+ ":"+serverMessage );
				
				if(serverResponseCode ==200){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//Toast.makeText(KidProfile.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
						}
					});
				}
				
				InputStream is = conn.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while((ch=is.read())!=-1){
					b.append((char)ch);
				}
				String s = boundary.toString();
				Log.e("Response File Upload",s);
				//close the streams//
				fileInputStream.close();
				dos.flush();
				dos.close();
			 }catch(Exception ex){
				 
			 } finally {
			}
			 return 1;
		 }
	 }
*/
	public String getPreference(String preferenceId) {

		SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
		Parent_ID = settings.getString("Parent_ID", "0");
		return Parent_ID;

	}

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
	

	 class CreateKidProfile extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			UserFunctions userFunction = new UserFunctions();
			SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
			Log.d("Parent_ID_STRING",settings.getString("Parent_ID", "0"));
			JSONObject json= userFunction.createKid(settings.getString("Parent_ID", "0"), KidName.getText().toString().trim(),
					NickName.getText().toString().trim(), Sex, Age.getText().toString().trim(), About.getText().toString().trim(),Imagename);
			//
			//uploadFile(profileImagePath);
			//check for response
			try {
				if(json.getString("success")!=null){
					
					if(Integer.parseInt(json.getString("success"))==0){
						Log.d("JSON", json.getString("message"));
					}else {
						runOnUiThread(new Runnable() {
							public void run() {
								//
								
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
			pDialog.setMessage("Creating Kid Profile");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}
	
	class ImageUploadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... param) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();					
			   	HttpPost httpPost = new HttpPost("http://playgroundhumor.com/demo/webservice/mywebservice.php");
					MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);						
					
					    entity.addPart("tag", new StringBody("inser_kid_profile"));
						entity.addPart("parent_id", new StringBody(param[0]));
					    Log.e("parent_id", param[0]);
						entity.addPart("name", new StringBody(param[1]));
						Log.e("name", param[1]);
						entity.addPart("nick_name", new StringBody(param[2]));
						Log.e("nick_name", param[2]);
						entity.addPart("sex", new StringBody(param[3]));	
						Log.e("sex", param[3]);
						entity.addPart("age", new StringBody(param[4]));
						Log.e("age", param[4]);
						entity.addPart("about", new StringBody(param[5]));
						Log.e("about", param[5]);
						entity.addPart("file", new FileBody(new File(param[6])));
						Log.e("file", param[6]);
										
					
					httpPost.setEntity(entity);
					Log.e("TAG", "Request  "+httpPost.getRequestLine());
					HttpResponse response;
					response = httpClient.execute(httpPost);
					resEntity = response.getEntity();
				
				
	            final String response_str = EntityUtils.toString(resEntity);
	            Log.e("TAG","Response "+ response_str);
				return response_str;
			} catch (Exception e) {	
				pDialog.dismiss();
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setTitle("Creating Kid Profile");
			pDialog.setMessage("Please Wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected void onPostExecute(String sResponse) {				
		if(sResponse!=null){
			pDialog.dismiss();
			
			try {
				JSONObject json = new JSONObject(sResponse);
				if(json.getInt("success") ==1){
					Toast.makeText(KidProfile.this, "Profile created successfully", Toast.LENGTH_LONG).show();
					Intent i = new Intent(KidProfile.this,KidList.class);
					startActivity(i);
					KidProfile.this.finish();
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
			}
	}
 
 public boolean isValid(){
	// KidName.getText().toString().trim(),NickName.getText().toString().trim(),  About.getText().toString().trim(),profileImagePath
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
