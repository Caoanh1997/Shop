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

import com.example.caoan.shop.Model.Phone;
import com.example.caoan.shop.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PhoneAdapter extends ArrayAdapter<Phone> {

    private List<Phone> phoneList;

    public PhoneAdapter(@NonNull Context context, @NonNull List<Phone> objects) {
        super(context, 0, objects);
        phoneList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.phone_item_layout, parent, false);

            viewHolder.imageView = convertView.findViewById(R.id.imagephone);
            viewHolder.tvname = convertView.findViewById(R.id.tvname);
            viewHolder.tvprice = convertView.findViewById(R.id.tvprice);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Phone phone = getItem(position);
        if (phone != null) {
            viewHolder.tvname.setText(phone.getName());
            viewHolder.tvprice.setText(String.valueOf(phone.getPrice()) + " d");
            new Processing(viewHolder.imageView).execute(phone.getUrlimage());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView tvname, tvprice;
    }

    private class Processing extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public Processing(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            String urlString = strings[0];
            try {
                URL url = new URL(urlString);
                InputStream inputStream = url.openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
