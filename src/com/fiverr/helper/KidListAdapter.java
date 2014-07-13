package com.fiverr.helper;

import com.fiverr.ui.BaseAcivity;
import com.fiverr.ui.EditKidProfile;
import com.fiverr.ui.R;
import com.fiverr.ui.Submit;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KidListAdapter implements OnClickListener{

	public View mView = null;
	public BaseAcivity base;
	public LinearLayout ll_each_row = null;
	public ImageView profile=null;
	public TextView Kidname=null;
	public TextView Kidage=null,KidNickNmae = null;
	public Button editbtn = null,postbtn = null;
	public String type = null;
	public String gender;
	public String kid_sex = null;
	public String age;
	public String name = null;
	public String nickname = null;
	public String kidid = null;
	
	public KidListAdapter(BaseAcivity b, String k,String n, String ni,String t, String g, String a){
		base=b;
		type = t;
		gender = g;
		age= a;
		name= n;
		nickname= ni;
		kidid= k;
		mView=View.inflate(base, R.layout.list_item_kid_detail, null);
		ll_each_row = (LinearLayout)mView.findViewById(R.id.ll_each_row);
		profile = (ImageView) mView.findViewById(R.id.kid_icon);
		Kidname = (TextView) mView.findViewById(R.id.kidName);
		Kidage= (TextView) mView.findViewById(R.id.kidAge);
		KidNickNmae = (TextView) mView.findViewById(R.id.nickName);
		editbtn = (Button) mView.findViewById(R.id.btnEdit);
		postbtn = (Button) mView.findViewById(R.id.btnPost);
		
		if(type.equals("submit")){
			editbtn.setVisibility(View.GONE);
		}else{
			postbtn.setVisibility(View.GONE);
		}	
		
		if(gender.equals("m")){
			kid_sex = "Male";
		}else{
			kid_sex = "Female";
		}
		
		Kidage.setText("Age: " + age+"   "+kid_sex);
		Kidname.setText(name);
		KidNickNmae.setText(nickname);
		ll_each_row.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_each_row:
			if(type.equals("submit")){
				Intent postQuote = new Intent(base,Submit.class);
				postQuote.putExtra("kid_id", kidid);
				postQuote.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				base.startActivity(postQuote);
			}else{
				Intent editintent = new Intent(base,EditKidProfile.class);
				editintent.putExtra("kid_id",kidid);
				editintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				base.startActivity(editintent);
			}	
			break;

		
		} 
		
	}


}




//package com.fiverr.helper;
//
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import com.fiverr.model.Kid;
//import com.fiverr.ui.EditKidProfile;
//import com.fiverr.ui.R;
//import com.fiverr.ui.Submit;
//
//public class ListAdapter extends ArrayAdapter<Kid> {
//
//	private List<Kid> kids;
//	private LayoutInflater mInflator;
//	private ImageLoader imgLoader;
//	private String mType;	
//	public ListAdapter(Activity activity, int resource, List<Kid> objects, String type) {
//		super(activity, resource, objects);
//		this.kids = objects;
//		this.mInflator = LayoutInflater.from(activity);
//		imgLoader = new ImageLoader(activity);
//		this.mType = type;
//	}
//	public View getView(int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			convertView = mInflator	.inflate(R.layout.list_item_kid_detail, null);
//		}
//		LinearLayout ll_meachRow = (LinearLayout)convertView.findViewById(R.id.ll_each_row);
//		ImageView profile = (ImageView) convertView.findViewById(R.id.kid_icon);
//		TextView Kidname = (TextView) convertView.findViewById(R.id.kidName);
//		TextView Kidage = (TextView) convertView.findViewById(R.id.kidAge);
//		TextView KidNickNmae = (TextView) convertView.findViewById(R.id.kidName);
//		Button editbtn = (Button) convertView.findViewById(R.id.btnEdit);
//		Button postbtn = (Button) convertView.findViewById(R.id.btnPost);
//		
//		if(mType.equals("submit")){
//			editbtn.setVisibility(View.GONE);
//		}else{
//			postbtn.setVisibility(View.GONE);
//		}		
//		final Kid kid = kids.get(position);
//		Kidname.setText(kid.getName());
//		KidNickNmae.setText(kid.getNick_Name());
//		String kid_sex = "";
//		if(kid.getGender().equals("m")){
//			kid_sex = "Male";
//		}else{
//			kid_sex = "Female";
//		}
//		Kidage.setText("Age: " + kid.getAge()+"   "+kid_sex);
//		if(!kid.getImage().equals("")){
//			imgLoader.DisplayImage("http://playgroundhumor.com/demo"+kid.getImage(),  profile);
//		}		
//		ll_meachRow.setOnClickListener(new OnClickListener() {			
//			public void onClick(View arg0) {
//				if(mType.equals("submit")){
//					Intent postQuote = new Intent(getContext(),Submit.class);
//					postQuote.putExtra("kid_id", kid.getKid_ID());
//					postQuote.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					getContext().startActivity(postQuote);
//				}else{
//					Intent editintent = new Intent(getContext(),EditKidProfile.class);
//					editintent.putExtra("kid_id", kid.getKid_ID());
//					editintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					getContext().startActivity(editintent);
//				}				
//			}
//		});		
//		postbtn.setOnClickListener(new OnClickListener() {		
//			public void onClick(View arg0) {			
//				Intent postQuote = new Intent(getContext(),Submit.class);
//				postQuote.putExtra("kid_id", kid.getKid_ID());
//				getContext().startActivity(postQuote);
//			}
//		});
//		return convertView;
//	}
//}
