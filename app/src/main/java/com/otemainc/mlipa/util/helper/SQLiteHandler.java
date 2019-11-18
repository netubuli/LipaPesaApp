package com.otemainc.mlipa.util.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "mlipa_db";
    // Login table name
    private static final String TABLE_USER = "user";
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "lname";
    private static final String KEY_ONAME = "oname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IDNO = "idno";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACNO = "acno";
    private static final String KEY_ACTYPE = "ac_type";
    private static final String KEY_CREATED_AT = "created_at";
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " VARCHAR(25),"
                + KEY_ONAME + " VARCHAR(25)," + KEY_EMAIL + " VARCHAR(60) UNIQUE,"
                + KEY_PHONE + " VARCHAR(15)," + KEY_IDNO + " VARCHAR(25),"
                + KEY_UID + " VARCHAR(255)," + KEY_ACNO + " VARCHAR(100),"
                + KEY_ACTYPE + " VARCHAR(5)," + KEY_CREATED_AT + " VARCHAR(100))";
        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String l_name, String o_name, String email, String phone, String id_no, String uid, String acNo, String acType, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, l_name); // Last Name
        values.put(KEY_ONAME, o_name); // Last Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PHONE, phone); // Phone number
        values.put(KEY_IDNO, id_no); // Id number
        values.put(KEY_UID, uid); // User Id
        values.put(KEY_ACNO,acNo); //Account Number
        values.put(KEY_ACTYPE, acType); //Account Type
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("other", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("phone",cursor.getString(4));
            user.put("uid", cursor.getString(6));
            user.put("account",cursor.getString(7));
            user.put("created_at", cursor.getString(9));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
}
