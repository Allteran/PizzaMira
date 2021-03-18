package com.allteran.pizzamira.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FoodCategory extends RealmObject  {
    @PrimaryKey
    private String id;
    private String name;

    public FoodCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public FoodCategory() {
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
}
