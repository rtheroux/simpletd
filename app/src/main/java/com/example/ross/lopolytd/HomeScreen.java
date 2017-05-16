package com.example.ross.lopolytd;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.R.attr.button;
import static android.R.attr.typeface;

public class HomeScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        TextView title = (TextView)findViewById(R.id.titleText);
        TextView play = (TextView)findViewById(R.id.playButton);
        TextView options = (TextView)findViewById(R.id.optionsButton);
        TextView exit = (TextView)findViewById(R.id.exitButton);


        Typeface title_font = Typeface.createFromAsset(getAssets(),  "fonts/Adlanta.otf");
        Typeface button_font = Typeface.createFromAsset(getAssets(), "fonts/Adlanta.otf");


        title.setTypeface(title_font);
        play.setTypeface(button_font);
        options.setTypeface(button_font);
        exit.setTypeface(button_font);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.xfiles);
        mp.start();

    }


    public void onPlayClicked(View view){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.pop);
        mp.start();
        Intent intent = new Intent(HomeScreen.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    public void onExitClicked(View view){
        finish();
        System.exit(0);
    }

    public void onOptionsClicked(View view){
        Context context = getApplicationContext();
        CharSequence text = "Unimplemented!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
