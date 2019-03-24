package com.example.caoan.shop;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.caoan.shop.Adapter.CartRecyclerViewAdapter;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Model.Cart;

import java.util.ArrayList;
import java.util.List;

public class TestCartRecyclerView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Cart> cartList;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private DataCart dataCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cart_recycler_view);
        dataCart = new DataCart(this);
        recyclerView = findViewById(R.id.rcvcart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        new ProgressBarProcess().execute();
    }

    class ProgressBarProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cartList = new ArrayList<Cart>();
            //SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
            //String key_store = sharedPreferences.getString("key","");
            cartList = dataCart.getCartList("-LZc0xgUdnAYkXD6AZv6");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 1; i <= 3; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            cartRecyclerViewAdapter = new CartRecyclerViewAdapter(TestCartRecyclerView.this, cartList);
            System.out.println(cartList.size());
            recyclerView.setAdapter(cartRecyclerViewAdapter);

        }
    }
}
