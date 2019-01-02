package com.livelihood.voidmain.livelihood;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonDetails extends AppCompatActivity implements View.OnClickListener {

    TextView position, name, groupName, contact, address, birthday, membershipDate, spouse, education,
    numOfFam, numOfVoters, tShirtSize, addedBy, syncStatus;
    private int person_id, group_id = 0;
    public RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        TextView tv = (TextView) findViewById(R.id.temp_text);

        ImageView delete = (ImageView) findViewById(R.id.imageView_delete);
        delete.setOnClickListener(this);

        ImageView edit = (ImageView) findViewById(R.id.imageView_edit);
        edit.setOnClickListener(this);
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageView_delete:
                deletePerson();
                break;

            case R.id.imageView_edit:
                //Toast.makeText(this, "Edit " + person_id, Toast.LENGTH_SHORT).show();
                DB_Helper db_helper = new DB_Helper(this);
                SQLiteDatabase db = db_helper.getWritableDatabase();
                String query = "SELECT " + CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID + " "
                        + "FROM " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME
                        + " WHERE " + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " = " + person_id;
                Cursor cursor = db_helper.runCustomQuery(query, db);
                if(cursor.getCount()>0){
                    while (cursor.moveToNext()){
                        group_id = cursor.getInt(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID));
                        Log.d("Group ID: ", group_id + "");
                    }
                }

                if(group_id>0){
                    Intent intent = new Intent(this, AddNew.class);
                    intent.putExtra("com.klevie.livelihood.GROUP_ID", group_id);
                    intent.putExtra("com.klevie.livelihood.USER_ID", 5001);
                    intent.putExtra("com.klevie.livelihood.ACTION", CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT);
                    intent.putExtra("com.klevie.livelihood.PERSON_ID", person_id);
                    ((Activity) this).startActivityForResult(intent, CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_CODE_EDIT);
                }

                db.close();
                break;
        }
    }

    private void deletePerson() {
        final ProgressDialog pd = ProgressDialog.show(this,"Deleting person","Please wait..");
        final DB_Helper db_helper = new DB_Helper(this);
        final SQLiteDatabase db = db_helper.getWritableDatabase();

        String query = "SELECT " + CONTRACT_DB_TABLES.Table_Person.PERSON_WEB_ID
                + " FROM " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME
                + " WHERE " + CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME + "."
                + CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " = " + person_id;

        Cursor cursor = db_helper.runCustomQuery(query, db);
        //Log.d("WEBID", cursor.getCount() + "");
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                //Log.d("WEBID", cursor.getCount() + "");
                final String web_id = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_WEB_ID));
                Log.d("WEBID", "ghfshefehbfsne " + web_id);

                JSONObject param = new JSONObject();
                try {
                    param.put("webID", web_id);
                    param.put("syncAction", CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_DELETE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!web_id.equals("")){
                    Log.d("itGoesHere","gdvbhbsef");
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, APPLICATION_SERVER.addPerson + APPLICATION_SERVER.ADD_PERSON_PACKAGE,
                                    param, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ResponceFromServer", response.toString());
                                    try {
                                        String result = response.getString("deletePerson");
                                        Log.d("ResultFromServer", result.toString());
                                        if (result.equals("DELETED")){
                                            SQLiteDatabase db2 = db_helper.getReadableDatabase();
                                            if(db2.delete(CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME,
                                                    CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " = " + person_id,
                                                    null)>0){

                                                if(pd!=null && pd.isShowing())
                                                    pd.dismiss();

                                                MessageDialog dialog = MessageDialog.newInstance("Delete Successful", "The Person was Deleted",
                                                        CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_SUCCESS);
                                                dialog.show(getSupportFragmentManager(), "MessageDialogDelete");
                                                Intent intent = new Intent();
                                                setResult(CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_SUCCESS, intent);
                                                finish();
                                            }
                                            db2.close();
                                        }else{
                                            if(pd!=null && pd.isShowing())
                                                pd.dismiss();
                                            MessageDialog dialog = MessageDialog.newInstance("Delete Error",
                                                    "Something went wrong while deleting the person",
                                                    CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_SUCCESS);
                                            Log.d("OnDeleteError", "This is Else! Error deleting the person");

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("JSONEXEPTION", e.getMessage().toString());
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    SQLiteDatabase db2;
                                    db2 = db_helper.getReadableDatabase();
                                    db_helper.updateSyncStatus(person_id, CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_FAILED,
                                            CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_DELETE, web_id, db2);

                                    if(pd!=null && pd.isShowing())
                                        pd.dismiss();

                                    MessageDialog dialog = MessageDialog.newInstance("Sync Required", "Person was deleted but needs to sync to the server." +
                                                    "\nSync your data once you can connect to server",
                                            CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_SUCCESS);
                                    dialog.show(getSupportFragmentManager(), "MessageDialogDelete");
                                    Intent intent = new Intent();
                                    setResult(CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_ERROR, intent);
                                    db2.close();
                                    // finish();
                                    //Log.d("OnDeleteError", error.getMessage().toString());
                                    error.printStackTrace();
                                }
                            }){
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {

                                    // Your header parameter if you have otherwise this is optional
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    return headers;
                                }
                            };

                            requestQueue.add(request);
                        }else{
                            if(db.delete(CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME,
                                    CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " = " +person_id, null)>0){
                                Intent intent = new Intent();
                                setResult(CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_ERROR, intent);
                                if(pd!=null && pd.isShowing())
                                    pd.dismiss();
                                finish();
                            }
                        }
                    }
                    db.close();
                }





        //Log.d("DeletePerson", query);


        //Cursor data = db_helper.runCustomQuery()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("OnActivityReseultPD", "request Code: " + requestCode + " Result Code: " + resultCode);
    }
}
