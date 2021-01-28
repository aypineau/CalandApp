package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UnlinkUserGroupRequest {

    public UnlinkUserGroupRequest(Activity activity, int user_id, int group_id) {
        String HttpURL = activity.getResources().getString(R.string.server_url) + "Unlink_user_group.php";

        // Build HashMap for build request with encrypted variables
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("group_id", String.valueOf(group_id));


        new PostRequest(activity, HttpURL, params).exeRequest( new VolleyCallback(){

            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("UnlinkGroupRequest", response.toString());

                ((HomeActivity) activity).doAfterUnlinkGroup(response);

            }
        });
    }
}
