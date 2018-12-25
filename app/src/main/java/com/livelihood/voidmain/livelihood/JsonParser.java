package com.livelihood.voidmain.livelihood;

import android.content.Context;
import android.util.Log;
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

public class JsonParser {
    private String url;
    private String jsonArray_name;
    private static JSONArray jsonArray;
    private RequestQueue requestQueue;
    private Context context;
    private boolean success = false;
    public JsonParser(Context context, String url, String jasonArray_name) {
        this.context = context;
        this.url = url;
        this.jsonArray_name = jasonArray_name;
        this.requestQueue = Volley.newRequestQueue(context);
        parse();
    }

    public void parse(){
        Log.d("Parser.parse", "This is from parse");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                             JSONArray j = response.getJSONArray(jsonArray_name);
                             setJson(j);
                           /* StringBuffer sb = new StringBuffer();
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);

                                String id = "ID: " + data.getString("group_id");
                                String name = "Name: " + data.getString("group_name");
                                String purok = "Purok: " + data.getString("purok");
                                String brgy = "Barangay: " + data.getString("barangay");
                                String city = "City: " + data.getString("city");
                                sb.append(id + " " + name + " " + purok + " " + brgy + " " + city + "\n");
                            }
                            */
                            Group group;
                            Log.d("Successful on try", "From: parser");
                            //Toast.makeText(MainActivity.this, sb.toString().trim(), Toast.LENGTH_SHORT).show();
                           //return jsonArray;
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

        requestQueue.add(jsonRequest);
       // Toast.makeText(context, "length of Jason Array: " + jsonArray.length(), Toast.LENGTH_SHORT).show();
    }

    private void setJson(JSONArray j) {
        Log.d("JArrayIniatialization", "Successfull");
        this.jsonArray = j;
        Log.d("JArraSize", jsonArray.length() + "");
        success = true;

    }

    public JSONArray getJsonArray(){
        if(success){
            Log.d("JArrayStat", jsonArray.length() + "");
        }else{
            Log.d("JArrayStat", "JarrayNotinialize");
        }

        return this.jsonArray;

    }
}
