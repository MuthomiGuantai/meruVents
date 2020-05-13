package com.example.meruvents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.meruvents.Fragments.AccountFragment;
import com.example.meruvents.Fragments.CreateFragment;
import com.example.meruvents.Fragments.DiscoverFragment;
import com.example.meruvents.Fragments.HomeFragment;
import com.example.meruvents.Fragments.NotificationsFragment;
import com.example.meruvents.models.Token;
import com.example.meruvents.models.Users;
import com.example.meruvents.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {
    public static final String NODE_USERS = "users";
    private FirebaseAuth mAuth;
    MeowBottomNavigation meo;
    private final static int ID_HOME=1;
    private final static int ID_DISCOVER=2;
    private final static int ID_CREATE=3;
    private final static int ID_NOTIFICATIONS=4;
    private final static int ID_ACCOUNT=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        meo=(MeowBottomNavigation)findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1,R.drawable.ic_action_home));
        meo.add(new MeowBottomNavigation.Model(2,R.drawable.ic_action_discover));
        meo.add(new MeowBottomNavigation.Model(3,R.drawable.ic_action_create));
        meo.add(new MeowBottomNavigation.Model(4,R.drawable.ic_action_notifications));
        meo.add(new MeowBottomNavigation.Model(5,R.drawable.ic_action_account));

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new HomeFragment()).commit();

        meo.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });
        meo.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment select_fragment=null;
                switch (item.getId()){
                    case ID_HOME:
                        select_fragment=new HomeFragment();
                        break;
                    case ID_DISCOVER:
                        select_fragment=new DiscoverFragment();
                        break;
                    case ID_CREATE:
                        select_fragment=new CreateFragment();
                        break;
                    case ID_NOTIFICATIONS:
                        select_fragment=new NotificationsFragment();
                        break;
                    case ID_ACCOUNT:
                        select_fragment=new AccountFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, select_fragment).commit();
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("updates");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String token = task.getResult().getToken();
                        saveToken(token);
                    }

                    private void saveToken(String token) {
                        String email = Prevalent.currentonlineUser.getEmail();
                        Token token1 = new Token(email, token);

                        DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference(NODE_USERS);
                        dbusers.child("token")
                                .setValue(token1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
    }
}
