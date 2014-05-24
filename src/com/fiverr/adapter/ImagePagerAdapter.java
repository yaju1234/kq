package com.fiverr.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;


/*
public class ImagePagerAdapter extends PagerAdapter {

	private ArrayList<ImageBean> images;
	private LayoutInflater inflater;

	ImagePagerAdapter(ArrayList<ImageBean> images) {
	    this.images = images;
	    inflater = getLayoutInflater();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    container.removeView((View) object);
	}

	@Override
	public int getCount() {
	    return images.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
	    View imageLayout = inflater.inflate(R.layout.item_pager_image,  view, false);
	    assert imageLayout != null;
	    ImageView imageView = (ImageView) imageLayout .findViewById(R.id.image);
	    final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
	    Button btn_submit = (Button) imageLayout .findViewById(R.id.btn_submit);
	    final EditText et_comments = (EditText) imageLayout .findViewById(R.id.et_comments);
	    
	    lv_comments = (ListView) imageLayout.findViewById(R.id.lv_comments);
	    commentArr.clear();
	    commentArr = Global.imageArr.get(Global.selectedPos).getCommentArr();
	    for(int i=0; i<commentArr.size(); i++){
		Log.e("TAG5", commentArr.get(i).getComments());
	    }
	    CommentsAdapter commentsAdapter = new CommentsAdapter(ImagePagerActivity.this, R.layout.row_comments, commentArr);
	    lv_comments.setAdapter(commentsAdapter);
	   
	    btn_submit.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    String comment = et_comments.getText().toString().trim();
		    if (comment.length() > 0) {
			et_comments.setText("");
			Global.selectedPos = position;
			Global.imageArr.get(position).commentArr.add(new CommentBean(myApp.getAppInfo().userFirstName,myApp.getAppInfo().userLastName, "", comment));
			new SubmitComments().execute(myApp.getAppInfo().userId,	images.get(position).getImageId(), comment);
			notifyDataSetChanged();
		    }

		}
	    });

	   // new GetComments().execute(images.get(position).getImageId());
	   
	    imageLoader.displayImage(images.get(position).getImageLink(),
		    imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			    spinner.setVisibility(View.VISIBLE);
			    Log.e("TAG5", "Reachhere "+position);
			   // commentArr.clear();
			    commentArr = Global.imageArr.get(position).getCommentArr();
			    for(int i=0; i<commentArr.size(); i++){
				Log.e("TAG5", commentArr.get(i).getComments());
			    }
			    CommentsAdapter commentsAdapter = new CommentsAdapter(ImagePagerActivity.this, R.layout.row_comments, commentArr);
			    lv_comments.setAdapter(commentsAdapter);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
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
			    Toast.makeText(ImagePagerActivity.this, message,
				    Toast.LENGTH_SHORT).show();

			    spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri,
				View view, Bitmap loadedImage) {
			    spinner.setVisibility(View.GONE);
			}
		    });

	    view.addView(imageLayout, 0);
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
    }

    public class GetComments extends
	    AsyncTask<String, String, ArrayList<CommentBean>> {

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();

	}

	@Override
	protected ArrayList<CommentBean> doInBackground(String... params) {
	    boolean flg = false;

	    try {
		JSONObject request = new JSONObject();
		request.put("image_id", params[0]);
		Log.e("snomada", "ImageID " + params[0]);
		JSONObject response = KlHttpClient.SendHttpPost(URL.VIEW_COMMENTS.getUrl(), request);
		Log.e("snomada", "view " + response.toString());
		if (response != null) {
		    flg = response.getBoolean("status");
		    commentArr.clear();
		    if (flg) {

			JSONArray jArr = response.getJSONArray("comments");

			for (int i = 0; i < jArr.length(); i++) {
			    JSONObject c = jArr.getJSONObject(i);
			    String fname = c.getString("first_name");
			    String lname = c.getString("last_name");
			    String profile_pic = c.getString("profile_picture");
			    String txt_commets = c.getString("comment");
			    commentArr.add(new CommentBean(fname, lname,
				    profile_pic, txt_commets));
			}

		    }
		}
	    } catch (JSONException e) {
		e.printStackTrace();
		return null;
	    }
	    return commentArr;
	}

	@Override
	protected void onPostExecute(ArrayList<CommentBean> result) {
	    super.onPostExecute(result);
	    if (result != null) {
		CommentsAdapter commentsAdapter = new CommentsAdapter(ImagePagerActivity.this, R.layout.row_comments, result);
		lv_comments.setAdapter(commentsAdapter);
	    }
	}

    }

    public class SubmitComments extends AsyncTask<String, String, Boolean> {

	String comments;

	@Override
	protected Boolean doInBackground(String... params) {
	    boolean flg = false;
	    comments = params[2];
	    try {
		JSONObject request = new JSONObject();
		request.put("user_id", params[0]);
		request.put("image_id", params[1]);
		request.put("comment", params[2]);

		JSONObject response = KlHttpClient.SendHttpPost(URL.SUBMIT_COMMENTS.getUrl(), request);
		Log.e("snomada", response.toString());
		if (response != null) {
		    flg = response.getBoolean("status");

		}
	    } catch (JSONException e) {
		e.printStackTrace();
		return null;
	    }
	    return flg;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    if (result != null) {
		 commentArr.add(new  CommentBean(myApp.getAppInfo().userFirstName,myApp.getAppInfo().userLastName, "", comments));
		  CommentsAdapter commentsAdapter = new	  CommentsAdapter(ImagePagerActivity.this,  R.layout.row_comments, commentArr);
		  lv_comments.setAdapter(commentsAdapter);
		 
	    }
	}
    }*/