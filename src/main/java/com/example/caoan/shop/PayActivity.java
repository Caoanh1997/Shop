package com.example.caoan.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.Adapter.OrderAdapter;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Model.Account;
import com.example.caoan.shop.Model.Bill;
import com.example.caoan.shop.Model.Cart;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PayActivity extends AppCompatActivity {

    private EditText etname, etemail, etaddress, etphone;
    private Spinner spinnertinh, spinnerhuyen, spinnerxa;
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
        btput = findViewById(R.id.btput);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Order");

        DatabaseReference reference = firebaseDatabase.getReference("Account");
        String key_user = getSharedPreferences("Account",Context.MODE_PRIVATE).getString("userID","");
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

        spinnerhuyen.setVisibility(View.INVISIBLE);
        spinnerxa.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        cartList = new ArrayList<>();
        cartList = (ArrayList<Cart>) intent.getSerializableExtra("listcart");

        OrderAdapter orderAdapter = new OrderAdapter(this,cartList);
        lvcart.setAdapter(orderAdapter);

        final DataCart dataCart = new DataCart(this);
        SharedPreferences sharedPreferences = getSharedPreferences("key_store", Context.MODE_PRIVATE);
        key_master = sharedPreferences.getString("key_master","");
        key = sharedPreferences.getString("key","");
        String str = dataCart.Total(key);
        float total = Float.valueOf(str);
        tvsum.setText(String.valueOf(total)+"d");

        initSpinner();

        btput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckInput(etname) && CheckInput(etaddress) && CheckInput(etphone) && checkSpinner()){
                    firebaseDatabase = FirebaseDatabase.getInstance();

                    databaseReference = firebaseDatabase.getReference("NewOrder");
                    //Toast.makeText(getApplicationContext(),"Store key: "+key+", master key: "+key_master,Toast.LENGTH_SHORT).show();
                    String key_cart = databaseReference.push().getKey();
                    String product="";
                    for (Cart cart : cartList){
                        product += cart.getName() + " ("+cart.getPrice()+" x"+cart.getNumber()+"),";
                    }
                    String total_price = dataCart.Total(key);
                    calendar = Calendar.getInstance();
                    SimpleDateFormat format_date = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");

                    String date_time = "";
                    date_time += format_date.format(calendar.getTime());
                    date_time += " "+format_time.format(calendar.getTime());
                    String address = String.valueOf(etaddress.getText()) +", "+ String.valueOf(spinnerxa.getTag())+"-"
                            +String.valueOf(spinnerhuyen.getTag())+"-"+String.valueOf(spinnertinh.getTag());
                    Bill bill = new Bill(key_cart,String.valueOf(etname.getText()),address,
                            String.valueOf(etphone.getText()),product,total_price,"Đang đợi xác nhận",key,date_time,"");
                    databaseReference.child(key_master).child(key_cart).setValue(bill).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Đặt hàng thành công",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PayActivity.this, FirstActivity.class));
                        }
                    });
                    dataCart.DeleteCart(key);
                }
            }
        });
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
    public boolean checkSpinner(){
        if(spinnertinh.getSelectedItem().toString().equals("Tỉnh/thành phố") || spinnerhuyen.getSelectedItem().toString().equals("Quận/huyện")
                || spinnerxa.getSelectedItem().toString().equals("Xã/phường")){
            Snackbar.make(spinnerxa,"Điền đầy đủ thông tin địa chỉ",Snackbar.LENGTH_LONG).setAction("Action",null)
                    .show();
            return false;
        }else {
            return true;
        }
    }
    public void initSpinner(){
        tinh  = getResources().getStringArray(R.array.tinh);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, tinh);
        spinnertinh.setAdapter(adapter);
        spinnertinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tinh = (String) adapterView.getItemAtPosition(i);
                if(!tinh.equals("Tỉnh/thành phố")){
                    spinnerhuyen.setVisibility(View.VISIBLE);
                    if(tinh.equals("Đà Nẵng")){
                        spinnertinh.setTag(tinh);
                        huyen = getResources().getStringArray(R.array.huyenDN);
                    }else {
                        spinnertinh.setTag("Quảng Nam");
                        huyen = getResources().getStringArray(R.array.huyenQN);
                    }
                    spinnerhuyen.setAdapter(new ArrayAdapter(PayActivity.this,android.R.layout.simple_spinner_item,huyen));
                }else {
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
                if(!huyen.equals("Quận/huyện")){
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
                    spinnerxa.setAdapter(new ArrayAdapter(PayActivity.this,android.R.layout.simple_spinner_item,xa));
                }else {
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
