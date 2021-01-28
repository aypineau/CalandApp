package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;
import com.example.CalandApp.log.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddGroupRequest {

    public AddGroupRequest(Activity activity, String ClassToDoReponse, String group_name){

        String HttpURL = activity.getResources().getString(R.string.server_url) + "Add_group.php";

        // Build HashMap for build request with encrypted variables
        HashMap<String,String> params = new HashMap<>();
        params.put("group_name",group_name);


        PostRequest addGroupRequest = new PostRequest(activity, HttpURL, params);
        addGroupRequest.exeRequest( new VolleyCallback() {

            private Object HomeActivity;
            private Object RegisterActivity;

            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("AddGroupRequest", response.toString());

                if(ClassToDoReponse.equals("RegisterActivity")){
                    ((RegisterActivity) activity).doAfterCreateGroup(response);
                } else if(ClassToDoReponse.equals("HomeActivity") ){
                    ((HomeActivity) activity).doAfterAddGroup(response);
                }
            }
        });


    }
}

