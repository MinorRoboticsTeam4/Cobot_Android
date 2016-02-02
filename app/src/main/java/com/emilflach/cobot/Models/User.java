package com.emilflach.cobot.Models;

/**
 * cobot
 * by Emil on 2015-11-23.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int location_id;
    private int product_id;
    private String location;

    User(int id, String name, String email, String password, int location_id, int product_id, String location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location_id = location_id;
        this.product_id = product_id;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
