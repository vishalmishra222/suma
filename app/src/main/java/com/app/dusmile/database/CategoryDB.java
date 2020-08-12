package com.app.dusmile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.app.dusmile.DBModel.Category;
import com.app.dusmile.DBModel.LoginTemplate;
import com.app.dusmile.database.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by suma on 14/03/17.
 */

public class CategoryDB {

    private static final String TAG = "CategoryDB";
    public static final String TABLE_SUB_CATEGORY = "subcategory_Table";
    public static final String SUB_CATEGORY_IS_FORM_MENU = "isFormMenu";
    public static final String SUB_CATEGORY_SEQUENCE_NO = "subcategory_sequence_no";
    public static final String TABLE_CATEGORY = "categoryTable";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String LOGIN_JSON_TABLE_ID = "login_json_template_id";

    // Insert a case into the database
    public static long addCategoryEntry(Category record, DBHelper database) {
        // Create and/or open the database for writing
        SQLiteDatabase db = database.getWritableDatabase();

        long rowID = 0;
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.

        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();

            values.put(CATEGORY_NAME, record.getCategory_name());
            values.put(LOGIN_JSON_TABLE_ID, record.getLogin_json_template_id());
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowID = db.insertOrThrow(TABLE_CATEGORY, null, values);
            Log.d(TAG, "Rows Inserted -- " + rowID);

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add case to database");
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rowID;
    }


    //Update Record
    public static int updateCategory(Category record, DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(LOGIN_JSON_TABLE_ID, record.getLogin_json_template_id());
            values.put(CATEGORY_NAME, record.getCategory_name());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            rows = db.update(TABLE_CATEGORY, values, LOGIN_JSON_TABLE_ID + "= ?" + " and " + CATEGORY_NAME + "= ?", new String[]{record.getLogin_json_template_id(), record.getCategory_name()});

            // Check if update succeeded
            if (rows != 0) {
                Log.d(TAG, "Number of rows updated - " + rows);
            }


        } catch (SQLiteException se) {
            Log.d(TAG, "Error while trying to update user");
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rows;
    }


    /*
     * getting all LoginTemplate
     * */
    public static ArrayList<Category> getAllCategories(DBHelper dbHelper) {
        ArrayList<Category> categoriesList = new ArrayList<Category>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setID(c.getString(c.getColumnIndex(CATEGORY_ID)));
                    category.setCategory_name(c.getString(c.getColumnIndex(CATEGORY_NAME)));
                    category.setLogin_json_template_id(c.getString(c.getColumnIndex(LOGIN_JSON_TABLE_ID)));
                    // adding to todo list
                    categoriesList.add(category);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                c.close();
                db.close();
            }
        }
        return categoriesList;
    }


    public static ArrayList<Category> getAllCategoriesDependsOnLoginJsonId(DBHelper dbHelper, String loginJsonId) {
        ArrayList<Category> categoriesList = new ArrayList<Category>();

        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;

        String selectQuery = "SELECT * FROM " + TABLE_SUB_CATEGORY + " ," + CategoryDB.TABLE_CATEGORY + " where " + CategoryDB.TABLE_CATEGORY + "." + CategoryDB.LOGIN_JSON_TABLE_ID + " = '" + loginJsonId + "'" + " AND " + CategoryDB.TABLE_CATEGORY + "." + CategoryDB.CATEGORY_ID + " = " + TABLE_SUB_CATEGORY + "." + CATEGORY_ID + " AND " + TABLE_SUB_CATEGORY + "." + SUB_CATEGORY_IS_FORM_MENU + " = '" + "false" + "'" + " ORDER BY " + SUB_CATEGORY_SEQUENCE_NO;

        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setID(c.getString(c.getColumnIndex(CATEGORY_ID)));
                    category.setCategory_name(c.getString(c.getColumnIndex(CATEGORY_NAME)));
                    category.setLogin_json_template_id(c.getString(c.getColumnIndex(LOGIN_JSON_TABLE_ID)));
                    // adding to todo list
                    categoriesList.add(category);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                c.close();
                db.close();
            }
        }
        return categoriesList;
    }


    public static Category getSingleCategory(DBHelper dbHelper, String Id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        Category category = new Category();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE " + CATEGORY_ID + " = '" + Id + "'";


        System.out.println("----------------------------------------------------------------------");

        System.out.println("----------------------------------------------------------------------");


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    category.setID(c.getString(c.getColumnIndex(CATEGORY_ID)));
                    category.setCategory_name(c.getString(c.getColumnIndex(CATEGORY_NAME)));
                    // adding to todo list
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                c.close();
                db.close();
            }
        }
        return category;
    }


    public static int getCategoryCount(DBHelper dbHelper, String categoryName, String login_template_json_id) {
        int cnt = 0;
        String countQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE " + CATEGORY_NAME + " = '" + categoryName + "'" + " and " + LOGIN_JSON_TABLE_ID + " = '" + login_template_json_id + "'";
        Log.d(TAG, countQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                cnt = cursor.getCount();
            }
        } catch (Exception e) {
        } finally {
            if (db != null && db.isOpen()) {
                cursor.close();
                db.close();
            }
        }
        return cnt;
    }

    public static int getCategoryID(DBHelper dbHelper, String categoryName, String login_template_json_id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        String categoryQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE " + CATEGORY_NAME + " = '" + categoryName + "'" + " and " + LOGIN_JSON_TABLE_ID + " = '" + login_template_json_id + "'";
        int id = 0;
        Log.d(TAG, categoryQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(categoryQuery, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(CATEGORY_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                cursor.close();
                db.close();
            }
        }
        return id;
    }


    public static int getCategoryIdDependsOnLoginJsonID(DBHelper dbHelper, String login_template_json_id) {
        //select tfm.* from topicFieldMap tfm inner join dss_user_audit_Topic_map atfm on tfm.topicServerID=atfm.topicServerID where atfm.topicServerID=87 and atfm.user_audit_id=1;
        String categoryQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE " + LOGIN_JSON_TABLE_ID + " = '" + login_template_json_id + "'";
        int id = 0;
        Log.d(TAG, categoryQuery);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(categoryQuery, null);
        try {

            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(CATEGORY_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                cursor.close();
                db.close();
            }
        }
        return id;
    }

    public static void deleteCategoryTable(DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_CATEGORY;
        try {
            Cursor c = db.rawQuery(deleteQuery, null);
            c.moveToFirst();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


}
