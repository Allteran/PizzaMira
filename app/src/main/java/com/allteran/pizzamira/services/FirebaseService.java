package com.allteran.pizzamira.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
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

    public interface DataStatus {
        void dataIsLoaded(List<FoodItem> foodList);

        void dataIsLoaded(User user);

        void dataIsLoaded(Order order);
    }

    public FirebaseService(FirebaseDatabase database) {
        mReference = database.getReference();
    }

    /**
     * Current method will be called only when user get's sign in for the first time, so it will fill user
     * only with two fields: phone and UUID, other fields will be updated after forming first order
     */
    public void addUser(FirebaseUser firebaseUser) {
        Log.d(TAG, "ADDUSER: Going to add user to DB");
        Log.d(TAG, "ADDUSER: FirebaseUser's phone " + firebaseUser.getPhoneNumber());
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setPhone(firebaseUser.getPhoneNumber());
        mReference.child(Const.DB_TREE_USERS).child(firebaseUser.getUid()).setValue(user);
        Log.d(TAG, "ADDUSER: user added");
    }

    public void loadFoodList(final DataStatus dataStatus) {
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

            }
        });
    }

    public void loadCurrentOrder(String userId, String orderId, final DataStatus dataStatus) {
        List<FoodItem> foodList = new ArrayList<>();
        mReference.child(Const.DB_TREE_CART).child(userId).child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot childSnap : snapshot.getChildren()) {
                        FoodItem item = childSnap.getValue(FoodItem.class);
                        foodList.add(item);
                    }
                    dataStatus.dataIsLoaded(foodList);
                } else {
                    Log.d(TAG, "loadcart: snapshot doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "loadCart: onCanceled ", error.toException());
            }
        });
    }

//    public void addFoodToOrder(String userId, FoodItem foodItem, Order order) {
//        Log.d(TAG, "ADDITEMTOORDER: begin");
//        order.getFoodListIds().add(foodItem.getId());
//        mReference.child(Const.DB_TREE_CART).child(userId).setValue(order);
//    }
//
//    public void pullCurrentOrder(String userId, final DataStatus dataStatus) {
//        mReference.child(Const.DB_TREE_CART).child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()) {
//                    if(task.getResult().exists()) {
//                        Order order = task.getResult().getValue(Order.class);
//                        dataStatus.dataIsLoaded(order);
//                    } else {
//                        Log.d(TAG, "pullCurrentOrder: there is no such order");
//                    }
//                } else {
//                    Log.d(TAG, "pullCurrentOrder: task was not successful", task.getException());
//                }
//            }
//        });
//    }

    public void findUserById(String userId, final DataStatus dataStatus) {
        mReference.child(Const.DB_TREE_USERS).child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    User user = task.getResult().getValue(User.class);
                    dataStatus.dataIsLoaded(user);
                } else {
                    Log.e(TAG, "finduserById: task is failed", task.getException());
                }
            }
        });
    }

}
