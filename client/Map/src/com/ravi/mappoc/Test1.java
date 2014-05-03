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


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Test1 extends ListActivity {
	
	GoogleMap gmap;

	
	private ProgressDialog pDialog;
 
	
   private static final String READ_COMMENTS_URL = "http://192.168.1.4:80/webservice/getlocations.php";
   //private static final String FRIEND_LIST_URL = "http://192.168.1.4:80/webservice/friendlist.php"; 
    //testing on Emulator:
    //private static final String READ_COMMENTS_URL = "http://10.0.2.2:1234/webservice/comments.php";
    
  
   
  //JSON IDS:
   // private static final String TAG_SUCCESS = "success";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_POSTS = "posts";
  //  private static final String TAG_POST_ID = "app_id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_LON = "longitude";
    
    

   //An array of all of our comments
    private JSONArray mComments = null;
    private JSONArray mFriends = null;
    
    private ArrayList<HashMap<String, String>> mCommentList;
    private ArrayList<HashMap<String, String>> mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note that use read_comments.xml instead of our single_post.xml
       setContentView(R.layout.read_address);   
       
       /*if(gmap==null){
    		SupportMapFragment smf	= ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map1));
    		 gmap=smf.getMap();
       }	*/ 
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	//loading the comments via AsyncTask
    	new LoadComments().execute();
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

	    	 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Test1.this);
		      String post_groupid = sp.getString("gid", "56789");
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
	                String address=c.getString(TAG_ADDRESS);
	                

	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	              
	                map.put(TAG_LAT, latitude);
	                map.put(TAG_LON, longitude);
	                map.put(TAG_USERNAME, username);
	                map.put(TAG_ADDRESS, address);
	             
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
	    	
	   
	    	/*
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
	            	
	            	gmap.animateCamera(CameraUpdateFactory.zoomTo(14));
	            	gmap.addMarker(new MarkerOptions().position(latlng).title(username));
	            	
	                

	                
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }*/
	    	ListAdapter adapter = new SimpleAdapter(this, mCommentList,
					R.layout.single_address, new String[] { 
							TAG_USERNAME, TAG_ADDRESS }, new int[] { R.id.username, R.id.address,
							 });

			
			setListAdapter(adapter);
			
			
			ListView lv = getListView();	
			
		}       

    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Test1.this);
			pDialog.setMessage("Loading Locations...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
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
        }
    }
    
    
}