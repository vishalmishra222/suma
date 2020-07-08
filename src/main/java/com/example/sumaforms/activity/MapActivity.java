package com.example.sumaforms.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Toolbar;

import com.example.sumaforms.R;
import com.example.sumaforms.utils.IOUtils;

import im.delight.android.webview.AdvancedWebView;

public class MapActivity extends AppCompatActivity implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView;
    private Toolbar toolbar;
    private ProgressDialog mapProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if(IOUtils.isInternetPresent(getApplicationContext()))
        {
            String pinCode = "";
            Bundle b = getIntent().getExtras();
            if (b != null) {
                pinCode = b.getString("postalCode");
            }
            findViews();
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // chromium, enable hardware acceleration
                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                // older android version, disable hardware acceleration
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            mWebView.setListener(this, this);
            mWebView.loadUrl("http://maps.google.com/maps?q= " + pinCode.trim());
        }
        else
        {
            IOUtils.showErrorMessage(getApplicationContext(),"No internet connection");
        }

    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        IOUtils.startLoading(MapActivity.this,"Please wait...");
    }

    @Override
    public void onPageFinished(String url) {
        IOUtils.stopLoading();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        IOUtils.stopLoading();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    private void findViews()
    {
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.app_bar);
        }
       // setSupportActionBar(toolbar);
        mWebView = (AdvancedWebView) findViewById(R.id.webview);*/
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Google Map");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        getSupportActionBar().setIcon(R.drawable.sumaicon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
              finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}