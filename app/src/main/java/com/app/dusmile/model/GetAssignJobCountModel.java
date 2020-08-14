package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAssignJobCountModel {

    @SerializedName("assignedJobsCount")
    @Expose
    private Integer assignedJobsCount;

    public Integer getAssignedJobsCount() {
        return assignedJobsCount;
    }

    public void setAssignedJobsCount(Integer assignedJobsCount) {
        this.assignedJobsCount = assignedJobsCount;
    }

}