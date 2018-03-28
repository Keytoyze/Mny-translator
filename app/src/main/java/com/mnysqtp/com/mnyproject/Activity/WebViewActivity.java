package com.mnysqtp.com.mnyproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mnysqtp.com.mnyproject.R;

public class WebViewActivity extends AppCompatActivity {

    private static final String ERROR_SSL_OBSOLETE_CIPHER = "net::ERR_SSL_OBSOLETE_CIPHER";
    private WebView webView;
    private TextView errorText;
    private static final String KEY_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        webView = (WebView)findViewById(R.id.webview_activity_web);
        errorText = (TextView)findViewById(R.id.webview_activity_error);

        onPrepareWebView(webView);

        onLoadUri(webView);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    protected void onLoadUri(WebView webView) {
        webView.loadUrl(getIntent().getStringExtra(KEY_URL));
    }

    protected void onPageStared(WebView webView, String url, Bitmap favicon) {}

    protected void onPageFinished(WebView webView, String url) {}

    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    protected void onPrepareWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        // NOTE: This gives double tap zooming.
        webSettings.setUseWideViewPort(true);
        webView.setWebChromeClient(new ChromeClient());
        webView.setWebViewClient(new ViewClient());
    }

    private void showError(String error) {
        webView.setVisibility(View.GONE);
        errorText.setText(error);
        errorText.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        errorText.setVisibility(View.GONE);
        errorText.setText(null);
        webView.setVisibility(View.VISIBLE);
    }

    private class ChromeClient extends WebChromeClient {

        // NOTE: WebView can be trying to show an AlertDialog after the activity is finished, which
        // will result in a WindowManager$BadTokenException.
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                setProgressBarIndeterminateVisibility(false);
            } else {
                setProgressBarIndeterminateVisibility(true);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
        }


    }

    private class ViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebViewActivity.this.onPageStared(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            WebViewActivity.this.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            showError(description);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return WebViewActivity.this.shouldOverrideUrlLoading(view, url);
        }
    }

    public static void sendIntent(String url, Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }
}
