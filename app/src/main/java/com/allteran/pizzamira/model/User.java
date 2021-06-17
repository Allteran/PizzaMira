package com.allteran.pizzamira.model;

import java.util.List;

public class User  {
    private String id;
    private String phone;
    private String firstName;
    private String secondName;
    private Role role;
    private String city;
    private String street;
    private String buildNo; //building number include number with slash or dash like '47/2\
    private int appNo; // appartament number
    private int floorNo; // number of floor
    private List<String> orderHistoryIds; //it would save orders only with status 'PAID'
    private Order currentOrder;

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public List<String> getOrderHistoryIds() {
        return orderHistoryIds;
    }

    public void setOrderHistoryIds(List<String> orderHistoryIds) {
        this.orderHistoryIds = orderHistoryIds;
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

    public List<String> getOrderHistory() {
        return orderHistoryIds;
    }

    public void setOrderHistory(List<String> orderHistory) {
        this.orderHistoryIds = orderHistory;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
