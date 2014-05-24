/**
 * @author CHAUHAN
 * 
 */
package com.fiverr.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.helper.Constant;
import com.fiverr.helper.ImageLoader;
import com.fiverr.helper.UserFunctions;
import com.fiverr.model.Kid;
import com.fiverr.model.Quote;
import com.fiverr.ui.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Quotes extends Activity{

	final Context context=this;
	private String tempParentID;
	private String rate_parent_id,rate_quote_id,rate;
	private int index = -1;
	private List<Quote> QuoteArray;
	private List<Kid> KidArray;
	private Quote quote;
	private Kid kid;
	//private Typeface cTypeface;
	private TextView txtQuote,txtName;
	private Button btnRate,btnFav;
	private ImageButton btnPrev,btnNext;
	private ImageView iv_kid;
	ProgressDialog pDialog;
	//--------Quote Share Test----------
	private ImageButton quoteShare;
	private ImageLoader imgloader;
	//
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quotes);
		Intent intent = getIntent();
		tempParentID = intent.getStringExtra("test");
		//cTypeface = Typeface.createFromAsset(getAssets(), "GochiHand-Regular.ttf");
		txtQuote = (TextView) findViewById(R.id.textQuote);
		txtName = (TextView) findViewById(R.id.textName);
		btnRate = (Button) findViewById(R.id.btnRate);
		btnFav =(Button) findViewById(R.id.btnFav);
		btnPrev = (ImageButton) findViewById(R.id.btnPrev);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		
		iv_kid = (ImageView)findViewById(R.id.imgKid);
		
		imgloader = new ImageLoader(getApplicationContext());
		/////----------------------------------
		quoteShare = (ImageButton) findViewById(R.id.quoteShare);
		//btnRate.setTypeface(cTypeface);
		//txtName.setTypeface(cTypeface);
		//txtQuote.setTypeface(cTypeface);
		//btnFav.setTypeface(cTypeface);
		
		txtName.setText("John Mackan 10yrs \n Columbia  ");
		QuoteArray = new ArrayList<Quote>();
		KidArray = new ArrayList<Kid>();
		//quote = new Quote();
		if(isOnline()){
			new GetQuotes().execute();
			
		}else{
			Toast.makeText(this.getApplicationContext(),"No Internet connection", Toast.LENGTH_LONG).show();
		}
		
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(index <= 0){
					Toast.makeText(context, "Nothing to show more", Toast.LENGTH_LONG).show();
				}else{
					--index;
					txtQuote.setText(QuoteArray.get(index).getQuote_Text());
					txtName.setText(QuoteArray.get(index).getChild_name()+"\n Age:"+QuoteArray.get(index).getChild_age()+" Gender :"+QuoteArray.get(index).getChild_gender());						
					imgloader.DisplayImage(Constant.imagr_url+QuoteArray.get(index).getImage_Id(), iv_kid);
				}								
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(index>=(QuoteArray.size()-1)){
					Toast.makeText(context, "Nothing to show more", Toast.LENGTH_LONG).show();
				}else{
					++index;
					txtQuote.setText(QuoteArray.get(index).getQuote_Text());
					txtName.setText(QuoteArray.get(index).getChild_name()+"\n Age:"+QuoteArray.get(index).getChild_age()+" Gender :"+QuoteArray.get(index).getChild_gender());						
					imgloader.DisplayImage(Constant.imagr_url+QuoteArray.get(index).getImage_Id(), iv_kid);
				}							
			}
		});
		
		/***
		 *  Button Rate on click event
		 */
		
		btnRate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
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
						rate_quote_id =""+QuoteArray.get(index).getQuote_Id();
						rate =""+ratingBar.getRating();
						Log.d("Rate",""+rate_parent_id+" "+rate_quote_id+" "+rate);
						new RateQuote().execute();
						dialog.dismiss();
					}
				});
				dialog.show();
			}
			
		});
		
		btnFav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new MarkFav().execute();
			}
		});
		
		quoteShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				shareIntent(index);
			}
		});
	}
	
	public void shareIntent(int tempIndex){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT,QuoteArray.get(tempIndex).getQuote_Text()+"\n **************** \n Quote Shared by PlayGround Humor APP");
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share Quote Via"));
	}
	public String getPreference(String preferenceId){
		
		SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
		Log.d("shared prefs", settings.getString("Parent_ID","0"));
		return settings.getString("preferenceId","0");
		
		
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
		
		jsonObject =userfunction.getQuotes(tempParentID);
		
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
			++index;
			txtQuote.setText(QuoteArray.get(index).getQuote_Text());
			txtName.setText(QuoteArray.get(index).getChild_name()+"\n Age:"+QuoteArray.get(index).getChild_age()+" Gender :"+QuoteArray.get(index).getChild_gender());
			imgloader.DisplayImage(Constant.imagr_url+QuoteArray.get(index).getImage_Id(), iv_kid);
			}
			pDialog.dismiss();
		}
	}
	class RateQuote extends AsyncTask<String[], String, Boolean> {

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

	class MarkFav extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			try{
				UserFunctions userfunction = new UserFunctions();
				SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
				JSONObject jOBJ= userfunction.markFavQuote(settings.getString("Parent_ID", "0"), QuoteArray.get(index).getQuote_Id()+"".trim());
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
}
