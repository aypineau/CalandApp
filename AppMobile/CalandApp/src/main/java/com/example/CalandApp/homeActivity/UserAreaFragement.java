package com.example.CalandApp.homeActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.CalandApp.R;
import com.example.CalandApp.log.MainActivity;
import com.example.CalandApp.homeActivity.popup.SettingPopup;

public class UserAreaFragement extends Fragment{

    private String email;
    private String firstname;
    private String lastname;
    private int user_id;

    private TextView tv_prenom_nom ;
    private TextView tv_email;
    private ImageView iv_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_area, container, false);

        email = ((HomeActivity) getActivity()).getEmail();
        firstname =((HomeActivity) getActivity()).getFirstname();
        lastname = ((HomeActivity) getActivity()).getLastname();

        tv_prenom_nom = (TextView) view.findViewById(R.id.tv_prenom_nom);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting) ;

        // Set value of fields
        tv_prenom_nom.setText(firstname + " " + lastname);
        tv_email.setText("email : " + email);

        // Darkmode detection
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Set logo in function of darkmode
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            iv_setting.setColorFilter(Color.WHITE);
        }

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingPopup settingPopup = new SettingPopup(getActivity());
                settingPopup.show();
            }
        });

        return view;
    }


}
