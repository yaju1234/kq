package com.fiverr.helper;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fiverr.model.Quote;
import com.fiverr.ui.AllQuote;
import com.fiverr.ui.R;

public class PostAdapter extends PagerAdapter {
	private List<Quote> kids;
	private ImageLoader imgLoader;
	private AllQuote act;
	private String quote_type;
	private LayoutInflater inflater;
	public PostAdapter(AllQuote activity, List<Quote> objects,String type) {
		this.kids = objects;
		imgLoader = new ImageLoader(activity);	
		this.act=activity;
		this.quote_type=type;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position){
		View convertView = inflater.inflate(R.layout.each_activity_quote, view, false);
		TextView avg_rating=(TextView)convertView.findViewById(R.id.avg_rating);
		TextView child_name=(TextView)convertView.findViewById(R.id.textName);
		TextView child_age=(TextView)convertView.findViewById(R.id.textAge);
		TextView child_gender=(TextView)convertView.findViewById(R.id.textGender);
		ImageView child_img = (ImageView)convertView.findViewById(R.id.imgKid);
		TextView child_quote=(TextView)convertView.findViewById(R.id.textQuote);
		ImageView imageQuote = (ImageView)convertView.findViewById(R.id.imageQuote);
		child_quote.setMovementMethod(new ScrollingMovementMethod());
		final Button btn_fav =(Button)convertView.findViewById(R.id.btn_fav);
		Button iv_rate =(Button)convertView.findViewById(R.id.iv_rate);
		Button iv_share = (Button)convertView.findViewById(R.id.iv_share);
		
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
		imgLoader.DisplayImage("http://playgroundhumor.com/demo"+kid.getKid_Image(), child_img);
		if(kid.getImage_Id()!=" "){
			imageQuote.setVisibility(View.VISIBLE);
			imgLoader.DisplayImage("http://playgroundhumor.com/demo"+kid.getImage_Id(), imageQuote);
		}else if(kid.getVideo_Id()!=" "){
			imageQuote.setVisibility(View.GONE);
		}else{
			imageQuote.setVisibility(View.GONE);
		}
		
		if(!kid.getIsfavQuote().equals("0")){
			btn_fav.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.disable_quote_fav, 0, 0);
		}else {
			btn_fav.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.fav_quote, 0, 0);
		}
		
		btn_fav.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(kid.getIsfavQuote().equals("0")){
					btn_fav.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.disable_quote_fav, 0, 0);
					act.callMarkAsFav(kid.getQuote_Id(),position);	
				}else{
					Toast.makeText(act, "You already marked it as favourite", Toast.LENGTH_SHORT)
					.show();
				}
				
			}
		});
		
		iv_rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				act.callRate(kid.getQuote_Id());
			}
		});
		((ViewPager) view).addView(convertView, 0);
		return convertView;
	}

	@Override
	public int getCount() {
		return kids.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
}
