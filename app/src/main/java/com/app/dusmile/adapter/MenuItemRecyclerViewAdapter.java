package com.app.dusmile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.app.dusmile.R;
import com.app.dusmile.fragment.MenuItemFragment.OnListFragmentInteractionListener;
import com.app.dusmile.model.MenuItems;
import com.app.dusmile.preferences.UserPreference;

import java.io.File;
import java.util.HashMap;
import java.util.List;


public class MenuItemRecyclerViewAdapter extends RecyclerView.Adapter<MenuItemRecyclerViewAdapter.ViewHolder> {

    private final List<MenuItems> mValues;
    private final OnListFragmentInteractionListener mListener;
    private HashMap<File,List<String>> pendingPdfListMap = new HashMap<>();
    private Context context;
    int data;
    public MenuItemRecyclerViewAdapter(List<MenuItems> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public MenuItemRecyclerViewAdapter(List<MenuItems> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //String selectedSubCategoryName = mValues.get(position).getSubcategory_name();
        String selectedSubCategoryName = mValues.get(position).getMenuName();
        holder.mItem = mValues.get(position);
        holder.menuImage.setImageDrawable(mValues.get(position).menuImage);
        holder.imageName.setText(mValues.get(position).menuName);
        //if(position==0)
        if (selectedSubCategoryName.equalsIgnoreCase("Available Jobs")) {
            if (UserPreference.readInteger(context, UserPreference.AVAILABLE_CNT, 0) > 0) {
                setBadge(holder.menuImage, UserPreference.readInteger(context, UserPreference.AVAILABLE_CNT, 0), position);
            }
        } else //if(position==1)
            if (selectedSubCategoryName.equalsIgnoreCase("Assigned Jobs")) {
                if (UserPreference.readInteger(context, UserPreference.ASSIGNED_CNT, 0) > 0) {
                    setBadge(holder.menuImage, UserPreference.readInteger(context, UserPreference.ASSIGNED_CNT, 0), position);
                }
            }
            else if (selectedSubCategoryName.equalsIgnoreCase("Pending")){
                /*if (PendingCount != 0){
                    setBadge(holder.menuImage,PendingCount, position);
                }*/
                if (UserPreference.readInteger(context, UserPreference.PENDING_CNT, 0) > 0) {
                    setBadge(holder.menuImage, UserPreference.readInteger(context, UserPreference.PENDING_CNT, 0), position);
                }
            }


       /* else if(position==2)
        {
            if(UserPreference.readInteger(context,UserPreference.COMPLETED_CNT,0)>0) {
                setBadge(holder.menuImage, UserPreference.readInteger(context, UserPreference.COMPLETED_CNT, 0));
            }
        }*/
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

  /*  private void getMapOfPendingPdf()
    {
        pendingPdfListMap.clear();
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" get hashmap of pending pdf files list");
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/"+ UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername());
        if(directory.isDirectory()){
            for(File file : directory.listFiles())
            {
                if(file.isDirectory())
                {
                    List<String> imageList = new ArrayList<>();
                    File pdfFile = null;
                    for(File infileFile : file.listFiles())
                    {
                        String fileName = infileFile.getName();
                        if(fileName.toLowerCase().contains(".pdf".toLowerCase()))
                        {
                            pdfFile = infileFile;
                        }
                        else
                        {
                            imageList.add(fileName.split("_")[0]);
                        }

                    }
                    if(pdfFile!=null)
                    {
                        pendingPdfListMap.put(pdfFile,imageList);
                    }
                }

            }
        }
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView menuImage;
        public final TextView imageName;
        public MenuItems mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            menuImage = (ImageView) view.findViewById(R.id.menuImage);
            imageName = (TextView) view.findViewById(R.id.imageName);
        }
    }

    private void setBadge(View view, int count, int position) {
        /* if(position==0) {*/
        BadgeFactory.create(context)
                .setTextColor(Color.BLACK)
                .setWidthAndHeight(27, 27)
                .setBadgeBackground(Color.parseColor("#ffffff"))
                .setTextSize(15)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBadgeCount(count)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .setSpace(0, 0)
                .bind(view);
        /* }*/
        /*if(position==1) {
            BadgeFactory.create(context)
                    .setTextColor(Color.RED)
                    .setWidthAndHeight(27, 27)
                    .setBadgeBackground(Color.parseColor("#ffffff"))
                    .setTextSize(15)
                    .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                    .setBadgeCount("✔")
                    .setSpace(0, 0)
                    .bind(view);
        }*/

    }
}
