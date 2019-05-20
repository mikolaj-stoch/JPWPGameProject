package com.example.myapplication;

import android.graphics.Bitmap;
import android.util.Log;

public class BitmapSheet {
    private final int WIDTH;        //width of bitmapSheet
    private final int HEIGHT;

    private final int COLUMNS;
    private final int ROWS;

    private final int SPRITE_WIDTH;     //width of one sprite
    private final int SPRITE_HEIGHT;

    private Bitmap[] bitmaps;
    private Bitmap bitmapSheet;

    public BitmapSheet(Bitmap bitmapSheet, int WIDTH, int HEIGHT, int COLUMNS, int ROWS) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.COLUMNS = COLUMNS;
        this.ROWS = ROWS;
        this.SPRITE_WIDTH = WIDTH/COLUMNS;
        this.SPRITE_HEIGHT = HEIGHT/ROWS;

        this.bitmapSheet = bitmapSheet;
        bitmaps = new Bitmap[COLUMNS*ROWS];
        loadBitmaps();
    }

    private void loadBitmaps(){
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLUMNS; col++) {
                Bitmap subImage = Bitmap.createBitmap(bitmapSheet,
                        col * SPRITE_WIDTH, row * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
                bitmaps[col + row*COLUMNS] = subImage;
            }
        }
    }

    public Bitmap getBitmap(int x, int y){
        if(x > WIDTH/SPRITE_WIDTH || y > HEIGHT/SPRITE_HEIGHT){
            Log.d("getBitmap", "x or y out of range");
            return null;
        }
        return bitmaps[x + y*COLUMNS];
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    public int getROWS() {
        return ROWS;
    }

    public int getSPRITE_WIDTH() {
        return SPRITE_WIDTH;
    }

    public int getSPRITE_HEIGHT() {
        return SPRITE_HEIGHT;
    }

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    public Bitmap getBitmapSheet() {
        return bitmapSheet;
    }
}
