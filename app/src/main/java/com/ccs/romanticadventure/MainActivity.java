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
import android.widget.TextView;
import android.widget.Toast;

import com.ccs.romanticadventure.data.PreferenceConfig;
import com.ccs.romanticadventure.system.ExitConfirmationDialog;

public class MainActivity extends AppCompatActivity {
    TextView startGame, settings;
    String ip;
    MediaPlayer mp;
    float volume;

    //метод, який створює екран
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //фіксуємо орієнтацію яка не зміниться (альбомна)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        volume = PreferenceConfig.getVolumeLevel(this);

        settings = findViewById(R.id.settings);
        startGame = findViewById(R.id.startBtn);
        //завантажуємо дані, які нам потрібно
        preferences();
    }


    //Метод який завжди виконується при старті програми
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();
        //нам потірбне права
        //<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        //<uses-permission android:name="android.permission.INTERNET"/>
        //<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

        ip = getDeviceIPAddress(MainActivity.this);
        PreferenceConfig.saveIP(getApplicationContext(), ip);

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


    }


    //отримання ІР по вай фаю
    public String getDeviceIPAddress(Context context) {
        if (isNetworkAvailable(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();

            // Форматирование IP-адреса
            @SuppressLint("DefaultLocale") String ipAddressStr = String.format("%d.%d.%d.%d",
                    (ipAddress & 0xFF),
                    (ipAddress >> 8 & 0xFF),
                    (ipAddress >> 16 & 0xFF),
                    (ipAddress >> 24 & 0xFF));

            // Выводим IP-адрес в Toast
            Toast.makeText(context, "IP Address: " + ipAddressStr, Toast.LENGTH_LONG).show();

            return ipAddressStr;
        } else {
            Toast.makeText(context, "Нет подключения к Интернету", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void preferences() {
        ip = PreferenceConfig.getIP(this);

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
        mp.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }
}
