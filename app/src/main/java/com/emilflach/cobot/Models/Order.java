package com.emilflach.cobot.Models;

/**
 * cobot
 * by Emil on 2015-11-24.
 */
public class Order {
    private int id;
    private int user;
    private String location;
    private int delivery_status;
    private String delivered_at;

    Order(int id, int user, String location, int delivery_status, String delivered_at) {
        this.id = id;
        this.user = user;
        this.location = location;
        this.delivery_status = delivery_status;
        this.delivered_at = delivered_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(int delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getDelivered_at() {
        return delivered_at;
    }

    public void setDelivered_at(String delivered_at) {
        this.delivered_at = delivered_at;
    }
}
