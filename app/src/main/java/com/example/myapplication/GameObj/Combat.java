package com.example.myapplication.GameObj;

import android.graphics.Rect;
import android.os.Parcelable;

public interface Combat extends Parcelable {
    boolean isAlive();
    int getHp();
    String getName();
    int getStamina();
    int getMaxHp();
    int getMaxStamina();
    int fastAttack();
    int mediumAttack();
    int heavyAttack();
    int getLevel();
    int getAttack();
    void block();
    void takeDmg(int dmg);
    int getXpToGet();
    String getDmgRange(int attackType);
    String getStaminaCost(int attackType);
    int[] getLoot();
    void die();
    int getBlockReg();
//    Rect getRect();
    void update();
}
