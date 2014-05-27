package com.fiverr.helper;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiverr.model.Quote;
import com.fiverr.ui.AllQuote;
import com.fiverr.ui.R;

public class CopyOfPostAdapter extends ArrayAdapter<Quote> {
	List<Quote> kids;
	private LayoutInflater mInflator;
	private ImageLoader imgLoader;
	private AllQuote act;
	String quote_type;
	public CopyOfPostAdapter(AllQuote activity, int resource, List<Quote> objects,String type) {
		super(activity, resource, objects);		
		this.kids = objects;
		this.mInflator = LayoutInflater.from(activity);		
		imgLoader = new ImageLoader(activity);	
		this.act=activity;
		this.quote_type=type;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflator
					.inflate(R.layout.each_activity_quote, null);
		}
		final TextView avg_rating=(TextView)convertView.findViewById(R.id.avg_rating);
		TextView child_name=(TextView)convertView.findViewById(R.id.textName);
		TextView child_age=(TextView)convertView.findViewById(R.id.textAge);
		TextView child_gender=(TextView)convertView.findViewById(R.id.textGender);
		ImageView child_img = (ImageView)convertView.findViewById(R.id.imgKid);
		TextView child_quote=(TextView)convertView.findViewById(R.id.textQuote);
		Button btn_fav =(Button)convertView.findViewById(R.id.btn_fav);
		ImageView iv_rate =(ImageView)convertView.findViewById(R.id.iv_rate);
		ImageView iv_share = (ImageView)convertView.findViewById(R.id.iv_share);
		
		if(quote_type.equals("fav")){
			btn_fav.setVisibility(View.GONE);
			iv_rate.setVisibility(View.GONE);
		}
		final Quote kid = kids.get(position);
		
		avg_rating.setText("Average Rating: "+kid.getAvg_Rate());
		child_name.setText(kid.getChild_name());
		child_age.setText("Age : " + kid.getChild_age()+"yr");
		
		if(kid.getChild_gender().equals("f")){
			child_gender.setText("Gender : "+ "Female");
		}else{
			child_gender.setText("Gender : "+ "Male");
		}
		
		child_quote.setText(kid.getQuote_Text());
		imgLoader.DisplayImage("http://playgroundhumor.com/demo"+kid.getImage_Id(), child_img);
		
		btn_fav.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				act.callMarkAsFav(kid.getQuote_Id(),position);
			}
		});
		
		iv_rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				act.callRate(kid.getQuote_Id(),position,avg_rating);
			}
		});
		
		return convertView;
	}
}
