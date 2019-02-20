package com.example.caoan.shop;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.caoan.shop.FragmentComponent.FoodFragment;

public class FirstActivity extends TabActivity {

    private TabHost tabHost;
    private TabHost.TabSpec tabSpec;
    private View view;
    private FoodFragment foodFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        tabHost = findViewById(android.R.id.tabhost);

        tabSpec = tabHost.newTabSpec("tab1");
        view = getLayoutInflater().inflate(R.layout.shop_tabhost_layout,null);
        tabSpec.setIndicator(view);
        //tabSpec.setIndicator("Shop",getDrawable(R.drawable.ic_home));
        tabSpec.setContent(new Intent(FirstActivity.this,MainActivity.class));
        tabHost.addTab(tabSpec);

        view = getLayoutInflater().inflate(R.layout.cart_tabhost_layout,null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(view)
                .setContent(new Intent(FirstActivity.this,CartActivity.class)));
        view = getLayoutInflater().inflate(R.layout.account_tabhost_layout,null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(view)
                .setContent(new Intent(FirstActivity.this,LoginActivity.class)));
        view = getLayoutInflater().inflate(R.layout.setting_tabhost_layout,null);
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(view)
        .setContent(new Intent(FirstActivity.this,SettingActivity.class)));

    }

}
