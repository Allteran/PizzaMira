package com.allteran.pizzamira.ui.menu;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.adapters.MenuAdapter;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.services.FirebaseService;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

//TODO: WARNING! This fragment doesn't implement ViewModel Pattern to display data. So you have to implement ViewModel
//TODO: or implement creating fragment with newInstance

public class MenuFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private FirebaseService mDatabaseService;

    private MenuViewModel mMenuViewModel;
    private List<FoodItem> mFoodList;

    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefresh;
    private ConstraintLayout mNoNetworkContainer;
    private AppCompatButton mResetNetworkButton;

    private MenuAdapter mMenuAdapter;
    private Point mScreenSize;
    private FragmentManager mFragmentManager;

    public static MenuFragment newInstance() {
        Bundle args = new Bundle();
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        mFoodMenuViewModel =
//                new ViewModelProvider(this).get(FoodMenuViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_food_menu, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        mFoodMenuViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        mDatabaseService = new FirebaseService(FirebaseDatabase.getInstance());
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycler = view.findViewById(R.id.food_list_recycler);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mSwipeRefresh = view.findViewById(R.id.swipe_to_refresh);
        mNoNetworkContainer = view.findViewById(R.id.no_network_container);
        mResetNetworkButton = view.findViewById(R.id.reset_network_button);

        mResetNetworkButton.setOnClickListener(this);
        mSwipeRefresh.setOnRefreshListener(this);

        mProgressBar.setVisibility(View.VISIBLE);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        mScreenSize = new Point();
        display.getRealSize(mScreenSize);

        //next lines mean that RecyclerView children (items) have fixed width and height
        //that allows RecyclerView to optimize by figuring out the exact parameters of entire list
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mFragmentManager = getActivity().getSupportFragmentManager();
        if (!isNetworkConnected()) {
            showNoNetwork();
        } else {
            showMenu();
        }
    }

    private void showMenu() {
        mNoNetworkContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.VISIBLE);
        mSwipeRefresh.post(this::loadMenu);
    }

    private void showNoNetwork() {
        mRecycler.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mNoNetworkContainer.setVisibility(View.VISIBLE);
    }

    private void loadMenu() {
        mDatabaseService.loadFoodList(new FirebaseService.FoodDataStatus() {
            @Override
            public void dataIsLoaded(List<FoodItem> foodList) {
                mMenuAdapter = new MenuAdapter(getContext(), mFragmentManager, mRecycler,
                        foodList, mScreenSize, getActivity());
                mRecycler.setAdapter(mMenuAdapter);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefresh.setRefreshing(false);
            }


            @Override
            public void onLoadError(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
                Snackbar.make(getView(), "При загрузке возникла ошибка. Проверьте подключение к сети", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    @Override
    public void onRefresh() {
        loadMenu();
        mMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reset_network_button) {
            if (isNetworkConnected()) {
                showMenu();
            }
        }
    }
}