package com.example.myapplication.Items;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.myapplication.GameObj.Player;

import java.util.Map;

public class Potion extends Item {

    private int value;
    private Bitmap texture;
    private String type;

    public static final int HEALING_ID = 0;
    public static final int STAMINA_ID = 1;

    public static final int HEALING_M_ID = 2;
    public static final int STAMINA_M_ID = 3;

    public Potion(String name, int id, int value, int resourceID) {
        super(name, id, true, resourceID);
        this.value = value;

        setType();
    }

    private void setType() {
        switch (getId()){
            case HEALING_ID:
            case HEALING_M_ID:
                type = "hp";
                break;
            case STAMINA_ID:
            case STAMINA_M_ID:
                type = "stamina";
                break;
        }
    }

    @Override
    public void use(Player player, Inventory inventory){

        switch (getId()){
            case HEALING_ID:
            case HEALING_M_ID:
                if(inventory.removeItem(this)){
                    player.restoreHp(value);
                    Log.i("Potion: ", "healing potion used");
                }
                break;
            case STAMINA_ID:
            case STAMINA_M_ID:
                if(inventory.removeItem(this)){
                    player.restoreStamina(value);
                    Log.i("Potion: ", "stamina potion used");
                }
                break;

        }
    }


    @Override
    public String toString() {
        return getName() + "\n\n" + "Restores: " + this.value + " " + type;
    }
}
