package com.example.CalandApp.log;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;
import com.example.CalandApp.requestDB.ServerMySQLDB.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static LoginActivity instance;

    private int user_id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;

    private ProgressDialog[] progressDialog = new ProgressDialog[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);

        // Graphics elements
        ImageView iv_backArrow = (ImageView) findViewById(R.id.iv_backArrow);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        EditText et_email = (EditText) findViewById(R.id.et_email);
        EditText et_password = (EditText) findViewById(R.id.et_password);


        // Darkmode detection
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Set Layout for register interface on click on button
        iv_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackIntent();
            }
        });

        // Set logo in function of darkmode
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            iv_backArrow.setColorFilter(Color.WHITE);
        } else {
            iv_backArrow.setColorFilter(Color.BLACK);
        }


        // Button for apply registration
        btn_login.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if(et_email.getText().toString().equals("") || et_password.getText().toString().equals("")){

                    Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                } else {

                    email = et_email.getText().toString();
                    password = et_password.getText().toString();

                    // Create a pupup for indicate waiting
                    progressDialog[0] = ProgressDialog.show(LoginActivity.this,"Loading Data",null,true,true);

                    // Call async function for prepare html request to server
                    new LoginRequest(LoginActivity.this, email, password);

                }
            }
        });

    }

    // For interact with class from other class
    public static LoginActivity getInstance() {
        return instance;
    }


    public void doAfterLogin(JSONObject response) throws JSONException {

        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            user_id = response.getInt("user_id");
            firstname = response.getString("firstname");
            lastname = response.getString("lastname");

            goUserAreaActivity(user_id, email, firstname, lastname);
            Log.i("LoginActivity", "Logged");
        } else {
            Log.i("LoginActivity", "Error during authentication");
            Toast.makeText(LoginActivity.this, "Incorrect login or password", Toast.LENGTH_SHORT).show();
        }

        // stop loading data popup
        progressDialog[0].dismiss();
    }

    public void goBackIntent(){
        // Go back
        this.finish();
    }


    public void goUserAreaActivity(int user_id, String email, String firstname, String lastname){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", email);
        intent.putExtra("firstname", firstname);
        intent.putExtra("lastname", lastname);
        startActivity(intent);
    }

}