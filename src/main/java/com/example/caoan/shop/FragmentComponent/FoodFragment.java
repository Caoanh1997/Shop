package com.example.caoan.shop.FragmentComponent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.caoan.shop.AccountActivity;
import com.example.caoan.shop.Adapter.FoodAdapter;
import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.CartActivity;
import com.example.caoan.shop.Database.DataCart;
import com.example.caoan.shop.FirstActivity;
import com.example.caoan.shop.LoginActivity;
import com.example.caoan.shop.MainActivity;
import com.example.caoan.shop.Model.Cart;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private View view;
    private GridView gridView;
    private TextView textView,tvname,tvdetail,tvprice;
    private String mParam1;
    private String mParam2;
    private FirebaseDatabase database;
    private FoodAdapter adapter;
    private Button button, btadd,btback;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Spinner spinner;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String[] arr = {"1","2","3","4","5","6","7","8","9","10","15","20","30","50","100"};
    private String number="1";
    private MainActivity mainActivity;
    private DataCart dataCart;
    private List<Food> foodList;
    private String keyStore;
    private String userKey;
//    private BottomSheetBehavior bottomSheetBehavior;
//    private LinearLayout bottomSheetLayout;
//    private View viewbg;
//    private Button btclose, btaddcart;

    public FoodFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String s, String s1) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        args.putString("keyStore",s);
        args.putString("userKey",s1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_food, container, false);
        textView = view.findViewById(R.id.tv);
        gridView = view.findViewById(R.id.gv);
        button = view.findViewById(R.id.btsize);
        progressBar = view.findViewById(R.id.progress);

//        bottomSheetLayout = view.findViewById(R.id.bottom_sheet);
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
//        viewbg = view.findViewById(R.id.viewbg);

        gridView.setVisibility(View.INVISIBLE);

        keyStore = getArguments().getString("keyStore");
        userKey = getArguments().getString("userKey");

        textView.setText(keyStore);

        foodList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product").child(keyStore).child("Food");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Food food = snapshot.getValue(Food.class);
                    foodList.add(food);
                }
                progressBar.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                adapter = new FoodAdapter(getContext(), foodList);
                gridView.setAdapter(adapter);
                YoYo.with(Techniques.BounceInUp).duration(1000).playOn(gridView);
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
                if(isOnline()){
                    Toast.makeText(getContext(),"Online "+foodList.size(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Offline "+foodList.size(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogview = getLayoutInflater().inflate(R.layout.food_detail_layout,null);
                builder.setTitle("Detail");
                builder.setView(dialogview);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                imageView = dialogview.findViewById(R.id.imagefood);
                tvname = dialogview.findViewById(R.id.name);
                tvdetail = dialogview.findViewById(R.id.detail);
                tvprice = dialogview.findViewById(R.id.price);
                spinner = dialogview.findViewById(R.id.spinner);
                btadd = dialogview.findViewById(R.id.btadd);
                btback = dialogview.findViewById(R.id.btback);

                final Food food = (Food) adapterView.getItemAtPosition(i);
                Picasso.get().load(food.getUrlimage()).into(imageView);
                tvname.setText(food.getName());
                tvdetail.setText(food.getDescription());
                tvprice.setText(String.valueOf(food.getPrice())+"d");

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
                            Cart cart = new Cart(food.getName(),food.getPrice(),Integer.valueOf(number),food.getUrlimage(),userKey,keyStore);
                            dataCart = new DataCart(getContext());
                            dataCart.InsertCart(cart);
                            Intent intent = new Intent(getActivity(), CartActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getActivity(), BottomNavigationBarActivity.class);
                            intent.putExtra("login",true);
                            startActivity(intent);
                        }
                    }
                });
                btback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });*/
                FoodAdapter.ShowDetail((Food)adapterView.getItemAtPosition(i),getContext());
//                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
            }
        });
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if(newState ==  BottomSheetBehavior.STATE_COLLAPSED){
//                    viewbg.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                viewbg.setVisibility(View.VISIBLE);
//                viewbg.setAlpha(slideOffset);
//            }
//        });
//        viewbg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//            }
//        });
//        btclose = view.findViewById(R.id.btclose);
//        btaddcart = view.findViewById(R.id.btaddcart);
//
//        btclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        });
//        btaddcart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(),"Add Cart",Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            this.mainActivity = (MainActivity) context;
        }
    }

    public List<Food> send(){
        return this.foodList;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
