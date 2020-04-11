package com.example.meruvents.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.meruvents.MainActivity;
import com.example.meruvents.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class LoginOrSignUpLay_3 extends AppCompatActivity implements View.OnClickListener{
    TextView signup_fragment_tv,login_fragment_tv;
    FrameLayout fragment_container;
    CallbackManager callbackManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up_lay_3);



        signup_fragment_tv = findViewById(R.id.signup_fragment_tv);
        login_fragment_tv = findViewById(R.id.login_fragment_tv);
        fragment_container = findViewById(R.id.fragment_container);

        signup_fragment_tv.setTextColor(Color.parseColor("#00BCD4"));
        login_fragment_tv.setTextColor(Color.GRAY);
        login_fragment_tv.setTextSize(18);

        fragment = new SignUpFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

        signup_fragment_tv.setOnClickListener(this);
        login_fragment_tv.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_fragment_tv:

                signup_fragment_tv.setTextColor(Color.parseColor("#00BCD4"));
                login_fragment_tv.setTextColor(Color.GRAY);
                signup_fragment_tv.setTextSize(35);
                login_fragment_tv.setTextSize(18);

                fragment = new SignUpFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.commit();
                break;
            case R.id.login_fragment_tv:

                signup_fragment_tv.setTextColor(Color.GRAY);
                login_fragment_tv.setTextColor(Color.parseColor("#00BCD4"));
                login_fragment_tv.setTextSize(35);
                signup_fragment_tv.setTextSize(18);

                fragment = new loginFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
