package com.example.sumaforms.recording.utils;

/*
 * Created by Ray Li
 * © Copyright 2017 Ray LI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sumaforms.R;

import java.util.ArrayList;


public class PermissionsDialogue extends DialogFragment {

    public static Integer NOTREQUIRED = 0;
    public static Integer REQUIRED = 1;
    public static Integer OPTIONAL = 2;
    public static String REQUIRE_PHONE = "REQUIRE_PHONE";
    public static String REQUIRE_SMS = "REQUIRE_SMS";
    public static String REQUIRE_CONTACTS = "REQUIRE_CONTACTS";
    public static String REQUIRE_CALENDAR = "REQUIRE_CALENDAR";
    public static String REQUIRE_STORAGE = "REQUIRE_STORAGE";
    public static String REQUIRE_CAMERA = "REQUIRE_CAMERA";
    public static String REQUIRE_AUDIO = "REQUIRE_AUDIO";
    public static String REQUIRE_LOCATION = "REQUIRE_LOCATION";
    private static final int REQUEST_PERMISSIONS = 1;
    private static final int REQUEST_PERMISSION = 2;

    private Button mButton;
    private CustomPermissionsAdapter requiredAdapter;
    private CustomPermissionsAdapter optionalAdapter;
    private ArrayList<String> requiredPermissions;
    private ArrayList<String> optionalPermissions;

    private Context mContext;
    private Builder builder;
    private Integer gravity = Gravity.CENTER;
    private static PermissionsDialogue instance = new PermissionsDialogue();
    public static final String TAG = PermissionsDialogue.class.getSimpleName();

    public static PermissionsDialogue getInstance() {
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.PermissionsDialogue);
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (builder != null)
            outState.putParcelable(Builder.class.getSimpleName(), builder);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //Configure floating window
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT; //Floating window WRAPS_CONTENT by default. Force fullscreen
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.windowAnimations = R.style.CustomDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        if (builder != null) {
            if (!builder.getCancelable()) {
                setCancelable(false);
            } else {
                setCancelable(true);
            }
        }

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_permissions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton = (Button) getView().findViewById(R.id.permissions_btn);
        mContext = getContext();

        initPermissionsView(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("Request Code", String.valueOf(requestCode));
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                Log.d("Permissons", "Request Permissions");
                refreshRequiredPermissions();
                boolean permissionsGranted = true;
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            boolean showRationale = shouldShowRequestPermissionRationale(permissions[i]);
                            if (!showRationale) {
                                permissionsGranted = false;
                            }
                        }
                    }
                }

                if (permissionsGranted) {
                    refreshPermissionsButton(false);
                    Log.d("Permissions", "Granted");
                } else {
                    refreshPermissionsButton(true);
                    Log.d("Permissions", "Denied");
                }
                return;
            }

            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    refreshOptionalPermissions();
                } else {

                }
                return;
            }
        }
    }

    private void initPermissionsView(View view)
    {
        if (builder != null) {

            Log.d(TAG, "Builder Not Null");

            if (builder.getRequiredPermissions().size() != 0)
            {
                initPermissionsRecyclerView(view);
                initPermissionsButton(view);
            }
            else
            {
                LinearLayout permissionsLayout = (LinearLayout) view.findViewById(R.id.permissions_required);
                permissionsLayout.setVisibility(View.GONE);
            }

            if (builder.getOptionalPermissions().size() != 0)
            {
                initOptionalPermissionsRecyclerView(view);
            }
            else
            {
                LinearLayout permissionsLayout = (LinearLayout) view.findViewById(R.id.permissions_optional);
                permissionsLayout.setVisibility(View.GONE);
            }
        }
        else
        {
            Log.d(TAG, "Builder Null");
        }
    }

    private void initPermissionsRecyclerView(View view) {
        if (builder.getTitle() != null)
        {
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(builder.getTitle());
        }
        if (builder.getMessage() != null)
        {
            TextView message = (TextView) view.findViewById(R.id.message);
            message.setText(builder.getMessage());
        }
        else
        {
            TextView message = (TextView) view.findViewById(R.id.message);
            message.setVisibility(View.GONE);
        }
        if (builder.getIcon() == false)
        {
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setVisibility(View.GONE);
        }

        requiredPermissions = new ArrayList<>();
        requiredPermissions = builder.getRequiredPermissions();
        int spanSize = requiredPermissions.size();
        RecyclerView permissionsRecyclerView = (RecyclerView) view.findViewById(R.id.permissions_list);
        requiredAdapter = new CustomPermissionsAdapter(mContext, requiredPermissions, false);
        permissionsRecyclerView.setAdapter(requiredAdapter);
        GridLayoutManager layoutManager= new GridLayoutManager(mContext, spanSize, LinearLayoutManager.VERTICAL, false);
        permissionsRecyclerView.setLayoutManager(layoutManager);
    }

    private void initOptionalPermissionsRecyclerView(View view) {
        optionalPermissions = new ArrayList<>();
        optionalPermissions = builder.getOptionalPermissions();
        int spanSize = optionalPermissions.size();
        if (spanSize > 2)
        {
            spanSize = 2;
        }
        RecyclerView permissionsRecyclerView = (RecyclerView) view.findViewById(R.id.permissions_list_optional);
        optionalAdapter = new CustomPermissionsAdapter(mContext, optionalPermissions, true);
        permissionsRecyclerView.setAdapter(optionalAdapter);
        GridLayoutManager layoutManager= new GridLayoutManager(mContext, spanSize, LinearLayoutManager.VERTICAL, false);
        permissionsRecyclerView.setLayoutManager(layoutManager);
        int spacing = Units.dpToPx(mContext, 40);
        boolean includeEdge = true;
        permissionsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanSize, spacing, includeEdge));
    }

    private void initPermissionsButton(View view) {
        mButton = (Button) view.findViewById(R.id.permissions_btn);
        if (builder.getRequiredRequestPermissions().size() == 0)
        {
            mButton.setText("Continue");
            mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.icon_add_activated));
            if (builder.getOnContinueClicked() != null)
            {
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.getOnContinueClicked().OnClick(getView(), getDialog());
                    }
                });
            }
            else
            {
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mButton.startAnimation(AnimateButton());
                        Handler handler = new Handler();
                        Runnable r = new Runnable() {
                            public void run() {
                                dismiss();
                            }
                        };
                        handler.postDelayed(r, 250);
                    }
                });
            }
        }
        else
        {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mButton.startAnimation(AnimateButton());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ArrayList<String> requestPermissions = builder.getRequiredRequestPermissions();
                        if (!requestPermissions.isEmpty()) {
                            requestPermissions(requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSIONS);
                        }
                        else
                        {

                        }
                    } else {

                    }
                }
            });
        }
    }

    private void refreshRequiredPermissions()
    {
        requiredPermissions = builder.getRequiredPermissions();
        requiredAdapter.permissionsList = requiredPermissions;
        requiredAdapter.notifyDataSetChanged();
    }

    private void refreshOptionalPermissions()
    {
        optionalPermissions = builder.getOptionalPermissions();
        optionalAdapter.permissionsList = optionalPermissions;
        optionalAdapter.notifyDataSetChanged();
    }

    private void refreshPermissionsButton(boolean denied)
    {
        if (denied)
        {
            mButton.setText("DENIED - Open Settings");
            mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.icon_add_error));
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    Toast.makeText(mContext, "Click Permissions to enable permissions", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getActivity().getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
        else if (builder.getRequiredRequestPermissions().size() == 0)
        {
            mButton.setText("Success!");
            mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.icon_add_activated));
            if (builder.getOnContinueClicked() != null)
            {
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.getOnContinueClicked().OnClick(getView(), getDialog());
                    }
                });
            }
            else
            {
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mButton.startAnimation(AnimateButton());
                        Handler handler = new Handler();
                        Runnable r = new Runnable() {
                            public void run() {
                                dismiss();
                            }
                        };
                        handler.postDelayed(r, 250);
                    }
                });
            }

            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    mButton.setText("Continue");
                }
            };
            handler.postDelayed(r, 1500);
        }
        else {
            mButton.setText("Permission Denied");
            mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.icon_add_error));
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mButton.startAnimation(AnimateButton());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ArrayList<String> requestPermissions = builder.getRequiredRequestPermissions();
                        if (!requestPermissions.isEmpty()) {
                            requestPermissions(requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSIONS);
                        } else {

                        }
                    } else {

                    }
                }
            });

            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    mButton.setText("Grant Permissions");
                    mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.icon_add));
                }
            };
            handler.postDelayed(r, 1500);
        }
        mButton.startAnimation(AnimateButton());
    }

    private Dialog show(Activity activity, Builder builder) {
        this.builder = builder;
        if (!isAdded())
            show(((AppCompatActivity) activity).getSupportFragmentManager(), TAG);
        return getDialog();
    }

    public static class Builder implements Parcelable {

        private String title;
        private String message;

        private OnContinueClicked onContinueClicked;

        private boolean autoHide;
        private boolean cancelable = true;
        private boolean showIcon = true;
        private Integer phone = 0;
        private Integer sms = 0;
        private Integer contacts = 0;
        private Integer calendar = 0;
        private Integer storage = 0;
        private Integer camera = 0;
        private Integer audio = 0;
        private Integer location = 0;
        private String phonedescription;
        private String smsdescription;
        private String contactsdescription;
        private String calendardescription;
        private String storagedescription;
        private String cameradescription;
        private String audiodescription;
        private String locationdescription;

        private Context context;

        protected Builder(Parcel in) {
            title = in.readString();
            message = in.readString();
            autoHide = in.readByte() != 0;
            cancelable = in.readByte() != 0;
            phone = in.readInt();
            sms = in.readInt();
            contacts = in.readInt();
            calendar = in.readInt();
            storage = in.readInt();
            camera = in.readInt();
            audio = in.readInt();
            location = in.readInt();
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel in) {
                return new Builder(in);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }
        public Context getContext() {
            return context;
        }

        public Builder getBuilder() { return this; }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public String getTitle() { return title; }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        public String getMessage() {
            return message;
        }

        public Builder setOnContinueClicked(OnContinueClicked onContinueClicked) {
            this.onContinueClicked = onContinueClicked;
            return this;
        }
        public OnContinueClicked getOnContinueClicked() {
            return onContinueClicked;
        }

        public Builder setAutoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }
        public boolean isAutoHide() {
            return autoHide;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }
        public boolean getCancelable() { return cancelable; }

        public Builder setIcon(boolean showicon) {
            this.showIcon = showicon;
            return this;
        }
        public boolean getIcon() { return showIcon; }

        public Builder setActivity(Context context) {
            this.context = context;
            return this;
        }

        public Builder setRequirePhone(Integer phone) {
            this.phone = phone;
            return this;
        }
        public Integer requirePhone() {
            return phone;
        }

        public Builder setRequireSMS(Integer sms) {
            this.sms = sms;
            return this;
        }
        public Integer requireSMS() {
            return sms;
        }

        public Builder setRequireContacts(Integer contacts) {
            this.contacts = contacts;
            return this;
        }
        public Integer requireContacts() {
            return contacts;
        }

        public Builder setRequireCalendar(Integer calendar) {
            this.calendar = calendar;
            return this;
        }
        public Integer requireCalendar() {
            return calendar;
        }

        public Builder setRequireStorage(Integer storage) {
            this.storage = storage;
            return this;
        }
        public Integer requireStorage() {
            return storage;
        }

        public Builder setRequireCamera(Integer camera) {
            this.camera = camera;
            return this;
        }
        public Integer requireCamera() {
            return camera;
        }

        public Builder setRequireAudio(Integer audio) {
            this.audio = audio;
            return this;
        }
        public Integer requireAudio() {
            return audio;
        }

        public Builder setRequireLocation(Integer location) {
            this.location = location;
            return this;
        }
        public Integer requireLocation() {
            return location;
        }

        public Builder setPhoneDescription(String phonedescription) {
            this.phonedescription = phonedescription;
            return this;
        }
        public String getPhoneDescription() {
            return phonedescription;
        }

        public Builder setSMSDescription(String smsdescription) {
            this.smsdescription = smsdescription;
            return this;
        }
        public String getSMSDescription() {
            return smsdescription;
        }

        public Builder setContactDescription(String contactsdescription) {
            this.contactsdescription = contactsdescription;
            return this;
        }
        public String getContactDescription() {
            return contactsdescription;
        }

        public Builder setCalendarDescription(String calendardescription) {
            this.calendardescription = calendardescription;
            return this;
        }
        public String getCalendarDescription() {
            return calendardescription;
        }

        public Builder setStorageDescription(String storagedescription) {
            this.storagedescription = storagedescription;
            return this;
        }
        public String getStorageDescription() {
            return storagedescription;
        }

        public Builder setCameraDescription(String cameradescription) {
            this.cameradescription = cameradescription;
            return this;
        }
        public String getCameraDescription() {
            return cameradescription;
        }

        public Builder setAudioDescription(String audiodescription) {
            this.audiodescription = audiodescription;
            return this;
        }
        public String getAudioDescription() {
            return audiodescription;
        }

        public Builder setLocationDescription(String locationdescription) {
            this.locationdescription = locationdescription;
            return this;
        }
        public String getLocationDescription() {
            return locationdescription;
        }

        public ArrayList<String> getRequiredPermissions()
        {
            ArrayList<String> requiredPermissions = new ArrayList<>();
            if (requirePhone() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_PHONE);
            }
            if (requireSMS() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_SMS);
            }
            if (requireContacts() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_CONTACTS);
            }
            if (requireCalendar() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_CALENDAR);
            }
            if (requireStorage() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_STORAGE);
            }
            if (requireCamera() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_CAMERA);
            }
            if (requireAudio() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_AUDIO);
            }
            if (requireLocation() == REQUIRED)
            {
                requiredPermissions.add(REQUIRE_LOCATION);
            }
            return requiredPermissions;
        }

        public ArrayList<String> getOptionalPermissions()
        {
            ArrayList<String> requiredPermissions = new ArrayList<>();
            if (requirePhone() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_PHONE);
            }
            if (requireSMS() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_SMS);
            }
            if (requireContacts() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_CONTACTS);
            }
            if (requireCalendar() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_CALENDAR);
            }
            if (requireStorage() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_STORAGE);
            }
            if (requireCamera() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_CAMERA);
            }
            if (requireAudio() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_AUDIO);
            }
            if (requireLocation() == OPTIONAL)
            {
                requiredPermissions.add(REQUIRE_LOCATION);
            }
            return requiredPermissions;
        }

        public ArrayList<String> getRequiredRequestPermissions()
        {
            ArrayList<String> requestPermissions = new ArrayList<>();
            if (requirePhone() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.CALL_PHONE)) {
                    requestPermissions.add(Manifest.permission.CALL_PHONE);
                }
            }
            if (requireSMS() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.SEND_SMS)) {
                    requestPermissions.add(Manifest.permission.SEND_SMS);
                }
            }
            if (requireContacts() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.WRITE_CONTACTS)) {
                    requestPermissions.add(Manifest.permission.WRITE_CONTACTS);
                }
            }
            if (requireCalendar() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.WRITE_CALENDAR)) {
                    requestPermissions.add(Manifest.permission.WRITE_CALENDAR);
                }
            }
            if (requireStorage() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
            if (requireCamera() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.CAMERA)) {
                    requestPermissions.add(Manifest.permission.CAMERA);
                }
            }
            if (requireAudio() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.RECORD_AUDIO)) {
                    requestPermissions.add(Manifest.permission.RECORD_AUDIO);
                }
            }
            if (requireLocation() == REQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
            return requestPermissions;
        }

        public ArrayList<String> getAllRequestPermissions()
        {
            ArrayList<String> requestPermissions = new ArrayList<>();
            if (requirePhone() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.CALL_PHONE)) {
                    requestPermissions.add(Manifest.permission.CALL_PHONE);
                }
            }
            if (requireSMS() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.SEND_SMS)) {
                    requestPermissions.add(Manifest.permission.SEND_SMS);
                }
            }
            if (requireContacts() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.WRITE_CONTACTS)) {
                    requestPermissions.add(Manifest.permission.WRITE_CONTACTS);
                }
            }
            if (requireCalendar() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.WRITE_CALENDAR)) {
                    requestPermissions.add(Manifest.permission.WRITE_CALENDAR);
                }
            }
            if (requireStorage() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
            if (requireCamera() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.CAMERA)) {
                    requestPermissions.add(Manifest.permission.CAMERA);
                }
            }
            if (requireAudio() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.RECORD_AUDIO)) {
                    requestPermissions.add(Manifest.permission.RECORD_AUDIO);
                }
            }
            if (requireLocation() > NOTREQUIRED) {
                if (!PermissionUtils.IsPermissionEnabled(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
            return requestPermissions;
        }

        public Builder(Context context) { this.context = context; }

        public Builder build() {
            return this;
        }

        public Dialog show() {
            return PermissionsDialogue.getInstance().show(((Activity) context), this);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnContinueClicked {
        void OnClick(View view, Dialog dialog);
    }

    public interface OnNegativeClicked {
        void OnClick(View view, Dialog dialog);
    }

    public interface OnCancelClicked {
        void OnClick(View view, Dialog dialog);
    }

    public Animation AnimateButton() {
        // Load the animation
        final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        double animationDuration = 1250;
        animation.setDuration((long) animationDuration);

        // Use custom animation interpolator to achieve the bounce effect
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);

        return animation;
    }

    public class CustomPermissionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public Context mContext;
        public ArrayList<String> permissionsList;
        public boolean optional;

        public CustomPermissionsAdapter(Context context, ArrayList<String> permissionsList, boolean optional) {
            this.mContext = context;
            this.permissionsList = permissionsList;
            this.optional = optional;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permission, parent, false);
            return new PermissionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder genericHolder, final int position) {
            String permission = permissionsList.get(position);
            PermissionViewHolder holder = (PermissionViewHolder) genericHolder;
            holder.setPermission(permission, optional);
        }

        @Override
        public int getItemCount() { return permissionsList.size(); }

        public String getPermission(int position) { return permissionsList.get(position); }
    }

    public class PermissionViewHolder extends RecyclerView.ViewHolder {

        public String permission;
        public TextView mName;
        public TextView mMessage;
        public ImageView mImage;
        public CustomButton mButton;
        public boolean optional;
        public Context mContext;

        public PermissionViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.permission_name);
            mMessage = (TextView) itemView.findViewById(R.id.permission_detail);
            mImage = (ImageView) itemView.findViewById(R.id.permission_icon);
            mButton = (CustomButton) itemView.findViewById(R.id.permission_btn);
            mContext = itemView.getContext();
        }

        public void setPermission(String permission, boolean optional) {
            this.permission = permission;
            this.optional = optional;

            if (!optional)
                mButton.setVisibility(View.GONE);

            if (REQUIRE_PHONE.equals(permission))
            {
                mName.setText("Phone");
                mImage.setImageResource(R.drawable.ic_phone);
                setRequestPermissions(Manifest.permission.CALL_PHONE);

                if (builder.getPhoneDescription() != null)
                {
                    mMessage.setText(builder.getPhoneDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else if (REQUIRE_SMS.equals(permission))
            {
                mName.setText("SMS");
                mImage.setImageResource(R.drawable.ic_text);

                setRequestPermissions(Manifest.permission.SEND_SMS);

                if (builder.getSMSDescription() != null)
                {
                    mMessage.setText(builder.getSMSDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else if (REQUIRE_CONTACTS.equals(permission))
            {
                mName.setText("Contacts");

                mImage.setImageResource(R.drawable.ic_contacts);

                setRequestPermissions(Manifest.permission.WRITE_CONTACTS);

                if (builder.getContactDescription() != null)
                {
                    mMessage.setText(builder.getContactDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else if (REQUIRE_CALENDAR.equals(permission))
            {
                mName.setText("Calendar");
                mImage.setImageResource(R.drawable.ic_calendar);

                setRequestPermissions(Manifest.permission.WRITE_CALENDAR);

                if (builder.getCalendarDescription() != null)
                {
                    mMessage.setText(builder.getCalendarDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else if (REQUIRE_STORAGE.equals(permission))
            {
                mName.setText("Storage");
                mImage.setImageResource(R.drawable.ic_folder);

                setRequestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (builder.getStorageDescription() != null)
                {
                    mMessage.setText(builder.getStorageDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else if (REQUIRE_CAMERA.equals(permission))
            {
                mName.setText("Camera");
                mImage.setImageResource(R.drawable.ic_camera);

                setRequestPermissions(Manifest.permission.CAMERA);

                if (builder.getCameraDescription() != null)
                {
                    mMessage.setText(builder.getCameraDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else if (REQUIRE_AUDIO.equals(permission))
            {
                mName.setText("Audio");
                mImage.setImageResource(R.drawable.ic_mic);

                setRequestPermissions(Manifest.permission.RECORD_AUDIO);

                if (builder.getAudioDescription() != null)
                {
                    mMessage.setText(builder.getAudioDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else if (REQUIRE_LOCATION.equals(permission))
            {
                mName.setText("Location");
                mImage.setImageResource(R.drawable.ic_location);

                setRequestPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

                if (builder.getLocationDescription() != null)
                {
                    mMessage.setText(builder.getLocationDescription());
                    mMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMessage.setVisibility(View.GONE);
                }
            }
            else
            {

            }
        }

        public void setRequestPermissions(final String requestPermission)
        {
            if (PermissionUtils.IsPermissionEnabled(mContext, requestPermission))
            {
                int color = ContextCompat.getColor(mContext, R.color.button_pressed);
                mImage.setColorFilter(color);
                if (optional) {
                    mButton.setText("Active");
                    mButton.setColor(ContextCompat.getColor(mContext, R.color.white), ContextCompat.getColor(mContext, R.color.white),
                            ContextCompat.getColor(mContext, R.color.green_light), ContextCompat.getColor(mContext, R.color.green), ContextCompat.getColor(mContext, R.color.green));
                    mButton.setButtonStatus(true);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mButton.setButtonStatus(true);
                        }
                    });
                }
            }
            else
            {
                int color = ContextCompat.getColor(mContext, R.color.button_inactive);
                mImage.setColorFilter(color);
                if (optional)
                {
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{requestPermission}, REQUEST_PERMISSION);
                            }
                        }
                    });
                }
            }
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

//                if (position < spanCount) { // top edge
//                    outRect.top = spacing/2;
//                }
                outRect.bottom = spacing/2; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
