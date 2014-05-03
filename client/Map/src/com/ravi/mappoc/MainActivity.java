package com.ravi.mappoc;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


//import com.example.reversegeocode.MainActivity.ReverseGeocodingTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
 
import android.support.v4.app.FragmentActivity;
import android.util.Log;
 
public class MainActivity extends FragmentActivity implements LocationListener{
	private Menu optionsMenu;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	switch (item.getItemId()) {
    case R.id.action_refresh:
    	Toast.makeText(getBaseContext(), "Refreshing map..", Toast.LENGTH_LONG).show();
    	setRefreshActionButtonState(true);
    	 onRestart();
    return true;
    
    case R.id.locate_friends:
    	
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    	Boolean isTaggedOnce=sp.getBoolean("tagged-once", false);
    	if(isTaggedOnce){
    	viewFriends();
    	}else{
    		showAlertDialogOneButton(MainActivity.this, "Error",
                    "To locate friends, tag your current location atleast once", false);
    	}
    	return true;
    	
    
    }
		return super.onOptionsItemSelected(item);
	}


@Override
protected void onRestart() {
	// TODO Auto-generated method stub
	super.onRestart();
	
	Intent intent= new Intent(this,MainActivity.class); 
	startActivity(intent);
	finish();
	
}

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


protected LocationManager locationManager;
protected LocationListener locationListener;
protected Context context;
GoogleMap gmap;

String provider;
String result;
String s;
double lat;
double lon;
MarkerOptions markerOptions;

private ProgressDialog pDialog;

// JSON parser class
JSONParser jsonParser = new JSONParser();

ConnectionDetector cd;
Boolean isInternetPresent = false;



private static final String POST_Location_URL = "http://192.168.1.2:80/webservice/savelocations.php";


//ids
private static final String TAG_SUCCESS = "success";
private static final String TAG_MESSAGE = "message";


String NoNetworkmsg = "", GpsErrmsg = "";
String addressText="";


 
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.main);

cd = new ConnectionDetector(getApplicationContext());

if(gmap==null){
	SupportMapFragment smf	= ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
	 gmap=smf.getMap();
	 
	 
	 
	 
}

setUpVariables();
 
}



private void setUpVariables() {
	// TODO Auto-generated method stub
	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	
	

	

	//Creating an empty criteria object
	Criteria criteria = new Criteria();

	// Getting the name of the provider that meets the criteria
	provider = locationManager.getBestProvider(criteria, false);

	if(provider!=null && !provider.equals("")){

	    // Get the location from the given provider
	    Location location = locationManager.getLastKnownLocation(provider);

	    locationManager.requestLocationUpdates(provider, 20000, 30, this);
	    
	    //for checking  gps connection
	    boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!gps_enabled) 
		{
			NoNetworkmsg = "Your current location was not determined. Your GPS/Network is off. Please enable and retry!.";

			GpsErrmsg = "No Gps Connectivity";
			if (!gps_enabled) {
				
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
			} 
			

		} 

	    if(location!=null)
	        onLocationChanged(location);
	    else
	        Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

	}else{
	    Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
	     }
}





@Override
public void onLocationChanged(Location location) {

	lat=location.getLatitude();
	lon=location.getLongitude();

	LatLng latlng= new LatLng(lat, lon);
	gmap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
	gmap.animateCamera(CameraUpdateFactory.zoomTo(14));
	//gmap.addMarker(new MarkerOptions().position(latlng).title("U R HERE"));
	
	markerOptions = new MarkerOptions();
	markerOptions.position(latlng);
	//markerOptions.title("U ARE HERE");

	// gmap.addMarker(markerOptions);
	
	 new ReverseGeocodingTask(getBaseContext()).execute(latlng);

	 setRefreshActionButtonState(false);
	
}

public void viewFriends()
{
   // check  whwther the user has already joined a group or not.
	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
	Boolean isJoined=sp.getBoolean("joined-group", false);
	
	if(isJoined){
		Intent i = new Intent(MainActivity.this, ReadLocations.class);
	    startActivity(i);
	}else{
		Intent i = new Intent(MainActivity.this, Group.class);
	    startActivity(i);
	}
	
	
}

public void TagMe(View v)
{
	
	isInternetPresent = cd.isConnectingToInternet();
	
	
	 if(isInternetPresent){
		 new PostLocation().execute();
	 }else{
		 showAlertDialogTwoButtons(MainActivity.this, "No Internet Connection",
                 "Wifi/3G is currently disabled on your device."+"\n"+"Would you like to enable it?", false);
	 }
	
}

public void showAlertDialogTwoButtons(Context context, String title, String message, Boolean status) {
    //AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
    // Setting Dialog Title
    alertDialog.setTitle(title);

    // Setting Dialog Message
    alertDialog.setMessage(message);
     
    // Setting alert dialog icon
   // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

    
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

public void showAlertDialogOneButton(Context context, String title, String message, Boolean status) {
    //AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
    // Setting Dialog Title
    alertDialog.setTitle(title);

    // Setting Dialog Message
    alertDialog.setMessage(message);
     
    // Setting alert dialog icon
   // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

   
    
    alertDialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
    	 
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

 
@Override
public void onProviderDisabled(String provider) {
Log.d("Latitude","disable");
}
 
@Override
public void onProviderEnabled(String provider) {
Log.d("Latitude","enable");
}
 
@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
Log.d("Latitude","status");
}

class PostLocation extends AsyncTask<String, String, String> {
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Saving Location...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
	
	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		 // Check for success tag
        int success;
        
       
       String post_lat = String.valueOf((long)lat);
       String post_lon = String.valueOf((long)lon);
        
      //Retrieving Saved Username Data:
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String post_username = sp.getString("username", "rac");
        
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", post_username));
            params.add(new BasicNameValuePair("latitude", post_lat));
            params.add(new BasicNameValuePair("longitude", post_lon));
            params.add(new BasicNameValuePair("address", addressText));
            Log.d("request!", "starting");
            
            //Posting user data to script 
            JSONObject json = jsonParser.makeHttpRequest(
            		POST_Location_URL, "POST", params);

            // full json response
            Log.d("Post Location attempt", json.toString());

            // json success element
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
            	Log.d("Location Added!", json.toString()); 
            	
            	Editor edit = sp.edit();
            	edit.putBoolean("tagged-once", true);
            	edit.commit();
            	//finish();
            	return json.getString(TAG_MESSAGE);
            }else{
            	Log.d("Location Failure!", json.getString(TAG_MESSAGE));
            	return json.getString(TAG_MESSAGE);
            	
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
		
	}
	
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once product deleted
        pDialog.dismiss();
        if (file_url != null){
        	Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
        }

    }
	
}
	 
private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String>{
    Context mContext;

    public ReverseGeocodingTask(Context context){
        super();
        mContext = context;
    }

    // Finding address using reverse geocoding
    @Override
    protected String doInBackground(LatLng... params) {
        Geocoder geocoder = new Geocoder(mContext);
        double latitude = params[0].latitude;
        double longitude = params[0].longitude;

        List<Address> addresses = null;
       

        try {
            addresses = geocoder.getFromLocation(latitude, longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses != null && addresses.size() > 0 ){
            Address address = addresses.get(0);

            addressText = String.format("%s, %s, %s",
            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
            address.getLocality(),
            address.getCountryName());
        }

        return addressText;
    }

    @Override
    protected void onPostExecute(String addressText) {
        // Setting the title for the marker.
        // This will be displayed on taping the marker
       // markerOptions.title(addressText);

        // Placing a marker on the touched position
        //googleMap.addMarker(markerOptions);
    	if(addressText.equals("")){
    		addressText="Anand Rd, Udyog Vihar Phase IV, Atlas Chowk , Gurgaon India";
    		markerOptions.title(addressText);
    		gmap.addMarker(markerOptions);
    		Toast.makeText(getBaseContext(), addressText, Toast.LENGTH_LONG).show();
    		
    	}else{
    		markerOptions.title(addressText);
    		gmap.addMarker(markerOptions);
    	Toast.makeText(getBaseContext(), addressText, Toast.LENGTH_LONG).show();
    	}
    }
}

}
