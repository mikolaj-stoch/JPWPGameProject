package com.example.myapplication.GameObj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.util.Log;

import com.example.myapplication.GameView;


// just a template so far (ver 0.6)
public class Mob1 extends GameObject implements Combat{//implements GameObject, Combat {

    private Context context;
    private GameView gameView;
    private Paint paint;
//    private Paint paint2;       //test

    private int HEIGHT;
    private int WIDTH;

    private int level;
    private boolean alive;

    //statistics
    private int maxHp;
    private int hp;
    private int attack;
    private int maxStamina;
    private int stamina;

    private int xpToGet;
    private int[] lootId;

    private Rect rect;

    public Boolean drawOrNo;

    public Mob1(Context context, Bitmap bitmap, int x, int y, String name, GameView gameView,Boolean drawOrNo) {
        super(bitmap, x, y, name, gameView,drawOrNo);
        this.context = context;
        this.gameView = gameView;

        HEIGHT = bitmap.getHeight();
        WIDTH = bitmap.getWidth();

        this.alive = true;
        paint = new Paint();
        rect = new Rect(x, y, x+WIDTH, y+HEIGHT);
//        paint2 = new Paint();     //for collision detection
//        paint2.setStyle(Paint.Style.STROKE);
//        paint2.setColor(Color.YELLOW);
//        paint2.setStrokeWidth(3);
        Log.d("Mob1", "initialized, (x, y): (" + x + ", " + y  + ")");
    }

    public Mob1(Context context, Bitmap bitmap, GameView gameView, int xPosition, int yPosition, int level,
                String name, int max_hp, int attack, int hp, int stamina, int xpToGet,Boolean drawOrNo) {
        this(context, bitmap, xPosition, yPosition, name, gameView,drawOrNo);
        this.maxHp = max_hp;
        this.attack = attack;
        this.hp = hp;
        this.stamina = stamina;
        this.xpToGet = xpToGet;
        this.maxStamina = stamina;
        this.level = level;
    }

    protected Mob1(Parcel in) {
        level = in.readInt();
        maxHp = in.readInt();
        hp = in.readInt();
        attack = in.readInt();
        maxStamina = in.readInt();
        stamina = in.readInt();
        alive = in.readByte() != 0;
        xpToGet = in.readInt();
        lootId = in.createIntArray();
    }


    @Override
    public void update() {
        super.update();
        rect = new Rect(getX(), getY(), getX()+WIDTH, getY()+HEIGHT);
    }

    //@Override
    /*
    public void draw(Canvas canvas) {
        canvas.drawBitmap(getImage(), getX(), getY(), paint);
//        canvas.drawRect(rect, paint2);        //check for collision detection
    }
    */

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void die(){
        alive = false;
    }

    @Override
    public int getBlockReg() {
        return 0;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    @Override
    public int fastAttack() {
        int faCost = 2;
        if(stamina - faCost < 0){
            return -1;
        }
        else {
            stamina -= 2;
            return attack-2;
        }
    }

    @Override
    public int mediumAttack() {
        if(stamina - 3 < 0){
            return -1;
        }
        else {
            stamina -= 3;
            return attack;
        }
    }

    @Override
    public int heavyAttack() {
        if(stamina - 5 < 0){
            return -1;
        }
        else {
            stamina -= 3;
            return attack+2;
        }
    }

    @Override
    public void block() {
        if(this.stamina + 3 > this.maxStamina){
            this.stamina = this.maxStamina;
        }
        else {
            stamina += 3;
        }

    }

    @Override
    public void takeDmg(int dmg){
        if(dmg >= this.hp){
            hp = 0;
            this.alive = false;
        }
        else {
            this.hp -= dmg;
        }
    }

    public void setLoot(int[] lootId){
        this.lootId = lootId;
    }

    public int[] getLoot(){
        if(!alive) {
            return lootId;
        }
        else {
            Log.e("Mob1", "getLoot/ enemy is still alive!");
            return null;
        }
    }

    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeInt(maxHp);
        dest.writeInt(hp);
        dest.writeInt(attack);
        dest.writeInt(maxStamina);
        dest.writeInt(stamina);
        dest.writeByte((byte) (alive ? 1 : 0));
        dest.writeInt(xpToGet);
        dest.writeIntArray(lootId);

    }

    public static final Creator<Mob1> CREATOR = new Creator<Mob1>() {
        @Override
        public Mob1 createFromParcel(Parcel in) {
            return new Mob1(in);
        }

        @Override
        public Mob1[] newArray(int size) {
            return new Mob1[size];
        }
    };

    @Override
    public int getXpToGet(){
        return this.xpToGet;
    }

    @Override
    public String getDmgRange(int attackType) {
        return null;
    }

    @Override
    public String getStaminaCost(int attackType) {
        return null;
    }

    public Rect getRect() {
        return rect;
    }
}

