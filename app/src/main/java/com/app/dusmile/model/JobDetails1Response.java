package com.app.dusmile.model;

/**
 * Created by suma on 22/05/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



/**
 * Created by sumasoft on 30/01/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobDetails1Response {

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


        @SerializedName("RequestDateandTime")
        @Expose
        private String RequestDateandTime;
        @SerializedName("templateName")
        @Expose
        private String JobType;
        @SerializedName("State_Name")
        @Expose
        private String State_Name;
        @SerializedName("city_Name")
        @Expose
        private String city_Name;
        @SerializedName("Area_Name")
        @Expose
        private String Area_Name;
        @SerializedName("customer_name")
        @Expose
        private String customer_name;
        @SerializedName("Gender")
        @Expose
        private String Gender;
        @SerializedName("Mobile")
        @Expose
        private String Mobile;
        @SerializedName("ResidentialAddress")
        @Expose
        private String ResidentialAddress;
        @SerializedName("EmailID")
        @Expose
        private String EmailID;
        @SerializedName("Pincode")
        @Expose
        private String PinCode;
        @SerializedName("VerifierName")
        @Expose
        private String VerifierName;

        public List<LoanDetailsTable> getLoanDetailsTable() {
            return loanDetailsTable;
        }

        public void setLoanDetailsTable(List<LoanDetailsTable> loanDetailsTable) {
            this.loanDetailsTable = loanDetailsTable;
        }

        @SerializedName("LoanDetailsTable")
        @Expose
        private List<LoanDetailsTable> loanDetailsTable = null;

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }



        public String getLoanAmount() {
            return LoanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            LoanAmount = loanAmount;
        }



        public String getApplicationFormNo() {
            return applicationFormNo;
        }

        public void setApplicationFormNo(String applicationFormNo) {
            this.applicationFormNo = applicationFormNo;
        }

        @SerializedName("DateOfBirth")
        @Expose
        private String DateOfBirth;
        @SerializedName("Age")
        @Expose
        private String Age;
        @SerializedName("MaritalStatus")
        @Expose
        private String MaritalStatus;
        @SerializedName("NoofFamilyMembers")
        @Expose
        private String NoofFamilyMembers;
        @SerializedName("Isspouseworking")
        @Expose
        private String Isspouseworking;
        @SerializedName("IncomeOfSpouse")
        @Expose
        private String IncomeOfSpouse;
        @SerializedName("Anyotherloans")
        @Expose
        private String Anyotherloans;
        @SerializedName("FI_status")
        @Expose
        private String FI_status;
        @SerializedName("FI_Remarks")
        @Expose
        private String FI_Remarks;
        @SerializedName("LandmarkAndVillage")
        @Expose
        private String LandmarkAndVillage;


        @SerializedName("OfficeAddress")
        @Expose
        private String OfficeAddress;
        @SerializedName("AlternateNumber")
        @Expose
        private String AlternateNumber;
        @SerializedName("Entryallowedinhouse")
        @Expose
        private String Entryallowedinhouse;
        @SerializedName("PersonContacted")
        @Expose
        private String PersonContacted;
        @SerializedName("RelationshipwithApplicant")
        @Expose
        private String RelationshipwithApplicant;
        @SerializedName("VehicleApplied")
        @Expose
        private String VehicleApplied;
        @SerializedName("UsageofVehicle")
        @Expose
        private String UsageofVehicle;
        @SerializedName("VehicleLoanAmount")
        @Expose
        private String VehicleLoanAmount;
        @SerializedName("EndUseroftheProduct")
        @Expose
        private String EndUseroftheProduct;
        @SerializedName("ParkingAvailable")
        @Expose
        private String ParkingAvailable;


        @SerializedName("Yearsincity")
        @Expose
        private String Yearsincity;
        @SerializedName("YearsatResidence")
        @Expose
        private String YearsatResidence;
        @SerializedName("Ownershipofresidence")
        @Expose
        private String Ownershipofresidence;
        @SerializedName("LocationofHouse")
        @Expose
        private String LocationofHouse;
        @SerializedName("ApproachtoHouse")
        @Expose
        private String ApproachtoHouse;
        @SerializedName("Neighborhood_Locality")
        @Expose
        private String Neighborhood_Locality;
        @SerializedName("TypeofResidence")
        @Expose
        private String TypeofResidence;
        @SerializedName("Nameonhouseexterior")
        @Expose
        private String Nameonhouseexterior;
        @SerializedName("Exteriorconditions")
        @Expose
        private String Exteriorconditions;
        @SerializedName("Interior")
        @Expose
        private String Interior;
        @SerializedName("Standardofliving")
        @Expose
        private String Standardofliving;
        @SerializedName("Assetsatresidence")
        @Expose
        private String Assetsatresidence;
        @SerializedName("Residencecarpetarea")
        @Expose
        private String Residencecarpetarea;
        @SerializedName("product")
        @Expose
        private String product;

        @SerializedName("Company")
        @Expose
        private String Company;

        public String getGender() {
            return Gender;
        }

        public void setGender(String _gender) {
            Gender = _gender;
        }

        public String getRequestDateandTime() {
            return RequestDateandTime;
        }

        public void setRequestDateandTime(String requestDateandTime) {
            RequestDateandTime = requestDateandTime;
        }

        public String getJobType() {
            return JobType;
        }

        public void setJobType(String jobType) {
            JobType = jobType;
        }

        public String getState_Name() {
            return State_Name;
        }

        public void setState_Name(String state_Name) {
            State_Name = state_Name;
        }

        public String getCity_Name() {
            return city_Name;
        }

        public void setCity_Name(String city_Name) {
            this.city_Name = city_Name;
        }

        public String getArea_Name() {
            return Area_Name;
        }

        public void setArea_Name(String area_Name) {
            Area_Name = area_Name;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getResidentialAddress() {
            return ResidentialAddress;
        }

        public void setResidentialAddress(String residentialAddress) {
            ResidentialAddress = residentialAddress;
        }

        public String getEmailID() {
            return EmailID;
        }

        public void setEmailID(String emailID) {
            EmailID = emailID;
        }

        public String getPinCode() {
            return PinCode;
        }

        public void setPinCode(String pinCode) {
            PinCode = pinCode;
        }

        public String getVerifierName() {
            return VerifierName;
        }

        public void setVerifierName(String verifierName) {
            VerifierName = verifierName;
        }

        public String getDateOfBirth() {
            return DateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            DateOfBirth = dateOfBirth;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String age) {
            Age = age;
        }

        public String getMaritalStatus() {
            return MaritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            MaritalStatus = maritalStatus;
        }

        public String getNoofFamilyMembers() {
            return NoofFamilyMembers;
        }

        public void setNoofFamilyMembers(String noofFamilyMembers) {
            NoofFamilyMembers = noofFamilyMembers;
        }

        public String getIsspouseworking() {
            return Isspouseworking;
        }

        public void setIsspouseworking(String isspouseworking) {
            Isspouseworking = isspouseworking;
        }

        public String getIncomeOfSpouse() {
            return IncomeOfSpouse;
        }

        public void setIncomeOfSpouse(String incomeOfSpouse) {
            IncomeOfSpouse = incomeOfSpouse;
        }

        public String getAnyotherloans() {
            return Anyotherloans;
        }

        public void setAnyotherloans(String anyotherloans) {
            Anyotherloans = anyotherloans;
        }

        public String getFI_status() {
            return FI_status;
        }

        public void setFI_status(String FI_status) {
            this.FI_status = FI_status;
        }

        public String getFI_Remarks() {
            return FI_Remarks;
        }

        public void setFI_Remarks(String FI_Remarks) {
            this.FI_Remarks = FI_Remarks;
        }

        public String getLandmarkAndVillage() {
            return LandmarkAndVillage;
        }

        public void setLandmarkAndVillage(String landmarkAndVillage) {
            LandmarkAndVillage = landmarkAndVillage;
        }

        public String getOfficeAddress() {
            return OfficeAddress;
        }

        public void setOfficeAddress(String officeAddress) {
            OfficeAddress = officeAddress;
        }

        public String getAlternateNumber() {
            return AlternateNumber;
        }

        public void setAlternateNumber(String alternateNumber) {
            AlternateNumber = alternateNumber;
        }

        public String getEntryallowedinhouse() {
            return Entryallowedinhouse;
        }

        public void setEntryallowedinhouse(String entryallowedinhouse) {
            Entryallowedinhouse = entryallowedinhouse;
        }

        public String getPersonContacted() {
            return PersonContacted;
        }

        public void setPersonContacted(String personContacted) {
            PersonContacted = personContacted;
        }

        public String getRelationshipwithApplicant() {
            return RelationshipwithApplicant;
        }

        public void setRelationshipwithApplicant(String relationshipwithApplicant) {
            RelationshipwithApplicant = relationshipwithApplicant;
        }

        public String getVehicleApplied() {
            return VehicleApplied;
        }

        public void setVehicleApplied(String vehicleApplied) {
            VehicleApplied = vehicleApplied;
        }

        public String getUsageofVehicle() {
            return UsageofVehicle;
        }

        public void setUsageofVehicle(String usageofVehicle) {
            UsageofVehicle = usageofVehicle;
        }

        public String getVehicleLoanAmount() {
            return VehicleLoanAmount;
        }

        public void setVehicleLoanAmount(String vehicleLoanAmount) {
            VehicleLoanAmount = vehicleLoanAmount;
        }

        public String getEndUseroftheProduct() {
            return EndUseroftheProduct;
        }

        public void setEndUseroftheProduct(String endUseroftheProduct) {
            EndUseroftheProduct = endUseroftheProduct;
        }

        public String getParkingAvailable() {
            return ParkingAvailable;
        }

        public void setParkingAvailable(String parkingAvailable) {
            ParkingAvailable = parkingAvailable;
        }

        public String getYearsincity() {
            return Yearsincity;
        }

        public void setYearsincity(String yearsincity) {
            Yearsincity = yearsincity;
        }

        public String getYearsatResidence() {
            return YearsatResidence;
        }

        public void setYearsatResidence(String yearsatResidence) {
            YearsatResidence = yearsatResidence;
        }

        public String getOwnershipofresidence() {
            return Ownershipofresidence;
        }

        public void setOwnershipofresidence(String ownershipofresidence) {
            Ownershipofresidence = ownershipofresidence;
        }

        public String getLocationofHouse() {
            return LocationofHouse;
        }

        public void setLocationofHouse(String locationofHouse) {
            LocationofHouse = locationofHouse;
        }

        public String getApproachtoHouse() {
            return ApproachtoHouse;
        }

        public void setApproachtoHouse(String approachtoHouse) {
            ApproachtoHouse = approachtoHouse;
        }

        public String getNeighborhood_Locality() {
            return Neighborhood_Locality;
        }

        public void setNeighborhood_Locality(String neighborhood_Locality) {
            Neighborhood_Locality = neighborhood_Locality;
        }

        public String getTypeofResidence() {
            return TypeofResidence;
        }

        public void setTypeofResidence(String typeofResidence) {
            TypeofResidence = typeofResidence;
        }

        public String getNameonhouseexterior() {
            return Nameonhouseexterior;
        }

        public void setNameonhouseexterior(String nameonhouseexterior) {
            Nameonhouseexterior = nameonhouseexterior;
        }

        public String getExteriorconditions() {
            return Exteriorconditions;
        }

        public void setExteriorconditions(String exteriorconditions) {
            Exteriorconditions = exteriorconditions;
        }

        public String getInterior() {
            return Interior;
        }

        public void setInterior(String interior) {
            Interior = interior;
        }

        public String getStandardofliving() {
            return Standardofliving;
        }

        public void setStandardofliving(String standardofliving) {
            Standardofliving = standardofliving;
        }

        public String getAssetsatresidence() {
            return Assetsatresidence;
        }

        public void setAssetsatresidence(String assetsatresidence) {
            Assetsatresidence = assetsatresidence;
        }

        public String getResidencecarpetarea() {
            return Residencecarpetarea;
        }

        public void setResidencecarpetarea(String residencecarpetarea) {
            Residencecarpetarea = residencecarpetarea;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String company) {
            Company = company;
        }

        public String getEMI() {
            return EMI;
        }

        public void setEMI(String EMI) {
            this.EMI = EMI;
        }

        public String getAddress_ID_sighted() {
            return Address_ID_sighted;
        }

        public void setAddress_ID_sighted(String address_ID_sighted) {
            Address_ID_sighted = address_ID_sighted;
        }

        public String getAddress_ID_type() {
            return Address_ID_type;
        }

        public void setAddress_ID_type(String address_ID_type) {
            Address_ID_type = address_ID_type;
        }

        public String getAffilation_political_party() {
            return Affilation_political_party;
        }

        public void setAffilation_political_party(String affilation_political_party) {
            Affilation_political_party = affilation_political_party;
        }

        public String getNoofvisits() {
            return Noofvisits;
        }

        public void setNoofvisits(String noofvisits) {
            Noofvisits = noofvisits;
        }

        @SerializedName("LoanAmount")
        @Expose
        private String LoanAmount;
        @SerializedName("EMI")
        @Expose
        private String EMI;
        @SerializedName("Address_ID_sighted")
        @Expose
        private String Address_ID_sighted;
        @SerializedName("Address_ID_type")
        @Expose
        private String Address_ID_type;
        @SerializedName("Affilation_political_party")
        @Expose
        private String Affilation_political_party;
        @SerializedName("Noofvisits")
        @Expose
        private String Noofvisits;

        @SerializedName("NBFC_Job_Creator")
        @Expose
        private String nBFCJobCreator;
        @SerializedName("city_Name")
        @Expose
        private String cityName;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("VehicleApplied")
        @Expose
        private String vehicleApplied;
        @SerializedName("RequestDateandTime")
        @Expose
        private String requestDateandTime;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("Pincode")
        @Expose
        private String pinCode;
        @SerializedName("co_Applicant_name")
        @Expose
        private String coApplicantName;
        @SerializedName("templateName")
        @Expose
        private String jobType;
        @SerializedName("applicationFormNo")
        @Expose
        private String applicationFormNo;
        @SerializedName("State_Name")
        @Expose
        private String stateName;
        @SerializedName("isMaster")
        @Expose
        private String isMaster;
        @SerializedName("VerifierName")
        @Expose
        private String verifierName;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("VehicleLoanAmount")
        @Expose
        private String vehicleLoanAmount;
        @SerializedName("Area_Name")
        @Expose
        private String areaName;
        public String getNBFCJobCreator() {
            return nBFCJobCreator;
        }

        public void setNBFCJobCreator(String nBFCJobCreator) {
            this.nBFCJobCreator = nBFCJobCreator;
        }

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



        public String getCoApplicantName() {
            return coApplicantName;
        }

        public void setCoApplicantName(String coApplicantName) {
            this.coApplicantName = coApplicantName;
        }



        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getIsMaster() {
            return isMaster;
        }

        public void setIsMaster(String isMaster) {
            this.isMaster = isMaster;
        }



        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }


        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
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
}

