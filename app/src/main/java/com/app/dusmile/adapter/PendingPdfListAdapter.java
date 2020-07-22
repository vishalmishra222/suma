package com.app.dusmile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.dusmile.R;
import com.app.dusmile.interfaces.BtnClickListener;
import com.app.dusmile.interfaces.PDFUploadListener;
import com.app.dusmile.view.UI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suma on 03/03/17.
 */

public class PendingPdfListAdapter extends  RecyclerView.Adapter<PendingPdfListAdapter.Holder>{

    private static final String TAG = "PendingPdfListAdapter" ;
    private Context mContext;
    private HashMap<File,List<String>> listHashMap;
    private final ArrayList mData;
    private PDFUploadListener pdfUploadListener;
    public PendingPdfListAdapter(Context mContext,HashMap<File,List<String>> listHashMap,PDFUploadListener pdfUploadListener) {
        this.mContext=mContext;
        this.listHashMap = listHashMap;
        mData = new ArrayList();
        mData.addAll(listHashMap.entrySet());
        this.pdfUploadListener = pdfUploadListener;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_pending_jobs, null);
        Holder holder = new Holder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(PendingPdfListAdapter.Holder holder, final int position) {


        int mod = position%2;
        holder.setIsRecyclable(false);
        Log.e(TAG, String.valueOf(mod));
        final Map.Entry<File, List<String>> item = getItem(position);
        holder.textViewPendingPdf.setText(item.getKey().getName());
        holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfUploadListener.uploadPDF(item);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listHashMap.size();
    }


    class Holder extends RecyclerView.ViewHolder {


        CardView cardView;
        public View itemView;
        TextView textViewPendingPdf;
        Button uploadBtn;
        public Holder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            cardView = (CardView) itemView.findViewById(R.id.pendingPDFCardView);
            textViewPendingPdf = (TextView) itemView.findViewById(R.id.textViewpendingPdf);
            uploadBtn = (Button) itemView.findViewById(R.id.uploadBtn);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void refresh(HashMap<File,List<String>> listHashMap)
    {
        //notifyDataSetChanged();
        try {
            this.listHashMap = listHashMap;
            notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int size()
    {
        return listHashMap.size();
    }

    public Map.Entry<File, List<String>> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }
}
