package com.fiverr.helper;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiverr.model.Kid;
import com.fiverr.ui.EditKidProfile;
import com.fiverr.ui.R;
import com.fiverr.ui.Submit;

public class ListAdapter extends ArrayAdapter<Kid> {

	List<Kid> kids;

	private LayoutInflater mInflator;
	//private Typeface cTypeFace;
	private ImageLoader imgLoader;
	
	
	public ListAdapter(Activity activity, int resource, List<Kid> objects) {
		super(activity, resource, objects);
		this.kids = objects;
		this.mInflator = LayoutInflater.from(activity);
		//cTypeFace = Typeface.createFromAsset(activity.getAssets(),
				//"GochiHand-Regular.ttf");
		imgLoader = new ImageLoader(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflator
					.inflate(R.layout.list_item_kid_detail, null);
		}

		ImageView profile = (ImageView) convertView.findViewById(R.id.kid_icon);
		TextView Kidname = (TextView) convertView.findViewById(R.id.kidName);
		TextView Kidage = (TextView) convertView.findViewById(R.id.kidAge);
		TextView KidNickNmae = (TextView) convertView.findViewById(R.id.kidName);
		Button editbtn = (Button) convertView.findViewById(R.id.btnEdit);
		Button postbtn = (Button) convertView.findViewById(R.id.btnPost);
		
		final Kid kid = kids.get(position);
		Kidname.setText(kid.getName());
		KidNickNmae.setText(kid.getNick_Name());
		Kidage.setText("Age: " + kid.getAge()+"   "+kid.getGender());
		int loader = R.drawable.default_photo;
		//String image_url = "http://api.androidhive.info/images/sample.jpg";
		if(!kid.getImage().equals("")){
			imgLoader.DisplayImage("http://playgroundhumor.com/demo"+kid.getImage(),  profile);
		}
		
		
		editbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent editintent = new Intent(getContext(),
						EditKidProfile.class);
				editintent.putExtra("kid_id", kid.getKid_ID());
				getContext().startActivity(editintent);
				
			}
		});
		
		postbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent postQuote = new Intent(getContext(),Submit.class);
				postQuote.putExtra("kid_id", kid.getKid_ID());
				getContext().startActivity(postQuote);
			}
		});
		return convertView;
	}
}
