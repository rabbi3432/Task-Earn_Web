package com.taskeearn.app;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

public class MainActivity extends Activity {
    private WebView webView;
    private TextView blocked;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        if (vpnActive()) { showBlocked(); return; }
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setLoadsImagesAutomatically(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        setContentView(webView);
        webView.loadUrl(BuildConfig.WEB_URL);
    }

    private boolean vpnActive() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        Network n = cm.getActiveNetwork();
        NetworkCapabilities c = n == null ? null : cm.getNetworkCapabilities(n);
        return c != null && c.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
    }

    private void showBlocked() {
        blocked = new TextView(this);
        blocked.setText("VPN চালু আছে।\n\nTask Earn ব্যবহার করতে VPN বন্ধ করুন এবং আবার অ্যাপ খুলুন।");
        blocked.setTextSize(19); blocked.setTextColor(Color.DKGRAY); blocked.setGravity(Gravity.CENTER); blocked.setPadding(40,40,40,40);
        setContentView(blocked);
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
}
