package com.allteran.pizzamira.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.util.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {
    private static final String TAG = "FIREBASE_HELPER";

    private DatabaseReference mReference;
    private List<FoodItem> mFoodList = new ArrayList<>();

    public interface DataStatus {
        void dataIsLoaded (List<FoodItem> foodList);
    }

    public FirebaseService(FirebaseDatabase database) {
        mReference = database.getReference();
    }

    /**
     * Current method will be called only when user get's sign in for the first time, so it will fill user
     * only with two fields: phone and UUID, other fields will be updated after forming first order
     */
    //TODO: test this and look how it will be displayed into database
    public void addUser(FirebaseUser firebaseUser) {
        Log.d(TAG, "ADDUSER: Going to add user to DB");
        Log.d(TAG, "ADDUSER: FirebaseUser's phone " + firebaseUser.getPhoneNumber());
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setPhone(firebaseUser.getPhoneNumber());
        mReference.child(Const.DB_TREE_USERS).child(firebaseUser.getUid()).setValue(user);
        Log.d(TAG, "ADDUSER: user added");
    }

    public void generateFakeFoodList() {
        String pizzaCategory = "pzzctgr";

        List<FoodItem> foodList = new ArrayList<>();
        foodList.add(new FoodItem("MARGARITA", "Пицца маргарита", "Изысканная пицейка та тоненьком тесте", 350, 450, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("peanaple", "Пицца с ананасами", "Странная и хипповая пицца с курицей и ананасиком", 220, 650, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("diavollo", "Пицца дьяволо", "жгучая, как слова бывшей пицейка с охренительно острым соусом", 341, 540, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("vegan", "Вегетерианская пицца", "Легкая и ненавязчивая пицца без мяса, короче кулич с сыром", 320, 350, pizzaCategory, "NOTHUB"));
        foodList.add(new FoodItem("id#5", "Пицца №5", "Ошеломительне описание новой пиццы под номером 5, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("id#6", "Пицца №6", "Ошеломительне описание новой пиццы под номером 6, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("id#7", "Пицца №7", "Ошеломительне описание новой пиццы под номером 7, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("id#8", "Пицца №8", "Ошеломительне описание новой пиццы под номером 8, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("id#9", "Пицца №9", "Ошеломительне описание новой пиццы под номером 9, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));
        foodList.add(new FoodItem("id#10", "Пицца №10", "Ошеломительне описание новой пиццы под номером 10, у меня кончились идеи", 120, 320, pizzaCategory, "NOTHUMB"));

        mReference.child(Const.DB_TREE_FOODLIST_FAKE).setValue(foodList);
    }

    public void loadFoodList(final DataStatus dataStatus) {
        mFoodList.clear();
        mReference.child(Const.DB_TREE_FOODLIST_FAKE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        FoodItem item = childSnap.getValue(FoodItem.class);
                        mFoodList.add(item);
                    }
                }
                dataStatus.dataIsLoaded(mFoodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addFoodItemToCart(String userId, String foodItemId) {

    }

//    public User findUserById(String id) {
//        Log.d(TAG, "FINDUSERBYID: 1. let's find user with id " + id);
//        final DataSnapshot[] userSnapshot = new DataSnapshot[1];
//        mReference.child(Const.DB_TREE_USERS).child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                Log.d(TAG, "FINDUSERBYID: task");
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "FINDUSERBYID: task is successful");
//                    Log.d(TAG, task.getResult().toString());
//                    userSnapshot[0] = task.getResult();
//                } else {
//                    Log.e(TAG, "FindUserById TASK FAILED", task.getException());
//                }
//            }
//        });
//        if(userSnapshot[0]==null) {
//            Log.d(TAG, "FINDUSERBYID: usersnapshot[0] = null");
//            return null;
//        }
//        return (User) userSnapshot[0].getValue();
//    }
}
