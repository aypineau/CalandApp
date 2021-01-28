package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;
import com.example.CalandApp.log.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LinkUserGroupRequest {

    public LinkUserGroupRequest(Activity activity, String ClassToDoReponse, int user_id, int group_id) {
        String HttpURL = activity.getResources().getString(R.string.server_url) + "Link_user_group.php";

        // Build HashMap for build request with encrypted variables
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("group_id", String.valueOf(group_id));


        new PostRequest(activity, HttpURL, params).exeRequest( new VolleyCallback(){

            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("LinkGroupRequest", response.toString());

                if(ClassToDoReponse.equals("RegisterActivity")){
                    ((RegisterActivity) activity).doAfterLinkGroup(response);
                } else if(ClassToDoReponse.equals("HomeActivity") ){
                    ((HomeActivity) activity).doAfterLinkGroup(response);
                }
            }
            });



    }
}
