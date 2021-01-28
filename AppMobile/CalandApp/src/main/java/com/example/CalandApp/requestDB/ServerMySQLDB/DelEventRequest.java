package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.EventActivity;
import com.example.CalandApp.homeActivity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DelEventRequest {

    public DelEventRequest(Activity activity, int event_id){
        String HttpURL = activity.getResources().getString(R.string.server_url) + "Delete_event.php";

        // Build HashMap for build request with encrypted variables
        HashMap<String,String> params = new HashMap<>();
        params.put("event_id",String.valueOf(event_id));

        new PostRequest(activity, HttpURL, params).exeRequest( new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("DelEventRequest", response.toString());

                ((EventActivity) activity).doAfterDelEvent(response);
            }
        });


    }
}

