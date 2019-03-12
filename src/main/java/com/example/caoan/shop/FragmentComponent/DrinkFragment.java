package com.example.caoan.shop.FragmentComponent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.example.caoan.shop.Model.Food;
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
    private String[] arr = {"1","2","3","4","5","6","7","8","9","10","15","20","30","50","100"};
    private String number="1";
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
        args.putString("keyStore",s);
        args.putString("userKey",s1);
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Drink drink = snapshot.getValue(Drink.class);
                    drinkList.add(drink);
                }
                progressBar.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                drinkAdapter = new DrinkAdapter(getContext(),drinkList);
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
                if(isOnline()){
                    Toast.makeText(getContext(),"Online "+drinkList.size(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Offline "+drinkList.size(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        drinkAdapter = new DrinkAdapter(getContext(),drinkList);
        gridView.setAdapter(drinkAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), ((Drink) (adapterView.getItemAtPosition(i))).toString(),
//                        Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogview = getLayoutInflater().inflate(R.layout.food_detail_layout,null);
                builder.setTitle("Detail");
                builder.setView(dialogview);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                ImageView imageView = dialogview.findViewById(R.id.imagefood);
                TextView tvname = dialogview.findViewById(R.id.name);
                TextView tvdetail = dialogview.findViewById(R.id.detail);
                TextView tvprice = dialogview.findViewById(R.id.price);
                Spinner spinner = dialogview.findViewById(R.id.spinner);
                Button btadd = dialogview.findViewById(R.id.btadd);
                Button btback = dialogview.findViewById(R.id.btback);

                final Drink drink = (Drink) adapterView.getItemAtPosition(i);
                Picasso.get().load(drink.getUrlimage()).into(imageView);
                tvname.setText(drink.getName());
                tvdetail.setText(drink.getDescription());
                tvprice.setText(String.valueOf(drink.getPrice())+"d");

                final ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, arr);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        number = (String) adapterView.getItemAtPosition(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                btadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        if(firebaseUser != null){
                            Cart cart = new Cart(drink.getName(),drink.getPrice(),Integer.valueOf(number),drink.getUrlimage(),userKey,keyStore);
                            dataCart = new DataCart(getContext());
                            dataCart.InsertCart(cart);
                            Intent intent = new Intent(getActivity(), CartActivity.class);
                            //intent.putExtra("cart", cart);
                            startActivity(intent);
//                            Toast.makeText(getContext(),cart.toString(),Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }
                });
                btback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        return view;
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
