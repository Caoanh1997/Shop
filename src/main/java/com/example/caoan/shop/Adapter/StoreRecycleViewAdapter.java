package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.FirstActivity;
import com.example.caoan.shop.ItemClickListener;
import com.example.caoan.shop.Model.Store;
import com.example.caoan.shop.R;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StoreRecycleViewAdapter extends RecyclerView.Adapter<StoreRecycleViewAdapter.ViewHolder> implements Filterable{

    private Context context;
    private List<Store> storeList, filterList;

    public StoreRecycleViewAdapter(Context context,List<Store> stores) {
        this.context = context;
        storeList = stores;
        filterList = stores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Store store = storeList.get(position);
        Picasso.get().load(store.getUrlImage()).into(holder.imageView);
        holder.tvname.setText(store.getName());
        holder.tvaddress.setText(store.getDuong()+", "+ store.getXa()+"-"+store.getHuyen()+"-"+store.getTinh());
        holder.tvphone.setText(store.getPhone());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    SharedPreferences sharedPreferences = context.getSharedPreferences("key_store", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("key",storeList.get(position).getKey());
                    editor.putString("key_master",storeList.get(position).getUserkey());
                    editor.commit();
                    Intent intent = new Intent(context, FirstActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context,"Long click",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
    public static String convertString(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "").replaceAll("đ", "d");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Store> suggestions = new ArrayList<>();
                if(charSequence == null || charSequence.length()==0){
                    suggestions.addAll(filterList);
                }else {
                    String str = convertString(charSequence.toString().toLowerCase().trim());
                    System.out.println(str);
                    for(Store store : filterList){
                        if(convertString(store.getName().toLowerCase().trim()).contains(str)){
                            suggestions.add(store);
                        }
                        if(convertString(store.getDuong().toLowerCase().trim()).contains(str)){
                            suggestions.add(store);
                        }
                        if(convertString(store.getTinh().toLowerCase().trim()).equals(str)){
                            suggestions.add(store);
                        }
                        if(convertString(store.getHuyen().toLowerCase().trim()).equals(str)){
                            suggestions.add(store);
                        }
                        if(convertString(store.getXa().toLowerCase().trim()).equals(str)){
                            suggestions.add(store);
                        }
                    }
                }
                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                storeList = (List<Store>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private ImageView imageView;
        private TextView tvname, tvaddress, tvphone;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivstore);
            tvname = itemView.findViewById(R.id.tvname);
            tvaddress = itemView.findViewById(R.id.tvaddress);
            tvphone = itemView.findViewById(R.id.tvphone);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
            return true;
        }
    }
}
