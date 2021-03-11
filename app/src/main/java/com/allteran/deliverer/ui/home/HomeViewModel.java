package com.allteran.deliverer.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allteran.deliverer.domain.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<String>> mFoodList;

    private static List<String> foodList = new ArrayList<>();
    static {
        for (int i = 0; i<10; i++) {
            foodList.add("Food no. " + (i+1));
        }
    }
    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mFoodList = new MutableLiveData<>();
        mFoodList.postValue(foodList);
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<List<String>> getFoodList() {
        return mFoodList;
    }

    public LiveData<String> getText() {
        return mText;
    }
}