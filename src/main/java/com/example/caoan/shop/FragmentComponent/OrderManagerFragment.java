package com.example.caoan.shop.FragmentComponent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.Model.Account;
import com.example.caoan.shop.OrderManagerActivity;
import com.example.caoan.shop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderManagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderManagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BottomNavigationBarActivity bottomNavigationBarActivity;
    private View view;
    private TextView tvname, tvallorder, tvsignout;
    private LinearLayout lnconfirm, lntransport, lndelivered;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private Account account;

    public OrderManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderManagerFragment newInstance(String param1, String param2) {
        OrderManagerFragment fragment = new OrderManagerFragment();
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
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_manager, container, false);

        tvname = view.findViewById(R.id.tvname);
        tvsignout = view.findViewById(R.id.tvsignout);
        tvallorder = view.findViewById(R.id.tvallorder);
        lnconfirm = view.findViewById(R.id.lnconfirm);
        lntransport = view.findViewById(R.id.lntransport);
        lndelivered = view.findViewById(R.id.lndelivered);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            reference = firebaseDatabase.getReference("Account");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    account = dataSnapshot.child(user.getUid()).getValue(Account.class);
                    tvname.setText(account.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        tvallorder.setClickable(true);
        tvallorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderManagerActivity.class));
            }
        });
        tvsignout.setClickable(true);
        tvsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Toast.makeText(getContext(),"Logout success",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), BottomNavigationBarActivity.class));
            }
        });
        lnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Chưa xác nhận", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrderManagerActivity.class);
                intent.putExtra("tab", 1);
                startActivity(intent);
            }
        });
        lntransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Đang giao", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrderManagerActivity.class);
                intent.putExtra("tab", 2);
                startActivity(intent);
            }
        });
        lndelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Đã giao", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrderManagerActivity.class);
                intent.putExtra("tab", 3);
                startActivity(intent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarActivity) {
            this.bottomNavigationBarActivity = (BottomNavigationBarActivity) context;
        }
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
