package com.example.caoan.shop;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.caoan.shop.Adapter.OrderManagerAdapter;
import com.example.caoan.shop.FragmentComponent.AllOrderFragment;
import com.example.caoan.shop.FragmentComponent.ConfirmOrderFragment;
import com.example.caoan.shop.FragmentComponent.DeleteOrderFragmnet;
import com.example.caoan.shop.FragmentComponent.DeliveredOrderFragment;
import com.example.caoan.shop.FragmentComponent.TransportOrderFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderManagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private OrderManagerAdapter orderManagerAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        fillFragment();
        orderManagerAdapter = new OrderManagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(orderManagerAdapter);

        int i = intent.getIntExtra("tab",0);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillFragment() {
        fragmentList = new ArrayList<Fragment>();

        fragmentList.add(new AllOrderFragment());
        fragmentList.add(new ConfirmOrderFragment());
        fragmentList.add(new TransportOrderFragment());
        fragmentList.add(new DeliveredOrderFragment());
        fragmentList.add(new DeleteOrderFragmnet());
    }
}
