package com.ravi.mappoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Group extends Activity{
	
	/*@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}*/

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
		setContentView(R.layout.form_join_group);
	}
	
 public void joinexisting(View v){
	 Intent i =new Intent(Group.this,LoginGroup.class);
	 startActivity(i);
 }
 
 public void newgroup(View v){
	 Intent i =new Intent(Group.this,AddFriend.class);
	 startActivity(i);
 }

}
