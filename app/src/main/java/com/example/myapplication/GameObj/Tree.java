package com.example.myapplication.GameObj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.myapplication.BitmapSheet;
import com.example.myapplication.GameView;

public class Tree {

    private Bitmap image;

    private int xMap = 300;   //Should be constant!!
    private int yMap = 300;

    private int x = 300;
    private int y = 300;

    public Tree(Bitmap res) {
        this.image = res;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image,x,y,null);

    }

    public void update(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return x;
    }

    public int getY() { return y; }

    public int getxMap() {
        return xMap;
    }

    public int getyMap() {
        return yMap;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
