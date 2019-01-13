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

import com.example.caoan.shop.Model.Store;
import com.example.caoan.shop.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class StoreAdapter extends ArrayAdapter<Store> {

    private List<Store> storeList;
    public StoreAdapter(@NonNull Context context, @NonNull List<Store> objects) {
        super(context, 0, objects);
        storeList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_item_layout,parent,false);

            viewHolder.imageView = convertView.findViewById(R.id.ivstore);
            viewHolder.tvname = convertView.findViewById(R.id.tvname);
            viewHolder.tvaddress = convertView.findViewById(R.id.tvaddress);
            viewHolder.tvphone = convertView.findViewById(R.id.tvphone);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Store store = getItem(position);
        if(store != null){
            new ProcessingImage(viewHolder.imageView).execute(store.getUrlImage());
            viewHolder.tvname.setText(store.getName());
            viewHolder.tvaddress.setText(store.getDuong()+" "+ store.getXa()+" "+store.getHuyen()+" "+store.getTinh());
            viewHolder.tvphone.setText(store.getPhone());
        }

        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView tvname, tvaddress, tvphone;
    }

    class ProcessingImage extends AsyncTask<String,Void,Bitmap>{

        private ImageView imageView;

        public ProcessingImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... voids) {
            Bitmap bitmap = null;
            String url = voids[0];

            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
