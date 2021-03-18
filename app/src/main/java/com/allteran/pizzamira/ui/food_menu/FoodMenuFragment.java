package com.allteran.pizzamira.ui.food_menu;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.List;

public class FoodMenuFragment extends Fragment {

    private FoodMenuViewModel mFoodMenuViewModel;

    private RecyclerView mRecycler;
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
        return inflater.inflate(R.layout.fragment_food_menu,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFoodMenuViewModel = new ViewModelProvider(this).get(FoodMenuViewModel.class);

        mRecycler = view.findViewById(R.id.food_list_recycler);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getRealSize(screenSize);

        //next lines mean that RecyclerView children (items) have fixed width and height
        //that allows RecyclerView to optimize by figuring out the exact parameters of entire list
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));

        FragmentManager fm = getActivity().getSupportFragmentManager();
        mMenuAdapter = new MenuAdapter(fm, mRecycler,
                mFoodMenuViewModel.getFoodList().getValue(), screenSize);
        mRecycler.setAdapter(mMenuAdapter);

        mFoodMenuViewModel.getFoodList().observe(getViewLifecycleOwner(), new Observer<List<FoodItem>>() {
            @Override
            public void onChanged(List<FoodItem> foodItems) {
                mMenuAdapter.refreshList(foodItems);
                //use this when state can be changed
            }
        });

    }

}