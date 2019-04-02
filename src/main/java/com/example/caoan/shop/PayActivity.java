package com.example.caoan.shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.Adapter.OrderAdapter;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Model.Account;
import com.example.caoan.shop.Model.Bill;
import com.example.caoan.shop.Model.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PayActivity extends AppCompatActivity {

    private EditText etname, etemail, etaddress, etphone;
    private Spinner spinnertinh, spinnerhuyen, spinnerxa, sppay;
    private ListView lvcart;
    private TextView tvsum;
    private Button btput;
    private String[] tinh;
    private String[] huyen;
    private String[] xa;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Cart> cartList;
    private String key;
    private String key_master;
    private Calendar calendar;
    private Account account;
    private DataCart dataCart;
    private ActionBar actionBar;
    private ProgressDialog progressDialog;
    //private CircularProgressButton loadingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etaddress = findViewById(R.id.etaddress);
        etphone = findViewById(R.id.etphone);
        tvsum = findViewById(R.id.tvsum);
        spinnertinh = findViewById(R.id.sptinh);
        spinnerhuyen = findViewById(R.id.sphuyen);
        spinnerxa = findViewById(R.id.spxa);
        lvcart = findViewById(R.id.lvcart);
        sppay = findViewById(R.id.sppay);
        //loadingButton = findViewById(R.id.btput);
        btput = findViewById(R.id.btput);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dataCart = new DataCart(this);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Order");

        DatabaseReference reference = firebaseDatabase.getReference("Account");
        String key_user = getSharedPreferences("Account", Context.MODE_PRIVATE).getString("userID", "");
        reference.child(key_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                account = dataSnapshot.getValue(Account.class);
                etname.setText(account.getName());
                etemail.setText(account.getEmail());
                etaddress.setText(account.getAddress());
                etphone.setText(account.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final String[] pay = getResources().getStringArray(R.array.pay);
        sppay.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, pay));


        spinnerhuyen.setVisibility(View.INVISIBLE);
        spinnerxa.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
        key_master = sharedPreferences.getString("key_master", "");
        key = sharedPreferences.getString("key", "");
        cartList = new ArrayList<>();
        cartList = (ArrayList<Cart>) dataCart.getCartList(key);
        OrderAdapter orderAdapter = new OrderAdapter(this, cartList);
        lvcart.setAdapter(orderAdapter);
        setListViewHeightBasedOnItems(lvcart);

        initSpinner();

        btput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInput(etname) && CheckInput(etaddress) && CheckInput(etphone) && checkSpinner()) {
                    //loadingButton.startAnimation();
                    progressDialog = new ProgressDialog(PayActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Đang đặt hàng...");
                    progressDialog.show();
                    firebaseDatabase = FirebaseDatabase.getInstance();

                    databaseReference = firebaseDatabase.getReference("NewOrder");
                    final DatabaseReference reference1 = firebaseDatabase.getReference("Order");
                    //Toast.makeText(getApplicationContext(),"Store key: "+key+", master key: "+key_master,Toast.LENGTH_SHORT).show();
                    final String key_cart = databaseReference.push().getKey();
                    String product = null;
                    for (Cart cart : cartList) {
                        product += cart.getName() + " (" + cart.getPrice() + " x" + cart.getNumber() + "),";
                    }
                    String total_price = dataCart.Total(key);
                    calendar = Calendar.getInstance();
                    SimpleDateFormat format_date = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");

                    String date_time = "";
                    date_time += format_date.format(calendar.getTime());
                    date_time += " " + format_time.format(calendar.getTime());

                    String address = String.valueOf(etaddress.getText())+", "+String.valueOf(spinnerxa.getTag())+"-"+
                            String.valueOf(spinnerhuyen.getTag())+"-"+String.valueOf(spinnertinh.getTag());
                    final Bill bill = new Bill(key_cart,String.valueOf(etname.getText()),address, String.valueOf(etphone.getText()),
                            getUserID(), cartList, total_price, "Đang chờ xác nhận", key, date_time, "");
                    databaseReference.child(key_master).child(key_cart).setValue(bill).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //loadingButton.revertAnimation();
                            reference1.child(getSharedPreferences("Account", Context.MODE_PRIVATE).getString("userID", ""))
                                    .child("New").child(key_cart).setValue(bill).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PayActivity.this, BottomNavigationBarActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //loadingButton.revertAnimation();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dataCart.DeleteCart(key);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getUserID() {
        String id = getSharedPreferences("Account", Context.MODE_PRIVATE)
                .getString("userID", "");
        return id;
    }

    public void setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        int numberOfItems = listAdapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = listAdapter.getView(itemPos, null, listView);
            float px = 500 * (listView.getResources().getDisplayMetrics().density);
            item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() *
                (numberOfItems - 1);
        // Get padding
        int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight + totalPadding;
        listView.setLayoutParams(params);
        listView.requestLayout();
        //setDynamicHeight(listView);

    }


    public boolean CheckInput(EditText editText) {
        String text = String.valueOf(editText.getText());
        if (text.isEmpty() || text == null || text.equals("")) {
            editText.setError("Bạn phải điền thông tin này");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkSpinner() {
        if (spinnertinh.getSelectedItem().toString().equals("Tỉnh/thành phố") || spinnerhuyen.getSelectedItem().toString().equals("Quận/huyện")
                || spinnerxa.getSelectedItem().toString().equals("Xã/phường")) {
            Snackbar.make(spinnertinh, "Điền đầy đủ thông tin địa chỉ", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    public void initSpinner() {
        tinh = getResources().getStringArray(R.array.tinh);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tinh);
        spinnertinh.setAdapter(adapter);
        spinnertinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tinh = (String) adapterView.getItemAtPosition(i);
                if (!tinh.equals("Tỉnh/thành phố")) {
                    spinnerhuyen.setVisibility(View.VISIBLE);
                    if (tinh.equals("Đà Nẵng")) {
                        spinnertinh.setTag(tinh);
                        huyen = getResources().getStringArray(R.array.huyenDN);
                    } else {
                        spinnertinh.setTag("Quảng Nam");
                        huyen = getResources().getStringArray(R.array.huyenQN);
                    }
                    spinnerhuyen.setAdapter(new ArrayAdapter(PayActivity.this, android.R.layout.simple_spinner_item, huyen));
                } else {
                    spinnerhuyen.setVisibility(View.INVISIBLE);
                    spinnerxa.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerhuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String huyen = (String) adapterView.getItemAtPosition(i);
                if (!huyen.equals("Quận/huyện")) {
                    spinnerxa.setVisibility(View.VISIBLE);
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
                    spinnerhuyen.setTag(huyen);
                    spinnerxa.setAdapter(new ArrayAdapter(PayActivity.this, android.R.layout.simple_spinner_item, xa));
                } else {
                    spinnerxa.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerxa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String xa = String.valueOf(adapterView.getItemAtPosition(i));
                spinnerxa.setTag(xa);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
