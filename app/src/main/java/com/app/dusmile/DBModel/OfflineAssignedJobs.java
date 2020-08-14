package com.app.dusmile.DBModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 05/06/17.
 */

public class OfflineAssignedJobs implements Parcelable {
    String ID;
    String offline_assigned_jobs_json;

    public OfflineAssignedJobs(Parcel in) {
        ID = in.readString();
        offline_assigned_jobs_json = in.readString();
    }

    public OfflineAssignedJobs() {
    }

    public static final Creator<OfflineAssignedJobs> CREATOR = new Creator<OfflineAssignedJobs>() {
        @Override
        public OfflineAssignedJobs createFromParcel(Parcel in) {
            return new OfflineAssignedJobs(in);
        }

        @Override
        public OfflineAssignedJobs[] newArray(int size) {
            return new OfflineAssignedJobs[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(offline_assigned_jobs_json);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOffline_assigned_jobs_json() {
        return offline_assigned_jobs_json;
    }

    public void setOffline_assigned_jobs_json(String offline_assigned_jobs_json) {
        this.offline_assigned_jobs_json = offline_assigned_jobs_json;
    }
}
