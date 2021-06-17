package com.allteran.pizzamira.util;

public final class Const {

    /**
     * Order statuses
     */
    public static final String ID_ORDER_STATUS_NEW = "order_status_new";
    public static final String ID_ORDER_STATUS_COOKING = "order_status_cooking";
    public static final String ID_ORDER_STATUS_DELIVERING = "order_status_delivering";
    public static final String ID_ORDER_STATUS_DELIVERED = "order_status_delivered";
    public static final String ID_ORDER_STATUS_PAID = "order_status_paid";

    public static final String ORDER_STATUS_NEW = "Новый заказ";
    public static final String ORDER_STATUS_COOKING = "Готовим!";
    public static final String ORDER_STATUS_DELIVERING = "Заказ уже едет к Вам!";
    public static final String ORDER_STATUS_DELIVERED = "Заказ доставлен";
    public static final String ORDER_STATUS_PAID = "Оплачено";

    /**
     * Payment type
     */
    public static final String PAY_CASH = "Наличные";
    public static final String PAY_CARD = "Карта";

    public static final String DB_TREE_USERS = "users";


    public static final String DB_TREE_FOODLIST_FAKE = "fake_food_list";
    public static final String DB_TREE_CART = "users_cart";

    public static final String KEY_FROM_LOGIN = "arg_activity_launch_key";
    public static final String ARG_FROM_LOGIN_TO_CART = "arg_activity_cart";
    public static final String ARG_FROM_LOGIN_TO_MAIN = "arg_activity_main";
}
