package com.emilflach.cobot.Models;

/**
 * cobot
 * by Emil on 2015-11-03.
 */
public class Coffee {

    private String name;
    private int photoId;
    private int strength;
    private int milk;
    private int sugar;
    private boolean mug;
    private String location;
    private boolean expanded;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}