package com.example.myapplication.Items;

import com.example.myapplication.GameObj.Player;

public class Weapon extends Item {

    private int attack;
    private int strengthBonus;
    private int agilityBonus;
    private int vitalityBonus;

    public static final int SWORD_0_ID = 10;
    public static final int SWORD_1_ID = 11;

    public static final int HELMET_0_ID = 20;
    public static final int HELMET_1_ID = 21;

    public static final int SHIELD_0_ID = 40;
    public static final int SHIELD_1_ID = 41;

    private WeaponType weaponType;
    public enum WeaponType {
        HELMET, CHEST, PRIMARY_WEAPON, SECONDARY_WEAPON;
    }

    public Weapon(String name, int id, boolean usable, int resourceID, int attack,
                  int strengthBonus, int agilityBonus, int vitalityBonus, WeaponType weaponType) {
        super(name, id, usable, resourceID);
        this.attack = attack;
        this.strengthBonus = strengthBonus;
        this.agilityBonus = agilityBonus;
        this.vitalityBonus = vitalityBonus;

        this.weaponType = weaponType;
    }

    @Override
    public String toString() {
        return getName() + "\n\nstrength bonus: " + this.strengthBonus + "\nagility bonus: " +
                this.agilityBonus + "\nvitality bonus: " + this.vitalityBonus;
    }

    @Override
    public void use(Player player, Inventory inventory) {

    }

    public int getAttack() {
        return attack;
    }

    public int getStrengthBonus() {
        return strengthBonus;
    }

    public int getAgilityBonus() {
        return agilityBonus;
    }

    public WeaponType getWeaponType(){
        return weaponType;
    }

    public int getVitalityBonus() {
        return vitalityBonus;
    }
}
