package com.example.ross.lopolytd;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Ross on 4/1/2017.
 */

public class Tower {
    int posX, posY;
    int radius;
    Paint paint;

    public Tower(int x, int y){
        posX = x;
        posY = y;
        radius = DisplayAdvisor.maxX/20;
        paint = new Paint();
        paint.setARGB(255,255,255,255);
    }

    void draw(Canvas canvas){
        canvas.drawCircle(posX, posY, radius, paint);
    }

    int getRadius(){
        return radius;
    }

}
