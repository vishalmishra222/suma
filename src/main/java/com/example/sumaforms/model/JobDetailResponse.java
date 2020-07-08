package com.example.sumaforms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobDetailResponse {

    @SerializedName("_id")
    @Expose
    private Id id;
    @SerializedName("job_id")
    @Expose
    private String jobId;
    @SerializedName("master_job_id")
    @Expose
    private String masterJobId;
    @SerializedName("prev_process_id")
    @Expose
    private String prevProcessId;
    @SerializedName("process_queue_id")
    @Expose
    private String processQueueId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("queueProcessName")
    @Expose
    private String queueProcessName;
    @SerializedName("currentProcessName")
    @Expose
    private String currentProcessName;
    @SerializedName("hold")
    @Expose
    private Boolean hold;
    @SerializedName("calculateTAT")
    @Expose
    private Boolean calculateTAT;
    @SerializedName("bTATExceeded")
    @Expose
    private Boolean bTATExceeded;
    @SerializedName("job_creation_date")
    @Expose
    private String jobCreationDate;
    @SerializedName("job_start_date")
    @Expose
    private String jobStartDate;
    @SerializedName("job_end_date")
    @Expose
    private Object jobEndDate;
    @SerializedName("job_creation_date_STR")
    @Expose
    private String jobCreationDateSTR;
    @SerializedName("job_start_date_STR")
    @Expose
    private String jobStartDateSTR;
    @SerializedName("job_end_date_STR")
    @Expose
    private Object jobEndDateSTR;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("totalTimeTaken")
    @Expose
    private Object totalTimeTaken;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("uploadedByUserID")
    @Expose
    private Object uploadedByUserID;
    @SerializedName("uploadedBy")
    @Expose
    private String uploadedBy;
    @SerializedName("actionType")
    @Expose
    private String actionType;
    @SerializedName("hasImagesAttached")
    @Expose
    private Boolean hasImagesAttached;
    @SerializedName("hoursToSubtractFromTotalTimeTaken")
    @Expose
    private Integer hoursToSubtractFromTotalTimeTaken;
    @SerializedName("fos_executive")
    @Expose
    private Object fosExecutive;
    @SerializedName("fos_executive_id")
    @Expose
    private Object fosExecutiveId;
    @SerializedName("nbfcname")
    @Expose
    private Object nbfcname;
    @SerializedName("tat")
    @Expose
    private String tat;
    @SerializedName("filocation")
    @Expose
    private Object filocation;

    @SerializedName("latestVersion")
    @Expose
    private String latestVersion;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMasterJobId() {
        return masterJobId;
    }

    public void setMasterJobId(String masterJobId) {
        this.masterJobId = masterJobId;
    }

    public String getPrevProcessId() {
        return prevProcessId;
    }

    public void setPrevProcessId(String prevProcessId) {
        this.prevProcessId = prevProcessId;
    }

    public String getProcessQueueId() {
        return processQueueId;
    }

    public void setProcessQueueId(String processQueueId) {
        this.processQueueId = processQueueId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getQueueProcessName() {
        return queueProcessName;
    }

    public void setQueueProcessName(String queueProcessName) {
        this.queueProcessName = queueProcessName;
    }

    public String getCurrentProcessName() {
        return currentProcessName;
    }

    public void setCurrentProcessName(String currentProcessName) {
        this.currentProcessName = currentProcessName;
    }

    public Boolean getHold() {
        return hold;
    }

    public void setHold(Boolean hold) {
        this.hold = hold;
    }

    public Boolean getCalculateTAT() {
        return calculateTAT;
    }

    public void setCalculateTAT(Boolean calculateTAT) {
        this.calculateTAT = calculateTAT;
    }

    public Boolean getBTATExceeded() {
        return bTATExceeded;
    }

    public void setBTATExceeded(Boolean bTATExceeded) {
        this.bTATExceeded = bTATExceeded;
    }

    public String getJobCreationDate() {
        return jobCreationDate;
    }

    public void setJobCreationDate(String jobCreationDate) {
        this.jobCreationDate = jobCreationDate;
    }

    public String getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(String jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public Object getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(Object jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public String getJobCreationDateSTR() {
        return jobCreationDateSTR;
    }

    public void setJobCreationDateSTR(String jobCreationDateSTR) {
        this.jobCreationDateSTR = jobCreationDateSTR;
    }

    public String getJobStartDateSTR() {
        return jobStartDateSTR;
    }

    public void setJobStartDateSTR(String jobStartDateSTR) {
        this.jobStartDateSTR = jobStartDateSTR;
    }

    public Object getJobEndDateSTR() {
        return jobEndDateSTR;
    }

    public void setJobEndDateSTR(Object jobEndDateSTR) {
        this.jobEndDateSTR = jobEndDateSTR;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getTotalTimeTaken() {
        return totalTimeTaken;
    }

    public void setTotalTimeTaken(Object totalTimeTaken) {
        this.totalTimeTaken = totalTimeTaken;
    }

    public String getUserId() {
        return userId;
    }


    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getUploadedByUserID() {
        return uploadedByUserID;
    }

    public void setUploadedByUserID(Object uploadedByUserID) {
        this.uploadedByUserID = uploadedByUserID;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Boolean getHasImagesAttached() {
        return hasImagesAttached;
    }

    public void setHasImagesAttached(Boolean hasImagesAttached) {
        this.hasImagesAttached = hasImagesAttached;
    }

    public Integer getHoursToSubtractFromTotalTimeTaken() {
        return hoursToSubtractFromTotalTimeTaken;
    }

    public void setHoursToSubtractFromTotalTimeTaken(Integer hoursToSubtractFromTotalTimeTaken) {
        this.hoursToSubtractFromTotalTimeTaken = hoursToSubtractFromTotalTimeTaken;
    }

    public Object getFosExecutive() {
        return fosExecutive;
    }

    public void setFosExecutive(Object fosExecutive) {
        this.fosExecutive = fosExecutive;
    }

    public Object getFosExecutiveId() {
        return fosExecutiveId;
    }

    public void setFosExecutiveId(Object fosExecutiveId) {
        this.fosExecutiveId = fosExecutiveId;
    }

    public Object getNbfcname() {
        return nbfcname;
    }

    public void setNbfcname(Object nbfcname) {
        this.nbfcname = nbfcname;
    }

    public String getTat() {
        return tat;
    }

    public void setTat(String tat) {
        this.tat = tat;
    }

    public Object getFilocation() {
        return filocation;
    }

    public void setFilocation(Object filocation) {
        this.filocation = filocation;
    }


    public class Id {

        @SerializedName("timestamp")
        @Expose
        private Long timestamp;
        @SerializedName("machineIdentifier")
        @Expose
        private Integer machineIdentifier;
        @SerializedName("processIdentifier")
        @Expose
        private Integer processIdentifier;
        @SerializedName("counter")
        @Expose
        private Integer counter;
        @SerializedName("timeSecond")
        @Expose
        private Integer timeSecond;
        @SerializedName("time")
        @Expose
        private Long time;
        @SerializedName("date")
        @Expose
        private String date;

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
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

        public Integer getCounter() {
            return counter;
        }

        public void setCounter(Integer counter) {
            this.counter = counter;
        }

        public Integer getTimeSecond() {
            return timeSecond;
        }

        public void setTimeSecond(Integer timeSecond) {
            this.timeSecond = timeSecond;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    public class Data {

        @SerializedName("Applicant")
        @Expose
        private JobDetailsResponse.Applicant applicant;

        public JobDetailsResponse.Applicant getApplicant() {
            return applicant;
        }

        public void setApplicant(JobDetailsResponse.Applicant applicant) {
            this.applicant = applicant;
        }

    }

}