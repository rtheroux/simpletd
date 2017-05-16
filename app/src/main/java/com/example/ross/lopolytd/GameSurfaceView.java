package com.example.ross.lopolytd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
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
    GameMap gameMap = new GameMap(this, gameActivity, getContext());
    boolean paused;

    public GameSurfaceView(Context context) {
        super(context);
        gameActivity = (GameActivity) context;
        paused = true;

    }

    public void run() {
        while (isRunning) {
            // Get the canvas
            SurfaceHolder surfaceHolder = getHolder();
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }

            // TODO: 4/23/2017 make better arraylist for storing enemy spawn times
            // list of times, if time getTimekeeper() is in arraylist, populate enemy array

            if (gameMap.getTimeKeeper()%25 == 0){
                gameMap.populateEnemyArray();
            }

            //spawning projectiles every fireRate frames if enemy is within range of tower
            if(!gameMap.towerArrayList.isEmpty()) {
                for (Tower t : gameMap.towerArrayList) {
                    //System.out.println(t.getFirstClosestEnemy(gameMap.enemyArrayList));
                    if ((gameMap.getTimeKeeper()+t.spawnTime) % t.fireRate == 0 && t.getFirstClosestEnemy(gameMap.enemyArrayList)!= null) {
                        gameMap.populateProjectileArray(t, t.getFirstClosestEnemy(gameMap.enemyArrayList));
                    }
                }
            }
            Canvas canvas = surfaceHolder.lockCanvas();

            drawEverything(canvas);

            if (gameActivity.wasTouched){
                Point p = gameActivity.getTouch();

                this.handleTouch(p, gameMap.getEnemyPath(), canvas);
            }

            // Display our canvas
            surfaceHolder.unlockCanvasAndPost(canvas);


            /*
            Where we draw the enemies and projectiles if they're active, otherwise remove them from their respective arraylist
            */

            if (paused != true) {   //if game isn't 'paused', draw enemies
                                    //moving the enemies -- kept in arraylist format for moving one at a time
                for (int i = gameMap.enemyArrayList.size()-1; i >=0; i--){
                    if (gameMap.enemyArrayList.get(i).active == true){
                        gameMap.enemyArrayList.get(i).moveToNextPoint();
                    }else{
                        gameMap.removeEnemy(i);
                    }
                }
                for (int i = gameMap.projectileArrayList.size()-1; i >=0; i--){ //TODO change :(for loop) to iterate backwards through the array and remove then
                    if (gameMap.projectileArrayList.get(i).active == true){
                        gameMap.projectileArrayList.get(i).moveTowardsEnemy(gameMap.enemyArrayList);
                    }else{
                        gameMap.removeProjectile(gameMap.projectileArrayList.get(i));
                    }
                }
                //increments the timer that keeps track of placing drawing enemies
                gameMap.incrementCheckTimeKeeper();
            }



//            try {
//                Thread.sleep(9);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
            if (gameMap.addTower(p, enemyPathList)) {
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.blop);
                mp.start();
            }
        }
    }

    private void drawEverything(Canvas canvas){
        paint.setARGB(255, 255, 0, 0);
        canvas.drawARGB(255, 0, 0, 0);
        gameMap.drawPath(canvas);
        gameMap.drawTowers(canvas);
        gameMap.drawEnemies(canvas);
        gameMap.drawProjectiles(canvas);
        gameMap.drawTowerCount(canvas, getContext(), paused);
    }

    public void showScoreScreen(int enemiesDestroyed){
        isRunning = false;
        gameActivity.showScoreScreen(enemiesDestroyed);
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

