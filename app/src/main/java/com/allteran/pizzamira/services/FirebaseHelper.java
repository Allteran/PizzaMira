package com.allteran.pizzamira.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.allteran.pizzamira.model.User;
import com.allteran.pizzamira.util.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static final String TAG = "FIREBASE_HELPER";

    private DatabaseReference mReference;

    public FirebaseHelper(FirebaseDatabase database) {
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

    public User findUserById(String id) {
        Log.d(TAG, "FINDUSERBYID: 1. let's find user with id " + id);
        final DataSnapshot[] userSnapshot = new DataSnapshot[1];
        mReference.child(Const.DB_TREE_USERS).child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d(TAG, "FINDUSERBYID: task");
                if (task.isSuccessful()) {
                    Log.d(TAG, "FINDUSERBYID: task is successful");
                    Log.d(TAG, task.getResult().toString());
                    userSnapshot[0] = task.getResult();
                    notifyAll();
                } else {
                    Log.e(TAG, "FindUserById TASK FAILED", task.getException());
                }
            }
        });
        if(userSnapshot[0]==null) {
            Log.d(TAG, "FINDUSERBYID: usersnapshot[0] = null");
            return null;
        }
        return (User) userSnapshot[0].getValue();
    }
}
