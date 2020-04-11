package com.example.meruvents.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meruvents.MainActivity;
import com.example.meruvents.R;
import com.example.meruvents.models.Users;
import com.example.meruvents.prevalent.Prevalent;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginOrSignUpLay_3 extends AppCompatActivity implements View.OnClickListener{
    TextView signup_fragment_tv,login_fragment_tv;
    FrameLayout fragment_container;
    CallbackManager callbackManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up_lay_3);

        loadingBar = new ProgressDialog(this);
        Paper.init(this);


        signup_fragment_tv = findViewById(R.id.signup_fragment_tv);
        login_fragment_tv = findViewById(R.id.login_fragment_tv);
        fragment_container = findViewById(R.id.fragment_container);

        signup_fragment_tv.setTextColor(Color.parseColor("#00BCD4"));
        login_fragment_tv.setTextColor(Color.GRAY);
        login_fragment_tv.setTextSize(18);

        fragment = new SignUpFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        signup_fragment_tv.setOnClickListener(this);
        login_fragment_tv.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

        private void AllowAccess(final String phone, final String password)
        {

            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child("Users").child(phone).exists())
                    {
                        Users userdata = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                        if (userdata.getPhone().equals(phone))
                        {
                            if (userdata.getPassword().equals(password))
                            {
                                Toast.makeText(LoginOrSignUpLay_3.this, "Please wait you are logged in already...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginOrSignUpLay_3.this, MainActivity.class);
                                Prevalent.currentonlineUser = userdata;
                                startActivity(intent);
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(LoginOrSignUpLay_3.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginOrSignUpLay_3.this, "Account with this" + phone + "number doesn't exist", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        //Toast.makeText(LoginActivity.this, "You need to create a new account.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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
