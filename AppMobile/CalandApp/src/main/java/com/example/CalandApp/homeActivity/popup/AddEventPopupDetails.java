package com.example.CalandApp.homeActivity.popup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;

import java.util.ArrayList;
import java.util.Map;


public class AddEventPopupDetails extends Dialog {

    private Activity activity;

    private int date_year;
    private int date_month;
    private int date_day;
    private int time_hours;
    private int time_minutes;
    private int duration_hours;
    private int duration_minutes;

    private int group_id = 0;

    public AddEventPopupDetails(Activity activity, int date_year, int date_month, int date_day, int time_hours, int time_minutes, int duration_hours,  int duration_minutes){
        super(activity, R.style.Theme_Design);

        this.activity = activity;

        this.date_year = date_year;
        this.date_month = date_month;
        this.date_day = date_day;
        this.time_hours = time_hours;
        this.time_minutes = time_minutes;
        this.duration_hours = duration_hours;
        this.duration_minutes = duration_minutes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_add_event_details);

        TextView tv_group = (TextView) findViewById(R.id.tv_group);
        EditText et_description = (EditText) findViewById(R.id.et_description);
        Button bt_submit_event = (Button) findViewById(R.id.bt_submit_event);

        tv_group.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
                builderSingle.setTitle("Groups");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item);

                // Recover eventList from HomeActivity
                ArrayList<Map<String, Object>> groupList = ((HomeActivity) activity).getGroupList();

                // Build groupNameList as ListView if groupNameList different of null
                groupList.forEach(( groupListElement ) -> {
                    arrayAdapter.add((String) groupListElement.get("group_name"));
                });

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Set value of selected group into tv_group
                        String selectedGroupName = arrayAdapter.getItem(which);
                        tv_group.setText(selectedGroupName);

                        // Find group_id of element selectedGroupName into groupList
                        for (Map element : groupList){
                            if (element.get("group_name") == selectedGroupName){
                                group_id = (int) element.get("group_id");
                            }
                        }
                    }
                });
                builderSingle.show();
            }
        });

        bt_submit_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String moment = date_year + "-" + date_month + "-" + date_day + " " + time_hours + ":" + time_minutes + ":" + 00;
                String duration= duration_hours + ":" + duration_minutes + ":" + 00;
                String description_event = et_description.getText().toString();


                if (group_id == 0){
                    Toast.makeText(activity, "Please select a group", Toast.LENGTH_SHORT).show();
                } else if (description_event.equals("")){
                    Toast.makeText(activity, "Please put a description", Toast.LENGTH_SHORT).show();
                } else {

                    // Send event to server
                    ((HomeActivity) activity).addEvent(group_id, moment, duration, description_event);

                    AddEventPopupDetails.this.dismiss();

                }
            }
        });
    }
}
