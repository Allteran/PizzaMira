package com.allteran.pizzamira.ui.cart;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.adapters.CartAdapter;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.services.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;

    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;

    private FirebaseService mDatabase;
    private CartAdapter mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mDatabase = new FirebaseService(FirebaseDatabase.getInstance());
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
//        // TODO: Use the ViewModel
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.progress_bar_cart);
        mRecycler = view.findViewById(R.id.recycler_cart);

        mProgressBar.setVisibility(View.VISIBLE);

        FragmentManager fm = getActivity().getSupportFragmentManager();


    }
}