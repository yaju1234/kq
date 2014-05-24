package com.fiverr.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fiverr.helper.Constant;
import com.fiverr.helper.ImageLoader;
import com.fiverr.helper.UserFunctions;
import com.fiverr.model.Quote;

public class FavQuotes extends Activity {

	final Context context = this;

	ProgressDialog pDialog;
	private int index = -1;
	private List<Quote> QuoteArray;
	private Quote quote;
	private Typeface cTypeface;
	private TextView txtQuote, txtName;
	private Button btnRate, btnFav;
	private ImageButton btnPrev, btnNext;
	private String tempParentID;
	private ImageLoader imgloader;
	private ImageButton quoteShare;
	private ImageView iv_kid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quotes);
		Intent intent = getIntent();
		tempParentID = intent.getStringExtra("test");
		cTypeface = Typeface.createFromAsset(getAssets(),
				"GochiHand-Regular.ttf");
		txtQuote = (TextView) findViewById(R.id.textQuote);
		txtName = (TextView) findViewById(R.id.textName);
		btnRate = (Button) findViewById(R.id.btnRate);
		
		imgloader = new ImageLoader(getApplicationContext());
		iv_kid = (ImageView)findViewById(R.id.imgKid);
		// Fav button
		btnFav = (Button) findViewById(R.id.btnFav);
		btnFav.setVisibility(View.GONE);
		btnRate.setVisibility(View.GONE);
		btnPrev = (ImageButton) findViewById(R.id.btnPrev);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		////--------------------------------------------------------///////
		quoteShare = (ImageButton) findViewById(R.id.quoteShare);
		quoteShare.setVisibility(View.GONE);
		////--------------------------------------------------------///////
		txtName.setTypeface(cTypeface);
		txtQuote.setTypeface(cTypeface);
		btnRate.setTypeface(cTypeface);
		QuoteArray = new ArrayList<Quote>();
		

		if (isOnline()) {
			new GetFavQuotes().execute();
		} else {
			Toast.makeText(context, "No internet connection detected",
					Toast.LENGTH_LONG).show();
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
					imgloader = new ImageLoader(getApplicationContext());
				}					
			}
		});
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null && ni.isConnected());
	}

	class GetFavQuotes extends AsyncTask<String, String, String> {

		private JSONArray jArray;
		JSONObject jsonObject, jsonObject2;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setTitle("Talking to server");
			pDialog.setMessage("Fetching your favorite quotes");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserFunctions userfunction = new UserFunctions();
			Log.d("Parent ID",tempParentID);
			jsonObject = userfunction.getFavQuotes(tempParentID);
			try {

				if (jsonObject.getString("success") != null)
					if (Integer.parseInt(jsonObject.getString("success")) == 1) {

						jArray = jsonObject.getJSONArray("data");
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jOBJ = jArray.getJSONObject(i);
							quote = new Quote();
							quote.setQuote_Id(jOBJ.getInt("id"));
							quote.setParent_ID(jOBJ.getInt("parent_id"));
							quote.setKids_Id(jOBJ.getInt("kids_id"));

							quote.setQuote_Text(jOBJ.getString("text"));
							quote.setAvg_Rate(jOBJ.getInt("avg_rate"));
							quote.setImage_Id(jOBJ.getString("image"));
							quote.setVideo_Id(jOBJ.getString("video"));
							
							quote.setChild_age(jOBJ.getString("age"));
							quote.setChild_name(jOBJ.getString("name"));
							quote.setChild_gender(jOBJ.getString("gender"));
							
							QuoteArray.add(quote);						
						}
					}

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
			Log.d("Array Size", QuoteArray.size() + "");
			if (QuoteArray.size() == 0) {
				finish();
				;
			} else {
				++index;
				txtQuote.setText(QuoteArray.get(index).getQuote_Text());
				txtName.setText(QuoteArray.get(index).getChild_name()+"\n Age:"+QuoteArray.get(index).getChild_age()+" Gender :"+QuoteArray.get(index).getChild_gender());
				imgloader = new ImageLoader(getApplicationContext());
			}
			pDialog.dismiss();
		}
	}
}
