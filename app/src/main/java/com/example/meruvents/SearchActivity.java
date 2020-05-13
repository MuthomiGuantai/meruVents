package com.example.meruvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meruvents.Fragments.EventsFragment;
import com.example.meruvents.Fragments.HomeFragment;
import com.example.meruvents.Fragments.ServiceFragment;
import com.facebook.CallbackManager;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    TextView event_fragment_tv,services_fragment_tv;
    FrameLayout fragment_container;
    CallbackManager callbackManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        event_fragment_tv = findViewById(R.id.events_fragment_tv);
        services_fragment_tv = findViewById(R.id.services_fragment_tv);
        fragment_container = findViewById(R.id.fragment_search);
        back = findViewById(R.id.goback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        services_fragment_tv.setTextColor(Color.parseColor("#000000"));
        event_fragment_tv.setTextColor(Color.GRAY);
        event_fragment_tv.setTextSize(15);

        fragment = new ServiceFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_search, fragment);
        fragmentTransaction.commit();

        services_fragment_tv.setOnClickListener(this);
        event_fragment_tv.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.services_fragment_tv:

                services_fragment_tv.setTextColor(Color.parseColor("#000000"));
                event_fragment_tv.setTextColor(Color.GRAY);
                services_fragment_tv.setTextSize(20);
                event_fragment_tv.setTextSize(15);

                fragment = new ServiceFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_search,fragment);
                fragmentTransaction.commit();
                break;
            case R.id.events_fragment_tv:

                services_fragment_tv.setTextColor(Color.GRAY);
                event_fragment_tv.setTextColor(Color.parseColor("#000000"));
                event_fragment_tv.setTextSize(20);
                services_fragment_tv.setTextSize(15);

                fragment = new EventsFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_search,fragment);
                fragmentTransaction.commit();
                break;
        }

    }
}
