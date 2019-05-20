package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.myapplication.GameObj.GameObject;
import com.example.myapplication.GameObj.Player;


//All class added in 0.1

public class Background {

    private Bitmap image;
    private int xDrawing;
    private int yDrawing;

    private int x;
    private int y;

    private int centerX;
    private int centerY;

    private int dx; //Only horizontal
    private int dy;

    private int movingVectorX;
    private int movingVectorY;

    private int SPEED = 7;

    private Boolean moving = false;

    private Player player;

    private GameView view;

    private Boolean oldAnimation= false;

    public Background(Bitmap res, GameView view, Player player){

        image = res;
        this.view = view;
        this.centerX = view.getScreenW() /2;
        this.centerY = view.getScreenH() /2;
        this.x = centerX;
        this.y = centerY;
        this.movingVectorX = centerX;
        this.movingVectorY = centerY;
        this.player = player;
    }


    public void leftScroll (){//int xNew, int xDest){
        xDrawing = xDrawing - dx;
        if (xDrawing < -GameView.WIDTH){
            xDrawing = 0; //Reset
        }
    }

    public void rightScroll() {
        xDrawing = xDrawing + dx;
        if (xDrawing > GameView.WIDTH) {
            xDrawing = 0; //Reset
        }
    }

    public void downScroll(){
        yDrawing = yDrawing + dy;
        if (yDrawing > GameView.HEIGHT){
            yDrawing = 0;
        }
    }

    public void upScroll(){
        yDrawing = yDrawing - dy;
        if (yDrawing < -GameView.HEIGHT){
            yDrawing = 0;
        }
    }

    public void update(){

        if (!oldAnimation) {
            int distX = this.movingVectorX - x;
            int distY = this.movingVectorY - y;

            if (distX - SPEED > 0) {
                leftScroll();
                this.x += SPEED;
            }
            if (distX + SPEED < 0) {
                rightScroll();
                this.x -= SPEED;
            }
            if (distY - SPEED > 0) {
                upScroll();
                this.y += SPEED;
            }
            if (distY + SPEED < 0) {
                downScroll();
                this.y -= SPEED;
            }

            if (Math.abs(x - movingVectorX) < 10 && Math.abs(y - movingVectorY) < 10) {
                this.movingVectorX = centerX;
                this.movingVectorY = centerY;
                this.x = centerX;
                this.y = centerY;
                moving = false;
                view.setX(view.getScreenW() / 2);
                view.setY(view.getScreenH() / 2);
            } else
                moving = true;

            player.animationUpdate(distX, distY, moving);
        }

    }


    public void setCenter (int x, int y){
        this.movingVectorX = x;
        this.movingVectorY = y;
    }

    public void updateCoordinates (int x,int y) {
        this.movingVectorX += x;
        this.movingVectorY += y;
    }

    public void stopMoving (){
        this.movingVectorX = centerX;
        this.movingVectorY = centerY;
        this.x = centerX;
        this.y = centerY;
        moving = false;
        view.setX(view.getScreenW() / 2);
        view.setY(view.getScreenH() / 2);
    }




    public void draw(Canvas canvas){
        canvas.drawBitmap(image,xDrawing,yDrawing,null);

        if (xDrawing > 0 && yDrawing > 0 ) {
            canvas.drawBitmap(image, xDrawing - GameView.WIDTH, yDrawing - GameView.HEIGHT, null);
        }
        else if (xDrawing < 0 && yDrawing > 0) {
            canvas.drawBitmap(image,xDrawing + GameView.WIDTH,yDrawing - GameView.HEIGHT,null);
        }
        else if ( xDrawing < 0 && yDrawing < 0){
            canvas.drawBitmap(image,xDrawing + GameView.WIDTH,yDrawing + GameView.HEIGHT,null);
        }
        else if ( xDrawing > 0 && yDrawing < 0) {
            canvas.drawBitmap(image, xDrawing - GameView.WIDTH, yDrawing + GameView.HEIGHT, null);
        }

        if ( xDrawing > 0)   //This for right scrolling
            canvas.drawBitmap(image,xDrawing - GameView.WIDTH,yDrawing,null);
        else if ( xDrawing < 0)
            canvas.drawBitmap(image,xDrawing + GameView.WIDTH,yDrawing,null);

        if ( yDrawing > 0)
            canvas.drawBitmap(image,xDrawing,yDrawing - GameView.HEIGHT,null );
        else if ( yDrawing < 0)
            canvas.drawBitmap(image,xDrawing,yDrawing + GameView.HEIGHT,null );
    }

    public void setVector(int dx, int dy){

        this.dx = dx;
        this.dy = dy;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setMovingVectorX(int movingVectorX) {
        this.movingVectorX = movingVectorX;
    }

    public void setMovingVectorY(int movingVectorY) {
        this.movingVectorY = movingVectorY;
    }

    public Boolean getMoving() { return moving; }

    public int getCenterX() { return centerX; }

    public int getCenterY() { return centerY; }

    public Boolean getOldAnimation() {
        return oldAnimation;
    }

    public void setOldAnimation(Boolean oldAnimation) {
        this.oldAnimation = oldAnimation;
    }
}
