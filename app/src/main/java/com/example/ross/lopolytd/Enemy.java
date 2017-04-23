package com.example.ross.lopolytd;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ross on 4/9/2017.
 */

public class Enemy {
    int posX,posY;
    int radius;
    Paint paint;
    ArrayList<Point> pathArray;
    double total_distanceX = 0;  //total distance to move between points x
    double total_distanceY = 0;  //total distance to move between points y
    double travel_distanceX = 0;
    double travel_distanceY = 0;
    double distance = 0;
    int path_point;             //path point to move towards
    int speed;
    boolean active = true;

    public Enemy(ArrayList<Point> pathArray){
        this.pathArray = pathArray;
        radius = (int)DisplayAdvisor.scaleX * 25;
        posX = pathArray.get(0).x;
        posY = pathArray.get(0).y;
        speed = 6;
        paint = new Paint();
        paint.setARGB(255,20,20,255);

    }

    void moveToNextPoint(){
        /* TODO
        implement better path following strategy
        the path is being followed but makes sharp turns? at speed = 4 or less
        looks like its only calculating correct path at intervals
        could have something to do with absolute values in travel_distance?
         */
        total_distanceX = (pathArray.get(path_point+1).x - posX);
        total_distanceY = (pathArray.get(path_point+1).y - posY);

        travel_distanceX = ((total_distanceX)/ (Math.abs(total_distanceX) + Math.abs(total_distanceY)))*speed; //relative
        travel_distanceY = ((total_distanceY)/ (Math.abs(total_distanceX) + Math.abs(total_distanceY)))*speed;

        posX += travel_distanceX;
        posY += travel_distanceY;


        distance = Math.sqrt((Math.pow((pathArray.get(path_point+1).x - pathArray.get(path_point).x), 2)) + (Math.pow((pathArray.get(path_point+1).y - pathArray.get(path_point).y), 2)));


        if(Math.sqrt((Math.pow((pathArray.get(path_point+1).x - posX), 2)) + (Math.pow((pathArray.get(path_point+1).y - posY), 2))) < 10){ //if enemy is within 10 px
            path_point++;
            posX = pathArray.get(path_point).x;
            posY = pathArray.get(path_point).y;
        }


    }

    void drawEnemy(Canvas canvas){
        if (active == true) {
            canvas.drawCircle(posX, posY, radius, paint);
        }
    }


}
