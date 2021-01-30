package com.example.CalandApp.homeActivity.popup;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;
import com.example.CalandApp.requestDB.ServerMySQLDB.UnlinkUserGroupRequest;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;


public class GroupManagerPopup extends Dialog {

    Activity activity;

    ArrayList<Map<String, Object>> groupList;
    ArrayList<String> groupNameList ;
    ArrayList<String> selectedGroupList;

    public GroupManagerPopup(Activity activity){
        super(activity, R.style.Theme_Design);


        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_group_manager);

        // Declare element for interaction
        Button bt_del_group = (Button) findViewById(R.id.bt_del_group);
        Button bt_add_group = (Button) findViewById(R.id.bt_add_group);
        ListView lv_group = (ListView) findViewById(R.id.lv_group);

        // Recover eventList from HomeActivity
        groupList = ((HomeActivity) activity).getGroupList();

        // Structure group_name list for show in ListView
        groupNameList = new ArrayList<>();

        // List of groups selected
        selectedGroupList = new ArrayList<String>();

        // Clear groupNameList
        groupNameList.clear();

        // Recover eventList from HomeActivity
        groupList = ((HomeActivity) activity).getGroupList();

        // Build groupNameList as ListView if groupNameList different of null
        groupList.forEach(( groupListElement ) -> {
            groupNameList.add((String) groupListElement.get("group_name"));

        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_multiple_choice, groupNameList);
        lv_group.setAdapter(arrayAdapter);


        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);

                if(selectedGroupList.contains(clickItemObj.toString())){
                    selectedGroupList.remove(clickItemObj.toString());
                } else{
                    selectedGroupList.add(clickItemObj.toString());
                }
            }
        });

        bt_del_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGroupList.size() == 0) {
                    Toast.makeText(activity, "Please select  group to leave", Toast.LENGTH_SHORT).show();
                } else if (selectedGroupList.size() == groupList.size()) {
                    Toast.makeText(activity, "You can't leave all groups", Toast.LENGTH_SHORT).show();
                } else {
                    // For each element of selectedGroupList
                    selectedGroupList.forEach(new Consumer<String>() {
                        @Override
                        public void accept(String nameGroup) {
                            // For each element of groupList
                            groupList.forEach(new Consumer<Map<String, Object>>() {
                                @Override
                                public void accept(Map<String, Object> groupMap) {
                                    if (groupMap.get("group_name") == nameGroup ){

                                        if (groupMap.get("group_name") == "Personal group"){

                                            Toast.makeText(activity, "You can't leave your personal groups", Toast.LENGTH_SHORT).show();

                                        } else {

                                            // Set value of group_id
                                            int group_id = (int) groupMap.get("group_id");

                                            // Send request for unlink group
                                            ((HomeActivity) activity).unlinkGroup(group_id);

                                            // Close this popup
                                            GroupManagerPopup.this.dismiss();
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        bt_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // New popup for add group
                new AddGroupPopup(activity).show();
                dismiss();
            }
        });

    }
}
