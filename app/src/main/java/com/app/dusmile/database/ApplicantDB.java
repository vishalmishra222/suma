package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.Applicant;
import com.app.dusmile.database.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 15/03/17.
 */

public class ApplicantDB {
    private static final String TAG = "ApplicantDB";
    public static final String TABLE_APPLICANT_JSON = "applicant_json_table";
    public static final String APPLICANT_JSON_ID="id";
    public static final String APPLICANT_JSON = "applicant_json";

    public static long addApplicantEntry(Applicant record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(APPLICANT_JSON_ID, record.getID());
            values.put(APPLICANT_JSON, record.getJson());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_APPLICANT_JSON, null, values);
            Log.d(TAG,"Rows Inserted -- "+rowID);

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add case to database");
        }
        return rowID;
    }


    //Update Record
    public static int updateApplicant(Applicant record, DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(APPLICANT_JSON, record.getJson());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_APPLICANT_JSON, values, APPLICANT_JSON_ID + "= ?", new String[]{record.getID()});

            // Check if update succeeded
            if (rows != 0)
            {
                Log.d(TAG, "Number of rows updated - "+rows);
            }


        }catch (SQLiteException se)
        {
            Log.d(TAG, "Error while trying to update user");
        }
        return rows;
    }


    /*
       * getting all LoginTemplate
       * */
    public static ArrayList<Applicant> getAllApplicant(DBHelper dbHelper) {
        ArrayList<Applicant> applicantList = new ArrayList<Applicant>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_APPLICANT_JSON ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {
                Applicant applicant =new Applicant();
                applicant.setID(c.getString(c.getColumnIndex(APPLICANT_JSON_ID)));
                applicant.setJson(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                // adding to todo list
                applicantList.add(applicant);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return applicantList;
    }


    public static Applicant getSingleApplicant(DBHelper dbHelper, String Id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        Applicant applicant =new Applicant();
        String selectQuery = "SELECT  * FROM " + TABLE_APPLICANT_JSON + " WHERE " + APPLICANT_JSON_ID + " = '"+Id+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {
                applicant.setID(c.getString(c.getColumnIndex(APPLICANT_JSON_ID)));
                applicant.setJson(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                // adding to todo list
            } while (c.moveToNext());
        }

        return applicant;
    }
    public static int getApplicantCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_APPLICANT_JSON;
        Log.d(TAG,countQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if( cursor != null && cursor.moveToFirst() )
        {
            cnt = cursor.getCount();
            cursor.close();
            db.close();

        }
        return cnt;
    }

}
