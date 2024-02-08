package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ccs.romanticadventure.data.PreferenceConfig;
import com.ccs.romanticadventure.data.WebAppInterface;
import com.ccs.romanticadventure.system.ExitConfirmationDialog;
//супер клас головного вікна, бо тільки так буде працювати код підтвердження виходу з програми

public class Game_First_Activity extends MainActivity {

    private WebView webView;
    private int katya;
    float volumeLvl;
    MediaPlayer mediaPlayer;
    boolean type;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_first);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
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
        com.ccs.romanticadventure.data.WebAppInterface webAppInterface = new com.ccs.romanticadventure.data.WebAppInterface(type);
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
    protected void onStart() {
        super.onStart();
    }

    // Клас для створення методів для взаємодії між кодом
    public class WebAppInterface {

        @JavascriptInterface
        public void firstChooseYes(){
            --katya;
            PreferenceConfig.setKatyaValue(getApplicationContext(), katya);

        }
        public void firstChooseNo(){
            --katya;
            PreferenceConfig.setKatyaValue(getApplicationContext(), katya);
        }
    }
    @Override
    public void onBackPressed() {
        ExitConfirmationDialog.showExitConfirmationDialog(this);
    }
}
