package com.example.myapplication.Items;

import android.graphics.Bitmap;

import com.example.myapplication.GameObj.Player;

public abstract class Item {

    private String name;
    private int id;
    private boolean usable;
    private int resourceID;

    public Item(String name, int id, boolean usable, int resourceID){
        this.name = name;
        this.id = id;
        this.resourceID = resourceID;
        this.usable = usable;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isUsable() {
        return usable;
    }

    public int getResourceID() {
        return resourceID;
    }

    public abstract String toString();
    public abstract void use(Player player, Inventory inventory);
}
