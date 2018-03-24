package com.abdulwd.lifeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatientLogin extends AppCompatActivity {

    private static final String TAG = "PATIENT LOGIN";
    @BindView(R.id.login_patient)
    TextView AreyouDoc;
    @BindView(R.id.click_here)
    TextView ClickHere;
    @BindView(R.id.et_aadhar)
    EditText mAadhar;
    @BindView(R.id.et_otp)
    EditText mOTP;
    @BindView(R.id.otp)
    android.support.design.widget.TextInputLayout otp;
    @BindView(R.id.btn_verify_otp)
    Button btnVerifyOTP;
    String aadharNumber;
    FirebaseUser firebaseUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth firebaseAuth;
    private String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        mAadhar.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    logIn();
                    return true;
                }
                return false;
            }
        });
    }

    private void logIn() {
        aadharNumber = mAadhar.getText().toString();
        if (TextUtils.isEmpty(aadharNumber)) {
            mAadhar.requestFocus();
            mAadhar.setError("This field is required");
            return;
        }

        if (aadharNumber.length() != 12) {
            mAadhar.requestFocus();
            mAadhar.setError("Enter valid Aadhar number");
            return;
        }
        switch (aadharNumber) {
            case "323223233333":
                authenticate("+919868086204");
                break;

            case "545445455555":
                authenticate("+918130504779");
                break;

            case "898998989999":
                authenticate("+918743886827");
                break;

            default:
                Toast.makeText(this, "No record found for the entered Aadhar", Toast.LENGTH_SHORT).show();
        }
    }

    private void authenticate(String phoneNumber) {
        AreyouDoc.setVisibility(View.GONE);
        ClickHere.setVisibility(View.GONE);
        otp.setVisibility(View.VISIBLE);
        btnVerifyOTP.setVisibility(View.VISIBLE);
        setUpVerificatonCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, callbacks);
    }

    private void setUpVerificatonCallbacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.i(TAG, "verification completed" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "verification failed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e(TAG, "Invalid credential: "
                            + e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
                    Log.e(TAG, "SMS Quota exceeded.");
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                resendToken = token;
                Log.e(TAG, "code sent " + verificationId);
            }
        };
    }

    @OnClick(R.id.btn_verify_otp)
    public void verifyCode() {
        String code = mOTP.getText().toString();
        if (TextUtils.isEmpty(code)) {
            mOTP.requestFocus();
            mOTP.setError("Enter OTP");

        } else {
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "code verified signIn successful");
                            firebaseUser = task.getResult().getUser();
                            Toast.makeText(PatientLogin.this, "code verified signIn successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PatientLogin.this, PatientDetails.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "code verification failed", task.getException());
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                mOTP.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    @OnClick(R.id.click_here)
    void changeLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
