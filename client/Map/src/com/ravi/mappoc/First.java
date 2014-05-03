package com.ravi.mappoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class First extends Activity{
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
	}
	
 public void register(View v){
	 Intent i =new Intent(First.this,Register.class);
	 startActivity(i);
 }
 
 public void login(View v){
	 Intent i =new Intent(First.this,LoginApp.class);
	 startActivity(i);
 }
 
}
