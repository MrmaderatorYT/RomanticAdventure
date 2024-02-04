package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ccs.romanticadventure.data.PreferenceConfig;

public class Settings extends AppCompatActivity {

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
        mediaPlayer = MediaPlayer.create(this, R.raw.intro); // Замініть your_audio_file на ім'я вашого аудіофайлу
        seekBar = findViewById(R.id.volume_set);
        volumeTextView = findViewById(R.id.volume_text);

        initializeSeekBar();
        initializeMediaPlayer();
    }

    private void initializeSeekBar() {
        seekBar.setMax((int) volumeLvl);
        seekBar.setProgress((int)volumeLvl);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (int)volumeLvl;
                updateVolume((int)volumeLvl);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Необов'язково реагувати на початок взаємодії з ползунком
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Необов'язково реагувати на завершення взаємодії з ползунком
            }
        });
    }

    private void initializeMediaPlayer() {
        // Ініціалізація mediaPlayer (відтворення, пауза, зупинка, тощо)
    }

    private void updateVolume(int progress) {
        volumeLvl = (float)progress/100;
        mediaPlayer.setVolume(volumeLvl, volumeLvl);
        volumeTextView.setText("Гучність: " + progress + "%");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release(); // Важливо звільнити ресурси mediaPlayer
    }
}