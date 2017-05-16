package com.example.ross.lopolytd;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Ross on 4/1/2017.
 */

public class Tower {
    int posX, posY;
    int radius;
    Paint paint;
    int fireRate; //how fast the tower will spawn a projectile
    int spawnTime;
    Enemy current_enemy;
    ArrayList<Enemy> enemyArrayList;
    int enemyCount;

    int range;

    public Tower(int x, int y, int spawnTime, ArrayList<Enemy> enemyArrayList){
        posX = x;
        posY = y;
        radius = DisplayAdvisor.maxX/21;
        paint = new Paint();
        paint.setARGB(255,255,255,255);
        this.spawnTime = spawnTime;
        this.enemyArrayList = enemyArrayList;
        range = 500;
        fireRate = 35;
        enemyCount = 0;
    }

    void draw(Canvas canvas){
        canvas.drawCircle(posX, posY, radius, paint);
    }

    Enemy getFirstClosestEnemy(ArrayList<Enemy> enemyArrayList){
        enemyCount = 0;
        if(!enemyArrayList.isEmpty()){
            for(int i = enemyArrayList.size()-1; i >=0; i--){
                if(Math.sqrt((Math.pow((enemyArrayList.get(i).posX - posX), 2)) + (Math.pow((enemyArrayList.get(i).posY - posY), 2))) < range){
                    current_enemy = enemyArrayList.get(i);
                    enemyCount++;
                }
            }
        }
        if(enemyCount>0) {
            return current_enemy;
        }else{
            return null;
        }
    }

    int getRadius(){
        return radius;
    }

}
