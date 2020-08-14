package com.app.dusmile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableJobsDataModel {

    @SerializedName("redirect")
    @Expose
    private Boolean redirect;
    @SerializedName("reportHeadersKeys")
    @Expose
    private List<String> reportHeadersKeys = null;
    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;
    @SerializedName("reportName")
    @Expose
    private String reportName;
    @SerializedName("reportHeaders")
    @Expose
    private List<String> reportHeaders = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("reportData")
    @Expose
    private List<ReportDatum> reportData = null;

    public Boolean getRedirect() {
        return redirect;
    }

    public void setRedirect(Boolean redirect) {
        this.redirect = redirect;
    }

    public List<String> getReportHeadersKeys() {
        return reportHeadersKeys;
    }

    public void setReportHeadersKeys(List<String> reportHeadersKeys) {
        this.reportHeadersKeys = reportHeadersKeys;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<String> getReportHeaders() {
        return reportHeaders;
    }

    public void setReportHeaders(List<String> reportHeaders) {
        this.reportHeaders = reportHeaders;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ReportDatum> getReportData() {
        return reportData;
    }

    public void setReportData(List<ReportDatum> reportData) {
        this.reportData = reportData;
    }

    public class ReportDatum {

        @SerializedName("city_Name")
        @Expose
        private String cityName;
        @SerializedName("templateName")
        @Expose
        private String jobType;
        @SerializedName("googleMapLink")
        @Expose
        private String googleMapLink;
        @SerializedName("NBFCName")
        @Expose
        private String nBFCName;
        @SerializedName("job_id")
        @Expose
        private String jobId;
        @SerializedName("job_creation_date_STR")
        @Expose
        private String jobCreationDateSTR;
        @SerializedName("performJob")
        @Expose
        private String performJob;
        @SerializedName("Pincode")
        @Expose
        private String pinCode;
        @SerializedName("status")
        @Expose
        private String status;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public String getGoogleMapLink() {
            return googleMapLink;
        }

        public void setGoogleMapLink(String googleMapLink) {
            this.googleMapLink = googleMapLink;
        }

        public String getNBFCName() {
            return nBFCName;
        }

        public void setNBFCName(String nBFCName) {
            this.nBFCName = nBFCName;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getJobCreationDateSTR() {
            return jobCreationDateSTR;
        }

        public void setJobCreationDateSTR(String jobCreationDateSTR) {
            this.jobCreationDateSTR = jobCreationDateSTR;
        }

        public String getPerformJob() {
            return performJob;
        }

        public void setPerformJob(String performJob) {
            this.performJob = performJob;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}