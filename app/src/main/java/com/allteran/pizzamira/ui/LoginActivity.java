package com.allteran.pizzamira.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.services.FirebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LOGIN_ACTIVITY";
    private FirebaseAuth mAuth;

    private FirebaseService mDatabaseService;

    private ConstraintLayout mPhoneContainer;
    private ConstraintLayout mCodeContainer;
    private ProgressBar mProgressBar;
    private ConstraintLayout mNoNetworkContainer;

    private EditText mPhoneEditText;
    private EditText mCodeEditText;
    private TextView mPhoneLabel;
    private TextView mCodeLabel;
    private TextView mResendCodeLabel;
    private TextView mAnotherPhoneLabel;

    private AppCompatButton mLoginButton;
    private AppCompatButton mEnterCodeButton;
    private AppCompatButton mResetNetworkButton;

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
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            mCodeFromCredential = phoneAuthCredential.getSmsCode();
            if (mCodeFromCredential != null) {
                mCodeEditText.setText(mCodeFromCredential);
            }
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
            showCodeLayout();

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };
    private String mCodeFromCredential;

    private void signInWithPhoneAuthCredentials(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signin: task is successful");
                                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                assert fUser != null;

                                mCodeContainer.setVisibility(View.GONE);
                                mProgressBar.setVisibility(View.VISIBLE);

                                //If it's first time when user going to login - we should add this user to database
                                mDatabaseService.findUserById(fUser.getUid(), new FirebaseService.DataStatus() {

                                    @Override
                                    public void dataIsLoaded(List<FoodItem> foodList) {
                                        //autogenerated
                                    }

                                    @Override
                                    public void dataIsLoaded(User user) {
                                        //Check if user already exist in database
                                        //if doesnt - add one to database
                                        if (user == null) {
                                            Log.d(TAG, "findUserById: user not found, add new one");
                                            mDatabaseService.addUser(fUser);
                                        } else {
                                            Log.d(TAG, "findUserById: user exist, so we wont add it to database");
                                        }

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void dataIsLoaded(Order order) {

                                    }

                                    @Override
                                    public void onLoadError(@NonNull @NotNull DatabaseError error) {
                                    }
                                });
                            } else {
                                Log.d(TAG, task.getException().getMessage());
                                Log.d(TAG, "singInWithPhoneAuthCredentials task failed. See loglist below");
                                task.getException().printStackTrace();
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    mCodeEditText.setError("Неверный код");
                                    mCodeEditText.requestFocus();
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            mCodeEditText.setError("Неверный код");
            mCodeEditText.requestFocus();
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseService = new FirebaseService(FirebaseDatabase.getInstance());

        mProgressBar = findViewById(R.id.progress_bar);

        mPhoneEditText = findViewById(R.id.user_phone_input);
        mCodeEditText = findViewById(R.id.code_input);

        mPhoneLabel = findViewById(R.id.user_phone_label);
        mCodeLabel = findViewById(R.id.user_code_label);

        mResendCodeLabel = findViewById(R.id.resend_code_label);
        mAnotherPhoneLabel = findViewById(R.id.enter_another_phone_label);

        mPhoneContainer = findViewById(R.id.phone_container);
        mCodeContainer = findViewById(R.id.code_container);
        mNoNetworkContainer = findViewById(R.id.no_network_container);

        mLoginButton = findViewById(R.id.login_button);
        mEnterCodeButton = findViewById(R.id.enter_code_button);
        mResetNetworkButton = findViewById(R.id.reset_network_button);

        mProgressBar.setVisibility(View.VISIBLE);

        if (isNetworkConnected()) {
            showLoginForm();
        } else {
            showNoNetwork();
        }
        mResetNetworkButton.setOnClickListener(this);
        mResendCodeLabel.setOnClickListener(this);
        mAnotherPhoneLabel.setOnClickListener(this);
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
        mEnterCodeButton.setOnClickListener(this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private void showLoginForm() {
        mProgressBar.setVisibility(View.GONE);
        mNoNetworkContainer.setVisibility(View.GONE);
        mPhoneContainer.setVisibility(View.VISIBLE);
    }

    private void showNoNetwork() {
        mProgressBar.setVisibility(View.GONE);
        mPhoneContainer.setVisibility(View.GONE);
        mCodeContainer.setVisibility(View.GONE);
        mNoNetworkContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        if (v.getId() == R.id.login_button) {
            Log.d(TAG, "clicked login button");
            sendVerificationCode(mPhoneEditText.getText().toString().trim());
            showCodeLayout();
        }
        if (v.getId() == R.id.enter_code_button) {
            Log.d(TAG, "enter_code_button pressed");
            if (mCodeEditText.getText().toString().trim().length() < 6) {
                Log.d(TAG, "code length less than 6 digits");
                mCodeEditText.setError("Код состоит из 6-ти цифр");
                mCodeEditText.requestFocus();
                return;
            }
            Log.d(TAG, "code length is normal");
            if (mCodeFromCredential == null) {
                Log.d(TAG, "mCodeFromCredential is null");
                signInWithPhoneAuthCredentials(mCodeEditText.getText().toString().trim());
            }
        }
        if (v.getId() == R.id.enter_another_phone_label) {
            showPhoneLayout();
        }
        if (v.getId() == R.id.resend_code_label) {
            Snackbar.make(v, "Код был выслан повторно на номер " + mPhoneEditText.getText().toString().trim(), Snackbar.LENGTH_SHORT).show();
            sendVerificationCode(mPhoneEditText.getText().toString().trim());
        }
        if (v.getId() == R.id.reset_network_button) {
            if (isNetworkConnected()) {
                showLoginForm();
            }
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

    private void showCodeLayout() {
        mPhoneContainer.setVisibility(View.GONE);
        mCodeContainer.setVisibility(View.VISIBLE);
        mCodeEditText.requestFocus();

        mCodeLabel.setText("Введите код, что был выслан на номер " + mPhoneEditText.getText().toString().trim());
    }

    private void showPhoneLayout() {
        mCodeContainer.setVisibility(View.GONE);
        mPhoneContainer.setVisibility(View.VISIBLE);
        mPhoneEditText.requestFocus();
    }

}
