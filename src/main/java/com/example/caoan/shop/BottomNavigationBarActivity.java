package com.example.caoan.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.caoan.shop.FragmentComponent.AccountFragment;
import com.example.caoan.shop.FragmentComponent.CartFragment;
import com.example.caoan.shop.FragmentComponent.DrinkFragment;
import com.example.caoan.shop.FragmentComponent.FoodFragment;
import com.example.caoan.shop.FragmentComponent.HomeFragment;
import com.example.caoan.shop.FragmentComponent.OrderManagerFragment;
import com.example.caoan.shop.FragmentComponent.SettingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBarActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private BottomNavigationView navigationView;
    private String key, userKey;
    private List<Fragment> listhide;
    private Fragment active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final Intent intent = getIntent();


        SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("key","null");
        userKey = sharedPreferences.getString("key_master","null");

        final Fragment fragment1 = new HomeFragment().newInstance(key,userKey);
//        final Fragment fragment2 = new CartFragment().newInstance(key,userKey);
//        final Fragment fragment3 = new OrderManagerFragment();
//        final Fragment fragment4 = new AccountFragment();
//        final Fragment fragment5 = new SettingFragment();
        navigationView = findViewById(R.id.navigation);
        final FragmentManager fragmentManager = getSupportFragmentManager();

//        fragmentManager.beginTransaction().add(R.id.frame_container,fragment5,"setting").commit();
//        fragmentManager.beginTransaction().add(R.id.frame_container,fragment4,"account").commit();
//        fragmentManager.beginTransaction().add(R.id.frame_container,fragment3,"order").commit();
//        fragmentManager.beginTransaction().add(R.id.frame_container,fragment2,"cart").commit();
        fragmentManager.beginTransaction().add(R.id.frame_container,fragment1,"home").commit();
        listhide = new ArrayList<Fragment>();
        active = fragment1;
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                /*switch (item.getItemId()){
                    case R.id.home:
                        actionBar.setTitle("Home");
                        fragment = fragment1;
                        //fragment = new FoodFragment().newInstance(key,userKey);
                        loadFragment(fragment,"home");
                        break;
                        //return true;*/
                if (item.getItemId() == R.id.home) {
                    if (active != fragment1) {
                        System.out.println("active != home");
                        fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                        //listhide.clear();
//                        listhide.add(fragment2);
//                        listhide.add(fragment3);
//                        listhide.add(fragment4);
//                        listhide.add(fragment5);
                        //hideFragment(listhide, fragmentManager);
                    } else {
                        System.out.println("active == home");
                        fragmentManager.beginTransaction().show(fragment1).commit();
                    }
                    active = fragment1;
                    actionBar.setTitle("Home");
                }
                /*if(item.getItemId()==R.id.cart) {
                    if (active != fragment2) {
                        fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                        listhide.clear();
                        listhide.add(fragment1);
                        listhide.add(fragment3);
                        listhide.add(fragment4);
                        listhide.add(fragment5);
                        hideFragment(listhide, fragmentManager);
                    } else {
                        fragmentManager.beginTransaction().show(fragment2).commit();
                    }
                    active = fragment2;
                    actionBar.setTitle("Cart");
                }
                if(item.getItemId()==R.id.account) {
                    if(user == null){
                        if (active != fragment4) {
                            fragmentManager.beginTransaction().hide(active).show(fragment4).commit();
                            listhide.clear();
                            listhide.add(fragment1);
                            listhide.add(fragment2);
                            listhide.add(fragment3);
                            listhide.add(fragment5);
                            hideFragment(listhide, fragmentManager);
                        } else {
                            fragmentManager.beginTransaction().show(fragment4).commit();
                        }
                        active = fragment4;
                    }else {
                        if (active != fragment3) {
                            fragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                            listhide.clear();
                            listhide.add(fragment1);
                            listhide.add(fragment2);
                            listhide.add(fragment4);
                            listhide.add(fragment5);
                            hideFragment(listhide, fragmentManager);
                        } else {
                            fragmentManager.beginTransaction().show(fragment3).commit();
                        }
                        active = fragment3;
                    }
                    actionBar.setTitle("Account");
                }
                if(item.getItemId()==R.id.setting) {
                    if (active != fragment5) {
                        fragmentManager.beginTransaction().hide(active).show(fragment5).commit();
                        listhide.clear();
                        listhide.add(fragment1);
                        listhide.add(fragment2);
                        listhide.add(fragment3);
                        listhide.add(fragment4);
                        hideFragment(listhide, fragmentManager);
                    } else {
                        fragmentManager.beginTransaction().show(fragment5).commit();
                    }
                    active = fragment5;
                    actionBar.setTitle("Setting");
                }*/
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.cart:
                        fragmentManager.beginTransaction().hide(fragment1).commit();
                        if(active != fragment1){
                            fragmentManager.beginTransaction().remove(active).commit();
                        }
                        fragment = new CartFragment().newInstance(key,userKey);
                        loadFragment(fragment,fragmentManager);
                        actionBar.setTitle("Cart");
                        active = fragment;
                        System.out.println("active = cart");
                        break;
                    case R.id.account:
                        fragmentManager.beginTransaction().hide(fragment1).commit();
                        if(active != fragment1){
                            fragmentManager.beginTransaction().remove(active).commit();
                        }
                        if(user != null){
                            fragment = new OrderManagerFragment();
                        }else {
                            fragment = new AccountFragment();
                        }
                        loadFragment(fragment,fragmentManager);
                        actionBar.setTitle("Account");
                        active = fragment;
                        System.out.println("active = account");
                        break;
                    case R.id.setting:
                        fragmentManager.beginTransaction().hide(fragment1).commit();
                        if(active != fragment1){
                            fragmentManager.beginTransaction().remove(active).commit();
                        }
                        fragment = new SettingFragment();
                        loadFragment(fragment,fragmentManager);
                        actionBar.setTitle("Setting");
                        active = fragment;
                        System.out.println("active = setting");
                        break;
                }
                return true;
            }
        });
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Home");

        boolean b = intent.getBooleanExtra("login",false);
        if (b){
            navigationView.setSelectedItemId(R.id.account);
        }else {
            navigationView.setSelectedItemId(R.id.home);
        }
        // attaching bottom sheet behaviour - hide / show on scroll
        //CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationView.getLayoutParams();
        //layoutParams.setBehavior(new BottomNavigationBehavior());
    }

    private void loadFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void hideFragment(List<Fragment> fragmentList, FragmentManager fragmentManager){
        for (Fragment fragment : fragmentList){
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
                //onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
