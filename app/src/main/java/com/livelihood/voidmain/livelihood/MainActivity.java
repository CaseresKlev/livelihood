package com.livelihood.voidmain.livelihood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    RequestQueue requestQueue;
    DB_Helper db_helper;
    SQLiteDatabase db;
    private boolean isLoaded = false;

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

        Log.d("OnCreateIsLoaded", isLoaded + " " + selectedFragment);
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
