package com.example.caoan.shop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caoan.shop.Model.Bill;
import com.example.caoan.shop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BillRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Bill> billList;

    public BillRecyclerViewAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_item_layout,parent,false);
        RecyclerView.ViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Bill bill = billList.get(position);
        if(bill != null){
            //Picasso.get().load(bill.getCartList().get)
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView tvkeycart, tvdateput, tvdatepay, tvnameproduct, tvprice, tvnumber, tvstate, tvsumprice;
        private ImageView avatar;

        public ItemViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            tvkeycart = itemView.findViewById(R.id.tvkeycart);
            tvdateput = itemView.findViewById(R.id.tvdateput);
            tvdatepay = itemView.findViewById(R.id.tvdatepay);
            tvnameproduct = itemView.findViewById(R.id.tvnameproduct);
            tvprice = itemView.findViewById(R.id.tvprice);
            tvnumber = itemView.findViewById(R.id.tvnumber);
            tvstate = itemView.findViewById(R.id.tvstate);
            tvsumprice = itemView.findViewById(R.id.tvsumprice);
        }
    }
}
