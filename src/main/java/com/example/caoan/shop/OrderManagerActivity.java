package com.example.caoan.shop;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.caoan.shop.Adapter.OrderManagerAdapter;
import com.example.caoan.shop.FragmentComponent.AllOrderFragment;
import com.example.caoan.shop.FragmentComponent.ConfirmOrderFragment;
import com.example.caoan.shop.FragmentComponent.DeliveredOrderFragment;
import com.example.caoan.shop.FragmentComponent.TransportOrderFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderManagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private OrderManagerAdapter orderManagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        fillFragment();
        orderManagerAdapter = new OrderManagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(orderManagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void fillFragment() {
        fragmentList = new ArrayList<Fragment>();

        fragmentList.add(new AllOrderFragment());
        fragmentList.add(new ConfirmOrderFragment());
        fragmentList.add(new TransportOrderFragment());
        fragmentList.add(new DeliveredOrderFragment());
    }
}
