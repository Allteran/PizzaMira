package com.allteran.pizzamira.services;

import android.content.Context;
import android.util.Log;

import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

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

    public static void deleteDatabase() {
        RealmConfiguration config = Realm.getDefaultConfiguration();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
        Realm.deleteRealm(config);
    }

    /**
     * There is should be only ONE order in database, because when order will change status -
     * it will be deleted from database for sure
     * In first time of init, you'd like to add first FoodItem to order
     */
    public void createOrder(Realm realm, FoodItem foodItem) {
        realm.executeTransactionAsync(iRealm -> {
            Log.d(TAG, "createOrder: execute");
            Order realmOrder = iRealm.createObject(Order.class);
            realmOrder.setId(new ObjectId().toString().trim());

            RealmList<FoodItem> rFoodList = new RealmList<>();

            FoodItem rFoodItem = iRealm.createObject(FoodItem.class);
            rFoodItem.setId(foodItem.getId());
            rFoodItem.setCategoryId(foodItem.getCategoryId());
            rFoodItem.setName(foodItem.getName());
            rFoodItem.setThumbSrc(foodItem.getThumbSrc());
            rFoodItem.setDescription(foodItem.getDescription());
            rFoodItem.setPrice(foodItem.getPrice());
            rFoodItem.setWeight(foodItem.getWeight());
            rFoodItem.setCountInCart(foodItem.getCountInCart());

            rFoodList.add(rFoodItem);
            realmOrder.setFoodList(rFoodList);
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
    public Order getCurrentOrder(Realm realm) {
        realm.beginTransaction();
        Order order = realm.where(Order.class).findFirst();
        realm.commitTransaction();
        return order;
    }

    public void addItemToOrder(Realm realm, FoodItem foodItem) {
        realm.executeTransactionAsync(r -> {
            Order order = r.where(Order.class).findFirst();
            FoodItem rFoodItem = r.createObject(FoodItem.class);
            rFoodItem.setId(foodItem.getId());
            rFoodItem.setCategoryId(foodItem.getCategoryId());
            rFoodItem.setName(foodItem.getName());
            rFoodItem.setThumbSrc(foodItem.getThumbSrc());
            rFoodItem.setDescription(foodItem.getDescription());
            rFoodItem.setPrice(foodItem.getPrice());
            rFoodItem.setWeight(foodItem.getWeight());
            rFoodItem.setCountInCart(1);

            if (order != null) {
                if (order.getFoodList().isEmpty()) {
                    order.getFoodList().add(rFoodItem);
                }
                //TODO: doesn't work properly
                boolean itemInCart = false;
                for (FoodItem item : order.getFoodList()) {
                    String itemId = item.getId();
                    if (itemId.equals(foodItem.getId())) {
                        int count = item.getCountInCart() + 1;
                        item.setCountInCart(count);
                        itemInCart = true;
                    }
                }
                if (!itemInCart) {
                    order.getFoodList().add(rFoodItem);
                }
            }
        }, () -> {
            Log.d(TAG, "addItemToOrder: execute successful");
        }, error -> {
            Log.e(TAG, "addItemToOrder: onError", error);
        });
    }


}
