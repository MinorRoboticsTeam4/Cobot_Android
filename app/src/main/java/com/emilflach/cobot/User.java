package com.emilflach.cobot;

/**
 * Created by Emil on 2015-11-23.
 */
public class User {
    int id;
    String name;
    String email;
    String password;
    String location;

    User(int id, String name, String email, String password, String location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
    }
}
