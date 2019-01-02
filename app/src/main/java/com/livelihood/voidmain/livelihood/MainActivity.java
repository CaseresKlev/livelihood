package com.livelihood.voidmain.livelihood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    //this will be the list to be pass in the adopter since it is needed
    //also we put value in the list here in main activity. the value may came from database
   // private List<Person> personList;

    //this will be the instance of the recycler view in our main_activity.xml
   // RecyclerView recyclerView;
    ProgressDialog dialog;
    private  int selectedFragment = 0;
    private  boolean hasSelectedNav=false;
    public Fragment fragment;
    public NavigationView nav_view;
    private RequestQueue requestQueue;
    DB_Helper db_helper;
    SQLiteDatabase db;
    private boolean isLoaded = false;
    public boolean connected = false;
    public static  int addCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //dialog = ProgressDialog.show(this, "Fetching", "Please Wait...", true);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);



         nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                refreshFragment();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(!isLoaded) {
            selectedFragment = 1;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentCall_MyListing()).commit();
            nav_view.setCheckedItem(R.id.nav_myListing);
        }


        db_helper = new DB_Helper(this);
        db = db_helper.getWritableDatabase();
        requestQueue  =  Volley.newRequestQueue(this);


       /* if(db_helper.isNetworkAvailable()
                && db_helper.isHostReachable(APPLICATION_SERVER.getGroupURL, 80, 5000)){
            Log.d("NetworkState", "Netwok is available and Connected");
        }else{
            Log.d("NetworkState", "Netwok is NOT available and NOT Connected");
        }*/

        //Log.d("IsServerIsReachable", new ConnectToServer().execute() + "");
        //new ConnectToServer(this).execute();
        boolean networkAvailable = db_helper.isNetworkAvailable();
        if(!networkAvailable){
            MessageDialog dialog = MessageDialog.newInstance("Warning",
                    "No network connection Available! Your data is volunerable for curruption.",
                    CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_ERROR);
            dialog.show(getSupportFragmentManager(), "MessageDialog");
        }else{
            //new CheckConnection(this, requestQueue).execute();
            getGroupFromWeb();
        }
        //Toast.makeText(this, db_helper.isNetworkAvailable() + "", Toast.LENGTH_SHORT).show();

        db_helper.closeDB(db);

    }

    private void refreshFragment() {


        if(hasSelectedNav && selectedFragment == 1 || selectedFragment == 2){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        }else if(hasSelectedNav && selectedFragment == 3){

            //closed the drawer then add transaction

            ProgressDialog pd = ProgressDialog.show(MainActivity.this,"Submitting Data","Please wait..");

            DB_Helper db_helper = new DB_Helper(MainActivity.this);
            SQLiteDatabase db = db_helper.getWritableDatabase();
            Cursor cursor = db_helper.submitData(db);
            if(cursor.getCount()>0){
                addCount = cursor.getCount();
                int count=1;
                while (cursor.moveToNext()){
                    int id = cursor.getInt(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_ID));
                    String name = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_NAME));
                    String position = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_POSITION));
                    String contact = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_CONTACT));
                    String bday = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_BIRTHDATE));
                    String date_added = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_DATE));
                    String spouse = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_SPOUSE));
                    String attainment = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_ATTAINMENT));
                    String shirtSize = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_SHIRT_SIZE));
                    int numOfFamily = cursor.getInt(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_FAMILY));
                    int numOfVoters = cursor.getInt(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_NUMBER_VOTERS));
                    String remarks = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_REMARKS));
                    int groupID = cursor.getInt(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_GROUP_ID));
                    String addedBy = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_ADDED_BY));
                    String syncAction = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_SYNC_ACTION));
                    String web_id = cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_Person.PERSON_WEB_ID));

                    submitListing(id, name, position, contact, bday, date_added, spouse, attainment,
                            shirtSize, numOfFamily, numOfVoters, remarks, groupID, addedBy, syncAction, web_id, count, pd);
                    count ++;
                }
            }else{
                if(pd!=null && pd.isShowing()){
                    pd.dismiss();
                }
                MessageDialog dialog = MessageDialog.newInstance("Data was Synced", "All your data was already Synced!",
                        CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_SUCCESS);
                dialog.show(getSupportFragmentManager(), "MessageDialog");

                setFirtsFragment();

            }
            db.close();



            //
            //Toast.makeText(this, "This is SYNC FUNCTION", Toast.LENGTH_SHORT).show();


        }


        hasSelectedNav = false;

    }

    private void setFirtsFragment() {
        hasSelectedNav = true;
        selectedFragment = 1;
        loadFragment();
        refreshFragment();
        nav_view.setCheckedItem(R.id.nav_myListing);
    }

    public class CheckConnection extends AsyncTask<Void, Void, Void> {

        Context context;
        private RequestQueue requestQueue;
        public CheckConnection(Context context, RequestQueue requestQueue) {
            this.context = context;
            this.requestQueue = requestQueue;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.GET, APPLICATION_SERVER.pingTest,
                    new Response.Listener<String>(){

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("connectionTest").toString();
                                Log.d("ResponceFromServer", "!" + status + "!");
                                if(!(status.equals("OK"))){
//                                    connected = true;
//                                    Log.d("ResponseStatus", "Connected");
                                    MessageDialog dialog = MessageDialog.newInstance("Connect Error",
                                            "Cannot connect to server!",
                                            CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_ERROR);
                                    dialog.show(getSupportFragmentManager(), "MessageDialog");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(MainActivity.this, (connected ? "Connected" : "Not Connected"), Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    MessageDialog dialog = MessageDialog.newInstance("Connect Error",
                            "Cannot connect to server!", CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_ERROR);
                    dialog.show(getSupportFragmentManager(), "MessageDialog");
                    error.printStackTrace();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map <String, String> params = new HashMap<>();
                    params.put("PassKey", "com.klevie.livelihood");
                    return super.getParams();
                }
            };

            this.requestQueue.add(request);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(MainActivity.this, (connected ? "Connected" : "Not Connected"), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("com.klevie.livelihood.SELECTED_FRAGMENT", selectedFragment);
        outState.putBoolean("com.klevie.livelihood.ISLOADED", isLoaded);
        Log.d("OnSavedIsLoaded", isLoaded + " " + selectedFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //person_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.PERSON_ID");
        selectedFragment = (int) savedInstanceState.getSerializable("com.klevie.livelihood.SELECTED_FRAGMENT");
        isLoaded = (boolean) savedInstanceState.getSerializable("com.klevie.livelihood.ISLOADED");
        Log.d("OnrestoreIsLoaded", isLoaded + " " + selectedFragment);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getGroupFromWeb() {
        Log.d("getGroupFromWebFN", "From: parser on main");
        final ProgressDialog pd = ProgressDialog.show(this,"Syncing Data","Please wait..");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, APPLICATION_SERVER.getGroupURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("group");
                            //setJson(j);

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);

                                String id =  data.getString("group_id");
                                String name = data.getString("group_name");
                                String purok = data.getString("purok");
                                String brgy = data.getString("barangay");
                                String city = data.getString("city");

                                db_helper.addGroup(Integer.parseInt(id), name, purok, brgy, city, db);
                            }

                            db_helper.closeDB(db);

                            if(pd!=null && pd.isShowing())
                                pd.dismiss();
                            Log.d("Successful on try", "From: parser on main");
                            //
                        } catch (JSONException e) {
                            Log.d("JSONException", e.getMessage());
                            //e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pd!=null && pd.isShowing())
                    pd.dismiss();
                MessageDialog dialog = MessageDialog.newInstance("Warning: Sync Failed!",
                        "Cannot connect to server! Your data will not be sync to the Application Server.",
                        CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_ERROR);
                dialog.show(getSupportFragmentManager(), "MessageDialog");
                error.printStackTrace();
            }
        });
        isLoaded = true;
        requestQueue.add(jsonRequest);

        // Toast.makeText(context, "length of Jason Array: " + jsonArray.length(), Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_myListing:
                selectedFragment = 1;
                //fragment = new FragmentCall_MyListing();
                hasSelectedNav = true;
                loadFragment();
                break;
            case R.id.nav_addNew:
                selectedFragment = 2;
                //fragment = new addNewListing();
                //fragment = new FragmentCall_GroupList();
                hasSelectedNav = true;
                loadFragment();
                //Intent intent = new Intent(this, R.layout.activity_add_new)
                break;

                case R.id.nav_sync_data:
                    selectedFragment = 3;
                    hasSelectedNav = true;
                    break;

            case R.id.nav_export_pdf:
                //Toast.makeText(this, "This is Export to pdf fn.", Toast.LENGTH_SHORT).show();
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(MainActivity.this, "neagtive", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                ConfimationDialog c = new ConfimationDialog(this, "This is title", "The Message", listener);
                c.show();
                break;
            case R.id.nav_logOut:
                Toast.makeText(this, "This is Logout FN", Toast.LENGTH_SHORT).show();
                break;
        }
        //drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.START);
       // hideDrawer();
        //dialog.dismiss();
        return true;
    }

    private void loadFragment(){
        if(selectedFragment==1){
            fragment = new FragmentCall_MyListing();
        }else if(selectedFragment==2){
            fragment = new FragmentCall_GroupList();
        }else if(selectedFragment==3){
        }


    }

    private void hideDrawer() {
        if(hasSelectedNav){
            //dialog = ProgressDialog.show(this, "Fetching", "Please Wait...", true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
            //dialog.dismiss();
        }

        //return GravityCompat.START;
    }


    public void submitListing(final int id, String name, String position, String contact,
                              String bday, String date_added, String spouse,
                              String attainment, String shirtSize, int numOfFamily,
                              int numOfVoters, String remarks, int groupID,
                              String addedBy, final String syncAction, final String web_id, final int count, final ProgressDialog pd){
        //ConfimationDialog c = new ConfimationDialog(this, "This is title", "The Message", this);
        Log.d("SubmitListing", syncAction);
        Date d = new Date(bday);
        bday = new SimpleDateFormat("yyyy-MM-dd").format(d);

        d = new Date(date_added);
        date_added = new SimpleDateFormat("yyyy-MM-dd").format(d);

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("name", name);
            postparams.put("position", position);
            postparams.put("contact", contact);
            postparams.put("bday", bday);
            postparams.put("date_added", date_added);
            postparams.put("spouse", spouse);
            postparams.put("attainment", attainment);
            postparams.put("shirtSize", shirtSize);
            postparams.put("numOfFamily", numOfFamily);
            postparams.put("numOfVoters", numOfVoters);
            postparams.put("remarks", remarks);
            postparams.put("groupID", groupID);
            postparams.put("addedBy", addedBy);
            postparams.put("syncAction", syncAction);
            postparams.put("webID", web_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request jsonObjReq = new JsonObjectRequest(Request.Method.POST, APPLICATION_SERVER.addPerson + APPLICATION_SERVER.ADD_PERSON_PACKAGE,
                postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("ResponcefromServer", response.toString());
                //Log.d("AddPersonInWeb", "Web ID: " + response.getInt("addperson"));
                try {

                    DB_Helper db_helper = new DB_Helper(MainActivity.this);
                    SQLiteDatabase db = db_helper.getWritableDatabase();
                    Log.d("SyncAction", "!"+syncAction+'!');
                    if(syncAction.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_ADD)){
                        String wb_id = response.getString("addperson");
                        db_helper.updateSyncStatus(id, CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_SUCCESS, "", wb_id, db);
                        db.close();
                    }else if(syncAction.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_DELETE)){
                        String result = response.getString("deletePerson");
                        Log.d("ResultOnDeleteFromMain", result);
                        if (result.equals("DELETED")){
                            Log.d("DeleteFromMain", "DeletedIsTrue");
                            db = db_helper.getWritableDatabase();

                            if(db.delete(CONTRACT_DB_TABLES.Table_Person.PERSON_TABLE_NAME,
                                    CONTRACT_DB_TABLES.Table_Person.PERSON_ID + " = " +id, null)>0){

                            }
                            db.close();
                        }
                    }else if(syncAction.equals(CONTRACT_DB_TABLES.SYNC_ACTION.ACTION_EDIT)){
                        Log.d("EditDetectedOnSync", response.toString());
                        String result = response.getString("editPerson");
                        Log.d("EditDetectedOnSync", result.toString());
                        if(result.equals("EDITED")){
                            db_helper.updateSyncStatus(id, CONTRACT_DB_TABLES.SYNC_STATUS.SYNC_SUCCESS, "", web_id, db);

                            Log.d("OnEditServer", "the person should be updated");

                        }else{
                            Log.d("OnEditServer", result.toString());
                        }

                        db.close();
                    }


                } catch (JSONException e) {
                    Log.d("ServerError", response.toString());
                    e.printStackTrace();
                }

                if(count==addCount){
                    if(pd!=null && pd.isShowing())
                        pd.dismiss();
                    MessageDialog dialog = MessageDialog.newInstance("Success", "Your data was Synced to the server", CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_SUCCESS);
                    dialog.show(getSupportFragmentManager(), "MessageDialog");
                    setFirtsFragment();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("ErrorDetected", error.toString());
                if(pd!=null && pd.isShowing())
                    pd.dismiss();
                MessageDialog dialog = MessageDialog.newInstance("Cannot Submit Data",
                        "Cannot connect to server!",
                        CONTRACT_DB_TABLES.CONNECT_STATUS.CONNECT_ERROR);
                dialog.show(getSupportFragmentManager(), "MessageDialog");
                error.printStackTrace();
                /*
                //ServerError serverError = error.get
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.d("onErrorResponce","TimeoutError");
                } else if (error instanceof AuthFailureError) {
                    Log.d("onErrorResponce","AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.d("onErrorResponce","ServerError");
                } else if (error instanceof NetworkError) {
                    Log.d("onErrorResponce","NetworkError");
                } else if (error instanceof ParseError) {
                    //ParseError parseError = (ParseError) error;
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.d("onErrorResponce","ParseError");
                    Log.d("ParseError", networkResponse.data.toString());
                }



                NetworkResponse networkResponse = error.networkResponse;

                String body;
                //get status code here
                String statusCode = String.valueOf(networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                Log.d("ErrorCode", statusCode + "");
                if(error.networkResponse.data!=null) {
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        Log.d("ErrorBody", body);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                */
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

        this.requestQueue.add(jsonObjReq);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("OnActivityReseultMain", "request Code: " + requestCode + " Result Code: " + resultCode);
        if(requestCode==CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_CODE_DELETE
                && resultCode==CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_SUCCESS){
            hasSelectedNav = true;
            selectedFragment = 1;
            loadFragment();
            refreshFragment();
            Log.d("OnActivityReseult", "request Code: " + requestCode + " Result Code: " + resultCode);
        }else if(requestCode==CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_CODE_DELETE
                && resultCode==CONTRACT_DB_TABLES.REQUEST_CODE.REQUEST_ERROR){
            setFirtsFragment();

        }
        //
    }

}
