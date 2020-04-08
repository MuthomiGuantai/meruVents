package com.example.meruvents;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class loginFragment extends Fragment {

    TextView btnlogin;
    EditText ed_email,ed_password;


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

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }


}
