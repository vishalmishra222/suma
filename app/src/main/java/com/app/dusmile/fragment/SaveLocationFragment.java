package com.app.dusmile.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.dusmile.R;
import com.app.dusmile.gps.GPSTracker;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms2.AssignedJobs;
import com.example.sumaforms2.AssignedJobsDB;
import com.example.sumaforms2.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import permission.auron.com.marshmallowpermissionhelper.FragmentManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

/**
 * Created by suma on 14/04/17.
 */

public class SaveLocationFragment extends FragmentManagePermission implements OnMapReadyCallback {
    private Context mContext;
    private String Tag = "SaveLocationFragment";
    public SaveLocationFragment() {
        super();
    }
    MapView mapView;
    private TextView titleTextView;
    GoogleMap map;
    private Button saveLocationButton;
    private DBHelper dbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_savelocation, container, false);
       // this.findViews(rootView);
        Activity activity = this.getActivity();
       // mapView = (MapView) rootView.findViewById(R.id.mapview);
        titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
        dbHelper = DBHelper.getInstance(mContext);
        saveLocationButton = (Button) rootView.findViewById(R.id.SaveLocbtn);
        mContext = activity;
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);
        titleTextView.setText("Save Location");

        onSaveLocationButtonClick();
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public static Fragment newInstance(int position) {
        SaveLocationFragment fragment = new SaveLocationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        checkPermission();
        boolean isGranted = isPermissionsGranted(getActivity(), new String[]{PermissionUtils.Manifest_ACCESS_FINE_LOCATION});
        if (isGranted) {
            GPSTracker gpsTracker = new GPSTracker(mContext);
            if (gpsTracker.canGetLocation()) {
                map.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()), 20));
            }
            else {
                showGPSSettingsAlert();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void onSaveLocationButtonClick()
    {
        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkPermission();
                    boolean isGranted = isPermissionsGranted(getActivity(), new String[]{PermissionUtils.Manifest_ACCESS_FINE_LOCATION});
                    if (isGranted) {
                        dbHelper = DBHelper.getInstance(mContext);

                        GPSTracker gpsTracker = new GPSTracker(mContext);
                        if (gpsTracker.canGetLocation()) {
                            String latitude = String.valueOf(gpsTracker.getLatitude());
                            String longitude = String.valueOf(gpsTracker.getLongitude());
                            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByProgress(dbHelper,"true");
                            AssignedJobsDB.updateAssignedJobsDependsOnJobId(dbHelper,assignedJobs.getAssigned_jobId(),latitude+","+longitude);
                            MyDynamicToast.successMessage(mContext, "Your location saved Successfully");
                        } else {
                            showGPSSettingsAlert();
                        }
                    }else {
                        MyDynamicToast.successMessage(mContext, "LOcation permission denied");
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



    //Check Premission for marshmallow
    public void checkPermission()
    {
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

}
