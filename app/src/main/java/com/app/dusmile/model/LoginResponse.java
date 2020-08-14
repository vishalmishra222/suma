package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("loggedInUser")
    @Expose
    private LoggedInUser loggedInUser;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public class LoggedInUser {

        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("userRole")
        @Expose
        private String userRole;
        @SerializedName("lastLoginDate")
        @Expose
        private Object lastLoginDate;
        @SerializedName("menuList")
        @Expose
        private List<MenuList> menuList = null;
        @SerializedName("role")
        @Expose
        private Role role;
        @SerializedName("fullName")
        @Expose
        private String fullName;
        @SerializedName("oldPassword")
        @Expose
        private Object oldPassword;
        @SerializedName("password")
        @Expose
        private Object password;
        @SerializedName("workflow")
        @Expose
        private Workflow workflow;
        @SerializedName("agencyName")
        @Expose
        private String agencyName;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
        }

        public Object getLastLoginDate() {
            return lastLoginDate;
        }

        public void setLastLoginDate(Object lastLoginDate) {
            this.lastLoginDate = lastLoginDate;
        }

        public List<MenuList> getMenuList() {
            return menuList;
        }

        public void setMenuList(List<MenuList> menuList) {
            this.menuList = menuList;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Object getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(Object oldPassword) {
            this.oldPassword = oldPassword;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public Workflow getWorkflow() {
            return workflow;
        }

        public void setWorkflow(Workflow workflow) {
            this.workflow = workflow;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public void setAgencyName(String agencyName) {
            this.agencyName = agencyName;
        }

    }

    public class MenuList {

        @SerializedName("id")
        @Expose
        private Id id;
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
        private List<MenuDetails.GroupToCategoryToSubCategory.SubCategory> subCategory = null;
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

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

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

        public List<MenuDetails.GroupToCategoryToSubCategory.SubCategory> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(List<MenuDetails.GroupToCategoryToSubCategory.SubCategory> subCategory) {
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
        @SerializedName("time")
        @Expose
        private long time;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("timeSecond")
        @Expose
        private Integer timeSecond;

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

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTimeSecond() {
            return timeSecond;
        }

        public void setTimeSecond(Integer timeSecond) {
            this.timeSecond = timeSecond;
        }

    }

    public class Id_ {

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
        @SerializedName("time")
        @Expose
        private long time;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("timeSecond")
        @Expose
        private Integer timeSecond;

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

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTimeSecond() {
            return timeSecond;
        }

        public void setTimeSecond(Integer timeSecond) {
            this.timeSecond = timeSecond;
        }

    }

    public class ManagerRole {

        @SerializedName("managerRoles")
        @Expose
        private String managerRoles;

        public String getManagerRoles() {
            return managerRoles;
        }

        public void setManagerRoles(String managerRoles) {
            this.managerRoles = managerRoles;
        }

    }

    public class Role {

        @SerializedName("id")
        @Expose
        private Id_ id;
        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("roleName")
        @Expose
        private String roleName;
        @SerializedName("homePage")
        @Expose
        private String homePage;
        @SerializedName("fieldDisableList")
        @Expose
        private Object fieldDisableList;
        @SerializedName("fieldEnableList")
        @Expose
        private Object fieldEnableList;
        @SerializedName("collectionName")
        @Expose
        private String collectionName;
        @SerializedName("managerRoles")
        @Expose
        private List<ManagerRole> managerRoles = null;

        public Id_ getId() {
            return id;
        }

        public void setId(Id_ id) {
            this.id = id;
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

        public String getHomePage() {
            return homePage;
        }

        public void setHomePage(String homePage) {
            this.homePage = homePage;
        }

        public Object getFieldDisableList() {
            return fieldDisableList;
        }

        public void setFieldDisableList(Object fieldDisableList) {
            this.fieldDisableList = fieldDisableList;
        }

        public Object getFieldEnableList() {
            return fieldEnableList;
        }

        public void setFieldEnableList(Object fieldEnableList) {
            this.fieldEnableList = fieldEnableList;
        }

        public String getCollectionName() {
            return collectionName;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }

        public List<ManagerRole> getManagerRoles() {
            return managerRoles;
        }

        public void setManagerRoles(List<ManagerRole> managerRoles) {
            this.managerRoles = managerRoles;
        }

    }

    public class Workflow {

        @SerializedName("102")
        @Expose
        private String _102;

        public String get102() {
            return _102;
        }

        public void set102(String _102) {
            this._102 = _102;
        }

    }
}
