package com.livelihood.voidmain.livelihood;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentCall_GroupList extends Fragment {

    List<Group> groupList;


    public FragmentCall_GroupList() {
        groupList = new ArrayList<>();
    }
    ////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);


       DB_Helper db_helper = new DB_Helper(getActivity());
       SQLiteDatabase db = db_helper.getWritableDatabase();
        Cursor cursor = db_helper.getGroup();

        if(cursor.getCount()==0){

        }else{
            while (cursor.moveToNext()){
                Group group = new Group(cursor.getInt(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_GROUP.GROUP_ID)),
                        cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_GROUP.GROUP_NAME)),
                        cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_GROUP.GROUP_PUROK)) + ", "
                        + cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_GROUP.GROUP_BARANGAY)),
                        cursor.getString(cursor.getColumnIndex(CONTRACT_DB_TABLES.Table_GROUP.GROUP_CITY)), 1);

                groupList.add(group);
            }
        }


        Spinner spinner_position = (Spinner) view.findViewById(R.id.spinner_choose_city);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, getResources().getStringArray(R.array.position));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_position.setAdapter(adapter);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.groupListRecycleView);

        Controller_Group groupController = new Controller_Group(groupList);
        recyclerView.setAdapter(groupController);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
              recyclerView.setLayoutManager(layoutManager);
        /*
*/
        return view;
    }



    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
}
