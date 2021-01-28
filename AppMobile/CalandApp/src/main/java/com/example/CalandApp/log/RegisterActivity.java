package com.example.CalandApp.log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.CalandApp.requestDB.ServerMySQLDB.AddGroupRequest;
import com.example.CalandApp.requestDB.ServerMySQLDB.LinkUserGroupRequest;
import com.example.CalandApp.requestDB.ServerMySQLDB.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private static RegisterActivity instance;
    private Intent intent;

    private int user_id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private int group_id;

    private ProgressDialog[] progressDialog = new ProgressDialog[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        intent = getIntent();
        setContentView(R.layout.activity_register);

        // Graphics elements
        ImageView iv_backArrow = (ImageView) findViewById(R.id.iv_backArrow);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        EditText et_email = (EditText) findViewById(R.id.et_email);
        EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
        EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
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
        btn_register.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if(et_email.getText().toString().equals("") || et_password.getText().toString().equals("")
                        || et_firstname.getText().toString().equals("") || et_lastname.getText().toString().equals("")){

                    Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                } else {
                    // Get data before register
                    email = et_email.getText().toString();
                    firstname = et_firstname.getText().toString();
                    lastname = et_lastname.getText().toString();
                    password = et_password.getText().toString();

                    // Create a pupup for indicate waiting
                    progressDialog[0] = ProgressDialog.show(RegisterActivity.this,"Loading Data",null,true,true);

                    // Send request to server for register
                    new RegisterRequest(RegisterActivity.this, email, firstname,lastname,password);

                }
            }
        });
    }

    // For interact with class from other class
    public static RegisterActivity getInstance() {
        return instance;
    }

    public void doAfterRegister(JSONObject response) throws JSONException {

        // If request successful
        if (response.getBoolean("success"))
        {
            // Get data from response of server
            user_id = response.getInt("user_id");
            String group_name = "perso_uid"+String.valueOf(user_id);

            // Say it's ok
            Log.i("RegisterActivity", "New profile created");

            // Send request for create new group
            new AddGroupRequest(RegisterActivity.this, "RegisterActivity", group_name);

        } else {
            // stop loading data popup
            progressDialog[0].dismiss();

            // Tell problem
            Log.i("RegisterActivity", "Error during user creation");
            Toast.makeText(RegisterActivity.this, "Error during user creation", Toast.LENGTH_SHORT).show();
        }
    }

    public void doAfterCreateGroup(JSONObject response ) throws JSONException {

        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            // Get data from response of server
            group_id = response.getInt("group_id");

            // Say it's ok
            Log.i("RegisterActivity", "New group created");
            Toast.makeText(RegisterActivity.this, "New group created", Toast.LENGTH_SHORT).show();

            // Send request for link new group with user
            new LinkUserGroupRequest(RegisterActivity.this, "RegisterActivity", user_id, group_id);
        } else {
            // stop loading data popup
            progressDialog[0].dismiss();

            // Tell problem
            Log.i("RegisterActivity", "Error during group creation");
            Toast.makeText(RegisterActivity.this, "Error during group creation", Toast.LENGTH_SHORT).show();
        }
    }

    public void doAfterLinkGroup(JSONObject response ) throws JSONException {

        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            // Say it's ok
            Log.i("RegisterActivity", "User and group linked");
            Toast.makeText(RegisterActivity.this, "User linked to group", Toast.LENGTH_SHORT).show();


            // Create a notification for say Welcome
            NotificationCompat.Builder builder = new NotificationCompat.Builder(RegisterActivity.this);

            builder.setSmallIcon(R.drawable.logo);
            builder.setContentTitle("Hi " + firstname + " from CalandApp");
            builder.setContentText("Welcome to CalandApp");
            builder.setAutoCancel(true);
            builder.setVibrate(new long[]{400, 400});
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);


            Intent[] intentTab = new Intent[1] ;
            intentTab[0] = intent;

            PendingIntent pendingIntent = PendingIntent.getActivities(RegisterActivity.this, 0,intentTab, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager =  (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.notify(0, builder.build());


            // Go to HomeActivity
            goHomeActivity(user_id, email, firstname, lastname);
        } else {
            // Tell error
            Log.i("RegisterActivity", "Error during linkage between user and group");
            Toast.makeText(RegisterActivity.this, "Error during linkage between user and group", Toast.LENGTH_SHORT).show();
        }
        // stop loading data popup
        progressDialog[0].dismiss();
    }


    public void goBackIntent(){
        // Go back
        this.finish();
    }


    public void goHomeActivity(int user_id, String email, String firstname, String lastname){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("email", email);
        intent.putExtra("firstname", firstname);
        intent.putExtra("lastname", lastname);
        startActivity(intent);
    }

}