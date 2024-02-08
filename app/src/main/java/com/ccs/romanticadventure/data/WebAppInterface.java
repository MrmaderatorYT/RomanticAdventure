package com.ccs.romanticadventure.data;

import android.webkit.JavascriptInterface;

public class WebAppInterface {
    private boolean type;

    public WebAppInterface(boolean type) {
        this.type = type;
    }

    @JavascriptInterface
    public boolean getValue() {
        return type;
    }
}

