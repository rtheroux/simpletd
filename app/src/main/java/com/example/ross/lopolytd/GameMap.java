package com.example.ross.lopolytd;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.ViewDebug;

import java.util.ArrayList;

/**
 * Created by Ross on 4/1/2017.
 */

public class GameMap {

    ArrayList<Point> enemyPathArray;
    ArrayList<Tower> towerArrayList;
    ArrayList<Enemy> enemyArrayList;
    Point p;
    Paint paint;
    private int timeKeeper; //used to insert enemies one at a time into enemy arraylist


    public GameMap() {
        towerArrayList = new ArrayList<>();
        enemyArrayList = new ArrayList<>();
        paint = new Paint();
        paint.setARGB(255, 255, 255, 255);
        enemyPathArray = new ArrayList<>();
        enemyPathArray.add(new Point(DisplayAdvisor.maxX / 5, 0));

        for (int i = 1; i < 6; i+=2){
            enemyPathArray.add(new Point(4 * DisplayAdvisor.maxX / 5, i * DisplayAdvisor.maxY / 6)); //creating enemy path (temporary)
            enemyPathArray.add(new Point(DisplayAdvisor.maxX / 5, (i+1) * DisplayAdvisor.maxY / 6));
        }

    }

    public void drawPath(Canvas canvas){
        for(int i = 0; i < enemyPathArray.size()-1; i++){
            canvas.drawLine(enemyPathArray.get(i).x, enemyPathArray.get(i).y, enemyPathArray.get(i+1).x, enemyPathArray.get(i+1).y, paint);
        }
    }

    //get slope for each segment of the path
    //plug x and y of tower into

    public void addTower(Point p, ArrayList<Point> enemyPathList){
        Tower t = new Tower(p.x, p.y);
        double slope;
        double yInt;
        double x,y;
        boolean cannotAdd = false;

        for (int i = 0; i < enemyPathList.size(); i++) {
            if (Math.sqrt(Math.pow(p.x - enemyPathList.get(i).x, 2) + Math.pow(p.y - enemyPathList.get(i).y, 2)) < t.getRadius()*2) {
                cannotAdd = true;
            }
        }
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

            //System.out.println(" x " + enemyPathList.get(2).x + " y " + enemyPathList.get(2).y);
            //System.out.println("line x = " +  + " p.y = " + p.y + " slope = " + slope);
            //System.out.println("x = " + x + " y = " + y + " yInt = " + yInt + " slope " + slope);


            if (Math.sqrt(Math.pow(Math.abs(x) - Math.abs(p.x), 2) + Math.pow(Math.abs(y) - Math.abs(p.y), 2)) < t.getRadius() *1.5) {
                if (p.x > Math.min(enemyPathList.get(i).x, enemyPathList.get(i+1).x) && p.x < Math.max(enemyPathList.get(i).x, enemyPathList.get(i+1).x)){
                    //the slope only accounts for just that; the slope, this will check
                    //if on the slope ad within the bounds of the two points, i and i+1

                    //TODO: do for y of points compared to slope too, done already for x in inside if statement
                    cannotAdd = true;
                }
            }
        }

        for(Tower t1:towerArrayList){
            if (Math.sqrt(Math.pow(Math.abs(t.posX) - Math.abs(t1.posX), 2) + Math.pow(Math.abs(t.posY) - Math.abs(t1.posY), 2)) < t.getRadius() *1.85){
                cannotAdd = true;
            }
        }

        if(!cannotAdd) {
            towerArrayList.add(new Tower(p.x, p.y));
        }
    }

    public void drawTowers(Canvas canvas) {
        if(!towerArrayList.isEmpty()){
            for (Tower t:towerArrayList){
                t.draw(canvas);
            }
        }
    }

    public void populateEnemyArray(){
        Enemy e = new Enemy(enemyPathArray);
        enemyArrayList.add(e);

        System.out.println("Enemy added!");
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
}
