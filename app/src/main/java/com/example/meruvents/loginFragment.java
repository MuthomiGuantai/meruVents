package com.example.meruvents;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meruvents.models.Users;
import com.example.meruvents.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class loginFragment extends Fragment {

    TextView btnlogin;
    EditText ed_email, ed_password;
    ProgressDialog loadingBar;
    CheckBox chkBoxRememberMe;
    String parentDbName = "Users";


    public loginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_login, container, false);

        btnlogin = view.findViewById(R.id.btnlogin);
        ed_email = view.findViewById(R.id.Ed_email);
        ed_password = view.findViewById(R.id.Ed_password);
        loadingBar = new ProgressDialog(getActivity());
        chkBoxRememberMe = (CheckBox) view.findViewById(R.id.remember_me_chkb);
        Paper.init(getActivity());


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Please Enter Your email", Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(password))) {
                    Toast.makeText(getActivity(), "Please Enter a Password", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("LogIn Account");
                    loadingBar.setMessage("Please wait, while we are checking your details");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccessToAccount(email, password);
                }

            }

            private void AllowAccessToAccount(final String phone, final String password) {
                if (chkBoxRememberMe.isChecked()) {
                    Paper.book().write(Prevalent.UserPhoneKey, phone);
                    Paper.book().write(Prevalent.UserPasswordKey, password);
                }


                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                            Users userdata = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                            if (userdata.getPhone().equals(phone)) {
                                if (userdata.getPassword().equals(password)) {
                                    if (parentDbName.equals("Users")) {
                                        Toast.makeText(getActivity(), "logged in successfully...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        Prevalent.currentonlineUser = userdata;
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(getActivity(), "Password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(), "Account with this" + phone + "phone doesn't exist", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Toast.makeText(getActivity(), "You need to create a new account.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });
        return view;


    }
}
