package com.example.caoan.shop.Adapter;

import android.content.Context;
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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.Model.Food;
import com.example.caoan.shop.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FoodAdapter extends ArrayAdapter<Food> {
    private List<Food> foodList;

    public FoodAdapter(@NonNull Context context, @NonNull List<Food> objects) {
        super(context, 0, objects);
        foodList = new ArrayList<>(objects);
        notifyDataSetChanged();
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
        Food food = getItem(position);
        if(food != null){
            viewHolder.tvname.setText(food.getName());
            viewHolder.tvprice.setText(String.valueOf(food.getPrice())+"đ");
            Picasso.get().load(food.getUrlimage()).into(viewHolder.imageView);
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
                                    Toast.makeText(getContext(),"Detail",Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.addcart:
                                    Toast.makeText(getContext(),"Add to cart",Toast.LENGTH_SHORT).show();

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

    class ViewHolder{
        ImageView imageView, ivmenu;
        TextView tvname,tvprice;
    }
    private class Processing extends AsyncTask<String,Void,Bitmap>{
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
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            List<Food> suggestions = new ArrayList<>();
            if(charSequence == null || charSequence.length()==0){
                System.out.println("null");
                suggestions.addAll(foodList);
                System.out.println(foodList.size());
            }else {
                String str = charSequence.toString().toLowerCase().trim();
                System.out.println(str);
                for(Food food : foodList){
                    if(food.getName().toLowerCase().contains(str)){
                        suggestions.add(food);
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
            return ((Food)resultValue).getName();
        }
    };

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }
}
