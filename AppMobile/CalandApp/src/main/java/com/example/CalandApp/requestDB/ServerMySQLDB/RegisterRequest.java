package com.example.CalandApp.requestDB.ServerMySQLDB;

import android.app.Activity;
import android.util.Log;

import com.example.CalandApp.R;
import com.example.CalandApp.log.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterRequest {

    public RegisterRequest(Activity activity, String email, String firstname, String lastname, String password) {
        String HttpURL = activity.getResources().getString(R.string.server_url) + "Register.php";

        // Build HashMap for build request with encrypted variables
        HashMap<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("firstname",firstname);
        params.put("lastname",lastname);
        params.put("password",password);

        new PostRequest(activity, HttpURL, params).exeRequest(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                Log.i("RegisterRequest", response.toString());
                RegisterActivity.getInstance().doAfterRegister(response);
            }
        });
    }
}
