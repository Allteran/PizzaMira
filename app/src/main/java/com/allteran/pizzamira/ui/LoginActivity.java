package com.allteran.pizzamira.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.allteran.pizzamira.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LOGIN_ACTIVITY";
    private FirebaseAuth mAuth;

    private ConstraintLayout mPhoneLayout;
    private ConstraintLayout mCodeLayout;

    private EditText mPhoneEditText;
    private EditText mCodeEditText;
    private TextView mPhoneLabel;
    private TextView mCodeLabel;

    private AppCompatButton mLoginButton;
    private AppCompatButton mEnterCodeButton;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            mPhoneLayout.setVisibility(View.GONE);
            mCodeLayout.setVisibility(View.VISIBLE);

            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                mCodeEditText.setText(code);
                signInWithPhoneAuthCredentials(code);
            }
//            signInWithPhoneAuthCredential(phoneAuthCredential);

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.e(TAG, "onVerificationFailed. Something IS FUCKING INVALID AND IDK WHAT IS THAT", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.e(TAG, "FirebaseAuthInvalidCredentialsException");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.e(TAG, "SMS Quota has been exceeded");
            }
            // Show a message and update the UI
            Toast.makeText(LoginActivity.this, "onVerificationFailed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            super.onCodeSent(verificationId, token);
            Log.d(TAG, "onCodeSent:" + verificationId);
            mPhoneLayout.setVisibility(View.GONE);
            mCodeLayout.setVisibility(View.VISIBLE);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

    private void signInWithPhoneAuthCredentials(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                            Log.d(TAG, "singInWithPhoneAuthCredentials task failed. See loglist below");
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mPhoneEditText = findViewById(R.id.user_phone_input);
        mCodeEditText = findViewById(R.id.code_input);

        mPhoneLabel = findViewById(R.id.user_phone_label);
        mCodeLabel = findViewById(R.id.user_code_label);

        mPhoneLayout = findViewById(R.id.phone_layout);
        mCodeLayout = findViewById(R.id.code_layout);

        mLoginButton = findViewById(R.id.login_button);
        mEnterCodeButton = findViewById(R.id.enter_code_button);

        //let's make uneditable prefix for phone number
        mPhoneEditText.setText("+7");
        Selection.setSelection(mPhoneEditText.getText(), mPhoneEditText.getText().length());

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //autogenerated
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //autogenerated
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+7")) {
                    mPhoneEditText.setText("+7");
                    Selection.setSelection(mPhoneEditText.getText(), mPhoneEditText.getText().length());
                }
                //validate entered phone number
                if (s.toString().length() != 12) {
                    mPhoneEditText.setError("+79XXXXXXXXX");
                }
            }
        });

        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            sendVerificationCode(mPhoneEditText.getText().toString().trim());
        }
        if (v.getId() == R.id.enter_code_button) {
            if (mCodeEditText.getText().toString().trim().length() < 6) {
                mCodeEditText.setError("Код состоит из 6-ти цифр");
                mCodeEditText.requestFocus();
                return;
            }
            signInWithPhoneAuthCredentials(mCodeEditText.getText().toString().trim());
        }
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions phoneOptions =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneOptions);
    }
}
