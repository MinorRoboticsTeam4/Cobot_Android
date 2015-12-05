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
    private int location;

    User(int id, String name, String email, String password, int location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
