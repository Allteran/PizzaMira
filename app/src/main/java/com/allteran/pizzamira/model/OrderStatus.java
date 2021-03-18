package com.allteran.pizzamira.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OrderStatus extends RealmObject {
    @PrimaryKey
    private String id;
    private String status;

    public OrderStatus() {
    }

    public OrderStatus(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
