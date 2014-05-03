package com.ravi.mappoc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginApp extends Activity implements OnClickListener {

	private EditText user, pass;
	private Button mLogin;

	 // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    
   private static final String LOGIN_URL = "http://192.168.1.2:80/webservice/login.php";

    //testing on Emulator:
   // private static final String LOGIN_URL = "http://10.0.2.2:80/webservice/login.php";

  //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		 cd = new ConnectionDetector(getApplicationContext());
		//setup input fields
		user = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);

		//setup buttons
		mLogin = (Button)findViewById(R.id.login1);
		mLogin.setOnClickListener(this);
		//mRegister = (Button)findViewById(R.id.register);

		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		isInternetPresent = cd.isConnectingToInternet();
		
		 if(isInternetPresent){
			 new AttemptLogin().execute();
		 }else{
			 showAlertDialog(LoginApp.this, "No Internet Connection",
                   "Wifi/3G is currently disabled on your device."+"\n"+"Would you like to enable it?", false);
		 }
				//new AttemptLogin().execute();
	}


	
	/*public void startlogin(View v) {
		
		isInternetPresent = cd.isConnectingToInternet();
		
		 if(isInternetPresent){
			 new AttemptLogin().execute();
		 }else{
			 showAlertDialog(LoginApp.this, "No Internet Connection",
                    "Wifi/3G is currently disabled on your device."+"\n"+"Would you like to enable it?", false);
		 }
				//new AttemptLogin().execute();
		
		
	}*/
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        //AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
        // Setting alert dialog icon
       // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });
        
        alertDialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
        	 
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        	// TODO Auto-generated method stub
        	dialog.cancel();
        	}
        	});
 
        AlertDialog alert=alertDialog.create();
        // Showing Alert Message
        alert.show();
    }

	class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginApp.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Login Successful!", json.toString());
                	
                	SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(LoginApp.this);
					Editor edit = sp.edit();
					edit.putString("username", username);
					edit.putBoolean("login", true);
					edit.commit();
					
                	Intent i = new Intent(LoginApp.this, MainActivity.class);
                	finish();
    				startActivity(i);
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

		}
		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(LoginApp.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

	}

	
	

}