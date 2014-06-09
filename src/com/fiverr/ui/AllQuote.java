package com.fiverr.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiverr.db.KqDatabaseAdapter;
import com.fiverr.helper.Constant;
import com.fiverr.helper.ImageLoader;
import com.fiverr.helper.PostAdapter;
import com.fiverr.helper.UserFunctions;
import com.fiverr.model.Kid;
import com.fiverr.model.Quote;

public class AllQuote extends Activity implements OnClickListener{
	final Context context=this;
	private String tempParentID;
	private String rate_parent_id,rate_quote_id,rate;
	private int index = -1;
	private List<Quote> QuoteArray;
	//private ViewFlipper flipper;
	private Quote quote;
	private Kid kid;
	//private Typeface cTypeface;
	private KqDatabaseAdapter kqDbAdapter;
	
	float initialX;
	private SharedPreferences settings ;
	ProgressDialog pDialog;
	//--------Quote Share Test----------
	
	private ImageLoader imgloader;
	//
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	
	private int pos = 0;
	private int cur_pos=1;
	
	private String quote_type;
	
	private LinearLayout btn_mSortByNewest;
	private LinearLayout btn_mSortByOldest;
	private LinearLayout btn_sort_by_rating;
	
	private int Quote_pos = 0;
	
	private double avg_rate;
	
	private RatingBar tv_mAvgRating;
	private TextView tv_mHeading;
	
	private String sort_type_select = "ASC";
	private int all_quote_index = 0;
	private int fav_quote_index = 0;
	private int kid_quote_index = 0;
	
	public int selected_index = 0;
	private boolean mPageEnd = false;  
	
	public ViewPager pager;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		
		tv_mHeading = (TextView)findViewById(R.id.quote_heading);
		pager = (ViewPager)findViewById(R.id.pager);
		
		Intent intent = getIntent();
		tempParentID = intent.getStringExtra("test");
		quote_type = intent.getStringExtra("quote_type");
		QuoteArray = new ArrayList<Quote>();
		//cTypeface = Typeface.createFromAsset(getAssets(), "GochiHand-Regular.ttf");	
		
		if(quote_type.equals("fav")){
			tv_mHeading.setText("Favourites Posts");
			fav_quote_index = 0;
			QuoteArray.clear();
		}else if(quote_type.equals("all")){
			tv_mHeading.setText("Browse Posts");
			all_quote_index = 0;
			QuoteArray.clear();
		}else if(quote_type.equals("kid_qoute")){
			tv_mHeading.setText("kid's Posts");
			kid_quote_index = 0;
			QuoteArray.clear();
		}
		btn_mSortByNewest = (LinearLayout)findViewById(R.id.btn_sort_by_newest);
		btn_mSortByOldest = (LinearLayout)findViewById(R.id.btn_sort_by_oldest);
		btn_sort_by_rating = (LinearLayout)findViewById(R.id.btn_sort_by_rating);
		
		imgloader = new ImageLoader(getApplicationContext());
		/////----------------------------------
		kqDbAdapter = KqDatabaseAdapter.createInstance(getApplicationContext());
		if(kqDbAdapter.fetChValue()!=null){
			Constant.kqArrRating = kqDbAdapter.fetChValue();
		}
		
		if(isOnline()){
			btn_mSortByNewest.setVisibility(View.VISIBLE);
			btn_mSortByOldest.setVisibility(View.VISIBLE);
			new GetQuotes().execute();
			
		}else{
			Toast.makeText(this.getApplicationContext(),"No Internet connection", Toast.LENGTH_LONG).show();
			btn_mSortByNewest.setVisibility(View.INVISIBLE);
			btn_mSortByOldest.setVisibility(View.INVISIBLE);
		}
		btn_mSortByNewest.setOnClickListener(this);
		btn_mSortByOldest.setOnClickListener(this);
		btn_sort_by_rating.setOnClickListener(this);
		
		btn_mSortByNewest.setBackgroundColor(Color.parseColor("#c9653f"));
		btn_mSortByOldest.setBackgroundColor(Color.parseColor("#7ab9f3"));
		btn_sort_by_rating.setBackgroundColor(Color.parseColor("#7ab9f3"));
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				selected_index = arg0;
				
			}
			 boolean callHappened;
			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				
				 if( mPageEnd && position == selected_index /*&& !callHappened*/)
			        {
			            Log.d(getClass().getName(), "Okay");
			            mPageEnd = false;//To avoid multiple calls. 
			          //  callHappened = true;
			            if(position == QuoteArray.size()-1){
							if(quote_type.equals("all")){
								all_quote_index = all_quote_index + 20;
							}else if(quote_type.equals("fav")){
								fav_quote_index = fav_quote_index + 20;
							}else if(quote_type.equals("kid_qoute")){
								kid_quote_index = kid_quote_index + 20;
							}
							new GetQuotes().execute();
						}
			        }else
			        {
			            mPageEnd = false;
			        }
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if(selected_index == QuoteArray.size() - 1)
		        {
		            mPageEnd = true;
		        }
				
			}
		});
	}
	
	
	private boolean isOnline()  {
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    return (ni != null && ni.isConnected());
	}
	
	class GetQuotes extends AsyncTask<String, String, String>{
		
		//private JSONObject jObj;
		private JSONArray jArray;
		JSONObject jsonObject,jsonObject2;
		
		@Override
		protected String doInBackground(String... arg0) {
			
		UserFunctions userfunction = new UserFunctions();
		SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
		if(quote_type.equals("fav")){
			jsonObject = userfunction.getFavQuotes(tempParentID,sort_type_select,fav_quote_index);
		}else if(quote_type.equals("all")){
			if(settings.getString("Parent_ID", "0").equals("0")){
				jsonObject =userfunction.getQuotes("0",sort_type_select,all_quote_index);
			}else{
				jsonObject =userfunction.getQuotes("0",sort_type_select,all_quote_index);
				//jsonObject =userfunction.getQuotes(settings.getString("Parent_ID", "0"));
			}
		}else if(quote_type.equals("kid_qoute")){
			jsonObject =userfunction.getQuotes(tempParentID,sort_type_select,kid_quote_index);
		}
				
		try {
			if(jsonObject.getString(KEY_SUCCESS)!=null)
			
				if(Integer.parseInt(jsonObject.getString(KEY_SUCCESS))==0){
				
				//finish();
				}else{
					jArray = jsonObject.getJSONArray("data");
					for(int i=0;i<jArray.length();i++){
						JSONObject jObj =jArray.getJSONObject(i);						
						boolean flag = true;
						quote =new Quote();
						quote.setQuote_Id(jObj.getInt("id"));
						quote.setParent_ID(jObj.getInt("parent_id"));
						quote.setKids_Id(jObj.getInt("kids_id"));
						
						quote.setQuote_Text(jObj.getString("text"));
						quote.setAvg_Rate(jObj.getInt("avg_rate"));
						if(!jObj.isNull("kidImg")){
							quote.setKid_Image(jObj.getString("kidImg"));
						}else{
							quote.setKid_Image("no");
						}
						
						quote.setVideo_Id(jObj.getString("video"));
						if(jObj.getString("video").length()>3){
							quote.setThunails(ThumbnailUtils.createVideoThumbnail( "http://playgroundhumor.com/demo"+jObj.getString("video"), MediaStore.Video.Thumbnails.MINI_KIND ));
						}else{
							quote.setThunails(null);
						}
						quote.setImage_Id(jObj.getString("image"));
						quote.setChild_age(jObj.getString("age"));
						quote.setChild_name(jObj.getString("name"));
						quote.setChild_gender(jObj.getString("gender"));
						if(jObj.has("isfavquate")) {
						    //it has it, do appropriate processing
							quote.setIsfavQuote(""+jObj.getInt("isfavquate"));
						}else{
							quote.setIsfavQuote("0");
						}
						
						for(int j=0;j<Constant.kqArrRating.size();j++){							
							if(Constant.kqArrRating.get(j).equals(""+jObj.getInt("id"))){
								flag = false;
								j = Constant.kqArrRating.size();								
							}
						}
						if(flag){
							quote.setIsRated("0");
						}else{
							quote.setIsRated("1");
						}
						QuoteArray.add(quote);
						
				}
					
				}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setTitle("Talking to server");
			pDialog.setMessage("Fetching quotes");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.e("Size",""+QuoteArray.size());
			
			if(QuoteArray.size()==0){
				finish();
			}
			else{
				
				
				PostAdapter adapter ;
				if(quote_type.equals("fav")){
					adapter = new com.fiverr.helper.PostAdapter(AllQuote.this, QuoteArray,quote_type);
				}else{
					adapter = new com.fiverr.helper.PostAdapter(AllQuote.this,QuoteArray,quote_type);
				}
				
				pager.setAdapter(adapter);
				if(quote_type.equals("kid_qoute")){
					pager.setCurrentItem(kid_quote_index);
				}else if(quote_type.equals("all")){
					pager.setCurrentItem(all_quote_index);
				}else if(quote_type.equals("fav")){
					pager.setCurrentItem(fav_quote_index);
				}
			}
			pDialog.dismiss();
		}
	}
	
	
	
	
	public class RateQuote extends AsyncTask<String[], String, Boolean> {

		@Override
		protected Boolean doInBackground(String[]... params) {
			try {
				UserFunctions userfunction = new UserFunctions();
				JSONObject jOBJ = userfunction.rateQuote(rate_parent_id,rate_quote_id,rate);
				String res = jOBJ.getString("success");
				if(Integer.parseInt(res)== 0)
					return false;
				if(Integer.parseInt(res)==1){
					kqDbAdapter.inserValue(rate_quote_id);
					Constant.kqArrRating.add(rate_quote_id);
					avg_rate=jOBJ.getDouble("data");
					return true;
				}
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			QuoteArray.get(Quote_pos).setIsRated("1");
			QuoteArray.get(Quote_pos).setAvg_Rate(avg_rate);
			tv_mAvgRating.setRating(Float.parseFloat(String.valueOf(avg_rate)));
		}
	}
	
	public class MarkFav extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			try{
				UserFunctions userfunction = new UserFunctions();
				SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
				JSONObject jOBJ= userfunction.markFavQuote(settings.getString("Parent_ID", "0"), params[0]);
				String success= jOBJ.getString("success");
				
				if(Integer.parseInt(success)==1){
					Log.d("Fav Quote","Sucess marking fav");
					QuoteArray.get(Quote_pos).setIsfavQuote("1");
				}else{
					Log.d("Fav Quote","Fails marking fav");
				}
			}catch(Exception e){
				Log.d("Exception", "Exception received");
			}
			return null;
		}		
	}
	
	public void callRate(final int quote_id, int position, RatingBar avg_rating, final ImageView iv_rate) {
		tv_mAvgRating=avg_rating;
		Quote_pos=position;
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Rate this Quote");
		Button dialogBtn = (Button) dialog.findViewById(R.id.Submit);
		final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
		dialogBtn.setOnClickListener( new OnClickListener() {
			
			public void onClick(View arg0) {
				SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
				rate_parent_id = settings.getString("Parent_ID", "0");
				rate_quote_id =""+quote_id;
				rate =""+ratingBar.getRating();
				Log.d("Rate",""+rate_parent_id+" "+rate_quote_id+" "+rate);
				iv_rate.setImageResource(R.drawable.disable_quote_rate);
				new RateQuote().execute();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public void callMarkAsFav(int quote_Id, int position) {
		settings = getSharedPreferences("MYPREFS", 0);
		Log.d("Parent_ID_STRING",settings.getString("Parent_ID", "0"));
		if(settings.getString("Parent_ID", "0").equals("0")){
			dialog_box();
		}else{
			Quote_pos=position;
			new MarkFav().execute(""+quote_Id);
		}	
	}
	private void dialog_box() {
		Builder builder = new AlertDialog.Builder(AllQuote.this);
		AlertDialog dialog = builder.create();
		dialog.setTitle("Alert");
		dialog.setMessage("You have to Login First");
		dialog.setButton("Yes", listenerAccept);
		dialog.setButton2("No", listenerDoesNotAccept);
		dialog.setCancelable(true);
		dialog.show();
	}

	DialogInterface.OnClickListener listenerAccept = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Intent loginIntent = new Intent(AllQuote.this,Login.class);
			startActivity(loginIntent);
		}
	};
	DialogInterface.OnClickListener listenerDoesNotAccept = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Toast.makeText(AllQuote.this, "Decline", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onClick(View v) {
		if(v == btn_mSortByNewest){		   
			sort_type_select = "ASC";
			all_quote_index = 0;
			kid_quote_index = 0;
			fav_quote_index = 0;
			
			btn_mSortByNewest.setBackgroundColor(Color.parseColor("#c9653f"));
			btn_mSortByOldest.setBackgroundColor(Color.parseColor("#7ab9f3"));
			btn_sort_by_rating.setBackgroundColor(Color.parseColor("#7ab9f3"));
			
			QuoteArray.clear();
			new GetQuotes().execute();
			
		}else if(v == btn_mSortByOldest){	
			sort_type_select = "DESC";
			all_quote_index = 0;
			kid_quote_index = 0;
			fav_quote_index = 0;
			
			btn_mSortByNewest.setBackgroundColor(Color.parseColor("#7ab9f3"));
			btn_mSortByOldest.setBackgroundColor(Color.parseColor("#c9653f"));
			btn_sort_by_rating.setBackgroundColor(Color.parseColor("#7ab9f3"));
			
			QuoteArray.clear();
			new GetQuotes().execute();
			
		}else if(v == btn_sort_by_rating){	
			//Toast.makeText(getApplicationContext(), "aaa", 2000).show();
			sort_type_select = "RATE";
			all_quote_index = 0;
			kid_quote_index = 0;
			fav_quote_index = 0;
			
			btn_mSortByNewest.setBackgroundColor(Color.parseColor("#7ab9f3"));
			btn_mSortByOldest.setBackgroundColor(Color.parseColor("#7ab9f3"));
			btn_sort_by_rating.setBackgroundColor(Color.parseColor("#c9653f"));
			
			QuoteArray.clear();
			new GetQuotes().execute();
		}
	}
	public void callShare(String quote_text, String image, String video) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");      

		intent.putExtra(Intent.EXTRA_TEXT, "eample");
		intent.putExtra(Intent.EXTRA_TITLE, "example");
		intent.putExtra(Intent.EXTRA_SUBJECT, "example");
		intent.putExtra(Intent.EXTRA_STREAM, image);

		Intent openInChooser = new Intent(intent);
		startActivity(openInChooser);
	}
}
