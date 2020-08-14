package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.LoginTemplate;
import com.app.dusmile.database.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 14/03/17.
 */

public class LoginTemplateDB {
    private static final String TAG = "LoginTemplateDB";

    public static final String TABLE_LOGIN_JSON_TEMPLATE = "LoginJsonTemplate";
    public static final String LOGIN_JSON_TABLE_ID = "login_json_template_id";
    public static final String LOGIN_JSON_KEY = "jsonKey";
    public static final String LOGIN_JSON_VALUE = "jsonValue";

    public static final String JSON_LANGUAGE = "language";

    // Insert a case into the database
    public static long addLoginTemplateEntry(LoginTemplate record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(LOGIN_JSON_KEY, record.getJson_key());
            values.put(LOGIN_JSON_VALUE, record.getJson_value());
            values.put(JSON_LANGUAGE, record.getLanguage());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_LOGIN_JSON_TEMPLATE, null, values);
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
    /*public static int updateLoginTemplate(LoginTemplate record,DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(LOGIN_JSON_KEY, record.getJson_key());
            values.put(LOGIN_JSON_VALUE, record.getJson_value());
            values.put(JSON_LANGUAGE, record.getLanguage());
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_LOGIN_JSON_TEMPLATE, values, LOGIN_JSON_KEY + "= ?"+" AND "+LOGIN_JSON_KEY + "= ?", new String[]{record.getJson_key(),record.getLanguage()});

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
*/

    /*
     * getting all LoginTemplate
     * */
    /*public static ArrayList<LoginTemplate> getAllLoginTemplate(DBHelper dbHelper) {
        ArrayList<LoginTemplate> loginTemplateMasterList = new ArrayList<LoginTemplate>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN_JSON_TEMPLATE ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    LoginTemplate loginTemplate = new LoginTemplate();
                    loginTemplate.setID(c.getString(c.getColumnIndex(LOGIN_JSON_TABLE_ID)));
                    loginTemplate.setJson_key(c.getString(c.getColumnIndex(LOGIN_JSON_KEY)));
                    loginTemplate.setJson_value(c.getString(c.getColumnIndex(LOGIN_JSON_VALUE)));
                    loginTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
                    // adding to todo list
                    loginTemplateMasterList.add(loginTemplate);
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
        return loginTemplateMasterList;
    }
*/

    /*public static LoginTemplate getSingleLoginTemplate(DBHelper dbHelper, String json_key, String language) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        LoginTemplate loginTemplate =new LoginTemplate();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN_JSON_TEMPLATE + " WHERE " + LOGIN_JSON_KEY + " = '"+json_key+"'"+ "and "  + JSON_LANGUAGE + " = '"+language+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    loginTemplate.setID(c.getString(c.getColumnIndex(LOGIN_JSON_TABLE_ID)));
                    loginTemplate.setJson_key(c.getString(c.getColumnIndex(LOGIN_JSON_KEY)));
                    loginTemplate.setJson_value(c.getString(c.getColumnIndex(LOGIN_JSON_VALUE)));
                    loginTemplate.setLanguage(c.getString(c.getColumnIndex(JSON_LANGUAGE)));
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
        return loginTemplate;
    }
*/

   /* public static int getLoginTemplateCount(DBHelper dbHelper,String json_key, String language) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN_JSON_TEMPLATE + " WHERE " + LOGIN_JSON_KEY + " = '"+json_key+"'" + "and " + JSON_LANGUAGE + " = '"+language+"'";

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

*/
  /*  public static String getLoginJsontemplateImageJson (DBHelper dbHelper, String json_key, String language) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN_JSON_TEMPLATE + " WHERE " + LOGIN_JSON_KEY + " = '"+json_key+"'"  + "and " + JSON_LANGUAGE + " = '"+language+"'";
        String imagesJson = null;
        Log.d(TAG,selectQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                imagesJson = cursor.getString(cursor.getColumnIndex(LOGIN_JSON_VALUE));
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
        return imagesJson;
    }
*/

    public static int getLoginJsontemplateID (DBHelper dbHelper, String json_key, String language) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        String getLoginJsontemplateIDQuery = "SELECT  * FROM " + TABLE_LOGIN_JSON_TEMPLATE + " WHERE " + LOGIN_JSON_KEY + " = '"+json_key+"'"  + "and " + JSON_LANGUAGE + " = '"+language+"'";
        int id = 0;
        Log.d(TAG,getLoginJsontemplateIDQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(getLoginJsontemplateIDQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(LOGIN_JSON_TABLE_ID));
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
        return id;
    }

    public static void deleteLoginTemplate(DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_LOGIN_JSON_TEMPLATE;
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
