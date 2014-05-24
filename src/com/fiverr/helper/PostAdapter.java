package com.fiverr.helper;

import java.util.List;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
/*
public class PostAdapter extends PagerAdapter {

	List<Quote> kids;
	private LayoutInflater mInflator;
	private ImageLoader imgLoader;
	private AllQuote act;
	String quote_type;
	private LayoutInflater inflater;

	PostAdapter(AllQuote activity, List<Quote> objects,String type) {
		this.kids = objects;
		this.mInflator = LayoutInflater.from(activity);		
		imgLoader = new ImageLoader(activity);	
		this.act=activity;
		this.quote_type=type;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public int getCount() {
		return kids.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		//final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

		imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
				}
				Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				spinner.setVisibility(View.GONE);
			}
		});

		((ViewPager) view).addView(imageLayout, 0);
		return imageLayout;
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
}*/
public class PostAdapter extends PagerAdapter {
	List<Quote> kids;
	private LayoutInflater mInflator;
	private ImageLoader imgLoader;
	private AllQuote act;
	String quote_type;
	private LayoutInflater inflater;
	public PostAdapter(AllQuote activity, List<Quote> objects,String type) {
		this.kids = objects;
		this.mInflator = LayoutInflater.from(activity);		
		imgLoader = new ImageLoader(activity);	
		this.act=activity;
		this.quote_type=type;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position){
		// TODO Auto-generated method stub
		/*if (convertView == null) {
			convertView = mInflator
					.inflate(R.layout.each_activity_quote, null);
		}*/
		View convertView = inflater.inflate(R.layout.each_activity_quote, view, false);
		TextView avg_rating=(TextView)convertView.findViewById(R.id.avg_rating);
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
				act.callMarkAsFav(kid.getQuote_Id());
			}
		});
		
		iv_rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				act.callRate(kid.getQuote_Id());
			}
		});
		((ViewPager) view).addView(convertView, 0);
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
