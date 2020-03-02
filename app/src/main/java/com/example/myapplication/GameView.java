package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Activities.ActivityCreator;
import com.example.myapplication.Activities.GameActivity;
import com.example.myapplication.Dialogs.Border;
import com.example.myapplication.Dialogs.DoorsMenu;
import com.example.myapplication.Dialogs.EnterMenu;
import com.example.myapplication.Dialogs.ExceptionWhileBackgroundMoving;
import com.example.myapplication.Dialogs.InventoryDialog;
import com.example.myapplication.Dialogs.ObjectMenu;
import com.example.myapplication.Dialogs.PauseMenu;
import com.example.myapplication.Dialogs.PlayerWeaponsDialog;
import com.example.myapplication.GameObj.Chest;
import com.example.myapplication.GameObj.Combat;
import com.example.myapplication.GameObj.GameObject;
import com.example.myapplication.GameObj.Mob1;
import com.example.myapplication.GameObj.Player;
import com.example.myapplication.Items.Inventory;
import com.example.myapplication.Items.Item;
import com.example.myapplication.Items.Potion;
import com.example.myapplication.Items.QuestItem;
import com.example.myapplication.Items.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TreeMap;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480; //Propably will be diffrent, now for test purpose. Added 0.1

    public static int screenW;
    public static int screenH;

    private Context context;
    private SurfaceHolder surfaceHolder;

    private GameThread thread;
    private Background background; //Added in 1.1
    private MenuBar menuBar;
    private CustomButton itemPack;
    private CustomButton menuButton;
    private CustomButton playerStats;
    private CustomButton weaponEqBtn;

    //game objects
    private Player player;
    private ArrayList<Mob1> enemies;
    private Chest chest;

    private Inventory inventory;

    private ArrayList<GameObject> trees; 
    private ArrayList<GameObject> stones;
    private ArrayList<GameObject> clickableItems;   

    private int numberOfTrees = 7;
    private int numberOfStones = 3;

    private BitmapSheet playerSheet;
    private BitmapSheet itemsSheet;

    private int x;
    private int y;

    private int xNew;
    private int yNew;

    private Boolean collisionProblem = false;

    private GameObject border;
    private Border border1;
    private int END = 3000;

    private GameObject castle;
    private GameObject house;
    private GameObject door;

    private int world;


    private int treesToCastle = 10;
    private Mob1 enemy3;

    ActivityCreator ac; //set ready to create Combat Activity

    public GameView(Context context){
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);      //Callback intercepts events
        thread = new GameThread(surfaceHolder, this);
        setFocusable(true);

        //get screen size
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenW = size.x;
        this.screenH = size.y;

        x = size.x / 2;
        y = size.y / 2;

        Log.d("WAZNE",screenW + " " + screenH);

        initGameObjects();
    }

    private void initGameObjects() {

        Bitmap playerSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_sheet);
        this.playerSheet = new BitmapSheet(playerSheet, playerSheet.getWidth(),
                playerSheet.getHeight(), 13, 21);

        this.player = new Player(this.context, this, this.playerSheet, "Joel the Ugly");

        //sprite sheet with all the items
        Bitmap imgSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_48x48);
        this.itemsSheet = new BitmapSheet(imgSheet, imgSheet.getWidth(), imgSheet.getHeight(), 20,15);

        this.background = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.grassbackground),this,player); //Added 0.1
        this.background.setVector(4,4); //Test. Sets how long image moves when touched.

        this.player.setBackground(background);
        //map = new Map(this.context,this);

        // init player
        this.inventory = new Inventory(this.context);
        this.player.initMission();
        startingPack();

        Random random = new Random();

        //Tree example
        trees = new ArrayList<>();
        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                250,300,"Tree " + 1,this,true));
        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                200,300,"Tree " + 2,this,true));
        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                250,450,"Tree " + 3,this,true));
        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                250,450,"Tree " + 4,this,true));

        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                650,360,"Tree " + 7,this,true));
        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                650,430,"Tree " + 7,this,true));

        house = new GameObject((BitmapFactory.decodeResource(getResources(),R.drawable.house_a)),-200,100,"House",this,true);

        stones = new ArrayList<>();
        stones.add(new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.stone),
                1100,70,"Stone " + 1, this,true));
        stones.add(new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.stone),
                1400,70,"Stone " + 2, this,true));
        stones.add(new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.stone),
                1800,800,"Stone " + 3, this,true));
        stones.add(new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.stone),
                2200,800,"Stone " + 4, this,true));
        stones.add(new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.stone),
                2000,700,"Stone " + 4, this,true));
        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                2200,630,"Tree " + 8,this,true));

        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                837,1220,"Tree " + 9,this,true));
        trees.add(new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.tree),
                550,1210,"Tree " + 10,this,true));

        //init elements
        initQuestItems();
        initEnemy();        //ver 1.5
        initMenuBar();

        border = new GameObject(0,0,"border",this,true);
        border1 = new Border(context);


        castle = new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.house2),775,-1000,"Edrick's house",this,true);
        for(int i = 0; i < treesToCastle/2; i++){
            trees.add(new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.tree),
                    610, -700 + i*100,"Tree " + i,this,true));
        }
        for(int i = 0; i < treesToCastle/2; i++){
            trees.add(new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.tree),
                    1700, -700 + i*100,"Tree " + i,this,true));
        }

        door = new GameObject(BitmapFactory.decodeResource(getResources(),R.drawable.castledoors),1300,400,"Doors",this,false);
    }

    private void initQuestItems() {
        clickableItems = new ArrayList<>();
        LinkedHashMap<Item, Integer> chestItems = new LinkedHashMap<>();
        chestItems.put(inventory.getStaminaPotion(), 2);
        chestItems.put(inventory.getShield0(), 1);
        chestItems.put(inventory.getSword0(), 1);
        chestItems.put(inventory.getHelmet0(), 1);
        chestItems.put(inventory.getHealingPotionM(), 1);
        Bitmap chestImg = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.chest0), 100, 100);
        chest = new Chest(chestImg,  250, 500, "mission chest", this,
                chestItems, null,false, 0);

        clickableItems.add(chest);
    }

    private void initMenuBar() { // change 1.5
        menuBar = new MenuBar(this.context, this.screenW, this.screenH);

        //init player stat btn
        int elementSize = menuBar.getElementWidth();
        Point position = menuBar.getPostion(2);
        Bitmap backpack = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player_stats), elementSize, elementSize);
        playerStats = new CustomButton(position.x, position.y, backpack, context, new ActivityCreator(this.context, this.player, 0));
        Log.i("initGameObj/POSITION", "x,y " + position.x + ", " + position.y);
        menuBar.addElement(playerStats);

        //init pause menu
        Bitmap menuIcon = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.menu_icon), elementSize, elementSize);
        Point position2 = menuBar.getPostion(1);
        menuButton = new CustomButton(position2.x, position2.y, menuIcon, context, new PauseMenu(this.context));
        menuBar.addElement(menuButton);

        Point position3 = menuBar.getPostion(3);
        Bitmap invBtn = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.knapsack),elementSize,elementSize);
        itemPack = new CustomButton(position3.x,position3.y,invBtn,context, new InventoryDialog(this.context, this.inventory, this.player));
        menuBar.addElement(itemPack);


        Point position4 = menuBar.getPostion(4);
        Bitmap weaponEq = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.battle_gear), elementSize, elementSize);
        weaponEqBtn = new CustomButton(position4.x, position4.y, weaponEq, context, new PlayerWeaponsDialog(context, player, inventory));
        menuBar.addElement(weaponEqBtn);
        ac = new ActivityCreator(this.context, this.player, 1);
    }

    public void initEnemy(){        //probably it would be better to create new class
        enemies = new ArrayList<>();
        //create enemies

        // mob1
        Mob1 enemy1;
        enemy1 = new Mob1(context, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.enemy_lvl1), this, 1200, 70, 1,"Invader",
                10, 3, 10, 10, 60,true);
        int[] loot = new int[10];        //array that holds indices of loot-items
        for(int i = 0; i < loot.length; i++){
            if( i % 2 == 0){
                loot[i] = 1;
            }
            else {
                loot[i] = 0;
            }
        }
        enemy1.setLoot(loot);

        // mob2
        Mob1 enemy2 = new Mob1(context, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.enemy_lvl1), this, 2000, 800,1, "Invader",
                10, 3, 10, 10, 50,true);
        int[] loot2 = new int[6];        //array that holds indices of loot-items
        for(int i = 0; i < loot2.length/2; i++){
            loot2[i] = Potion.HEALING_ID;
        }
        for(int i = 3; i < loot2.length; i++){
            loot2[i] = Potion.STAMINA_ID;
        }

        enemy2.setLoot(loot2);

        enemy3 = new Mob1(context, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.enemy_lvl3), this, 200, 500, 3, "Edrick",
                20, 5, 20, 18, 120,false);
        int[] loot3 = new int[6];        //array that holds indices of loot-items
        for(int i = 0; i < loot3.length/2; i++){
            loot3[i] = Potion.HEALING_M_ID;
        }
        for(int i = 3; i < loot3.length; i++){
            loot3[i] = Potion.STAMINA_M_ID;
        }

        enemy3.setLoot(loot3);

        Mob1 enemy4 = new Mob1(context, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.enemy_lvl2), this, 700, 1200, 2,"Invader",
                25, 2, 25, 20, 85,true);
        int[] loot4 = new int[4];        //array that holds indices of loot-items
        loot4[0] = Weapon.SWORD_1_ID;
        loot4[1] = Weapon.SHIELD_1_ID;
        loot4[3] = Weapon.HELMET_1_ID;
        loot4[2] = Potion.STAMINA_M_ID;
        enemy4.setLoot(loot4);


        enemies.add(enemy1);
        enemies.add(enemy2);
        //enemies.add(enemy3);
        enemies.add(enemy4);
    }

    @Override
    public void draw(Canvas canvas){        //draw called manually, onDraw automatically
        super.draw(canvas);
        final float scaleFactorX = getWidth() / (float) WIDTH; //Scalling factor. Added in 0.1
        final float scaleFactorY = getHeight()/ (float) HEIGHT;
        if(canvas != null){
            final int savedState = canvas.save(); //Added in 0.1

            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas); //Added 1.1
            canvas.restoreToCount(savedState); //Keep scalling every time without this line.

            for(GameObject chest: clickableItems){
                chest.draw(canvas);
            }

            //itemPack.draw(canvas);
            for(Mob1 enemy: enemies){
                if(enemy.isAlive()) {
                    enemy.draw(canvas);
                }
            }

            for(GameObject tree: trees){
                tree.draw(canvas);
            }
            for (GameObject stone: stones){
                stone.draw(canvas);
            }

            menuBar.draw(canvas);
            player.draw(canvas);
            castle.draw(canvas);
            house.draw(canvas);
            if (enemy3.isAlive())
                enemy3.draw(canvas);
            door.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        synchronized (surfaceHolder) {
            int eventAction = event.getAction();
            xNew = (int) event.getX();
            yNew = (int) event.getY();
            Log.d("Position touch", "(x, y): " + xNew + ", " + yNew);

            switch (eventAction) {

                case MotionEvent.ACTION_DOWN: {
                    for (CustomButton element : menuBar.getOptions()) {
                        element.checkClick(xNew, yNew, this);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (xNew < menuBar.getRect().left) {
                        if (!background.getOldAnimation()) {
                            if (border.getX() + x - xNew > END || border.getX() + x - xNew < -END || border.getY() + y - yNew > END || border.getY() + y - yNew < -END) {
                                border1.show();
                                break;
                            }
                            stopMovingForBackgroundAndObjects();
                            background.setCenter(xNew, yNew);

                            Boolean objectOrNo = false;

                            if (checkIfClickedForListGameObject(trees, xNew, yNew) ||
                                    checkIfClickedForListGameObject(stones, xNew, yNew) ||
                                    checkIfClickedForListMob(enemies, xNew, yNew) ||
                                    checkIfClickedForEnterObject(castle, xNew, yNew) ||
                                    checkIfClickedForEnterObject(house,xNew,yNew))
                                objectOrNo = true;

                            if (!objectOrNo) {
                                background.setCenter(xNew, yNew);
                                updateCoordinatesBothMobsAndGamesObjects(xNew, yNew);
                            }
                        } else {
                            if (world == 1) {
                                if (checkIfClickedGameObject(enemy3, xNew, yNew)) {
                                    showCombatDialog(enemy3);
                                }
                                else if (checkIfClickedGameObject(door,xNew,yNew)){
                                    DoorsMenu doorsMenu = new DoorsMenu(context,"Doors",this);
                                    doorsMenu.show();
                                }
                                else
                                    player.setPosition(xNew,yNew);
                            }
                            if (world == 2)
                                if (checkIfClickedGameObject(chest, xNew, yNew)) {
                                    if(chest.getID() == 0 && !chest.isOpened()){
                                        player.getMission().nextStage();
                                    }
                                    chest.open();
                                }
                                else if (checkIfClickedGameObject(door,xNew,yNew)){
                                    DoorsMenu doorsMenu = new DoorsMenu(context,"Doors",this);
                                    doorsMenu.show();
                                }
                            else
                                player.setPosition(xNew,yNew);
                        }
                    }
               }
                     
            }
        return true;
        }
        //invalidate();

    private void showCombatDialog(final Combat opponent) {  // final - check !!!
        final Dialog ad = new Dialog(context);
        ad.setContentView(R.layout.combat_alert);
        //set enemy statistics
        ((TextView)ad.findViewById(R.id.enemyName)).setText(opponent.getName());
        ((TextView)ad.findViewById(R.id.enemyLvl)).setText(String.valueOf(opponent.getLevel()));
        ((TextView)ad.findViewById(R.id.enemyAttack)).setText(String.valueOf(opponent.getAttack()));
        ((TextView)ad.findViewById(R.id.enemyHp)).setText(String.valueOf(opponent.getMaxHp()));
        ((TextView)ad.findViewById(R.id.enemyStamina)).setText(String.valueOf(opponent.getMaxStamina()));

        Button fightBtn = (Button)ad.findViewById(R.id.fightBtn);
        fightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.setEnemy(opponent);
                ac.createCombat(context, player, opponent);
                ad.dismiss();
            }
        });

        Button cancelBtn = (Button)ad.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ad.show();
    }

    // ---- SOME REDUCING LINES OF TEXT AND HELPFUL FUNCTIONS ----

    public boolean checkIfClickedGameObject (GameObject gameObject, int x, int y){
        if (gameObject.getRect().contains(x,y)){
            background.stopMoving();
            return true;
        }
        return false;
    }

    public boolean checkIfClickedGameObject (Mob1 gameObject, int x, int y){
        if (gameObject.getRect().contains(x,y)){
            background.stopMoving();
            return true;
        }
        return false;
    }

    public boolean checkIfClickedForEnterObject (GameObject gameObject, int x, int y) {
        if(checkIfClickedGameObject(gameObject,x,y)){
            EnterMenu enterMenu = new EnterMenu(context, gameObject.getName(),this);
            enterMenu.show();
            return true;
            }
        return false;
    }


    public void updateCoordinatesForListGameObject(int x, int xNew, int y, int yNew, ArrayList<GameObject> gameObjects){
        for(GameObject gameObject: gameObjects)
            gameObject.updateCoordinates(x - xNew, y - yNew);
     }

    public void updateCoordinatesForListMob(int x, int xNew, int y, int yNew, ArrayList<Mob1> gameObjects){
        for(GameObject gameObject: gameObjects)
            gameObject.updateCoordinates(x - xNew, y - yNew);
    }

     public boolean checkIfClickedForListGameObject (ArrayList<GameObject> gameObjects, int x, int y) {
        for (GameObject gameObject : gameObjects)
            if(checkIfClickedGameObject(gameObject,x,y)){
                ObjectMenu objectMenu = new ObjectMenu(context, gameObject.getName());
                objectMenu.show();
                return true;
            }
        return false;
    }

    public boolean checkIfClickedForOtherItems (ArrayList<GameObject> gameObjects, int x, int y) {
        for (GameObject gameObject : gameObjects)
            if(checkIfClickedGameObject(gameObject, x, y)){
                if(gameObject instanceof Chest){
                    chest.open();
                }
                return true;
            }
        return false;
    }

    public boolean checkIfClickedForListMob (ArrayList<Mob1> gameObjects, int x, int y) {
        for (Mob1 gameObject : gameObjects)
            if(gameObject.isAlive()) {          // add ver 1.48
                if (checkIfClickedGameObject(gameObject, x, y)) {
                    showCombatDialog(gameObject);
                    return true;
                }
            }
        return false;
    }

    public void updateListOfObjects (ArrayList<GameObject> gameObjects){
        for (GameObject gameObject : gameObjects)
            gameObject.update();
    }

    public void updateListOfMobs (ArrayList<Mob1> gameObjects){
        for (GameObject gameObject : gameObjects)
            gameObject.update();
    }

    public void stopMovingForObjects (ArrayList<GameObject> gameObjects){
        for (GameObject gameObject : gameObjects)
            gameObject.stopMoving();
    }

    public void stopMovingForMobs (ArrayList<Mob1> gameObjects){
        for (GameObject gameObject : gameObjects)
            gameObject.stopMoving();
    }

    public void stopMovingForBackgroundAndObjects (){

        background.stopMoving();
        stopMovingForObjects(stones);
        stopMovingForObjects(trees);
        stopMovingForMobs(enemies);
        stopMovingForObjects(clickableItems);       //ver 1.48
        border.stopMoving();
        castle.stopMoving();
        house.stopMoving();
    }

    public void updateCoordinatesBothMobsAndGamesObjects(int xNew, int yNew){
        updateCoordinatesForListGameObject(x, xNew, y, yNew, trees);
        updateCoordinatesForListGameObject(x, xNew, y, yNew, stones);
        updateCoordinatesForListGameObject(x, xNew, y, yNew, clickableItems);   //!TODO ver 1.48
        updateCoordinatesForListMob(x,xNew,y,yNew,enemies);
        border.updateCoordinates(x- xNew, y - yNew);
        castle.updateCoordinates(x - xNew, y - yNew);
        house.updateCoordinates(x - xNew, y - yNew);
    }

    public void stopDrawObject(GameObject gameObject){
        gameObject.setDrawOrNo(false);
    }

    public void startDrawObject(GameObject gameObject){
        gameObject.setDrawOrNo(true);
    }

    public void stopDrawForObjectsInMainWorld (){
        for(GameObject tree : trees){
            stopDrawObject(tree);
        }
        for(GameObject stone : stones){
            stopDrawObject(stone);
        }
        for(Mob1 enemy : enemies){
            stopDrawObject(enemy);
        }
        stopDrawObject(castle);
        stopDrawObject(house);
    }

    public void startDrawForObjectsInMainWorld () {
        for (GameObject tree : trees) {
            startDrawObject(tree);
        }
        for (GameObject stone : stones) {
            startDrawObject(stone);
        }
        for (Mob1 enemy : enemies) {
            startDrawObject(enemy);
        }
        startDrawObject(castle);
        startDrawObject(house);
    }

    // --- END ---

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        if(thread.getState() == Thread.State.NEW){          //check why !!!!!!
            thread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }
            catch (InterruptedException e ){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    //update coordinates
    public void update() {
        player.update();
        updateListOfObjects(trees);
        updateListOfObjects(stones);
        updateListOfMobs(enemies);
        updateListOfObjects(clickableItems);
        background.update();
        border.update();
        castle.update();
        border.update();
        house.update();
        chest.update();
        door.update();
        if (border.getX() > END || border.getX() < -END || border.getY() > END || border.getY() < -END){
            stopMovingForBackgroundAndObjects();
            if (border.getX() > END)
                border.setMovingVectorX(END - 5);
            if (border.getX() < -END)
                border.setMovingVectorX(-END + 5);
            if (border.getY() > END)
                border.setMovingVectorY(END - 5);
            if (border.getY() < -END)
                border.setMovingVectorY(- END + 5);
        }
    }


    public void pause() {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }
            catch (InterruptedException e ){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void resume(){
        thread = new GameThread(surfaceHolder, this);
        thread.setRunning(true);
        thread.start();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void startingPack() {
        for(int i = 0; i < 10; i++){
            inventory.addItem(inventory.getHealingPotion());
        }
        for(int i = 0; i < 10; i++){
            inventory.addItem(inventory.getStaminaPotion());
        }

        inventory.addItem(inventory.getHealingPotionM());
        inventory.addItem(inventory.getStaminaPotionM());   //enemies drops weapons
        inventory.addItem(inventory.getKey1());
        inventory.addItem(inventory.getKey1());
    }

    public void enterCastle (){
        stopDrawForObjectsInMainWorld();
        background.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.background2));
        background.setOldAnimation(true);
        world = 1;
        enemy3.setDrawOrNo(true);
        door.setDrawOrNo(true);
        //END = 1200;
    }

    public void enterHouse (){
        stopDrawForObjectsInMainWorld();
        background.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.background));
        background.setOldAnimation(true);
        world = 2;
        chest.setDrawOrNo(true);
        door.setDrawOrNo(true);
    }

    public void exitCastle(){
        startDrawForObjectsInMainWorld();
        background.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.grassbackground));
        background.setOldAnimation(false);
        world = 0;
        enemy3.setDrawOrNo(false);
        door.setDrawOrNo(false);
        player.centerPlayer();

    }
    public void exitHouse (){
        startDrawForObjectsInMainWorld();
        background.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.grassbackground));
        background.setOldAnimation(false);
        world = 0;
        chest.setDrawOrNo(false);
        door.setDrawOrNo(false);
        player.centerPlayer();
    }

    public int getScreenW() {
        return screenW;
    }

    public int getScreenH() {
        return screenH;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public GameThread getThread() {
        return thread;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<GameObject> getTrees() {
        return trees;
    }

    public ArrayList<GameObject> getStones() {
        return stones;
    }

    public MenuBar getMenuBar() { return menuBar; }

    public Inventory getInventory(){
        return inventory;   //ver 1.5
    }

    public ArrayList getEnemies(){
        return enemies;
    }

    public ActivityCreator getAc(){
        return ac;
    }

    public Background getMyBackground() {
        return this.background;
    }

    public BitmapSheet getItemsSheet() {
        return itemsSheet;
    }

    public int getxNew() {
        return xNew;
    }

    public void setxNew(int xNew) {
        this.xNew = xNew;
    }

    public int getyNew() {
        return yNew;
    }

    public void setyNew(int yNew) {
        this.yNew = yNew;
    }

    public Boolean getCollisionProblem() {
        return collisionProblem;
    }

    public void setCollisionProblem(Boolean collisionProblem) {
        this.collisionProblem = collisionProblem;
    }

    public GameObject getBorder() {
        return border;
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }
}
