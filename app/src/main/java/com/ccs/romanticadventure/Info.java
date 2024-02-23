package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

public class Info extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView textView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        scrollView = findViewById(R.id.scrollView);
        textView = findViewById(R.id.textView);
        handler = new Handler();

        // Пример текста для отображения
        String longText = "Mr_maderator_YT - CEO, головний кодер, редактор фотографій" +
                "Costic Antonii a.k.a ssokeer - QA, сюжет\n" +
                "man_humor - сюжет \n" +
                "amygoxy - робота з фотографіями \n" +
                "\n" +
                "Dalee-3 - створювач картинок (yes)";

        // Устанавливаем текст
        textView.setText(longText);

        // Автоматическая прокрутка до конца текста
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }
}
