package com.example.ross.lopolytd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by Ross on 2/22/2017.
 */

public class GameSurfaceView extends SurfaceView implements Runnable {
    private GameActivity gameActivity;
    boolean isRunning = false;     // true when activity is active and running
    private Thread thread = null;  // the thread that's doing the drawing.

    // note that this is NOT the main UI thread
    // we spawned this thread

    Paint paint = new Paint();
    Tower t;
    GameMap gameMap = new GameMap();
    boolean paused;

    public GameSurfaceView(Context context) {
        super(context);
        //t = new Tower(500, 500);
        gameActivity = (GameActivity) context;
        //bug = new BugSprite(gameActivity);
        gameMap.populateEnemyArray();

        paused = true;

    }

    public void run() {
        while (isRunning) {
            // Get the canvas
            SurfaceHolder surfaceHolder = getHolder();
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }

            // TODO: 4/20/2017 ask about keeping time/change if statement to something else
            // list of times, if time getTimekeeper() is in arraylist, populate enemy array

            if (gameMap.getTimeKeeper() == 50 || gameMap.getTimeKeeper() == 100 || gameMap.getTimeKeeper() == 150 ){
                gameMap.populateEnemyArray();
            }
            Canvas canvas = surfaceHolder.lockCanvas();

            // We're going to be doing a lot more things here.
            drawEverything(canvas);

            if (gameActivity.wasTouched){
                Point p = gameActivity.getTouch();

                this.handleTouch(p, gameMap.getEnemyPath(), canvas);
            }

            // Display our canvas
            surfaceHolder.unlockCanvasAndPost(canvas);

            if (paused != true) {   //if game isn't 'paused', draw enemies
                                    //moving the enemies -- kept in arraylist format for moving one at a time
                for (Enemy e : gameMap.enemyArrayList) {
                    e.moveToNextPoint();
                }
                //increments the timer that keeps track of placing drawing enemies
                gameMap.incrementCheckTimeKeeper();
            }



            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTouch(Point p, ArrayList<Point> enemyPathList, Canvas canvas) {
        if (p.y > DisplayAdvisor.maxY*4/5 && p.x < DisplayAdvisor.maxX*1/4 ) {         //handling pausing based on touch
            if (paused == true) {
                paused = false;
            }
            else if (paused == false) {
                paused = true;
            }
        }else {
            gameMap.addTower(p, enemyPathList);
        }
        //System.out.println(p.x);
    }

    private void drawEverything(Canvas canvas){
        paint.setARGB(255, 255, 0, 0);
        canvas.drawARGB(255, 0, 0, 0);
        gameMap.drawPath(canvas);
        gameMap.drawTowers(canvas);
        gameMap.drawEnemies(canvas);

        //canvas.drawCircle(DisplayAdvisor.maxX, DisplayAdvisor.maxY, 50, paint);
//        worm.draw(canvas);
//        bug.drawBug(canvas);
    }

    public void onResume() {
        // We've become active.  Create a thread
        // and start drawing.
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPause() {
        // We're no longer active.
        isRunning = false;  // This will stop the loop in run and thread will end.
        while (thread != null) {
            try {
                thread.join();  // Wait for thread to end.
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

