package com.emilflach.cobot.Models;

/**
 * cobot
 * by Emil on 2015-11-24.
 */
public class Order {
    private int id = 0;
    private int user = 0;
    private int location = 0;
    private int delivery_status = 0;
    private String delivered_at = "No time";

    public Order() {
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
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
