package com.fiverr.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.helper.Constant;
import com.fiverr.helper.KidListAdapter;
import com.fiverr.helper.UserFunctions;
import com.fiverr.model.Kid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class KidList extends BaseAcivity{
	
private ArrayList<Kid> kids = new ArrayList<Kid>();
private Kid kid;
private String type;
private ImageView iv_add_kids_profile;
private LinearLayout  ll_kid_list ;

protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kid_list);
		ll_kid_list = (LinearLayout) findViewById(R.id.ll_kid_list);
		iv_add_kids_profile = (ImageView)findViewById(R.id.iv_add_kids_profile);
		if(Constant.mEditKidFlag == false){
			Bundle extras = getIntent().getExtras();
			type = extras.getString("type");
		}		
		if(type.equals("submit")){
			iv_add_kids_profile.setVisibility(View.GONE);
		}	
		iv_add_kids_profile.setOnClickListener(this);
		new GetKids().execute();			
	}
	public void onResume(){
		super.onResume();
		if(Constant.mEditKidFlag == true){
			Constant.mEditKidFlag = false ;
			new GetKids().execute();
		}		
	}	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_kids_profile:
			Intent intent = new Intent(KidList.this,KidProfile.class);
			startActivity(intent);
			break;			
		}
	}
private class GetKids extends AsyncTask<Void , Void, Void>{
	private JSONArray jArray;	
	protected Void doInBackground(Void... arg0) {	
		try {
			kids.clear();
			UserFunctions user = new UserFunctions();				
			JSONObject json =user.getKids(""+app.info.useid);
			String res =json.getString("success");
			if(Integer.parseInt(res)==1){
				jArray= json.getJSONArray("data");
				for(int i=0;i<jArray.length();i++){
					JSONObject jObj = jArray.getJSONObject(i);
					kid = new Kid();
					kid.setKid_ID(jObj.getInt("id"));
					kid.setParen_ID(jObj.getInt("parent_id"));
					kid.setName(jObj.getString("name"));
					kid.setNick_Name(jObj.getString("nick_name"));
					kid.setGender(jObj.getString("gender"));
					kid.setAge(jObj.getString("age"));
					kid.setImage(jObj.getString("image"));
					kid.setAbout(jObj.getString("about"));
					kids.add(kid);
				}
			}else{
				runOnUiThread(new Runnable() {
					public void run() {
					showToast("Please Create Kids Profile");
						
					}
				});
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	protected void onPreExecute() {
		showProgressDailog();
	}
	protected void onPostExecute(Void result) {
		dissmissProgressDialog();
		for(int i = 0; i<kids.size(); i++){
			Kid kid = kids.get(i);
			ll_kid_list.removeAllViews();
			ll_kid_list.addView(new KidListAdapter(KidList.this, ""+kid.getKid_ID(), kid.getName(), kid.getNick_Name(),type,kid.getGender(),kid.getAge()).mView);
		}		
	}	
 }
}
