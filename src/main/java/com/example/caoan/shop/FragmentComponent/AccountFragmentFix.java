package com.example.caoan.shop.FragmentComponent;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.caoan.shop.BottomNavigationBarActivity;
import com.example.caoan.shop.Model.Account;
import com.example.caoan.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragmentFix.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragmentFix#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragmentFix extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CircularProgressButton btsignin, btsignup;
    private EditText etemail, etpassword, etname, etaddress, etphone;
    private TextView tvsignup, tvsignin;
    private FirebaseAuth firebaseAuth;
    private Animation animation;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    private LinearLayout lospinner;
    private String[] tinh;
    private String[] huyen;
    private String[] xa;
    private Spinner spinnertinh, spinnerhuyen, spinnerxa;
    private View view;

    private BottomNavigationBarActivity bottomNavigationBarActivity;

    public AccountFragmentFix() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragmentFix.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragmentFix newInstance(String param1, String param2) {
        AccountFragmentFix fragment = new AccountFragmentFix();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_fragment_fix, container, false);

        btsignin = view.findViewById(R.id.btsignin);
        btsignup = view.findViewById(R.id.btsignup);
        etemail = view.findViewById(R.id.etemail);
        etpassword = view.findViewById(R.id.etpassword);
        etname = view.findViewById(R.id.etname);
        etaddress = view.findViewById(R.id.etaddress);
        etphone = view.findViewById(R.id.etphone);
        tvsignup = view.findViewById(R.id.tvsignup);
        tvsignin = view.findViewById(R.id.tvsignin);
        lospinner = view.findViewById(R.id.spinner);
        spinnertinh = view.findViewById(R.id.spinnertinh);
        spinnerhuyen = view.findViewById(R.id.spinnerhuyen);
        spinnerxa = view.findViewById(R.id.spinnerxa);

        spinnerhuyen.setVisibility(View.INVISIBLE);
        spinnerxa.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        fillSpinner();

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Account");

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btsignin.setVisibility(View.GONE);
                tvsignup.setVisibility(View.GONE);

                etname.setVisibility(View.VISIBLE);
                lospinner.setVisibility(View.VISIBLE);
                etaddress.setVisibility(View.VISIBLE);
                etphone.setVisibility(View.VISIBLE);
                btsignup.setVisibility(View.VISIBLE);
                tvsignin.setVisibility(View.VISIBLE);
                tvsignin.setClickable(true);

                //Amination
                YoYo.with(Techniques.BounceInRight).duration(2000).playOn(etname);
                YoYo.with(Techniques.BounceInRight).duration(2000).playOn(etaddress);
                YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(lospinner);
                YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(etphone);
                YoYo.with(Techniques.StandUp).duration(1000).playOn(btsignup);
                YoYo.with(Techniques.StandUp).duration(1000).playOn(tvsignin);
            }
        });
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getActivity(), BottomNavigationBarActivity.class);
                intent.putExtra("login", true);
                startActivity(intent);*/
                updateUI(null);
            }
        });
        final LinearLayout linearLayout = view.findViewById(R.id.linearlayout);

        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
                String email = String.valueOf(etemail.getText());
                String password = String.valueOf(etpassword.getText());
                if (CheckOnline()) {

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

                                            //tvuserid.setText(user.getUid());
                                            startActivity(new Intent(getContext(), BottomNavigationBarActivity.class)
                                            );
                                            Toast.makeText(getContext(), "Sign in success", Toast.LENGTH_SHORT).show();
                                            //Crouton.makeText(getActivity(),"Success",Style.INFO).show();
                                        } else {
                                            btsignin.revertAnimation();
                                            Toast.makeText(getContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                                            //Crouton.makeText(getActivity(),"Sign in failed",Style.ALERT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    btsignin.revertAnimation();

                    Toast.makeText(getContext(), "Kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
                String email = String.valueOf(etemail.getText());
                String password = String.valueOf(etpassword.getText());
                if (CheckOnline()) {

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

                                    Account account = new Account(userID, name, email, address, tinh, huyen, xa, phone);
                                    databaseReference.child(user.getUid()).setValue(account);
                                    //updateUI(user);
                                    SaveAccountToSharedPreferences(user);
                                    btsignup.dispose();

                                    startActivity(new Intent(getContext(), BottomNavigationBarActivity.class)
                                    );
                                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                    //Crouton.makeText(getActivity(),"Success", Style.INFO).show();
                                } else {
                                    btsignup.revertAnimation();
                                    Toast.makeText(getContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                                    //Crouton.makeText(getActivity(),"Sign up failed",Style.ALERT).show();
                                }
                            }
                        });
                    }
                } else {
                    btsignup.revertAnimation();

                    Toast.makeText(getContext(), "Kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void fillSpinner() {
        tinh = getResources().getStringArray(R.array.tinh);
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, tinh);
        spinnertinh.setAdapter(adapter);
        spinnertinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tinh = (String) adapterView.getItemAtPosition(i);
                if (!tinh.equals("Tỉnh/thành phố")) {
                    spinnerhuyen.setVisibility(View.VISIBLE);
                    if (tinh.equals("Đà Nẵng")) {
                        spinnertinh.setTag(tinh);
                        huyen = getResources().getStringArray(R.array.huyenDN);
                    } else {
                        spinnertinh.setTag("Quảng Nam");
                        huyen = getResources().getStringArray(R.array.huyenQN);
                    }
                    spinnerhuyen.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, huyen));
                } else {
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
                if (!huyen.equals("Quận/huyện")) {
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
                    spinnerxa.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, xa));
                } else {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        updateUI(user);
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

    public void SaveAccountToSharedPreferences(FirebaseUser user) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", user.getUid());
        editor.commit();
    }

    public boolean checkSpinner() {
        if (spinnertinh.getSelectedItem().toString().equals("Tỉnh/thành phố") || spinnerhuyen.getSelectedItem().toString().equals("Quận/huyện")
                || spinnerxa.getSelectedItem().toString().equals("Xã/phường")) {
            Snackbar.make(spinnerxa, "Điền đầy đủ thông tin địa chỉ", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    private void updateUI(final FirebaseUser user) {
        if (user == null) {
            etname.setVisibility(View.GONE);
            lospinner.setVisibility(View.GONE);
            etaddress.setVisibility(View.GONE);
            etphone.setVisibility(View.GONE);
            btsignup.setVisibility(View.GONE);
            tvsignin.setVisibility(View.GONE);
            etname.setClickable(false);
            lospinner.setClickable(false);
            etaddress.setClickable(false);
            etphone.setClickable(false);
            btsignin.setVisibility(View.VISIBLE);
            tvsignup.setVisibility(View.VISIBLE);
            tvsignup.setClickable(true);

            //Amination
            YoYo.with(Techniques.SlideInUp).duration(1000).playOn(etemail);
            YoYo.with(Techniques.SlideInUp).duration(1000).playOn(etpassword);
            YoYo.with(Techniques.FlipInX).duration(1000).playOn(btsignin);
            YoYo.with(Techniques.FlipInX).duration(1000).playOn(tvsignup);
        } else {

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
