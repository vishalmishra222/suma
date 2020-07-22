package com.app.dusmile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dusmile.R;
import com.app.dusmile.view.DatabaseUI;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;

/**
 * Created by suma on 03/02/17.
 */

public class ReportDetailsAdapter extends RecyclerView.Adapter<ReportDetailsAdapter.Holder> {

    private static final String TAG = "ReportDetails";
    private Context mContext;
    private JSONArray reportHeadersArray;
    private JSONArray reportHeadersUIArray;
    private JSONArray reportDataArray;
    public JSONArray cardHeadersKeyArray;
    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;

    public ReportDetailsAdapter(Context mContext, JSONArray reportHeadersArray, JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray) {
        this.mContext = mContext;
        this.reportDataArray = reportDataArray;
        this.reportHeadersArray = reportHeadersArray;
        this.reportHeadersUIArray = reportHeadersUIArray;
        this.cardHeadersKeyArray = cardHeadersKeyArray;
    }


    @Override
    public ReportDetailsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.expandable_without_header_btn, null);
        ReportDetailsAdapter.Holder holder = new ReportDetailsAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ReportDetailsAdapter.Holder holder, final int position) {
        try {

            int mod = position % 2;

            Log.e(TAG, String.valueOf(mod));
            holder.expandableLin.removeAllViews();
            holder.expandableLin.invalidate();
            holder.mainHeaderLin.setSelected(false);
            holder.expandableLayout.collapse(false);
            if (mod == 0) {
                holder.expandableLayout.setBackgroundColor(mContext.getResources().getColor(R.color.expandable_color));
            } else {
                holder.expandableLayout.setBackgroundColor(mContext.getResources().getColor(R.color.orange_500));

            }
          //  DatabaseUI.createReportAdapterUI(mContext, holder.expandableLin, holder.mainHeaderLin, position, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
            DatabaseUI.createGenericAdapterUI(mContext, holder.expandableLin, holder.cardVerticalLin, position, reportHeadersArray, reportDataArray, reportHeadersUIArray,cardHeadersKeyArray, null);

            holder.cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder != null) {
                        holder.mainHeaderLin.setSelected(false);
                        holder.expandableLayout.collapse();
                        holder.cardView2.setVisibility(View.GONE);
                    }

                    if (position == selectedItem) {
                        selectedItem = UNSELECTED;
                    } else {
                        holder.mainHeaderLin.setSelected(true);
                        holder.expandableLayout.expand();
                        holder.cardView2.setVisibility(View.VISIBLE);
                        selectedItem = position;
                    }
                }
            });
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

        CardView cardView1;
        CardView cardView2;
        private int position;
        LinearLayout mainHeaderLin;
        ExpandableLayout expandableLayout;
        TextView jobIdTextView;
        TextView nbfcNameTextView;
        TextView pinCodeTextView;
        TextView timeLeftTextView;
        LinearLayout cardVerticalLin;
        Button viewJob;
        LinearLayout expandableLin;

        // CardView cardView;
        public Holder(View itemView) {
            super(itemView);
            cardView1 = (CardView) itemView.findViewById(R.id.cv);
            cardView2 = (CardView) itemView.findViewById(R.id.cv2);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            mainHeaderLin = (LinearLayout) itemView.findViewById(R.id.itemLin);
            /*jobIdTextView = (TextView) itemView.findViewById(R.id.jobIdTextView);
            nbfcNameTextView = (TextView) itemView.findViewById(R.id.clientNameTextView);
            pinCodeTextView = (TextView) itemView.findViewById(R.id.pinCodeTextView);
            timeLeftTextView = (TextView) itemView.findViewById(R.id.timeLeftTextView);*/
            expandableLin = (LinearLayout) itemView.findViewById(R.id.expandableLin);
            cardVerticalLin = (LinearLayout) itemView.findViewById(R.id.verticalLin);
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
