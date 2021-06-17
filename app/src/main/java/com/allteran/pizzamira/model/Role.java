package com.allteran.pizzamira.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Role extends RealmObject {
    public static final String ID_ADMIN="admin";
    public static final String ID_MANAGER="manager";
    public static final String ID_CUSTOMER="customer";

    public static final String NAME_ADMIN="Администратор";
    public static final String NAME_MANAGER="Менеджер";
    public static final String NAME_CUSTOMER="Пользователь";

    @PrimaryKey
    private String role;
    private String displayName;

    public Role() {
    }

    public Role(String roleId, String displayName) {
        this.role = roleId;
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
