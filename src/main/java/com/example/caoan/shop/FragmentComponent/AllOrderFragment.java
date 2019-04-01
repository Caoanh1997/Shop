package com.example.caoan.shop.FragmentComponent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.caoan.shop.Adapter.BillExpandListAdapter;
import com.example.caoan.shop.LoadEvent;
import com.example.caoan.shop.Model.Bill;
import com.example.caoan.shop.Model.Cart;
import com.example.caoan.shop.OrderManagerActivity;
import com.example.caoan.shop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private FirebaseDatabase firebaseDatabase;
    private ExpandableListView expandableListView;

    private OrderManagerActivity orderManagerActivity;
    private HashMap<Bill, List<Cart>> ListBillDetail;
    private List<Bill> billList;
    private BillExpandListAdapter billExpandListAdapter;
    private String[] arr = {"New", "Transport", "Delivered", "Delete"};
    private String userID;
    private ProgressBar progressBar;

    public AllOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllOrderFragment newInstance(String param1, String param2) {
        AllOrderFragment fragment = new AllOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_order, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);
        progressBar = view.findViewById(R.id.progress);
        loadBill();

        return view;
    }

    public void getUserID(){
        userID = getActivity().getSharedPreferences("Account",Context.MODE_PRIVATE)
                .getString("userID","");
    }

    public void loadBill() {
        ListBillDetail = new HashMap<Bill, List<Cart>>();
        billList = new ArrayList<Bill>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Order");

        getUserID();
        databaseReference.child(userID).child("New").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Cart> cartList;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bill bill = snapshot.getValue(Bill.class);
                    cartList = bill.getCartList();
                    Bill b = new Bill(bill.getKey_cart(),bill.getName_user(),bill.getAddress(),
                            bill.getPhone(),bill.getUserID(), bill.getTotal_price(),
                            bill.getState(), bill.getKey_store(), bill.getDatetime(), bill.getDatetime_delivered());

                    billList.add(b);
                    ListBillDetail.put(b, cartList);
                }
                firebaseDatabase.getReference("Order").child(userID).child("Transport").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Cart> cartList;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Bill bill = snapshot.getValue(Bill.class);
                            cartList = bill.getCartList();
                            Bill b = new Bill(bill.getKey_cart(),bill.getName_user(),bill.getAddress(),
                                    bill.getPhone(), bill.getUserID(), bill.getTotal_price(),
                                    bill.getState(), bill.getKey_store(), bill.getDatetime(), bill.getDatetime_delivered());

                            billList.add(b);
                            ListBillDetail.put(b, cartList);
                        }
                        firebaseDatabase.getReference("Order").child(userID)
                                .child("Delivered").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<Cart> cartList;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Bill bill = snapshot.getValue(Bill.class);
                                    cartList = bill.getCartList();
                                    Bill b = new Bill(bill.getKey_cart(),bill.getName_user(),bill.getAddress(),
                                            bill.getPhone(), bill.getUserID(), bill.getTotal_price(),
                                            bill.getState(), bill.getKey_store(), bill.getDatetime(), bill.getDatetime_delivered());

                                    billList.add(b);
                                    ListBillDetail.put(b, cartList);
                                }
                                firebaseDatabase.getReference("Order").child(userID)
                                        .child("Delete").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        List<Cart> cartList;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Bill bill = snapshot.getValue(Bill.class);
                                            cartList = bill.getCartList();
                                            Bill b = new Bill(bill.getKey_cart(),bill.getName_user(),bill.getAddress(),
                                                    bill.getPhone(), bill.getUserID(), bill.getTotal_price(),
                                                    bill.getState(), bill.getKey_store(), bill.getDatetime(), bill.getDatetime_delivered());

                                            billList.add(b);
                                            ListBillDetail.put(b, cartList);
                                        }

                                        progressBar.setVisibility(View.GONE);
                                        if (billList.size() == 0) {
                                            Crouton.makeText(getActivity(), "Không có đơn hàng", Style.ALERT).show();
                                        } else {
                                            billExpandListAdapter = new BillExpandListAdapter(getContext(), billList, ListBillDetail, new AllOrderFragment());
                                            expandableListView.setAdapter(billExpandListAdapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrderManagerActivity) {
            this.orderManagerActivity = (OrderManagerActivity) context;
        }
    }

    @Subscribe
    public void Load(LoadEvent loadEvent){
        if(loadEvent.isLoad()){
            ListBillDetail.clear();
            billList.clear();
            loadBill();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
}
