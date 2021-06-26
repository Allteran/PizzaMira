package com.allteran.pizzamira.model;

/**
 * Current class will hold only two fields. It should be for less weight of database
 * for tree 'Order' hold less data
 */
public class FirebaseFoodItem {
    private String id;
    private int quantity;

    public FirebaseFoodItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
