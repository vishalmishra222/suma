package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.database.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 14/03/17.
 */

public class ClientTemplateDB {

    private static final String TAG = "ClientTemplateDB";
    public static final String TABLE_CLIENT_TEMPLATE = "client_template_Table";
    public static final String CLIENT_TEMPLATE_ID="client_template_id";
    public static final String CLIENT_NAME="client_name";
    public static final String TEMPLATE_NAME="template_name";
    public static final String TEMPLATE_FORM_NAME_ARRAY="form_name_array";
    public static final String TEMPLATE_VERSION="version";
    public static final String IS_DEPRECATED_TEMPLATE="is_deprecated";
    public static final String JSON_LANGUAGE = "language";

    public static long addClientTemplateEntry(ClientTemplate record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(CLIENT_TEMPLATE_ID, record.getID());
            values.put(CLIENT_NAME, record.getClient_name());
            values.put(TEMPLATE_NAME, record.getTemplate_name());
            values.put(TEMPLATE_FORM_NAME_ARRAY, record.getTemplate_form_name_array());
            values.put(TEMPLATE_VERSION, record.getVersion());
            values.put(IS_DEPRECATED_TEMPLATE, record.getIs_deprecated());
            values.put(JSON_LANGUAGE, record.getLanguage());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_CLIENT_TEMPLATE, null, values);
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
    public static int updateClientTemplate(ClientTemplate record, DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(CLIENT_TEMPLATE_ID, record.getID());
            values.put(CLIENT_NAME, record.getClient_name());
            values.put(TEMPLATE_NAME, record.getTemplate_name());
            values.put(TEMPLATE_FORM_NAME_ARRAY, record.getTemplate_form_name_array());
            values.put(TEMPLATE_VERSION, record.getVersion());
            values.put(IS_DEPRECATED_TEMPLATE, record.getIs_deprecated());
            values.put(JSON_LANGUAGE, record.getLanguage());
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_CLIENT_TEMPLATE, values, CLIENT_TEMPLATE_ID + "= ?", new String[]{record.getID()});

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
    public static ArrayList<ClientTemplate> getAllClientTemplate(DBHelper dbHelper) {
        ArrayList<ClientTemplate> clientTemplateList = new ArrayList<ClientTemplate>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    ClientTemplate clientTemplate = new ClientTemplate();
                    clientTemplate.setID(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    clientTemplate.setClient_name(c.getString(c.getColumnIndex(CLIENT_NAME)));
                    clientTemplate.setTemplate_form_name_array(c.getString(c.getColumnIndex(TEMPLATE_FORM_NAME_ARRAY)));
                    clientTemplate.setTemplate_name(c.getString(c.getColumnIndex(TEMPLATE_NAME)));
                    clientTemplate.setVersion(c.getString(c.getColumnIndex(TEMPLATE_VERSION)));
                    clientTemplate.setIs_deprecated(c.getString(c.getColumnIndex(IS_DEPRECATED_TEMPLATE)));
                    clientTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
                    // adding to todo list
                    clientTemplateList.add(clientTemplate);
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
        return clientTemplateList;
    }


    public static ArrayList<ClientTemplate> getAllClientTemplateDependsOnDeprecatedFlag(DBHelper dbHelper, String flag) {
        ArrayList<ClientTemplate> clientTemplateList = new ArrayList<ClientTemplate>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + IS_DEPRECATED_TEMPLATE + " = '"+flag+"' ";

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    ClientTemplate clientTemplate = new ClientTemplate();
                    clientTemplate.setID(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    clientTemplate.setClient_name(c.getString(c.getColumnIndex(CLIENT_NAME)));
                    clientTemplate.setTemplate_form_name_array(c.getString(c.getColumnIndex(TEMPLATE_FORM_NAME_ARRAY)));
                    clientTemplate.setTemplate_name(c.getString(c.getColumnIndex(TEMPLATE_NAME)));
                    clientTemplate.setVersion(c.getString(c.getColumnIndex(TEMPLATE_VERSION)));
                    clientTemplate.setIs_deprecated(c.getString(c.getColumnIndex(IS_DEPRECATED_TEMPLATE)));
                    clientTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
                    // adding to todo list
                    clientTemplateList.add(clientTemplate);
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
        return clientTemplateList;
    }

    public static ClientTemplate getSingleClientTemplateAccVerionNo(DBHelper dbHelper, String templateName, String clientName, String language,String version_no) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        ClientTemplate clientTemplate =  new ClientTemplate();
        //String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + CLIENT_NAME + " = '"+clientName+"' AND "+ TEMPLATE_NAME + " = '"+templateName+"' AND "+ JSON_LANGUAGE + " = '"+language+"' AND "+ TEMPLATE_VERSION + " = '"+version_no+"' ";
        String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + CLIENT_NAME + " = '"+clientName+"' AND "+ TEMPLATE_NAME + " = '"+templateName+"' AND "+ JSON_LANGUAGE + " = '"+language+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");
        Log.d("query",selectQuery);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {

                    clientTemplate.setID(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    clientTemplate.setClient_name(c.getString(c.getColumnIndex(CLIENT_NAME)));
                    clientTemplate.setTemplate_form_name_array(c.getString(c.getColumnIndex(TEMPLATE_FORM_NAME_ARRAY)));
                    clientTemplate.setTemplate_name(c.getString(c.getColumnIndex(TEMPLATE_NAME)));
                    clientTemplate.setVersion(c.getString(c.getColumnIndex(TEMPLATE_VERSION)));
                    clientTemplate.setIs_deprecated(c.getString(c.getColumnIndex(IS_DEPRECATED_TEMPLATE)));
                    clientTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
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
        return clientTemplate;
    }


    public static ClientTemplate getSingleClientTemplate(DBHelper dbHelper, String client_template_id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        ClientTemplate clientTemplate =new ClientTemplate();
        String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + CLIENT_TEMPLATE_ID + " = '"+client_template_id+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");
        Log.d("query",selectQuery);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {

                    clientTemplate.setID(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    clientTemplate.setClient_name(c.getString(c.getColumnIndex(CLIENT_NAME)));
                    clientTemplate.setTemplate_form_name_array(c.getString(c.getColumnIndex(TEMPLATE_FORM_NAME_ARRAY)));
                    clientTemplate.setTemplate_name(c.getString(c.getColumnIndex(TEMPLATE_NAME)));
                    clientTemplate.setVersion(c.getString(c.getColumnIndex(TEMPLATE_VERSION)));
                    clientTemplate.setIs_deprecated(c.getString(c.getColumnIndex(IS_DEPRECATED_TEMPLATE)));
                    clientTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
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
        return clientTemplate;
    }


    public static ClientTemplate getSingleClientTemplateByTemplateNameClientLanguage(DBHelper dbHelper, String templateName, String clientName, String language, String isDeprecatedFlag) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        ClientTemplate clientTemplate =new ClientTemplate();
        String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + CLIENT_NAME + " = '"+clientName+"' AND "+ TEMPLATE_NAME + " = '"+templateName+"' AND "+ JSON_LANGUAGE + " = '"+language+"' AND "+ IS_DEPRECATED_TEMPLATE + " = '"+isDeprecatedFlag+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");
        Log.d("query",selectQuery);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {

                    clientTemplate.setID(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    clientTemplate.setClient_name(c.getString(c.getColumnIndex(CLIENT_NAME)));
                    clientTemplate.setTemplate_form_name_array(c.getString(c.getColumnIndex(TEMPLATE_FORM_NAME_ARRAY)));
                    clientTemplate.setTemplate_name(c.getString(c.getColumnIndex(TEMPLATE_NAME)));
                    clientTemplate.setVersion(c.getString(c.getColumnIndex(TEMPLATE_VERSION)));
                    clientTemplate.setIs_deprecated(c.getString(c.getColumnIndex(IS_DEPRECATED_TEMPLATE)));
                    clientTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
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
        return clientTemplate;
    }


    public static int getClientTemplateCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE;
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

    public static int getClientTemplateID(DBHelper dbHelper,String templateName, String clientName, String language, String version) {
        int client_template_id=0;
        String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + CLIENT_NAME + " = '"+clientName+"' AND "+ TEMPLATE_NAME + " = '"+templateName+"' AND "+ JSON_LANGUAGE + " = '"+language+"' AND "+TEMPLATE_VERSION + " = '"+version+"'";

        Log.d(TAG,selectQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                client_template_id = cursor.getInt(cursor.getColumnIndex(CLIENT_TEMPLATE_ID));
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
        return client_template_id;
    }

    public static ClientTemplate getMaxVersionSingleClientTemplate(DBHelper dbHelper, String templateName, String clientName, String language) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        ClientTemplate clientTemplate =new ClientTemplate();
        String selectQuery = "SELECT  * FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + CLIENT_NAME + " = '"+clientName+"' AND "+ TEMPLATE_NAME + " = '"+templateName+"' AND "+ JSON_LANGUAGE + " = '"+language+"' ";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");
        Log.d("query",selectQuery);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {

                    clientTemplate.setID(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    clientTemplate.setClient_name(c.getString(c.getColumnIndex(CLIENT_NAME)));
                    clientTemplate.setTemplate_form_name_array(c.getString(c.getColumnIndex(TEMPLATE_FORM_NAME_ARRAY)));
                    clientTemplate.setTemplate_name(c.getString(c.getColumnIndex(TEMPLATE_NAME)));
                    clientTemplate.setVersion(c.getString(c.getColumnIndex(TEMPLATE_VERSION)));
                    clientTemplate.setIs_deprecated(c.getString(c.getColumnIndex(IS_DEPRECATED_TEMPLATE)));
                    clientTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
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
        return clientTemplate;
    }

    public static void removeClientTemplateById(DBHelper dbHelper, String clientTemplateId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_CLIENT_TEMPLATE + " WHERE " + CLIENT_TEMPLATE_ID + "= '" + clientTemplateId + "'";
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
