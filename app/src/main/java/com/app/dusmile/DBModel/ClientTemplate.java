package com.app.dusmile.DBModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 14/03/17.
 */

public class ClientTemplate implements Parcelable{
    String ID;
    String client_name;
    String template_name;
    String template_form_name_array;
    String version;
    String language;
    String is_deprecated;


    public ClientTemplate() {
    }

    protected ClientTemplate(Parcel in) {
        ID = in.readString();
        client_name = in.readString();
        template_name = in.readString();
        template_form_name_array = in.readString();
        version = in.readString();
        language = in.readString();
        is_deprecated = in.readString();
    }


    public String getIs_deprecated() {
        return is_deprecated;
    }

    public void setIs_deprecated(String is_deprecated) {
        this.is_deprecated = is_deprecated;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getTemplate_form_name_array() {
        return template_form_name_array;
    }

    public void setTemplate_form_name_array(String template_form_name_array) {
        this.template_form_name_array = template_form_name_array;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static Creator<ClientTemplate> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<ClientTemplate> CREATOR = new Creator<ClientTemplate>() {
        @Override
        public ClientTemplate createFromParcel(Parcel in) {
            return new ClientTemplate(in);
        }

        @Override
        public ClientTemplate[] newArray(int size) {
            return new ClientTemplate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(client_name);
        dest.writeString(template_name);
        dest.writeString(template_form_name_array);
        dest.writeString(version);
        dest.writeString(language);
        dest.writeString(is_deprecated);


    }
}
