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

    public static final String CREATE_TABLE_PERSON = "CREATE TABLE " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME
            + " (\n" + CONTRACT_DB_TABLES.Table_Person.PERSON_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_NAME + " TEXT NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_BIRTHDATE + " DATE NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_CONTACT + " TEXT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_ATTAINMENT + " TEXT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_SPOUSE + " TEXT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_FAMILY + " INTEGER,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_VOTERS + " INTEGER,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_SHIRT_SIZE + " TEXT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_POSITION + " TEXT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID + " INTEGER NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_SYNC_STATUS + " INTEGER NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_SYNC_ACTION + " TEXT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_DATE + " DATE NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_BY + " INTEGER NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_WEB_ID + " TEXT,\n"
            + CONTRACT_DB_TABLES.Table_Person.PERSON_REMARKS + " TEXT"
            + ");";

    public static final String CREATE_TABLE_GROUP = "CREATE TABLE " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME
            + " (\n" + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID +" INTEGER NOT NULL PRIMARY KEY,\n"
            + CONTRACT_DB_TABLES.Table_GROUP.GROUP_NAME+ " TEXT NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_GROUP.GROUP_PUROK + " TEXT NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_GROUP.GROUP_BARANGAY + " TEXT NOT NULL,\n"
            + CONTRACT_DB_TABLES.Table_GROUP.GROUP_CITY + " TEXT NOT NULL"
            + ");";

   // public  static  final String CREATE_CONSTRAINT_GROUP = "ALTER TABLE " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + " ADD UNIQUE `unique_groupID` (CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID);";


    public static String DROP_TABLE_PERSON = "DROP TABLE IF EXISTS " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME;
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
        //db.execSQL(CREATE_CONSTRAINT_GROUP);
        //Log.d("GroupID Constraint", "CONSTRAINT CREATED..");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_PERSON);
        db.execSQL(DROP_TABLE_GROUP);
        onCreate(db);
    }

    public boolean addGroup(int id, String group_name, String purok, String barangay, String city , SQLiteDatabase db){
        db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID, id);
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_NAME, group_name);
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_PUROK, purok);
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_BARANGAY, barangay);
        contentValues.put(CONTRACT_DB_TABLES.Table_GROUP.GROUP_CITY, city);


        long lastInsertId = db.insert(CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME, null, contentValues);


        if(lastInsertId!=-1){
            Log.d("InsertGroup", "Group Inserted");
            return true;
        }else{
            Log.d("InsertGroup", "Group Not Inserted");
            return false;
        }
    }

    public Cursor getGroup(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + " ORDER BY " + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID + " DESC LIMIT 5", null);
        return data;
    }


    public boolean addPerson(String name, String birthday, String contact, String attainment,
                             String spouse, int family_member, int family_voters, String tshirt_size,
                             String position, int group_id, int sync_status, String sync_action,
                             String date_added, int added_by, String web_id, String remarks,
                             SQLiteDatabase db){


        db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_NAME, name);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_BIRTHDATE, birthday);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_CONTACT, contact);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_ATTAINMENT, attainment);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_SPOUSE, spouse);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_FAMILY, family_member);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_VOTERS, family_voters);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_SHIRT_SIZE, tshirt_size);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_POSITION, position);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID, group_id);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_SYNC_STATUS, sync_status);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_SYNC_ACTION, sync_action);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_DATE, date_added);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_BY, added_by);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_WEB_ID, web_id);
        contentValues.put(CONTRACT_DB_TABLES.Table_Person.PERSON_REMARKS, remarks);
        long lastInsertId = db.insert(CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME, null, contentValues);

        StringBuffer sb = new StringBuffer();
        sb.append("Name: " + name + "\n");
        sb.append("Birthday: " + birthday + "\n");
        sb.append("Contact: " + contact + "\n");
        sb.append("Attainment: " + attainment + "\n");
        sb.append("Spouse: " + spouse + "\n");
        sb.append("Family Member: " + family_member + "\n");
        sb.append("Family Voters: " + family_voters + "\n");
        sb.append("Tshirt Size: " + tshirt_size + "\n");
        sb.append("Position: " + position + "\n");
        sb.append("Group ID: " + group_id + "\n");
        sb.append("Sync Status: " + sync_status + "\n");
        sb.append("Sync Action: " + sync_action + "\n");
        sb.append("Added By: " + added_by + "\n");
        sb.append("Added date: " + date_added + "\n");
        sb.append("Web id: " + web_id + "\n");
        sb.append("Remarks: " + remarks);
        Log.d("DataInputed:", "\n" + sb.toString());
        if(lastInsertId!=-1){
            Log.d("InsertPerson", "Person Inserted");
            return true;
        }else{
            Log.d("InsertPerson", "Person Not Inserted");
            return false;
        }
       //return false;
    }

    public Cursor getPerson(int person_id, SQLiteDatabase db){

        String sql = "Select "
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " AS person_id,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_NAME + " AS person_name,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_POSITION + " As person_position,\n"
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_NAME + " AS person_group,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_CONTACT + " AS person_contact,\n"
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_PUROK + " AS person_purok,\n"
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_BARANGAY + " AS person_barangay,\n"
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_CITY + " AS person_city,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_BIRTHDATE + " AS person_bday,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_DATE + " As person_date_added,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_SPOUSE + " AS person_spouse,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_ATTAINMENT + " AS person_attainment,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_FAMILY + " As person_num_family,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_VOTERS + " As person_num_voters,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_SHIRT_SIZE + " AS person_shirt_size,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_BY + " AS person_added_by,\n"
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_SYNC_STATUS + " AS person_sync_status "
                + "FROM " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME
                + " INNER JOIN " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME
                + " ON " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID
                + " = " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID
                + " WHERE "
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " = " + person_id;

        //Log.d("OnGetPersonDetails", sql);
        Cursor data = db.rawQuery(sql, null);
        return data;
    }

    public Cursor getPersonMin(SQLiteDatabase db, int limit){
        String sql = "SELECT " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " AS person_id, "
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_NAME +" AS person_name, "
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_NAME+ " AS person_group_name, "
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_PUROK + " AS person_purok, "
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_BARANGAY + " AS person_barangay, "
                + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_CITY + " AS person_city, "
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID + " AS person_group_id"
                + " FROM " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME
                + " INNER JOIN " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME
                + " ON " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID
                +  " = " + CONTRACT_DB_TABLES.Table_GROUP.TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID + " WHERE 1 ORDER BY "
                + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "." + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " DESC LIMIT " + limit;

        Cursor data = db.rawQuery(sql, null);
        Log.d("SQL_SCRIPT", sql);
        return data;
    }

    public void closeDB(SQLiteDatabase db){
        db.close();
    }

    public SQLiteDatabase getWritableDB(){
        return this.getWritableDatabase();
    }
}
