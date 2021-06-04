package com.allteran.pizzamira.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.services.RealmService;
import com.allteran.pizzamira.ui.LoginActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import io.realm.Realm;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME_FRAGMENT";
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatButton deleteRealmButton = view.findViewById(R.id.button_delete_realm);
        deleteRealmButton.setOnClickListener(v -> {
            RealmService.deleteDatabase();
            Log.d(TAG, "realm database deleted");
            Toast.makeText(getActivity(), "Realm database deleted", Toast.LENGTH_SHORT).show();
        });
        RealmService realmService = new RealmService(getActivity());
        realmService.showBadge(Realm.getDefaultInstance());

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            Log.d(TAG, "fUser is logged in");
            Log.d(TAG, fUser.toString());
        } else {
            Log.d(TAG, "fUser is null, so its not logged in");
        }
    }
}