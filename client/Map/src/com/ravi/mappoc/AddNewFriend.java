package com.ravi.mappoc;



import android.net.Uri;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class AddNewFriend extends Activity {
	
	
	 JSONParser jsonParser = new JSONParser();
	
	private static final int CONTACT_PICKER_RESULT = 1001;
	private static final String DEBUG_TAG = null;
	
	 ConnectionDetector cd;
	    Boolean isInternetPresent = false;
	String email = "";
	String grpname,grpid;
	EditText emailEntry; 
	int gids;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnewfriend);
		
		 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddNewFriend.this);
		 
		 cd = new ConnectionDetector(getApplicationContext());
		 emailEntry = (EditText) findViewById(R.id.invite_email);
		 grpname=sp.getString("groupname", "cool");
		 grpid=sp.getString("gid", "111111");
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
	
	/*public void done(View v){
		if(gids==0){
			Toast.makeText(this, "To form group send atleast one invite.",
                    Toast.LENGTH_LONG).show();
			finish();
		}else{
			Intent i =new Intent(AddNewFriend.this,ReadLocations.class);
			startActivity(i);
			//finish();
		}
	}*/
	
public void done(View v){
		
		isInternetPresent = cd.isConnectingToInternet();
		if(isInternetPresent){
		if(gids==0){
			Toast.makeText(this, "To form group send atleast one invite.",
                    Toast.LENGTH_LONG).show();
			finish();
		}else{
			Intent i =new Intent(AddNewFriend.this,ReadLocations.class);
			startActivity(i);
			//finish();
		}
	}else{
		 showAlertDialog(AddNewFriend.this, "No Internet Connection",
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
	
	
	if(isInternetPresent){
	gids=Integer.valueOf(grpid);
	
	
	
	
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
		 showAlertDialog(AddNewFriend.this, "No Internet Connection",
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


	


}
