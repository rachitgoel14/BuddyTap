package com.ravi.mappoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class GroupList extends ListActivity{

	private ProgressDialog pDialog;
	
	private static final String READ_GROUPS_URL = "http://192.168.1.4:80/webservice/showgroups.php";
	
	//private static final String TAG_USERNAME = "username";
	private static final String TAG_GROUPNAME = "groupname";
	private static final String TAG_GID = "gid";
	private static final String TAG_POSTS = "posts";
	
	private JSONArray groups = null;
	private ArrayList<HashMap<String, String>> FriendGroupList;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	   
	       setContentView(R.layout.read_groups);   
	    }
	 
	 
	    @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	    	
	    	new LoadGroups().execute();
	    }
	    
	    public class LoadGroups extends AsyncTask<Void, Void, Boolean> {

	    	@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(GroupList.this);
				pDialog.setMessage("Loading Groups...");
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
	   	            updateList();
	        }
			
	    }
	    
	    private void updateJSONdata() {
			
	    	
	    	 FriendGroupList=new ArrayList<HashMap<String, String>>();
	    	 JSONParser jParser = new JSONParser();
	    	 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(GroupList.this);
	         String post_username = sp.getString("username", "rac");
	         
	         try{
	        	 List<NameValuePair> params = new ArrayList<NameValuePair>();
	             params.add(new BasicNameValuePair("username", post_username));
	             
	             Log.d("request!", "starting");
	             
	             
	             JSONObject json = jParser.makeHttpRequest(
	            		 READ_GROUPS_URL, "POST", params);

	        
	             Log.d("Post Location attempt", json.toString());
	             
	             groups=json.getJSONArray(TAG_POSTS);
	             
	             for (int i = 0; i < groups.length(); i++) {
		                JSONObject c = groups.getJSONObject(i);

		                //gets the content of each tag
		                
		                String groupname = c.getString(TAG_GROUPNAME);
		                String grpid=c.getString(TAG_GID);

		                // creating new HashMap
		                HashMap<String, String> map = new HashMap<String, String>();
		              
		                
		                map.put(TAG_GROUPNAME, groupname);
		                map.put(TAG_GID, grpid);
		             
		                // adding HashList to ArrayList
		                FriendGroupList.add(map);
		                
		               
		            }
	             
	             
	         }catch(JSONException e){
	        	 e.printStackTrace();
	         }
			
		}
	    
	    private void updateList() {
	    	ListAdapter adapter = new SimpleAdapter(this, FriendGroupList,
					R.layout.singlegroup, new String[] { TAG_GROUPNAME }, new int[] {R.id.groupname });

			setListAdapter(adapter);
			
			ListView lv = getListView();
			
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					

				}
			});
			
		}

}
