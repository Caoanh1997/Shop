package com.example.caoan.shop.FragmentComponent;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.FirstActivity;
import com.example.caoan.shop.LoginActivity;
import com.example.caoan.shop.Model.Account;
import com.example.caoan.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BottomNavigationBarActivity bottomNavigationBarActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    private CircularProgressButton btsignin, btsignup, btsignout;
    private EditText etemail, etpassword, etname, etaddress, etphone;
    private TextView tvuserid, tvsignup, tvsignin;
    private FirebaseAuth firebaseAuth;
    private Animation animation;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    private LinearLayout lospinner;
    private String[] tinh;
    private String[] huyen;
    private String[] xa;
    private Spinner spinnertinh, spinnerhuyen, spinnerxa;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        updateUI(user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_account, container, false);

        btsignin = view.findViewById(R.id.btsignin);
        btsignup = view.findViewById(R.id.btsignup);
        btsignout = view.findViewById(R.id.btsignout);
        etemail = view.findViewById(R.id.etemail);
        etpassword = view.findViewById(R.id.etpassword);
        etname = view.findViewById(R.id.etname);
        etaddress = view.findViewById(R.id.etaddress);
        etphone = view.findViewById(R.id.etphone);
        tvuserid = view.findViewById(R.id.tvuserid);
        tvsignup = view.findViewById(R.id.tvsignup);
        tvsignin = view.findViewById(R.id.tvsignin);
        lospinner = view.findViewById(R.id.spinner);
        spinnertinh = view.findViewById(R.id.spinnertinh);
        spinnerhuyen = view.findViewById(R.id.spinnerhuyen);
        spinnerxa = view.findViewById(R.id.spinnerxa);

        spinnerhuyen.setVisibility(View.INVISIBLE);
        spinnerxa.setVisibility(View.INVISIBLE);

        fillSpinner();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Account");

        animation = new AnimationUtils().loadAnimation(getContext(), R.anim.fade_out);

        final ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.fade_out_animator);
        final ObjectAnimator objectAnimator1 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.fade_out_animator);
        final ObjectAnimator objectAnimator2 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.object_animator_ex);
        final ObjectAnimator objectAnimator3 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_left_animator);
        final ObjectAnimator objectAnimator4 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_right_animator);
        final ObjectAnimator objectAnimator5 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_left_animator);
        final ObjectAnimator objectAnimator6 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_up_animator);

        final ObjectAnimator objectAnimator9 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_up_animator);
        final ObjectAnimator objectAnimator10 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_up_animator);
        final ObjectAnimator objectAnimator11 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_up_animator);

        objectAnimator.setTarget(tvsignup);
        objectAnimator1.setTarget(btsignin);
        objectAnimator2.setTarget(tvsignup);
        objectAnimator3.setTarget(etname);
        objectAnimator11.setTarget(lospinner);
        objectAnimator4.setTarget(etaddress);
        objectAnimator5.setTarget(etphone);
        objectAnimator6.setTarget(btsignup);

        objectAnimator9.setTarget(etemail);
        objectAnimator10.setTarget(etpassword);

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(tvsignup,"Đăng ký",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                btsignin.startAnimation(animation);
                btsignin.setEnabled(false);
                btsignin.setVisibility(View.GONE);
                etname.setVisibility(View.VISIBLE);
                lospinner.setVisibility(View.VISIBLE);
                etaddress.setVisibility(View.VISIBLE);
                etphone.setVisibility(View.VISIBLE);
                btsignup.setVisibility(View.VISIBLE);
                tvsignin.setVisibility(View.VISIBLE);
                tvsignin.setClickable(true);
                tvsignup.setVisibility(View.GONE);

                //etpassword.startAnimation(animation);
                objectAnimator.start();
                objectAnimator1.start();
                objectAnimator2.start();
                objectAnimator3.start();
                objectAnimator4.start();
                objectAnimator5.start();
                objectAnimator6.start();
                objectAnimator9.start();
                objectAnimator10.start();
                objectAnimator11.start();
            }
        });
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().finish();
                //startActivity(getActivity().getIntent());
                Intent intent = new Intent(getActivity(),BottomNavigationBarActivity.class);
                intent.putExtra("login",true);
                startActivity(intent);

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(etemail.getText());
                String password = String.valueOf(etpassword.getText());
                if (CheckOnline()) {
                    progressDialog.setMessage("Đang đăng nhập...");
                    if (CheckInput(etemail) && CheckInput(etpassword)) {
                        //progressDialog.show();
                        btsignin.startAnimation();
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            //updateUI(user);
                                            SaveAccountToSharedPreferences(user);
                                            btsignin.dispose();
                                            progressDialog.dismiss();
                                            //tvuserid.setText(user.getUid());
                                            startActivity(new Intent(getContext(),BottomNavigationBarActivity.class)
                                                    .putExtra("login",true));
                                        } else {
                                            btsignin.revertAnimation();
                                            Toast.makeText(getContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    btsignin.revertAnimation();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(etemail.getText());
                String password = String.valueOf(etpassword.getText());
                if (CheckOnline()) {
                    progressDialog.setMessage("Đang đăng ký...");
                    if (CheckInput(etemail) && CheckInput(etpassword) && CheckInput(etname) && CheckInput(etaddress) && CheckInput(etphone) && checkSpinner()) {
                        btsignup.startAnimation();
                        //progressDialog.show();
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String userID = user.getUid();
                                    String name = String.valueOf(etname.getText());
                                    String email = String.valueOf(etemail.getText());
                                    String address = String.valueOf(etaddress.getText());
                                    String tinh = String.valueOf(spinnertinh.getTag());
                                    String huyen = String.valueOf(spinnerhuyen.getTag());
                                    String xa = String.valueOf(spinnerxa.getTag());
                                    String phone = String.valueOf(etphone.getText());

                                    Account account = new Account(userID,name,email,address,tinh,huyen,xa,phone);
                                    databaseReference.child(user.getUid()).setValue(account);
                                    updateUI(user);
                                    SaveAccountToSharedPreferences(user);
                                    btsignup.dispose();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getContext(),BottomNavigationBarActivity.class));
                                } else {
                                    btsignup.revertAnimation();
                                    Toast.makeText(getContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    btsignup.revertAnimation();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Account",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userID");
                editor.commit();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarActivity) {
            this.bottomNavigationBarActivity = (BottomNavigationBarActivity) context;
        }
    }
    public void fillSpinner(){
        tinh  = getResources().getStringArray(R.array.tinh);
        final ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,tinh);
        spinnertinh.setAdapter(adapter);
        spinnertinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tinh = (String) adapterView.getItemAtPosition(i);
                if(!tinh.equals("Tỉnh/thành phố")){
                    spinnerhuyen.setVisibility(View.VISIBLE);
                    if(tinh.equals("Đà Nẵng")){
                        spinnertinh.setTag(tinh);
                        huyen = getResources().getStringArray(R.array.huyenDN);
                    }else {
                        spinnertinh.setTag("Quảng Nam");
                        huyen = getResources().getStringArray(R.array.huyenQN);
                    }
                    spinnerhuyen.setAdapter(new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,huyen));
                }else {
                    spinnerhuyen.setVisibility(View.INVISIBLE);
                    spinnerxa.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerhuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String huyen = (String) adapterView.getItemAtPosition(i);
                if(!huyen.equals("Quận/huyện")){
                    spinnerxa.setVisibility(View.VISIBLE);
                    if (huyen.equals("Thanh Khê")) {
                        xa = getResources().getStringArray(R.array.xaDN1);
                    }
                    if (huyen.equals("Liên Chiểu")) {
                        xa = getResources().getStringArray(R.array.xaDN3);
                    }
                    if (huyen.equals("Hải Châu")) {
                        xa = getResources().getStringArray(R.array.xaDN2);
                    }
                    if (huyen.equals("Điện Bàn")) {
                        xa = getResources().getStringArray(R.array.xaQN1);
                    }
                    if (huyen.equals("Đại Lộc")) {
                        xa = getResources().getStringArray(R.array.xaQN2);
                    }
                    if (huyen.equals("Duy Xuyên")) {
                        xa = getResources().getStringArray(R.array.xaQN3);
                    }
                    spinnerhuyen.setTag(huyen);
                    spinnerxa.setAdapter(new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,xa));
                }else {
                    spinnerxa.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerxa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String xa = String.valueOf(adapterView.getItemAtPosition(i));
                spinnerxa.setTag(xa);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public boolean CheckOnline() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean CheckInput(EditText editText) {
        String text = String.valueOf(editText.getText());
        if (text.isEmpty() || text == null || text.equals("")) {
            editText.setError("Bạn phải điền thông tin này");
            return false;
        } else {
            return true;
        }
    }

    public void SaveAccountToSharedPreferences(FirebaseUser user){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Account",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID",user.getUid());
        editor.commit();
    }
    public boolean checkSpinner(){
        if(spinnertinh.getSelectedItem().toString().equals("Tỉnh/thành phố") || spinnerhuyen.getSelectedItem().toString().equals("Quận/huyện")
                || spinnerxa.getSelectedItem().toString().equals("Xã/phường")){
            Snackbar.make(spinnerxa,"Điền đầy đủ thông tin địa chỉ",Snackbar.LENGTH_LONG).setAction("Action",null)
                    .show();
            return false;
        }else {
            return true;
        }
    }
    private void updateUI(final FirebaseUser user) {
        if(user == null){
            final ObjectAnimator objectAnimator7 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_down_animator);
            final ObjectAnimator objectAnimator8 = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.move_down_animator);

            objectAnimator7.setTarget(etemail);
            objectAnimator8.setTarget(etpassword);

            objectAnimator7.start();
            objectAnimator8.start();

            etname.setVisibility(View.GONE);
            lospinner.setVisibility(View.GONE);
            etaddress.setVisibility(View.INVISIBLE);
            etphone.setVisibility(View.INVISIBLE);
            etname.setClickable(false);
            lospinner.setClickable(false);
            etaddress.setClickable(false);
            etphone.setClickable(false);
            btsignup.setVisibility(View.GONE);
            btsignout.setVisibility(View.GONE);
            tvsignin.setVisibility(View.GONE);

            tvsignup.setClickable(true);
        }else {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Account");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account account = dataSnapshot.child(user.getUid()).getValue(Account.class);
                    tvuserid.setText("Tên khách hàng: "+account.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            etemail.setVisibility(View.GONE);
            etpassword.setVisibility(View.GONE);
            etname.setVisibility(View.GONE);
            lospinner.setVisibility(View.GONE);
            etaddress.setVisibility(View.GONE);
            etphone.setVisibility(View.GONE);
            btsignin.setVisibility(View.GONE);
            btsignup.setVisibility(View.GONE);
            tvsignin.setVisibility(View.GONE);
            tvsignup.setVisibility(View.GONE);
            btsignup.setEnabled(false);
            btsignin.setEnabled(false);
            tvsignup.setClickable(false);
            tvsignin.setClickable(false);
            etemail.setEnabled(false);
            etpassword.setEnabled(false);
            etname.setClickable(false);
            lospinner.setClickable(false);
            etaddress.setClickable(false);
            etphone.setClickable(false);

            tvuserid.setVisibility(View.VISIBLE);
            btsignout.setVisibility(View.VISIBLE);
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
