package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.Activities.GameActivity;

// ***********************deprecated***********************************
public class TitleView extends View{
    private int screenH;
    private int screenW;

    private boolean playBtnPressed;

    private Bitmap titleGraphic;
    private Bitmap playButtonUp;
    private Bitmap playButtonDown;

    private Context myContext;

    public TitleView(Context context) {
        super(context);
        myContext = context;

        titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
        playButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_up);
        playButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_down);
    }

    protected void onDraw(Canvas canvas){
        //super.draw(canvas);
        canvas.drawRGB(0, 0, 0);
        canvas.drawBitmap(titleGraphic, (screenW - titleGraphic.getWidth())/2,
                                            (int)(0.1*screenH),null);
       //render buttonDown bitmap when btn is pressed
        if(playBtnPressed){
            canvas.drawBitmap(playButtonDown,(screenW - playButtonUp.getWidth())/2,
                                            (int)(screenH*0.6), null);
        }
        else {
            canvas.drawBitmap(playButtonUp, (screenW - playButtonUp.getWidth())/2,
                                            (int)(screenH*0.6), null);
        }
//        Paint paint = new Paint();
//        paint.setColor(Color.rgb(250, 0, 0));
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(10);
//        canvas.drawCircle(circleX, circleY, 40, paint);
//        if(canvas != null) {
//            canvas.drawRGB(0, 100, 205);
//
//        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int eventAction = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:   //put finger down on the screen
                if(x > (screenW-playButtonUp.getWidth())/2 &&
                        x < (screenW+playButtonUp.getWidth())/2 &&
                        y > screenH*0.6 &&
                        y < screenH*0.6+playButtonUp.getHeight()) {
                    playBtnPressed = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:   //drag finger across the screen
                break;

            case MotionEvent.ACTION_UP:     //remove finger from the screen
                if(playBtnPressed){
                    //intent is a request from app to the Android system to perform an operation e.x. launch a new activity
                    Intent gameIntent = new Intent(myContext, GameActivity.class);
                    myContext.startActivity(gameIntent);
                }
                playBtnPressed = false;
                break;
        }
        invalidate();       //tell to the view that change occurred and onDraw() need to be called soon
        return true;
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh){       //called after View constructor, but before anything is drawn
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
}
