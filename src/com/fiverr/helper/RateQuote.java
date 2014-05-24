package com.fiverr.helper;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class RateQuote extends AsyncTask<String[], String, Boolean> {

	@Override
	protected Boolean doInBackground(String[]... params) {
		// TODO Auto-generated method stub
		try {
			UserFunctions userfunction = new UserFunctions();
			JSONObject jOBJ = userfunction.rateQuote(params[0].toString(), params[1].toString(),params[2].toString());
			String res = jOBJ.getString("success");
			if(Integer.parseInt(res)== 0)
				return false;
			if(Integer.parseInt(res)==1)
				return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
