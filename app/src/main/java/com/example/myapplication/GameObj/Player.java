package com.example.myapplication.GameObj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Rect;
import android.os.Parcel;
import android.util.Log;

import com.example.myapplication.Background;
import com.example.myapplication.BitmapSheet;
import com.example.myapplication.GameView;
//import com.example.myapplication.Items.Coins;
import com.example.myapplication.Items.Coins;
import com.example.myapplication.Items.Item;
import com.example.myapplication.Items.Weapon;
import com.example.myapplication.Mission;
import com.example.myapplication.R;

import java.util.Random;
import java.util.TreeMap;

// in order to pass player object we have to implements Parcelable
// ver 0.71, focused on sending values between 2 activities and creating inventory layout
public class Player implements Combat {      //update ver 0.5, 0.71

    private Context context;

    private int x;
    private int y;

    private int xCoordinate;
    private int yCoordinate;

    private boolean moving;
    private static final int ROW_TOP_TO_BOTTOM = 10;
    private static final int ROW_RIGHT_TO_LEFT = 9;
    private static final int ROW_LEFT_TO_RIGHT = 11;
    private static final int ROW_BOTTOM_TO_TOP = 8;

    private static final int DELAY = 1;
    private int count;

    private int colCount = 9;

    private int rowUsing = ROW_LEFT_TO_RIGHT;
    private int colUsing;

    // Velocity of game character (pixel/millisecond)
    private int movingVectorX = 10;
    private int movingVectorY = 5;

    private static final double BASIC_FA_DMG = 0.7;
    private static final double BASIC_MA_DMG = 1;
    private static final double BASIC_HA_DMG = 1.5;
    private static final int BASIC_STAMINA_COST_FA = 3;
    private static final int BASIC_STAMINA_COST_MA = 4;
    private static final int BASIC_STAMINA_COST_HA = 6;
    private static final double STAMINA_FA_RATE = 0.2;
    private static final double STAMINA_MA_RATE = 0.3;
    private static final double STAMINA_HA_RATE = 0.4;

    private int SPEED = 7;
    private int BASIC_HP;
    private int BASIC_ATTACK;
    private int BASIC_STAMINA;

    private String nickname;
    private int level;
    private boolean alive;
    private int xp;

    private int xpToNextLvl;

    private int maxHp;
    private int hp;
    private int attack;
    private int maxStamina;
    private int stamina;

    private int strength;
    private int agility;
    private int vitality;
    private int wisdom;

    private int pointsToSpend;

    private Bitmap bitmap;
    private GameView view;
    private Background background;
    private Paint paint;

    private BitmapSheet playerSheet;
    private Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;

    private int animationSpeed = 3;

    private Random generator;//v0.91
    private Rect rect;

    private Mission mission;

    //equipment
    private Weapon helmet;
    private Weapon chestArmor;
    private Weapon primaryWeapon;
    private Weapon secondaryWeapon;

    private GameView gameView;

    public Player(Context context, GameView gameView, BitmapSheet playerSheet, String nickname){
        this.gameView = gameView;
        this.context = context;
        this.view = gameView;
        paint = new Paint();
        bitmap =  gameView.getResizedBitmap(BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.player), 200, 280);

        this.x = (gameView.getScreenW()-bitmap.getWidth())/2;
        this.y = (int)(gameView.getScreenH()*0.7) - 300;
        this.xCoordinate = gameView.getScreenW() / 2;
        this.yCoordinate = gameView.getScreenH() / 2;
        this.movingVectorX = this.x + bitmap.getWidth()/2;
        this.movingVectorY = this.y + bitmap.getHeight()/2;

        this.alive = true;

        //load bitmap sheet (ver. 0.5)
        this.playerSheet = playerSheet;
        bitmap = playerSheet.getBitmap(0,10);
        this.leftToRights = new Bitmap[colCount];
        this.rightToLefts = new Bitmap[colCount];
        this.topToBottoms = new Bitmap[colCount];
        this.bottomToTops = new Bitmap[colCount];

        for(int col = 0; col < colCount; col++){
            leftToRights[col] = playerSheet.getBitmap(col, ROW_LEFT_TO_RIGHT);
            rightToLefts[col] = playerSheet.getBitmap(col, ROW_RIGHT_TO_LEFT);
            topToBottoms[col] = playerSheet.getBitmap(col, ROW_TOP_TO_BOTTOM);
            bottomToTops[col] = playerSheet.getBitmap(col, ROW_BOTTOM_TO_TOP);
        }
        generator = new Random();
        rect = new Rect(x, y, x + playerSheet.getSPRITE_WIDTH(),  y + playerSheet.getSPRITE_HEIGHT());

        this.nickname = nickname;      //!TODO add to constructor
        initStats();
        calculateStats();
    }

    public void initMission() {
        int numOfStages = 2;
        String description = context.getResources().getString(R.string.mission1);
        TreeMap<Integer, String> stages = new TreeMap<>();
        stages.put(1, "Prepare to fight. Find weapons that will help you.");
        stages.put(2, "Kill all invaders outside the castle");
        stages.put(3, "Go to the Edrick's house and kill Edrick");
        int[] reward = new int[500];

        for(int i = 0; i < 500; i++){
            reward[i] = Coins.COIN_ID;
        }

        String missionName = "Villagers in danger";
        mission = new Mission(this.context, this, this.view.getInventory(), missionName, numOfStages, reward, stages, description);
        mission.setKillMission(2, 3);
        mission.setKillMission(3, 1);
    }

    private void initStats(){
        BASIC_HP = 10;
        BASIC_ATTACK = 2;
        BASIC_STAMINA = 10;
        level = 1;

        xp = 0;
        xpToNextLvl = 100;

        maxHp = BASIC_HP;
        hp = 10;
        attack = BASIC_ATTACK;
        maxStamina = BASIC_STAMINA;
        stamina = 10;

        strength = 0;
        agility = 0;
        vitality = 0;
        wisdom = 0;

        pointsToSpend = 5;

    }

    protected Player(Parcel in) {
        BASIC_HP = in.readInt();
        BASIC_ATTACK = in.readInt();
        BASIC_STAMINA = in.readInt();
        level = in.readInt();
        nickname = in.readString();

        xp = in.readInt();
        xpToNextLvl = in.readInt();

        maxHp = in.readInt();
        hp = in.readInt();
        attack = in.readInt();
        maxStamina = in.readInt();
        stamina = in.readInt();

        strength = in.readInt();
        agility = in.readInt();
        vitality = in.readInt();
        wisdom = in.readInt();

        pointsToSpend = in.readInt();
        alive = in.readByte() != 0;

        generator = new Random();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BASIC_HP);
        dest.writeInt(BASIC_ATTACK);
        dest.writeInt(BASIC_STAMINA);
        dest.writeInt(level);
        dest.writeString(nickname);
        dest.writeInt(xp);
        dest.writeInt(xpToNextLvl);
        dest.writeInt(maxHp);
        dest.writeInt(hp);
        dest.writeInt(attack);
        dest.writeInt(maxStamina);
        dest.writeInt(stamina);
        dest.writeInt(strength);
        dest.writeInt(agility);
        dest.writeInt(vitality);
        dest.writeInt(wisdom);
        dest.writeInt(pointsToSpend);
        dest.writeByte((byte) (alive ? 1 : 0));
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public Bitmap[] getMoveBitmaps()  {
        switch (rowUsing)  {
            case ROW_BOTTOM_TO_TOP:
                return  this.bottomToTops;
            case ROW_LEFT_TO_RIGHT:
                return this.leftToRights;
            case ROW_RIGHT_TO_LEFT:
                return this.rightToLefts;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        //Log.d("USING:", "colUsing, rowUsing: " + colUsing + ", " + rowUsing );
        return bitmaps[this.colUsing];
    }

    //@Override
    public void update()  {
        //It breaks interface, looking for another way to repair it.
        //rect.set(this.x, this.y, x + playerSheet.getSPRITE_WIDTH(), y + playerSheet.getSPRITE_HEIGHT());
       // Log.d("update", "newX: " + newX + "  newY: " + newY + " x: " + x + " y: " + y);
        if(view.getMyBackground().getOldAnimation()) {
            int distX = (movingVectorX - x - bitmap.getWidth() / 2);
            int distY = (movingVectorY - y - bitmap.getHeight() / 2);
            // --------works---------------
            if (distX - SPEED > 0) {
                this.x += SPEED;
                //Log.d("Move","Moving right ---> " + Integer.toString(this.x));
            }
            if (distX + SPEED < 0) {          //when character 'stops' -> every update move left and right
                this.x -= SPEED;
                //Log.d("Move","Moving left <--- " + Integer.toString(this.x));
            }
            if (distY - SPEED > 0) {
                this.y += SPEED;
                //Log.d("Move","Moving down " + Integer.toString(this.y));
            }
            if (distY + SPEED < 0) {
                this.y -= SPEED;
                //Log.d("Move","Moving up " + Integer.toString(this.y));
            }


            if (distX - SPEED > 0 ||
                    distX + SPEED < 0 ||
                    distY + SPEED < 0 ||
                    distY - SPEED > 0) {
                moving = true;
                //Log.d("MOVING: ",Boolean.toString(moving));
            } else {
                moving = false;
            }

            // character is moving
            if (moving) {
                if (count++ > DELAY) {
                    this.colUsing++;
                    count = 0;
                }

                if (colUsing >= this.colCount) {
                    this.colUsing = 0;
                }
                // choose bitmap

                if (distX > SPEED) {       //move right
                    if (Math.abs(distY) > Math.abs(distX) && distY > 0) {
                        rowUsing = ROW_TOP_TO_BOTTOM;
                    } else if (Math.abs(distY) > Math.abs(distX) && distY < 0) {
                        rowUsing = ROW_BOTTOM_TO_TOP;
                    } else {
                        rowUsing = ROW_LEFT_TO_RIGHT;
                    }
                } else {
                    if (Math.abs(distY) > Math.abs(distX) && distY > 0) {
                        rowUsing = ROW_TOP_TO_BOTTOM;
                    } else if (Math.abs(distY) > Math.abs(distX) && distY < 0) {
                        rowUsing = ROW_BOTTOM_TO_TOP;
                    } else {
                        rowUsing = ROW_RIGHT_TO_LEFT;
                    }
                }

            } else {
                colUsing = 0;
                rowUsing = 10;
            }


            //Log.d("Moving", Boolean.toString(moving) + "distX, distY, usingRow: " + distX + " " + distY + " | " + rowUsing);
        }
    }

    public void animationUpdate (int distX, int distY, boolean moving) {
        if (moving){

            count = count + 10;

            if (count > 90 / animationSpeed){
                this.colUsing++;
                count = 0;
                if (colUsing == colCount)
                    this.colUsing = 0;
            }


            if (distX > SPEED) {       //move right
                if (Math.abs(distY) > Math.abs(distX) && distY > 0) {
                    rowUsing = ROW_TOP_TO_BOTTOM;
                } else if (Math.abs(distY) > Math.abs(distX) && distY < 0) {
                    rowUsing = ROW_BOTTOM_TO_TOP;
                } else {
                    rowUsing = ROW_LEFT_TO_RIGHT;
                }
            } else {
                if (Math.abs(distY) > Math.abs(distX) && distY > 0) {
                    rowUsing = ROW_TOP_TO_BOTTOM;
                } else if (Math.abs(distY) > Math.abs(distX) && distY < 0) {
                    rowUsing = ROW_BOTTOM_TO_TOP;
                } else {
                    rowUsing = ROW_RIGHT_TO_LEFT;
                }
            }

        } else {
            colUsing = 0;
            rowUsing = 10;
        }
    }

    //@Override
    public void draw(Canvas canvas) {
        Bitmap currentBitmap = getCurrentMoveBitmap();
        canvas.drawBitmap(currentBitmap, this.x, this.y, this.paint);
    }

    public void setPosition(int newX, int newY){
        this.movingVectorX = newX;
        this.movingVectorY = newY;
        //Log.d("setPosition", "newX: " + newX + "  newY: " + newY);
    }

    public void increaseStrength(int value, boolean ignorePoints){
        if(!ignorePoints) {
            if (pointsToSpend - value > -1) {
                this.strength += value;
                this.pointsToSpend -= value;
                calculateStats();
            }
        }
        else {
            this.strength += value;
            calculateStats();
        }
    }

    private void decreaseStrength(int value){
        if(value > 0){
            this.strength -= value;
            calculateStats();
        }
    }

    public void increaseAgility(int value, boolean ignorePoints){
        if (!ignorePoints){
            if(pointsToSpend - value > -1) {
                this.agility += value;
                this.pointsToSpend -= value;
                this.stamina += 1;
                calculateStats();
            }
        }
        else {
            this.agility += value;
            this.stamina += value;
            calculateStats();
        }
    }

    private void decreaseAgility(int value){
        if(value > 0){
            this.agility -= value;
            calculateStats();
        }
    }

    public void increaseVitality(int value, boolean ignorePoints){
        if(!ignorePoints) {
            if (pointsToSpend - value > -1) {
                this.vitality += value;
                this.pointsToSpend -= value;
                this.hp += value;
                calculateStats();
            }
        }
        else {
            this.vitality += value;
            this.hp += value;
            calculateStats();
        }
    }

    private void decreaseVitality(int value){
        if(value > 0){
            this.vitality -= value;
            calculateStats();
        }
    }

    public void increaseWisdom(int value, boolean ignorePoints){
        if(pointsToSpend  - value > -1){
            this.wisdom += value;
            this.pointsToSpend -= value;
            calculateStats();
        }
        else {

        }
    }

    public int fastAttack(){
        int staminaCost = Player.BASIC_STAMINA_COST_FA + (int)(this.strength*Player.STAMINA_FA_RATE);
        if(staminaCost <= this.stamina) {
            this.stamina -= staminaCost;
            int dmg = generator.nextInt(attack) + 1  + (int)(Player.BASIC_FA_DMG*this.strength);
            return dmg;
        }
        else {
            return -1;
        }
    }

    public int mediumAttack(){
        int staminaCost = Player.BASIC_STAMINA_COST_MA + (int)(this.strength*STAMINA_MA_RATE);
        if(staminaCost <= this.stamina) {
            this.stamina -= staminaCost;
            int dmg = generator.nextInt(attack) + 1 + (int)(Player.BASIC_MA_DMG*this.strength);
            return dmg;
        }
        else {
            return -1;
        }
    }

    public int heavyAttack(){
        int staminaCost = Player.BASIC_STAMINA_COST_HA + (int)(this.strength*STAMINA_HA_RATE);
        if(staminaCost <= this.stamina) {
            this.stamina -= staminaCost;
            int dmg = generator.nextInt(attack) + 1 + (int)(Player.BASIC_HA_DMG*this.strength);
            return dmg;
        }
        else {
            return -1;
        }
    }

    public void block(){
        int staminaReg = 2 + (int)(agility*0.7);
        if(this.stamina + staminaReg < this.maxStamina){
            this.stamina += staminaReg;
        }
        else {
            this.stamina = this.maxStamina;
        }
    }

    @Override
    public int getBlockReg(){
        return 2 + (int)(agility*0.7);
    }

    public Rect getRect() {
        return null;
    }

    public void takeDmg(int dmg){
        if(dmg >= this.hp){
            hp = 0;
            this.alive = false;
        }
        else {
            this.hp -= dmg;
        }
    }

    @Override
    public String getDmgRange(int attackType){      // 1 - fa, 2 - ma, 3 -ha
        if (attackType < 1 || attackType > 3){
            return "";
        }
        else {
            String min;
            String max;
            switch (attackType){
                case 1:
                    min = String.valueOf(1 + (int)(Player.BASIC_FA_DMG*this.strength));
                    max = String.valueOf(Integer.parseInt(min) + attack);
                    return min + "-" + max;
                case 2:
                    min = String.valueOf(1 + (int)(Player.BASIC_MA_DMG*this.strength));
                    max = String.valueOf(Integer.parseInt(min) + attack);
                    return min + "-" + max;
                case 3:
                    min = String.valueOf(1 + (int)(Player.BASIC_HA_DMG*this.strength));
                    max = String.valueOf(Integer.parseInt(min) + attack);
                    return min + "-" + max;
                default:
                    return "";
            }
        }
    }

    @Override
    public String getStaminaCost(int attackType){      // 1 - fa, 2 - ma, 3 -ha
        if (attackType < 1 || attackType > 3){
            return "";
        }
        else {
            int staminaCost;
            switch (attackType){
                case 1:
                    staminaCost = Player.BASIC_STAMINA_COST_FA + (int)(this.strength*Player.STAMINA_FA_RATE);
                    return String.valueOf(staminaCost);
                case 2:
                    staminaCost = Player.BASIC_STAMINA_COST_MA+ (int)(this.strength*Player.STAMINA_MA_RATE);
                    return String.valueOf(staminaCost);
                case 3:
                    staminaCost = Player.BASIC_STAMINA_COST_HA + (int)(this.strength*Player.STAMINA_HA_RATE);
                    return String.valueOf(staminaCost);
                default:
                    return "";
            }
        }
    }

    public void calculateStats(){
        this.maxHp = getBASIC_HP() + this.vitality;
        if(this.hp > this.maxHp){
            hp = maxHp;
        }
        this.maxStamina = getBASIC_STAMINA() + this.agility;
        if(stamina > maxStamina){
            stamina = maxStamina;
        }
    }

    public boolean checkLvlUp(){
        if(this.xp >= this.xpToNextLvl){
            this.level += 1;
            this.xp -= this.xpToNextLvl;
            this.xpToNextLvl += 20;
            this.pointsToSpend += 1;
            return true;
        }
        else {
            return false;
        }
    }

    public void restoreHp(int value){
        if(this.hp + value <= this.maxHp){
            this.hp += value;
        }
        else {
            this.hp = this.maxHp;
        }
    }

    public void restoreStamina(int value){
        if(this.stamina + value <= this.maxStamina){
            this.stamina += value;
        }
        else {
            this.stamina = this.maxStamina;
        }
    }

    public void stop(){         // !TODO have to be changed
        if(background == null){
            Log.e("PLAYER/STOP", "Background is a null");
        }
        else {
            this.background.setMovingVectorX(this.x);
            this.background.setMovingVectorY(this.y);
        }
    }

    public void equip(Item item){       // !!
        if (item instanceof Weapon){
            switch (((Weapon) item).getWeaponType()){
                case HELMET:
                    helmet = (Weapon) item;
                    break;
                case CHEST:
                    chestArmor = (Weapon) item;
                    break;
                case PRIMARY_WEAPON:
                    primaryWeapon = (Weapon) item;
                    break;
                case SECONDARY_WEAPON:
                    secondaryWeapon = (Weapon) item;
                    break;
            }
            increaseStrength(((Weapon) item).getStrengthBonus(), true);
            increaseAgility(((Weapon) item).getAgilityBonus(), true);
            increaseVitality(((Weapon) item).getVitalityBonus(), true);
        }
        Log.d("Player/equip", "str: " + strength + ", agi: " + agility + ", vit: " + vitality);
    }

    public void unequip(Weapon.WeaponType weaponType){  // !!
        switch (weaponType){
            case HELMET:         //helmet
                if (helmet != null){
                    decreaseStrength((helmet).getStrengthBonus());
                    decreaseAgility((helmet).getAgilityBonus());
                    decreaseVitality(helmet.getVitalityBonus());
                    this.helmet = null;
                }
                else {
                    Log.i("Player", "unequip/ helmet null");
                }
                break;

            case CHEST:         //chest
                if (chestArmor != null){
                    decreaseStrength((chestArmor).getStrengthBonus());
                    decreaseAgility((chestArmor).getAgilityBonus());
                    decreaseVitality(chestArmor.getVitalityBonus());
                    this.chestArmor = null;
                }
                else {
                    Log.i("Player", "unequip/ chest armor null");
                }
                break;

            case PRIMARY_WEAPON:         //primaryWeapon
                if (primaryWeapon != null){
                    decreaseStrength((primaryWeapon).getStrengthBonus());
                    decreaseAgility((primaryWeapon).getAgilityBonus());
                    decreaseVitality(primaryWeapon.getVitalityBonus());
                    this.primaryWeapon = null;
                }
                else {
                    Log.i("Player", "unequip/ primary_weapon null");
                }
                break;
            case SECONDARY_WEAPON:         //secondaryWeapon
                if (secondaryWeapon != null){
                    decreaseStrength(((Weapon) secondaryWeapon).getStrengthBonus());
                    decreaseAgility(((Weapon) secondaryWeapon).getAgilityBonus());
                    this.secondaryWeapon = null;
                }
                else {
                    Log.i("Player", "unequip/ secondary_weapon null");
                }
                break;
        }
        Log.d("Player/unequip", "str: " + strength + ", agi: " + agility + ", vit: " + vitality);
    }

    @Override
    public int[] getLoot() {
        return new int[0];
    }

    @Override
    public void die() {
        this.alive = false;
    }


    @Override
    public int getXpToGet() {
        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void addXp(int xp){
        this.xp += xp;
    }

    public void setBitmap(Bitmap playerBitmap){
        this.bitmap = playerBitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMoving() { return moving; }

    public int getSPEED() {
        return SPEED;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public String getName() {
        return this.nickname;
    }

    public int getAttack() {
        return attack;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Paint getPaint(){
        return paint;
    }


    public int getBASIC_HP() {
        return BASIC_HP;
    }

    public int getBASIC_ATTACK() {
        return BASIC_ATTACK;
    }

    public int getBASIC_STAMINA() {
        return BASIC_STAMINA;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getXpToNextLvl() {
        return xpToNextLvl;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public int getStamina() {
        return stamina;
    }

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }

    public int getVitality() {
        return vitality;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getPointsToSpend() {
        return pointsToSpend;
    }

    public GameView getView() {
        return view;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public void setPointsToSpend(int pointsToSpend) {
        this.pointsToSpend = pointsToSpend;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRowUsing(int rowUsing) { this.rowUsing = rowUsing; }

    public void setCount(int count) {
        this.count = count;
    }

    public void setColUsing(int colUsing) { this.colUsing = colUsing; }

    public int getColCount() { return colCount; }

    public int getColUsing() { return colUsing; }

    public static int getDELAY() { return DELAY; }

    public void updateCount (int count){ this.count += count;}

    public int getCount() { return count;}

    public void updateColUsing (int ColUsing) {this.colUsing += ColUsing;}

    public void setBackground (Background background){
        this.background = background;
    }

    public Weapon getHelmet() {
        return helmet;
    }

    public Weapon getChestArmor() {
        return chestArmor;
    }

    public Weapon getPrimaryWeapon() {
        return primaryWeapon;
    }

    public Weapon getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public void updateCoordinates (int x, int y){
        this.xCoordinate += x;
        this.yCoordinate += y;
    }

    public Mission getMission() {
        return mission;
    }

    public void centerPlayer(){
        this.x = (gameView.getScreenW()-bitmap.getWidth())/2;
        this.y = (int)(gameView.getScreenH()*0.7) - 300;
        this.xCoordinate = gameView.getScreenW() / 2;
        this.yCoordinate = gameView.getScreenH() / 2;
        this.movingVectorX = this.x + bitmap.getWidth()/2;
        this.movingVectorY = this.y + bitmap.getHeight()/2;
    }
}
