package com.example.caoan.shop.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.CartActivity;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.Model.Drink;
import com.example.caoan.shop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DrinkAdapter extends ArrayAdapter<Drink> {
    private List<Drink> drinkList;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;
    private static DataCart dataCart;
    private static String userKey;
    private static String keyStore;

    public DrinkAdapter(@NonNull Context context, @NonNull List<Drink> objects) {
        super(context, 0, objects);
        drinkList = new ArrayList<>(objects);
        notifyDataSetChanged();
        dataCart = new DataCart(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences("key_store", Context.MODE_PRIVATE);
        keyStore = sharedPreferences.getString("key","null");
        userKey = sharedPreferences.getString("key_master","null");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item_layout,parent,false);

            viewHolder.imageView = convertView.findViewById(R.id.imagefood);
            viewHolder.tvname = convertView.findViewById(R.id.tvname);
            viewHolder.tvprice = convertView.findViewById(R.id.tvprice);
            viewHolder.ivmenu = convertView.findViewById(R.id.ivmenu);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Drink drink = getItem(position);
        if(drink != null){
            viewHolder.tvname.setText(drink.getName());
            viewHolder.tvprice.setText(String.valueOf(drink.getPrice())+"đ");
            Picasso.get().load(drink.getUrlimage()).into(viewHolder.imageView);
            viewHolder.ivmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),view);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.detail:
                                    ShowDetail(drink,getContext());
                                    return true;
                                case R.id.addcart:
                                    firebaseAuth = FirebaseAuth.getInstance();
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    if(firebaseUser != null){
                                        Cart cart = new Cart(drink.getName(),drink.getPrice(),1,drink.getUrlimage(),userKey,keyStore);
                                        dataCart = new DataCart(getContext());
                                        dataCart.InsertCart(cart);
                                        Intent intent = new Intent(getContext(), CartActivity.class);
                                        //intent.putExtra("cart", cart);
                                        //getContext().startActivity(intent);
//                            Toast.makeText(getContext(),cart.toString(),Toast.LENGTH_SHORT).show();
                                    }else {
                                        Intent intent = new Intent(getContext(), BottomNavigationBarActivity.class);
                                        intent.putExtra("login",true);
                                        getContext().startActivity(intent);
                                    }

                                    return true;
                                case R.id.vote:
                                    Toast.makeText(getContext(),"Vote",Toast.LENGTH_SHORT).show();
                                    return true;
                            }
                            return false;
                        }
                    });
                }
            });
        }

        return convertView;
    }
    public static void ShowDetail(final Drink drink, final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.product_detail_layout,null,false);
        builder.setView(view1);

        ImageView avatar = view1.findViewById(R.id.avatar);
        ImageView ivclose = view1.findViewById(R.id.ivclose);
        TextView tvnameproduct = view1.findViewById(R.id.tvnameproduct);
        TextView tvdetail = view1.findViewById(R.id.tvdetail);
        Button btclose = view1.findViewById(R.id.btclose);
        Button btaddcart = view1.findViewById(R.id.btaddcart);

        Picasso.get().load(drink.getUrlimage()).into(avatar);
        ivclose.setClickable(true);
        tvnameproduct.setText(drink.getName());
        tvdetail.setText(drink.getDescription());
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        btclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btaddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Cart cart = new Cart(drink.getName(),drink.getPrice(),1,drink.getUrlimage(),userKey,keyStore);
                    dataCart = new DataCart(context);
                    dataCart.InsertCart(cart);
                    Intent intent = new Intent(context, CartActivity.class);
                    //intent.putExtra("cart", cart);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, BottomNavigationBarActivity.class);
                    intent.putExtra("login",true);
                    context.startActivity(intent);
                }
                alertDialog.dismiss();
            }
        });
    }

    class ViewHolder{
        ImageView imageView, ivmenu;
        TextView tvname,tvprice;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            List<Drink> suggestions = new ArrayList<>();
            if(charSequence == null || charSequence.length()==0){
                System.out.println("null");
                suggestions.addAll(drinkList);
                System.out.println(drinkList.size());
            }else {
                String str = charSequence.toString().toLowerCase().trim();
                System.out.println(str);
                for(Drink drink : drinkList){
                    if(drink.getName().toLowerCase().contains(str)){
                        suggestions.add(drink);
                    }
                }
                System.out.println(suggestions.size());
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
//            return super.convertResultToString(resultValue);
            return ((Drink)resultValue).getName();
        }
    };

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }
}
