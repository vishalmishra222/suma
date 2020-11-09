package com.app.dusmile.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dusmile.R;
import com.app.dusmile.interfaces.BtnClickListener;
import com.app.dusmile.interfaces.ImageOperationListener;
import com.app.dusmile.model.ImagesModel;
import com.app.dusmile.view.DatabaseUI;
import com.app.dusmile.view.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * Created by suma on 21/03/17.
 */

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.Holder>{

    private static final String TAG = "UploadImageAdapter" ;
    private Context mContext;
    private List<String> imageList;
    private Activity activity;
    Bitmap bitmap = null;
    private BtnClickListener btnClickListener;
    private ImageOperationListener imageOperationListener;
    HashMap<String, ArrayList<String>> hashMapFilenames;
    HashMap<String, ArrayList<Bitmap>> hashMapImages;
    public UploadImageAdapter(Context mContext,List<String> imageList, Activity activity,BtnClickListener btnClickListener, ImageOperationListener imageOperationListener,HashMap<String, ArrayList<Bitmap>> hashMapImages,HashMap<String, ArrayList<String>> hashMapFilenames) {
        this.mContext=mContext;
        this.imageList = imageList;
        this.activity = activity;
        this.btnClickListener = btnClickListener;
        this.imageOperationListener = imageOperationListener;
        this.hashMapImages = hashMapImages;
        this.hashMapFilenames = hashMapFilenames;
    }




    @Override
    public UploadImageAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_upload_image_items, null);
        UploadImageAdapter.Holder holder = new UploadImageAdapter.Holder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final UploadImageAdapter.Holder holder, final int position) {


        int mod = position%2;

        Log.e(TAG, String.valueOf(mod));
        holder.formNameTextView.setText(imageList.get(position));
        holder.linearLayout.removeAllViews();
        holder.linearLayout.invalidate();
        for(final Map.Entry<String, ArrayList<Bitmap>> imagesEntry: hashMapImages.entrySet())
        {
            if(imageList.get(position).equalsIgnoreCase(imagesEntry.getKey()))
            {
                List<String> filenameList = new ArrayList<>();
                filenameList = hashMapFilenames.get(imagesEntry.getKey());

                for(int i=0;i<imagesEntry.getValue().size();i++) {
                    final LinearLayout linearLayoutInner = new LinearLayout(mContext);
                    LinearLayout.LayoutParams relparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearLayoutInner.setLayoutParams(relparams);
                    linearLayoutInner.setOrientation(LinearLayout.VERTICAL);
                    linearLayoutInner.setTag("linearLayout_" + imageList.get(position) + "_" + position + "_" + i);
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
                    params.setMargins(5, 5, 5, 5);
                    imageView.setLayoutParams(params);
                    imageView.setTag("imageView_" + filenameList.get(i));
                    imageView.setImageBitmap(imagesEntry.getValue().get(i));
                    /*bitmap = imagesEntry.getValue().get(i);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(bitmap!=null) {
                                DatabaseUI.openFullImagePopup(mContext, bitmap);
                            }
                        }
                    });*/
                    TextView btnDelete = new TextView(new ContextThemeWrapper(mContext,R.style.ThemButton));
                    LinearLayout.LayoutParams paramsClose = new LinearLayout.LayoutParams(150, 50);
                    paramsClose.setMargins(7, 2, 7, 10);
                    btnDelete.setLayoutParams(paramsClose);
                    btnDelete.setTag("delete_" + filenameList.get(i));
                    btnDelete.setText("Delete");
                   // btnDelete.setPadding(5, 5, 5, 5);
                    btnDelete.setTextColor(mContext.getResources().getColor(R.color.white));
                    btnDelete.setBackgroundColor(mContext.getResources().getColor(R.color.clrLoginButton));
                    btnDelete.setGravity(Gravity.CENTER);
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] tag_arr = v.getTag().toString().split("_");
                            //LinearLayout linearLayout = (LinearLayout)llGalleryView.findViewWithTag(tag_arr[0]+"_"+tag_arr[1]);
                            holder.linearLayout.removeView(linearLayoutInner);
                            holder.linearLayout.invalidate();
                            imageOperationListener.imageDelete(tag_arr[1], Integer.parseInt(tag_arr[2]), Integer.parseInt(tag_arr[3]));

                        }
                    });
                    linearLayoutInner.addView(imageView);
                    linearLayoutInner.addView(btnDelete);
                    holder.uploadMoreImageView.setVisibility(View.VISIBLE);
                    holder.linearLayout.addView(linearLayoutInner);
                }
            }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          btnClickListener.buttonListener(position);

            }
        });
        holder.uploadMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickListener.buttonListener(position);
            }
        });

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
       return imageList.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        ImageView imageViewUploadImage,uploadMoreImageView;
        TextView formNameTextView;
        CardView cardView;
        public View itemView;
        LinearLayout linearLayout;

        public Holder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            cardView = (CardView) itemView.findViewById(R.id.uploadImageCardView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.imageLin);
            formNameTextView = (TextView) itemView.findViewById(R.id.formNameTextView);
            imageViewUploadImage = (ImageView) itemView.findViewById(R.id.uploadImage);
            uploadMoreImageView = (ImageView) itemView.findViewById(R.id.uploadMore);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void refresh(List<String> imageList,HashMap<String, ArrayList<Bitmap>> hashMapImages,HashMap<String, ArrayList<String>> hashMapFilenames)
    {
        this.imageList = imageList;
        this.hashMapFilenames = hashMapFilenames;
        this.hashMapImages = hashMapImages;
        notifyDataSetChanged();
    }

    public int size()
    {
        return imageList.size();
    }





}
