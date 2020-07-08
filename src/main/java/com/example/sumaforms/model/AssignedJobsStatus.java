package com.example.sumaforms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 17/03/17.
 */

public class AssignedJobsStatus implements Parcelable{
    String ID;
    String job_id;
    //String json_template_id;
    String form_data_update_time;


    public AssignedJobsStatus() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

   /* public String getJson_template_id() {
        return json_template_id;
    }

    public void setJson_template_id(String json_template_id) {
        this.json_template_id = json_template_id;
    }
*/
    public String getForm_data_update_time() {
        return form_data_update_time;
    }

    public void setForm_data_update_time(String form_data_update_time) {
        this.form_data_update_time = form_data_update_time;
    }

    public static Creator<AssignedJobsStatus> getCREATOR() {
        return CREATOR;
    }

    protected AssignedJobsStatus(Parcel in) {
        ID = in.readString();
        job_id = in.readString();
       // json_template_id = in.readString();
        form_data_update_time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(job_id);
       // dest.writeString(json_template_id);
        dest.writeString(form_data_update_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssignedJobsStatus> CREATOR = new Creator<AssignedJobsStatus>() {
        @Override
        public AssignedJobsStatus createFromParcel(Parcel in) {
            return new AssignedJobsStatus(in);
        }

        @Override
        public AssignedJobsStatus[] newArray(int size) {
            return new AssignedJobsStatus[size];
        }
    };
}
