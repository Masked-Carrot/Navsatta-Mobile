package com.carrot.navsatta;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.TextureView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.view.View.LAYER_TYPE_HARDWARE;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isNetworkAvailable()){
            new AlertDialog.Builder(this)
                    .setTitle("No Internet")
                    .setMessage(" MediaVigil needs an active internet connection. Try reconnecting?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshweb);
        webView = findViewById(R.id.web);
        findViewById(R.id.web).setVisibility(View.INVISIBLE);
        findViewById(R.id.logo).setVisibility(View.VISIBLE);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alert();
                webView.loadUrl( "javascript:window.location.reload( true)");

            }
        });

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                findViewById(R.id.logo).setVisibility(View.INVISIBLE);
                findViewById(R.id.web).setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                alert();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://navsatta.com/");







    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void alert(){
        if(!isNetworkAvailable()){
            webView.stopLoading();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            Paint greyscalePaint = new Paint();
            greyscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            WebView v = findViewById(R.id.web);
            v.setLayerType(LAYER_TYPE_HARDWARE, greyscalePaint);
            new AlertDialog.Builder(this)
                    .setTitle("No Internet")
                    .setMessage("Navsatta needs an active internet connection to bring you the latest news. Try reconnecting?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alert();
                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{
            ColorMatrix cm = new ColorMatrix();
            cm.reset();
            Paint color = new Paint();
            color.setColorFilter(new ColorMatrixColorFilter(cm));
            WebView v = findViewById(R.id.web);
            v.setLayerType(LAYER_TYPE_HARDWARE, color);
        }
    }

}