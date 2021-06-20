package com.allteran.pizzamira.model;

import com.allteran.pizzamira.util.Const;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Order extends RealmObject {
    private String id;

    private String userPhone;
    private String customerFirstName;
    private String customerSecondName;

    private RealmList<FoodItem> foodList;
    private int fullPrice;

    private String status; //store only status ID
    private String payType;

    private Date creationDate;
    private Date payDate;

    private String city;
    private String streetName; // name of street for order to deliver
    private String buildingNo; // number of building including slash or dashes like '4/25'
    private int entrance;
    private String intercom;
    private int appNo; // number of appartaments
    private int floorNo; // number of floor

    private String userComment;
    private String operatorComment; // for emergency comment from operator
    private int numberOfPersons;
    private int change;

    public Order() {
    }

    public Order(String id, String userPhone, int fullPrice) {
        this.id = id;
        this.userPhone = userPhone;
        this.fullPrice = fullPrice;
        this.status = Const.ID_ORDER_STATUS_NEW;
        this.creationDate = new Date();
        this.foodList = new RealmList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerSecondName() {
        return customerSecondName;
    }

    public void setCustomerSecondName(String secondName) {
        this.customerSecondName = secondName;
    }

    public RealmList<FoodItem> getFoodList() {
        return foodList;
    }

    public void setFoodList(RealmList<FoodItem> foodList) {
        this.foodList = foodList;
    }

    public int getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(int fullPrice) {
        this.fullPrice = fullPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getEntrance() {
        return entrance;
    }

    public void setEntrance(int entrance) {
        this.entrance = entrance;
    }

    public String getIntercom() {
        return intercom;
    }

    public void setIntercom(String intercom) {
        this.intercom = intercom;
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

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getOperatorComment() {
        return operatorComment;
    }

    public void setOperatorComment(String operatorComment) {
        this.operatorComment = operatorComment;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }
}
