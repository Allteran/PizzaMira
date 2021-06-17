package com.allteran.pizzamira.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.allteran.pizzamira.R;
import com.allteran.pizzamira.model.FoodItem;
import com.allteran.pizzamira.model.Order;
import com.allteran.pizzamira.util.Const;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.types.ObjectId;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Realm will hold only a little part of data - current order. When user wants to make an order (a real one)
 * data will be transferred to Firebase Database and deleted from realm. This is how we gonna win in perfomance
 */

public class RealmService {
    private static final String TAG = "REALM_SERVICE";
    private AppCompatActivity mActivity;


    public RealmService(Activity activity) {
        mActivity = (AppCompatActivity) activity;
    }

    //Empty constructor required for situation when you don't have to update badges
    public RealmService() {

    }


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
            int fullPrice = foodItem.getPrice() * foodItem.getCountInCart();
            realmOrder.setFullPrice(fullPrice);

        }, () -> {
            Log.d(TAG, "createOrder: onSuccess");
            realm.beginTransaction();
            Order order = realm.where(Order.class).findFirst();
            if (order != null) {
                updateCartBadgeCount(order, mActivity);
            }
            realm.commitTransaction();
        }, error -> {
            Log.e(TAG, "createOrder: onError", error);
        });
    }

    /**
     * We should check if @return value is null. In that case we would create a new order. If value
     *
     * @return != null => we will pull it from database and work with it further
     */
    public Order getCurrentOrder(Realm realm) {
        realm.beginTransaction();
        Order order = realm.where(Order.class).findFirst();
        realm.commitTransaction();
        return order;
    }

    public void changeItemCount(Realm realm, FoodItem item, int count) {
        Log.d(TAG, "changeItemCount");
        realm.beginTransaction();
        int fullPrice = 0;
        Order order = realm.where(Order.class).findFirst();
        for (FoodItem foodItem : order.getFoodList()) {
            String id = foodItem.getId();
            if (id.equals(item.getId())) {
                foodItem.setCountInCart(count);
            }
            fullPrice += foodItem.getPrice() * foodItem.getCountInCart();
        }
        order.setFullPrice(fullPrice);

        updateCartBadgeCount(order, mActivity);
        realm.commitTransaction();
    }

    public void deleteItemFromOrder(Realm realm, FoodItem item) {
        Log.d(TAG, "deleteItemFromOrder");
        realm.beginTransaction();
        Order order = realm.where(Order.class).findFirst();
        for (Iterator<FoodItem> iterator = order.getFoodList().iterator(); iterator.hasNext(); ) {
            String id = iterator.next().getId();
            if (id.equals(item.getId())) {
                iterator.remove();
            }
        }

        int fullPrice = 0;
        for (FoodItem foodItem : order.getFoodList()) {
            fullPrice += foodItem.getPrice() * foodItem.getCountInCart();
        }
        order.setFullPrice(fullPrice);

        realm.commitTransaction();

        realm.beginTransaction();
        Order orderForBadges = realm.where(Order.class).findFirst();
        updateCartBadgeCount(orderForBadges, mActivity);
        realm.commitTransaction();
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
            int fullPrice = 0;
            for (FoodItem item : order.getFoodList()) {
                fullPrice += item.getPrice() * item.getCountInCart();
            }
            order.setFullPrice(fullPrice);

        }, () -> {
            Log.d(TAG, "addItemToOrder: execute successful");
            realm.beginTransaction();
            Order order = realm.where(Order.class).findFirst();
            if (order != null) {
                updateCartBadgeCount(order, mActivity);
            }
            realm.commitTransaction();
        }, error -> {
            Log.e(TAG, "addItemToOrder: onError", error);
        });
    }

    /**
     * Next method will show exactly number of ordered dishes into cart's badge
     * and update it when its needed
     */
    private void updateCartBadgeCount(Order order, AppCompatActivity activity) {
        Log.d(TAG, "updateBadge: init");
        BottomNavigationView navView = activity.findViewById(R.id.nav_view);
        int badgeCount = 0;
        BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_cart);
        for (FoodItem item : order.getFoodList()) {
            badgeCount = badgeCount + item.getCountInCart();
        }
        Log.d(TAG, "updateBadge: badgeCount = " + badgeCount);
        badge.setNumber(badgeCount);
        badge.setVisible(true, true);

        if (badgeCount <= 0) {
            badge.setVisible(false, true);
        }
    }

    public void updateOrderDetails(Realm realm, Order order) {
        Log.d(TAG, "updateOrderDetails");
        realm.beginTransaction();

        Order rOrder = realm.where(Order.class).findFirst();
        rOrder.setCustomerFirstName(order.getCustomerFirstName());
        rOrder.setCustomerSecondName(order.getCustomerSecondName());

        rOrder.setStreetName(order.getStreetName());
        rOrder.setBuildingNo(order.getBuildingNo());
        rOrder.setEntrance(order.getEntrance());
        rOrder.setIntercom(order.getIntercom());
        rOrder.setFloorNo(order.getFloorNo());
        rOrder.setAppNo(order.getAppNo());

        rOrder.setNumberOfPersons(rOrder.getNumberOfPersons());
        rOrder.setUserComment(order.getUserComment());

        rOrder.setPayType(order.getPayType());
        if (order.getPayType().equals(Const.PAY_CASH)) {
            rOrder.setChange(order.getChange());
        }

        realm.commitTransaction();

        Log.d(TAG, "orderDetails: done");
    }

    /**
     * This will show cart badge when app starts in case when there was unfinished order
     */
    public void showCartBadge(Realm realm) {
        realm.beginTransaction();
        Order order = realm.where(Order.class).findFirst();
        if (order != null) {
            updateCartBadgeCount(order, mActivity);
        }
        realm.commitTransaction();
    }
}
