package com.app.dusmile.DBModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 14/03/17.
 */

public class TemplateJson implements Parcelable{

    String ID;
    String form_name;
    String field_json;
    String client_template_id;
    String is_table_exists;
    String controller_name;
    String mandatory_field_keys;
    String other_field_keys;

    public TemplateJson() {
    }

    protected TemplateJson(Parcel in) {
        ID = in.readString();
        form_name = in.readString();
        field_json = in.readString();
        client_template_id = in.readString();
        is_table_exists = in.readString();
        controller_name = in.readString();
        mandatory_field_keys = in.readString();
        other_field_keys = in.readString();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    public String getField_json() {
        return field_json;
    }

    public void setField_json(String field_json) {
        this.field_json = field_json;
    }

    public String getClient_template_id() {
        return client_template_id;
    }

    public void setClient_template_id(String client_template_id) {
        this.client_template_id = client_template_id;
    }

    public String getIs_table_exists() {
        return is_table_exists;
    }

    public void setIs_table_exists(String is_table_exists) {
        this.is_table_exists = is_table_exists;
    }

    public String getController_name() {
        return controller_name;
    }

    public void setController_name(String controller_name) {
        this.controller_name = controller_name;
    }

    public String getMandatory_field_keys() {
        return mandatory_field_keys;
    }

    public void setMandatory_field_keys(String mandatory_field_keys) {
        this.mandatory_field_keys = mandatory_field_keys;
    }

    public String getOther_field_keys() {
        return other_field_keys;
    }

    public void setOther_field_keys(String other_field_keys) {
        this.other_field_keys = other_field_keys;
    }

    public static Creator<TemplateJson> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<TemplateJson> CREATOR = new Creator<TemplateJson>() {
        @Override
        public TemplateJson createFromParcel(Parcel in) {
            return new TemplateJson(in);
        }

        @Override
        public TemplateJson[] newArray(int size) {
            return new TemplateJson[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(form_name);
        dest.writeString(field_json);
        dest.writeString(client_template_id);
        dest.writeString(is_table_exists);
        dest.writeString(controller_name);
        dest.writeString(mandatory_field_keys);
        dest.writeString(other_field_keys);
    }
}
