package com.example.caoan.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.Adapter.CartAdapter;
import com.example.caoan.shop.Adapter.CartRecyclerViewAdapter;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Model.Cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private static TextView textView;
    private Button btthanhtoan, bthome;
    private ActionBar actionBar;
    private ProgressBar progressBar;
    private CartAdapter cartAdapter;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private ListView listView;
    private RecyclerView recyclerView;
    private List<Cart> cartList;
    private DataCart dataCart;
    private int num;
    //private float total;
    //private String str="";
    private String key_store;

    public static void setTextviewSum(String sum) {
        textView.setText(sum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btthanhtoan = findViewById(R.id.btthanhtoan);
        bthome = findViewById(R.id.bthome);
        listView = findViewById(R.id.lvcart);
        recyclerView = findViewById(R.id.rcvcart);
        textView = findViewById(R.id.tvsum);
        progressBar = findViewById(R.id.progress);
        //listView.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
        key_store = sharedPreferences.getString("key", "");
        dataCart = new DataCart(this);
        textView.setText(dataCart.Total(key_store) + "đ");

        new ProgressBarProcess().execute();
        registerForContextMenu(listView);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListener() {
            @Override
            public void onSwipe(int vitri, int huong) {
                cartRecyclerViewAdapter.swipe(vitri, huong);
                new ProgressBarProcess().execute();
            }

            @Override
            public void onMove(int oldPostion, int newPosition) {
                cartRecyclerViewAdapter.move(oldPostion, newPosition);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
                String userID = getSharedPreferences("Account", Context.MODE_PRIVATE).getString("userID", "");
                if (userID.equals("")) {
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
                } else {
                    Intent intent = new Intent(CartActivity.this, PayActivity.class);
                    intent.putExtra("listcart", (Serializable) cartList);
                    startActivity(intent);
                }
            }
        });
        bthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), ((Cart) adapterView.getItemAtPosition(i)).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setTag(((Cart) adapterView.getItemAtPosition(i)).getId());
                num = i;
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                int id = (int) listView.getTag();
                dataCart.Delete(id);
                cartList.remove(num);
                listView.invalidateViews();
                textView.setText(dataCart.Total(key_store) + "đ");
                if (dataCart.Total(key_store).equals("0")) {
                    btthanhtoan.setEnabled(false);
                } else {
                    btthanhtoan.setEnabled(true);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }

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
                cartRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh:
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        System.out.println("Cart pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        System.out.println("Cart resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("Cart stop");
    }

    class ProgressBarProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cartList = new ArrayList<Cart>();
            SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
            String key_store = sharedPreferences.getString("key", "");
            cartList = dataCart.getCartList(key_store);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 1; i <= 100; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            //listView.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(CartActivity.this, cartList);
            listView.setAdapter(cartAdapter);
            cartRecyclerViewAdapter = new CartRecyclerViewAdapter(CartActivity.this, cartList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(cartRecyclerViewAdapter);

//            str = dataCart.Total(key_store);
//            float total = Float.valueOf(str);
            //textView.setText(cartAdapter.getSum()+"đ");
            String str = (String) textView.getText();
            if (dataCart.Total(key_store).equals("0") || str.equals("0đ")) {
                btthanhtoan.setEnabled(false);
            } else {
                btthanhtoan.setEnabled(true);
            }
            bthome.setEnabled(true);

        }
    }
}
