package com.example.sumaforms.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.sumaforms.DBmodel.DynamicTableField;
import com.example.sumaforms.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 14/03/17.
 */

public class DynamicFieldTableDB {
    private static final String TAG = "DynamicFieldTableDB";
    public static final String TABLE_DYNAMIC_FIELD_TABLE = "dynamic_table_fields_Table";
    public static final String DYNAMIC_FIELD_TABLE_ID="dynamic_field_table_id";
    public static final String DYNAMIC_FIELD_TABLE_NAME="table_name";
    public static final String TEMPLATE_JSON_ID="template_json_id";
    public static final String DYNAMIC_FIELD_TABLE_HEADERS_ARRAY="table_headers_array";
    public static final String TABLE_KEY_ARRAY="table_keys_array";
    public static final String MANDATORY_TABLE_FIELDS="mandatory_fields";
    public static final String MANDATORY_TABLE_HEADERS="mandatory_table_headers";
    public static final String CLEAR_SUBPROCESS_FIELDS="clear_subprocess_fields";

    // Insert a case into the database
    public static long addDynamicFieldTableEntry(DynamicTableField record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(DYNAMIC_FIELD_TABLE_ID, record.getID());
            values.put(DYNAMIC_FIELD_TABLE_NAME, record.getTable_name());
            values.put(DYNAMIC_FIELD_TABLE_HEADERS_ARRAY, record.getTable_headers_array());
            values.put(TABLE_KEY_ARRAY, record.getTable_keys_array());
            values.put(TEMPLATE_JSON_ID, record.getTemplate_json_id());
            values.put(MANDATORY_TABLE_FIELDS, record.getMandatory_table_fields());
            values.put(MANDATORY_TABLE_HEADERS, record.getMandatory_table_headers());
            values.put(CLEAR_SUBPROCESS_FIELDS, record.getClear_subprocess_fields());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_DYNAMIC_FIELD_TABLE, null, values);
            Log.d(TAG,"Rows Inserted -- "+rowID);

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add case to database");
        }
        finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rowID;
    }


    //Update Record
    public static int updateDynamicFieldTable(DynamicTableField record,DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(DYNAMIC_FIELD_TABLE_NAME, record.getTable_name());
            values.put(DYNAMIC_FIELD_TABLE_HEADERS_ARRAY, record.getTable_headers_array());
            values.put(TABLE_KEY_ARRAY, record.getTable_keys_array());
            values.put(TEMPLATE_JSON_ID, record.getTemplate_json_id());
            values.put(MANDATORY_TABLE_FIELDS, record.getMandatory_table_fields());
            values.put(MANDATORY_TABLE_HEADERS, record.getMandatory_table_headers());
            values.put(CLEAR_SUBPROCESS_FIELDS, record.getClear_subprocess_fields());
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_DYNAMIC_FIELD_TABLE, values, DYNAMIC_FIELD_TABLE_ID + "= ?", new String[]{record.getID()});

            // Check if update succeeded
            if (rows != 0)
            {
                Log.d(TAG, "Number of rows updated - "+rows);
            }


        }catch (SQLiteException se)
        {
            Log.d(TAG, "Error while trying to update user");
        }
        finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rows;
    }


    /*
       * getting all LoginTemplate
       * */
    public static ArrayList<DynamicTableField> getAllDynamicFieldTable(DBHelper dbHelper) {
        ArrayList<DynamicTableField> dynamicTableFieldMasterList = new ArrayList<DynamicTableField>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_DYNAMIC_FIELD_TABLE ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    DynamicTableField dynamicTableField = new DynamicTableField();
                    dynamicTableField.setID(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_ID)));
                    dynamicTableField.setTable_name(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_NAME)));
                    dynamicTableField.setTable_headers_array(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_HEADERS_ARRAY)));
                    dynamicTableField.setTable_keys_array(c.getString(c.getColumnIndex(TABLE_KEY_ARRAY)));
                    dynamicTableField.setTemplate_json_id(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));
                    dynamicTableField.setMandatory_table_fields(Boolean.parseBoolean(c.getString(c.getColumnIndex(MANDATORY_TABLE_FIELDS))));
                    dynamicTableField.setMandatory_table_headers(c.getString(c.getColumnIndex(MANDATORY_TABLE_HEADERS)));
                    dynamicTableField.setClear_subprocess_fields(c.getString(c.getColumnIndex(CLEAR_SUBPROCESS_FIELDS)));
                    // adding to todo list
                    dynamicTableFieldMasterList.add(dynamicTableField);
                } while (c.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                c.close();
                db.close();
            }
        }
        return dynamicTableFieldMasterList;
    }


    /*
     * getting all LoginTemplate
     * */
    public static ArrayList<DynamicTableField> getAllDynamicFieldDependsOnJsonTemplateID(DBHelper dbHelper, String jsonTemplateId) {
        ArrayList<DynamicTableField> dynamicTableFieldMasterList = new ArrayList<DynamicTableField>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_DYNAMIC_FIELD_TABLE + " WHERE " + TEMPLATE_JSON_ID + " = '"+jsonTemplateId+"'";

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    DynamicTableField dynamicTableField = new DynamicTableField();
                    dynamicTableField.setID(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_ID)));
                    dynamicTableField.setTable_name(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_NAME)));
                    dynamicTableField.setTable_headers_array(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_HEADERS_ARRAY)));
                    dynamicTableField.setTable_keys_array(c.getString(c.getColumnIndex(TABLE_KEY_ARRAY)));
                    dynamicTableField.setTemplate_json_id(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));
                    dynamicTableField.setMandatory_table_fields(Boolean.parseBoolean(c.getString(c.getColumnIndex(MANDATORY_TABLE_FIELDS))));
                    dynamicTableField.setMandatory_table_headers(c.getString(c.getColumnIndex(MANDATORY_TABLE_HEADERS)));
                    dynamicTableField.setClear_subprocess_fields(c.getString(c.getColumnIndex(CLEAR_SUBPROCESS_FIELDS)));
                    // adding to todo list
                    dynamicTableFieldMasterList.add(dynamicTableField);
                } while (c.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                c.close();
                db.close();
            }
        }
        return dynamicTableFieldMasterList;
    }


    public static DynamicTableField getSingleDynamicFieldTable(DBHelper dbHelper, String Id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        DynamicTableField dynamicTableField =new DynamicTableField();
        String selectQuery = "SELECT  * FROM " + TABLE_DYNAMIC_FIELD_TABLE + " WHERE " + TEMPLATE_JSON_ID + " = '"+Id+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {

                    dynamicTableField.setID(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_ID)));
                    dynamicTableField.setTable_name(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_NAME)));
                    dynamicTableField.setTable_headers_array(c.getString(c.getColumnIndex(DYNAMIC_FIELD_TABLE_HEADERS_ARRAY)));
                    dynamicTableField.setTable_keys_array(c.getString(c.getColumnIndex(TABLE_KEY_ARRAY)));
                    dynamicTableField.setTemplate_json_id(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));
                    dynamicTableField.setMandatory_table_fields(Boolean.parseBoolean(c.getString(c.getColumnIndex(MANDATORY_TABLE_FIELDS))));
                    dynamicTableField.setMandatory_table_headers(c.getString(c.getColumnIndex(MANDATORY_TABLE_HEADERS)));
                    dynamicTableField.setClear_subprocess_fields(c.getString(c.getColumnIndex(CLEAR_SUBPROCESS_FIELDS)));
                    // adding to todo list
                } while (c.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                c.close();
                db.close();
            }
        }
        return dynamicTableField;
    }


    public static int getDynamicFieldTableCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_DYNAMIC_FIELD_TABLE;
        Log.d(TAG,countQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                cnt = cursor.getCount();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                cursor.close();
                db.close();
            }
        }
        return cnt;
    }

    public static void removeDynamicFieldsByJsonTemplateId(DBHelper dbHelper, String jsonTemplateId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_DYNAMIC_FIELD_TABLE + " WHERE " + TEMPLATE_JSON_ID + "= '" + jsonTemplateId + "'";
        try {
            Cursor c = db.rawQuery(deleteQuery, null);
            c.moveToFirst();
            c.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

}
