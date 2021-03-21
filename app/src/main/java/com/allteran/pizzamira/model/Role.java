package com.allteran.pizzamira.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Role extends RealmObject {
    @PrimaryKey
    private String role;
    private String displayName;

    public Role() {
    }

    public Role(String role, String displayName) {
        this.role = role;
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
