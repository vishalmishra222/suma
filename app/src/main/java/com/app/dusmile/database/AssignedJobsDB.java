package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.DBModel.LoginTemplate;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.utils.IOUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by suma on 17/03/17.
 */

public class AssignedJobsDB {

    private static final String TAG = "AssignedJobsDB";
    public static final String TABLE_ASSIGNED_JOBS_TABLE = "assigned_jobs_Table";
    public static final String ASSIGNED_JOBS_ID = "assigned_jobs_id";
    public static final String JOB_ID ="job_id";
    public static final String ORIGINAL_APPLICANT_JSON = "original_applicant_json";
    public static final String APPLICANT_JSON = "applicant_json";
    public static final String ASSIGNED_JOB_END_TIME = "job_end_time";
    public static final String CLIENT_TEMPLATE_ID="client_template_id";
    public static final String IS_IN_PROGRESS="is_in_progress";
    public static final String IS_SUBMIT="is_submit";
    public static final String LATLONG = "latlong";
    public static final String SUBMIT_JSON = "submit_json";
    public static final String JOB_TYPE = "job_type";
    // Insert a case into the database
    public static long addAssignedJobsEntry(AssignedJobs record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            IOUtils.appendLog( " : " + IOUtils.getCurrentTimeStamp() + record.getApplicant_json());
            ContentValues values = new ContentValues();
            values.put(JOB_ID, record.getAssigned_jobId());
            values.put(ORIGINAL_APPLICANT_JSON, record.getApplicant_json());
            values.put(APPLICANT_JSON, record.getApplicant_json());
            values.put(ASSIGNED_JOB_END_TIME, record.getJob_end_time());
            values.put(CLIENT_TEMPLATE_ID, record.getClient_template_id());
            values.put(IS_IN_PROGRESS, record.getIS_IN_PROGRESS());
            values.put(IS_SUBMIT, record.getIs_submit());
            values.put(LATLONG,record.getLatLong());
            values.put(SUBMIT_JSON,record.getSubmit_json());
            values.put(JOB_TYPE,record.getJob_type());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_ASSIGNED_JOBS_TABLE, null, values);
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
    public static int updateAssignedJobs(AssignedJobs record,DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(ASSIGNED_JOBS_ID, record.getID());
            values.put(JOB_ID, record.getAssigned_jobId());
            values.put(ORIGINAL_APPLICANT_JSON, record.getApplicant_json());
            values.put(APPLICANT_JSON, record.getApplicant_json());
            values.put(ASSIGNED_JOB_END_TIME, record.getJob_end_time());
            values.put(CLIENT_TEMPLATE_ID, record.getClient_template_id());
            values.put(IS_IN_PROGRESS, record.getIS_IN_PROGRESS());
            values.put(IS_SUBMIT, record.getIs_submit());
            values.put(LATLONG,record.getLatLong());
            values.put(SUBMIT_JSON, record.getSubmit_json());
            values.put(JOB_TYPE,record.getJob_type());
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_ASSIGNED_JOBS_TABLE, values, ASSIGNED_JOBS_ID + "= ?", new String[]{record.getID()});

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
    public static ArrayList<AssignedJobs> getAllAssignedJobs(DBHelper dbHelper) {

                ArrayList<AssignedJobs> assignedJobsMasterList = new ArrayList<AssignedJobs>();

                //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

                String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE;

                System.out.println("----------------------------------------------------------------------");

                System.out.println("----------------------------------------------------------------------");


                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery(selectQuery, null);
               try {
                if (c.moveToFirst()) {
                    do {
                        AssignedJobs assignedJobs = new AssignedJobs();
                        assignedJobs.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_ID)));
                        assignedJobs.setAssigned_jobId(c.getString(c.getColumnIndex(JOB_ID)));
                        assignedJobs.setOriginal_applicant_json(c.getString(c.getColumnIndex(ORIGINAL_APPLICANT_JSON)));
                        assignedJobs.setApplicant_json(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                        assignedJobs.setJob_end_time(c.getString(c.getColumnIndex(ASSIGNED_JOB_END_TIME)));
                        assignedJobs.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                        assignedJobs.setIS_IN_PROGRESS(c.getString(c.getColumnIndex(IS_IN_PROGRESS)));
                        assignedJobs.setIs_submit(c.getString(c.getColumnIndex(IS_SUBMIT)));
                        assignedJobs.setLatLong(c.getString(c.getColumnIndex(LATLONG)));
                        assignedJobs.setSubmit_json(c.getString(c.getColumnIndex(SUBMIT_JSON)));
                        assignedJobs.setJob_type(c.getString(c.getColumnIndex(JOB_TYPE)));

                        // adding to todo list
                        assignedJobsMasterList.add(assignedJobs);
                    } while (c.moveToNext());
                }
            }
            catch(Exception e)
            {
              e.printStackTrace();
            }
               finally {
                   if (db != null && db.isOpen()) {
                       c.close();
                       db.close();
                   }
               }
        return assignedJobsMasterList;
    }



    public static ArrayList<AssignedJobs> getAllAssignedSubmittedJobs(DBHelper dbHelper, String isSubmit) {
        ArrayList<AssignedJobs> assignedJobsMasterList = new ArrayList<AssignedJobs>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE+ " WHERE "+IS_SUBMIT+" = '"+isSubmit+"' OR "+SUBMIT_JSON+" IS NOT NULL" ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
                if (c.moveToFirst()) {
                    do {
                        AssignedJobs assignedJobs =new AssignedJobs();
                        assignedJobs.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_ID)));
                        assignedJobs.setAssigned_jobId(c.getString(c.getColumnIndex(JOB_ID)));
                        assignedJobs.setOriginal_applicant_json(c.getString(c.getColumnIndex(ORIGINAL_APPLICANT_JSON)));
                        assignedJobs.setApplicant_json(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                        assignedJobs.setJob_end_time(c.getString(c.getColumnIndex(ASSIGNED_JOB_END_TIME)));
                        assignedJobs.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                        assignedJobs.setIS_IN_PROGRESS(c.getString(c.getColumnIndex(IS_IN_PROGRESS)));
                        assignedJobs.setIs_submit(c.getString(c.getColumnIndex(IS_SUBMIT)));
                        assignedJobs.setLatLong(c.getString(c.getColumnIndex(LATLONG)));
                        assignedJobs.setSubmit_json(c.getString(c.getColumnIndex(SUBMIT_JSON)));
                        assignedJobs.setJob_type(c.getString(c.getColumnIndex(JOB_TYPE)));

                        // adding to todo list
                        assignedJobsMasterList.add(assignedJobs);
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
            return assignedJobsMasterList;
        }


    public static AssignedJobs getSingleAssignedJobs(DBHelper dbHelper, String client_template_id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        AssignedJobs assignedJobs =new AssignedJobs();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE + " WHERE " + CLIENT_TEMPLATE_ID + " = '"+client_template_id+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    assignedJobs.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_ID)));
                    assignedJobs.setAssigned_jobId(c.getString(c.getColumnIndex(JOB_ID)));
                    assignedJobs.setOriginal_applicant_json(c.getString(c.getColumnIndex(ORIGINAL_APPLICANT_JSON)));
                    assignedJobs.setApplicant_json(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                    assignedJobs.setJob_end_time(c.getString(c.getColumnIndex(ASSIGNED_JOB_END_TIME)));
                    assignedJobs.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    assignedJobs.setIS_IN_PROGRESS(c.getString(c.getColumnIndex(IS_IN_PROGRESS)));
                    assignedJobs.setIs_submit(c.getString(c.getColumnIndex(IS_SUBMIT)));
                    assignedJobs.setLatLong(c.getString(c.getColumnIndex(LATLONG)));
                    assignedJobs.setJob_type(c.getString(c.getColumnIndex(JOB_TYPE)));
                    assignedJobs.setSubmit_json(c.getString(c.getColumnIndex(SUBMIT_JSON)));
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
        return assignedJobs;
    }


    public static AssignedJobs getSingleSubmittedAssignedJob(DBHelper dbHelper, String jobId, String isSubmit) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        AssignedJobs assignedJobs =new AssignedJobs();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE + " WHERE " + JOB_ID + " = '"+jobId+"'"+ " AND "+IS_SUBMIT+" = '"+isSubmit+"'";

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    assignedJobs.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_ID)));
                    assignedJobs.setAssigned_jobId(c.getString(c.getColumnIndex(JOB_ID)));
                    assignedJobs.setOriginal_applicant_json(c.getString(c.getColumnIndex(ORIGINAL_APPLICANT_JSON)));
                    assignedJobs.setApplicant_json(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                    assignedJobs.setJob_end_time(c.getString(c.getColumnIndex(ASSIGNED_JOB_END_TIME)));
                    assignedJobs.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    assignedJobs.setIS_IN_PROGRESS(c.getString(c.getColumnIndex(IS_IN_PROGRESS)));
                    assignedJobs.setIs_submit(c.getString(c.getColumnIndex(IS_SUBMIT)));
                    assignedJobs.setLatLong(c.getString(c.getColumnIndex(LATLONG)));
                    assignedJobs.setJob_type(c.getString(c.getColumnIndex(JOB_TYPE)));
                    assignedJobs.setSubmit_json(c.getString(c.getColumnIndex(SUBMIT_JSON)));
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
        return assignedJobs;
    }


    public static int getAssignedJobsCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE;
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

    public static int getPendingJobsCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE + " WHERE "+IS_SUBMIT+" = '"+true+"' OR "+SUBMIT_JSON+" IS NOT NULL";
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

/*    public static boolean deletePendingJobsFromAssign(DBHelper dbHelper, String JOB_ID) {
        boolean result = false;
        String countQuery = "SELECT  " + JOB_ID + " FROM " + TABLE_ASSIGNED_JOBS_TABLE + " WHERE "+IS_SUBMIT+" = '"+true+"'";
        Log.d(TAG,countQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
               result = true;
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
        return result;
    }*/

    public static AssignedJobs getJobsByJobId(DBHelper dbHelper, String job_id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        AssignedJobs assignedJobs =new AssignedJobs();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE + " WHERE " + JOB_ID + " = '"+job_id+"'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {

            if (c.moveToFirst()) {
                do {
                    assignedJobs.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_ID)));
                    assignedJobs.setAssigned_jobId(c.getString(c.getColumnIndex(JOB_ID)));
                    assignedJobs.setOriginal_applicant_json(c.getString(c.getColumnIndex(ORIGINAL_APPLICANT_JSON)));
                    assignedJobs.setApplicant_json(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                    assignedJobs.setJob_end_time(c.getString(c.getColumnIndex(ASSIGNED_JOB_END_TIME)));
                    assignedJobs.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    assignedJobs.setIS_IN_PROGRESS(c.getString(c.getColumnIndex(IS_IN_PROGRESS)));
                    assignedJobs.setIs_submit(c.getString(c.getColumnIndex(IS_SUBMIT)));
                    assignedJobs.setLatLong(c.getString(c.getColumnIndex(LATLONG)));
                    assignedJobs.setJob_type(c.getString(c.getColumnIndex(JOB_TYPE)));
                    assignedJobs.setSubmit_json(c.getString(c.getColumnIndex(SUBMIT_JSON)));
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
        return assignedJobs;
    }


    public static AssignedJobs getJobsByProgress(DBHelper dbHelper, String is_in_progress) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        AssignedJobs assignedJobs = new AssignedJobs();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_JOBS_TABLE + " WHERE " + IS_IN_PROGRESS + " = '"+is_in_progress+"'"+" AND "+IS_SUBMIT+" = 'false'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {

            if (c.moveToFirst()) {
                do {

                    assignedJobs.setID(c.getString(c.getColumnIndex(ASSIGNED_JOBS_ID)));
                    assignedJobs.setAssigned_jobId(c.getString(c.getColumnIndex(JOB_ID)));
                    assignedJobs.setOriginal_applicant_json(c.getString(c.getColumnIndex(ORIGINAL_APPLICANT_JSON)));
                    assignedJobs.setApplicant_json(c.getString(c.getColumnIndex(APPLICANT_JSON)));
                    assignedJobs.setJob_end_time(c.getString(c.getColumnIndex(ASSIGNED_JOB_END_TIME)));
                    assignedJobs.setClient_template_id(c.getString(c.getColumnIndex(CLIENT_TEMPLATE_ID)));
                    assignedJobs.setIS_IN_PROGRESS(c.getString(c.getColumnIndex(IS_IN_PROGRESS)));
                    assignedJobs.setIs_submit(c.getString(c.getColumnIndex(IS_SUBMIT)));
                    assignedJobs.setLatLong(c.getString(c.getColumnIndex(LATLONG)));
                    assignedJobs.setJob_type(c.getString(c.getColumnIndex(JOB_TYPE)));
                    assignedJobs.setSubmit_json(c.getString(c.getColumnIndex(SUBMIT_JSON)));
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
        return assignedJobs;
    }

    public static void updateAllJobsStatus(DBHelper dbHelper, String is_in_progress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "UPDATE   " + TABLE_ASSIGNED_JOBS_TABLE + " set " + IS_IN_PROGRESS + " = '"+is_in_progress+"'"+" AND "+IS_SUBMIT+" = 'false'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");

        try {
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public static void updateJobsStatusByJobId(DBHelper dbHelper, String is_in_progress, String jobId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "UPDATE   " + TABLE_ASSIGNED_JOBS_TABLE + " set " + IS_IN_PROGRESS + " = '"+is_in_progress+"' WHERE "+JOB_ID+" ='"+jobId+"' ";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");

        try {
            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public static void updateApplicant(DBHelper dbHelper, String jobId, String applicant, String isSubmit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "UPDATE   " + TABLE_ASSIGNED_JOBS_TABLE + " set " + APPLICANT_JSON + " = '"+applicant+"' , "+ IS_SUBMIT + " = '"+isSubmit+"' WHERE "+JOB_ID+" ='"+jobId+"' ";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");

        try {
            Cursor c = db.rawQuery(selectQuery, null);
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

    public static void updateSubmitJson(DBHelper dbHelper, String jobId, String submit_json, String isSubmit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "UPDATE   " + TABLE_ASSIGNED_JOBS_TABLE + " set " + SUBMIT_JSON + " = '"+submit_json+"' , "+ IS_SUBMIT + " = '"+isSubmit+"' WHERE "+JOB_ID+" ='"+jobId+"' ";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");

        try {
            Cursor c = db.rawQuery(selectQuery, null);
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


    public static void removeJobByJobId(DBHelper dbHelper, String jobId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_ASSIGNED_JOBS_TABLE + " WHERE " + JOB_ID + "= '" + jobId + "'";
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


    //Update Record
    public static void updateAssignedJobsDependsOnJobId(DBHelper dbHelper,String jobId,String latLong)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "UPDATE   " + TABLE_ASSIGNED_JOBS_TABLE + " set " + LATLONG + " ='"+latLong+"'"+" WHERE "+JOB_ID+" ='"+jobId+"' ";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");

        try {
            Cursor c = db.rawQuery(selectQuery, null);
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
