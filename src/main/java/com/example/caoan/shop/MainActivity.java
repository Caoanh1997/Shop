package com.example.caoan.shop;


import android.content.Context;
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

        FoodFragment foodFragment = new FoodFragment().newInstance("Food haha");
        DrinkFragment drinkFragment = new DrinkFragment().newInstance("Drink haha");
        //PhoneFragment phoneFragment = new PhoneFragment().newInstance("Phone haha");
        //DrinkFragment drinkFragment1 = new DrinkFragment().newInstance("Drink haha");

        fragmentList.add(foodFragment);
        fragmentList.add(drinkFragment);
        //fragmentList.add(phoneFragment);
        //fragmentList.add(drinkFragment1);
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

    /*private class ProgressBarAsync extends AsyncTask<Void,Integer,String>{

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=0;i<=100;i++){
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"Load completed",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }*/
    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

}
