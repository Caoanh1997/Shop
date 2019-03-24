package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.FragmentComponent.AllOrderFragment;
import com.example.caoan.shop.Model.Bill;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.OrderManagerActivity;
import com.example.caoan.shop.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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
        TextView tvdateput = view.findViewById(R.id.tvdateput);
        TextView tvdatepay = view.findViewById(R.id.tvdatepay);
        TextView tvsumprice = view.findViewById(R.id.tvsumprice);
        TextView tvstate = view.findViewById(R.id.tvstate);
        final TextView tvdeleteorder = view.findViewById(R.id.tvdeleteorder);

        tvkeycart.setText("Mã đơn hàng: " + bill.getKey_cart());
        tvdateput.setText("Ngày đặt: " + bill.getDatetime());
        tvdatepay.setText("Ngày thanh toán: " + bill.getDatetime_delivered());
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
                    DatabaseReference databaseReference = firebaseDatabase.getReference("NewOrder");
                    final DatabaseReference reference = firebaseDatabase.getReference("Order");
                    /*Bill bill1 = new Bill(bill.getKey_cart(),bill.getUserID(),cartList,bill.getTotal_price()
                            ,"Đã hủy đơn hàng",bill.getKey_store(),bill.getDatetime(),"");
                    //System.out.println(cartList);*/
                    databaseReference.child(key_master).child(bill.getKey_cart()).child("state").setValue("Đã hủy đơn hàng").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            reference.child(userID).child(bill.getKey_cart()).child("state").setValue("Đã hủy đơn hàng")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Hủy thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, OrderManagerActivity.class);
                                            if (fragment instanceof AllOrderFragment) {
                                                intent.putExtra("tab", 0);
                                            } else {
                                                intent.putExtra("tab", 1);
                                            }
                                            context.startActivity(intent);
                                        }
                                    });
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
}
