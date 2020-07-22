package com.app.dusmile.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordErrorModel {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("errors")
    @Expose
    private List<Error> errors = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("details")
    @Expose
    private List<String> details = null;

    public List<String> getDetails() {
        return details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public static class Error {

        @SerializedName("codes")
        @Expose
        private List<String> codes = null;
        @SerializedName("arguments")
        @Expose
        private List<Argument> arguments = null;
        @SerializedName("defaultMessage")
        @Expose
        private String defaultMessage;
        @SerializedName("objectName")
        @Expose
        private String objectName;
        @SerializedName("field")
        @Expose
        private String field;
        @SerializedName("rejectedValue")
        @Expose
        private String rejectedValue;
        @SerializedName("bindingFailure")
        @Expose
        private Boolean bindingFailure;
        @SerializedName("code")
        @Expose
        private String code;

        public List<String> getCodes() {
            return codes;
        }

        public void setCodes(List<String> codes) {
            this.codes = codes;
        }

        public List<Argument> getArguments() {
            return arguments;
        }

        public void setArguments(List<Argument> arguments) {
            this.arguments = arguments;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(String rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public Boolean getBindingFailure() {
            return bindingFailure;
        }

        public void setBindingFailure(Boolean bindingFailure) {
            this.bindingFailure = bindingFailure;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    public class Argument {

        @SerializedName("codes")
        @Expose
        private List<String> codes = null;
        @SerializedName("arguments")
        @Expose
        private Object arguments;
        @SerializedName("defaultMessage")
        @Expose
        private String defaultMessage;
        @SerializedName("code")
        @Expose
        private String code;

        public List<String> getCodes() {
            return codes;
        }

        public void setCodes(List<String> codes) {
            this.codes = codes;
        }

        public Object getArguments() {
            return arguments;
        }

        public void setArguments(Object arguments) {
            this.arguments = arguments;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

}