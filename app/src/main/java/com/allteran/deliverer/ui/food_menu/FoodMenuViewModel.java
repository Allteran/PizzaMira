package com.allteran.deliverer.ui.food_menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodMenuViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public FoodMenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}