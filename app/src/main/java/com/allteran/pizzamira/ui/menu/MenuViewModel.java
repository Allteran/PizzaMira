package com.allteran.pizzamira.ui.menu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.services.FirebaseService;
import com.allteran.pizzamira.util.Const;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {
    private static final String TAG = "FOOD_MENU_VIEW_MODEL";
    private MutableLiveData<List<FoodItem>> fakeFoodList;

    public MenuViewModel() {
        loadFoodList();
    }

    private void loadFoodList() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Const.DB_TREE_FOODLIST_FAKE);
        final List<FoodItem>[] foodItems = new List[]{new ArrayList<>()};

        FirebaseService mDatabaseService = new FirebaseService(FirebaseDatabase.getInstance());

        mDatabaseService.loadFoodList(new FirebaseService.FoodDataStatus() {
            @Override
            public void dataIsLoaded(List<FoodItem> foodList) {
                foodItems[0] = foodList;
                Log.d(TAG, "dataIsLoaded");
            }

            @Override
            public void onLoadError(@NonNull @NotNull DatabaseError error) {
                error.toException().printStackTrace();
            }

        });

//
//        FoodCategory pizzaCategory = new FoodCategory("PZZ", "Пиццы");
//
//        List<FoodItem> foodList = new ArrayList<>();
//        foodList.add(new FoodItem("MARGARITA", "Пицца маргарита", "Изысканная пицейка та тоненьком тесте", 350, 450, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("peanaple", "Пицца с ананасами", "Странная и хипповая пицца с курицей и ананасиком", 220, 650, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("diavollo", "Пицца дьяволо", "жгучая, как слова бывшей пицейка с охренительно острым соусом", 341, 540, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("vegan", "Вегетерианская пицца", "Легкая и ненавязчивая пицца без мяса, короче кулич с сыром", 320, 350, pizzaCategory, "NOTHUB"));
//        foodList.add(new FoodItem("id#5", "Пицца №5", "Ошеломительне описание новой пиццы под номером 5, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("id#6", "Пицца №6", "Ошеломительне описание новой пиццы под номером 6, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("id#7", "Пицца №7", "Ошеломительне описание новой пиццы под номером 7, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("id#8", "Пицца №8", "Ошеломительне описание новой пиццы под номером 8, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("id#9", "Пицца №9", "Ошеломительне описание новой пиццы под номером 9, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
//        foodList.add(new FoodItem("id#10", "Пицца №10", "Ошеломительне описание новой пиццы под номером 10, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));

        fakeFoodList = new MutableLiveData<>(foodItems[0]);
        System.out.println("FAKE_DATA_LOADED");
    }

    public LiveData<List<FoodItem>> getFoodList() {
        return fakeFoodList;
    }

}