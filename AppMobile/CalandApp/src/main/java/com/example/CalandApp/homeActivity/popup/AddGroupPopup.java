package com.example.CalandApp.homeActivity.popup;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.CalandApp.R;
import com.example.CalandApp.homeActivity.HomeActivity;
import com.example.CalandApp.requestDB.ServerMySQLDB.AddGroupRequest;


public class AddGroupPopup extends Dialog {

    private Activity activity;

    private Button bt_add_group;
    private EditText et_group_name;


    public AddGroupPopup(Activity activity){
        super(activity, R.style.Theme_Design);

        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_add_group);

        EditText et_group_name = (EditText) findViewById(R.id.et_group_name);
        Button bt_add_group = (Button) findViewById(R.id.bt_add_group);

        bt_add_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((HomeActivity) activity).addGroup(et_group_name.getText().toString());

                AddGroupPopup.this.dismiss();
            }
        });
    }
}
