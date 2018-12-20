package com.livelihood.voidmain.livelihood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_Helper extends SQLiteOpenHelper {

    public static final String DB_NAME = "db_livelihood";
    public static final int DB_VERSION = 1;

    public static final String CREATE_TABLE_PERSON = "CREATE TABLE " + CONTRACT_DB_TABLES.Table_Person.TABLE_NAME
            + " (\n" + CONTRACT_DB_TABLES.Table_Person.PERSON_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_NAME + " TEXT NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_Person.GROUP_ID + " INTEGER NOT NULL);";

    public static final String CREATE_TABLE_GROUP = "CREATE TABLE " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME
            + " (\n" + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID +" INTEGER NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_GROUP.GROUP_NAME+ " TEXT NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ADDRESS + " TEXT NOT NULL\n"
            + ");";


    public static String DROP_TABLE_PERSON = "DROP TABLE IF EXISTS " + CONTRACT_DB_TABLES.Table_Person.TABLE_NAME;
    public static String DROP_TABLE_GROUP = "DROP TABLE IF EXISTS " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME;

    public DB_Helper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        Log.d("Database Creation", "Database Created..");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GROUP);
        Log.d("Group_table Created", "Table created..");
        db.execSQL(CREATE_TABLE_PERSON);
        Log.d("Person_table Created", "Table created..");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_PERSON);
        db.execSQL(DROP_TABLE_GROUP);
        onCreate(db);
    }

    public boolean addGroup(int id, String group_name, String group_address , SQLiteDatabase db){
        db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID, id);
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_NAME, group_name);
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_ADDRESS, group_address);


        long lastInsertId = db.insert(CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME, null, contentValues);
        Log.d("Insert on Group Success", "Person Inserted");

        if(lastInsertId!=-1){
            return true;
        }else{
            return false;
        }
    }

    public Cursor getGroup(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + " ORDER BY " + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID + " DESC LIMIT 5", null);
        return data;
    }


    public boolean addPerson(String name, int group_id, SQLiteDatabase db){
        db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_NAME, name);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.GROUP_ID, group_id);
        long lastInsertId = db.insert(CONTRACT_DB_TABLES.Table_Person.TABLE_NAME, null, contentValues);
        Log.d("Insert Person Success", "Person Inserted");

        if(lastInsertId!=-1){
            return true;
        }else{
            return false;
        }
    }

    public Cursor getPerson(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT "+ CONTRACT_DB_TABLES.Table_Person.PERSON_ID
                + ", " + CONTRACT_DB_TABLES.Table_Person.PERSON_NAME
                + ", " + CONTRACT_DB_TABLES.Table_Person.GROUP_ID
                + ", " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ADDRESS
                + " FROM " + CONTRACT_DB_TABLES.Table_Person.TABLE_NAME + " INNER JOIN " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME
                + " ON " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID
                + "=" + CONTRACT_DB_TABLES.Table_Person.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.GROUP_ID + " ORDER BY " + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " DESC LIMIT 5", null);

        /*Cursor data = db.rawQuery("SELECT "+ CONTRACT_DB_TABLES.Table_Person.PERSON_ID
                + ", " + CONTRACT_DB_TABLES.Table_Person.PERSON_NAME
                + ", " + CONTRACT_DB_TABLES.Table_Person.PERSON_ADDRESS
                + ", " + CONTRACT_DB_TABLES.Table_Person.GROUP_ID
                + " FROM " + CONTRACT_DB_TABLES.Table_Person.TABLE_NAME
                + " ORDER BY " + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " DESC LIMIT 5", null);*/
        return data;
    }
}
