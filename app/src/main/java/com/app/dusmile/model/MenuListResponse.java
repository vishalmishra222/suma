package com.app.dusmile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuListResponse {

    @SerializedName("workflowID")
    @Expose
    private String workflowID;
    @SerializedName("workflowList")
    @Expose
    private List<WorkflowList> workflowList = null;
    @SerializedName("workflowMenuList")
    @Expose
    private List<WorkflowMenuList> workflowMenuList = null;
    @SerializedName("homePage")
    @Expose
    private String homePage;
    @SerializedName("roleId")
    @Expose
    private Integer roleId;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("agencyName")
    @Expose
    private String agencyName;
    @SerializedName("dealerBranch")
    @Expose
    private List<String> dealerBranch = null;
    @SerializedName("stateName")
    @Expose
    private Object stateName;

    public String getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(String workflowID) {
        this.workflowID = workflowID;
    }

    public List<WorkflowList> getWorkflowList() {
        return workflowList;
    }

    public void setWorkflowList(List<WorkflowList> workflowList) {
        this.workflowList = workflowList;
    }

    public List<WorkflowMenuList> getWorkflowMenuList() {
        return workflowMenuList;
    }

    public void setWorkflowMenuList(List<WorkflowMenuList> workflowMenuList) {
        this.workflowMenuList = workflowMenuList;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public List<String> getDealerBranch() {
        return dealerBranch;
    }

    public void setDealerBranch(List<String> dealerBranch) {
        this.dealerBranch = dealerBranch;
    }

    public Object getStateName() {
        return stateName;
    }

    public void setStateName(Object stateName) {
        this.stateName = stateName;
    }

    public class WorkflowList {

        @SerializedName("workflowName")
        @Expose
        private String workflowName;
        @SerializedName("workflowId")
        @Expose
        private String workflowId;

        public String getWorkflowName() {
            return workflowName;
        }

        public void setWorkflowName(String workflowName) {
            this.workflowName = workflowName;
        }

        public String getWorkflowId() {
            return workflowId;
        }

        public void setWorkflowId(String workflowId) {
            this.workflowId = workflowId;
        }

    }

    public class WorkflowMenuList {

        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("seq_id")
        @Expose
        private Integer seqId;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("subCategory")
        @Expose
        private List<Object> subCategory = null;
        @SerializedName("action")
        @Expose
        private String action;
        @SerializedName("methodType")
        @Expose
        private String methodType;
        @SerializedName("iconClass")
        @Expose
        private String iconClass;
        @SerializedName("callFunction")
        @Expose
        private Boolean callFunction;
        @SerializedName("functionName")
        @Expose
        private String functionName;
        @SerializedName("iconColor")
        @Expose
        private String iconColor;
        @SerializedName("product")
        @Expose
        private Object product;
        @SerializedName("workflowId")
        @Expose
        private String workflowId;

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public Integer getSeqId() {
            return seqId;
        }

        public void setSeqId(Integer seqId) {
            this.seqId = seqId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<Object> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(List<Object> subCategory) {
            this.subCategory = subCategory;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getMethodType() {
            return methodType;
        }

        public void setMethodType(String methodType) {
            this.methodType = methodType;
        }

        public String getIconClass() {
            return iconClass;
        }

        public void setIconClass(String iconClass) {
            this.iconClass = iconClass;
        }

        public Boolean getCallFunction() {
            return callFunction;
        }

        public void setCallFunction(Boolean callFunction) {
            this.callFunction = callFunction;
        }

        public String getFunctionName() {
            return functionName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }

        public String getIconColor() {
            return iconColor;
        }

        public void setIconColor(String iconColor) {
            this.iconColor = iconColor;
        }

        public Object getProduct() {
            return product;
        }

        public void setProduct(Object product) {
            this.product = product;
        }

        public String getWorkflowId() {
            return workflowId;
        }

        public void setWorkflowId(String workflowId) {
            this.workflowId = workflowId;
        }

    }
}