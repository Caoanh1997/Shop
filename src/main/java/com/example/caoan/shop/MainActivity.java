package com.example.caoan.shop;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import com.example.caoan.shop.Adapter.FoodAdapter;
import com.example.caoan.shop.Adapter.FragmentAdapter;
import com.example.caoan.shop.FragmentComponent.DrinkFragment;
import com.example.caoan.shop.FragmentComponent.FoodFragment;
import com.example.caoan.shop.FragmentComponent.PhoneFragment;
import com.example.caoan.shop.Model.Food;
import com.example.caoan.shop.ViewPageTransformer.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabHost tabHost;
    private TabHost.TabSpec tabSpec;
    private FragmentAdapter adapter;
    private List<Fragment> fragmentList;
    private ViewPager viewPager;
    private FoodAdapter foodAdapter;
    private FoodFragment foodFragment;
    private List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);

        initTabhost();
        fillFragment();

        adapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true,new DepthPageTransformer());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabHost.setCurrentTab(viewPager.getCurrentItem());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                viewPager.setCurrentItem(tabHost.getCurrentTab());
            }
        });

    }

    private void fillFragment() {
        fragmentList = new ArrayList<Fragment>();
        SharedPreferences sharedPreferences = getSharedPreferences("key_store",Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key","null");
        String userKey = sharedPreferences.getString("key_master","null");
        FoodFragment foodFragment = new FoodFragment().newInstance(key,userKey);
        DrinkFragment drinkFragment = new DrinkFragment().newInstance(key,userKey);

        fragmentList.add(foodFragment);
        fragmentList.add(drinkFragment);
    }


    private void initTabhost() {
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tabfood").setIndicator("Food")
                .setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String s) {
                        View view = new View(MainActivity.this);
                        return view;
                    }
                }));
        tabHost.addTab(tabHost.newTabSpec("tabdrink").setIndicator("Drink")
                .setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String s) {
                        View view = new View(MainActivity.this);
                        return view;
                    }
                }));
//        tabHost.addTab(tabHost.newTabSpec("taball").setIndicator("All")
//                .setContent(new TabHost.TabContentFactory() {
//                    @Override
//                    public View createTabContent(String s) {
//                        View view = new View(MainActivity.this);
//                        return view;
//                    }
//                }));
//        tabHost.addTab(tabHost.newTabSpec("tabdrink2").setIndicator("Drink2 haha")
//                .setContent(new TabHost.TabContentFactory() {
//                    @Override
//                    public View createTabContent(String s) {
//                        View view = new View(MainActivity.this);
//                        return view;
//                    }
//                }));
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

}
