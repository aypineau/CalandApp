package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.log.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginRequest{

    public LoginRequest(Activity activity, String email, String password) {
        String HttpURL = activity.getResources().getString(R.string.server_url) + "Login.php";

        // Build HashMap for build request with encrypted variables
        HashMap<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);

        new PostRequest(activity, HttpURL, params).exeRequest( new VolleyCallback(){

            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("Response LoginRequest", response.toString());

                LoginActivity.getInstance().doAfterLogin(response);
            }
        });

    }
}