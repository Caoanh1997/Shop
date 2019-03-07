package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CartAdapter extends ArrayAdapter<Cart> {
    private List<Cart> cartList;
    public CartAdapter(@NonNull Context context, @NonNull List<Cart> objects) {
        super(context, 0, objects);
        cartList = objects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item_layout,parent,false);
            viewHolder.imageView = convertView.findViewById(R.id.image);
            viewHolder.tvname = convertView.findViewById(R.id.name);
            viewHolder.tvprice = convertView.findViewById(R.id.price);
            viewHolder.tvnumber = convertView.findViewById(R.id.number);
            viewHolder.tvtotal = convertView.findViewById(R.id.total);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Cart cart = getItem(position);
        if(cart != null){
            Picasso.get().load(cart.getUrlImage()).into(viewHolder.imageView);
            viewHolder.tvname.setText(cart.getName());
            viewHolder.tvprice.setText(String.valueOf(cart.getPrice())+"d");
            viewHolder.tvnumber.setText("x "+String.valueOf(cart.getNumber()));
            float tong = cart.getPrice()*cart.getNumber();
            viewHolder.tvtotal.setText(String.valueOf(tong)+"d");
        }
        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView tvname,tvprice,tvnumber,tvtotal;
    }
}
