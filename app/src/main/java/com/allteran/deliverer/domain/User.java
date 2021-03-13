package com.allteran.deliverer.domain;

import java.util.List;

public class User {
    private String id;
    private String city;
    private String street;
    private String buildNo; //building number include number with slash or dash like '47/2\
    private int appNo; // appartament number
    private int floorNo; // number of floor
    private List<Order> orderHistory;
    private Order currentOrder;

}
