package com.example.meruvents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;


public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation meo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        meo=(MeowBottomNavigation)findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1,R.drawable.ic_action_home));
        meo.add(new MeowBottomNavigation.Model(2,R.drawable.ic_action_discover));
        meo.add(new MeowBottomNavigation.Model(3,R.drawable.ic_action_create));
        meo.add(new MeowBottomNavigation.Model(4,R.drawable.ic_action_notifications));
        meo.add(new MeowBottomNavigation.Model(5,R.drawable.ic_action_account));
    }
}
