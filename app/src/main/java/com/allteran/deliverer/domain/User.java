package com.allteran.deliverer.domain;

import java.util.List;

public class User {
    private String id;
    private String city;
    private String street;
    private String buildNo; //building number include number with slash or dash like '47/2\
    private int appNo; // appartament number
    private int floorNo; // number of floor
    private List<Order> orderHistory; //it would save orders only with status 'PAID'
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

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
