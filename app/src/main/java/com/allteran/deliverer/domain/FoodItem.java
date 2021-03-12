package com.allteran.deliverer.domain;

public class FoodItem {
    private String id;
    private String name;
    private String description;
    private int weight;
    private int price;
    private FoodCategory category;
    private String thumbSrc;

    public FoodItem() {
    }

    public FoodItem(String id, String name, String description, int weight, int price, FoodCategory category, String thumbSrc) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.price = price;
        this.category = category;
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

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
