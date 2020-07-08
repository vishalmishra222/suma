package com.example.sumaforms.DBmodel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.sumaforms.helper.DBHelper;
import com.example.sumaforms.model.SubCategory;

import java.util.ArrayList;

/**
 * Created by suma on 14/03/17.
 */

public class SubCategoryDB {
    private static final String TAG = "SubCategoryDB";
    public static final String TABLE_SUB_CATEGORY = "subcategory_Table";
    public static final String SUB_CATEGORY_ID="sub_category_id";
    public static final String CATEGORY_ID="category_id";
    public static final String SUB_CATEGORY_NAME="subcategory_name";
    public static final String SUB_CATEGORY_SEQUENCE_NO="subcategory_sequence_no";
    public static final String SUB_CATEGORY_IS_FORM_MENU="isFormMenu";
    public static final String SUB_CATEGORY_ICON="icon";
    public static final String SUB_CATEGORY_ACTION= "actio";
    public static long addSubCategoryEntry(SubCategory record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(CATEGORY_ID, record.getCategory_id());
            values.put(SUB_CATEGORY_NAME, record.getSubcategory_name());
            values.put(SUB_CATEGORY_SEQUENCE_NO, record.getSequence_no());
            values.put(SUB_CATEGORY_IS_FORM_MENU, record.getIsFormMenu());
            values.put(SUB_CATEGORY_ICON,record.getIcon());
            values.put(SUB_CATEGORY_ACTION,record.getAction());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_SUB_CATEGORY, null, values);
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
    public static int updateSubCategory(SubCategory record, DBHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows=0;


        try
        {
            ContentValues values = new ContentValues();
            values.put(CATEGORY_ID, record.getCategory_id());
            values.put(SUB_CATEGORY_NAME, record.getSubcategory_name());
            values.put(SUB_CATEGORY_SEQUENCE_NO, record.getSequence_no());
            values.put(SUB_CATEGORY_IS_FORM_MENU, record.getIsFormMenu());
            values.put(SUB_CATEGORY_ICON,record.getIcon());
            values.put(SUB_CATEGORY_ACTION,record.getAction());
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_SUB_CATEGORY, values, CATEGORY_ID + "= ?"+ " and "+SUB_CATEGORY_NAME + "= ?", new String[]{record.getCategory_id(),record.getSubcategory_name()});

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
    public static ArrayList<SubCategory> getAllSubCategories(DBHelper dbHelper) {
        ArrayList<SubCategory> subcategoriesList = new ArrayList<SubCategory>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_SUB_CATEGORY ;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    SubCategory subcategory = new SubCategory();
                    subcategory.setID(c.getString(c.getColumnIndex(SUB_CATEGORY_ID)));
                    subcategory.setCategory_id(c.getString(c.getColumnIndex(CATEGORY_ID)));
                    subcategory.setSubcategory_name(c.getString(c.getColumnIndex(SUB_CATEGORY_NAME)));
                    subcategory.setSequence_no(c.getString(c.getColumnIndex(SUB_CATEGORY_SEQUENCE_NO)));
                    subcategory.setIsFormMenu(c.getString(c.getColumnIndex(SUB_CATEGORY_IS_FORM_MENU)));
                    subcategory.setIcon(c.getString(c.getColumnIndex(SUB_CATEGORY_ICON)));
                    subcategory.setAction(c.getString(c.getColumnIndex(SUB_CATEGORY_ACTION)));
                    // adding to todo list
                    subcategoriesList.add(subcategory);
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
        return subcategoriesList;
    }


    public static SubCategory getSingleSubCategory(DBHelper dbHelper, String Id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        SubCategory subcategory =new SubCategory();
        String selectQuery = "SELECT  * FROM " + TABLE_SUB_CATEGORY + " WHERE " + SUB_CATEGORY_ID + " = '"+Id+"'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {

                    subcategory.setSubcategory_name(c.getString(c.getColumnIndex(SUB_CATEGORY_NAME)));
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
        return subcategory;
    }
    public static int getSubCategoryCount(DBHelper dbHelper) {
        int cnt=0;
        String countQuery = "SELECT  * FROM " + TABLE_SUB_CATEGORY;
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

    public static ArrayList<SubCategory> getSubCategoriesDependsOnCategory(DBHelper dbHelper, String loginJsonTemplateId) {
        ArrayList<SubCategory> subcategoriesList = new ArrayList<SubCategory>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT * FROM "+TABLE_SUB_CATEGORY  +" ," +CategoryDB.TABLE_CATEGORY+" where "+ CategoryDB.TABLE_CATEGORY+"."+CategoryDB.LOGIN_JSON_TABLE_ID+" = '"+loginJsonTemplateId+"'"+" AND "+CategoryDB.TABLE_CATEGORY+"."+CategoryDB.CATEGORY_ID+" = "+TABLE_SUB_CATEGORY+"."+CATEGORY_ID+ " ORDER BY "+CATEGORY_ID;
        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    SubCategory subcategory = new SubCategory();
                    subcategory.setID(c.getString(c.getColumnIndex(SUB_CATEGORY_ID)));
                    subcategory.setCategory_id(c.getString(c.getColumnIndex(CATEGORY_ID)));
                    subcategory.setSubcategory_name(c.getString(c.getColumnIndex(SUB_CATEGORY_NAME)));
                    subcategory.setSequence_no(c.getString(c.getColumnIndex(SUB_CATEGORY_SEQUENCE_NO)));
                    subcategory.setIsFormMenu(c.getString(c.getColumnIndex(SUB_CATEGORY_IS_FORM_MENU)));
                    subcategory.setIcon(c.getString(c.getColumnIndex(SUB_CATEGORY_ICON)));
                    subcategory.setAction(c.getString(c.getColumnIndex(SUB_CATEGORY_ACTION)));
                    // adding to todo list
                    subcategoriesList.add(subcategory);
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
        return subcategoriesList;
    }

    public static boolean checkSubCategoryPresentOrNot(DBHelper dbHelper,String menuName) {
        boolean isSubCategoryAvailable = false;
        int cnt = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_SUB_CATEGORY+" WHERE " + SUB_CATEGORY_NAME + " = '"+menuName+"'";
        Log.d(TAG,selectQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isSubCategoryAvailable = true;
                }
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

        return isSubCategoryAvailable;
    }

    public static ArrayList<SubCategory> getSubCategoriesDependsOnIsMenuFlag(DBHelper dbHelper, String loginJsonTemplateId, String isFormMenu) {
        ArrayList<SubCategory> subcategoriesList = new ArrayList<SubCategory>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        // String selectQuery = "SELECT * FROM "+TABLE_SUB_CATEGORY  +" where "+SubCategoryDB.SUB_CATEGORY_ID+" = '"+loginJsonTemplateId+"'"+" AND "+SUB_CATEGORY_IS_FORM_MENU +" = '"+isFormMenu+"'";
        //String selectQuery = "SELECT * FROM "+TABLE_SUB_CATEGORY  +" ," +CategoryDB.TABLE_CATEGORY+" where "+ CategoryDB.TABLE_CATEGORY+"."+CategoryDB.LOGIN_JSON_TABLE_ID+" = '"+loginJsonTemplateId+"'"+" AND "+CategoryDB.TABLE_CATEGORY+"."+CategoryDB.CATEGORY_ID+" = "+TABLE_SUB_CATEGORY+"."+CATEGORY_ID+ " AND "+TABLE_SUB_CATEGORY+"."+SUB_CATEGORY_IS_FORM_MENU +" = '"+isFormMenu+"'" + " ORDER BY "+CATEGORY_ID;
        String selectQuery = "SELECT * FROM "+TABLE_SUB_CATEGORY  +" ," +CategoryDB.TABLE_CATEGORY+" where "+ CategoryDB.TABLE_CATEGORY+"."+CategoryDB.LOGIN_JSON_TABLE_ID+" = '"+loginJsonTemplateId+"'"+" AND "+CategoryDB.TABLE_CATEGORY+"."+CategoryDB.CATEGORY_ID+" = "+TABLE_SUB_CATEGORY+"."+CATEGORY_ID+ " AND "+TABLE_SUB_CATEGORY+"."+SUB_CATEGORY_IS_FORM_MENU +" = '"+isFormMenu+"'" + " ORDER BY "+SUB_CATEGORY_SEQUENCE_NO;
        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    SubCategory subcategory = new SubCategory();
                    subcategory.setID(c.getString(c.getColumnIndex(SUB_CATEGORY_ID)));
                    subcategory.setCategory_id(c.getString(c.getColumnIndex(CATEGORY_ID)));
                    subcategory.setSubcategory_name(c.getString(c.getColumnIndex(SUB_CATEGORY_NAME)));
                    subcategory.setSequence_no(c.getString(c.getColumnIndex(SUB_CATEGORY_SEQUENCE_NO)));
                    subcategory.setIsFormMenu(c.getString(c.getColumnIndex(SUB_CATEGORY_IS_FORM_MENU)));
                    subcategory.setIcon(c.getString(c.getColumnIndex(SUB_CATEGORY_ICON)));
                    subcategory.setAction(c.getString(c.getColumnIndex(SUB_CATEGORY_ACTION)));
                    // adding to todo list
                    subcategoriesList.add(subcategory);
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
        return subcategoriesList;
    }


    public static ArrayList<SubCategory> getSubCategoriesDependsOnCategoryIdAndIsMenuFlag(DBHelper dbHelper, String categoryId,String isFormMenu) {
        ArrayList<SubCategory> subcategoriesList = new ArrayList<SubCategory>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT * FROM "+TABLE_SUB_CATEGORY  +" where "+CATEGORY_ID +" = '"+categoryId+"'" +" AND "+SUB_CATEGORY_IS_FORM_MENU+" = '"+isFormMenu+"'"+" AND "+SUB_CATEGORY_NAME+"<> 'Upload'"+" AND "+SUB_CATEGORY_NAME+" <> 'Pending'"+ " ORDER BY "+CATEGORY_ID;
        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    SubCategory subcategory = new SubCategory();
                    subcategory.setID(c.getString(c.getColumnIndex(SUB_CATEGORY_ID)));
                    subcategory.setCategory_id(c.getString(c.getColumnIndex(CATEGORY_ID)));
                    subcategory.setSubcategory_name(c.getString(c.getColumnIndex(SUB_CATEGORY_NAME)));
                    subcategory.setSequence_no(c.getString(c.getColumnIndex(SUB_CATEGORY_SEQUENCE_NO)));
                    subcategory.setIsFormMenu(c.getString(c.getColumnIndex(SUB_CATEGORY_IS_FORM_MENU)));
                    subcategory.setIcon(c.getString(c.getColumnIndex(SUB_CATEGORY_ICON)));
                    subcategory.setAction(c.getString(c.getColumnIndex(SUB_CATEGORY_ACTION)));
                    // adding to todo list
                    /*if(!(subcategory.getSubcategory_name().toString().equals("Available Jobs"))) {*/
                    subcategoriesList.add(subcategory);
                    //}//netra
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
        return subcategoriesList;
    }

    public static void deleteMenusFromDB(DBHelper dbHelper, String categoryId, String ismenuFlag) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_SUB_CATEGORY + " WHERE " + CATEGORY_ID + "= '" + categoryId + "'"+ " AND "+SUB_CATEGORY_IS_FORM_MENU+ "= '" + ismenuFlag + "'";
        try {
            db.execSQL(deleteQuery);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public static void deleteStaticMenusFromDB(DBHelper dbHelper, String categoryId,String staticMenu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_SUB_CATEGORY + " WHERE " + CATEGORY_ID + "= '" + categoryId + "'"+ " AND "+SUB_CATEGORY_NAME+ "= '" + staticMenu + "'";
        try {
            db.execSQL(deleteQuery);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    public static void deleteSubCategoryTable(DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_SUB_CATEGORY;
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
