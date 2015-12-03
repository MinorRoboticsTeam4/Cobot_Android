package com.emilflach.cobot;

/**
 * Created by Emil on 2015-11-24.
 */
public class Order {
    int id;
    int user;
    String location;
    int delivery_status;
    String delivered_at;

    Order(int id, int user, String location, int delivery_status, String delivered_at) {
        this.id = id;
        this.user = user;
        this.location = location;
        this.delivery_status = delivery_status;
        this.delivered_at = delivered_at;
    }
}
