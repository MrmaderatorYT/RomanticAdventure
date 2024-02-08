package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ccs.romanticadventure.data.PreferenceConfig;
import com.ccs.romanticadventure.system.ExitConfirmationDialog;

public class MainActivity extends AppCompatActivity {
    TextView startGame, settings;
    MediaPlayer mp;
    float volume;

    //метод, який створює екран
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //фіксуємо орієнтацію яка не зміниться (альбомна)

        settings = findViewById(R.id.settings);
        startGame = findViewById(R.id.startBtn);
        //завантажуємо дані, які нам потрібно
    }


    //Метод який завжди виконується при старті програми
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();
        volume = PreferenceConfig.getVolumeLevel(this);
        Toast.makeText(MainActivity.this, "аа"+volume, Toast.LENGTH_LONG).show();
        //створюємо пісню, яка буде нескінченною
        //і запускаємо
        mp = MediaPlayer.create(this, R.raw.intro);
        mp.setLooping(true);
        mp.setVolume(volume, volume);
        mp.start();

        startGame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Game_First_Activity.class));
                overridePendingTransition(0, 0);
                return false;
            }
        });
        settings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Settings.class));
                overridePendingTransition(0,0);
                return false;
            }
        });


    }


    private void preferences() {
        volume = PreferenceConfig.getVolumeLevel(this);
    }

    @Override
    public void onBackPressed() {
        ExitConfirmationDialog.showExitConfirmationDialog(this);
    }

    public void exit() {
        finish(); // завершення актівіті
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }
}
