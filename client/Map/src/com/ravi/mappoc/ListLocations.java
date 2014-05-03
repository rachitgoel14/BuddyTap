package com.ravi.mappoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class ListLocations extends ListActivity {
	
	ConnectionDetector cd;
    Boolean isInternetPresent = false;
	
	private Menu optionsMenu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.listlocations, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	switch (item.getItemId()) {
    case R.id.action_refresh3:
    	Toast.makeText(getBaseContext(), "Refreshing List..", Toast.LENGTH_LONG).show();
    	setRefreshActionButtonState(true);
    	// onRestart();
    	Intent intent= new Intent(this,ListLocations.class); 
		startActivity(intent);
    return true;
    
    case R.id.map_friends:
    	Intent i = new Intent(ListLocations.this, ReadLocations.class);
	    startActivity(i);
    	return true;
    	
    }
		return super.onOptionsItemSelected(item);
	}
	
	
	/*@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		Intent intent= new Intent(this,ListLocations.class); 
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
	
	//GoogleMap gmap;

	
	private ProgressDialog pDialog;
 
	
   private static final String READ_COMMENTS_URL = "http://192.168.1.2:80/webservice/getlocations.php";
   
   //final Handler myHandler = new Handler();
   
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
   // private JSONArray mFriends = null;
    
    private ArrayList<HashMap<String, String>> mCommentList;
   // private ArrayList<HashMap<String, String>> mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note that use read_comments.xml instead of our single_post.xml
       setContentView(R.layout.read_address);   
       cd = new ConnectionDetector(getApplicationContext());
       /*Timer myTimer = new Timer();
       myTimer.schedule(new TimerTask() {
          @Override
          public void run() {UpdateGUI();}
       }, 0, 60000);*/
      
    }
    
    /*private void UpdateGUI() {
       // i++;
        //tv.setText(String.valueOf(i));
        myHandler.post(myRunnable);
     }
    
    final Runnable myRunnable = new Runnable() {
        public void run() {
        	new LoadComments().execute();
        	
        }
     };*/
     
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	//loading the friends via AsyncTask
    	
    	isInternetPresent = cd.isConnectingToInternet();
		if(isInternetPresent){
    	new LoadComments().execute();
		}else{
			 showAlertDialog(ListLocations.this, "No Internet Connection",
	                 "Wifi/3G is currently disabled on your device."+"\n"+"Would you like to enable it?", false);
			 //finish();
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

  
	    public void updateJSONdata() {
	    	
	    	//friendlist();

	    	 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ListLocations.this);
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
	                
	               
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
	

    /**
     * Inserts the parsed data into our listview
     */
	    private void updateList() {
	    	
	   
	    	/*Calendar c = Calendar.getInstance();
	    	System.out.println("Current time => "+c.getTime());

	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
	    	String formattedDate = df.format(c.getTime());*/
	    	
	    	ListAdapter adapter = new SimpleAdapter(this, mCommentList,
					R.layout.single_address, new String[] { 
							TAG_USERNAME, TAG_ADDRESS }, new int[] { R.id.username, R.id.address
							 });

			
			setListAdapter(adapter);
			
			
			ListView lv = getListView();	
			
			 lv.setOnItemClickListener(new OnItemClickListener() {
				  
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view,
		                    int position, long id) {
		            	 String username=""; 
		            	 String address="";
		            	 String latitude="";
		            	 String longitude="";
		            	 JSONObject c;
						try {
							c = mComments.getJSONObject(position);
							 latitude = c.getString(TAG_LAT);
			                 longitude = c.getString(TAG_LON);
			                username = c.getString(TAG_USERNAME);
			                 address=c.getString(TAG_ADDRESS);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

			               
		                // Starting new intent
		                Intent in = new Intent(getApplicationContext(),
		                        SingleFriend.class);
		                
		                in.putExtra("NAME", username);
		                in.putExtra("ADDRESS", address);
		                in.putExtra("LATITUDE", latitude);
		                in.putExtra("LONGITUDE", longitude);
		                startActivity(in);
		            }
		        });
			 
			 
			Toast.makeText(getBaseContext(), "Done refreshing list..", Toast.LENGTH_SHORT).show();
			/*Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();*/
		}       

    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListLocations.this);
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
          
            updateList();
            setRefreshActionButtonState(false);
        }
    }
    
    
}