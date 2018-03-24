package com.abdulwd.lifeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.btn_signup)
    Button signUp;
    private String email;
    private String password;
    private String number;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String userID;
    private UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        Log.e("HUzaifa", "Button Clicked");
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        number = etNumber.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.requestFocus();
            etEmail.setError ("This field is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etEmail.requestFocus();
            etEmail.setError("Create a password");
            return;
        }

        if (TextUtils.isEmpty(number)) {
            etEmail.requestFocus();
            etEmail.setError("Create a password");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etEmail.requestFocus();
            etEmail.setError("Create a password");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        etPassword.setError("Password is too weak");
                        etPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        etEmail.setError("Email is invalid");
                        etEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        etEmail.setError("User already exists");
                        etEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e("SIGNUP_ACTIVITY", e.getMessage());
                    }

                } else {

                    mAuth = FirebaseAuth.getInstance();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        userID = user.getUid();
                    }
                    userInformation = new UserInformation(email, number);
                    myRef.child("users").child(userID).setValue(userInformation);
                }
            }
        });
    }
}
