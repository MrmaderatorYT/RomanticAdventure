package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ccs.romanticadventure.data.PreferenceConfig;

public class MainActivity extends AppCompatActivity {
    TextView startGame;
    String ip;

    //метод, який створює екран
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //фіксуємо орієнтацію яка не зміниться (альбомна)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        startGame = findViewById(R.id.startBtn);
        //завантажуємо дані, які нам потрібно
        preferences();
    }


    //Метод який завжди виконується при старті програми
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();
        ip = getDeviceIPAddress(MainActivity.this);
        PreferenceConfig.saveIP(getApplicationContext(), ip);

        startGame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Game_First_Activity.class));
                overridePendingTransition(0, 0);
                return false;
            }
        });

    }


    //отримання ІР по вай фаю
    public String getDeviceIPAddress(Context context) {
        //Створюємо змінну, яка отримує дані ІР по вай фаю
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //отримуємо тип підключення (Є безпровідна, а є ще Езернет)
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();

        // Форматування по 16ти розрядному тексту
        @SuppressLint("DefaultLocale") String ipAddressStr = String.format("%d.%d.%d.%d",
                (ipAddress & 0xFF),
                (ipAddress >> 8 & 0xFF),
                (ipAddress >> 16 & 0xFF),
                (ipAddress >> 24 & 0xFF));
        //Виводимо на екран текст
        Toast.makeText(MainActivity.this, ""+ipAddress+"", Toast.LENGTH_LONG).show();
        return ipAddressStr;
    }
    private void preferences(){
        ip = PreferenceConfig.getIP(this);

    }
}