package com.app.dusmile.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetJobCountModel {

    @SerializedName("available_jobs")
    @Expose
    private Integer availableJobs;
    @SerializedName("assigned_caseList")
    @Expose
    private List<String> assignedCaseList = null;
    @SerializedName("assigned_jobs")
    @Expose
    private Integer assignedJobs;
    @SerializedName("completed_jobs")
    @Expose
    private Integer completedJobs;

    public Integer getAvailableJobs() {
        return availableJobs;
    }

    public void setAvailableJobs(Integer availableJobs) {
        this.availableJobs = availableJobs;
    }

    public List<String> getAssignedCaseList() {
        return assignedCaseList;
    }

    public void setAssignedCaseList(List<String> assignedCaseList) {
        this.assignedCaseList = assignedCaseList;
    }

    public Integer getAssignedJobs() {
        return assignedJobs;
    }

    public void setAssignedJobs(Integer assignedJobs) {
        this.assignedJobs = assignedJobs;
    }

    public Integer getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(Integer completedJobs) {
        this.completedJobs = completedJobs;
    }

}