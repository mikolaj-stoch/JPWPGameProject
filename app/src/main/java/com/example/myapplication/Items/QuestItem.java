package com.example.myapplication.Items;

import com.example.myapplication.GameObj.Player;

public class QuestItem extends Item {

    public static final int KEY1_ID = 101;

    public QuestItem(String name, int id, boolean usable, int resourceID){
        super(name, id, usable, resourceID);
    }
    @Override
    public String toString() {
        return "Key";
    }

    @Override
    public void use(Player player, Inventory inventory) {

    }
}
