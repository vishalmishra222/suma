package com.app.dusmile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportDataResponseModel {

@SerializedName("redirect")
@Expose
public Boolean redirect;
@SerializedName("redirectURL")
@Expose
public String redirectURL;
@SerializedName("reportHeaders")
@Expose
public List<String> reportHeaders = null;
@SerializedName("success")
@Expose
public Boolean success;
@SerializedName("reportData")
@Expose
public List<ReportDatum> reportData = null;
@SerializedName("reportHeadersUI")
@Expose
public List<ReportHeadersUI> reportHeadersUI = null;

public Boolean getRedirect() {
return redirect;
}

public void setRedirect(Boolean redirect) {
this.redirect = redirect;
}

public String getRedirectURL() {
return redirectURL;
}

public void setRedirectURL(String redirectURL) {
this.redirectURL = redirectURL;
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

public List<ReportHeadersUI> getReportHeadersUI() {
return reportHeadersUI;
}

public void setReportHeadersUI(List<ReportHeadersUI> reportHeadersUI) {
this.reportHeadersUI = reportHeadersUI;
}

    public class ReportDatum {

        @SerializedName("NBFCName")
        @Expose
        public String NBFCName;
        @SerializedName("data")
        @Expose
        public Data data;
        @SerializedName("job_creation_date_STR")
        @Expose
        public String job_creation_date_STR;
        @SerializedName("job_id")
        @Expose
        public String job_id;
        @SerializedName("job_start_date_STR")
        @Expose
        public String job_start_date_STR;
        @SerializedName("job_end_date_STR")
        @Expose
        public String job_end_date_STR;
        @SerializedName("status")
        @Expose
        public String status;

        public String getNBFCName() {
            return NBFCName;
        }

        public void setNBFCName(String NBFCName) {
            this.NBFCName = NBFCName;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getJob_creation_date_STR() {
            return job_creation_date_STR;
        }

        public void setJob_creation_date_STR(String job_creation_date_STR) {
            this.job_creation_date_STR = job_creation_date_STR;
        }

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getJob_start_date_STR() {
            return job_start_date_STR;
        }

        public void setJob_start_date_STR(String job_start_date_STR) {
            this.job_start_date_STR = job_start_date_STR;
        }

        public String getJob_end_date_STR() {
            return job_end_date_STR;
        }

        public void setJob_end_date_STR(String job_end_date_STR) {
            this.job_end_date_STR = job_end_date_STR;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


    public class ReportHeadersUI {

        @SerializedName("data")
        @Expose
        public String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }

    public class Applicant {


        @SerializedName("templateName")
        @Expose
        public String JobType;
        @SerializedName("city_Name")
        @Expose
        public String city_Name;
        @SerializedName("applicationFormNo")
        @Expose
        public String applicationFormNo;
        @SerializedName("Pincode")
        @Expose
        public String PinCode;

        public Applicant() {
        }

        public String getJobType() {
            return JobType;
        }

        public void setJobType(String jobType) {
            JobType = jobType;
        }

        public String getCity_Name() {
            return city_Name;
        }

        public void setCity_Name(String city_Name) {
            this.city_Name = city_Name;
        }

        public String getApplicationFormNo() {
            return applicationFormNo;
        }

        public void setApplicationFormNo(String applicationFormNo) {
            this.applicationFormNo = applicationFormNo;
        }

        public String getPinCode() {
            return PinCode;
        }

        public void setPinCode(String pinCode) {
            PinCode = pinCode;
        }
    }

    public class Data {

        @SerializedName("Applicant")
        @Expose
        public Applicant Applicant;

        public ReportDataResponseModel.Applicant getApplicant() {
            return Applicant;
        }

        public void setApplicant(ReportDataResponseModel.Applicant applicant) {
            Applicant = applicant;
        }
    }

}