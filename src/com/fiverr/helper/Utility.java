package com.fiverr.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fiverr.ui.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Utility {
	
	
	public static final String [] sender= new String [] {"Lalit", "RobinHood", "Captain", "HotVerySpicy", "Dharmendra", "PareshMayani", "Abhi", "SpK", "CapDroid"};
	public static final String [] messages= new String [] {
		"Aah! thats cool",
		"Tu really CoLor 6e", 
		"Get Lost!!",
		"@AdilSoomro @AdilSoomro",
		"Lets see what the Rock is cooking..!!",
		"Yeah! thats great.",
		"Awesome Awesome!",
		"@RobinHood.",
		"Lalit ka Dillllll...!!!",
		"I'm fine, thanks, what about you?"};
	
	
	public static String readXMLinString(String fileName, Context c) {
		try {
			InputStream is = c.getAssets().open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String text = new String(buffer);

			return text;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	 public static void CopyStream(InputStream is, OutputStream os)
	    {
	        final int buffer_size=1024;
	        try
	        {
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }
	 public static void showErrorToast(String errorMsg, Context ctx) {

			LayoutInflater inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.toast_layout, null);
			TextView text = (TextView) layout.findViewById(R.id.tv_toast_msg);
			text.setText(errorMsg);

			Toast toast = new Toast(ctx);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);

			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();

		}
}
