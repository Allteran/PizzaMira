package com.allteran.pizzamira.ui.cart;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.adapters.CartAdapter;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.services.FirebaseService;
import com.allteran.pizzamira.services.RealmService;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CartFragment extends Fragment {

    private static final String TAG = "CART_FRAGMENT";
    private CartViewModel mViewModel;

    private RecyclerView mRecycler;

    private FirebaseService mFirebase;

    private CartAdapter mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFirebase = new FirebaseService(FirebaseDatabase.getInstance());
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

        ProgressBar mProgressBar = view.findViewById(R.id.progress_bar_cart);
        mRecycler = view.findViewById(R.id.recycler_cart);

        mProgressBar.setVisibility(View.VISIBLE);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));

        FragmentManager fm = getActivity().getSupportFragmentManager();

        RealmService mRealmService = new RealmService();
        Realm realm = Realm.getDefaultInstance();
        Order order = mRealmService.getCurrentOrder(realm);

        mAdapter = new CartAdapter(order.getFoodList(), fm, mRecycler);
        mRecycler.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "CartAdapter is set");

    }
}