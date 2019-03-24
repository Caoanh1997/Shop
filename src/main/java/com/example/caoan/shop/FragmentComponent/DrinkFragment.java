package com.example.caoan.shop.FragmentComponent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.caoan.shop.Adapter.DrinkAdapter;
import com.example.caoan.shop.CartActivity;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.LoginActivity;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.Model.Drink;
import com.example.caoan.shop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DrinkFragment extends Fragment {

    private View view;
    private TextView textView;
    private GridView gridView;
    private Button button;
    private FirebaseDatabase database;
    private DrinkAdapter drinkAdapter;
    private ProgressBar progressBar;
    private String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20", "30", "50", "100"};
    private String number = "1";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String keyStore;
    private String userKey;
    private DataCart dataCart;

    public DrinkFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DrinkFragment newInstance(String s, String s1) {
        DrinkFragment fragment = new DrinkFragment();
        Bundle args = new Bundle();
        args.putString("keyStore", s);
        args.putString("userKey", s1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drink, container, false);

        textView = view.findViewById(R.id.tv);
        gridView = view.findViewById(R.id.gv);
        button = view.findViewById(R.id.btsize);
        progressBar = view.findViewById(R.id.progress);

        gridView.setVisibility(View.INVISIBLE);

        //String str = getArguments().getString("text");
        keyStore = getArguments().getString("keyStore");
        userKey = getArguments().getString("userKey");
        //textView.setText(str);

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product").child(keyStore).child("Drink");

        final List<Drink> drinkList = new ArrayList<Drink>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Drink drink = snapshot.getValue(Drink.class);
                    drinkList.add(drink);
                }
                progressBar.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                drinkAdapter = new DrinkAdapter(getContext(), drinkList);
                gridView.setAdapter(drinkAdapter);
                YoYo.with(Techniques.BounceInUp).duration(1000).playOn(gridView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    Toast.makeText(getContext(), "Online " + drinkList.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Offline " + drinkList.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        drinkAdapter = new DrinkAdapter(getContext(), drinkList);
        gridView.setAdapter(drinkAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               DrinkAdapter.ShowDetail((Drink) adapterView.getItemAtPosition(i),getContext());
            }
        });
        return view;
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
