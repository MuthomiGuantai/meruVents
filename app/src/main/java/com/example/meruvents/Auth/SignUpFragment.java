package com.example.meruvents.Auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meruvents.Auth.Otp_Verification_Activity;
import com.example.meruvents.MainActivity;
import com.example.meruvents.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

import static com.facebook.AccessTokenManager.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private static final String EMAIL = "email";

    TextView btnlogin;
    EditText ed_email, ed_username, ed_phone, ed_password;
    ProgressDialog loadingBar;
    CallbackManager callbackManager;
    LoginButton loginButton;
    GoogleSignInClient mGoogleSignInClient;
    final static int RC_SIGN_IN = 123;
    FirebaseAuth mAuth;


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


        FacebookSdk.sdkInitialize(getActivity());

        mAuth = FirebaseAuth.getInstance();

        createRequest();
        view.findViewById(R.id.google_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i(TAG, "LOGIN SUCCESFUL");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "LOGIN CANCEL");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i(TAG, "LOGIN ERROR");
            }
        });


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
                    Intent intent = new Intent(getActivity(),Otp_Verification_Activity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
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

                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void createRequest() {


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(getActivity(), "Sorry auth failed.", Toast.LENGTH_SHORT).show();


                        }


                        // ...
                    }
                });
    }
}