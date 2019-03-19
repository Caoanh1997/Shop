package com.example.caoan.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    private Button btchangestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btchangestore = findViewById(R.id.btchangestore);

        btchangestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,StoreActivity.class));
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
//        System.out.println("Setting pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        System.out.println("Setting resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("Setting stop");
    }
}
