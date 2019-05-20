package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.myapplication.Dialogs.PauseMenu;
import com.example.myapplication.GameObj.Player;

public class CustomButton {
    private int x;
    private int y;

    private int width;
    private int height;

    private Bitmap bitmap;
    private Context context;
    private Clickable actionBtn;

    public CustomButton(int x, int y, Bitmap bitmap, Context context, Clickable actionBtn) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.context = context;
        this.actionBtn = actionBtn;

        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void checkClick(int x, int y, GameView view){ //Don't like this way :/  -->
        if(x > this.x && x < this.x + width && y > this.y && y < this.y + height){
            actionBtn.actionOnClick();
//            view.getMap().centerPlayer(view);
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(this.bitmap, this.x, this.y, new Paint());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
