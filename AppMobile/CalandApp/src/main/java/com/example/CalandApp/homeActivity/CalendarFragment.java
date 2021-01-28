package com.example.CalandApp.homeActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.CalandApp.homeActivity.popup.AddEventPopupTime;
import com.example.CalandApp.homeActivity.popup.GroupManagerPopup;
import com.example.CalandApp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// Source for calandar : https://github.com/Applandeo/Material-Calendar-View

public class CalendarFragment extends Fragment {

    private ImageView iv_manage_group;
    private CalendarView calendarView;
    private Button bt_add_event;
    private Date selectedDate;


    public  static CalendarFragment newInstance(){
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        super.onCreate(savedInstanceState);

        iv_manage_group = (ImageView) view.findViewById(R.id.iv_manage_group) ;
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        bt_add_event = (Button) view.findViewById(R.id.bt_add_event);

        iv_manage_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupManagerPopup groupManagerPopup = new GroupManagerPopup(getActivity());
                groupManagerPopup.show();
            }
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                // HightLight day on click on it
                Calendar clickedDayCalendar = eventDay.getCalendar();
                List<Calendar> selectedDates = calendarView.getSelectedDates();
                selectedDates.add(clickedDayCalendar);
                calendarView.setHighlightedDays(selectedDates);

                // Set selected date
                selectedDate = clickedDayCalendar.getTime();
            }
        });

        bt_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show popup for create new event
                if (selectedDate != null){
                    new AddEventPopupTime(getActivity(), selectedDate).show();
                } else{
                    new AddEventPopupTime(getActivity()).show();
                }
            }
        });

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void actualiseEventList() {

        ArrayList<Map<String, Object>> eventList = ((HomeActivity) getActivity()).getEventList();
        List<EventDay> events = new ArrayList<>();

        eventList.forEach(new Consumer<Map<String, Object>>() {
            @Override
            public void accept(Map<String, Object> stringObjectMap) {

                // Read date of event
                String event_moment = ((String) stringObjectMap.get("event_moment"));
                String date_event = event_moment.split(" ")[0]; // Select yyyy-MM-dd part for yyyy-MM-dd hh:mm:ss datetime

                // Create new Calendar instance and set it date of event
                Calendar calendar = Calendar.getInstance();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    calendar.setTime(df.parse(date_event));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Read value of group id for get color of event point
                int group_id = ((int) stringObjectMap.get("group_id"));
                String group_color = ((HomeActivity) getActivity()).getGroupColor(group_id);

                // Create new Drawable of color of group
                Drawable pointEvent = getResources().getDrawable(R.drawable.sample_circle).mutate();
                pointEvent.setColorFilter(Color.parseColor(group_color), PorterDuff.Mode.MULTIPLY);

                // Add event to list
                events.add(new EventDay(calendar, pointEvent));
            }
        });
        calendarView.setEvents(events);
    }
}

