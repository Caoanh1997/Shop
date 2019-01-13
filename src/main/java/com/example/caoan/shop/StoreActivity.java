package com.example.caoan.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.caoan.shop.Adapter.StoreAdapter;
import com.example.caoan.shop.Model.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private Spinner spinner1,spinner2,spinner3;
    private ListView listView;
    private String[] tinh = {"All","Da Nang","Quang Nam"};
    private String[] huyenDN = {"All","Thanh Khe","Lien Chieu","Hai Chau"};
    private String[] huyenQN = {"All","Dien Ban","Dai Loc","Duy Xuyen"};
    private String[] xaDN1 = {"All","A","B","C"};
    private String[] xaDN2 = {"All","D","E","F"};
    private String[] xaDN3 = {"All","G","H","I"};
    private String[] xaQN1 = {"All","AA","BB","CC"};
    private String[] xaQN2 = {"All","DD","EE","FF"};
    private String[] xaQN3 = {"All","GG","HH","II"};
    private String[] xa,huyen;
    private List<Store> storeList;
    private StoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        spinner1 = findViewById(R.id.spxa);
        spinner2 = findViewById(R.id.sphuyen);
        spinner3 = findViewById(R.id.sptinh);
        checkBox = findViewById(R.id.cbaddress);
        listView = findViewById(R.id.lvstore);

        checkBox.setChecked(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox.isChecked()){
                    spinner3.setVisibility(View.VISIBLE);
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this,android.R.layout.simple_spinner_item,tinh);
                    spinner3.setAdapter(adapter);
                }else {
                    spinner3.setVisibility(View.GONE);
                    spinner2.setVisibility(View.GONE);
                    spinner1.setVisibility(View.GONE);
                }
            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                if(!s.equals("All")){
                    String t;
                    spinner2.setVisibility(View.VISIBLE);
                    if(s.equals("Da Nang")){
                        t = "Da Nang";
                        huyen = huyenDN;
                    }else {
                        t = "Quang Nam";
                        huyen = huyenQN;
                    }
                    spinner3.setTag(t);
                    Toast.makeText(getApplicationContext(),t,Toast.LENGTH_SHORT).show();
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this,android.R.layout.simple_spinner_item,huyen);
                    spinner2.setAdapter(adapter);
                }else {
                    spinner2.setVisibility(View.GONE);
                    spinner1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                if(!s.equals("All")){
                    String h = null;
                    spinner1.setVisibility(View.VISIBLE);
                    if(s.equals("Thanh Khe")){
                        h = "Thanh Khe";
                        xa = xaDN1;
                    }
                    if(s.equals("Lien Chieu")){
                        h = "Lien Chieu";
                        xa = xaDN2;
                    }
                    if(s.equals("Hai Chau")){
                        h = "Hai Chau";
                        xa = xaDN3;
                    }
                    if(s.equals("Dien Ban")){
                        h = "Dien Ban";
                        xa = xaQN1;
                    }
                    if(s.equals("Dai Loc")){
                        h = "Dai Loc";
                        xa = xaQN2;
                    }
                    if(s.equals("Duy Xuyen")){
                        h = "Duy Xuyen";
                        xa = xaQN3;
                    }
                    spinner2.setTag(h);
                    Toast.makeText(getApplicationContext(),h,Toast.LENGTH_SHORT).show();
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this,android.R.layout.simple_spinner_item,xa);
                    spinner1.setAdapter(adapter);
                }else {
                    spinner1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                if(!s.equals("All")){
                    String t = (String) spinner3.getTag();
                    String h = (String) spinner2.getTag();
                    Toast.makeText(getApplicationContext(),t+h+s,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fillStore();
        adapter = new StoreAdapter(this,storeList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    public void fillStore(){
        storeList = new ArrayList<>();

        Store store = new Store("Store A","Dien Bien Phu","Hoa Khe","Thanh Khe","Da Nang"
                ,"wnyMYkzqWxgGwf5D7AsJ7TV6b1v2","https://www.kob.com/kobtvimages/repository/2018-05/sears.jpg"
                ,"0123456789");
        for (int i=0;i<10;i++){
            storeList.add(store);
        }
    }


}
