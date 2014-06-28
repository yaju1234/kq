package com.fiverr.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseView extends Activity implements OnClickListener {
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void showProgressDailog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				pDialog = new ProgressDialog(BaseView.this);
				pDialog.setTitle("Talking To Server");
				pDialog.setMessage("Checking Credential");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();

			}
		});
	}

	public void dissmissProgressDialog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (pDialog.isShowing()) {
					pDialog.dismiss();
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
	}

}
