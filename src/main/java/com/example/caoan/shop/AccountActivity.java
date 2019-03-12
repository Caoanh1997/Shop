package com.example.caoan.shop;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private Button btsignup,btsignin,btlogout,btverify;
    private TextView textView;
    private FirebaseAuth mAuth;
    private EditText etemail,etpassword;
    private FirebaseUser firebaseUser;
    private String s="";
    private ActionBar actionBar;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btsignup = findViewById(R.id.btsignup);
        btsignin = findViewById(R.id.btsignin);
        btlogout = findViewById(R.id.btlogout);
        btverify = findViewById(R.id.btverify);
        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        textView = findViewById(R.id.tvuserid);
        back = findViewById(R.id.home);

        btlogout.setVisibility(View.INVISIBLE);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        firebaseUser = mAuth.getCurrentUser();

        btsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();

                if(email== null || password == null || email.equals("") || password.equals("")){
                    etemail.setError("Dien thong tin nay");
                    etpassword.setError("Dien thong tin nay");
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(AccountActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        Toast.makeText(getApplicationContext(), "Sign up success.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(), "Sign up failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
                    try {
                        Thread.sleep(3000);
                        firebaseUser = mAuth.getCurrentUser();
                        firebaseUser.sendEmailVerification().addOnCompleteListener(AccountActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btverify.setEnabled(true);
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Verification email sent to " + firebaseUser.getEmail(),
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();

                if(email== null || password == null || email.equals("") || password.equals("")){
                    etemail.setError("Dien thong tin nay");
                    etpassword.setError("Dien thong tin nay");
                }else{
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(AccountActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Sign in success",Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }else {
                                Toast.makeText(getApplicationContext(),"Sign in failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                firebaseUser = mAuth.getCurrentUser();
                updateUI(firebaseUser);
            }
        });
        btverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser = mAuth.getCurrentUser();
                Toast.makeText(getApplicationContext(),String.valueOf(firebaseUser.isEmailVerified()),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();
        updateUI(firebaseUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            etpassword.setVisibility(View.GONE);
            etemail.setVisibility(View.GONE);

            btsignup.setVisibility(View.GONE);
            btsignin.setVisibility(View.GONE);
            btlogout.setVisibility(View.VISIBLE);
            btverify.setVisibility(View.VISIBLE);

            textView.setText("Email: "+user.getEmail()+"(Verify: "+user.isEmailVerified()+")"+"\nUserID: "+user.getUid());
        }else {
            etpassword.setVisibility(View.VISIBLE);
            etemail.setVisibility(View.VISIBLE);

            btsignup.setVisibility(View.VISIBLE);
            btsignin.setVisibility(View.VISIBLE);
            btlogout.setVisibility(View.INVISIBLE);
            btverify.setVisibility(View.INVISIBLE);
            btverify.setEnabled(false);
            textView.setText("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Account pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Account resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Account stop");
    }
}
