package com.emilflach.cobot.Models;

/**
 * cobot
 * by Emil on 2015-11-24.
 */
public class Product {
    private int id;
    private String name;
    private String image;
    private int type;
    private int strength;
    private int milk;
    private int sugar;
    private boolean mug;
    private boolean isNFC;

    Product(int id, String name, String image, int type, int strength, int milk, int sugar, boolean mug, boolean isNFC) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.strength = strength;
        this.milk = milk;
        this.sugar = sugar;
        this.mug = mug;
        this.isNFC = isNFC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getMilk() {
        return milk;
    }

    public void setMilk(int milk) {
        this.milk = milk;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public boolean isMug() {
        return mug;
    }

    public void setMug(boolean mug) {
        this.mug = mug;
    }

    public boolean isNFC() {
        return isNFC;
    }

    public void setIsNFC(boolean isNFC) {
        this.isNFC = isNFC;
    }
}
