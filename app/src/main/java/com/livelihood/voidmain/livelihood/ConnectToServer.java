package com.livelihood.voidmain.livelihood;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConnectToServer extends AsyncTask<Void, Void, Boolean> {
    String url;
    Context context;
    RequestQueue requestQueue;
    boolean connected = false;


    public ConnectToServer(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        /*
        String url = "http:google.com";
        //this.url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
        this.url = url.replace("^https", "http");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            Log.d("IsServerIsReachable", responseCode + "");
            if(responseCode==200){
                return true;
            }else{
                return false;
            }
            //return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
        //return null;
       // return null;*/
        final int[] ret = new int[1];

        StringRequest request = new StringRequest(Request.Method.GET,APPLICATION_SERVER.pingTest,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String responce = jsonObject.getString("connectionTest");
                            Log.d("ResponceFromServer", responce);
                            if(responce=="OK"){
                                //ret[0] = 1;
                                connected = true;
                            }else{
                                ret[0] = 1;
                                connected = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        requestQueue.add(request);
        Log.d("ValueOfConnected", ret[0] + "");
        if(ret[0]==1){
            return true;
        }else{
            return false;
        }
        //return connected;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // use the result
        //super.onPostExecute(result);
        Log.d("ResultBoolean", result + "");
        Toast.makeText(context, (result ? "Connected" : "Not Connected"), Toast.LENGTH_LONG).show();
    };

    //
}
