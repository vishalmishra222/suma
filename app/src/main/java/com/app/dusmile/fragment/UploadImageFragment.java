package com.app.dusmile.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.R;
import com.app.dusmile.adapter.UploadImageAdapter;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.database.ClientTemplateDB;
import com.app.dusmile.gps.GPSTracker;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.BitmapCompression;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms2.AssignedJobs;
import com.example.sumaforms2.AssignedJobsDB;
import com.example.sumaforms2.BtnClickListener;
import com.example.sumaforms2.DBHelper;
import com.example.sumaforms2.DatabaseUI;
import com.example.sumaforms2.ImageOperationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by suma on 21/03/17.
 */

public class UploadImageFragment extends DialogFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    private Context mContext;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private UploadImageAdapter uploadImageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Uri imageUri;
    private static double latitude, longitude;
    List<String> imageList;
    private String selectedForm;
    private int selectedPosition;
    String jobId,clientName;
    private ImageView closeUploadButton;
    private String Tag = "UploadImageFragment";
    private TextView titleTextView;
    private Button btnDone;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    HashMap<String, ArrayList<Bitmap>> hashMapImages = new HashMap<>();
    HashMap<String, ArrayList<String>> hashMapFilenames = new HashMap<>();
    private static final int CHOOSE_FILE_REQUESTCODE = 101;
    private static final String[] INITIAL_PERMS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final String[] INITIAL_PERMS_CAMERA = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int INITIAL_REQUEST_CAMERA = 1339;
    private static final int REQUEST_CAMERA = 0;
    int position;
    public UploadImageFragment() {
        super();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        mContext = activity;
        latitude = 0.0;
        longitude = 0.0;
        dbHelper = DBHelper.getInstance(mContext);
        AssignedJobs currentAssignedJob = AssignedJobsDB.getJobsByProgress(dbHelper,"true");
        String clientTemplateId = currentAssignedJob.getClient_template_id();
        ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplate(dbHelper,clientTemplateId);
        jobId = currentAssignedJob.getAssigned_jobId();
        clientName = clientTemplate.getClient_name();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload_image ,container, false);
        this.findViews(rootView);
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        if(!TextUtils.isEmpty(jobId) && !TextUtils.isEmpty(clientName))
        {
            titleTextView.setText("Upload Image");
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/"+ UserPreference.getUserRecord(mContext).getUsername()+"/"+jobId);
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }
        }
        onCloseUploadDialogButtonClick();
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
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

    private void findViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.upload_image_recycler_view);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        closeUploadButton = (ImageView) view.findViewById(R.id.closeUploadButton);

    }

     private void setAdapter()
    {
        try {
            hashMapImages.clear();
            hashMapFilenames.clear();
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Setting images into adapter");
        /*String imagesJson = LoginTemplateDB.getLoginJsontemplateImageJson(dbHelper,"Images", UserPreference.getLanguage(mContext));
        if(imagesJson!=null && !imagesJson.isEmpty())
        {
            imagesJson = "{\"images\":"+imagesJson+"}";
            Gson gson = new Gson();
            ImagesModel imagesModel = gson.fromJson(imagesJson,ImagesModel.class);
            imageList = new ArrayList<>();
            imageList = imagesModel.getImages();*/
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                imageList = new ArrayList<>();
                imageList = bundle.getStringArrayList("imagesList");
                hashMapImages = getAlreadyExistingImagesHashmap(jobId);
                if(imageList.size()>0) {
                    uploadImageAdapter = new UploadImageAdapter(mContext, imageList, getActivity(), btnClickListener, mCallBack, hashMapImages, hashMapFilenames);
                    recyclerView.setAdapter(uploadImageAdapter);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void openMenu() {
        final CharSequence[] items = {mContext.getResources().getString(R.string.capture_photo)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.uploadPhoto));
        builder.setIcon(R.drawable.camera_dialog);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= Build.VERSION_CODES.M) {
                        if (!(canAccessCamera() && canAccessFiles())) {
                            requestPermissions(INITIAL_PERMS_CAMERA, INITIAL_REQUEST_CAMERA);
                        } else {
                            cameraIntent();
                        }
                    } else {
                        cameraIntent();
                    }
                } else if (item == 1) {
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= Build.VERSION_CODES.M) {
                        if (!canAccessFiles()) {
                           requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                        } else {

                            browseFiles("image/*");
                        }
                    } else {
                        browseFiles("image/*");
                    }
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openDeviceCamera()
    {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (!(canAccessCamera() && canAccessFiles())) {
                requestPermissions(INITIAL_PERMS_CAMERA, INITIAL_REQUEST_CAMERA);
            } else {
                cameraIntent();
            }
        } else {
            cameraIntent();
        }
    }

    private boolean canAccessCamera() {
        return (hasPermissionCamera(android.Manifest.permission.CAMERA));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasPermissionCamera(String perm) {
        return (PackageManager.PERMISSION_GRANTED == mContext.checkSelfPermission(perm));
    }

    private boolean canAccessFiles() {
        return (hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasPermission(String perm, String perm2) {
        return (PackageManager.PERMISSION_GRANTED == mContext.checkSelfPermission(perm) && PackageManager.PERMISSION_GRANTED == mContext.checkSelfPermission(perm2));
    }

    private void cameraIntent()
    {
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Open camera to capture images");
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                imageUri = mContext.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                imageUri = mContext.getContentResolver().insert(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exception occured while capturing images "+e.getMessage());
        }
    }

    private void browseFiles(String minmeType) {
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Browse images from gallery");
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(minmeType);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // special intent for Samsung file manager
            Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            // if you want any file type, you can skip next line
            sIntent.putExtra("CONTENT_TYPE", minmeType);
            sIntent.addCategory(Intent.CATEGORY_DEFAULT);

            Intent chooserIntent;
            if (getActivity().getPackageManager().resolveActivity(sIntent, 0) != null) {
                // it is device with samsung file manager
                chooserIntent = Intent.createChooser(sIntent, "Open file");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
            } else {
                chooserIntent = Intent.createChooser(intent, "Open file");
            }

            try {
               startActivityForResult(chooserIntent, CHOOSE_FILE_REQUESTCODE);

            } catch (ActivityNotFoundException ex) {

               // Toast.makeText(mContext, "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                MyDynamicToast.errorMessage(mContext, "No suitable File Manager was found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exception occured while browsing images from gallery "+e.getMessage());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {

            return;
        }
        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                imageUri = data.getData();
                convertAndSaveBitmap();
                break;
            case REQUEST_CAMERA:
                convertAndSaveBitmap();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

   BtnClickListener btnClickListener = new BtnClickListener() {
       @Override
       public void uploadImageListener() {

       }

       @Override
       public void saveListerners() {

       }

       @Override
       public void submitListener() {

       }

       @Override
       public void audioListener() {

       }

       @Override
       public void cancelListener() {

       }

       @Override
       public void clickListener(String val) {

       }

       @Override
       public void buttonListener(int pos) {
           openDeviceCamera();
           selectedPosition = pos;
           selectedForm = imageList.get(pos);
       }

       @Override
       public void viewListener(int pos) {

       }

       @Override
       public void sendGeoLocationListener() {

       }

       @Override
       public void holdListener(String date, String reason, String jobID, String nbfcName) {

       }




       @Override
       public void showHoldPopupListener(int pos) {

       }

   };


    private void convertAndSaveBitmap() {
        try {
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Convert and save bitmap in local");
            Bitmap decoded = BitmapCompression.bitmapCompressor(getActivity(),imageUri);
            Matrix mat = new Matrix();
            int height = decoded.getHeight();
            int width = decoded.getWidth();
            if(width>height) {
                mat.setRotate(90);
            }
            Bitmap bmpPic1 = Bitmap.createBitmap(decoded, 0, 0, decoded.getWidth(), decoded.getHeight(), mat, true);
            DatabaseUI.createCropImageDialog(mContext,bmpPic1,mCallBack);

        } catch (Exception e) {
            Log.e("Gallery", "Error while creating temp file", e);
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+"Exception occured while saving bitmap in local "+e.getMessage());
        }
    }

    ImageOperationListener mCallBack = new ImageOperationListener() {
        @Override
        public void imageDelete(String formName, int formPosition, int imagePosition) {
            try {
                IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Deleting image");
                File sdCard = Environment.getExternalStorageDirectory();
                File file = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/"+ UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername()+"/"+jobId+"/"+formName+"_"+formPosition+"_"+imagePosition+".jpg");
                if(file.exists()){
                    file.delete();
                   // Toast.makeText(mContext,"Image Deleted Successfully",Toast.LENGTH_LONG).show();
                    MyDynamicToast.successMessage(mContext, "Image Deleted Successfully");
                    setAdapter();
                }

            }
            catch (Exception e)
            {
                IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exception occured while deleting image "+e.getMessage());
                e.printStackTrace();
            }
        }
        @Override
        public void cropImageListener(Bitmap bitmap) {
           setCropImageToGallery(bitmap);
        }
    };


    private void setCropImageToGallery(Bitmap bitmap)
    {
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Set cropped image to gallery");
        try {
              AssignedJobs currentAssignedJob = AssignedJobsDB.getJobsByProgress(dbHelper,"true");
              String jobId = currentAssignedJob.getAssigned_jobId();
              hashMapImages = getAlreadyExistingImagesHashmap(jobId);
              int addPosition = 0;
            if (hashMapImages.containsKey(selectedForm)) {
                //addPosition = hashMapImages.get(selectedForm).size() + 1;
               int maxPos = getMaxPosition(selectedForm);
                addPosition = maxPos+1;
            }else{
                addPosition = 1;
            }
            FileOutputStream out = null;

            String new_filename = selectedForm+"_"+selectedPosition+"_"+addPosition+".jpg";
            String current_filename = BitmapCompression.getFilename(new_filename,jobId);
            out = new FileOutputStream(current_filename);
//          write the compressed bitmap at the destination specified by filename.
          /*  GPSTracker gpsTracker = new GPSTracker(mContext);
            if(gpsTracker.canGetLocation()) {
                double new_Latitude = Double.parseDouble(new DecimalFormat("##.######").format(gpsTracker.getLatitude()));
                double new_Longitude = Double.parseDouble(new DecimalFormat("##.######").format(gpsTracker.getLongitude()));
                bitmap = IOUtils.mark(bitmap,"x = "+new_Latitude, "y = "+new_Longitude);
            }*/
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " Updating Job status");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date currentDate = new Date();
            String currentTimeStamp = sdf.format(currentDate);

            GPSTracker gpsTracker = new GPSTracker(mContext);
            if(isLocationEnabled() || gpsTracker.canGetLocation())
            {
                if(latitude>0.0 && longitude>0.0) {
                    double new_Latitude = Double.parseDouble(new DecimalFormat("##.######").format(latitude));
                    double new_Longitude = Double.parseDouble(new DecimalFormat("##.######").format(longitude));
                    bitmap = IOUtils.mark(bitmap, "x = " + new_Latitude, "y = " + new_Longitude,currentTimeStamp);
                }
                else
                {

                    double new_Latitude = Double.parseDouble(new DecimalFormat("##.######").format(gpsTracker.getLatitude()));
                    double new_Longitude = Double.parseDouble(new DecimalFormat("##.######").format(gpsTracker.getLongitude()));
                   bitmap = IOUtils.mark(bitmap, "x = " + new_Latitude, "y = " + new_Longitude,currentTimeStamp);
                }
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            Bitmap decoded = BitmapCompression.bitmapCompressor(mContext,current_filename);

            if (hashMapImages.containsKey(selectedForm)) {
                ArrayList<Bitmap> arrlBmp = hashMapImages.get(selectedForm);
                ArrayList<String> fileNameList = hashMapFilenames.get(selectedForm);
                arrlBmp.add(decoded);
                fileNameList.add(new_filename.split("\\.")[0]);
                hashMapImages.put(selectedForm, arrlBmp);
                hashMapFilenames.put(selectedForm, fileNameList);

            } else {
                ArrayList<Bitmap> arrayListbmp = new ArrayList<>();
                ArrayList<String> fileNameList = new ArrayList<>();
                arrayListbmp.add(decoded);
                fileNameList.add(new_filename.split("\\.")[0]);
                hashMapImages.put(selectedForm, arrayListbmp);
                hashMapFilenames.put(selectedForm, fileNameList);

            }
            uploadImageAdapter = new UploadImageAdapter(mContext,imageList,getActivity(),btnClickListener,mCallBack,hashMapImages,hashMapFilenames);
            uploadImageAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(uploadImageAdapter);
            MyDynamicToast.successMessage(mContext, "Image Saved Successfully");
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exception occured while adding cropped image to gallery "+e.getMessage());
        }

    }

    private HashMap<String, ArrayList<Bitmap>> getAlreadyExistingImagesHashmap(String jobId)
    {
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Get already existing images from folder");
        int imageUploaded = 0;   //pdf not created = 0, uploaded successfully = 1, not uploaded = 2
        try {
            hashMapImages.clear();
            hashMapFilenames.clear();
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/"+ UserPreference.getUserRecord(mContext).getUsername()+"/"+jobId);
            if(directory.isDirectory()){
                int i = 0;
                for (File c : directory.listFiles()) {
                    String fileName = c.getName();
                    String [] fileName_arr = fileName.split("_");
                    String selectedForm = fileName_arr[0];
                    int selectedPosition = Integer.parseInt(fileName_arr[1]);
                    String lastPos = fileName_arr[2].substring(0,fileName_arr[2].lastIndexOf("."));
                    int addPosition  = Integer.parseInt(lastPos);
                    Bitmap decoded = BitmapFactory.decodeFile(c.getPath());

                    if (hashMapImages.containsKey(selectedForm)) {
                        ArrayList<Bitmap> arrlBmp = hashMapImages.get(selectedForm);
                        ArrayList<String> fileNameList = hashMapFilenames.get(selectedForm);
                        arrlBmp.add(decoded);
                        fileNameList.add(fileName.split("\\.")[0]);
                        hashMapImages.put(selectedForm, arrlBmp);
                        hashMapFilenames.put(selectedForm,fileNameList);
                    } else {
                        ArrayList<Bitmap> arrayListbmp = new ArrayList<>();
                        ArrayList<String> fileNameList = new ArrayList<>();
                        arrayListbmp.add(decoded);
                        fileNameList.add(fileName.split("\\.")[0]);
                        hashMapImages.put(selectedForm, arrayListbmp);
                        hashMapFilenames.put(selectedForm,fileNameList);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exception occured while getting already existing images from folder "+e.getMessage());
        }
        return hashMapImages;
    }


    public static Fragment newInstance(int position) {
        UploadImageFragment fragment = new UploadImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }


    private int getMaxPosition(String selectedForm)
    {
        int max = 1;
        if(hashMapFilenames.size()>0)
        {

            for(Map.Entry<String, ArrayList<String>> listEntry :hashMapFilenames.entrySet())
            {
               if(listEntry.getKey().equals(selectedForm))
               {
                   ArrayList<String> value = listEntry.getValue();
                   for(String val : value)
                   {
                       int position = Integer.parseInt(val.split("_")[2]);
                       if(position>max)
                       max = position;
                   }
               }

            }
        }
        return max;
    }

    private void onCloseUploadDialogButtonClick()
    {
        closeUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        } else {
            Toast.makeText(getActivity(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(Tag, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(Tag, "Connection failed. Error: " + connectionResult.getErrorCode());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100)
                .setFastestInterval(100);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
