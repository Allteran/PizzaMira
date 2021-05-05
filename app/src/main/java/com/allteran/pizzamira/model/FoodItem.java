package com.allteran.pizzamira.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class FoodItem implements RealmModel {
    private String id;
    private String name;
    private String description;
    private int weight;
    private int price;
    private String categoryId;
    private String thumbSrc;
    private int countInCart;

    public FoodItem() {
    }

    public FoodItem(String id, String name, String description, int weight, int price, String categoryId, String thumbSrc) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.price = price;
        this.categoryId = categoryId;
        this.thumbSrc = thumbSrc;
    }

    public String getThumbSrc() {
        return thumbSrc;
    }

    public void setThumbSrc(String thumbSrc) {
        this.thumbSrc = thumbSrc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCountInCart() {
        return countInCart;
    }

    public void setCountInCart(int countInCart) {
        this.countInCart = countInCart;
    }
}
