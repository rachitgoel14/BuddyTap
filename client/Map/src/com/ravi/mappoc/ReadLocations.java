package com.ravi.mappoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class ReadLocations extends FragmentActivity {
	private Menu optionsMenu;
	ConnectionDetector cd;
    Boolean isInternetPresent = false;

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.readlocations, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	switch (item.getItemId()) {
    case R.id.action_refresh2:
    	Toast.makeText(getBaseContext(), "Refreshing map..", Toast.LENGTH_LONG).show();
    	setRefreshActionButtonState(true);
    	 //onRestart();
    	Intent intent= new Intent(this,ReadLocations.class); 
		startActivity(intent);
    return true;
    
    case R.id.list_friends:
    	listfriends();
    	
    	return true;
    	
    case R.id.add_friend:
    	
    	Intent i= new Intent(this,AddNewFriend.class); 
		startActivity(i);
    	return true;
    	
   /* case R.id.form_group:
    	
    	return true;
    
    case R.id.leave_group:
    	
    	return true;*/
    }
		return super.onOptionsItemSelected(item);
	}
	
	private void listfriends() {
		Intent i =new Intent(ReadLocations.this,ListLocations.class);
    	startActivity(i);
		//finish();
	}

	/*protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		Intent intent= new Intent(this,ReadLocations.class); 
		startActivity(intent);
		finish();
		
	}*/
	
	public void setRefreshActionButtonState(final boolean refreshing) {
	    if (optionsMenu != null) {
	        final MenuItem refreshItem = optionsMenu
	            .findItem(R.id.action_refresh);
	        if (refreshItem != null) {
	            if (refreshing) {
	                refreshItem.setActionView(R.layout.progress);
	            } else {
	                refreshItem.setActionView(null);
	            }
	        }
	    }
	}
	
	GoogleMap gmap;

	
	private ProgressDialog pDialog;
 
	
   private static final String READ_COMMENTS_URL = "http://192.168.1.2:80/webservice/getlocations.php";
   //private static final String FRIEND_LIST_URL = "http://192.168.1.4:80/webservice/friendlist.php"; 
    //testing on Emulator:
    //private static final String READ_COMMENTS_URL = "http://10.0.2.2:1234/webservice/comments.php";
    
  
   
  @Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	
	Intent intent = new Intent(ReadLocations.this, MainActivity.class);
    Log.i("Hello", "This is Coomon Log");
    startActivity(intent);
       return;
}

	//JSON IDS:
   // private static final String TAG_SUCCESS = "success";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_POSTS = "posts";
  //  private static final String TAG_POST_ID = "app_id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_LON = "longitude";
    
    

   //An array of all of our comments
    private JSONArray mComments = null;
   // private JSONArray mFriends = null;
    
    private ArrayList<HashMap<String, String>> mCommentList;
    //private ArrayList<HashMap<String, String>> mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note that use read_comments.xml instead of our single_post.xml
       setContentView(R.layout.mark);   
       cd = new ConnectionDetector(getApplicationContext());
       if(gmap==null){
    		SupportMapFragment smf	= ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map1));
    		 gmap=smf.getMap();
       }	 
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	//loading the comments via AsyncTask
    	isInternetPresent = cd.isConnectingToInternet();
		if(isInternetPresent){
    	new LoadComments().execute();
		}else{
			 showAlertDialog(ReadLocations.this, "No Internet Connection",
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
        	finish();
        	}
        	});

        AlertDialog alert=alertDialog.create();
        // Showing Alert Message
        alert.show();
    }
    
/*	public void friendlist(){
		 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ReadLocations.this);
	      String post_groupid = sp.getString("gid", "111111");

	      JSONParser jsonParser = new JSONParser();
	      mFriendList = new ArrayList<HashMap<String, String>>();
			 // Check for success tag
          int success;
          
          try {
              // Building Parameters
              List<NameValuePair> params = new ArrayList<NameValuePair>();
              
              params.add(new BasicNameValuePair("gid", post_groupid));

              Log.d("request!", "starting");
              // getting usernames  by making HTTP request
              JSONObject json = jsonParser.makeHttpRequest(
            		  FRIEND_LIST_URL, "POST", params);
              try{
            	  mFriends = json.getJSONArray(TAG_POSTS);
            	  
            	  for (int i = 0; i < mFriends.length(); i++) {
  	                JSONObject c = mFriends.getJSONObject(i);

  	                //gets the content of each tag
  	               
  	                String username = c.getString(TAG_USERNAME);
  	                

  	                // creating new HashMap
  	                HashMap<String, String> map = new HashMap<String, String>();
  	              
  	               
  	                map.put(TAG_USERNAME, username);
  	             
  	                // adding HashList to ArrayList
  	                mFriendList.add(map);
  	                
  	                
  	                //annndddd, our JSON data is up to date same with our array list
  	            }
              }catch(JSONException e){
            	  
              }

             
          } catch (Exception e) {
              e.printStackTrace();
          }

        

		
	}*/

  
	    public void updateJSONdata() {
	    	
	    	//friendlist();

	    	 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ReadLocations.this);
		      String post_groupid = sp.getString("gid", "111111");
	    	//String post_groupid="111111";
	    	
	        mCommentList = new ArrayList<HashMap<String, String>>();
	       // ArrayList<String> friends = new ArrayList<String>();
	        
	        JSONParser jParser = new JSONParser();
	        
	        //JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

	       
	        try {
	        	List<NameValuePair> params = new ArrayList<NameValuePair>();
               
                params.add(new BasicNameValuePair("gid", post_groupid));
                
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jParser.makeHttpRequest(
                		READ_COMMENTS_URL, "POST", params);

	        	
	            mComments = json.getJSONArray(TAG_POSTS);

	            // looping through all posts according to the json object returned
	            for (int i = 0; i < mComments.length(); i++) {
	                JSONObject c = mComments.getJSONObject(i);

	                //gets the content of each tag
	                String latitude = c.getString(TAG_LAT);
	                String longitude = c.getString(TAG_LON);
	               String username = c.getString(TAG_USERNAME);
	                
	                

	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	              
	                map.put(TAG_LAT, latitude);
	                map.put(TAG_LON, longitude);
	                map.put(TAG_USERNAME, username);
	             
	                // adding HashList to ArrayList
	                mCommentList.add(map);
	                
	                //annndddd, our JSON data is up to date same with our array list
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
	

    /**
     * Inserts the parsed data into our listview
     */
	    private void updateMap() {
	    	
	   
	    	
           try {
	           

	            
	            for (int i = 0; i < mComments.length(); i++) {
	                JSONObject c = mComments.getJSONObject(i);

	                //gets the content of each tag
	                String latitude = c.getString(TAG_LAT);
	                String longitude = c.getString(TAG_LON);
	                String username = c.getString(TAG_USERNAME);
	                
	                double lat=Double.valueOf(latitude);
	                double lon=Double.valueOf(longitude);
	                
	                LatLng latlng= new LatLng(lat, lon);
	            	
	            	gmap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
	            	
	            	gmap.animateCamera(CameraUpdateFactory.zoomTo(10));
	            	gmap.addMarker(new MarkerOptions().position(latlng).title(username));
	            	
	                

	                
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    	
			
		}       

    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReadLocations.this);
			pDialog.setMessage("Loading Locations...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	
            updateJSONdata();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
          
            updateMap();
            setRefreshActionButtonState(false);
        }
    }
    
    
}