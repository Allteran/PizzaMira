package com.allteran.pizzamira.model;

import com.allteran.pizzamira.util.Const;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Order extends RealmObject {
    private String id;
    private String userId;
    private RealmList<String> foodListIds;
    private int fullPrice;
    private OrderStatus status;
    private String payType;
    private Date creationDate;
    private Date payDate;
    private String streetName; // name of street for order to deliver
    private String buildingNo; // number of building including slash or dashes like '4/25'
    private int appNo; // number of appartaments
    private int floorNo; // number of floor
    private String userComment;
    private String operatorComment; // for emergency comment from operator
    private int numberOfPersons;

    public Order() {
    }

    public Order(String id, String userId, RealmList<String> foodListIds, int fullPrice) {
        this.id = id;
        this.userId = userId;
        this.fullPrice = fullPrice;
        this.status = new OrderStatus(Const.ID_ORDER_STATUS_NEW, Const.ORDER_STATUS_NEW);
        this.creationDate = new Date();
        this.foodListIds = foodListIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RealmList<String> getFoodListIds() {
        return foodListIds;
    }

    public void setFoodListIds(RealmList<String> foodListIds) {
        this.foodListIds = foodListIds;
    }

    public int getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(int fullPrice) {
        this.fullPrice = fullPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
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
}
