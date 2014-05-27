package com.fiverr.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fiverr.helper.UserFunctions;
import com.fiverr.model.Kid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class KidList extends Activity{
	
private ArrayList<Kid> kids;
private Kid kid;

@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kid_list);
		kids = new ArrayList<Kid>();
		new GetKids().execute();
	}



private class GetKids extends AsyncTask<Void , Void, Void>{

	private JSONArray jArray;
	ProgressDialog pDialog;
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		try {
			UserFunctions user = new UserFunctions();
			SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
			Log.d("PREFS STRING",settings.getString("Parent_ID", "0"));
			JSONObject json =user.getKids(settings.getString("Parent_ID", "0").toString());
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
				//do nothing
				finish();
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
		pDialog = ProgressDialog.show(KidList.this, "Talking To Server", "Loading");
	}
	
	@Override
	protected void onPostExecute(Void result) {
		pDialog.dismiss();
		ListView kidlist = (ListView) findViewById(R.id.listKids);
		ListAdapter adapter = new com.fiverr.helper.ListAdapter(KidList.this, R.layout.list_item_kid_detail, kids);
		kidlist.setAdapter(adapter);
		kidlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,		long arg3) {
				Intent postQuote = new Intent(KidList.this,Submit.class);
				postQuote.putExtra("kid_id", kids.get(arg2).getKid_ID());
				startActivity(postQuote);
				finish();
			}
			
		});
		
		
	}
	
}
}
