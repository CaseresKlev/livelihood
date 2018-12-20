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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, addNewListing.OnDbOperationListener {
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
        Intent intent;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        selectedFragment = 1;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentCall_MyListing()).commit();
        nav_view.setCheckedItem(R.id.nav_myListing);

        DB_Helper db_helper = new DB_Helper(this);
        SQLiteDatabase db = null;
        /* */
        //db_helper.addGroup(1, "Group 2", "Purok-2, Apo Macote Malaybalay City", db);

        Cursor cursor = db_helper.getGroup();
        StringBuffer sb = new StringBuffer();
        sb.append("data: \n");
        if(cursor.getCount()>0){

            while (cursor.moveToNext()){

                sb.append("Group ID: " + cursor.getInt(0) + "\n");
                sb.append("Group Name: " + cursor.getString(1) + "\n");
                sb.append("Group Address: " + cursor.getString(2) + "\n");
            }


        }
        //Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
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
                fragment = new FragmentCall_MyListing();
                hasSelectedNav = true;
                break;
            case R.id.nav_addNew:
                selectedFragment = 2;
                //fragment = new addNewListing();
                fragment = new FragmentCall_GroupList();
                hasSelectedNav = true;
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

    private void hideDrawer() {
        if(hasSelectedNav){
            //dialog = ProgressDialog.show(this, "Fetching", "Please Wait...", true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
            //dialog.dismiss();
        }

        //return GravityCompat.START;
    }


    @Override
    public void dbOperation(int method) {
        switch (method){
            case 1:
                Toast.makeText(this, "The operation is Add", Toast.LENGTH_SHORT).show();
                break;
        }
    }




    /*

    if(hasSelectedNav){
            dialog = ProgressDialog.show(this, "Fetching", "Please Wait...", true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                    dialog.dismiss();
        }

     */
}
