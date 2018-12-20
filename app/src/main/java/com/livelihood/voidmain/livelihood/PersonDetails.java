package com.livelihood.voidmain.livelihood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PersonDetails extends AppCompatActivity {

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
                int person_id = b.getInt("com.klevie.livelihood.PERSON_ID");
            }
        }else{
            person_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.PERSON_ID");
            //tv.setText(person_id + "");
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("com.klevie.livelihood.PERSON_ID", person_id);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        person_id = (int) savedInstanceState.getSerializable("com.klevie.livelihood.PERSON_ID");

        super.onRestoreInstanceState(savedInstanceState);
    }
}
