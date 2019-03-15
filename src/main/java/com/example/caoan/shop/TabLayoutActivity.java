package com.example.caoan.shop;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.caoan.shop.Adapter.FragmentAdapter;
import com.example.caoan.shop.FragmentComponent.DrinkFragment;
import com.example.caoan.shop.FragmentComponent.FoodFragment;
import com.example.caoan.shop.ViewPageTransformer.CubeInScalingTransformation;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        mViewPager = (ViewPager) findViewById(R.id.container);
        fillFragment();
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setPageTransformer(true,new CubeInScalingTransformation());
        mViewPager.setAdapter(fragmentAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
    private void fillFragment() {
        fragmentList = new ArrayList<Fragment>();
        SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key","null");
        String userKey = sharedPreferences.getString("key_master","null");
        FoodFragment foodFragment = new FoodFragment().newInstance(key,userKey);
        DrinkFragment drinkFragment = new DrinkFragment().newInstance(key,userKey);

        fragmentList.add(foodFragment);
        fragmentList.add(drinkFragment);
    }
}
