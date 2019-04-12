package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caoan.shop.CartActivity;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.FragmentComponent.CartFragment;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Cart> cartList;
    private List<Cart> filterList;
    private Context context;
    private DataCart dataCart;

    public CartRecyclerViewAdapter(Context context, List<Cart> cartList) {
        this.cartList = cartList;
        this.context = context;
        filterList = cartList;
        dataCart = new DataCart(context);
        String key_store = context.getSharedPreferences("key_store", Context.MODE_PRIVATE).getString("key", "");
        String sum = String.valueOf(dataCart.Total(key_store));
        if (context instanceof CartActivity) {
            CartActivity.setTextviewSum(sum + "đ");
        } else {
            CartFragment.setTextview(sum + "đ");
        }
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartRecyclerViewAdapter.ViewHolder holder, int position) {
        final Cart cart = cartList.get(position);
        System.out.println(cart.toString());

        Picasso.get().load(cart.getUrlImage()).into(holder.imageView);
        holder.tvname.setText(cart.getName());
        holder.tvprice.setText(String.valueOf(cart.getPrice() * dataCart.GetNumber(cart)) + "đ");
        holder.tvnumber.setText(String.valueOf(dataCart.GetNumber(cart)));

        if (dataCart.GetNumber(cart) == 1) {
            holder.remove.setEnabled(false);
        } else if (dataCart.GetNumber(cart) > 1) {
            holder.remove.setEnabled(true);
        }
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = dataCart.GetNumber(cart);
                number++;
                if (number > 1) {
                    holder.remove.setEnabled(true);
                }
                dataCart.UpdateNumber(number, cart);
                holder.tvnumber.setText(String.valueOf(dataCart.GetNumber(cart)));
                holder.tvprice.setText(String.valueOf(cart.getPrice() * number) + "đ");
                String key_store = context.getSharedPreferences("key_store", Context.MODE_PRIVATE).getString("key", "");
                String sum = String.valueOf(dataCart.Total(key_store));
                if (context instanceof CartActivity) {
                    CartActivity.setTextviewSum(sum + "đ");
                } else {
                    CartFragment.setTextview(sum + "đ");
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = dataCart.GetNumber(cart);
                number--;
                if (number == 1) {
                    holder.remove.setEnabled(false);
                }
                dataCart.UpdateNumber(number, cart);
                holder.tvnumber.setText(String.valueOf(dataCart.GetNumber(cart)));
                holder.tvprice.setText(String.valueOf(cart.getPrice() * number) + "đ");
                String key_store = context.getSharedPreferences("key_store", Context.MODE_PRIVATE).getString("key", "");
                String sum = String.valueOf(dataCart.Total(key_store));
                if (context instanceof CartActivity) {
                    CartActivity.setTextviewSum(sum + "đ");
                } else {
                    CartFragment.setTextview(sum + "đ");
                }
            }
        });

    }

    public void move(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(cartList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(cartList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void swipe(int vitri, int huong) {
        Cart cart = cartList.get(vitri);
        dataCart.Delete(cart.getId());
        cartList.remove(vitri);
        notifyItemRemoved(vitri);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Cart> suggestion = new ArrayList<>();

                if (charSequence.length() == 0 || charSequence == null) {
                    suggestion.addAll(filterList);
                } else {
                    String str = StoreRecycleViewAdapter.convertString(charSequence.toString().toLowerCase().trim());
                    for (Cart cart : filterList) {
                        if (StoreRecycleViewAdapter.convertString(cart.getName().toLowerCase().trim()).contains(str)) {
                            suggestion.add(cart);
                        }
                    }
                }
                FilterResults results = new FilterResults();

                results.values = suggestion;
                results.count = suggestion.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cartList = (List<Cart>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, add, remove;
        private TextView tvname, tvprice, tvnumber;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            tvname = itemView.findViewById(R.id.name);
            tvprice = itemView.findViewById(R.id.price);
            tvnumber = itemView.findViewById(R.id.number);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
