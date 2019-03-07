package com.example.caoan.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.caoan.shop.Adapter.StoreAdapter;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Model.Store;
import com.example.caoan.shop.Model.StoreList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private Spinner spinner1, spinner2, spinner3;
    private ListView listView;
    private String[] tinh = {"Tinh/thanh pho", "Đà Nẵng", "Quảng Nam"};
    private String[] huyenDN = {"Quan/huyen", "Thanh Khê", "Liên Chiểu", "Hải Châu"};
    private String[] huyenQN = {"Quan/huyen", "Điện Bàn", "Đại Lộc", "Duy Xuyên"};
    private String[] xaDN1 = {"Xa/phuong", "Hòa Khê", "An Khê", "Vĩnh Trung"};
    private String[] xaDN2 = {"Xa/phuong", "Hòa Khánh Nam", "Hòa Khánh Nam", "Hòa Minh"};
    private String[] xaDN3 = {"Xa/phuong", "Hải Châu 1", "Hải Châu 2", "Hòa Cường Bắc","Hòa Cường Nam","Thuận Phước"};
    private String[] xaQN1 = {"Xa/phuong", "Điện Phước", "Điện Hồng", "Điện Thọ"};
    private String[] xaQN2 = {"Xa/phuong", "DD", "EE", "FF"};
    private String[] xaQN3 = {"Xa/phuong", "GG", "HH", "II"};
    private String[] xa, huyen;
    private List<Store> storeList;
    private List<Store> stores;
    private StoreAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button btsize, btdata;
    private ProgressBar progressBar;
    private DataCart data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        spinner1 = findViewById(R.id.spxa);
        spinner2 = findViewById(R.id.sphuyen);
        spinner3 = findViewById(R.id.sptinh);
        checkBox = findViewById(R.id.cbaddress);
        listView = findViewById(R.id.lvstore);
        btsize = findViewById(R.id.btsize);
        btdata = findViewById(R.id.btdata);
        progressBar = findViewById(R.id.progressstore);

        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);

        data = new DataCart(this);
        checkBox.setChecked(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    spinner3.setVisibility(View.VISIBLE);
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this, android.R.layout.simple_spinner_item, tinh);
                    spinner3.setAdapter(adapter);
                } else {
                    adapter.getFilter().filter(null);
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
                if (!s.equals("Tinh/thanh pho")) {
                    String t;
                    spinner2.setVisibility(View.VISIBLE);
                    if (s.equals("Đà Nẵng")) {
                        t = "Da Nang";
                        huyen = huyenDN;
                    } else {
                        t = "Quang Nam";
                        huyen = huyenQN;
                    }
                    spinner3.setTag(t);
//                    Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
                    adapter.getFilter().filter(t);
                    listView.invalidateViews();
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this, android.R.layout.simple_spinner_item, huyen);
                    spinner2.setAdapter(adapter);
                } else {
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
                if (!s.equals("Quan/huyen")) {
                    String h = null;
                    spinner1.setVisibility(View.VISIBLE);
                    if (s.equals("Thanh Khê")) {
                        h = "Thanh Khe";
                        xa = xaDN1;
                    }
                    if (s.equals("Liên Chiểu")) {
                        h = "Lien Chieu";
                        xa = xaDN2;
                    }
                    if (s.equals("Hải Châu")) {
                        h = "Hai Chau";
                        xa = xaDN3;
                    }
                    if (s.equals("Điện Bàn")) {
                        h = "Dien Ban";
                        xa = xaQN1;
                    }
                    if (s.equals("Đại Lộc")) {
                        h = "Dai Loc";
                        xa = xaQN2;
                    }
                    if (s.equals("Duy Xuyên")) {
                        h = "Duy Xuyen";
                        xa = xaQN3;
                    }
                    spinner2.setTag(h);
//                    Toast.makeText(getApplicationContext(), h, Toast.LENGTH_SHORT).show();
                    adapter.getFilter().filter(h);
                    listView.invalidateViews();
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this, android.R.layout.simple_spinner_item, xa);
                    spinner1.setAdapter(adapter);
                } else {
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
                if (!s.equals("Xa/phuong")) {
                    String t = (String) spinner3.getTag();
                    String h = (String) spinner2.getTag();
//                    Toast.makeText(getApplicationContext(), t + h + s, Toast.LENGTH_SHORT).show();
                    adapter.getFilter().filter(s);
                    listView.invalidateViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        storeList = new ArrayList<>();
        Load();

        btsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), String.valueOf(storeList.size()), Toast.LENGTH_SHORT).show();
                listView.invalidateViews();
            }
        });
        btdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stores = data.getStoreList();
                Toast.makeText(getApplicationContext(), String.valueOf(stores.size()), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Store store = (Store) adapterView.getItemAtPosition(i);
                SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key",store.getKey());
                editor.putString("key_master",store.getUserkey());
                editor.commit();
                startActivity(new Intent(StoreActivity.this, FirstActivity.class));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isOnline()){
            Toast.makeText(getApplicationContext(),"Bạn cần kết nối internet",Toast.LENGTH_SHORT).show();
        }

    }
    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                listView.invalidateViews();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                storeList.clear();
                Load();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fillStore() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Store");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Store store = snapshot.getValue(Store.class);
                    storeList.add(store);
                    //data.InsertStore(store);
                }
                progressBar.setVisibility(View.GONE);
                adapter = new StoreAdapter(getApplicationContext(),storeList);
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        System.out.println("Call stop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("Call destroy");
        super.onDestroy();
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    @Override
    protected void onRestart() {
        System.out.println("Call restart");
        super.onRestart();
    }

    class ProgressBarProcess extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPostExecute(String s) {
            //storeList = data.getStoreList();
            adapter = new StoreAdapter(StoreActivity.this, storeList);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Void... voids) {
            for (int i = 0; i < 100; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Done";
        }
    }
    public void Load(){
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        fillStore();
        //new ProgressBarProcess().execute();
    }

    /*public void API(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://shop-aa1b6.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FirebaseAPI firebaseAPI = retrofit.create(FirebaseAPI.class);
        Call<Store> listCall = firebaseAPI.listStore();
        listCall.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                storeList.add(response.body());
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                System.out.println("Error:" + t.getMessage());
            }
        });
        adapter = new StoreAdapter(this,storeList);
        listView.setAdapter(adapter);
        listView.invalidateViews();
    }*/
}
