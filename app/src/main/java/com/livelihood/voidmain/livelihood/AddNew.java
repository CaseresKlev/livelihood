package com.livelihood.voidmain.livelihood;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNew extends AppCompatActivity implements View.OnClickListener {

    int group_id, userId,  numOfFam, numVoters;
    long person_id = 0;
    String name , birthday, strSpouse, strContact, strRemarks, strPosition, strAttainment, strtshirtSize;

    String action = "";
    String webId = "";

    TextInputLayout inputName, bday, contact, spouse, numFamilyMember, numOfVoters, remarks ;
    Spinner spinner_position, spinner_tshirtSize, spinner_attainment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //group_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.GROUP_ID");
       // userId = (int) savedInstanceState.getSerializable("com.klevie.livelihood.USER_ID");

        Button save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(this);


        inputName = findViewById(R.id.editText_Name);
        bday = findViewById(R.id.editText_Bday);
        contact = findViewById(R.id.editText_Contact);
        spouse = findViewById(R.id.editText_Spouse);
        numFamilyMember = findViewById(R.id.editText_numFamilyMember);
        numOfVoters = findViewById(R.id.editText_numOfVoters);
        remarks = findViewById(R.id.editText_remarks);

        spinner_position = (Spinner) findViewById(R.id.position);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner, getResources().getStringArray(R.array.position));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_position.setAdapter(adapter);

        spinner_tshirtSize = (Spinner) findViewById(R.id.tshirtSize);
        adapter = new ArrayAdapter<>(this, R.layout.custom_spinner, getResources().getStringArray(R.array.tshirtSizes));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tshirtSize.setAdapter(adapter);

        spinner_attainment = (Spinner) findViewById(R.id.education);
        adapter = new ArrayAdapter<>(this, R.layout.custom_spinner, getResources().getStringArray(R.array.eduationAttainment));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_attainment.setAdapter(adapter);

        Bundle b = null;
        if(savedInstanceState==null){
            b = getIntent().getExtras();
            if(b==null){

            }else{
                group_id = b.getInt("com.klevie.livelihood.GROUP_ID");
                userId = b.getInt("com.klevie.livelihood.USER_ID");
                action = b.getString("com.klevie.livelihood.ACTION");

                if(action.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT)){
                    person_id = b.getInt("com.klevie.livelihood.PERSON_ID");
                    Log.d("ResultAction", action);
                    initForm();

                }else{
                    person_id = 0;
                }
            }

        }else{
            group_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.GROUP_ID");
            userId = (int) savedInstanceState.getSerializable("com.klevie.livelihood.USER_ID");
            action = (String) savedInstanceState.getSerializable("com.klevie.livelihood.ACTION");

            if(action.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT)){
                person_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.PERSON_ID");
                initForm();
            }else{
                person_id = 0;
            }

            //tv.setText(person_id + "");
        }
    }

    private void initForm() {
        Log.d("InitForm", action);
        DB_Helper db_helper = new DB_Helper(this);
        SQLiteDatabase db = db_helper.getWritableDatabase();

        Cursor cursor = db_helper.getPerson(person_id, db);
        if(cursor.getCount()>0){

            while (cursor.moveToNext()){
                inputName.getEditText().setText(cursor.getString(cursor.getColumnIndex("person_name")));
                bday.getEditText().setText(cursor.getString(cursor.getColumnIndex("person_bday")));
                spouse.getEditText().setText(cursor.getString(cursor.getColumnIndex("person_spouse")));
                contact.getEditText().setText(cursor.getString(cursor.getColumnIndex("person_contact")));
                numFamilyMember.getEditText().setText(cursor.getInt(cursor.getColumnIndex("person_num_family")) + "");
                numOfVoters.getEditText().setText(cursor.getInt(cursor.getColumnIndex("person_num_voters")) + "");
                remarks.getEditText().setText(cursor.getString(cursor.getColumnIndex("person_remarks")));
                spinner_position.setSelection(1);
                spinner_attainment.setSelection(1);
                spinner_tshirtSize.setSelection(1);

                webId = cursor.getString(cursor.getColumnIndex("person_web_id"));
            }

        }else{
            MessageDialog dialog = MessageDialog.newInstance("Cannot find person",
                    "The person you are trying to edit is no longer available",
                    CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_FAILED);
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        Log.d("ResultAction", action);
        name = inputName.getEditText().getText() .toString().trim();
        birthday = bday.getEditText().getText().toString().trim();
        strSpouse = spouse.getEditText().getText().toString().trim();
        strContact = contact.getEditText().getText().toString().trim();
        numOfFam = Integer.parseInt(numFamilyMember.getEditText().getText().toString().trim());
        numVoters = Integer.parseInt(numOfVoters.getEditText().getText().toString().trim());
        strRemarks = remarks.getEditText().getText().toString().trim();
        strPosition = spinner_position.getSelectedItem().toString().trim();
        strAttainment = spinner_attainment.getSelectedItem().toString().trim();
        strtshirtSize = spinner_tshirtSize.getSelectedItem().toString().trim();
        strRemarks = remarks.getEditText().getText().toString().trim();
        DB_Helper db_helper = new DB_Helper(v.getContext());
        SQLiteDatabase db = db_helper.getWritableDatabase();

        boolean result;

        if(action.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT)){

            Log.d("ResultofAction", "true");
            result = db_helper.editPerson(person_id, name, birthday, strContact, strAttainment, strSpouse, numOfFam, numVoters,
                    strtshirtSize, strPosition, group_id, CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_FAILED,
                    CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT, "12/25/2018",
                    1, webId, strRemarks, db);



        }else {
            Log.d("ResultofAction", "false");
            result = db_helper.addPerson(name, birthday, strContact, strAttainment, strSpouse, numOfFam, numVoters,
                    strtshirtSize, strPosition, group_id, CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_FAILED,
                    CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_ADD, "12/25/2018",
                    1, "", strRemarks, db);
        }
        String msg = "";

        if(result){
            if(action.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT)){
                msg = "The person details was updated!";
            }else{
                msg = "The person is added!";
            }
            MessageDialog dialog = MessageDialog.newInstance("Success",
                    msg, CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_SUCCESS);
            dialog.show(getSupportFragmentManager(), "MessageDialog");

            inputName.getEditText().setText("");
            bday.getEditText().setText("");
            spouse.getEditText().setText("");
            contact.getEditText().setText("");
            remarks.getEditText().setText("");
            spinner_position.setSelection(1);
            spinner_attainment.setSelection(1);
            spinner_tshirtSize.setSelection(1);
            numFamilyMember.getEditText().setText("");
            numOfVoters.getEditText().setText("");

            //Toast.makeText(this, "Person Successfully Added!", Toast.LENGTH_SHORT).show();
        }else{
            if(action.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT)){
                msg = "Error occurred while updating person details! Please try again!";
            }else{
                msg = "Error occurred while inserting the person!";
            }

            MessageDialog dialog = MessageDialog.newInstance("Success",
                    msg, 0);
            dialog.show(getSupportFragmentManager(), "MessageDialog");
        }

        db.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("com.klevie.livelihood.GROUP_ID", group_id);
        outState.putInt("com.klevie.livelihood.USER_ID", userId);

      /*  name = inputName.getEditText().getText() .toString().trim();
        birthday = bday.getEditText().getText().toString().trim();
        strSpouse = spouse.getEditText().getText().toString().trim();
        strContact = contact.getEditText().getText().toString().trim();
        String strNumOfFam = numFamilyMember.getEditText().getText().toString().trim();
        String strNumVoters = numOfVoters.getEditText().getText().toString().trim();
        strRemarks = remarks.getEditText().getText().toString().trim();
        int selectedPosition = spinner_position.getSelectedItemPosition();
        int selectedAttainment = spinner_attainment.getSelectedItemPosition();
        int selectedShirt = spinner_tshirtSize.getSelectedItemPosition();

        outState.putString("com.klevie.livelihood.INPUTED_NAME", name);
        outState.putString("com.klevie.livelihood.INPUTED_BIRTHDAY", birthday);
        outState.putString("com.klevie.livelihood.INPUTED_SPOUSE", strSpouse);
        outState.putString("com.klevie.livelihood.INPUTED_CONTACT", strContact);
        outState.putString("com.klevie.livelihood.INPUTED_REMARKS", strRemarks);
        outState.putInt("com.klevie.livelihood.INPUTED_POSITION", selectedPosition);
        outState.putInt("com.klevie.livelihood.INPUTED_ATTAINMENT", selectedAttainment);
        outState.putInt("com.klevie.livelihood.INPUTED_TSHIRT", selectedShirt);
        outState.putString("com.klevie.livelihood.INPUTED_NUMFAM", strNumOfFam);
        outState.putString("com.klevie.livelihood.INPUTED_NUMVOTERS", strNumVoters);

        */
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        group_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.GROUP_ID");
        userId = (int) savedInstanceState.getSerializable("com.klevie.livelihood.USER_ID");

      /*  inputName.getEditText().setText((String) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_NAME"));
        bday.getEditText().setText((String) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_BIRTHDAY"));
        spouse.getEditText().setText((String) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_SPOUSE"));
        contact.getEditText().setText((String) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_CONTACT"));
        remarks.getEditText().setText((String) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_REMARKS"));
        spinner_position.setSelection((int) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_POSITION"));
        spinner_attainment.setSelection((int) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_ATTAINMENT"));
        spinner_tshirtSize.setSelection((int) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_TSHIRT"));
        numFamilyMember.getEditText().setText((String) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_NUMFAM"));
        numOfVoters.getEditText().setText((String) savedInstanceState.getSerializable("com.klevie.livelihood.INPUTED_NUMVOTERS"));
        */


        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_CANCEL, intent);
        finish();
    }
}
