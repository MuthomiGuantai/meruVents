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

import com.example.meruvents.Activities.Otp_Verification_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    TextView btnlogin;
    EditText ed_email, ed_username, ed_phone, ed_password;
    ProgressDialog loadingBar;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_sign_up, container, false);

        btnlogin = view.findViewById(R.id.btnsignup);
        ed_email = view.findViewById(R.id.Ed_email);
        ed_username = view.findViewById(R.id.Ed_username);
        ed_phone = view.findViewById(R.id.Ed_phone);
        ed_password = view.findViewById(R.id.Ed_password);
        loadingBar = new ProgressDialog(getActivity());


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_email.getText().toString();
                String username = ed_username.getText().toString();
                String phone = ed_phone.getText().toString();
                String password = ed_password.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getActivity(), "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(email))) {
                    Toast.makeText(getActivity(), "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(phone))) {
                    Toast.makeText(getActivity(), "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(password))) {
                    Toast.makeText(getActivity(), "Please Enter a Password", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait, while we are checking your details");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    ValidatePhoneNumber(username, email, phone, password);
                }

            }

            private void ValidatePhoneNumber(final String name, final String email, final String phone, final String password) {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!(dataSnapshot.child("Users").child(phone).exists())) {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("phone", phone);
                            userdataMap.put("password", password);
                            userdataMap.put("name", name);
                            userdataMap.put("email", email);

                            RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();

                                                Intent intent = new Intent(getActivity(), Otp_Verification_Activity.class);
                                                startActivity(intent);
                                            } else {
                                                loadingBar.dismiss();
                                                Toast.makeText(getActivity(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "This" + phone + "already exists", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "Please try again using another phone number", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
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
