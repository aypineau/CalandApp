package com.example.CalandApp.homeActivity.popup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.CalandApp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddEventPopupTime extends Dialog {

    final Calendar c = Calendar.getInstance();

    private int date_year =c.get(Calendar.YEAR);
    private int date_month=c.get(Calendar.MONTH);
    private int date_day =c.get(Calendar.DAY_OF_MONTH);
    private int time_hours =c.get(Calendar.HOUR_OF_DAY);
    private int time_minutes =c.get(Calendar.MINUTE);
    private int duration_hours =0;
    private int duration_minutes =0;

    // Default date displayed
    private String str_date = "00/00/0000";


    // Let know if information are grasped
    private boolean isSetDate = false;
    private boolean isSetTime = false;
    private boolean isSetDuration = false;


    private Activity activity;

    public AddEventPopupTime(Activity activity){
        super(activity, R.style.Theme_Design);

        this.activity = activity;
    }

    public AddEventPopupTime(Activity activity, Date date){
        super(activity, R.style.Theme_Design);

        // Format date_event into into dd/MM/yyyy
        str_date = new SimpleDateFormat("dd/MM/yyyy").format(date);

        date_day = Integer.parseInt( str_date.split("/")[0]); ;
        date_month = Integer.parseInt(str_date.split("/")[1]);
        date_year = Integer.parseInt(str_date.split("/")[2]);

        isSetDate = true;

        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_add_event_time);

        TextView tv_date = (TextView) findViewById(R.id.tv_date);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);
        TextView tv_duration = (TextView) findViewById(R.id.tv_duration);
        Button bt_next = (Button) findViewById(R.id.bt_next);

        tv_date.setText(str_date);

        tv_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get Current Date
                date_year = c.get(Calendar.YEAR);
                date_month = c.get(Calendar.MONTH);
                date_day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                date_year = year;
                                date_month = monthOfYear + 1;
                                date_day = dayOfMonth;
                                isSetDate=true;

                                String str_date = "00/00/1000";
                                // Format date_event into into dd/MM/yyyy
                                try {
                                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    str_date = new SimpleDateFormat("dd/MM/yyyy").format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                tv_date.setText(str_date);

                            }
                        }, date_year, date_month, date_day);
                datePickerDialog.show();
            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Initialise hour and minutes
                        time_hours = hourOfDay;
                        time_minutes = minute;
                        isSetTime = true;

                        // Initialise calandar
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,time_hours,time_minutes);
                        // Set selected time on text view
                        tv_time.setText(DateFormat.format("HH:mm", calendar));
                    }
                },24,0, true);
                // Displayed previous selected time
                timePickerDialog.updateTime(time_hours,time_minutes);
                // Show dialog
                timePickerDialog.show();
            }
        });

        tv_duration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Initialize hour and minutes
                        duration_hours = hourOfDay;
                        duration_minutes = minute;
                        isSetDuration = true;

                        // Initialise calandar
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,duration_hours,duration_minutes);
                        // Set selected time on text view
                        tv_duration.setText(DateFormat.format("HH:mm", calendar));

                    }
                },24, 24,true);
                timePickerDialog.updateTime(duration_hours,duration_minutes);
                // Show dialog
                timePickerDialog.show();
            }
        });


        bt_next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (!isSetDate && !isSetTime || !isSetDate && !isSetDuration || !isSetTime && !isSetDuration ){
                    Toast.makeText(activity, "Please set a moment for event", Toast.LENGTH_SHORT).show();
                } else if (!isSetDate){
                    Toast.makeText(activity, "Please select a date", Toast.LENGTH_SHORT).show();
                } else if (!isSetTime ){
                    Toast.makeText(activity, "Please enter a time", Toast.LENGTH_SHORT).show();
                } else if (!isSetDuration){
                    Toast.makeText(activity, "Please enter a duration", Toast.LENGTH_SHORT).show();
                } else{
                    AddEventPopupDetails addEventPopupDetails = new AddEventPopupDetails(
                            activity,
                            date_year,
                            date_month,
                            date_day,
                            time_hours,
                            time_minutes,
                            duration_hours,
                            duration_minutes
                            );
                    addEventPopupDetails.show();
                    AddEventPopupTime.this.hide();
                }
            }
        });

    }
}
