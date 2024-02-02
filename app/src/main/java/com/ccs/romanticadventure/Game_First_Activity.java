package com.ccs.romanticadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ccs.romanticadventure.data.PreferenceConfig;

public class Game_First_Activity extends AppCompatActivity {

    private WebView webView;
    private int choose;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_first);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Додаємо інтерфейс для можливості взаємодії Android коду та JavaScript. Тег для цього є Android
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        // Загрузка локального HTML-файла
        webView.loadUrl("file:///android_asset/game_first_activity.html");
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences();
    }

    // Клас для створення методів для взаємодії між кодом
    public class WebAppInterface {

        @JavascriptInterface
        public void firstButtonTouched(){
            choose = 0;
            PreferenceConfig.setFirstChoose(getApplicationContext(), choose);
            Intent intent = new Intent(Game_First_Activity.this, MainActivity.class);
            startActivity(intent);
        }
        public void secondButtonTouched(){
            choose = 1;
            PreferenceConfig.setFirstChoose(getApplicationContext(), choose);
            Intent intent = new Intent(Game_First_Activity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private void preferences(){
        choose = PreferenceConfig.getFirstChoose(this);
    }
}
