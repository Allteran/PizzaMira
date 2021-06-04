package com.allteran.pizzamira.ui.cart;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.adapters.CartAdapter;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.services.FirebaseService;
import com.allteran.pizzamira.services.RealmService;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;

public class CartFragment extends Fragment {

    private static final String TAG = "CART_FRAGMENT";
    private CartViewModel mViewModel;

    private RecyclerView mRecycler;

    private FirebaseService mFirebase;

    private ProgressBar mProgressBar;
    private LinearLayout mNoOrderContainer;
    private AppCompatButton mMenuButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFirebase = new FirebaseService(FirebaseDatabase.getInstance());
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.progress_bar_cart);
        mRecycler = view.findViewById(R.id.recycler_cart);
        mNoOrderContainer = view.findViewById(R.id.no_order_container);
        mMenuButton = view.findViewById(R.id.open_menu_button);

        mProgressBar.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        FragmentManager fm = getActivity().getSupportFragmentManager();

        RealmService realmService = new RealmService();
        Realm realm = Realm.getDefaultInstance();
        Order order = realmService.getCurrentOrder(realm);
        if (order == null) {
            displayNoOrderMessage();
        } else if (order.getFoodList().isEmpty()) {
            displayNoOrderMessage();
        } else {
            CartAdapter adapter = new CartAdapter(order.getFoodList(), fm, mRecycler, getActivity());
            mRecycler.setAdapter(adapter);
            mRecycler.setVisibility(View.VISIBLE);
            mNoOrderContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            Log.d(TAG, "CartAdapter is set");
        }


    }

    private void displayNoOrderMessage() {
        mProgressBar.setVisibility(View.GONE);
        mNoOrderContainer.setVisibility(View.VISIBLE);
        mMenuButton.setOnClickListener(v ->
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.navigation_menu));
    }
}