package com.rss.rtabdil.pcapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rss.rtabdil.pcapp.ui.ApplicationDesign;

/**
 * Created by rtabdil on 3/18/17.
 */

public class WebView extends AppCompatActivity {
    android.webkit.WebView webView;
    android.webkit.WebResourceRequest webResourceRequest;
    private static final String TAG = WebView.class.getName();
    Context mContext;
    protected ApplicationDesign mApplicationDesign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationDesign = new ApplicationDesign(this);
        //Creating Layouts and adding views programmatically
        LinearLayout lrLayout = new LinearLayout(getApplicationContext());
        lrLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(params);
        Toolbar toolbar = mApplicationDesign.createToolbar(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        webView = new android.webkit.WebView(getApplicationContext());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        webView.setLayoutParams(params2);
        webView.setId(R.id.webviewP);

        Bundle bundle = getIntent().getExtras();
        String link = bundle.getString("link") + "?displayMobileNavigation=0";
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(link);

        TextView tView = createTextView();
        tView.setText(bundle.getString("title"));

        toolbar.addView(tView);
        frameLayout.addView(webView);
        lrLayout.addView(toolbar);
        lrLayout.addView(frameLayout);

        setContentView(lrLayout);


        this.webView.setWebViewClient(new WebViewClient() {

        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

    /**
     * Creating Textview Programatically
     *
     * @return
     */
    public TextView createTextView() {
        TextView tView = new TextView(this);
        tView.setMaxLines(2);
        tView.setEllipsize(TextUtils.TruncateAt.END);
        tView.setTextColor(Color.WHITE);
        return tView;
    }

}
