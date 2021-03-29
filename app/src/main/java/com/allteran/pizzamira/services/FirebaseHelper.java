package com.allteran.pizzamira.services;

import com.allteran.pizzamira.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
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
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setPhone(firebaseUser.getPhoneNumber());
    }
}
