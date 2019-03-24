package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caoan.shop.CartActivity;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.FragmentComponent.CartFragment;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends ArrayAdapter<Cart> {
    private List<Cart> cartList;
    private DataCart dataCart;

    public CartAdapter(@NonNull Context context, @NonNull List<Cart> objects) {
        super(context, 0, objects);
        cartList = objects;
        dataCart = new DataCart(context);
        String key_store = getContext().getSharedPreferences("key_store", Context.MODE_PRIVATE).getString("key", "");
        String sum = String.valueOf(dataCart.Total(key_store));
        if (context instanceof CartActivity) {
            CartActivity.setTextviewSum(sum + "đ");
        } else {
            CartFragment.setTextview(sum + "đ");
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item_layout, parent, false);
            viewHolder.imageView = convertView.findViewById(R.id.image);
            viewHolder.tvname = convertView.findViewById(R.id.name);
            viewHolder.tvprice = convertView.findViewById(R.id.price);
            viewHolder.tvnumber = convertView.findViewById(R.id.number);
            //viewHolder.tvtotal = convertView.findViewById(R.id.total);
            viewHolder.add = convertView.findViewById(R.id.add);
            viewHolder.remove = convertView.findViewById(R.id.remove);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Cart cart = getItem(position);
        if (cart != null) {

            Picasso.get().load(cart.getUrlImage()).into(viewHolder.imageView);
            viewHolder.tvname.setText(cart.getName());
            viewHolder.tvprice.setText(String.valueOf(cart.getPrice() * dataCart.GetNumber(cart)) + "đ");
            viewHolder.tvnumber.setText(String.valueOf(dataCart.GetNumber(cart)));
            float tong = cart.getPrice() * cart.getNumber();
            //viewHolder.tvtotal.setText(String.valueOf(tong)+"d");
            if (dataCart.GetNumber(cart) == 1) {
                viewHolder.remove.setEnabled(false);
            } else if (dataCart.GetNumber(cart) > 1) {
                viewHolder.remove.setEnabled(true);
            }

            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int number = dataCart.GetNumber(cart);
                    number++;
                    if (number > 1) {
                        viewHolder.remove.setEnabled(true);
                    }
                    dataCart.UpdateNumber(number, cart);
                    viewHolder.tvnumber.setText(String.valueOf(dataCart.GetNumber(cart)));
                    viewHolder.tvprice.setText(String.valueOf(cart.getPrice() * number) + "đ");
                    String key_store = getContext().getSharedPreferences("key_store", Context.MODE_PRIVATE).getString("key", "");
                    String sum = String.valueOf(dataCart.Total(key_store));
                    if (getContext() instanceof CartActivity) {
                        CartActivity.setTextviewSum(sum + "đ");
                    } else {
                        CartFragment.setTextview(sum + "đ");
                    }
                }
            });

            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Cart c = getItem(position);
                    int number = dataCart.GetNumber(cart);
                    number--;
                    if (number == 1) {
                        viewHolder.remove.setEnabled(false);
                    }
                    dataCart.UpdateNumber(number, cart);
                    viewHolder.tvnumber.setText(String.valueOf(dataCart.GetNumber(cart)));
                    viewHolder.tvprice.setText(String.valueOf(cart.getPrice() * number) + "đ");
                    String key_store = getContext().getSharedPreferences("key_store", Context.MODE_PRIVATE).getString("key", "");
                    String sum = String.valueOf(dataCart.Total(key_store));
                    if (getContext() instanceof CartActivity) {
                        CartActivity.setTextviewSum(sum + "đ");
                    } else {
                        CartFragment.setTextview(sum + "đ");
                    }
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView imageView, add, remove;
        private TextView tvname, tvprice, tvnumber;//,tvtotal;
    }
}
