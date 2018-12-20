package com.livelihood.voidmain.livelihood;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FragmentCall_MyListing extends Fragment {
    List<Person> personList;
    DB_Helper db_helper;
    Cursor cursor;
    public FragmentCall_MyListing() {
        personList = new ArrayList<>();

        //SQLiteDatabase db = null;



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mylisting, container, false);

        db_helper  = new DB_Helper(getContext());
        cursor  = db_helper.getPerson();

        if(cursor.getCount()==0){
            //Toast.makeText(getContext(), "No data in the Database!", Toast.LENGTH_SHORT).show();
        }else{
            //StringBuffer sb = new StringBuffer();
            while (cursor.moveToNext()){
                //sb.append("Address: " +cursor.getString(4)+"\n");
                Person p = new Person(cursor.getInt(0), cursor.getString(1).toString(), cursor.getString(3).toString(),
                        cursor.getInt(2));
                personList.add(p);
            }

            //Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.listingRecycleView);

        Controler_Person c_person= new Controler_Person(personList);
        recyclerView.setAdapter(c_person);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return v;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
}
