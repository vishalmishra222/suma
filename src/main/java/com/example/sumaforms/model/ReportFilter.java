package com.example.sumaforms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportFilter {

    @SerializedName("headers")
    @Expose
    private List<Header> headers = null;
    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;
    @SerializedName("reportId")
    @Expose
    private String reportId;
    @SerializedName("reportName")
    @Expose
    private String reportName;
    @SerializedName("reportHeaders")
    @Expose
    private List<String> reportHeaders = null;
    @SerializedName("reportFilters")
    @Expose
    private List<ReportFilterModel.ReportFilter> reportFilters = null;
    @SerializedName("reportData")
    @Expose
    private List<ReportDatum> reportData = null;
    @SerializedName("cardHeadersKeys")
    @Expose
    private List<CardHeadersKey> cardHeadersKeys = null;
    @SerializedName("reportHeadersKeys")
    @Expose
    private List<String> reportHeadersKeys = null;
    @SerializedName("reportHeaderKeys")
    @Expose
    private ReportHeaderKeys reportHeaderKeys;
    @SerializedName("hideColumns")
    @Expose
    private List<Object> hideColumns = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("reportButtons")
    @Expose
    private List<ReportButton> reportButtons = null;

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
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

    public List<ReportFilterModel.ReportFilter> getReportFilters() {
        return reportFilters;
    }

    public void setReportFilters(List<ReportFilterModel.ReportFilter> reportFilters) {
        this.reportFilters = reportFilters;
    }

    public List<ReportDatum> getReportData() {
        return reportData;
    }

    public void setReportData(List<ReportDatum> reportData) {
        this.reportData = reportData;
    }

    public List<CardHeadersKey> getCardHeadersKeys() {
        return cardHeadersKeys;
    }

    public void setCardHeadersKeys(List<CardHeadersKey> cardHeadersKeys) {
        this.cardHeadersKeys = cardHeadersKeys;
    }

    public List<String> getReportHeadersKeys() {
        return reportHeadersKeys;
    }

    public void setReportHeadersKeys(List<String> reportHeadersKeys) {
        this.reportHeadersKeys = reportHeadersKeys;
    }

    public ReportHeaderKeys getReportHeaderKeys() {
        return reportHeaderKeys;
    }

    public void setReportHeaderKeys(ReportHeaderKeys reportHeaderKeys) {
        this.reportHeaderKeys = reportHeaderKeys;
    }

    public List<Object> getHideColumns() {
        return hideColumns;
    }

    public void setHideColumns(List<Object> hideColumns) {
        this.hideColumns = hideColumns;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ReportButton> getReportButtons() {
        return reportButtons;
    }

    public void setReportButtons(List<ReportButton> reportButtons) {
        this.reportButtons = reportButtons;
    }

    public class CardHeadersKey {

        @SerializedName("weightage")
        @Expose
        private Integer weightage;
        @SerializedName("column")
        @Expose
        private List<Column> column = null;
        @SerializedName("row")
        @Expose
        private String row;

        public Integer getWeightage() {
            return weightage;
        }

        public void setWeightage(Integer weightage) {
            this.weightage = weightage;
        }

        public List<Column> getColumn() {
            return column;
        }

        public void setColumn(List<Column> column) {
            this.column = column;
        }

        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }

    }


    public class Column {

        @SerializedName("headerKey")
        @Expose
        private String headerKey;
        @SerializedName("layout_weight")
        @Expose
        private Integer layoutWeight;

        public String getHeaderKey() {
            return headerKey;
        }

        public void setHeaderKey(String headerKey) {
            this.headerKey = headerKey;
        }

        public Integer getLayoutWeight() {
            return layoutWeight;
        }

        public void setLayoutWeight(Integer layoutWeight) {
            this.layoutWeight = layoutWeight;
        }

    }

    public class Header {

        @SerializedName("columnDef")
        @Expose
        private String columnDef;
        @SerializedName("header")
        @Expose
        private String header;
        @SerializedName("isInnerJsonField")
        @Expose
        private String isInnerJsonField;
        @SerializedName("outerJsonKey")
        @Expose
        private Object outerJsonKey;
        @SerializedName("showIcon")
        @Expose
        private String showIcon;
        @SerializedName("iconName")
        @Expose
        private Object iconName;
        @SerializedName("iconColor")
        @Expose
        private Object iconColor;
        @SerializedName("functionName")
        @Expose
        private Object functionName;
        @SerializedName("iconParameterKey")
        @Expose
        private Object iconParameterKey;
        @SerializedName("iconClass")
        @Expose
        private String iconClass;
        @SerializedName("backgroundClass")
        @Expose
        private Object backgroundClass;

        public String getColumnDef() {
            return columnDef;
        }

        public void setColumnDef(String columnDef) {
            this.columnDef = columnDef;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getIsInnerJsonField() {
            return isInnerJsonField;
        }

        public void setIsInnerJsonField(String isInnerJsonField) {
            this.isInnerJsonField = isInnerJsonField;
        }

        public Object getOuterJsonKey() {
            return outerJsonKey;
        }

        public void setOuterJsonKey(Object outerJsonKey) {
            this.outerJsonKey = outerJsonKey;
        }

        public String getShowIcon() {
            return showIcon;
        }

        public void setShowIcon(String showIcon) {
            this.showIcon = showIcon;
        }

        public Object getIconName() {
            return iconName;
        }

        public void setIconName(Object iconName) {
            this.iconName = iconName;
        }

        public Object getIconColor() {
            return iconColor;
        }

        public void setIconColor(Object iconColor) {
            this.iconColor = iconColor;
        }

        public Object getFunctionName() {
            return functionName;
        }

        public void setFunctionName(Object functionName) {
            this.functionName = functionName;
        }

        public Object getIconParameterKey() {
            return iconParameterKey;
        }

        public void setIconParameterKey(Object iconParameterKey) {
            this.iconParameterKey = iconParameterKey;
        }

        public String getIconClass() {
            return iconClass;
        }

        public void setIconClass(String iconClass) {
            this.iconClass = iconClass;
        }

        public Object getBackgroundClass() {
            return backgroundClass;
        }

        public void setBackgroundClass(Object backgroundClass) {
            this.backgroundClass = backgroundClass;
        }

    }


    public class Id {

        @SerializedName("timestamp")
        @Expose
        private Integer timestamp;
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

        public Integer getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Integer timestamp) {
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

    public class ReportDatum {

        @SerializedName("city_Name")
        @Expose
        private String cityName;
        @SerializedName("JobType")
        @Expose
        private String jobType;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("job_id")
        @Expose
        private String jobId;
        @SerializedName("job_creation_date_STR")
        @Expose
        private String jobCreationDateSTR;
        @SerializedName("bTATExceeded")
        @Expose
        private Boolean bTATExceeded;
        @SerializedName("_id")
        @Expose
        private Id id;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("PinCode")
        @Expose
        private String pinCode;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public Boolean getBTATExceeded() {
            return bTATExceeded;
        }

        public void setBTATExceeded(Boolean bTATExceeded) {
            this.bTATExceeded = bTATExceeded;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

    }

    public class ReportHeaderKeys {

        @SerializedName("job_id")
        @Expose
        private Integer jobId;
        @SerializedName("job_creation_date_STR")
        @Expose
        private Integer jobCreationDateSTR;
        @SerializedName("JobType")
        @Expose
        private String jobType;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("totalTimeTaken")
        @Expose
        private String totalTimeTaken;
        @SerializedName("bTATExceeded")
        @Expose
        private Integer bTATExceeded;
        @SerializedName("city_Name")
        @Expose
        private String cityName;
        @SerializedName("PinCode")
        @Expose
        private String pinCode;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("ApplicantOrCoApplicant")
        @Expose
        private String applicantOrCoApplicant;
        @SerializedName("status")
        @Expose
        private Integer status;

        public Integer getJobId() {
            return jobId;
        }

        public void setJobId(Integer jobId) {
            this.jobId = jobId;
        }

        public Integer getJobCreationDateSTR() {
            return jobCreationDateSTR;
        }

        public void setJobCreationDateSTR(Integer jobCreationDateSTR) {
            this.jobCreationDateSTR = jobCreationDateSTR;
        }

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getTotalTimeTaken() {
            return totalTimeTaken;
        }

        public void setTotalTimeTaken(String totalTimeTaken) {
            this.totalTimeTaken = totalTimeTaken;
        }

        public Integer getBTATExceeded() {
            return bTATExceeded;
        }

        public void setBTATExceeded(Integer bTATExceeded) {
            this.bTATExceeded = bTATExceeded;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getApplicantOrCoApplicant() {
            return applicantOrCoApplicant;
        }

        public void setApplicantOrCoApplicant(String applicantOrCoApplicant) {
            this.applicantOrCoApplicant = applicantOrCoApplicant;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }

    public class ReportButton {

        @SerializedName("onClick")
        @Expose
        private String onClick;
        @SerializedName("fieldName")
        @Expose
        private String fieldName;
        @SerializedName("lable")
        @Expose
        private String lable;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("fieldID")
        @Expose
        private Integer fieldID;

        public String getOnClick() {
            return onClick;
        }

        public void setOnClick(String onClick) {
            this.onClick = onClick;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getFieldID() {
            return fieldID;
        }

        public void setFieldID(Integer fieldID) {
            this.fieldID = fieldID;
        }

    }
}
