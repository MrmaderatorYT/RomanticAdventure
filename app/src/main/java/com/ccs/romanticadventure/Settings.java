package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ccs.romanticadventure.data.PreferenceConfig;

public class Settings extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView volumeTextView;
    float volumeLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        volumeLvl = PreferenceConfig.getVolumeLevel(this);
        System.out.println("aaaaaaaaaaaaaaaaaaaaa"+volumeLvl);
        mediaPlayer = MediaPlayer.create(this, R.raw.intro);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mediaPlayer.setVolume(volumeLvl, volumeLvl);

        seekBar = findViewById(R.id.volume_set);
        volumeTextView = findViewById(R.id.volume_text);


    }


    private void initializeSeekBar(int value) {
        value = (int)volumeLvl;
        seekBar.setMax(100); // Встановіть максимальне значення, ви можете взяти те, яке вам потрібно
        seekBar.setProgress(value);
        System.out.println("ініціалізація"+value);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeLvl = (float) progress/100; // Оновлюємо значення volumeLvl
                System.out.println("aaaaaaaaaaaaaaaaaaaaa"+volumeLvl);
                updateVolume(); // Передаємо нове значення у метод updateVolume
                PreferenceConfig.setVolumeLevel(getApplicationContext(), volumeLvl);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Необов'язково реагувати на початок взаємодії з ползунком
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Необов'язково реагувати на завершення взаємодії з ползунком
                PreferenceConfig.setVolumeLevel(getApplicationContext(), volumeLvl);

            }
        });
    }


    private void initializeMediaPlayer() {
        // Ініціалізація mediaPlayer (відтворення, пауза, зупинка, тощо)
    }

    private void updateVolume() {
        mediaPlayer.setVolume(volumeLvl, volumeLvl);
        volumeTextView.setText("Гучність: " + (int) (volumeLvl * 100) + "%");
        System.out.println("aaaaaaaaaaaaaaaaaaaaa"+volumeLvl);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release(); // Важливо звільнити ресурси mediaPlayer

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release(); // Важливо звільнити ресурси mediaPlayer

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferenceConfig.setVolumeLevel(getApplicationContext(), volumeLvl);
        finish();
        overridePendingTransition(0,0);
    }
    @Override
    protected void onStart(){
        super.onStart();
        initializeSeekBar((int)volumeLvl);
        initializeMediaPlayer();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        volumeLvl = PreferenceConfig.getVolumeLevel(this);

    }
}
