package com.example.caoan.shop;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.caoan.shop.Adapter.StoreRecycleViewAdapter;
import com.example.caoan.shop.BroadcastReceiver.DataChangeBroadcast;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Interface.ItemTouchListenerStore;
import com.example.caoan.shop.Model.Store;
import com.example.caoan.shop.Service.CheckDataChangeFirebase;
import com.example.caoan.shop.Service.CheckNetwork;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private Spinner spxa, sphuyen, sptinh;
    private String[] xa, huyen;
    private List<Store> storeList;
    private List<Store> stores;
    private StoreRecycleViewAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DataCart data;
    private TextView tvstore;
    private RecyclerView rcvstore;
    private ShimmerRecyclerView shimmerRecyclerView;
    private DataChangeBroadcast dataChangeBroadcast;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean doubleClickBackPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        spxa = findViewById(R.id.spxa);
        sphuyen = findViewById(R.id.sphuyen);
        sptinh = findViewById(R.id.sptinh);
        checkBox = findViewById(R.id.cbaddress);
        tvstore = findViewById(R.id.tvstore);
        rcvstore = findViewById(R.id.rcvstore);
        shimmerRecyclerView = findViewById(R.id.shimmer_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //dataChangeBroadcast = new DataChangeBroadcast();
        //IntentFilter intentFilter = new IntentFilter("dataChange.Broadcast");
        //registerReceiver(dataChangeBroadcast,intentFilter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallbackStore(new ItemTouchListenerStore() {
            @Override
            public void onSwipe(int position, int direction) {
                adapter.Swipe(position, direction);
            }

            @Override
            public void onMove(int oldPostion, int newPosition) {
                adapter.Move(oldPostion, newPosition);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rcvstore);

        YoYo.with(Techniques.RollIn).duration(2000).playOn(tvstore);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeList.clear();
                Load();
            }
        });

        data = new DataCart(this);
        checkBox.setChecked(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    sptinh.setVisibility(View.VISIBLE);
                    sptinh.setAdapter(new ArrayAdapter(StoreActivity.this, android.R.layout.simple_spinner_item,
                            getResources().getStringArray(R.array.tinh)));
                } else {
                    adapter.getFilter().filter(null);
                    sptinh.setVisibility(View.GONE);
                    sphuyen.setVisibility(View.GONE);
                    spxa.setVisibility(View.GONE);
                }
            }
        });

        sptinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tinh = (String) adapterView.getItemAtPosition(i);
                if (!tinh.equals("Tỉnh/thành phố")) {
                    sphuyen.setVisibility(View.VISIBLE);
                    if (tinh.equals("Đà Nẵng")) {
                        huyen = getResources().getStringArray(R.array.huyenDN);
                    } else {
                        huyen = getResources().getStringArray(R.array.huyenQN);
                    }
                    sptinh.setTag(tinh);
                    adapter.getFilter().filter(tinh);
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this, android.R.layout.simple_spinner_item, huyen);
                    sphuyen.setAdapter(adapter);
                } else {
                    adapter.getFilter().filter(null);
                    sphuyen.setVisibility(View.GONE);
                    spxa.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sphuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String huyen = (String) adapterView.getItemAtPosition(i);
                if (!huyen.equals("Quận/huyện")) {
                    spxa.setVisibility(View.VISIBLE);
                    if (huyen.equals("Thanh Khê")) {
                        xa = getResources().getStringArray(R.array.xaDN1);
                    }
                    if (huyen.equals("Liên Chiểu")) {
                        xa = getResources().getStringArray(R.array.xaDN3);
                    }
                    if (huyen.equals("Hải Châu")) {
                        xa = getResources().getStringArray(R.array.xaDN2);
                    }
                    if (huyen.equals("Điện Bàn")) {
                        xa = getResources().getStringArray(R.array.xaQN1);
                    }
                    if (huyen.equals("Đại Lộc")) {
                        xa = getResources().getStringArray(R.array.xaQN2);
                    }
                    if (huyen.equals("Duy Xuyên")) {
                        xa = getResources().getStringArray(R.array.xaQN3);
                    }
                    sphuyen.setTag(huyen);
                    adapter.getFilter().filter(huyen);
                    ArrayAdapter adapter = new ArrayAdapter(StoreActivity.this, android.R.layout.simple_spinner_item, xa);
                    spxa.setAdapter(adapter);
                } else {
                    spxa.setVisibility(View.GONE);
                    adapter.getFilter().filter(String.valueOf(sptinh.getTag()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spxa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String xa = (String) adapterView.getItemAtPosition(i);
                if (!xa.equals("Xã/phường")) {
                    String tinh = (String) sptinh.getTag();
                    String huyen = (String) sphuyen.getTag();
                    adapter.getFilter().filter(xa);
                } else {
                    adapter.getFilter().filter(String.valueOf(sphuyen.getTag()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        storeList = new ArrayList<>();
        Load();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    System.out.println("Failed: " + task.getException());
                } else {
                    String token = task.getResult().getToken();
                    System.out.println(token);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, CheckNetwork.class);
        startService(intent);
        Intent intent1 = new Intent(this, CheckDataChangeFirebase.class);
        startService(intent1);

//        if(!EventBus.getDefault().isRegistered(this)){
//            EventBus.getDefault().register(this);
//        }
        if (!isOnline()) {
            //Toast.makeText(getApplicationContext(), "Bạn cần kết nối internet", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
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
        checkBox.setEnabled(false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Store");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot ss : snapshot.getChildren()){
                        Store store = ss.getValue(Store.class);
                        storeList.add(store);
                    }
                }
                rcvstore.setVisibility(View.VISIBLE);
                checkBox.setEnabled(true);
                shimmerRecyclerView.hideShimmerAdapter();
                adapter = new StoreRecycleViewAdapter(getApplicationContext(), storeList);
                rcvstore.setAdapter(adapter);
                rcvstore.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                YoYo.with(Techniques.BounceInRight).duration(3000).playOn(rcvstore);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
//        unregisterReceiver(dataChangeBroadcast);
    }

    /*@Subscribe
    public void Load(LoadEvent loadEvent){
        if(loadEvent.isLoad()){
            storeList.clear();
            Load();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void Load() {
        rcvstore.setVisibility(View.INVISIBLE);
        shimmerRecyclerView.showShimmerAdapter();
        fillStore();
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

    @Override
    public void onBackPressed() {
        if (doubleClickBackPress) {
            finish();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.back_to_exit), Toast.LENGTH_SHORT).show();
        this.doubleClickBackPress = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleClickBackPress = false;
            }
        }, 5000);
    }
}