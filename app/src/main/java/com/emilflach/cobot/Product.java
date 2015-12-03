package com.emilflach.cobot;

/**
 * Created by Emil on 2015-11-24.
 */
public class Product {
    int id;
    String name;
    String image;
    int type;
    int strength;
    int milk;
    int sugar;
    boolean mug;

    Product(int id, String name, String image, int type, int strength, int milk, int sugar, boolean mug) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.strength = strength;
        this.milk = milk;
        this.sugar = sugar;
        this.mug = mug;

    }
}
