package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.AssignedJobsStatus;
import com.example.sumaforms2.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 17/03/17.
 */

public class AssignedJobsStatusDB {
    private static final String TAG = "AssignedJobsStatusDB";
    public static final String TABLE_ASSIGNED_JOBS_STATUS_TABLE = "assigned_jobs_status_Table";
    public static final String ASSIGNED_JOBS_PROGRESS_ID = "assigned_jobs_progress_id";
    public static final String ASSIGNED_JOBID ="job_id";
    public static final String FORM_DATA_UPDATE_TIME = "form_data_update_time";
   // public static final String TEMPLATE_JSON_ID="template_json_id";

    // Insert a case into the database
    public static long addAssignedJobsStatusEntry(AssignedJobsStatus record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(ASSIGNED_JOBID, record.getJob_id());
            values.put(FORM_DATA_UPDATE_TIME, record.getForm_data_update_time());
           // values.put(TEMPLATE_JSON_ID, record.getJson_template_id());


            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_ASSIGNED_JOBS_STATUS_TABLE, null, values);
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
    public static int updateAssignedJobsStatus(AssignedJobsStatus record, DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;

        try
        {
            ContentValues values = new ContentValues();
            values.put(ASSIGNED_JOBS_PROGRESS_ID, record.getID());
            values.put(ASSIGNED_JOBID, record.getJob_id());
            values.put(FORM_DATA_UPDATE_TIME, record.getForm_data_update_time());
           // values.put(TEMPLATE_JSON_ID, record.getJson_template_id());
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_ASSIGNED_JOBS_STATUS_TABLE, values, ASSIGNED_JOBS_PROGRESS_ID + "= ?", new String[]{record.getID()});

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
    public static ArrayList<AssignedJobsStatus> getAllAssignedJobsStatus(DBHelper dbHelper) {
        ArrayList<AssignedJobsStatus> assignedJobsStatusMasterList = new ArrayList<AssignedJobsStatus>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_STATUS_TABLE ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    AssignedJobsStatus assignedJobsStatus = new AssignedJobsStatus();
                    assignedJobsStatus.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_PROGRESS_ID)));
                    assignedJobsStatus.setJob_id(c.getString(c.getColumnIndex(ASSIGNED_JOBID)));
                    assignedJobsStatus.setForm_data_update_time(c.getString(c.getColumnIndex(FORM_DATA_UPDATE_TIME)));
                   // assignedJobsStatus.setJson_template_id(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));

                    // adding to todo list
                    assignedJobsStatusMasterList.add(assignedJobsStatus);
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
        return assignedJobsStatusMasterList;
    }


    public static AssignedJobsStatus getSingleAssignedJobsStatus(DBHelper dbHelper, String Id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        AssignedJobsStatus assignedJobsStatus =new AssignedJobsStatus();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_STATUS_TABLE + " WHERE " + ASSIGNED_JOBID + " = '"+Id+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        try {
            if (c.moveToFirst()) {
                do {
                    assignedJobsStatus.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_PROGRESS_ID)));
                    assignedJobsStatus.setJob_id(c.getString(c.getColumnIndex(ASSIGNED_JOBID)));
                    assignedJobsStatus.setForm_data_update_time(c.getString(c.getColumnIndex(FORM_DATA_UPDATE_TIME)));
                  //  assignedJobsStatus.setJson_template_id(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));

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
        return assignedJobsStatus;
    }

    public static int getAssignedJobsStatusCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_STATUS_TABLE;
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

    public static AssignedJobsStatus getAllAssignedJobsStatusByJsonTemplateIdAnndJobId(DBHelper dbHelper, String jobId) {
        AssignedJobsStatus assignedJobsStatus =new AssignedJobsStatus();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_STATUS_TABLE + " WHERE "  + ASSIGNED_JOBID +" = '"+jobId+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    assignedJobsStatus.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_PROGRESS_ID)));
                    assignedJobsStatus.setJob_id(c.getString(c.getColumnIndex(ASSIGNED_JOBID)));
                    assignedJobsStatus.setForm_data_update_time(c.getString(c.getColumnIndex(FORM_DATA_UPDATE_TIME)));
                   // assignedJobsStatus.setJson_template_id(c.getString(c.getColumnIndex(TEMPLATE_JSON_ID)));

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
        return assignedJobsStatus;
    }

    public static void removeJobStatusByJobId(DBHelper dbHelper, String jobId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_ASSIGNED_JOBS_STATUS_TABLE + " WHERE " + ASSIGNED_JOBID + "= '" + jobId + "'";
        try {
            Cursor c = db.rawQuery(deleteQuery, null);
            if(c!=null) {
                c.moveToFirst();
                c.close();
            }
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
