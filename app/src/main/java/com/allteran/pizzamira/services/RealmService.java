package com.allteran.pizzamira.services;

import android.content.Context;
import android.util.Log;

import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Realm will hold only a little part of data - current order. When user wants to make an order (a real one)
 * data will be transferred to Firebase Database and deleted from realm. This is how we gonna win in perfomance
 */

public class RealmService {
    private static final String TAG = "REALM_SERVICE";


    public static void initRealm(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
        Realm.init(context);
    }

    /**
     * There is should be only ONE order in database, because when order will change status -
     * it will be deleted from database
     */
    public void createOrder(Realm realm) {
        Order order = new Order();
        realm.executeTransactionAsync(iRealm -> {
            Log.d(TAG, "createOrder: execute");
            Order realmOrder = iRealm.createObject(Order.class);
            realmOrder.setId(new ObjectId().toString().trim());
        }, () -> {
            Log.d(TAG, "createOrder: onSuccess");
        }, error -> {
            Log.e(TAG, "createOrder: onError", error);
        });
    }

    /**
     * We should check if @return value is null. In that case we would create a new order. If value
     * @return != null => we will pull it from database and work with it further
     */
    //TODO: ERROR: java.lang.IllegalArgumentException: Null objects cannot be copied from Realm
    public Order getCurrentOrder(Realm realm) {
        realm.beginTransaction();
        Order order = realm.copyFromRealm(realm.where(Order.class)
                .findFirst());
        realm.commitTransaction();
        return order;
    }

    public void addItemToOrder(Realm realm, FoodItem foodItem) {
        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "addItemToOrder: execute");
            Order order = r.where(Order.class).findFirst();
            assert order != null;
            order.getFoodListIds().add(foodItem.getId());
        }, () -> {
            Log.d(TAG, "addItemToOrder: execute");
        }, error -> {
            Log.e(TAG, "addItemToOrder: onError", error);
        });
    }


}
