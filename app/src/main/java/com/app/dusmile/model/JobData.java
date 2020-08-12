package com.app.dusmile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobData {

    @SerializedName("reportData")
    @Expose
    private List<ReportDatum> reportData = null;

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
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("job_creation_date_STR")
        @Expose
        private String jobCreationDateSTR;
        @SerializedName("bTATExceeded")
        @Expose
        private Boolean bTATExceeded;
        @SerializedName("Product")
        @Expose
        private String product;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("JobType")
        @Expose
        private String jobType;
        @SerializedName("job_id")
        @Expose
        private String jobId;
        @SerializedName("_id")
        @Expose
        private Id id;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("Pincode")
        @Expose
        private String pincode;
        @SerializedName("status")
        @Expose
        private String status;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getJobCreationDateSTR() {
            return jobCreationDateSTR;
        }

        public void setJobCreationDateSTR(String jobCreationDateSTR) {
            this.jobCreationDateSTR = jobCreationDateSTR;
        }

        public Boolean getBTATExceeded() {
            return bTATExceeded;
        }

        public void setBTATExceeded(Boolean bTATExceeded) {
            this.bTATExceeded = bTATExceeded;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class Id {

        @SerializedName("timestamp")
        @Expose
        private Integer timestamp;
        @SerializedName("counter")
        @Expose
        private Integer counter;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private Integer time;
        @SerializedName("machineIdentifier")
        @Expose
        private Integer machineIdentifier;
        @SerializedName("processIdentifier")
        @Expose
        private Integer processIdentifier;
        @SerializedName("timeSecond")
        @Expose
        private Integer timeSecond;

        public Integer getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Integer timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getCounter() {
            return counter;
        }

        public void setCounter(Integer counter) {
            this.counter = counter;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }

        public Integer getMachineIdentifier() {
            return machineIdentifier;
        }

        public void setMachineIdentifier(Integer machineIdentifier) {
            this.machineIdentifier = machineIdentifier;
        }

        public Integer getProcessIdentifier() {
            return processIdentifier;
        }

        public void setProcessIdentifier(Integer processIdentifier) {
            this.processIdentifier = processIdentifier;
        }

        public Integer getTimeSecond() {
            return timeSecond;
        }

        public void setTimeSecond(Integer timeSecond) {
            this.timeSecond = timeSecond;
        }

    }
}
