package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.TemplateJson;
import com.example.sumaforms2.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 14/03/17.
 */

public class TemplateJsonDB {
    private static final String TAG = "TemplateJsonDB";
    public static final String TABLE_TEMPLATE_JSON = "template_json_Table";
    public static final String TEMPLATE_JSON_ID="template_json_id";
    public static final String TEMPLATE_JSON_FORM_NAME="form_name";
    public static final String CLIENT_TEMPLATE_ID="client_template_id";
    public static final String TEMPLATE_JSON_FORM_FIELD_JSON="field_json";
    public static final String IS_TABLE_EXISTS="istable_exists";
    public static final String CONTROLLER_NAME="controller_name";
    public static final String MANDATORY_FIELD_KEY_ARRAY="mandatory_field_key_array";
    public static final String OTHER_FIELD_KEY_ARRAY="other_field_key_array";

    // Insert a case into the database
    public static long addTemplateJsonEntry(TemplateJson record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(TEMPLATE_JSON_ID, record.getID());
            values.put(TEMPLATE_JSON_FORM_NAME, record.getForm_name());
            values.put(TEMPLATE_JSON_FORM_FIELD_JSON, record.getField_json());
            values.put(CLIENT_TEMPLATE_ID, record.getClient_template_id());
            values.put(IS_TABLE_EXISTS, record.getIs_table_exists());
            values.put(CONTROLLER_NAME, record.getController_name());
            values.put(MANDATORY_FIELD_KEY_ARRAY, record.getMandatory_field_keys());
            values.put(OTHER_FIELD_KEY_ARRAY, record.getOther_field_keys());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_TEMPLATE_JSON, null, values);
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
    public static int updateTemplateJson(TemplateJson record, DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(TEMPLATE_JSON_FORM_NAME, record.getForm_name());
            values.put(TEMPLATE_JSON_FORM_FIELD_JSON, record.getField_json());
            values.put(IS_TABLE_EXISTS, record.getIs_table_exists());
            values.put(CLIENT_TEMPLATE_ID, record.getClient_template_id());
            values.put(CONTROLLER_NAME, record.getController_name());
            values.put(MANDATORY_FIELD_KEY_ARRAY, record.getMandatory_field_keys());
            values.put(OTHER_FIELD_KEY_ARRAY, record.getOther_field_keys());
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_TEMPLATE_JSON, values, TEMPLATE_JSON_ID + "= ?", new String[]{record.getID()});

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
    public static ArrayList<TemplateJson> getAllTemplateJson(DBHelper dbHelper) {
        ArrayList<TemplateJson> templateJsonMasterList = new ArrayList<TemplateJson>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_TEMPLATE_JSON ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

         try {
             if (c.moveToFirst()) {
                 do {
                     TemplateJson templateJson = new TemplateJson();
                     templateJson.setID(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));
                     templateJson.setField_json(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_FIELD_JSON)));
                     templateJson.setController_name(c.getString(c.getColumnIndex(CONTROLLER_NAME)));
                     templateJson.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                     templateJson.setIs_table_exists(c.getString(c.getColumnIndex(IS_TABLE_EXISTS)));
                     templateJson.setMandatory_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                     templateJson.setOther_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                     // adding to todo list
                     templateJsonMasterList.add(templateJson);
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
        return templateJsonMasterList;
    }

    public static ArrayList<TemplateJson> getAllTemplateJsonBtClientTemplateId(DBHelper dbHelper,String client_template_id) {
        ArrayList<TemplateJson> templateJsonMasterList = new ArrayList<TemplateJson>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_TEMPLATE_JSON +" WHERE "+CLIENT_TEMPLATE_ID +" = '"+client_template_id+"'";

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    TemplateJson templateJson = new TemplateJson();
                    templateJson.setID(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));
                    templateJson.setForm_name(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_NAME)));
                    templateJson.setField_json(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_FIELD_JSON)));
                    templateJson.setController_name(c.getString(c.getColumnIndex(CONTROLLER_NAME)));
                    templateJson.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    templateJson.setIs_table_exists(c.getString(c.getColumnIndex(IS_TABLE_EXISTS)));
                    templateJson.setMandatory_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                    templateJson.setOther_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                    // adding to todo list
                    templateJsonMasterList.add(templateJson);
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
        return templateJsonMasterList;
    }
    public static TemplateJson getSingleTemplateJson(DBHelper dbHelper, String client_template_id,String formName) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        TemplateJson templateJson=new TemplateJson();
        String selectQuery = "SELECT  * FROM " + TABLE_TEMPLATE_JSON + " WHERE " + CLIENT_TEMPLATE_ID + " = '"+client_template_id+"' AND "+TEMPLATE_JSON_FORM_NAME + " = '"+formName+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    templateJson.setID(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));
                    templateJson.setField_json(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_FIELD_JSON)));
                    templateJson.setController_name(c.getString(c.getColumnIndex(CONTROLLER_NAME)));
                    templateJson.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    templateJson.setIs_table_exists(c.getString(c.getColumnIndex(IS_TABLE_EXISTS)));
                    templateJson.setMandatory_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                    templateJson.setOther_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                    templateJson.setForm_name(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_NAME)));
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
        return templateJson;
    }

    public static int getTemplateJsonCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_TEMPLATE_JSON;
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

    public static String getTemplateJsonID(TemplateJson templateJson, DBHelper dbHelper) {
        String selectQuery = "SELECT  * FROM " + TABLE_TEMPLATE_JSON + " WHERE " + CLIENT_TEMPLATE_ID + " = '"+templateJson.getClient_template_id()+"' AND "+TEMPLATE_JSON_FORM_NAME + " = '"+templateJson.getForm_name()+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        String templateJsonID = null;
        try {

            if (c.moveToFirst()) {
                do {
                    templateJsonID = c.getString(c.getColumnIndex(TEMPLATE_JSON_ID));

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

        return templateJsonID;
    }


    public static TemplateJson getTemplateByClientTemplateId(DBHelper dbHelper, String client_template_id,int formPosition) {
        TemplateJson templateJson=new TemplateJson();

        String selectQuery = "SELECT  * FROM " + TABLE_TEMPLATE_JSON + " WHERE " + CLIENT_TEMPLATE_ID + " = '"+client_template_id+"' LIMIT "+formPosition+" , 1";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

       try {
           if (c.moveToFirst()) {
               do {

                   templateJson.setID(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));
                   templateJson.setField_json(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_FIELD_JSON)));
                   templateJson.setController_name(c.getString(c.getColumnIndex(CONTROLLER_NAME)));
                   templateJson.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                   templateJson.setIs_table_exists(c.getString(c.getColumnIndex(IS_TABLE_EXISTS)));
                   templateJson.setMandatory_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                   templateJson.setOther_field_keys(c.getString(c.getColumnIndex(MANDATORY_FIELD_KEY_ARRAY)));
                   templateJson.setForm_name(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_NAME)));
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
        return templateJson;
    }



    public static ArrayList<String> getAllFormNamesDependsOnClientTemplateID(DBHelper dbHelper, String client_template_id) {
        ArrayList<String> templateFormNameList = new ArrayList<String>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT "+TEMPLATE_JSON_FORM_NAME+" FROM " + TABLE_TEMPLATE_JSON+ " WHERE "+ CLIENT_TEMPLATE_ID+" = "+client_template_id;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    templateFormNameList.add(c.getString(c.getColumnIndex(TEMPLATE_JSON_FORM_NAME)));
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
        return templateFormNameList;
    }

    public static void removeTemplateJsonByJsonTemplateId(DBHelper dbHelper, String jsonTemplateId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_TEMPLATE_JSON + " WHERE " + TEMPLATE_JSON_ID + "= '" + jsonTemplateId + "'";
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
