package com.app.dusmile.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportMetaData {

    @SerializedName("reportHeadersKeys")
    @Expose
    private List<String> reportHeadersKeys = null;
    @SerializedName("headers")
    @Expose
    private List<Header> headers = null;
    @SerializedName("reportHeaderKeys")
    @Expose
    private ReportHeaderKeys reportHeaderKeys;
    @SerializedName("reportName")
    @Expose
    private String reportName;
    @SerializedName("reportId")
    @Expose
    private String reportId;
    @SerializedName("reportHeaders")
    @Expose
    private List<String> reportHeaders = null;
    @SerializedName("hideColumns")
    @Expose
    private List<Object> hideColumns = null;
    @SerializedName("reportFilters")
    @Expose
    private List<Object> reportFilters = null;
    @SerializedName("resources")
    @Expose
    private Object resources;
    @SerializedName("cardHeadersKeys")
    @Expose
    private List<CardHeadersKey> cardHeadersKeys = null;
    @SerializedName("workflowId")
    @Expose
    private String workflowId;
    @SerializedName("reportButtons")
    @Expose
    private List<ReportButton> reportButtons = null;

    public List<String> getReportHeadersKeys() {
        return reportHeadersKeys;
    }

    public void setReportHeadersKeys(List<String> reportHeadersKeys) {
        this.reportHeadersKeys = reportHeadersKeys;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public ReportHeaderKeys getReportHeaderKeys() {
        return reportHeaderKeys;
    }

    public void setReportHeaderKeys(ReportHeaderKeys reportHeaderKeys) {
        this.reportHeaderKeys = reportHeaderKeys;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public List<String> getReportHeaders() {
        return reportHeaders;
    }

    public void setReportHeaders(List<String> reportHeaders) {
        this.reportHeaders = reportHeaders;
    }

    public List<Object> getHideColumns() {
        return hideColumns;
    }

    public void setHideColumns(List<Object> hideColumns) {
        this.hideColumns = hideColumns;
    }

    public List<Object> getReportFilters() {
        return reportFilters;
    }

    public void setReportFilters(List<Object> reportFilters) {
        this.reportFilters = reportFilters;
    }

    public Object getResources() {
        return resources;
    }

    public void setResources(Object resources) {
        this.resources = resources;
    }

    public List<CardHeadersKey> getCardHeadersKeys() {
        return cardHeadersKeys;
    }

    public void setCardHeadersKeys(List<CardHeadersKey> cardHeadersKeys) {
        this.cardHeadersKeys = cardHeadersKeys;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
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
        private String iconName;
        @SerializedName("iconColor")
        @Expose
        private String iconColor;
        @SerializedName("functionName")
        @Expose
        private String functionName;
        @SerializedName("iconParameterKey")
        @Expose
        private String iconParameterKey;
        @SerializedName("iconClass")
        @Expose
        private String iconClass;
        @SerializedName("backgroundClass")
        @Expose
        private Object backgroundClass;
        @SerializedName("redirectURL")
        @Expose
        private Object redirectURL;
        @SerializedName("redirectURLParam")
        @Expose
        private Object redirectURLParam;

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

        public String getIconName() {
            return iconName;
        }

        public void setIconName(String iconName) {
            this.iconName = iconName;
        }

        public String getIconColor() {
            return iconColor;
        }

        public void setIconColor(String iconColor) {
            this.iconColor = iconColor;
        }

        public String getFunctionName() {
            return functionName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }

        public String getIconParameterKey() {
            return iconParameterKey;
        }

        public void setIconParameterKey(String iconParameterKey) {
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

        public Object getRedirectURL() {
            return redirectURL;
        }

        public void setRedirectURL(Object redirectURL) {
            this.redirectURL = redirectURL;
        }

        public Object getRedirectURLParam() {
            return redirectURLParam;
        }

        public void setRedirectURLParam(Object redirectURLParam) {
            this.redirectURLParam = redirectURLParam;
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

    public class ReportHeaderKeys {

        @SerializedName("job_id")
        @Expose
        private Integer jobId;
        @SerializedName("job_creation_date_STR")
        @Expose
        private String jobCreationDateSTR;
        @SerializedName("JobType")
        @Expose
        private String jobType;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("bTATExceeded")
        @Expose
        private Integer bTATExceeded;
        @SerializedName("city_Name")
        @Expose
        private String cityName;
        @SerializedName("Pincode")
        @Expose
        private String pincode;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("Product")
        @Expose
        private String product;
        @SerializedName("TAT")
        @Expose
        private String tAT;
        @SerializedName("ASSIGNED_TAT")
        @Expose
        private String aSSIGNEDTAT;

        public Integer getJobId() {
            return jobId;
        }

        public void setJobId(Integer jobId) {
            this.jobId = jobId;
        }

        public String getJobCreationDateSTR() {
            return jobCreationDateSTR;
        }

        public void setJobCreationDateSTR(String jobCreationDateSTR) {
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

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getTAT() {
            return tAT;
        }

        public void setTAT(String tAT) {
            this.tAT = tAT;
        }

        public String getASSIGNEDTAT() {
            return aSSIGNEDTAT;
        }

        public void setASSIGNEDTAT(String aSSIGNEDTAT) {
            this.aSSIGNEDTAT = aSSIGNEDTAT;
        }

    }
}
