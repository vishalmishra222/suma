package com.example.sumaforms2;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=2;

    public static final String DATABASE_NAME="db_dusmile";


    public static DBHelper sInstance;

    public static final String TAG ="DBHelper" ;

    public static final String DB_FULL_PATH = "/data/data/com.app.dusmile/databases/Dusmile";

    //common
    public static final String JSON_LANGUAGE = "language";


    //LoginTemplateTable
    public static final String TABLE_LOGIN_JSON_TEMPLATE = "LoginJsonTemplate";
    public static final String LOGIN_JSON_TABLE_ID = "login_json_template_id";
    public static final String LOGIN_JSON_KEY = "jsonKey";
    public static final String LOGIN_JSON_VALUE = "jsonValue";



    //CategoryTable
    public static final String TABLE_CATEGORY = "categoryTable";
    public static final String CATEGORY_ID="category_id";
    public static final String CATEGORY_NAME="category_name";


    //SubCategoryTable
    public static final String TABLE_SUB_CATEGORY = "subcategory_Table";
    public static final String SUB_CATEGORY_ID="sub_category_id";
    public static final String SUB_CATEGORY_NAME="subcategory_name";
    public static final String SUB_CATEGORY_SEQUENCE_NO="subcategory_sequence_no";
    public static final String SUB_CATEGORY_IS_FORM_MENU="isFormMenu";
    public static final String SUB_CATEGORY_ICON = "icon";
    public static final String SUB_CATEGORY_ACTION = "actio";

    //ClientTemplateTable
    public static final String TABLE_CLIENT_TEMPLATE = "client_template_Table";
    public static final String CLIENT_TEMPLATE_ID="client_template_id";
    public static final String CLIENT_NAME="client_name";
    public static final String TEMPLATE_NAME="template_name";
    public static final String TEMPLATE_FORM_NAME_ARRAY="form_name_array";
    public static final String TEMPLATE_VERSION="version";
    public static final String IS_DEPRECATED_TEMPLATE="is_deprecated";


    //TEMPLATEJSONTable
    public static final String TABLE_TEMPLATE_JSON = "template_json_Table";
    public static final String TEMPLATE_JSON_ID="template_json_id";
    public static final String TEMPLATE_JSON_FORM_NAME="form_name";
    public static final String TEMPLATE_JSON_FORM_FIELD_JSON="field_json";
    public static final String IS_TABLE_EXISTS="istable_exists";
    public static final String CONTROLLER_NAME="controller_name";
    public static final String MANDATORY_FIELD_KEY_ARRAY="mandatory_field_key_array";
    public static final String OTHER_FIELD_KEY_ARRAY="other_field_key_array";



    //DynamicfieldTable
    public static final String TABLE_DYNAMIC_FIELD_TABLE = "dynamic_table_fields_Table";
    public static final String DYNAMIC_FIELD_TABLE_ID="dynamic_field_table_id";
    public static final String DYNAMIC_FIELD_TABLE_NAME="table_name";
    public static final String DYNAMIC_FIELD_TABLE_HEADERS_ARRAY="table_headers_array";
    public static final String TABLE_KEY_ARRAY="table_keys_array";
    public static final String MANDATORY_TABLE_FIELDS="mandatory_fields";
    public static final String MANDATORY_TABLE_HEADERS="mandatory_table_headers";
    public static final String CLEAR_SUBPROCESS_FIELDS="clear_subprocess_fields";


  /*  //ApplicationTable
    public static final String TABLE_APPLICANT_JSON = "applicant_json_table";
    public static final String APPLICANT_JSON_ID="id";
    public static final String APPLICANT_JSON = "applicant_json";*/

    //Assigned Jobs
    public static final String TABLE_ASSIGNED_JOBS_TABLE = "assigned_jobs_Table";
    public static final String ASSIGNED_JOBS_ID = "assigned_jobs_id";
    public static final String JOB_ID ="job_id";
    public static final String ORIGINAL_APPLICANT_JSON = "original_applicant_json";
    public static final String APPLICANT_JSON = "applicant_json";
    public static final String ASSIGNED_JOB_END_TIME = "job_end_time";
    public static final String IS_IN_PROGRESS = "is_in_progress";
    public static final String IS_SUBMIT="is_submit";
    public static final String LATLONG = "latlong";
    public static final String SUBMIT_JSON = "submit_json";
    public static final String JOB_TYPE = "job_type";


    //Assigned Jobs Status
    public static final String TABLE_ASSIGNED_JOBS_STATUS_TABLE = "assigned_jobs_status_Table";
    public static final String ASSIGNED_JOBS_PROGRESS_ID = "assigned_jobs_progress_id";
    public static final String ASSIGNED_JOBID ="job_id";
    public static final String FORM_DATA_UPDATE_TIME = "form_data_update_time";

    //Offline Assigned Jobs List
    public static final String TABLE_OFFLINE_ASSIGNED_JOBS = "offline_assigned_jobs_Table";
    public static final String OFFLINE_ASSIGNED_JOBS_ID = "id";
    public static final String OFFLINE_ASSIGNED_JOBS_JSON ="offline_assigned_json";



    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context);
        }
        return sInstance;
    }

    /**
     * Constructor should be public to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    public DBHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null,DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them

            db.execSQL("ALTER TABLE "+TABLE_ASSIGNED_JOBS_TABLE+" ADD COLUMN "+JOB_TYPE+" TEXT");
            onCreate(db);
        }
    }
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    public void DropDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGIN_JSON_TEMPLATE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SUB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CLIENT_TEMPLATE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TEMPLATE_JSON);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DYNAMIC_FIELD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ASSIGNED_JOBS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ASSIGNED_JOBS_STATUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_OFFLINE_ASSIGNED_JOBS);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TEMPLATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN_JSON_TEMPLATE +
                "(" +
                LOGIN_JSON_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOGIN_JSON_KEY + " TEXT," +
                LOGIN_JSON_VALUE + " TEXT," +
                JSON_LANGUAGE + " TEXT " +
                ")";

        String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY +
                "(" +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                CATEGORY_NAME + " TEXT ," +
                LOGIN_JSON_TABLE_ID + " INTEGER " +
                ")";


        String CREATE_TABLE_SUB_CATEGORY = "CREATE TABLE  IF NOT EXISTS " + TABLE_SUB_CATEGORY +
                "(" +
                SUB_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                CATEGORY_ID + " INTEGER ," +
                SUB_CATEGORY_NAME + " TEXT ," +
                SUB_CATEGORY_SEQUENCE_NO+ " TEXT ," +
                SUB_CATEGORY_IS_FORM_MENU+ " TEXT ," +
                SUB_CATEGORY_ICON+ " TEXT ," +
                SUB_CATEGORY_ACTION+ " TEXT " +
                ")";


        String CREATE_TABLE_CLIENT_TEMPLATE = "CREATE TABLE  IF NOT EXISTS " + TABLE_CLIENT_TEMPLATE +
                "(" +
                CLIENT_TEMPLATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                CLIENT_NAME+ " TEXT ," +
                TEMPLATE_NAME + " TEXT ," +
                TEMPLATE_FORM_NAME_ARRAY + " TEXT ," +
                TEMPLATE_VERSION + " TEXT ," +
                JSON_LANGUAGE + " TEXT ," +
                IS_DEPRECATED_TEMPLATE + " TEXT " +
                ")";

        String CREATE_TABLE_TEMPLATE_JSON = "CREATE TABLE  IF NOT EXISTS " + TABLE_TEMPLATE_JSON +
                "(" +
                TEMPLATE_JSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                TEMPLATE_JSON_FORM_NAME + " TEXT ," +
                TEMPLATE_JSON_FORM_FIELD_JSON + " TEXT," +
                CLIENT_TEMPLATE_ID + " INTEGER ," +
                IS_TABLE_EXISTS + " TEXT ," +
                CONTROLLER_NAME + " TEXT ," +
                MANDATORY_FIELD_KEY_ARRAY + " TEXT ," +
                OTHER_FIELD_KEY_ARRAY + " TEXT " +
                ")";

        String CREATE_TABLE_DYNAMIC_FIELD_TABLE = "CREATE TABLE  IF NOT EXISTS " + TABLE_DYNAMIC_FIELD_TABLE +
                "(" +
                DYNAMIC_FIELD_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DYNAMIC_FIELD_TABLE_NAME + " TEXT," +
                DYNAMIC_FIELD_TABLE_HEADERS_ARRAY + " TEXT ," +
                TABLE_KEY_ARRAY + " TEXT ," +
                MANDATORY_TABLE_FIELDS + " TEXT ," +
                MANDATORY_TABLE_HEADERS + " TEXT ," +
                CLEAR_SUBPROCESS_FIELDS + " TEXT ," +
                TEMPLATE_JSON_ID + " INTEGER " +
                ")";

        String CREATE_TABLE_ASSIGNED_JOBS = "CREATE TABLE  IF NOT EXISTS " + TABLE_ASSIGNED_JOBS_TABLE +
                "(" +
                ASSIGNED_JOBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                JOB_ID + " TEXT," +
                ORIGINAL_APPLICANT_JSON + " TEXT ," +
                APPLICANT_JSON + " TEXT ," +
                ASSIGNED_JOB_END_TIME + " TEXT ," +
                JOB_TYPE + " TEXT ," +
                CLIENT_TEMPLATE_ID + " INTEGER ," +
                IS_IN_PROGRESS + " TEXT , " +
                IS_SUBMIT + " TEXT, " +
                LATLONG + " TEXT, " +
                SUBMIT_JSON + " TEXT " +
                ")";

        String CREATE_TABLE_ASSIGNED_JOBS_STATUS = "CREATE TABLE  IF NOT EXISTS " + TABLE_ASSIGNED_JOBS_STATUS_TABLE +
                "(" +
                ASSIGNED_JOBS_PROGRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ASSIGNED_JOBID + " TEXT," +
                FORM_DATA_UPDATE_TIME + " TEXT " +
                ")";

        String CREATE_TABLE_OFFLINE_ASSIGNED_JOBS = "CREATE TABLE  IF NOT EXISTS " + TABLE_OFFLINE_ASSIGNED_JOBS +
                "(" +
                OFFLINE_ASSIGNED_JOBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OFFLINE_ASSIGNED_JOBS_JSON + " TEXT" +
                ")";

        db.execSQL(CREATE_LOGIN_TEMPLATE_TABLE);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_SUB_CATEGORY);
        db.execSQL(CREATE_TABLE_CLIENT_TEMPLATE);
        db.execSQL(CREATE_TABLE_TEMPLATE_JSON);
        db.execSQL(CREATE_TABLE_DYNAMIC_FIELD_TABLE);
        db.execSQL(CREATE_TABLE_ASSIGNED_JOBS);
        db.execSQL(CREATE_TABLE_ASSIGNED_JOBS_STATUS);
        db.execSQL(CREATE_TABLE_OFFLINE_ASSIGNED_JOBS);

    }





}
