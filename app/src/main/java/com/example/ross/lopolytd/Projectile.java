package com.example.ross.lopolytd;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Ross on 4/23/2017.
 */

public class Projectile {
    int posX,posY;
    int radius;
    Enemy targetEnemy;       //when created, a projectile will spawn with a target to move towards
    Paint paint;
    double total_distanceX, total_distanceY;
    double travel_distanceX, travel_distanceY;
    boolean active;
    int speed;
    int damage;
    double closestEnemyDistance;

    Enemy closestEnemy;

    public Projectile(Tower t,Enemy targetEnemy){
        //this.targetEnemy = targetEnemy;
        radius = 14;
        posX = t.posX;
        posY = t.posY;
        speed = 15;
        paint = new Paint();
        paint.setARGB(255,255,255,255);
        active = true;
        damage = 5;



        this.targetEnemy = targetEnemy;

    }

    synchronized void moveTowardsEnemy(ArrayList<Enemy> enemies){
        if (targetEnemy.active) {
            double distance = calcDistanceToEnemy(posX, posY, targetEnemy.posX, targetEnemy.posY);
            if (distance > targetEnemy.radius) {
                total_distanceX = (targetEnemy.posX - posX);
                total_distanceY = (targetEnemy.posY - posY);

                travel_distanceX = ((total_distanceX) / (Math.abs(total_distanceX) + Math.abs(total_distanceY))) * speed; //relative
                travel_distanceY = ((total_distanceY) / (Math.abs(total_distanceX) + Math.abs(total_distanceY))) * speed;

                posX += travel_distanceX;
                posY += travel_distanceY;

            } else {
                targetEnemy.wasHit(damage);
                active = false;
            }
        }else{
            closestEnemyDistance = DisplayAdvisor.maxY;
            //TODO add support for no enemies
            for(Enemy e:enemies){
                if (calcDistanceToEnemy(e.posX, e.posY, posX, posY) < closestEnemyDistance){
                    closestEnemyDistance = calcDistanceToEnemy(e.posX, e.posY, posX, posY);
                    closestEnemy = e;
                }
            }
            targetEnemy = closestEnemy;
        }

    }

    double calcDistanceToEnemy(int fromX, int fromY, int toX, int toY){
        return Math.sqrt(Math.pow(Math.abs(fromX) - Math.abs(toX), 2) + Math.pow(Math.abs(fromY) - Math.abs(toY), 2));
    }

    public void drawProjectile(Canvas canvas) {
        canvas.drawCircle(posX, posY, radius, paint);
    }
}
