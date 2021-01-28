package com.example.CalandApp.requestDB.ServerMySQLDB;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PostRequest {
    private String URL;
    private Map<String, String> data;
    private Activity activity;

    PostRequest(Activity activity, String URL, Map<String, String> data) {
        this.activity = activity;
        this.URL = URL;
        this.data = data;
    }

    public void exeRequest(final VolleyCallback callback) {

        Log.i("Request prepared for", URL);

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Server response", response);
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            callback.onSuccess(jsonResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public  void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                return data;
            }

        };
        requestQueue.add(stringRequest);
    }

}


