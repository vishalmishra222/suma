package com.example.sumaforms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 17/03/17.
 */

public class AssignedJobs implements Parcelable {
    String ID;
    String assigned_jobId;
    String original_applicant_json;
    String applicant_json;
    String job_end_time;
    String client_template_id;
    String IS_IN_PROGRESS;
    String is_submit;
    String latLong;
    String FIlat;
    String FILon;
    String submit_json;

    protected AssignedJobs(Parcel in) {
        ID = in.readString();
        assigned_jobId = in.readString();
        original_applicant_json = in.readString();
        applicant_json = in.readString();
        job_end_time = in.readString();
        client_template_id = in.readString();
        IS_IN_PROGRESS = in.readString();
        is_submit = in.readString();
        latLong = in.readString();
        submit_json = in.readString();
        FIlat = in.readString();
        FILon = in.readString();
        job_type = in.readString();
    }

    public static final Creator<AssignedJobs> CREATOR = new Creator<AssignedJobs>() {
        @Override
        public AssignedJobs createFromParcel(Parcel in) {
            return new AssignedJobs(in);
        }

        @Override
        public AssignedJobs[] newArray(int size) {
            return new AssignedJobs[size];
        }
    };

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    String job_type;

    public String getIS_IN_PROGRESS() {
        return IS_IN_PROGRESS;
    }

    public void setIS_IN_PROGRESS(String IS_IN_PROGRESS) {
        this.IS_IN_PROGRESS = IS_IN_PROGRESS;
    }

    public String getIs_submit() {
        return is_submit;
    }

    public void setIs_submit(String is_submit) {
        this.is_submit = is_submit;
    }

    public AssignedJobs() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAssigned_jobId() {
        return assigned_jobId;
    }

    public void setAssigned_jobId(String assigned_jobId) {
        this.assigned_jobId = assigned_jobId;
    }

    public String getApplicant_json() {
        return applicant_json;
    }

    public String getFIlat() {
        return FIlat;
    }

    public void setFIlat(String FIlat) {
        this.FIlat = FIlat;
    }

    public String getFILon() {
        return FILon;
    }

    public void setFILon(String FILon) {
        this.FILon = FILon;
    }
    public void setApplicant_json(String applicant_json) {
        this.applicant_json = applicant_json;
    }

    public String getJob_end_time() {
        return job_end_time;
    }

    public void setJob_end_time(String job_end_time) {
        this.job_end_time = job_end_time;
    }

    public String getClient_template_id() {
        return client_template_id;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public void setClient_template_id(String client_template_id) {
        this.client_template_id = client_template_id;
    }

    public String getSubmit_json() {
        return submit_json;
    }

    public void setSubmit_json(String submit_json) {
        this.submit_json = submit_json;
    }

    public String getOriginal_applicant_json() {
        return original_applicant_json;
    }

    public void setOriginal_applicant_json(String original_applicant_json) {
        this.original_applicant_json = original_applicant_json;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(assigned_jobId);
        dest.writeString(original_applicant_json);
        dest.writeString(applicant_json);
        dest.writeString(job_end_time);
        dest.writeString(client_template_id);
        dest.writeString(IS_IN_PROGRESS);
        dest.writeString(is_submit);
        dest.writeString(latLong);
        dest.writeString(submit_json);
        dest.writeString(job_type);
        dest.writeString(FIlat);
        dest.writeString(FILon);
    }
}

