package com.fiverr.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

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
	
	private Button btn_mDateSort;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		Intent intent = getIntent();
		tempParentID = intent.getStringExtra("test");
		quote_type = intent.getStringExtra("quote_type");
		//cTypeface = Typeface.createFromAsset(getAssets(), "GochiHand-Regular.ttf");		
		btn_mDateSort = (Button)findViewById(R.id.btn_sort_by_newest);
		
		imgloader = new ImageLoader(getApplicationContext());
		/////----------------------------------
		
		//btnRate.setTypeface(cTypeface);
		//txtName.setTypeface(cTypeface);
		//txtQuote.setTypeface(cTypeface);
		//btnFav.setTypeface(cTypeface);
		
		
		QuoteArray = new ArrayList<Quote>();
		
		//quote = new Quote();
		if(isOnline()){
			new GetQuotes().execute();
			
		}else{
			Toast.makeText(this.getApplicationContext(),"No Internet connection", Toast.LENGTH_LONG).show();
		}
		btn_mDateSort.setOnClickListener(this);
		
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
			jsonObject = userfunction.getFavQuotes(tempParentID);
		}else if(quote_type.equals("all")){
			jsonObject =userfunction.getQuotes("0");
		}else if(quote_type.equals("kid_qoute")){
			jsonObject =userfunction.getQuotes(tempParentID);
		}
				
		try {
			if(jsonObject.getString(KEY_SUCCESS)!=null)
			
				if(Integer.parseInt(jsonObject.getString(KEY_SUCCESS))==0){
				
				finish();
				}else{
					jArray = jsonObject.getJSONArray("data");
					for(int i=0;i<jArray.length();i++){
						JSONObject jObj =jArray.getJSONObject(i);
						quote =new Quote();
						quote.setQuote_Id(jObj.getInt("id"));
						quote.setParent_ID(jObj.getInt("parent_id"));
						quote.setKids_Id(jObj.getInt("kids_id"));
						
						quote.setQuote_Text(jObj.getString("text"));
						quote.setAvg_Rate(jObj.getInt("avg_rate"));
						quote.setImage_Id(jObj.getString("image"));
						quote.setVideo_Id(jObj.getString("video"));
						
						quote.setChild_age(jObj.getString("age"));
						quote.setChild_name(jObj.getString("name"));
						quote.setChild_gender(jObj.getString("gender"));
						
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
				
				ViewPager pager = (ViewPager) findViewById(R.id.pager);
				PostAdapter adapter ;
				if(quote_type.equals("fav")){
					adapter = new com.fiverr.helper.PostAdapter(AllQuote.this, QuoteArray,quote_type);
				}else{
					adapter = new com.fiverr.helper.PostAdapter(AllQuote.this,QuoteArray,quote_type);
				}
				
				pager.setAdapter(adapter);
				pager.setCurrentItem(0);
				/*
			++index;
			txtQuote.setText(QuoteArray.get(index).getQuote_Text());
			txtName.setText(QuoteArray.get(index).getChild_name()+"\n Age:"+QuoteArray.get(index).getChild_age()+" Gender :"+QuoteArray.get(index).getChild_gender());
			imgloader.DisplayImage(Constant.imagr_url+QuoteArray.get(index).getImage_Id(), iv_kid);
			
			setFlipper(QuoteArray);	*/
			}
			pDialog.dismiss();
		}
	}
	/*private void setFlipper(List<Quote> quoteArray) {
		// TODO Auto-generated method stub			
		if(flipper.getChildCount()>0){			
	        while (flipper.getChildCount() > 0)
	        	flipper.removeViewAt(0);
		}
		LayoutInflater inflater = (LayoutInflater)this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		
		for(pos=0; pos<quoteArray.size(); pos++){
		View view = inflater.inflate( R.layout.each_activity_quote, null );
		TextView child_name=(TextView)view.findViewById(R.id.textName);
		TextView child_age=(TextView)view.findViewById(R.id.textAge);
		TextView child_gender=(TextView)view.findViewById(R.id.textGender);
		ImageView child_img = (ImageView)view.findViewById(R.id.imgKid);
		TextView child_quote=(TextView)view.findViewById(R.id.textQuote);
		Button btn_fav =(Button)view.findViewById(R.id.btnFav);
		Button btn_rate =(Button)view.findViewById(R.id.btnRate);
		Button quote_share = (Button)view.findViewById(R.id.quoteShare);
		
		if(quote_type.equals("fav")){
			btn_fav.setVisibility(View.GONE);
			btn_rate.setVisibility(View.GONE);
			quote_share.setVisibility(View.GONE);
		}
		
		child_name.setText(quoteArray.get(pos).getChild_name());
		child_age.setText("Age : " + quoteArray.get(pos).getChild_age()+"yr");
		
		if(quoteArray.get(pos).getChild_gender().equals("f")){
			child_gender.setText("Gender : "+ "Female");
		}else{
			child_gender.setText("Gender : "+ "Male");
		}
		
		child_quote.setText(quoteArray.get(pos).getQuote_Text());
		imgloader.DisplayImage("http://playgroundhumor.com/demo"+quoteArray.get(pos).getImage_Id(), child_img);
		
		
		btn_rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				// custom dialog
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.custom_dialog);
				dialog.setTitle("Rate this Quote");
				Button dialogBtn = (Button) dialog.findViewById(R.id.Submit);
				final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
				dialogBtn.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// code for rating
						SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
						rate_parent_id = settings.getString("Parent_ID", "0");
						rate_quote_id =""+QuoteArray.get(cur_pos-1).getQuote_Id();
						rate =""+ratingBar.getRating();
						Log.d("Rate",""+rate_parent_id+" "+rate_quote_id+" "+rate);
						new RateQuote().execute();
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		btn_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				new MarkFav().execute(""+QuoteArray.get(cur_pos-1).getQuote_Id());
			}
		});
		flipper.addView(view);	
		}
	}*/	
	
	public class RateQuote extends AsyncTask<String[], String, Boolean> {

		@Override
		protected Boolean doInBackground(String[]... params) {
			try {
				UserFunctions userfunction = new UserFunctions();
				JSONObject jOBJ = userfunction.rateQuote(rate_parent_id,rate_quote_id,rate);
				String res = jOBJ.getString("success");
				if(Integer.parseInt(res)== 0)
					return false;
				if(Integer.parseInt(res)==1)
					return true;
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
				}else{
					Log.d("Fav Quote","Fails marking fav");
				}
			}catch(Exception e){
				Log.d("Exception", "Exception received");
			}
			return null;
		}		
	}
	
	public void callRate(final int quote_id) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Rate this Quote");
		Button dialogBtn = (Button) dialog.findViewById(R.id.Submit);
		final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
		dialogBtn.setOnClickListener( new OnClickListener() {
			
			public void onClick(View arg0) {
				// code for rating
				SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
				rate_parent_id = settings.getString("Parent_ID", "0");
				rate_quote_id =""+quote_id;
				rate =""+ratingBar.getRating();
				Log.d("Rate",""+rate_parent_id+" "+rate_quote_id+" "+rate);
				new RateQuote().execute();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public void callMarkAsFav(int quote_Id) {
		// TODO Auto-generated method stub
		settings = getSharedPreferences("MYPREFS", 0);
		Log.d("Parent_ID_STRING",settings.getString("Parent_ID", "0"));
		if(settings.getString("Parent_ID", "0").equals("0")){
			dialog_box();
		}else{
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
			Toast.makeText(AllQuote.this, "Decline", Toast.LENGTH_SHORT)
					.show();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_mDateSort){
		    Collections.reverse(QuoteArray);
		    if(btn_mDateSort.getText().equals("DESC")){
		    	btn_mDateSort.setText("ASC");
		    }else{
		    	btn_mDateSort.setText("DESC");
		    }
		    ViewPager pager = (ViewPager) findViewById(R.id.pager);
			PostAdapter adapter ;
			if(quote_type.equals("fav")){
				adapter = new com.fiverr.helper.PostAdapter(AllQuote.this, QuoteArray,quote_type);
			}else{
				adapter = new com.fiverr.helper.PostAdapter(AllQuote.this,QuoteArray,quote_type);
			}
			
			pager.setAdapter(adapter);
			pager.setCurrentItem(0);
		}
	}
	
	/*  @Override
    public boolean onTouchEvent(MotionEvent event) {
     // TODO Auto-generated method stub
    	switch (event.getAction()) {
           case MotionEvent.ACTION_DOWN:
               initialX = event.getX();
               break;
           case MotionEvent.ACTION_UP:
               float finalX = event.getX();
               if (initialX > finalX)
               {
            	   if(cur_pos<QuoteArray.size()){
            		   flipper.showNext();           		   
            			   cur_pos++;                      
            	   }else{
            		   Toast.makeText(context, "Nothing to show more", Toast.LENGTH_LONG).show();
            	   }
                   
                   
               } 
               else
               {
            	   if(cur_pos>1){
            		   flipper.showPrevious();                	
            			   cur_pos--;             		  
            	   }else{
            		   Toast.makeText(context, "Nothing to show more", Toast.LENGTH_LONG).show();
            	   }
            	   
                
               }
               break;
           }
           return false;
    }*/
}
