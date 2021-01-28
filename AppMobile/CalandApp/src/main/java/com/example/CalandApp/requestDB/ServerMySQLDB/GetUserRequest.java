package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetUserRequest {

    public GetUserRequest(Activity activity){
        String HttpURL = activity.getResources().getString(R.string.server_url) + "Get_all_user.php";


        // Build HashMap for build request with encrypted variables
        HashMap<String,String> params = new HashMap<>();

        new PostRequest(activity, HttpURL, params).exeRequest( new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("GetUserRequest", response.toString());

                ((HomeActivity) activity).doAfterGetUser(response);
            }
        });


    }
}

