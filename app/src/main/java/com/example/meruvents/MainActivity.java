package com.example.meruvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.meruvents.Fragments.AccountFragment;
import com.example.meruvents.Fragments.CreateFragment;
import com.example.meruvents.Fragments.DiscoverFragment;
import com.example.meruvents.Fragments.HomeFragment;
import com.example.meruvents.Fragments.NotificationsFragment;


public class MainActivity extends AppCompatActivity {
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
    }
}
