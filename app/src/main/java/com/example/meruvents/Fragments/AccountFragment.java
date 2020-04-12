package com.example.meruvents.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meruvents.Auth.LoginOrSignUpLay_3;
import com.example.meruvents.MainActivity;
import com.example.meruvents.R;
import com.example.meruvents.models.Users;
import com.example.meruvents.prevalent.Prevalent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    TextView tv_logout,tv_username, tv_email;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_account, container, false);
        tv_logout = view.findViewById(R.id.log_out);
        tv_username = view.findViewById(R.id.username);
        tv_email = view.findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();


        if (Prevalent.currentonlineUser != null){
            tv_username.setText(Prevalent.currentonlineUser.getName());
            tv_email.setText(Prevalent.currentonlineUser.getEmail());
        }


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            tv_email.setText(acct.getEmail());
            tv_username.setText(acct.getDisplayName());
        }



        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                Intent intent = new Intent(getActivity(), LoginOrSignUpLay_3.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

     return view;


    }


}
