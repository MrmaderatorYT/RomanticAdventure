package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ccs.romanticadventure.data.PreferenceConfig;
import com.ccs.romanticadventure.data.WebAppInterface;
import com.ccs.romanticadventure.system.ExitConfirmationDialog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
//супер клас головного вікна, бо тільки так буде працювати код підтвердження виходу з програми

public class Game_First_Activity extends MainActivity {

    private WebView webView;
    private int katya, choose;
    float volumeLvl;
    MediaPlayer mediaPlayer;
    boolean type;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_first);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        choose = PreferenceConfig.getChoose(this);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        katya = PreferenceConfig.getKatyaValue(this);
        type = PreferenceConfig.getAnimSwitchValue(this);

        volumeLvl = PreferenceConfig.getVolumeLevel(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.school);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volumeLvl, volumeLvl);

        // Додаємо інтерфейс для можливості взаємодії Android коду та JavaScript. Тег для цього є Android
        com.ccs.romanticadventure.data.WebAppInterface webAppInterface = new com.ccs.romanticadventure.data.WebAppInterface(type, choose);
        webView.addJavascriptInterface(webAppInterface, "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        // Загрузка локального HTML-файла
        webView.loadUrl("file:///android_asset/index.html");
    }


    @Override
    public void onBackPressed() {
        ExitConfirmationDialog.showExitConfirmationDialog(this);
    }

    public class WebAppInterface {
        @JavascriptInterface
        public int indexFromJS(int value){
            choose = value;
            PreferenceConfig.setChoose(getApplicationContext(), choose);
            Toast.makeText(Game_First_Activity.this, ""+choose, Toast.LENGTH_LONG);
            return value;
        }
    }
}
