package com.ravi.mappoc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class SingleFriend extends Activity{
	
	String username=""; 
	 String address="";
	 String latitude="";
	 String longitude="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_friend);
		  Intent i = getIntent();
		  username=i.getStringExtra("NAME");
		  address=i.getStringExtra("ADDRESS");
		  latitude=i.getStringExtra("LATITUDE");
		  longitude=i.getStringExtra("LONGITUDE");
		  
		  TextView lbl_name = (TextView) findViewById(R.id.name);
			TextView lbl_address = (TextView) findViewById(R.id.address);
			TextView lbl_phone = (TextView) findViewById(R.id.pdate);
			TextView lbl_location = (TextView) findViewById(R.id.location);
			
			Calendar c = Calendar.getInstance();
	    	System.out.println("Current time => "+c.getTime());

	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
	    	String formattedDate = df.format(c.getTime());

			lbl_name.setText(username);
			lbl_address.setText(address);
			lbl_phone.setText(Html.fromHtml("<b>Location Tagged On:</b> " + formattedDate));
			lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));


	}
	
	public void back(View v){
		onPause();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
