package com.allteran.pizzamira.services;

import android.content.Context;

import com.allteran.pizzamira.model.Order;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Realm will hold only a little part of data - current order. When user wants to make an order (a real one)
 * data will be transferred to Firebase Database and deleted from realm. This is how we gonna win in perfomance
 */

public class RealmService {


    public static void initRealm (Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm.init(context);
        Realm.setDefaultConfiguration(config);
    }

    /**
     * TODO: use findFirst for ORDER, when you will consider to create order
     * There is should be only ONE order in database, because when order will change status -
     * it will be deleted from database
     */
    public void createOrder(Realm realm, Order order) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm iRealm) {
                Order realmOrder = iRealm.createObject(Order.class);
                realmOrder.setId(new ObjectId().toString().trim());
                realmOrder.setFoodListIds(order.getFoodListIds());
            }
        });
    }
}
