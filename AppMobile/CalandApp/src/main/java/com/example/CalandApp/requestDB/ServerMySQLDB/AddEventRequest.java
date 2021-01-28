package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddEventRequest {

    public AddEventRequest(Activity activity, int user_id, int group_id, String event_moment, String event_duration, String description ){

        String HttpURL = activity.getResources().getString(R.string.server_url) + "Add_event.php";

        // Build HashMap for build request with encrypted variables
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("group_id", String.valueOf(group_id));
        params.put("event_moment", event_moment);
        params.put("event_duration", event_duration);
        params.put("description", description);

        // Create and sent request for add a new group
        new PostRequest(activity, HttpURL, params).exeRequest( new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("AddEventRequest", response.toString());

                // after response from server execute  doAfterAddEvent() method from HomeActivity
                ((HomeActivity) activity).doAfterAddEvent(response);
            }
        });
    }
}

