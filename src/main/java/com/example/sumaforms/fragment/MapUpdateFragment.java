package com.example.sumaforms.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms.model.AssignedJobs;
import com.example.sumaforms.DBmodel.AssignedJobsDB;
import com.example.sumaforms.helper.DBHelper;
import com.example.sumaforms.R;
import com.example.sumaforms.gps.GPSTracker;
import com.example.sumaforms.utils.IOUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by suma on 03/05/17.
 */

public class MapUpdateFragment extends DialogFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    String locationKey;
    GoogleMap mGoogleMap;
    MapView mapView;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private ImageView closeMapDialog;
    private Context mContext;
    private TextView titleTextView;
    private String Tag = "MapUpdateFragment";
    private Button saveLocationButton;
    private DBHelper dbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            locationKey = bundle.getString("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

               View rootView = inflater.inflate(R.layout.fragment_savelocation, container, false);
               // this.findViews(rootView);
               try {
                   MapsInitializer.initialize(getActivity());
               } catch (Exception e) {
                   // TODO handle this situation
               }
               Activity activity = getActivity();
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
               mapView = (MapView) rootView.findViewById(R.id.mapview);
               mContext = activity;
               titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
               closeMapDialog = (ImageView) rootView.findViewById(R.id.closeMapButton);
               dbHelper = DBHelper.getInstance(mContext);
               saveLocationButton = (Button) rootView.findViewById(R.id.SaveLocbtn);
               titleTextView.setText("Save Location");
               try {
                   mapView.onCreate(savedInstanceState);
                } catch (Exception e) {

              }
               mapView.getMapAsync(this);
               onSaveLocationButtonClick();
               onCloseMapDialogButtonClick();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        try {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
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
            mapView.onResume();
            super.onResume();
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mGoogleMap = googleMap;
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Initialize Google Play Services
            buildGoogleApiClient();
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
               // MyDynamicToast.successMessage(mContext, "Please Switch On the Location!!");
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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
            mLocationRequest.setSmallestDisplacement(0.1f);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
       /* Toast.makeText(getActivity(), "Location Changed " + location.getLatitude()
                + location.getLongitude(), Toast.LENGTH_LONG).show();*/
        try {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                try {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                            mGoogleMap.setMyLocationEnabled(true);
                        }

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /*public void checkPermission()
    {
        boolean isGranted = false;
        //String permissionAsk[] = {PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,PermissionUtils.Manifest_READ_CALL_LOG,PermissionUtils.Manifest_WRITE_CALL_LOG,PermissionUtils.Manifest_READ_PHONE_STATE};
        String permissionAsk[] = {PermissionUtils.Manifest_ACCESS_FINE_LOCATION};
        askCompactPermissions(permissionAsk, new PermissionResult() {

            public void permissionGranted() {

            }


            public void permissionDenied() {

            }

            @Override
            public void permissionForeverDenied() {
                {
                    String permissionAsk[] = {PermissionUtils.Manifest_ACCESS_FINE_LOCATION};
                    boolean isGranted = isPermissionsGranted(getActivity(),permissionAsk);
                    if(!isGranted){
                        // showDialog();
                    }else {
                        System.out.println("IS GRANTED -- " + isGranted);
                    }
                }
            }
        });

    }
*/

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

        }
    }

    public static Fragment newInstance(int position) {
        MapUpdateFragment fragment = new MapUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void onSaveLocationButtonClick()
    {
        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        dbHelper = DBHelper.getInstance(mContext);
                        GPSTracker gpsTracker = new GPSTracker(mContext);
                        if (gpsTracker.canGetLocation()) {
                            String latitude = String.valueOf(gpsTracker.getLatitude());
                            String longitude = String.valueOf(gpsTracker.getLongitude());
                            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByProgress(dbHelper,"true");
                            AssignedJobsDB.updateAssignedJobsDependsOnJobId(dbHelper,assignedJobs.getAssigned_jobId(),locationKey+","+latitude+","+longitude);
                            MyDynamicToast.successMessage(mContext, "Your location saved Successfully");
                            if(getDialog()!=null)
                            {
                                getDialog().dismiss();
                            }
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
        if (mContext != null && ((Activity) mContext)!=null) {
            if (!((Activity) mContext).isFinishing()) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(mContext);
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

                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
