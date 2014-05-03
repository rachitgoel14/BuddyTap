package com.ravi.mappoc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(3000);					
				} catch (InterruptedException e){
					e.printStackTrace();
				}finally{
					/*Intent openStartingPoint = new Intent(SplashActivity.this,First.class);
					startActivity(openStartingPoint);*/
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
					Boolean islogin=sp.getBoolean("login", false);
					
					if(islogin){
						Intent i = new Intent(SplashActivity.this, MainActivity.class);
					    startActivity(i);
					}else{
						Intent i = new Intent(SplashActivity.this, First.class);
					    startActivity(i);
					}
					
				}
			}
		};
		timer.start();

		
		
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		finish();
	}


}