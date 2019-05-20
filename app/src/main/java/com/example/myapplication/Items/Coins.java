package com.example.myapplication.Items;

import com.example.myapplication.GameObj.Player;

public class Coins extends Item {

    public static final int COIN_ID = 1000;

    public Coins(String name, int id, boolean usable, int resourceID) {
        super(name, id, usable, resourceID);
    }

    @Override
    public String toString() {
        return "Coins are useless, because we haven't programed them yet";
    }

    @Override
    public void use(Player player, Inventory inventory) {

    }
}
