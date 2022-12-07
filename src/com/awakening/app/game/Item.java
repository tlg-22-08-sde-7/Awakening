package com.awakening.app.game;

public class Item {
    private String name;
    private String description;
    // the charge variable will hold the charge of the camera item
    private int charge;


    public Item(){}

    public Item(String name, String description){
        this.name = name;
        this.description=description;
    }

    public Item(String name, String description, int charge){
        this.name = name;
        this.description = description;
        this.charge = charge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }
}
