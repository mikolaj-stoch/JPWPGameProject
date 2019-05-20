package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

class GameThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private Context context;
    private GameView gameView;
    private Canvas canvas;


    boolean running = false;
    // desired fps
    private final static int    MAX_FPS = 40;
    // maximum number of frames to be skipped
    private final static int    MAX_FRAME_SKIPS = 5;
    // the frame period
    private final static int    FRAME_PERIOD = 1000 / MAX_FPS;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        //this.context = context;
        this.gameView = gameView;
    }


    @Override
    public void run() {
        Canvas canvas;
        Log.d("GAMETHREAD", "Starting game loop");

        int sleepTime;      // ms to sleep (<0 if we're behind)
        int framesSkipped;  // number of frames being skipped

        long lastTime = System.nanoTime();
        int ups = 40;
        double nanoSecondConversion = 1000000000/ups;       //1 second [ns] / fps
        double changeInSeconds = 0;

        sleepTime = 0;

        while (running) {
            canvas = null;

            // try locking the canvas for exclusive pixel editing
            // in the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                //Log.d("Thread", "LOCK");

                synchronized (surfaceHolder) {
                    // game loop that holds 30 fps
                    long now = System.nanoTime();

                    changeInSeconds += (now - lastTime) / nanoSecondConversion;
                    while (changeInSeconds >= 1) {
                        this.gameView.update();
                        changeInSeconds--;
                    }

                    if (canvas != null) {
                        this.gameView.draw(canvas);
                    }
                    lastTime = now;
                }
            }
            catch (Exception e){
                Log.d("Thread", "Excpetion");
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
            finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);      //2 times canvas is locked, when switch between activities !
                    //Log.d("Thread", "UNLOCK");
                }
                }
            }
        }

    public void setRunning(boolean b){
        this.running = b;
    }

    public boolean isRunning(){
        return running;
    }
}
