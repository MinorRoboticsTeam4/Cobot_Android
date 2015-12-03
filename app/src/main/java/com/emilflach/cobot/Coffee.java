package com.emilflach.cobot;

/**
 * Created by Emil on 2015-11-03.
 */
public class Coffee {

    String name;
    int photoId;
    int strength;
    int milk;
    int sugar;
    boolean mug;
    String location;
    boolean expanded;

    Coffee(String name, int photoId, int strength, int milk, int sugar, boolean mug, String location, boolean expanded) {
        this.name = name;
        this.photoId = photoId;
        this.strength = strength;
        this.milk = milk;
        this.sugar = sugar;
        this.mug = mug;
        this.location = location;
        this.expanded = expanded;
    }
}