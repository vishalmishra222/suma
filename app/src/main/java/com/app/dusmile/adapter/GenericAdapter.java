package com.app.dusmile.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.R;
import com.app.dusmile.activity.LoginActivity;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.interfaces.BtnClickListener;
import com.app.dusmile.view.DatabaseUI;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.Holder> {
    private static final String TAG = "AdapterAuditList";
    private Activity activity;
    private Context mContext;
    private JSONArray reportHeadersArray;
    private JSONArray reportHeadersUIArray;
    private JSONArray reportDataArray;
    private JSONArray cardHeadersKeyArray;
    private BtnClickListener btnClickListener;
    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;

    public GenericAdapter(Context mContext, JSONArray reportHeadersArray, JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray, BtnClickListener btnClickListener) {
        this.mContext = mContext;
        this.reportDataArray = reportDataArray;
        this.reportHeadersArray = reportHeadersArray;
        this.reportHeadersUIArray = reportHeadersUIArray;
        this.cardHeadersKeyArray = cardHeadersKeyArray;
        this.btnClickListener = btnClickListener;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (AppConstant.isCompletedJobs) {
            view = LayoutInflater.from(mContext).inflate(R.layout.expandable_without_header_btn, null);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.exapandable_with_header_btn, null);
        }

        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        try {
            int mod = position % 2;
            Log.e(TAG, String.valueOf(mod));
            holder.setIsRecyclable(false);
            //holder.mainHeaderLin.setSelected(false);
            holder.expandableLayout.collapse(false);
            if (AppConstant.isAssinedJobs && holder != null) {

                holder.viewJob.setVisibility(View.VISIBLE);
                //holder.holdJob.setVisibility(View.VISIBLE);
                //holder.tATimeTextView.setVisibility(View.VISIBLE);
                holder.callButton.setVisibility(View.GONE);
                holder.viewMapButton.setVisibility(View.VISIBLE);

                holder.listImageView.setBackground(mContext.getResources().getDrawable(R.drawable.assigned_jobs));
                holder.viewJob.setText("Perform");

                holder.viewJob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnClickListener.viewListener(position);
                    }
                });

                holder.holdJob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnClickListener.showHoldPopupListener(position);
                    }
                });

                holder.callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            AppConstant.isCallButtonClicked = true;
                            JSONObject jsonObject = reportDataArray.getJSONObject(position);
                            String phnNo = jsonObject.getString("Mobile");
                            if (!TextUtils.isEmpty(phnNo)) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phnNo));
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                mContext.startActivity(callIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.viewMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            AppConstant.isCallButtonClicked = true;
                            JSONObject jsonObject = reportDataArray.getJSONObject(position);
                            String address = jsonObject.getString("Address");
                            if (address == null && address.equals("")) {
                                MyDynamicToast.errorMessage(mContext, "Invalid Address");
                            } else {
                                String map = "http://maps.google.co.in/maps?q=" + address;
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse(map));
                                //Uri.parse("google.navigation:q="+address));//commented by Netra
                                mContext.startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else if (AppConstant.isAvilableJobs && holder != null) {
                holder.viewJob.setVisibility(View.VISIBLE);
                holder.holdJob.setVisibility(View.GONE);
                //holder.tATimeTextView.setVisibility(View.GONE);
                holder.callButton.setVisibility(View.GONE);
                holder.viewMapButton.setVisibility(View.GONE);
                holder.listImageView.setBackground(mContext.getResources().getDrawable(R.drawable.available_jobs));
                holder.viewJob.setText("Accept");
                holder.viewJob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnClickListener.buttonListener(position);
                    }
                });
            } else if (AppConstant.isCompletedJobs) {

                holder.listImageView.setBackground(mContext.getResources().getDrawable(R.drawable.completed_jobs));
            }

            //DatabaseUI.createGenericAdapterUI(mContext, holder.jobIdTextView, holder.nbfcNameTextView, holder.pinCodeTextView, holder.timeLeftTextView, holder.tATimeTextView, holder.expandableLin, position, reportHeadersArray, reportDataArray, reportHeadersUIArray,btnClickListener);//by netra
            String pjobId = null;
            boolean pendingFlag = false;
            DBHelper dbHelper = DBHelper.getInstance(mContext);
            List<AssignedJobs> PendingList = AssignedJobsDB.getAllAssignedSubmittedJobs(dbHelper, "true");
            if (PendingList.size() > 0) {
                for (int i = 0; i < PendingList.size(); i++) {
                    pjobId = PendingList.get(i).getAssigned_jobId();
                    JSONObject jsonObject = reportDataArray.getJSONObject(position);
                    String myId = jsonObject.getString("job_id");
                    if (myId.equalsIgnoreCase(pjobId)) {
                        pendingFlag = true;
                    }
                }
            }

            if (pendingFlag != true) {
                DatabaseUI.createGenericAdapterUI(mContext, holder.expandableLin, holder.cardVerticalLin, position, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
            } else {
                holder.cardView1.setVisibility(View.GONE);
            }


            holder.cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder != null) {
                        //holder.mainHeaderLin.setSelected(false);
                        holder.expandableLayout.collapse();
                        holder.cardView2.setVisibility(View.GONE);
                    }

                    if (position == selectedItem) {
                        selectedItem = UNSELECTED;
                    } else {
                        //holder.mainHeaderLin.setSelected(true);
                        holder.expandableLayout.expand();
                        holder.cardView2.setVisibility(View.VISIBLE);
                        selectedItem = position;
                    }
                }
            });

            if (mod == 0) {
                holder.expandableLayout.setBackgroundColor(mContext.getResources().getColor(R.color.expandable_color));
            } else {
                holder.expandableLayout.setBackgroundColor(mContext.getResources().getColor(R.color.orange_500));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendMessage(String auditID) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return reportDataArray.length();
    }


    class Holder extends RecyclerView.ViewHolder {

        CardView cardView1, cardView2;
        private int position;
        //LinearLayout mainHeaderLin;
        LinearLayout cardVerticalLin;
        ExpandableLayout expandableLayout;
        TextView jobIdTextView;
        TextView nbfcNameTextView;
        TextView pinCodeTextView;
        TextView timeLeftTextView;
        //TextView tATimeTextView;
        Button viewMapButton;
        Button viewJob, holdJob, callButton;
        LinearLayout expandableLin;
        ImageView listImageView;

        public Holder(View itemView) {
            super(itemView);
            cardView1 = (CardView) itemView.findViewById(R.id.cv);
            cardView2 = (CardView) itemView.findViewById(R.id.cv2);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
            cardVerticalLin = (LinearLayout) itemView.findViewById(R.id.verticalLin);
            listImageView = (ImageView) itemView.findViewById(R.id.listImageView);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            //mainHeaderLin = (LinearLayout) itemView.findViewById(R.id.itemLin);
            /*jobIdTextView = (TextView) itemView.findViewById(R.id.jobIdTextView);
            pinCodeTextView = (TextView) itemView.findViewById(R.id.pinCodeTextView);
            timeLeftTextView = (TextView) itemView.findViewById(R.id.timeLeftTextView);
            nbfcNameTextView = (TextView) itemView.findViewById(R.id.clientNameTextView);*/
            if (AppConstant.isAssinedJobs || AppConstant.isAvilableJobs) {
                viewJob = (Button) itemView.findViewById(R.id.viewJobButton);
                holdJob = (Button) itemView.findViewById(R.id.holdButton);
                //tATimeTextView = (TextView) itemView.findViewById(R.id.tATimeTextView);
                callButton = (Button) itemView.findViewById(R.id.callButton);
                viewMapButton = (Button) itemView.findViewById(R.id.mapButton);
            }
            expandableLin = (LinearLayout) itemView.findViewById(R.id.expandableLin);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void refresh(JSONArray reportDataJsonArray) {
        this.reportDataArray = reportDataJsonArray;
        notifyDataSetChanged();

    }

}
