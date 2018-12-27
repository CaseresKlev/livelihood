package com.livelihood.voidmain.livelihood;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    //this will be the list to be pass in the adopter since it is needed
    //also we put value in the list here in main activity. the value may came from database
   // private List<Person> personList;

    //this will be the instance of the recycler view in our main_activity.xml
   // RecyclerView recyclerView;
    ProgressDialog dialog;
    private  int selectedFragment = 0;
    private  boolean hasSelectedNav=false;
    Fragment fragment;
    private RequestQueue requestQueue;
    DB_Helper db_helper;
    SQLiteDatabase db;
    private boolean isLoaded = false;
    public boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //dialog = ProgressDialog.show(this, "Fetching", "Please Wait...", true);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                if(hasSelectedNav){

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                }
                hasSelectedNav = false;
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


        if(!isLoaded){
            getGroupFromWeb();
        }

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
                    "No network connection Available! Your data is volunerable for curruption.", 1);
            dialog.show(getSupportFragmentManager(), "MessageDialog");
        }else{
            new CheckConnection(this, requestQueue).execute();
        }
        //Toast.makeText(this, db_helper.isNetworkAvailable() + "", Toast.LENGTH_SHORT).show();

        db_helper.closeDB(db);

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
            StringRequest request = new StringRequest(Request.Method.GET,APPLICATION_SERVER.pingTest,
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
                                    MessageDialog dialog = MessageDialog.newInstance("Success", "Good! You are Connected to the Applicatio Server!", 1);
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
                    connected = false;
                    Log.d("ResponceFromServer", "ERROR");
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
                //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d("errorResponce", error.getMessage());
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
            case R.id.nav_export_pdf:
                Toast.makeText(this, "This is Export to pdf fn.", Toast.LENGTH_SHORT).show();
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


  /*  @Override
    public void dbOperation(int method) {
        switch (method){
            case 1:
                Toast.makeText(this, "The operation is Add", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    */




    /*

    if(hasSelectedNav){
            dialog = ProgressDialog.show(this, "Fetching", "Please Wait...", true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                    dialog.dismiss();
        }

     */
}
