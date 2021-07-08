package com.allteran.pizzamira.ui.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    private ConstraintLayout mMainContainer;
    private ProgressBar mProgress;

    private TextView mOrderTitle;
    private TextView mHeaderPhone;
    private TextView mHeaderTitle;
    private TextView mHeaderAction;

    private View mOrderSeparator;


    private AppCompatButton mActiveOrderButton;
    private AppCompatButton mOrdersHistoryButton;
    private AppCompatButton mEmailButton;
    private AppCompatButton mPhoneButton;
    private AppCompatButton mInstagramButton;
    private AppCompatButton mVkButton;

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

        mMainContainer = view.findViewById(R.id.main_countainer_account);
        mProgress = view.findViewById(R.id.progress_bar_account);

        mOrderTitle = view.findViewById(R.id.order_list_title);
        mHeaderPhone = view.findViewById(R.id.header_phone);
        mHeaderTitle = view.findViewById(R.id.header_title);
        mHeaderAction = view.findViewById(R.id.header_action);

        mActiveOrderButton = view.findViewById(R.id.active_orders_button);
        mOrdersHistoryButton = view.findViewById(R.id.order_history_button);
        mEmailButton = view.findViewById(R.id.email_button);
        mPhoneButton = view.findViewById(R.id.phone_button);
        mInstagramButton = view.findViewById(R.id.instagram_button);
        mVkButton = view.findViewById(R.id.vk_button);

        mOrderSeparator = view.findViewById(R.id.order_separator);

        mHeaderTitle.setOnClickListener(this);
        mHeaderAction.setOnClickListener(this);

        mActiveOrderButton.setOnClickListener(this);
        mOrdersHistoryButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mPhoneButton.setOnClickListener(this);
        mInstagramButton.setOnClickListener(this);
        mVkButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            //User is logged in
            final User[] currentUser = new User[1];
            mDatabase.findUserByPhone(firebaseUser.getPhoneNumber(), new FirebaseService.UserDataStatus() {
                @Override
                public void dataIsLoaded(User user) {
                    currentUser[0] = user;
                    mProgress.setVisibility(View.GONE);
                    mMainContainer.setVisibility(View.VISIBLE);
                    showForLoggedUser();
                }

                @Override
                public void onLoadError(@NonNull @NotNull DatabaseError error) {
                    Log.e(TAG, error.toException().fillInStackTrace().toString());
                    mProgress.setVisibility(View.GONE);
                    mMainContainer.setVisibility(View.VISIBLE);
                    showForAnon();
                }
            });
        } else {
            mProgress.setVisibility(View.GONE);
            mMainContainer.setVisibility(View.VISIBLE);
            showForAnon();
        }


    }

    private void showForLoggedUser() {
        mOrderTitle.setVisibility(View.VISIBLE);
        mActiveOrderButton.setVisibility(View.VISIBLE);
        mOrdersHistoryButton.setVisibility(View.VISIBLE);
        mOrderSeparator.setVisibility(View.VISIBLE);
    }

    private void showForAnon() {
        mOrderTitle.setVisibility(View.GONE);
        mActiveOrderButton.setVisibility(View.GONE);
        mOrdersHistoryButton.setVisibility(View.GONE);
        mOrderSeparator.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.header_title) {
            return;
        }
        if (v.getId() == R.id.header_action) {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
            return;
        }
        if (v.getId() == R.id.active_orders_button) {
            return;
        }
        if (v.getId() == R.id.order_history_button) {
            return;
        }
        if (v.getId() == R.id.email_button) {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc882");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@email.io"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв о работе сервиса ПиццаМира.рф");
                startActivity(Intent.createChooser(emailIntent, "Отправить электронное письмо"));
            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(getActivity(), "Произошла ошибка, попробуйте позже", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (v.getId() == R.id.phone_button) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String feedbackPhone = "+79021335276";
                intent.setData(Uri.parse("tel: " + feedbackPhone));
                startActivity(intent);
            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(getActivity(), "Произошла ошибка, попробуйте позже", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (v.getId() == R.id.instagram_button) {
            try {
                Intent instIntent = new Intent(Intent.ACTION_VIEW);
                instIntent.setData(Uri.parse("https://www.instagram.com/allteran"));
                startActivity(instIntent);
            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(getActivity(), "Произошла ошибка, попробуйте позже", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (v.getId() == R.id.vk_button) {
            try {
                Intent vkIntent = new Intent(Intent.ACTION_VIEW);
                vkIntent.setData(Uri.parse("https://www.vk.com/allteran"));
                startActivity(vkIntent);
            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(getActivity(), "Произошла ошибка, попробуйте позже", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
}