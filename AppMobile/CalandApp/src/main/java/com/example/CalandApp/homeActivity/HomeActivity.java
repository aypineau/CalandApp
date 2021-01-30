package com.example.CalandApp.homeActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.CalandApp.R;
import com.example.CalandApp.requestDB.LocalSQLiteDB.SQLiteEventHelper;
import com.example.CalandApp.requestDB.ServerMySQLDB.AddEventRequest;
import com.example.CalandApp.requestDB.ServerMySQLDB.AddGroupRequest;
import com.example.CalandApp.requestDB.ServerMySQLDB.GetEventRequest;
import com.example.CalandApp.requestDB.ServerMySQLDB.GetGroupRequest;
import com.example.CalandApp.requestDB.ServerMySQLDB.GetUserRequest;
import com.example.CalandApp.requestDB.ServerMySQLDB.LinkUserGroupRequest;
import com.example.CalandApp.requestDB.LocalSQLiteDB.SQLiteGroupHelper;
import com.example.CalandApp.requestDB.ServerMySQLDB.UnlinkUserGroupRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private String email;
    private String firstname;
    private String lastname;
    private int user_id;

    private ArrayList<Map<String,Object>> groupList = new ArrayList<Map<String,Object>>();
    private Map<Integer, String> groupName = new HashMap<Integer, String>();
    private Map<Integer,String> groupColor = new HashMap<Integer,String>();
    private Map<Integer, String> userName = new HashMap<Integer, String>();
    private ArrayList<Map<String,Object>> eventList = new ArrayList<Map<String,Object>>();
    private EventListingFragement eventListingFragement;
    private CalendarFragment calendarFragment;

    // Create class for local group DB interaction
    private SQLiteGroupHelper sqlLiteGroupDB = new SQLiteGroupHelper(HomeActivity.this);
    // Create class for local event DB interaction
    private SQLiteEventHelper sqlLiteEventDB = new SQLiteEventHelper(HomeActivity.this);


    private ViewPager viewPager;

    private ProgressDialog[] progressDialog = new ProgressDialog[1];

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager);
        FragmentPagerAdapter adapterViewPager = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);

        intent = getIntent();

        this.email = intent.getStringExtra("email");
        this.firstname = intent.getStringExtra("firstname");
        this.lastname = intent.getStringExtra("lastname");
        this.user_id = intent.getIntExtra("user_id", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get group and actualize list with server request
        initGroup();
        initEvent();
        getUser();
        getGroup();
    }

    @Override
    public void onBackPressed() {
        // Disable goback arrow pressing and got EventListing
        viewPager.setCurrentItem(1);
    }

    public void goEventActivity(int event_id, int organizer_id, int group_id, String date_event,String hour_event, String duration_event,String description){
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("event_id", event_id);
        intent.putExtra("organizer_name", getUserName(user_id));
        intent.putExtra("group_name", getGroupName(group_id));
        intent.putExtra("date_event", date_event);
        intent.putExtra("hour_event", hour_event);
        intent.putExtra("duration_event", duration_event);
        intent.putExtra("description", description);
        startActivity(intent);

    }


    // Page adapter allow to manage swiping between fragments
    public class PageAdapter extends FragmentPagerAdapter{

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    // UserArea fragment
                    return new UserAreaFragement();
                case 1:
                    // EventListing fragment
                    return eventListingFragement = new EventListingFragement();
                case 2:
                    // EventListing fragment
                    return calendarFragment = new CalendarFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // return number of fragment who make up PageAdapter
            return 3;
        }
    }



    // Requests for interact with local and distant DB
    public void initGroup(){
        // Clear Lists
        groupList.clear();
        eventList.clear();

        // Cursor allow to target one row of DB
        Cursor cursor = sqlLiteGroupDB.readAllData();

        // For each line in DB
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){

                // Get value of group
                int group_id = cursor.getInt(0);
                String group_name = cursor.getString(1);
                String group_color = String.format("#%06x", new Random().nextInt(0xffffff + 1));
                

                // Set values in groupDetails Map
                Map<String,Object> groupDetails = new HashMap<String,Object>();
                groupDetails.put("group_id", group_id);
                groupDetails.put("group_name", group_name);
                groupDetails.put("group_color", group_color );

                // For save data during activity opened
                groupColor.put(group_id, group_color);
                groupName.put(group_id, group_name);


                // add groupDetails Map int groupList ArrayList
                groupList.add(groupDetails);
            }
        }
    }

    public void getGroup(){
        // Clear Lists
        groupList.clear();
        eventList.clear();

        // Send request for getGroup from server
        new GetGroupRequest(HomeActivity.this, user_id);
    }

    public void doAfterGetGroup(JSONObject response ) throws JSONException{
        // log only if login indicate true
        if (response.getBoolean("success")){

            // Clean database
            sqlLiteGroupDB.deleteAllData();

            // Clean groupList
            groupList.clear();

            // Clean groupList
            eventList.clear();

            // For each group send from
            for ( int i = 1 ;  i < response.length() ; i++){
                JSONObject jOBJ_group = response.getJSONObject(String.valueOf(i));

                // Get value of group
                int group_id = jOBJ_group.getInt("group_id");
                String group_name = jOBJ_group.getString("group_name");

                // If group group_name is perso_uidXX, change for personal group
                if(group_name.equals("perso_uid"+user_id)){
                    group_name="Personal group";
                }

                // Add data to local DB
                sqlLiteGroupDB.addGroup(group_id,group_name);

                // Set values in groupDetails Map
                Map<String,Object> groupDetails = new HashMap<String,Object>();
                groupDetails.put("group_id", group_id);
                groupDetails.put("group_color", groupColor.get("group_id"));
                groupDetails.put("group_name", group_name);


                // add groupDetails Map int groupList ArrayList
                groupList.add(groupDetails);

                getEvent(group_id);
            }

            // Say it's ok
            Log.i("HomeActivity", "User group correctly received");

        } else {
            // Tell error
            Log.i("RegisterActivity", "Error during get user group");
            Toast.makeText(HomeActivity.this, "Error during get user group", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUser(){
        // Send request for getUser from server
        new GetUserRequest(HomeActivity.this);
    }

    public void doAfterGetUser(JSONObject response) throws JSONException {
        // log only if login indicate true
        if (response.getBoolean("success")){


            // For each group send from
            for ( int i = 1 ;  i < response.length() ; i++){
                JSONObject jOBJ_group = response.getJSONObject(String.valueOf(i));

                // Get value of group
                int user_id = jOBJ_group.getInt("user_id");
                String firstname = jOBJ_group.getString("firstname");
                String lastname = jOBJ_group.getString("lastname");
                String user_name = firstname + " " + lastname;

                userName.put(user_id,user_name);
            }
        } else {
            // Tell error
            Log.i("RegisterActivity", "Error during get userList");
            Toast.makeText(HomeActivity.this, "Error during get userList", Toast.LENGTH_SHORT).show();
        }
    }

    public void initEvent(){

        Cursor cursor = sqlLiteEventDB.readAllData();

        // For each line in DB
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){

                // Get value of event
                int event_id = cursor.getInt(0);
                int organizer_id = cursor.getInt(1);
                int group_id_cursor = cursor.getInt(2);
                String event_moment = cursor.getString(3);
                String event_duration = cursor.getString(4);
                String description = cursor.getString(5);

                // Set values in groupDetails Map
                Map<String,Object> eventDetails = new HashMap<String,Object>();
                eventDetails.put("event_id", event_id);
                eventDetails.put("organizer_id", organizer_id);
                eventDetails.put("group_id", group_id_cursor);
                eventDetails.put("event_moment", event_moment);
                eventDetails.put("event_duration", event_duration);
                eventDetails.put("description", description);

                // add groupDetails Map int groupList ArrayList
                eventList.add(eventDetails);
            }
        }
    }

    public void getEvent(int group_id){

        Log.i("HomeActivity", "Get event of group_id:" + String.valueOf(group_id));
        new GetEventRequest(HomeActivity.this, group_id);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void doAfterGetEvent(JSONObject response ) throws JSONException{

        // Clean database
        sqlLiteEventDB.deleteAllData();

        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            for ( int i = 1 ;  i < response.length() ; i++){
                JSONObject jOBJ_group = response.getJSONObject(String.valueOf(i));

                // Get values of JSONObject received
                int event_id = jOBJ_group.getInt("event_id");
                int organizer_id = jOBJ_group.getInt("organizer_id");
                int group_id = jOBJ_group.getInt("group_id");
                String event_moment = jOBJ_group.getString("event_moment");
                String event_duration = jOBJ_group.getString("event_duration");
                String description = jOBJ_group.getString("description");

                // Add data to local DB
                sqlLiteEventDB.addEvent(event_id,organizer_id,group_id,event_moment,event_duration,description);

                // Map value in eventDetails
                Map<String,Object> eventDetails = new HashMap<String,Object>();
                eventDetails.put("event_id", event_id);
                eventDetails.put("organizer_id", organizer_id);
                eventDetails.put("group_id", group_id);
                eventDetails.put("event_moment", event_moment);
                eventDetails.put("event_duration", event_duration);
                eventDetails.put("description", description);

                // Add eventDetails Map in eventList ArrayList
                eventList.add(eventDetails);
            }

            // Actualize event list from fragments
            eventListingFragement.actualiseEventList();
            calendarFragment.actualiseEventList();


            Log.i("HomeActivity", "Event listing correctly received");

        } else {
            Log.i("HomeActivity", "Error during event listing reception, or no events for group");
        }

        // Actualize event list from fragments
        eventListingFragement.actualiseEventList();
        calendarFragment.actualiseEventList();
    }

    public void addGroup(String group_name){
        new AddGroupRequest(HomeActivity.this, "HomeActivity", group_name);

        // Add data to local DB
        sqlLiteGroupDB.addGroup(group_name);
    }

    public void doAfterAddGroup(JSONObject response ) throws JSONException {

        // Login only if login indicate true
        if (response.getBoolean("success"))
        {
            // Get values of JSONObject received
            int group_id = response.getInt("group_id");

            // Say it's ok
            Log.i("RegisterActivity", "New group created");
            Toast.makeText(HomeActivity.this, "New group created", Toast.LENGTH_SHORT).show();

            linkGroup(group_id);
        } else {

            // Tell error
            Log.i("HomeActivity", "Error during group creation");
            Toast.makeText(HomeActivity.this, "Error during group creation", Toast.LENGTH_SHORT).show();
        }

    }

    public void linkGroup(int group_id){
        // Send request for link new group to user
        new LinkUserGroupRequest(HomeActivity.this, "HomeActivity", user_id, group_id);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void doAfterLinkGroup(JSONObject response ) throws JSONException {

        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            // Actualise groups
            getGroup();

            // Say it's ok
            Log.i("HomeActivity", "User and group linked");
        } else {
            // Tell error
            Log.i("HomeActivity", "Error during linkage between user and group");
            Toast.makeText(HomeActivity.this, "Error during linkage between user and group", Toast.LENGTH_SHORT).show();
        }

        // Actualize event list from fragement
        eventListingFragement.actualiseEventList();
        calendarFragment.actualiseEventList();
    }

    public void unlinkGroup(int group_id){
        new UnlinkUserGroupRequest( HomeActivity.this, user_id, group_id);
    }

    public void doAfterUnlinkGroup(JSONObject response ) throws JSONException {

        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            // Actualise groups
            getGroup();
        } else {
            // Tell error
            Log.i("HomeActivity", "Error during unlinkage of user and group");
            Toast.makeText(HomeActivity.this, "Error during unlinkage of user and group", Toast.LENGTH_SHORT).show();
        }
    }

    public void addEvent(int group_id, String date, String duration, String description_event){

        // Add data to local DB
        sqlLiteEventDB.addEvent(user_id,group_id,date,duration,description_event);

        // Send event to server
        new AddEventRequest(HomeActivity.this, user_id, group_id, date, duration, description_event );

        getGroup();
    }

    public void doAfterAddEvent(JSONObject response) throws JSONException {

        // log only if login indicate true
        if (response.getBoolean("success"))
        {
            int event_id = response.getInt("event_id");

            // Say it's ok
            Log.i("HomeActivity", "Event " + event_id + " created");
            Toast.makeText(HomeActivity.this, "Event " + event_id + " created", Toast.LENGTH_SHORT).show();

        } else {
            // Tell error
            Log.i("HomeActivity", "Error during event creation");
            Toast.makeText(HomeActivity.this, "Error during event creation", Toast.LENGTH_SHORT).show();
        }
    }

    // For get values of activity
    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public ArrayList<Map<String, Object>> getGroupList() {
        return groupList;
    }

    public ArrayList<Map<String, Object>> getEventList() {
        // Sort eventList by event_moment and return it
        Collections.sort(eventList, new MapComparator("event_moment"));
        return eventList;
    }

    public String getGroupColor(int group_id) {
        // Return group color if exit, black color else
        if (groupColor.containsKey(group_id)){
            return groupColor.get(group_id);
        } else{
            // Prevent error by create new color
            String group_color = String.format("#%06x", new Random().nextInt(0xffffff + 1));

            groupColor.put(group_id, group_color);
            return group_color;
        }
    }

    public String getGroupName(int group_id) {
        // Return group color if exit, black color else
        if (groupName.containsKey(group_id)){
            return groupName.get(group_id);
        } else{
            // Prevent error
            return null;
        }
    }

    public String getUserName(int user_id) {
        // Return group color if exit, black color else
        if (userName.containsKey(user_id)){
            return userName.get(user_id);
        } else{
            // Prevent error
            return null;
        }
    }

}
