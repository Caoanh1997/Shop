package com.example.caoan.shop.FragmentComponent;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.caoan.shop.Adapter.DrinkAdapter;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.Model.Drink;
import com.example.caoan.shop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DrinkFragment extends Fragment {

    private View view;
    private GridView gridView;
    private FirebaseDatabase database;
    private DrinkAdapter drinkAdapter;
    private ProgressBar progressBar;
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


        gridView = view.findViewById(R.id.gv);
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
        drinkAdapter = new DrinkAdapter(getContext(), drinkList);
        gridView.setAdapter(drinkAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DrinkAdapter.ShowDetail((Drink) adapterView.getItemAtPosition(i), getContext());
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
