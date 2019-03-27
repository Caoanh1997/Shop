package com.example.caoan.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.caoan.shop.FragmentComponent.AccountFragmentFix;
import com.example.caoan.shop.FragmentComponent.CartFragment;
import com.example.caoan.shop.FragmentComponent.HomeFragment;
import com.example.caoan.shop.FragmentComponent.OrderManagerFragment;
import com.example.caoan.shop.FragmentComponent.SettingFragment;
import com.example.caoan.shop.Model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBarActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private BottomNavigationView navigationView;
    private String key, userKey;
    private List<Fragment> listhide;
    private Fragment active;
    private FirebaseDatabase firebaseDatabase;
    private boolean doubleClickBackPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final Intent intent = getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("key", "null");
        userKey = sharedPreferences.getString("key_master", "null");

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Store");


        navigationView = findViewById(R.id.navigation);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        final Fragment fragment1 = new HomeFragment().newInstance(key, userKey);
        fragmentManager.beginTransaction().add(R.id.frame_container, fragment1, "home").commit();
        listhide = new ArrayList<Fragment>();
        active = fragment1;
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home) {
                    if (active != fragment1) {

                        fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    } else {

                        fragmentManager.beginTransaction().show(fragment1).commit();
                    }
                    active = fragment1;
                }
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.cart:
                        fragmentManager.beginTransaction().hide(fragment1).commit();
                        if (active != fragment1) {
                            fragmentManager.beginTransaction().remove(active).commit();
                        }
                        fragment = new CartFragment().newInstance(key, userKey);
                        loadFragment(fragment, fragmentManager);
                        //actionBar.setTitle("Cart");
                        active = fragment;

                        break;
                    case R.id.account:
                        fragmentManager.beginTransaction().hide(fragment1).commit();
                        if (active != fragment1) {
                            fragmentManager.beginTransaction().remove(active).commit();
                        }
                        if (user != null) {
                            fragment = new OrderManagerFragment();
                        } else {
                            fragment = new AccountFragmentFix();
                        }
                        loadFragment(fragment, fragmentManager);
                        //actionBar.setTitle("Account");
                        active = fragment;

                        break;
                    case R.id.setting:
                        fragmentManager.beginTransaction().hide(fragment1).commit();
                        if (active != fragment1) {
                            fragmentManager.beginTransaction().remove(active).commit();
                        }
                        fragment = new SettingFragment();
                        loadFragment(fragment, fragmentManager);
                        //actionBar.setTitle("Setting");
                        active = fragment;

                        break;
                }
                return true;
            }
        });
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.child(key).getValue(Store.class);
                actionBar.setTitle(store.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        boolean b = intent.getBooleanExtra("login", false);
        if (b) {
            navigationView.setSelectedItemId(R.id.account);
        } else {
            navigationView.setSelectedItemId(R.id.home);
        }
        // attaching bottom sheet behaviour - hide / show on scroll
        //CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationView.getLayoutParams();
        //layoutParams.setBehavior(new BottomNavigationBehavior());
    }

    private void loadFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void hideFragment(List<Fragment> fragmentList, FragmentManager fragmentManager) {
        for (Fragment fragment : fragmentList) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "Back", Toast.LENGTH_SHORT).show();
                //onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleClickBackPress) {
            /*Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);*/
            finish();
        }
        Toast.makeText(getApplicationContext(), "Please click BACK again to Store", Toast.LENGTH_SHORT).show();
        this.doubleClickBackPress = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleClickBackPress = false;
            }
        }, 5000);

    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
