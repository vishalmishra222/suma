package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.OfflineAssignedJobs;
import com.example.sumaforms2.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 05/06/17.
 */

public class OfflineAssignedJobsDB {

    public static final String TABLE_OFFLINE_ASSIGNED_JOBS = "offline_assigned_jobs_Table";
    public static final String OFFLINE_ASSIGNED_JOBS_ID = "id";
    public static final String OFFLINE_ASSIGNED_JOBS_JSON ="offline_assigned_json";
    private static final String TAG = "OfflineAssignedJobsDB";

    public static long addOfflineAssignedJobsEntry(OfflineAssignedJobs record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();

            values.put(OFFLINE_ASSIGNED_JOBS_JSON, record.getOffline_assigned_jobs_json());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_OFFLINE_ASSIGNED_JOBS, null, values);
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
    public static int updateOfflineAssignedJobsEntry(OfflineAssignedJobs record,DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(OFFLINE_ASSIGNED_JOBS_JSON, record.getOffline_assigned_jobs_json());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_OFFLINE_ASSIGNED_JOBS, values, OFFLINE_ASSIGNED_JOBS_ID + "= ?", new String[]{record.getID()});

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

    public static ArrayList<OfflineAssignedJobs> getAllOfflineAssignedJobsJson(DBHelper dbHelper) {
        ArrayList<OfflineAssignedJobs> offlineAssignedJobsMasterList = new ArrayList<OfflineAssignedJobs>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_OFFLINE_ASSIGNED_JOBS ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    OfflineAssignedJobs assignedJobs =new OfflineAssignedJobs();
                    assignedJobs.setID(c.getString(c.getColumnIndex(OFFLINE_ASSIGNED_JOBS_ID)));
                    assignedJobs.setOffline_assigned_jobs_json(c.getString(c.getColumnIndex(OFFLINE_ASSIGNED_JOBS_JSON)));
                    // adding to todo list
                    offlineAssignedJobsMasterList.add(assignedJobs);
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
        return offlineAssignedJobsMasterList;
    }

    public static void removeOfflineAssignedJobs(DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_OFFLINE_ASSIGNED_JOBS ;
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
