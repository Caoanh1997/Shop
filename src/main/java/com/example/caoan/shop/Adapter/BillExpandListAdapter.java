package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caoan.shop.EventBus.BillEvent;
import com.example.caoan.shop.EventBus.LoadEvent;
import com.example.caoan.shop.Model.Bill;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.Model.Store;
import com.example.caoan.shop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

public class BillExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Bill> billList;
    private HashMap<Bill, List<Cart>> ListDetailBill;
    private String key;
    private String key_master;
    private String userID;
    private FirebaseDatabase firebaseDatabase;
    private List<Cart> cartList;
    private Fragment fragment;
    private Bill item;
    private String token_master;

    public BillExpandListAdapter(Context context, List<Bill> billList, HashMap<Bill, List<Cart>> listDetailBill, Fragment fragment) {
        this.context = context;
        this.billList = billList;
        ListDetailBill = listDetailBill;
        this.fragment = fragment;
    }

    public void getStoreInfor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("key_store", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("key", "");
        key_master = sharedPreferences.getString("key_master", "");
//        token_master = sharedPreferences.getString("token_master","");
        sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID", "");
    }

    @Override
    public int getGroupCount() {
        return this.billList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.ListDetailBill.get(this.billList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.billList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.ListDetailBill.get(this.billList.get(i)).get(i1);
    }

    public List<Cart> getCartList(int i) {
        return this.ListDetailBill.get(this.billList.get(i));
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final Bill bill = (Bill) getGroup(i);
        cartList = ListDetailBill.get(bill);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.bill_item_layout, null);
        }

        TextView tvkeycart = view.findViewById(R.id.tvkeycart);
        final TextView tvstore = view.findViewById(R.id.tvstore);
        TextView tvdateput = view.findViewById(R.id.tvdateput);
        TextView tvdatepay = view.findViewById(R.id.tvdatepay);
        TextView tvname = view.findViewById(R.id.tvname);
        TextView tvaddress = view.findViewById(R.id.tvaddress);
        TextView tvphone = view.findViewById(R.id.tvphone);
        TextView tvsumprice = view.findViewById(R.id.tvsumprice);
        TextView tvstate = view.findViewById(R.id.tvstate);
        final TextView tvdeleteorder = view.findViewById(R.id.tvdeleteorder);

        tvkeycart.setText("Mã đơn hàng: " + bill.getKey_cart());
        FirebaseDatabase.getInstance().getReference("Shopmaster").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    for (DataSnapshot ss : snapshot.getChildren()){
//                        Store store = ss.getValue(Store.class);
//                        if(store.getKey().equals(bill.getKey_store())){
//                            tvstore.setText("Cửa hàng: " + store.getName());
//                        }
//                    }
//                }
                Store store = dataSnapshot.child(bill.getKey_store()).getValue(Store.class);
                if(store != null){
                    tvstore.setText("Cửa hàng: " + store.getName());
                }else {
                    tvstore.setText("Cửa hàng không tồn tại hoặc đã xóa");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tvdateput.setText("Ngày đặt: " + bill.getDatetime());
        tvdatepay.setText("Ngày thanh toán: " + bill.getDatetime_delivered());
        tvname.setText("Tên khách hàng: " + bill.getName_user());
        tvaddress.setText("Địa chỉ giao hàng: " + bill.getAddress());
        tvphone.setText("Số điện thoại: " + bill.getPhone());
        tvsumprice.setText("Tổng tiền: " + bill.getTotal_price() + "đ");
        tvstate.setText(bill.getState());
        if (String.valueOf(tvstate.getText()).equals("Đang chờ xác nhận")) {
            tvdeleteorder.setEnabled(true);
            tvdeleteorder.setVisibility(View.VISIBLE);
            tvdeleteorder.setClickable(true);

            tvdeleteorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getStoreInfor();

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference("Order").child(userID).child("New")
                            .child(bill.getKey_cart()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            item = dataSnapshot.getValue(Bill.class);
                            EventBus.getDefault().post(new BillEvent(item));
                            firebaseDatabase.getReference("Order").child(userID).child("New")
                                    .child(bill.getKey_cart()).removeValue();
                            String key_store = bill.getKey_store();
                            FirebaseDatabase.getInstance().getReference("Shopmaster").child(key_store)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Store store = dataSnapshot.getValue(Store.class);
                                            String key_master = store.getUserkey();
                                            FirebaseDatabase.getInstance().getReference("NewOrder").child(key_master)
                                                    .child(bill.getKey_cart()).removeValue();
                                            EventBus.getDefault().post(new LoadEvent(true, fragment));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    tvdeleteorder.setEnabled(false);
                }
            });
        } else {
            tvdeleteorder.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Cart cart = (Cart) getChild(i, i1);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.detail_bill_item_layout, null);
        }
        ImageView avatar = view.findViewById(R.id.avatar);
        TextView tvnameproduct = view.findViewById(R.id.tvnameproduct);
        TextView tvprice = view.findViewById(R.id.tvprice);
        TextView tvnumber = view.findViewById(R.id.tvnumber);

        Picasso.get().load(cart.getUrlImage()).into(avatar);
        tvnameproduct.setText(cart.getName());
        tvprice.setText("Giá: " + String.valueOf(cart.getPrice()) + "đ");
        tvnumber.setText("Số lượng: " + String.valueOf(cart.getNumber()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setNewItem(List<Bill> billList, HashMap<Bill, List<Cart>> billListHashMap){
        this.billList = billList;
        this.ListDetailBill = billListHashMap;
        notifyDataSetChanged();
    }
}
