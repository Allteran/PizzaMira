package com.allteran.pizzamira.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.services.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import org.jetbrains.annotations.NotNull;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ACCOUNT_FRAGMENT";
    private FirebaseAuth mAuth;
    private FirebaseService mDatabase;

    private TextView mHeaderPhone;
    private TextView mHeaderTitle;
    private TextView mHeaderAction;

    private TextView mActiveOrder;
    private TextView mOrdersHistory;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderPhone = view.findViewById(R.id.header_phone);
        mHeaderTitle = view.findViewById(R.id.header_title);
        mHeaderAction = view.findViewById(R.id.header_second_title);

        mActiveOrder = view.findViewById(R.id.active_orders_button);
        mOrdersHistory = view.findViewById(R.id.order_history_button);

        mHeaderTitle.setOnClickListener(this);
        mHeaderAction.setOnClickListener(this);
        mActiveOrder.setOnClickListener(this);
        mOrdersHistory.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            //User is logged in
            final User[] currentUser = new User[1];
            mDatabase.findUserByPhone(firebaseUser.getPhoneNumber(), new FirebaseService.UserDataStatus() {
                @Override
                public void dataIsLoaded(User user) {
                    currentUser[0] = user;
                }

                @Override
                public void onLoadError(@NonNull @NotNull DatabaseError error) {
                    Log.e(TAG,error.toException().fillInStackTrace().toString());
                }
            });
        } else {

        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.header_title) {
            return;
        }
        if(v.getId() == R.id.header_second_title) {
            return;
        }
        if(v.getId() == R.id.active_orders_button) {
            return;
        }
        if(v.getId() == R.id.order_history_button) {
            return;
        }
    }
}