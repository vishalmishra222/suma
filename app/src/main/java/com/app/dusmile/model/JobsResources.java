package com.app.dusmile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
public class JobsResources {
        @SerializedName("dataApi")
        @Expose
        private String dataApi;
        @SerializedName("method")
        @Expose
        private String method;

        public String getDataApi() {
            return dataApi;
        }

        public void setDataApi(String dataApi) {
            this.dataApi = dataApi;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
}
