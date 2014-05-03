package com.ravi.mappoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class AddFriend extends Activity {
	
	private ProgressDialog pDialog;
	 JSONParser jsonParser = new JSONParser();
	 private static final String LOGIN_URL = "http://192.168.1.2:80/webservice/gid.php";
	 private static final String TAG_SUCCESS = "success";
	    private static final String TAG_MESSAGE = "message";
	private static final int CONTACT_PICKER_RESULT = 1001;
	private static final String DEBUG_TAG = null;
	 private static final int[] pins = new int[900000];  
	 private static int pinCount;
	 ConnectionDetector cd;
	    Boolean isInternetPresent = false;
	String email = "";
	String grpname;
	EditText emailEntry,grpnameEntry; 
	int gids;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfriend);
		 cd = new ConnectionDetector(getApplicationContext());
		 emailEntry = (EditText) findViewById(R.id.invite_email);
		 grpnameEntry=(EditText)findViewById(R.id.choosegrpname);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void doLaunchContactPicker(View view) {
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
	            Contacts.CONTENT_URI);
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        switch (requestCode) {
	        case CONTACT_PICKER_RESULT:
	            Cursor cursor = null;
	            
	            try {
	                Uri result = data.getData();
	                Log.v(DEBUG_TAG, "Got a contact result: "
	                        + result.toString());
	                // get the contact id from the Uri
	                String id = result.getLastPathSegment();
	                // query for everything email
	                cursor = getContentResolver().query(Email.CONTENT_URI,
	                        null, Email.CONTACT_ID + "=?", new String[] { id },
	                        null);
	                int emailIdx = cursor.getColumnIndex(Email.DATA);
	                // let's just get the first email
	                if (cursor.moveToFirst()) {
	                    email = cursor.getString(emailIdx);
	                    Log.v(DEBUG_TAG, "Got email: " + email);
	                } else {
	                    Log.w(DEBUG_TAG, "No results");
	                }
	            } catch (Exception e) {
	                Log.e(DEBUG_TAG, "Failed to get email data", e);
	            } finally {
	                if (cursor != null) {
	                    cursor.close();
	                }
	                
	                emailEntry.setText(email);
	                if (email.length() == 0) {
	                    Toast.makeText(this, "No email found for contact.",
	                            Toast.LENGTH_LONG).show();
	                }
	            }
	            break;
	        }
	    } else {
	        Log.w(DEBUG_TAG, "Warning: activity result not ok");
	    }
	}
	
	public void done(View v){
		
		isInternetPresent = cd.isConnectingToInternet();
		if(isInternetPresent){
		if(gids==0){
			Toast.makeText(this, "To form group send atleast one invite.",
                    Toast.LENGTH_LONG).show();
			finish();
		}else{
			Intent i =new Intent(AddFriend.this,ReadLocations.class);
			startActivity(i);
			//finish();
		}
	}else{
		 showAlertDialog(AddFriend.this, "No Internet Connection",
                 "Wifi/3G is currently disabled on your device."+"\n"+"Would you like to enable it?", false);
	}	
}

/*@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}*/

public void SendEmail(View view){
	
	isInternetPresent = cd.isConnectingToInternet();
	/*if(emailEntry.getText().toString()==null){
		Toast.makeText(this, "Email cant be empty.",
                Toast.LENGTH_LONG).show();
	}
	
	else{
		gids=generateGroupId();
		
		new CreateGroupId().execute();
		
		String emailaddress[] = { emailEntry.getText().toString() };
		String message="Please join me on BuddyLocator.Your GroupId is"+"\n"+gids;
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation");
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		startActivity(emailIntent);
		//Toast.makeText(this, "Invitation Sent",
                //Toast.LENGTH_LONG).show();
		emailEntry.setText("");

	}*/
	
	if(isInternetPresent){
	gids=generateGroupId();
	grpname=grpnameEntry.getText().toString();
	
	new CreateGroupId().execute();
	
	String emailaddress[] = { emailEntry.getText().toString() };
	String message="Please join me on BuddyTap.Please use following credentials to join." +
			"\n"+
			" Group Id :"+gids+
			"\n"+
			"Group Name :"+grpname;
	
	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation");
	emailIntent.setType("plain/text");
	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
	startActivity(emailIntent);
	
	emailEntry.setText("");
	}else{
		 showAlertDialog(AddFriend.this, "No Internet Connection",
                 "Wifi/3G is currently disabled on your device."+"\n"+"Would you like to enable it?", false);
	}
}

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

private int generateGroupId() {
	// TODO Auto-generated method stub
	
	for (int i = 0; i < pins.length; i++)
        pins[i] = 100000 + i;
	Random random = new Random(); 
	int index = random.nextInt(pins.length - pinCount) + pinCount;
    int pin = pins[index];
    pins[index] = pins[pinCount++];
    return pin;
	/*Random random = new Random(System.nanoTime() % 100000);

	  int randomInt = random.nextInt(1000000000);
	  return randomInt;*/
    }
	
class CreateGroupId extends AsyncTask<String, String, String> {

	 /**
   * Before starting background thread Show Progress Dialog
   * */
	boolean failure = false;
	
  @Override
  protected void onPreExecute() {
      super.onPreExecute();
      pDialog = new ProgressDialog(AddFriend.this);
      pDialog.setMessage("Please wait ...");
      pDialog.setIndeterminate(false);
      pDialog.setCancelable(true);
      pDialog.show();
  }
	
	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		 // Check for success tag
      int success;
      String gid = Integer.toString(gids);
   
    //Retrieving Saved Username Data:
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddFriend.this);
      String post_username = sp.getString("username", "rac");
      
      Editor edit = sp.edit();
		
		
      try {
          // Building Parameters
          List<NameValuePair> params = new ArrayList<NameValuePair>();
          params.add(new BasicNameValuePair("gid", gid));
          params.add(new BasicNameValuePair("username", post_username)); 
          params.add(new BasicNameValuePair("groupname", grpname)); 
          Log.d("request!", "starting");
          
          //Posting user data to script 
          JSONObject json = jsonParser.makeHttpRequest(
                 LOGIN_URL, "POST", params);

          // full json response
          Log.d("Login attempt", json.toString());

          // json success element
          success = json.getInt(TAG_SUCCESS);
          if (success == 1) {
        	  edit.putString("gid", gid);
        	  edit.putString("groupname", grpname);
      	    edit.putBoolean("joined-group", true);
      		edit.commit();
          	Log.d("Id Created!", json.toString());              	
          	//finish();
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
      	Toast.makeText(AddFriend.this, file_url, Toast.LENGTH_LONG).show();
      }

  }
	
}

}
