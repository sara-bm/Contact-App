package com.example.tp1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDB";
    private static final String TABLE_NAME = "users";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "TEL";
    private static final String COL_4 = "NICKNAME";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, TEL TEXT, NICKNAME TEXT)");
    }
    public void deleteAllRows() {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database
        try {
            db.execSQL("DELETE FROM "+TABLE_NAME); // Replace with your actual table name
        } catch (Exception e) {
            Log.e("Database Error", "Error deleting rows: " + e.getMessage());
        } finally {
            db.close(); // Close the database
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String name, String tel,String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, tel);
        contentValues.put(COL_4, nickname);
//        getTableSchema(TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.i("azerty","p");
        return result != -1;
    }
    public void getTableSchema(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database
        Cursor cursor = null;

        try {
            // Query to get the schema of the table
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

            // Check if the cursor contains results
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get column details
                    @SuppressLint("Range") String columnName = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String columnType = cursor.getString(cursor.getColumnIndex("type"));
                    @SuppressLint("Range") int isNotNull = cursor.getInt(cursor.getColumnIndex("notnull"));
                    @SuppressLint("Range") String defaultValue = cursor.getString(cursor.getColumnIndex("dflt_value"));
                    @SuppressLint("Range") int primaryKey = cursor.getInt(cursor.getColumnIndex("pk"));

                    // Print column information to Logcat
                    Log.d("TableSchema", "Column: " + columnName + ", Type: " + columnType +
                            ", Not Null: " + isNotNull + ", Default Value: " + defaultValue +
                            ", Primary Key: " + primaryKey);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error retrieving schema: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close(); // Close the cursor
            }
            db.close(); // Close the database
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
    public boolean updateData(String id, String name, String tel,String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, tel);
        contentValues.put(COL_4, nickname);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public boolean checkUser(String name, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // SQL query to check if the name and phone exist
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "=? AND " + COL_3 + "=?";
            cursor = db.rawQuery(query, new String[]{name, phone});

            // If the cursor has results, it means the user exists
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure the cursor is closed to avoid memory leaks
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }
}

