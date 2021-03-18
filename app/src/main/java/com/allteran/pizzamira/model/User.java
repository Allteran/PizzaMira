package com.allteran.pizzamira.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private String id;
    private String city;
    private String street;
    private String buildNo; //building number include number with slash or dash like '47/2\
    private int appNo; // appartament number
    private int floorNo; // number of floor
    private RealmList<String> orderHistoryIds; //it would save orders only with status 'PAID'
    private Order currentOrder;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildNo() {
        return buildNo;
    }

    public void setBuildNo(String buildNo) {
        this.buildNo = buildNo;
    }

    public int getAppNo() {
        return appNo;
    }

    public void setAppNo(int appNo) {
        this.appNo = appNo;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    public RealmList<String> getOrderHistory() {
        return orderHistoryIds;
    }

    public void setOrderHistory(RealmList<String> orderHistory) {
        this.orderHistoryIds = orderHistory;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
