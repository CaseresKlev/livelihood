package com.livelihood.voidmain.livelihood;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class addNewListing extends Fragment implements View.OnClickListener {

    TextInputLayout textInputname;
    Spinner spinner_position;
    Button save;
    OnDbOperationListener dbOperationListener;


    public addNewListing() {
    }

    @Override
    public void onClick(View v) {
        //validateInput(v);
        SQLiteDatabase db = null;
        DB_Helper db_helper = new DB_Helper(getContext());
        String nameInput = textInputname.getEditText().getText().toString().trim();
        boolean status = db_helper.addPerson(nameInput, 1, db);

        if(status){
            Toast.makeText(getContext(), "Successfully Added!", Toast.LENGTH_SHORT).show();
            textInputname.getEditText().setText("");
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //showData(db_helper);
    }


    //we created universal interface that should be implemented to use the DB Opeartion
    public interface OnDbOperationListener{
        public void dbOperation(int method);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_listing, container, false);

        save = (Button) view.findViewById(R.id.btn_save);
        save.setOnClickListener(this);

        textInputname = view.findViewById(R.id.editText_Name);

        spinner_position = (Spinner) view.findViewById(R.id.position);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner, getResources().getStringArray(R.array.position));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_position.setAdapter(adapter);


        return view;
    }

    public  boolean validateInput(View v){
        //Toast.makeText(getContext(), "Hellow ", Toast.LENGTH_SHORT).show();
        String nameInput = textInputname.getEditText().getText().toString().trim();
        //Toast.makeText(getContext(), "Hellow " + nameinput, Toast.LENGTH_SHORT).show();

        if(nameInput.isEmpty()){
            textInputname.setError("Name Cannot be empty!");
            return false;
        }else{
            textInputname.setError(null);
            return true;
        }

    }
    
}
