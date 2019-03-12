package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Cart> {

    private List<Cart> cartList;
    private Context context;

    public OrderAdapter(@NonNull Context context, @NonNull List<Cart> objects) {
        super(context, 0,  objects);
        this.context = context;
        cartList = objects;
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        ViewHolder viewHolder;
//        if(convertView == null){
//            viewHolder = new ViewHolder();
//
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item_pay_layout,parent,false);
//            viewHolder.tvname = convertView.findViewById(R.id.tvname);
//            viewHolder.tvnumber = convertView.findViewById(R.id.tvnumber);
//            viewHolder.tvprice = convertView.findViewById(R.id.tvprice);
//
//            Cart cart = getItem(position);
//            if(cart != null){
//                viewHolder.tvname.setText(cart.getName());
//                viewHolder.tvnumber.setText(String.valueOf(cart.getNumber()));
//                float tong = cart.getPrice()*cart.getNumber();
//                viewHolder.tvprice.setText(String.valueOf(tong)+" đ");
//            }
//            convertView.setTag(viewHolder);
//        }else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        return convertView;
//    }
//
//    class ViewHolder{
//        private TextView tvname,tvnumber,tvprice;
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item_layout,parent,false);
            viewHolder.avatar = convertView.findViewById(R.id.avatar);
            viewHolder.tvnameproduct = convertView.findViewById(R.id.tvnameproduct);
            viewHolder.tvprice = convertView.findViewById(R.id.tvprice);
            viewHolder.tvnumber = convertView.findViewById(R.id.tvnumber);
            viewHolder.tvstate = convertView.findViewById(R.id.tvstate);
            viewHolder.tvsumprice = convertView.findViewById(R.id.tvsumprice);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Cart cart = getItem(position);
        if (cart != null){
            fullscreen(viewHolder.avatar);
            Picasso.get().load(cart.getUrlImage()).into(viewHolder.avatar);
            viewHolder.tvnameproduct.setText(cart.getName());
            viewHolder.tvprice.setText(String.valueOf(cart.getPrice()));
            viewHolder.tvnumber.setText(String.valueOf(cart.getNumber()));
            viewHolder.tvstate.setText("Chưa xác nhận");
            viewHolder.tvsumprice.setText(String.valueOf(cart.getPrice()*cart.getNumber())+"đ");
        }
        return convertView;
    }
    class ViewHolder{
        private ImageView avatar;
        private TextView tvnameproduct,tvprice,tvnumber,tvstate,tvsumprice;
    }

    public void fullscreen(ImageView imageView){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH ){
            imageView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );

        }
        else if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
            imageView.setSystemUiVisibility( View.STATUS_BAR_HIDDEN );
        else{}
    }
}
