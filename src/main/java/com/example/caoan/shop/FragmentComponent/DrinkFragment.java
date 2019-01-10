package com.example.caoan.shop.FragmentComponent;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.Adapter.DrinkAdapter;
import com.example.caoan.shop.Model.Drink;
import com.example.caoan.shop.Model.Food;
import com.example.caoan.shop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DrinkFragment extends Fragment {

    private View view;
    private TextView textView;
    private GridView gridView;
    private Button button;
    private FirebaseDatabase database;
    private DrinkAdapter drinkAdapter;

    public DrinkFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DrinkFragment newInstance(String s) {
        DrinkFragment fragment = new DrinkFragment();
        Bundle args = new Bundle();
        args.putString("text",s);
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

        String str = getArguments().getString("text");
        textView.setText(str);

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Drink");

        /*for (int i=0;i<5;i++){
            String drinkID = reference.push().getKey();
            Drink drink = new Drink("Cafe","This is cafe"
                    ,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQk8nzJW-1ke89Cghd0d-zF9p8KvQwx7Q9RNPubC4Zp9XGxADS3RQ",15000);
            reference.child(drinkID).setValue(drink);
        }*/
        final List<Drink> drinkList = new ArrayList<Drink>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Drink drink = snapshot.getValue(Drink.class);
                    drinkList.add(drink);
                }
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
                //System.out.println(foodList.size());
                //Toast.makeText(getApplicationContext(),foodList.size(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), ((Drink) (adapterView.getItemAtPosition(i))).toString(),
                        Toast.LENGTH_SHORT).show();
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
