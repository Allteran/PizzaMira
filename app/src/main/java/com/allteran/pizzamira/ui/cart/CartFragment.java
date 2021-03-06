package com.allteran.pizzamira.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.adapters.CartAdapter;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.services.FirebaseService;
import com.allteran.pizzamira.services.RealmService;
import com.allteran.pizzamira.ui.order.OrderActivity;
import com.allteran.pizzamira.util.StringUtils;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;

public class CartFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CART_FRAGMENT";
    private CartViewModel mViewModel;

    private RecyclerView mRecycler;

    private ProgressBar mProgressBar;
    private LinearLayout mNoOrderContainer;
    private AppCompatButton mMenuButton;
    private TextView mConfirmOrderButton;
    private TextView mPriceOrderButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.progress_bar_cart);
        mRecycler = view.findViewById(R.id.recycler_cart);
        mNoOrderContainer = view.findViewById(R.id.no_order_container);

        mMenuButton = view.findViewById(R.id.open_menu_button);
        mConfirmOrderButton = view.findViewById(R.id.button_confirm_order);
        mPriceOrderButton = view.findViewById(R.id.button_price_order);

        mPriceOrderButton.setOnClickListener(this);
        mConfirmOrderButton.setOnClickListener(this);

        mProgressBar.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);

        mMenuButton.setOnClickListener(v ->
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.navigation_menu));

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
            CartAdapter adapter = new CartAdapter(order.getFoodList(), fm, mRecycler, getActivity(),
                    mPriceOrderButton, mConfirmOrderButton, mProgressBar, mNoOrderContainer);
            mRecycler.setAdapter(adapter);
            mRecycler.setVisibility(View.VISIBLE);
            mNoOrderContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            String fullPrice = StringUtils.priceFormatter(order.getFullPrice());
            mPriceOrderButton.setText(fullPrice);
        }
    }

    private void displayNoOrderMessage() {
        mProgressBar.setVisibility(View.GONE);
        mNoOrderContainer.setVisibility(View.VISIBLE);
        mPriceOrderButton.setVisibility(View.GONE);
        mConfirmOrderButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_confirm_order || v.getId() == R.id.button_price_order) {
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            startActivity(intent);
        }
    }
}