package com.example.CalandApp.homeActivity.popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.CalandApp.R;
import com.example.CalandApp.log.MainActivity;

public class SettingPopup extends Dialog {

    private Activity activity;

    public SettingPopup(Activity activity){
        super(activity, R.style.Theme_Design);
        setContentView(R.layout.popup_setting);

        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv_settings = (TextView) findViewById(R.id.tv_settings);
        Button bt_disconnect = (Button) findViewById(R.id.bt_disconnection);

        bt_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnMainActivity();
            }
        });
    }

    public void returnMainActivity(){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
