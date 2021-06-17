package com.allteran.pizzamira.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.model.Role;
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


    public interface UserDataStatus {
        void dataIsLoaded(User user);

        void onLoadError(@NonNull DatabaseError error);
    }

    public interface FoodDataStatus {
        void dataIsLoaded(List<FoodItem> foodList);

        void onLoadError(@NonNull DatabaseError error);
    }

    public interface OrderDataStatus {
        void dataIsLoaded(Order order);

        void onLoadError(@NonNull DatabaseError error);
    }

    public FirebaseService(FirebaseDatabase database) {
        mReference = database.getReference();
    }

    /**
     * Current method will be called only when user get's sign in for the first time, so it will fill user
     * only with two fields: phone and UUID, other fields will be updated after forming first order
     */
    public void addUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setPhone(firebaseUser.getPhoneNumber());
        user.setRole(new Role(Role.ID_CUSTOMER, Role.NAME_CUSTOMER));
        mReference.child(Const.DB_TREE_USERS).child(firebaseUser.getUid()).setValue(user);
    }

    public void loadFoodList(final FoodDataStatus dataStatus) {
        List<FoodItem> foodList = new ArrayList<>();
        mReference.child(Const.DB_TREE_FOODLIST_FAKE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        FoodItem item = childSnap.getValue(FoodItem.class);
                        foodList.add(item);
                    }
                }
                dataStatus.dataIsLoaded(foodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataStatus.onLoadError(error);
            }

        });
    }

    public void getFoodListByIds(List<String> foodListIds, final FoodDataStatus dataStatus) {
        List<FoodItem> foodList = new ArrayList<>();
        mReference.child(Const.DB_TREE_FOODLIST_FAKE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        FoodItem item = childSnap.getValue(FoodItem.class);
                        for (String foodId : foodListIds) {
                            if (foodId.equals(item.getId())) {
                                foodList.add(item);
                            }
                        }
                    }
                    dataStatus.dataIsLoaded(foodList);
                } else {
                    Log.d(TAG, "loadFoodListByIds: snapshot doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "loadFoodListByIds: onCanceled ", error.toException());
                dataStatus.onLoadError(error);
            }
        });
    }

    public void findUserById(String userId, final UserDataStatus dataStatus) {
        mReference.child(Const.DB_TREE_USERS).child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().getValue(User.class);
                dataStatus.dataIsLoaded(user);
            } else {
                Log.e(TAG, "finduserById: task is failed", task.getException());
            }
        });
    }

}
