package com.app.dusmile.model;

/**
 * Created by sumasoft on 30/01/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobDetailsResponse {

    @SerializedName("redirect")
    @Expose
    private Boolean redirect;
    @SerializedName("tabData")
    @Expose
    private String tabData;
    @SerializedName("JOB_ID")
    @Expose
    private String jOBID;
    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;
    @SerializedName("isDownloadFiles")
    @Expose
    private Boolean isDownloadFiles;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("NBFC_NAME")
    @Expose
    private String nBFCNAME;
    @SerializedName("details")
    @Expose
    private Details details;
    @SerializedName("subProcessFieldsData")
    @Expose
    private String subProcessFieldsData;

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    @SerializedName("latestVersion")
    @Expose
    private String latestVersion;

    public Boolean getRedirect() {
        return redirect;
    }

    public void setRedirect(Boolean redirect) {
        this.redirect = redirect;
    }

    public String getTabData() {
        return tabData;
    }

    public void setTabData(String tabData) {
        this.tabData = tabData;
    }

    public String getJOBID() {
        return jOBID;
    }

    public void setJOBID(String jOBID) {
        this.jOBID = jOBID;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public Boolean getIsDownloadFiles() {
        return isDownloadFiles;
    }

    public void setIsDownloadFiles(Boolean isDownloadFiles) {
        this.isDownloadFiles = isDownloadFiles;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getNBFCNAME() {
        return nBFCNAME;
    }

    public void setNBFCNAME(String nBFCNAME) {
        this.nBFCNAME = nBFCNAME;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public String getSubProcessFieldsData() {
        return subProcessFieldsData;
    }

    public void setSubProcessFieldsData(String subProcessFieldsData) {
        this.subProcessFieldsData = subProcessFieldsData;
    }

    public class ProcessDataJSON {

        @SerializedName("Applicant")
        @Expose
        private Applicant applicant;

        public Applicant getApplicant() {
            return applicant;
        }

        public void setApplicant(Applicant applicant) {
            this.applicant = applicant;
        }

    }

    public class Details {

        @SerializedName("ProcessDataJSON")
        @Expose
        private ProcessDataJSON processDataJSON;

        public ProcessDataJSON getProcessDataJSON() {
            return processDataJSON;
        }

        public void setProcessDataJSON(ProcessDataJSON processDataJSON) {
            this.processDataJSON = processDataJSON;
        }

    }


    public class Applicant {

        @SerializedName("applicationFormNo")
        @Expose
        private String applicationFormNumber;
        @SerializedName("JobType")
        @Expose
        private String jobType;
        @SerializedName("State_Name")
        @Expose
        private String stateName;
        @SerializedName("city_Name")
        @Expose
        private String cityName;
        @SerializedName("JobComment")
        @Expose
        private String jobComment;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("DateOfBirth")
        @Expose
        private String dateOfBirth;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("emailId")
        @Expose
        private String emailId;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Pincode")
        @Expose
        private String pincode;
        @SerializedName("Landmark")
        @Expose
        private String landmark;
        @SerializedName("job_id")
        @Expose
        private String jobId;
        @SerializedName("agencyName")
        @Expose
        private String agencyName;
        @SerializedName("dealerBranch")
        @Expose
        private String dealerBranch;

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getJobComment() {
            return jobComment;
        }

        public void setJobComment(String jobComment) {
            this.jobComment = jobComment;
        }

        public String getCustomer_name() {
            return customerName;
        }

        public void setCustomer_name(String customerName) {
            this.customerName = customerName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public void setAgencyName(String agencyName) {
            this.agencyName = agencyName;
        }

        public String getDealerBranch() {
            return dealerBranch;
        }

        public void setDealerBranch(String dealerBranch) {
            this.dealerBranch = dealerBranch;
        }

    }

    public class LoanDetailsTable {

        @SerializedName("EMI")
        @Expose
        private String EMI;
        @SerializedName("Company")
        @Expose
        private String Company;
        @SerializedName("product")
        @Expose
        private String product;
        @SerializedName("LoanAmount")
        @Expose
        private String LoanAmount;

        public String getEMI() {
            return EMI;
        }

        public void setEMI(String EMI) {
            this.EMI = EMI;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String Company) {
            this.Company = Company;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String Product) {
            this.product = Product;
        }

        public String getLoanAmount() {
            return LoanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            this.LoanAmount = loanAmount;
        }
    }


    public class AssetsTable {

        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("usage")
        @Expose
        private String usage;
        @SerializedName("Assets")
        @Expose
        private String Assets;
        @SerializedName("area")
        @Expose
        private String area;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUsage() {
            return usage;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public String getAssets() {
            return Assets;
        }

        public void setAssets(String Assets) {
            this.Assets = Assets;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

    }


    public class VehicleDetailsTable {

        @SerializedName("VehicleMake")
        @Expose
        private String VehicleMake;
        @SerializedName("RegistrationNo")
        @Expose
        private String RegistrationNo;
        @SerializedName("Vehicle")
        @Expose
        private String Vehicle;
        @SerializedName("VehicleUsage")
        @Expose
        private String VehicleUsage;

        public String getVehicleMake() {
            return VehicleMake;
        }

        public void setVehicleMake(String vehicleMake) {
            VehicleMake = vehicleMake;
        }

        public String getRegistrationNo() {
            return RegistrationNo;
        }

        public void setRegistrationNo(String registrationNo) {
            RegistrationNo = registrationNo;
        }

        public String getVehicle() {
            return Vehicle;
        }

        public void setVehicle(String vehicle) {
            Vehicle = vehicle;
        }

        public String getVehicleUsage() {
            return VehicleUsage;
        }

        public void setVehicleUsage(String vehicleUsage) {
            VehicleUsage = vehicleUsage;
        }


    }
}

