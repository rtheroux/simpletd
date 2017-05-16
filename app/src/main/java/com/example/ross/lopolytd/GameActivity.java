package com.example.ross.lopolytd;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    GameSurfaceView surfaceView;
    Point touched;
    boolean wasTouched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        DisplayAdvisor.setScreenDimensions(metrics);

        touched = new Point();
        wasTouched = false;

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        surfaceView = new GameSurfaceView(this);

        // Set the view content to our surface view, not R.layout.activity_game
        setContentView(surfaceView);
    }

    @Override public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();

        switch(action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                wasTouched = true;
                touched.x = (int) event.getX();
                touched.y = (int) event.getY();
                break;
        }
        return true;
    }

    public boolean wasScreenTouched(){
        return wasTouched;
    }

    public Point getTouch(){
        wasTouched = false;

        return touched;
    }

    protected void onPause() {
        super.onPause();
        surfaceView.onPause();  // tell surfaceview that it's not running
    }

    protected void onResume() {
        super.onResume();
        surfaceView.onResume();  // tell surfaceview that it is running
    }

    public void showScoreScreen(int enemiesDestroyed){
        Intent intent = new Intent(GameActivity.this, ScoreScreen.class);
        //System.out.println(enemiesDestroyed);
        String newString = Integer.toString(enemiesDestroyed);
        String key = null;
        intent.putExtra("score", newString);
        startActivity(intent);
        finish();

    }



}
