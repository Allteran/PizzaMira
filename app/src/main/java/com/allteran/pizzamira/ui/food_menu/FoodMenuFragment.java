package com.allteran.pizzamira.ui.food_menu;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.adapters.MenuAdapter;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.services.FirebaseService;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

//TODO: WARNING! This fragment doesn't implement ViewModel Pattern to display data. So you have to implement ViewModel
//TODO: or implement creating fragment with newInstance

public class FoodMenuFragment extends Fragment {

    private FirebaseService mDatabaseService;

    private FoodMenuViewModel mFoodMenuViewModel;
    private List<FoodItem> mFoodList;

    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;

    private MenuAdapter mMenuAdapter;

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
        return inflater.inflate(R.layout.fragment_food_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFoodMenuViewModel = new ViewModelProvider(this).get(FoodMenuViewModel.class);

        mRecycler = view.findViewById(R.id.food_list_recycler);
        mProgressBar = view.findViewById(R.id.progress_bar);

        mProgressBar.setVisibility(View.VISIBLE);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getRealSize(screenSize);

        //next lines mean that RecyclerView children (items) have fixed width and height
        //that allows RecyclerView to optimize by figuring out the exact parameters of entire list
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FragmentManager fm = getActivity().getSupportFragmentManager();



        mDatabaseService.loadFoodList(new FirebaseService.DataStatus() {
            @Override
            public void dataIsLoaded(List<FoodItem> foodList) {
                mMenuAdapter = new MenuAdapter(fm, mRecycler,
                        foodList, screenSize);
                mRecycler.setAdapter(mMenuAdapter);
                mProgressBar.setVisibility(View.GONE);
            }
        });

//        mFoodMenuViewModel.getFoodList().observe(getViewLifecycleOwner(), foodItems -> {
//            mMenuAdapter.refreshList(foodItems);
//            mProgressBar.setVisibility(View.GONE);
//        });

    }

}