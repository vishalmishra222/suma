package com.app.dusmile.gps;
import com.app.dusmile.R;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms2.AssignedJobs;
import com.example.sumaforms2.AssignedJobsDB;
import com.example.sumaforms2.DBHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MapDemoActivity extends AppCompatActivity implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener{
	private GoogleMap mMap;
	String locationKey;
	GoogleApiClient mGoogleApiClient;
	Location mLastLocation;
	Marker mCurrLocationMarker;
	LocationRequest mLocationRequest;
	private ImageView closeMapDialog;
	private String Tag = "LocationActivity";
	private Button saveLocationButton;
	private TextView titleTextView;
	private DBHelper dbHelper;
	private static String latitude, longitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checkLocationPermission();
		}
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */


	protected synchronized void buildGoogleApiClient() {
		try {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
			mGoogleApiClient.connect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		try {
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(1000);
			mLocationRequest.setFastestInterval(1000);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED) {
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onLocationChanged(Location location) {
		try {
			mLastLocation = location;
			if (mCurrLocationMarker != null) {
				mCurrLocationMarker.remove();
			}
			//Place current location marker
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
           latitude = String.valueOf(location.getLatitude());
		   longitude = String.valueOf(location.getLongitude());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
	public boolean checkLocationPermission(){
		boolean isPermissionGranted = false;
		try {
			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {

				// Asking user if explanation is needed
				if (ActivityCompat.shouldShowRequestPermissionRationale(this,
						Manifest.permission.ACCESS_FINE_LOCATION)) {

					// Show an explanation to the user *asynchronously* -- don't block
					// this thread waiting for the user's response! After the user
					// sees the explanation, try again to request the permission.

					//Prompt the user once explanation has been shown
					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
							MY_PERMISSIONS_REQUEST_LOCATION);


				} else {
					// No explanation needed, we can request the permission.
					ActivityCompat.requestPermissions(this,
							new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
							MY_PERMISSIONS_REQUEST_LOCATION);
				}
				isPermissionGranted =  false;
			} else {
				isPermissionGranted =  true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return isPermissionGranted;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_LOCATION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted. Do the
					// contacts-related task you need to do.
					if (ContextCompat.checkSelfPermission(this,
							Manifest.permission.ACCESS_FINE_LOCATION)
							== PackageManager.PERMISSION_GRANTED) {

						if (mGoogleApiClient == null) {
							buildGoogleApiClient();
						}
						mMap.setMyLocationEnabled(true);
					}

				} else {

					// Permission denied, Disable the functionality that depends on this permission.
					Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
				}
				return;
			}

			// other 'case' lines to check for other permissions this app might request.
			// You can add here other case statements according to your requirement.
		}
	}

	private void onSaveLocationButtonClick()
	{
		saveLocationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dbHelper = DBHelper.getInstance(getApplicationContext());
					GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
					if (gpsTracker.canGetLocation()) {
						String latitude = String.valueOf(gpsTracker.getLatitude());
						String longitude = String.valueOf(gpsTracker.getLongitude());
						AssignedJobs assignedJobs = AssignedJobsDB.getJobsByProgress(dbHelper,"true");
						AssignedJobsDB.updateAssignedJobsDependsOnJobId(dbHelper,assignedJobs.getAssigned_jobId(),locationKey+","+latitude+","+longitude);
						MyDynamicToast.successMessage(getApplicationContext(), "Your location saved Successfully");
						IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+"Location saved successfully"+ " "+latitude +" "+longitude);
						finish();
					} else {
						showGPSSettingsAlert();
					}
					IOUtils.appendLog(Tag+": Saved user location successfully");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}


	public void showGPSSettingsAlert() {
		if (getApplicationContext() != null && ((Activity) getApplicationContext())!=null) {
			if (!((Activity) getApplicationContext()).isFinishing()) {
				android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getApplicationContext());
				alertDialog.setCancelable(false);
				// Setting Dialog Title
				alertDialog.setTitle(getString(R.string.app_name));

				// Setting Dialog Message
				alertDialog
						.setMessage("Not able access your location. GPS need to be ON.");

				// On pressing Settings button
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(intent, 1);
								//((Activity) mContext).finish();
								dialog.cancel();
							}
						});

           /* // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });*/

				// Showing Alert Message
				alertDialog.show();
			}
		}
	}

	private void onCloseMapDialogButtonClick()
	{
		closeMapDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	@Override
	public void onPause() {
		super.onPause();

		//stop location updates when Activity is no longer active
		try {
			if (mGoogleApiClient != null) {
				LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		try {
			super.onResume();
			if (mGoogleApiClient != null) {
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
