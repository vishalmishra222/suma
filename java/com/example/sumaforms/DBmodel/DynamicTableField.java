package com.example.sumaforms.DBmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suma on 14/03/17.
 */

public class DynamicTableField implements Parcelable{

    String ID;
    String table_name;
    String table_headers_array;
    String table_keys_array;
    boolean mandatory_table_fields;
    String mandatory_table_headers;
    String clear_subprocess_fields;
    String template_json_id;

    public DynamicTableField() {
    }

    protected DynamicTableField(Parcel in) {
        ID = in.readString();
        table_name = in.readString();
        table_headers_array = in.readString();
        table_keys_array = in.readString();
        mandatory_table_fields = Boolean.parseBoolean(in.readString());
        mandatory_table_headers = in.readString();
        clear_subprocess_fields = in.readString();
        template_json_id = in.readString();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_headers_array() {
        return table_headers_array;
    }

    public void setTable_headers_array(String table_headers_array) {
        this.table_headers_array = table_headers_array;
    }

    public String getTable_keys_array() {
        return table_keys_array;
    }

    public void setTable_keys_array(String table_keys_array) {
        this.table_keys_array = table_keys_array;
    }

    public boolean getMandatory_table_fields() {
        return mandatory_table_fields;
    }

    public void setMandatory_table_fields(boolean mandatory_table_fields) {
        this.mandatory_table_fields = mandatory_table_fields;
    }

    public String getMandatory_table_headers() {
        return mandatory_table_headers;
    }

    public void setMandatory_table_headers(String mandatory_table_headers) {
        this.mandatory_table_headers = mandatory_table_headers;
    }

    public String getClear_subprocess_fields() {
        return clear_subprocess_fields;
    }

    public void setClear_subprocess_fields(String clear_subprocess_fields) {
        this.clear_subprocess_fields = clear_subprocess_fields;
    }

    public String getTemplate_json_id() {
        return template_json_id;
    }

    public void setTemplate_json_id(String template_json_id) {
        this.template_json_id = template_json_id;
    }

    public static Creator<DynamicTableField> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<DynamicTableField> CREATOR = new Creator<DynamicTableField>() {
        @Override
        public DynamicTableField createFromParcel(Parcel in) {
            return new DynamicTableField(in);
        }

        @Override
        public DynamicTableField[] newArray(int size) {
            return new DynamicTableField[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(table_name);
        dest.writeString(table_headers_array);
        dest.writeString(table_keys_array);
        dest.writeString(String.valueOf(mandatory_table_fields));
        dest.writeString(mandatory_table_headers);
        dest.writeString(clear_subprocess_fields);
        dest.writeString(template_json_id);
    }
}
