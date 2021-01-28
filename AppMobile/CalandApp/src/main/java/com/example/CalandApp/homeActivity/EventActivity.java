package com.example.CalandApp.homeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CalandApp.R;
import com.example.CalandApp.requestDB.LocalSQLiteDB.SQLiteEventHelper;
import com.example.CalandApp.requestDB.ServerMySQLDB.DelEventRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventActivity extends AppCompatActivity {

    private Intent previousIntent;
    private int event_id;
    private String organizer_name;
    private String group_name;
    private String date_event;
    private String hour_event;
    private String duration_event;
    private String description;
    private int organizer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();

        this.event_id = intent.getIntExtra("event_id", 0);
        this.organizer_name = intent.getStringExtra("organizer_name");
        this.group_name = intent.getStringExtra("group_name");
        this.date_event = intent.getStringExtra("date_event");
        this.date_event = intent.getStringExtra("date_event");
        this.hour_event = intent.getStringExtra("hour_event");
        this.duration_event = intent.getStringExtra("duration_event");
        this.description = intent.getStringExtra("description");
        this.organizer_id = intent.getIntExtra("organizer_id",0);


        // Find related view object inside the itemView.
        TextView tv_date_event = (TextView) findViewById(R.id.tv_date_event);
        TextView tv_hour_event = (TextView) findViewById(R.id.tv_hour_event);
        TextView tv_duration_event = (TextView) findViewById(R.id.tv_duration_event);
        TextView tv_description = (TextView) findViewById(R.id.tv_description);
        TextView tv_organizer = (TextView) findViewById(R.id.tv_organizer);
        TextView tv_group = (TextView) findViewById(R.id.tv_group);
        Button bt_del_event = (Button) findViewById(R.id.bt_dell_event);

        // Set text
        tv_date_event.setText(date_event);
        tv_hour_event.setText("At " + hour_event);
        tv_duration_event.setText("During "+duration_event);
        tv_description.setText(description);
        tv_organizer.setText("Created by " + organizer_name);
        tv_group.setText("Including " + group_name);

        bt_del_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DelEventRequest(EventActivity.this, event_id);
            }
        });
    }

    public void setPreviousIntent(Intent previousIntent) {
        this.previousIntent = previousIntent;
    }

    public void doAfterDelEvent(JSONObject response) throws JSONException {
        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            // Say it's ok
            Log.i("EventActivity", "Event " + event_id + " deleted");
            Toast.makeText(EventActivity.this, "Event " + event_id + " deleted", Toast.LENGTH_SHORT).show();

            finish();

        } else {
            // Tell error
            Log.i("EventActivity", "Error event suppression");
            Toast.makeText(EventActivity.this, "Error during event suppression\n", Toast.LENGTH_SHORT).show();
        }
    }
}