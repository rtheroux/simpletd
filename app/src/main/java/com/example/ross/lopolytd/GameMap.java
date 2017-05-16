package com.example.ross.lopolytd;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewDebug;

import java.util.ArrayList;

import static com.example.ross.lopolytd.R.string.tower_count;

/**
 * Created by Ross on 4/1/2017.
 */

public class GameMap{

    ArrayList<Point> enemyPathArray;
    ArrayList<Tower> towerArrayList;
    ArrayList<Enemy> enemyArrayList;
    ArrayList<Projectile> projectileArrayList;
    Paint paint;
    Paint textPaint;
    private int timeKeeper; //used to insert enemies one at a time into enemy arraylist
    int towerCount; //the number of towers the player is allowed to place
    Typeface textFont;
    AssetManager assetManager;
    int playerHealth;
    int enemiesDestroyed;
    GameSurfaceView gameSurfaceView;
    GameActivity gameActivity;

    Bitmap play, pause;



    public GameMap(GameSurfaceView gameSurfaceView, GameActivity gameActivity, Context context) {

        this.gameSurfaceView = gameSurfaceView;
        this.gameActivity = gameActivity;


        towerArrayList = new ArrayList<>();
        enemyArrayList = new ArrayList<>();
        projectileArrayList = new ArrayList<>();
        paint = new Paint();
        paint.setARGB(255, 255, 255, 255);
        enemyPathArray = new ArrayList<>();
        enemyPathArray.add(new Point(DisplayAdvisor.maxX / 5, 0));
        timeKeeper = 1; //set to 1 to support towers being placed before timekeeper increments (for firerate can't divide by zero)
        towerCount = 5;
        playerHealth = 25;
        enemiesDestroyed = 0;

        textPaint = new Paint();
        textPaint.setARGB(255, 0, 153, 204);
        textPaint.setTextSize(64);

        play = BitmapFactory.decodeResource(context.getResources(), R.drawable.play1);
        play = Bitmap.createScaledBitmap(play, DisplayAdvisor.maxX/5, DisplayAdvisor.maxX/5, false);

        pause = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
        pause = Bitmap.createScaledBitmap(pause, DisplayAdvisor.maxX/5, DisplayAdvisor.maxX/5, false);




        /*TODO: get new font to work for tower count
        assetManager = new AssetManager();
        textFont = Typeface.createFromAsset(assetManager, );
        */
        for (int i = 1; i < 6; i+=2) {
            enemyPathArray.add(new Point(4 * DisplayAdvisor.maxX / 5, i * DisplayAdvisor.maxY / 6)); //creating enemy path (temporary)
            enemyPathArray.add(new Point(DisplayAdvisor.maxX / 5, (i + 1) * DisplayAdvisor.maxY / 6));
        }

    }

    public void drawPath(Canvas canvas){
        for(int i = 0; i < enemyPathArray.size()-1; i++){
            canvas.drawLine(enemyPathArray.get(i).x, enemyPathArray.get(i).y, enemyPathArray.get(i+1).x, enemyPathArray.get(i+1).y, paint);
        }
    }

    //get slope for each segment of the path
    //plug x and y of tower into

    public boolean addTower(Point p, ArrayList<Point> enemyPathList){
        Tower t = new Tower(p.x, p.y, getTimeKeeper(), enemyArrayList);
        double slope;
        double yInt;
        double x,y;
        boolean cannotAdd = false;

        for (int i = 0; i<enemyPathList.size()-1; i++) {                //this for loop generates formula to check if tower is placed on path
            if (enemyPathList.get(i).x < enemyPathList.get(i+1).x) {    //if the path goes from left to right
                slope = ((double) enemyPathList.get(i).y - (double) enemyPathList.get(i + 1).y) / ((double) enemyPathList.get(i).x - (double) enemyPathList.get(i+1).x);
                yInt = (double) enemyPathList.get(i + 1).y - ((double) enemyPathList.get(i + 1).x * slope);
            }else{                                                      //if the path goes from right to left
                slope = ((double) enemyPathList.get(i+1).y - (double) enemyPathList.get(i).y) / ((double) enemyPathList.get(i+1).x - (double) enemyPathList.get(i).x);
                yInt = (double) enemyPathList.get(i).y - ((double) enemyPathList.get(i).x * slope);
            }

            y = p.x * slope + yInt;
            x = (y - yInt) / (-slope);


            if (Math.sqrt(Math.pow(Math.abs(x) - Math.abs(p.x), 2) + Math.pow(Math.abs(y) - Math.abs(p.y), 2)) < t.getRadius() *1.5) {
                if (p.x > Math.min(enemyPathList.get(i).x, enemyPathList.get(i+1).x) && p.x < Math.max(enemyPathList.get(i).x, enemyPathList.get(i+1).x)){
                    //the slope only accounts for just that; the slope, this will check
                    //if on the slope ad within the bounds of the two points, i and i+1
                    //TODO: do for y of points compared to slope too, done already for x in inside if statement
                    cannotAdd = true;
                }
            }


            // because the last if statement only accounts for the tower (center point) being on
            // the line somewhere, this next few lines determines if the tower is within a distance of the path points
            if (Math.sqrt(Math.pow(p.x - enemyPathList.get(i).x, 2) + Math.pow(p.y - enemyPathList.get(i).y, 2)) < t.getRadius()*2) {
                cannotAdd = true;
            }
        }

        for(Tower t1:towerArrayList){
            if (Math.sqrt(Math.pow(Math.abs(t.posX) - Math.abs(t1.posX), 2) + Math.pow(Math.abs(t.posY) - Math.abs(t1.posY), 2)) < t.getRadius() *1.85){
                cannotAdd = true;
            }
        }

        if(towerCount <= 0){
            cannotAdd = true;
        }

        if(!cannotAdd) {
            towerArrayList.add(new Tower(p.x, p.y, getTimeKeeper(), enemyArrayList));
            towerCount --;
            return true;
        }
        return false;
    }

    public void drawTowers(Canvas canvas) {
        if(!towerArrayList.isEmpty()){
            for (Tower t:towerArrayList){
                t.draw(canvas);
            }
        }
    }

    public void populateEnemyArray(){
        Enemy e = new Enemy(enemyPathArray, this);
        enemyArrayList.add(e);

    }
    public void populateProjectileArray(Tower t, Enemy e){
        Projectile p = new Projectile(t, e);
        projectileArrayList.add(p);    //add projectile for each tower if timekeeper is some value for that tower
    }

    public void drawEnemies(Canvas canvas){
        if(!enemyArrayList.isEmpty()){
            for (Enemy e:enemyArrayList){
                e.drawEnemy(canvas);
            }
        }
    }

    public ArrayList<Point> getEnemyPath() {
        return enemyPathArray;
    }

    public void incrementCheckTimeKeeper(){
        timeKeeper++;
    }

    public int getTimeKeeper(){
        //System.out.print(timeKeeper);
        return timeKeeper;
    }

    public synchronized void drawProjectiles(Canvas canvas) {
        if(!projectileArrayList.isEmpty()){
            for (Projectile p:projectileArrayList){
                if (p.active) {
                    p.drawProjectile(canvas);
                }
            }

        }

    }

    public void removeProjectile(Projectile p){
        if(p.active == false){
            projectileArrayList.remove(p);
        }
    }

    public void drawTowerCount(Canvas canvas, Context context, boolean paused) {
        String count = context.getResources().getString(R.string.tower_count) + Integer.toString(towerCount);
        canvas.drawText(count, DisplayAdvisor.maxX*3/5, DisplayAdvisor.maxY/30, textPaint);

        // gets called every frame
        if (paused) {
            canvas.drawBitmap(play, DisplayAdvisor.maxX / 15, DisplayAdvisor.maxY * 4 / 5, null);
        }else{
            canvas.drawBitmap(pause, DisplayAdvisor.maxX / 15, DisplayAdvisor.maxY * 4 / 5, null);
        }
    }

    public void removeHealth(int health){
        playerHealth -= health;
        if(playerHealth <= 0){
            gameSurfaceView.showScoreScreen(enemiesDestroyed);
        }
    }

    public void removeEnemy(int i) {
        if(enemyArrayList.get(i).health <= 0){
            enemiesDestroyed++;
        }
        enemyArrayList.remove(i);
    }
}
