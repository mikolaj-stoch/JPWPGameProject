package com.example.myapplication.GameObj;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

import com.example.myapplication.Clickable;
import com.example.myapplication.GameView;

/*
interface GameObject {
    //Every object (By object I mean - tree, some monsters, stones XD every single object in fact.. ) should have xMap and yMap to draw on map.
    void update(int x, int y);
    void draw(Canvas canvas);
}
*/

public class GameObject implements Clickable {

    private Bitmap image;

    protected int x;
    protected int y;

    private int movingVectorX;
    private int movingVectorY;

    private int SPEED = 7;

    protected String name;

    private int WIDTH;
    private int HEIGHT;

    private Rect rect;

    protected GameView gameView;

    private Boolean collision = false;

    private Boolean drawOrNo;

    public GameObject(Bitmap res, int x,int y, String name,GameView gameView,Boolean drawOrNo) {

        this.image = res;

        this.x = x;
        this.movingVectorX = x;

        this.y = y;
        this.movingVectorY = y;

        this.name = name;

        this.WIDTH = res.getWidth();
        this.HEIGHT = res.getHeight();

        this.gameView = gameView;

        this.drawOrNo = drawOrNo;

    }

    public GameObject(int x,int y, String name,GameView gameView,Boolean drawOrNo) {

        this.x = x;
        this.movingVectorX = x;

        this.y = y;
        this.movingVectorY = y;

        this.name = name;
        this.gameView = gameView;

        this.drawOrNo = drawOrNo;

    }

    public GameObject(){        //for testing

    }

    public void draw(Canvas canvas) {
        if (drawOrNo) {
            if (x < gameView.getMenuBar().getRect().left - (WIDTH * 2) / 3) {
                canvas.drawBitmap(image, x, y, null);
                 }
        }
    }

    public void update () {

        int distX = this.movingVectorX - x;
        int distY = this.movingVectorY - y;

        rect = new Rect(getX(), getY(), getX() + WIDTH, getY() + HEIGHT);
        if (drawOrNo) {
            updatePosition();
        }
        /*
        if(collision){
            int tempX = this.x;
            int tempY = this.y;

            if (distX - SPEED > 0) {
                tempX += 20*SPEED;
            }
            if (distX + SPEED < 0) {
                tempY -= 20*SPEED;
            }
            if (distY - SPEED > 0) {
                tempX += 20*SPEED;
            }
            if (distY + SPEED < 0) {
                tempY -= 20*SPEED;
            }

            Rect rectTemp = new Rect(tempX - 35,tempY - 35,tempX + WIDTH + 35, tempY + HEIGHT + 35);

            if (rectTemp.contains(gameView.getMyBackground().getCenterX() ,gameView.getMyBackground().getCenterY())) {
                gameView.stopMovingForBackgroundAndObjects();
                Log.d("GAMEOBJECT", "Im holding you!");
            }
            else {
                collision = false;
                gameView.setCollisionProblem(false);
                updatePosition();
            }
        }
        else
            updatePosition();

    */
    }

    public void updatePosition(){
        int distX = this.movingVectorX - x;
        int distY = this.movingVectorY - y;

        if (distX - SPEED > 0) {
            this.x += SPEED;
        }
        if (distX + SPEED < 0) {
            this.x -= SPEED;
        }
        if (distY - SPEED > 0) {
            this.y += SPEED;
        }
        if (distY + SPEED < 0) {
            this.y -= SPEED;
        }

        rect = new Rect(getX(), getY(), getX() + WIDTH, getY() + HEIGHT);
        Rect rectCollision = new Rect(getX() - 35, getY() - 35, getX() + WIDTH + 35, getY() + HEIGHT + 35);

        /*
        if (rectCollision.contains(gameView.getMyBackground().getCenterX() ,gameView.getMyBackground().getCenterY())) {
            collision = true;
            //Log.d("GAMEOBJECT", "Collision Detected");
            gameView.stopMovingForBackgroundAndObjects();
            gameView.setCollisionProblem(true);
            //Log.d("GAMEOBJECT", "" + rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom);
        }
        */

    }

    public void stopMoving () {
        this.movingVectorX = this.x;
        this.movingVectorY = this.y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public void setPosition (int x, int y){
        this.movingVectorX = x;
        this.movingVectorY = y;
    }

    public void updateCoordinates (int x,int y) {
        this.movingVectorX += x;
        this.movingVectorY += y;
    }


    public int getMovingVectorX() {
        return movingVectorX;
    }

    public int getMovingVectorY() {
        return movingVectorY;
    }

    @Override
    public void actionOnClick() {}

    public Bitmap getImage() {
        return image;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public String getName() {
        return name;
    }

    public Rect getRect() { return rect; }

    public Boolean getCollision() {
        return collision;
    }

    public void setCollision(Boolean collision) {
        this.collision = collision;
    }

    public void setMovingVectorX(int movingVectorX) {
        this.movingVectorX = movingVectorX;
        this.x = movingVectorX;
    }

    public void setMovingVectorY(int movingVectorY) {
        this.movingVectorY = movingVectorY;
        this.y = movingVectorY;
    }

    public Boolean getDrawOrNo() {
        return drawOrNo;
    }

    public void setDrawOrNo(Boolean drawOrNo) {
        this.drawOrNo = drawOrNo;
    }

    //public boolean onTouch
}