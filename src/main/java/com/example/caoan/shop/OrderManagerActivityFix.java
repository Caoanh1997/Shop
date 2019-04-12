package com.example.caoan.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.caoan.shop.EventBus.BillEvent;
import com.example.caoan.shop.EventBus.LoadEvent;
import com.example.caoan.shop.FragmentComponent.AllOrderFragment;
import com.example.caoan.shop.FragmentComponent.ConfirmOrderFragment;
import com.example.caoan.shop.FragmentComponent.DeleteOrderFragmnet;
import com.example.caoan.shop.FragmentComponent.DeliveredOrderFragment;
import com.example.caoan.shop.FragmentComponent.TransportOrderFragment;
import com.example.caoan.shop.Model.Bill;
import com.example.caoan.shop.Model.Store;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class OrderManagerActivityFix extends AppCompatActivity {

    private ActionBar actionBar;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.orderconfirm:
                            fragment = new ConfirmOrderFragment();
                            loadFragment(fragment);
                            actionBar.setTitle("Đơn hàng đợi xác nhận");
                            return true;
                        case R.id.ordertransport:
                            fragment = new TransportOrderFragment();
                            loadFragment(fragment);
                            actionBar.setTitle("Đơn hàng đang giao");
                            return true;
                        case R.id.orderdelivered:
                            fragment = new DeliveredOrderFragment();
                            loadFragment(fragment);
                            actionBar.setTitle("Đơn hàng đã giao");
                            return true;
                        case R.id.orderdelete:
                            fragment = new DeleteOrderFragmnet();
                            loadFragment(fragment);
                            actionBar.setTitle("Đơn hàng đã xóa");
                            return true;
                        case R.id.orderall:
                            fragment = new AllOrderFragment();
                            loadFragment(fragment);
                            actionBar.setTitle("Tất cả đơn hàng");
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager_fix);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        bottomNavigationView = findViewById(R.id.navigation);

        loadFragment(new ConfirmOrderFragment());
        actionBar.setTitle("Đơn hàng cần xác nhận");
        Intent intent = getIntent();
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        int tab = intent.getIntExtra("tab", 0);
        if (tab == 1) {
            bottomNavigationView.setSelectedItemId(R.id.ordertransport);
        } else {
            if (tab == 2) {
                bottomNavigationView.setSelectedItemId(R.id.orderdelivered);
            } else {
                if (tab == 3) {
                    bottomNavigationView.setSelectedItemId(R.id.orderdelete);
                } else {
                    if (tab == 4) {
                        bottomNavigationView.setSelectedItemId(R.id.orderall);
                    } else {
                        bottomNavigationView.setSelectedItemId(R.id.orderconfirm);
                    }
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //startActivity(new Intent(OrderManagerActivityFix.this,BottomNavigationBarActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(OrderManagerActivityFix.this,BottomNavigationBarActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void Load(LoadEvent loadEvent){
        if(loadEvent.isLoad()){
            if(loadEvent.getFragment() instanceof ConfirmOrderFragment){
                bottomNavigationView.setSelectedItemId(R.id.orderconfirm);
            }else {
                bottomNavigationView.setSelectedItemId(R.id.orderall);
            }
        }
    }

    @Subscribe
    public void OnCustomEvent(BillEvent billEvent) {

        final Bill bill = billEvent.getBill();
        final Bill b = new Bill(bill.getKey_cart(),bill.getName_user(),bill.getAddress(),
                bill.getPhone(), bill.getUserID(), bill.getCartList(), bill
                .getTotal_price(), "Đã hủy đơn hàng",
                bill.getKey_store(), bill.getDatetime(), bill.getDatetime_delivered());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");

        databaseReference.child(getSharedPreferences("Account", Context.MODE_PRIVATE)
                .getString("userID", "")).child("Delete").child(billEvent.getBill().getKey_cart())
                .setValue(b).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference("Shopmaster").child(b.getKey_store())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Store store = dataSnapshot.getValue(Store.class);
                                        String key_master = store.getUserkey();
                                        FirebaseDatabase.getInstance().getReference("DeleteOrder")
                                                .child(key_master).child(b.getKey_cart())
                                                .setValue(b).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(OrderManagerActivityFix.this, "Add delete order", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
        );
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_fix, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
