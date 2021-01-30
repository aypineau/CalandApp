package com.example.CalandApp.homeActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.CalandApp.homeActivity.popup.AddEventPopupTime;
import com.example.CalandApp.homeActivity.popup.GroupManagerPopup;
import com.example.CalandApp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

public class EventListingFragement extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private ImageView iv_manage_group;
    private Button bt_add_event;
    private ListView lv_group_list;
    private ListView lv_event_list;
    private TextView tv_no_event_yet;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_listring, container, false);

        super.onCreate(savedInstanceState);

        // Declare element for interaction
        iv_manage_group = (ImageView) view.findViewById(R.id.iv_manage_group);
        bt_add_event = (Button) view.findViewById(R.id.bt_add_event);
        lv_event_list = (ListView) view.findViewById(R.id.lv_event_list);
        lv_group_list = (ListView) view.findViewById(R.id.lv_group_list);
        tv_no_event_yet = (TextView) view.findViewById(R.id.tv_no_event_yet);

        actualiseEventList();

        iv_manage_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show popup to see group list
                new GroupManagerPopup(getActivity()).show();
            }
        });

        bt_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show popup for create new event
                new AddEventPopupTime(getActivity()).show();
            }
        });


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void actualiseEventList(){

        // Recover eventList from HomeActivity
        ArrayList<Map<String, Object>> eventList = ((HomeActivity) getActivity()).getEventList();
        ArrayList<Map<String, Object>> futurEventList = new ArrayList<Map<String, Object>>();
        ArrayList<Map<String, Object>> groupList = ((HomeActivity) getActivity()).getGroupList();

        eventList.forEach(new Consumer<Map<String, Object>>() {
            @Override
            public void accept(Map<String, Object> stringObjectMap) {
                String event_moment = ((String) stringObjectMap.get("event_moment"));
                if(!eventIsOver(event_moment)){
                    futurEventList.add(stringObjectMap);
                }
            }
        });

        if (futurEventList.size() > 0){
            // Hide tv_no_event_yet
            tv_no_event_yet.setVisibility(View.INVISIBLE);
            lv_event_list.setVisibility(View.VISIBLE);


            // Create a BaseAdapter for group list
            BaseAdapter GroupBaseAdapter = new BaseAdapter() {
                // Return list view item count.
                @Override
                public int getCount() {
                    // Return number of elements of groupList
                    return groupList.size();
                }

                @Override
                public boolean isEnabled(int position) {
                    return false;
                }

                @Override
                public Object getItem(int i) {
                    return null;
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public View getView(int itemIndex, View itemView, ViewGroup viewGroup) {

                    if(itemView == null) {
                        // First inflate the RelativeView object.
                        itemView = LayoutInflater.from(getActivity()).inflate(R.layout.group_lv_base_adapter, null);
                    }

                    // Find related view object inside the itemView.
                    TextView tv_group = (TextView)itemView.findViewById(R.id.tv_group);
                    Spinner sp_group_color = (Spinner)itemView.findViewById(R.id.sp_group_color);

                    // Build String date_event and hour_event
                    int group_id = (int) ((Map) groupList.get(itemIndex)).get("group_id");
                    String group_name = (String) ((Map) groupList.get(itemIndex)).get("group_name");
                    String color_group = ((HomeActivity) getActivity()).getGroupColor(group_id);

                    tv_group.setText(group_name);
                    sp_group_color.setBackgroundColor(Color.parseColor(color_group));

                    return itemView;
                }
            };

            // Create a BaseAdapter for event list
            BaseAdapter EventBaseAdapter = new BaseAdapter() {
                // Return list view item count.
                @Override
                public int getCount() {
                    // Return number of elements of eventList
                    return futurEventList.size();
                }

                @Override
                public Object getItem(int i) {
                    return null;
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public View getView(int itemIndex, View itemView, ViewGroup viewGroup) {

                    if(itemView == null) {
                        // First inflate the RelativeView object.
                        itemView = LayoutInflater.from(getActivity()).inflate(R.layout.event_lv_base_adapter, null);
                    }

                    // Find related view object inside the itemView.
                    TextView tv_date_event = (TextView)itemView.findViewById(R.id.tv_date_event);
                    TextView tv_hour_event = (TextView)itemView.findViewById(R.id.tv_hour_event);
                    TextView tv_duration_event = (TextView)itemView.findViewById(R.id.tv_duration_event);
                    TextView tv_description = (TextView)itemView.findViewById(R.id.tv_description);
                    TextView tv_organizer = (TextView)itemView.findViewById(R.id.tv_organizer);
                    Spinner sp_group_color = (Spinner)itemView.findViewById(R.id.sp_group_color);


                    // Prepare data
                    int group_id = (int) ((Map) futurEventList.get(itemIndex)).get("group_id");
                    int organizer_id = (int) ((Map) futurEventList.get(itemIndex)).get("organizer_id");
                    String organizer_name =  ((HomeActivity) getActivity()).getUserName(organizer_id);
                    String event_moment = ((String) ((Map) futurEventList.get(itemIndex)).get("event_moment"));
                    String date_event = event_moment.split(" ")[0]; // Select yyyy-MM-dd part for yyyy-MM-dd hh:mm:ss datetime
                    String hour_event = (event_moment.split(" ")[1]).substring(0, 5); // substring for remove second of string
                    String duration_event = ((String) ((Map) futurEventList.get(itemIndex)).get("event_duration")).substring(0, 5); // substring for remove second of string
                    String description = (String) ((Map) futurEventList.get(itemIndex)).get("description");
                    String color_group = ((HomeActivity) getActivity()).getGroupColor(group_id);


                    // Format date_event into into dd/MM/yyyy
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(date_event);
                        date_event = new SimpleDateFormat("dd/MM/yyyy").format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Set text
                    tv_date_event.setText(date_event);
                    tv_hour_event.setText("At " + hour_event);
                    tv_duration_event.setText("During "+duration_event);
                    tv_description.setText(description);
                    tv_organizer.setText("Created by " + organizer_name );
                    sp_group_color.setBackgroundColor(Color.parseColor(color_group));

                    return itemView;
                }
            };

            // Set the custom base adapter to it.
            lv_group_list.setAdapter(GroupBaseAdapter);
            lv_event_list.setAdapter(EventBaseAdapter);

            lv_event_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                    // Prepare data
                    int event_id = (int) ((Map) eventList.get(index)).get("event_id");
                    int organizer_id = (int) ((Map) eventList.get(index)).get("organizer_id");
                    int group_id = (int) ((Map) eventList.get(index)).get("group_id");
                    String event_moment = ((String) ((Map) eventList.get(index)).get("event_moment"));
                    String date_event = event_moment.split(" ")[0]; // Select yyyy-MM-dd part for yyyy-MM-dd hh:mm:ss datetime
                    String hour_event = (event_moment.split(" ")[1]).substring(0, 5); // substring for remove second of string
                    String duration_event = ((String) ((Map) eventList.get(index)).get("event_duration")).substring(0, 5); // substring for remove second of string
                    String description = (String) ((Map) eventList.get(index)).get("description");


                    // Format date_event into into dd/MM/yyyy
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(date_event);
                        date_event = new SimpleDateFormat("dd/MM/yyyy").format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ((HomeActivity) getActivity()).goEventActivity(event_id, organizer_id, group_id, date_event,hour_event, duration_event,description);

                }
            });
        } else {
            // Hide lv event
            lv_event_list.setVisibility(View.INVISIBLE);
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_time.setText( hourOfDay + ":" + minute);
    }

    private boolean eventIsOver(String event_moment) {
        // Actual moment for compare if the event is over
        String now = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(Calendar.getInstance().getTime());

        // Show event in list if the event has not happened
        if (event_moment.compareTo(now) >= 0) {
            return false;
        } else {
            return true;
        }
    }
}



