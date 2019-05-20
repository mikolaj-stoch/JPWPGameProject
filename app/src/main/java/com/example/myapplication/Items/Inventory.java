package com.example.myapplication.Items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.myapplication.BitmapSheet;
import com.example.myapplication.GameObj.Combat;
import com.example.myapplication.GameObj.GameObject;
import com.example.myapplication.Mission;
import com.example.myapplication.R;

import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//change map to list - simpler !

//---------ID-----------
// potions: 0-9
// primaryWeapons: 10-19
// helmet: 20-29
// chest: 30-39
// secondaryWeapons: 40-49
//-----------------------

public class Inventory {
    private Map<Item, Integer> items;
    private ArrayList<Item> itemList;
    private ArrayList<Item> itemBase;
//    public static Map<String, Integer> itemsBase;       //<name, id>

    //list of available items if the number increases, create separate class to manage items
    private Potion healingPotion;
    private Potion staminaPotion;
    private Potion healingPotionM;
    private Potion staminaPotionM;

    private QuestItem key1;

    private Weapon sword0;
    private Weapon sword1;

    private Weapon helmet0;
    private Weapon helmet1;

    private Weapon shield0;
    private Weapon shield1;

    private Coins coins;

    private Context context;

    public Inventory(Context context){
        this.context = context;
        this.items = new HashMap<>();
        this.itemList = new ArrayList<>();
        this.itemBase = new ArrayList<>();
//        this.itemsBase = new HashMap<>();

        initItems();
        updateBase();
    }

    private void initItems() {

        healingPotion = new Potion(context.getString(R.string.healingPotion), Potion.HEALING_ID, 5, R.drawable.hp_s);
        staminaPotion = new Potion(context.getString(R.string.staminaPotion), Potion.STAMINA_ID, 5, R.drawable.stamina_s);

        healingPotionM = new Potion("Medium healing potion", Potion.HEALING_M_ID, 10, R.drawable.hp_m);
        staminaPotionM = new Potion("Medium stamina potion", Potion.STAMINA_M_ID, 10, R.drawable.stamina_m);

        sword0 = new Weapon("Wooden Sword", Weapon.SWORD_0_ID,
                false, R.drawable.sword_01a, 1, 1, 2, 0, Weapon.WeaponType.PRIMARY_WEAPON);
        sword1 = new Weapon("Silver Sword", Weapon.SWORD_1_ID,
                false, R.drawable.sword_01b, 2, 3, 2, 0, Weapon.WeaponType.PRIMARY_WEAPON);

        helmet0 = new Weapon("Bronze Helmet", Weapon.HELMET_0_ID,
                false, R.drawable.helmet_02a, 0, 1, 1, 1, Weapon.WeaponType.HELMET);

        helmet1 = new Weapon("Silver Helmet", Weapon.HELMET_1_ID,
                false, R.drawable.helmet_02b, 1, 1, 2, 2, Weapon.WeaponType.HELMET);

        shield0 = new Weapon("Bronze Shield", Weapon.SHIELD_0_ID,
                false, R.drawable.shield_01a, 0, 0, 0, 2, Weapon.WeaponType.SECONDARY_WEAPON);

        shield1 = new Weapon("Silver Shield", Weapon.SHIELD_1_ID,
                false, R.drawable.shield_02b, 1, 0, 0, 4, Weapon.WeaponType.SECONDARY_WEAPON);

        key1 = new QuestItem("Key1", QuestItem.KEY1_ID, false, R.drawable.key_01d);

        coins = new Coins("Coins", Coins.COIN_ID, false, R.drawable.coin_04d);
    }

    public void addItem(Item item){
        if (items.containsKey(item)){
            items.put(item, items.get(item)+1);
            Log.i("Inventory", "ADD/Number of " + item.getName() + ": " + items.get(item));
        }
        else {
            items.put(item, 1);
            itemList.add(item);
            Log.i("Inventory", "ADD/Number of " + item.getName() + ": " + items.get(item));
        }
    }

    public boolean removeItem(Item item){
        if(items.containsKey(item)){
            if(items.get(item) > 1){
                items.put(item, items.get(item)-1);
                Log.i("Inventory", "REMOVE/Number of " + item.getName() + ": " + items.get(item));
            }
            else {
                items.remove(item);
                itemList.remove(item);
                Log.i("Inventory", "REMOVE/Number of " + item.getName() + ": " + items.get(item));
            }
            return true;
        }
        else {
            Log.e("Inventory", "Inventory doesn't contain this item");
            return false;
        }
    }

    public Item decodeItem(int id){
        for(Item item: itemBase){
            if(item.getId() == id){
                return item;
            }
        }
        Log.e("Invetory", "decodeItem/ There is no item of index " + id + " in base");
        return null;
    }

    public void addLoot(Combat enemy){
        if(enemy == null){
            Log.d("Invetory", "enemy is a null !");
        }
        int[] loots = enemy.getLoot();
        if(loots == null){
            Log.d("Invetory", "loots is a null !");
        }
        for(int lootId: loots){
            this.addItem(decodeItem(lootId));
        }
    }

    public void addLoot(Mission mission){
        if(mission == null){
            Log.d("Invetory", "mission is a null !");
        }
        int[] loots = mission.getReward();
        if(loots == null){
            Log.d("Invetory", "reward is a null !");
        }
        for(int lootId: loots){
            this.addItem(decodeItem(lootId));
        }

    }


    public void updateBase(){       //for loot purposes
        itemBase.add(this.healingPotion);
        itemBase.add(this.staminaPotion);
        itemBase.add(this.healingPotionM);
        itemBase.add(this.staminaPotionM);
        itemBase.add(this.sword0);
        itemBase.add(this.sword1);
        itemBase.add(this.helmet0);
        itemBase.add(this.helmet1);
        itemBase.add(this.shield0);
        itemBase.add(this.shield1);
        itemBase.add(this.key1);
        itemBase.add(this.coins);
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public Potion getHealingPotion() {
        return healingPotion;
    }

    public Potion getStaminaPotion() {
        return staminaPotion;
    }

    public ArrayList getItemList() {
        return this.itemList;
    }

    public ArrayList<Item> getItemBase() {
        return itemBase;
    }

    public Potion getHealingPotionM() {
        return healingPotionM;
    }

    public Potion getStaminaPotionM() {
        return staminaPotionM;
    }

    public Weapon getSword0() {
        return sword0;
    }

    public Weapon getSword1() {
        return sword1;
    }

    public Weapon getHelmet0() {
        return helmet0;
    }

    public Weapon getHelmet1() {
        return helmet1;
    }

    public Weapon getShield0() {
        return shield0;
    }

    public Weapon getShield1() {
        return shield1;
    }

    public QuestItem getKey1() {
        return key1;
    }


    public Coins getCoins() {
        return coins;
    }

}
