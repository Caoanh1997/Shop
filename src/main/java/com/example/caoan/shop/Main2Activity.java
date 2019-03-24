package com.example.caoan.shop;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.caoan.shop.Adapter.FoodAdapter;
import com.example.caoan.shop.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private String imgURL;
    private GridView gridView;
    private ProgressBar progressBar;
    private FoodAdapter adapter;
    private FirebaseDatabase database;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        gridView = findViewById(R.id.gv);
        //progressBar = findViewById(R.id.progressbar);
        button = findViewById(R.id.btsize);

        //progressBar.setMax(100);
        //progressBar.setProgress(0);
        //new ProgressBarAsync().execute();
        //progressBar.setVisibility(View.INVISIBLE);

        final List<Food> foodList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Food");

        for (int i = 0; i < 5; i++) {
            String foodID = reference.push().getKey();
            Food food = new Food("Chuoi", "This is banana", "https://cdn1.woolworths.media/content/wowproductimages/medium/306510.jpg", 10000);
            reference.child(foodID).setValue(food);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food food = snapshot.getValue(Food.class);
                    System.out.println(food.getName());
                    foodList.add(food);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(foodList.size());
                //Toast.makeText(getApplicationContext(),foodList.size(),Toast.LENGTH_SHORT).show();
                if (isOnline()) {
                    Toast.makeText(getApplicationContext(), "Online", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new FoodAdapter(this, foodList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), ((Food) (adapterView.getItemAtPosition(i))).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
