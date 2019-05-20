package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

// class imitate menu bar, that holds options and opens new activities or switch between views
public class MenuBar {
    private int screenW;
    private int screenH;

    private int position;       // currenlty not used
    private ArrayList<CustomButton> options;

    private Context context;
    private Paint paint;
    private Paint paintBorder;
    private Rect rect;

    private int padding;
    private int menuWidth;
    private int menuHeight;
    private int elementWidth;
    private int marginBottom = 50;

    public MenuBar(Context context, int screenW, int screenH){
        this.context = context;
        this.screenH = screenH;
        this.screenW = screenW;

        paint = new Paint();
        Log.i("MenuBar/Constructor", "left,top,right,bottom: " + 0.90*screenW + "," +0 +","+screenW+","+ screenH);
        this.menuWidth = screenW - (int)(0.90*screenW);
        this.menuHeight = screenH;
        rect = new Rect((int)(0.90*screenW), 0, (screenW), screenH);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(221, 195, 0));

        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStrokeWidth(5);

        this.padding = 30;
        options = new ArrayList<>();

        this.elementWidth = menuWidth - 2*padding;
    }

    public void draw(Canvas canvas){

        canvas.drawRect(rect, paint);
        canvas.drawRect(rect, paintBorder);

        for(CustomButton element: options){
            element.draw(canvas);
        }
    }

    public Point getPostion(int elementNumber){
        if(elementNumber > 5){      // 4 should be changed to MAX_OPTIONS
            return null;
        }
        else {
            int x = this.getRect().left + this.padding;
            int y = this.menuHeight - elementNumber*(elementWidth + this.padding*2) - marginBottom;
            return new Point(x, y);
        }
    }

    public void addElement(CustomButton element){
        options.add(element);
    }

    public int getPadding() {
        return padding;
    }

    public int getMenuWidth() {
        return menuWidth;
    }

    public int getMenuHeight() {
        return menuHeight;
    }

    public Rect getRect() {
        return rect;
    }

    public ArrayList<CustomButton> getOptions() {
        return options;
    }

    public int getElementWidth() {
        return elementWidth;
    }
}
