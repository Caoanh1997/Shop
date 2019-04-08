package com.example.caoan.shop.FragmentComponent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.EventBus.LoadEvent;
import com.example.caoan.shop.Model.Account;
import com.example.caoan.shop.OrderManagerActivityFix;
import com.example.caoan.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private TextView tvname, tvallorder, tvsignout, tvverify;
    private LinearLayout lnconfirm, lntransport, lndelivered;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private Account account;
    private SwipeRefreshLayout refreshLayout;

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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_manager, container, false);

        tvname = view.findViewById(R.id.tvname);
        tvverify = view.findViewById(R.id.tvverify);
        tvsignout = view.findViewById(R.id.tvsignout);
        tvallorder = view.findViewById(R.id.tvallorder);
        lnconfirm = view.findViewById(R.id.lnconfirm);
        lntransport = view.findViewById(R.id.lntransport);
        lndelivered = view.findViewById(R.id.lndelivered);
        refreshLayout = view.findViewById(R.id.refresh_layout);

        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        ClickVerifyEmail();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                user.reload().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            refreshLayout.setRefreshing(false);
                            LoadUser();
                            ClickVerifyEmail();
                        }
                    }
                });

            }
        });
        tvverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Kiểm tra email để xác thực", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Send verified failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        LoadUser();

        tvallorder.setClickable(true);
        tvallorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderManagerActivityFix.class));
            }
        });
        tvsignout.setClickable(true);
        tvsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                DeleteAccountFromSharedPreferences();
                Toast.makeText(getContext(),"Logout success",Toast.LENGTH_SHORT).show();
                //Crouton.makeText(getActivity(),"Logout success", Style.CONFIRM).show();
                startActivity(new Intent(getActivity(), BottomNavigationBarActivity.class));
            }
        });
        lnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Chưa xác nhận", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrderManagerActivityFix.class);
                intent.putExtra("tab", 0);
                startActivity(intent);
            }
        });
        lntransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Đang giao", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrderManagerActivityFix.class);
                intent.putExtra("tab", 1);
                startActivity(intent);
            }
        });
        lndelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Đã giao", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrderManagerActivityFix.class);
                intent.putExtra("tab", 2);
                startActivity(intent);
            }
        });

        return view;
    }

    private void ClickVerifyEmail() {
        if (!user.isEmailVerified()) {
            tvverify.setClickable(true);
        } else {
            tvverify.setClickable(false);
            tvverify.setEnabled(false);
        }
    }

    private void LoadUser() {
        if (user != null) {
            final String str;
            if (!user.isEmailVerified()) {
                str = "Email: " + user.getEmail() + ". Nhấn để xác thực email";
            } else {
                str = "Email: " + user.getEmail() + ". Email đã xác thực";
            }
            firebaseDatabase = FirebaseDatabase.getInstance();
            reference = firebaseDatabase.getReference("Account");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    account = dataSnapshot.child(user.getUid()).getValue(Account.class);
                    tvname.setText(account.getName());
                    tvverify.setText(str);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().getCurrentUser().reload();
    }

    private void DeleteAccountFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userID");
        editor.commit();
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

    @Subscribe
    public void onLoadEvent(LoadEvent loadEvent) {
        if (loadEvent.isLoad()) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
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
