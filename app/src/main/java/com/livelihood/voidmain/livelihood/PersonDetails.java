package com.livelihood.voidmain.livelihood;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PersonDetails extends AppCompatActivity {

    TextView position, name, groupName, contact, address, birthday, membershipDate, spouse, education,
    numOfFam, numOfVoters, tShirtSize, addedBy, syncStatus;
    private int person_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        TextView tv = (TextView) findViewById(R.id.temp_text);
        Bundle b = null;
        if(savedInstanceState==null){
            b = getIntent().getExtras();
            if(b==null){
                tv.setText("No Data Arrived");
            }else{
                 person_id = b.getInt("com.klevie.livelihood.PERSON_ID");
            }
        }else{
            person_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.PERSON_ID");
            //tv.setText(person_id + "");
        }



        initializeText();

    }

    private void initializeText() {

        name = (TextView) findViewById(R.id.details_textView_name);
        position = (TextView) findViewById(R.id.details_textView_Position);
        groupName = (TextView) findViewById(R.id.details_textView_Group);
        contact = (TextView) findViewById(R.id.details_textView_Contact);
        address = (TextView) findViewById(R.id.details_textView_Address);
        birthday = (TextView) findViewById(R.id.details_textView_BirthDay);
        membershipDate = (TextView) findViewById(R.id.details_textView_Membership);
        spouse = (TextView) findViewById(R.id.details_textView_Spouse);
        education = (TextView) findViewById(R.id.details_textView_Education);
        numOfFam = (TextView) findViewById(R.id.details_textView_numFam);
        numOfVoters = (TextView) findViewById(R.id.details_textView_numVoters);
        tShirtSize = (TextView) findViewById(R.id.details_textView_tshirtSize);
        addedBy = (TextView) findViewById(R.id.details_textView_AddedBy);
        syncStatus = (TextView) findViewById(R.id.details_textView_syncstatus);

        DB_Helper db_helper = new DB_Helper(this);
        SQLiteDatabase db = db_helper.getWritableDatabase();

        Cursor cursor = db_helper.getPerson(person_id, db);

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                name.setText(cursor.getString(cursor.getColumnIndex("person_name")));
                //name.setText("hhhhhhhhhhhhhh");
                position.setText(cursor.getString(cursor.getColumnIndex("person_position")));
                groupName.setText(cursor.getString(cursor.getColumnIndex("person_group")).toUpperCase());
                contact.setText(cursor.getString(cursor.getColumnIndex("person_contact")));
                String strAddress = cursor.getString(cursor.getColumnIndex("person_purok"))
                        + ", " + cursor.getString(cursor.getColumnIndex("person_barangay"))
                        + ", " + cursor.getString(cursor.getColumnIndex("person_city"));
                address.setText(strAddress);
                birthday.setText(cursor.getString(cursor.getColumnIndex("person_bday")));
                membershipDate.setText(cursor.getString(cursor.getColumnIndex("person_date_added")));
                spouse.setText(cursor.getString(cursor.getColumnIndex("person_spouse")));
                education.setText(cursor.getString(cursor.getColumnIndex("person_attainment")));
                numOfFam.setText(cursor.getString(cursor.getColumnIndex("person_num_family")));
                numOfVoters.setText(cursor.getString(cursor.getColumnIndex("person_num_voters")));
                tShirtSize.setText(cursor.getString(cursor.getColumnIndex("person_shirt_size")));
                addedBy.setText(cursor.getString(cursor.getColumnIndex("person_added_by")));
                int sync_status_number = Integer.parseInt(cursor.getString(cursor.getColumnIndex("person_sync_status")));
                if(sync_status_number==CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_SUCCESS){
                    syncStatus.setText("SUCCESSFUL");
                }else{
                    syncStatus.setText("FAILED");
                }

            }

        }

        db.close();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("com.klevie.livelihood.PERSON_ID", person_id);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        person_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.PERSON_ID");
        initializeText();
        super.onRestoreInstanceState(savedInstanceState);
    }
}
