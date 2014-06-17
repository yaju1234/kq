package com.fiverr.helper;

import java.util.List;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiverr.model.Quote;
import com.fiverr.ui.AllQuote;
import com.fiverr.ui.CustomVideoView;
import com.fiverr.ui.ImageDetailScreen;
import com.fiverr.ui.R;
import com.fiverr.ui.VideoPlayView;

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
		final RatingBar avg_rating=(RatingBar)convertView.findViewById(R.id.avg_rating);
		TextView child_name=(TextView)convertView.findViewById(R.id.textName);
		TextView child_age=(TextView)convertView.findViewById(R.id.textAge);
		TextView child_gender=(TextView)convertView.findViewById(R.id.textGender);
		ImageView child_img = (ImageView)convertView.findViewById(R.id.imgKid);
		TextView child_quote=(TextView)convertView.findViewById(R.id.textQuote);
		ImageView imageQuote = (ImageView)convertView.findViewById(R.id.imageQuote);
		child_quote.setMovementMethod(new ScrollingMovementMethod());
		final LinearLayout ll_fav =(LinearLayout)convertView.findViewById(R.id.ll_fav);
		final LinearLayout ll_rate =(LinearLayout)convertView.findViewById(R.id.ll_rate);
		final LinearLayout ll_share = (LinearLayout)convertView.findViewById(R.id.ll_share);
		
		
		final ImageView iv_fav =(ImageView)convertView.findViewById(R.id.iv_fav);
		final ImageView iv_rate =(ImageView)convertView.findViewById(R.id.iv_rate);
		final ImageView iv_share = (ImageView)convertView.findViewById(R.id.iv_share);
		
		final ImageView vv_preview = (ImageView)convertView.findViewById(R.id.videoPreview);
		final CustomVideoView vv = (CustomVideoView)convertView.findViewById(R.id.videoQuote);
		RelativeLayout rl_videoQuote = (RelativeLayout)convertView.findViewById(R.id.rl_videoQuote);
		ImageView iv_play = (ImageView)convertView.findViewById(R.id.iv_play);
		final ProgressBar bar = (ProgressBar)convertView.findViewById(R.id.progressBar1);
		
		if(quote_type.equals("fav")){
			ll_fav.setVisibility(View.GONE);
			ll_rate.setVisibility(View.GONE);
		}
		final Quote kid = kids.get(position);
		
		//avg_rating.setText("Average Rating: "+kid.getAvg_Rate());
		avg_rating.setRating(Float.parseFloat(""+kid.getAvg_Rate()));
		child_name.setText(kid.getChild_name());
		child_age.setText("Age : " + kid.getChild_age()+"yr");
		
		if(kid.getChild_gender().equals("f")){
			child_gender.setText("Gender : "+ "Female");
		}else{
			child_gender.setText("Gender : "+ "Male");
		}
		
		child_quote.setText(kid.getQuote_Text());
		imgLoader.DisplayImage("http://playgroundhumor.com/demo"+kid.getKid_Image(), child_img);
		if(kid.getImage_Id().length()>3){
			imageQuote.setVisibility(View.VISIBLE);
			rl_videoQuote.setVisibility(View.GONE);
			imgLoader.DisplayImage("http://playgroundhumor.com/demo"+kid.getImage_Id(), imageQuote);
		}else if(kid.getVideo_Id().length()>3){
			Log.e("ggg", "hhh");
			imageQuote.setVisibility(View.GONE);
			rl_videoQuote.setVisibility(View.VISIBLE);
			
			//final Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail( "http://playgroundhumor.com/demo"+kid.getVideo_Id(), MediaStore.Video.Thumbnails.MINI_KIND );
			vv_preview.setImageBitmap(kid.getThunails());
			/*vv.setVideoURI(Uri.parse(("http://playgroundhumor.com/demo"+kid.getVideo_Id())));
			MediaController mc = new MediaController(act);
            
            vv.setMediaController(mc);
            vv.requestFocus();
            vv.seekTo(1000);
           // vv.start();         
            mc.show();*/
		}else{
			imageQuote.setVisibility(View.GONE);
			rl_videoQuote.setVisibility(View.GONE);
		}
		
		
		if(kid.getIsRate().equals("0")){
			iv_rate.setImageResource(R.drawable.rate_quote);
		}else{
			iv_rate.setImageResource( R.drawable.disable_quote_rate);
		}
		
		if(!kid.getIsfavQuote().equals("0")){
			iv_fav.setImageResource( R.drawable.disable_quote_fav);
		}else {
			iv_fav.setImageResource(R.drawable.fav_quote);
		}
		
		ll_fav.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(kid.getIsfavQuote().equals("0")){
					iv_fav.setImageResource(R.drawable.fav_quote);
					act.callMarkAsFav(kid.getQuote_Id(),position);	
				}else{
					/*Toast.makeText(act, "You already marked it as favourite", Toast.LENGTH_SHORT)
					.show();*/
					iv_fav.setImageResource(R.drawable.disable_quote_fav);
					act.callMarkAsFav(kid.getQuote_Id(),position);	
				}
				
			}
		});
		
		imageQuote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(act,ImageDetailScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("url", "http://playgroundhumor.com/demo"+kid.getImage_Id());
				act.startActivity(i);
			}
		});
		
		iv_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*vv.setVideoURI(Uri.parse(("http://playgroundhumor.com/demo"+kids.get(position).getVideo_Id())));
	            MediaController mc = new MediaController(act);
	            vv.setMediaController(mc);
	            vv.requestFocus();
	            vv.seekTo(1000);
				bar.setVisibility(View.VISIBLE);
	           vv.start(); */ 
				Intent i = new Intent(act,VideoPlayView.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("url", "http://playgroundhumor.com/demo"+kids.get(position).getVideo_Id());
				act.startActivity(i);
				
			}
		});
		/*vv.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				bar.setVisibility(View.GONE);
				vv.setVideoURI(Uri.parse(("http://playgroundhumor.com/demo"+kids.get(position).getVideo_Id())));
	            MediaController mc = new MediaController(act);
	            vv.setMediaController(mc);
	            vv.requestFocus();
	            vv.seekTo(1000);
	            mc.show();
				
			}
		});*/
		ll_rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(kid.getIsRate().equals("0")){
					//ll_rate.setBackgroundResource(R.drawable.disable_quote_rate);
					act.callRate(kid.getQuote_Id(),position,avg_rating,iv_rate);
				}else{
					Toast.makeText(act, "You already rate it...", Toast.LENGTH_SHORT)
					.show();
				}
				
			}
		});
		ll_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//act.callShare(kid.getQuote_Text(),"http://playgroundhumor.com/demo"+kid.getImage_Id(),"http://playgroundhumor.com/demo"+kids.get(position).getVideo_Id());
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
