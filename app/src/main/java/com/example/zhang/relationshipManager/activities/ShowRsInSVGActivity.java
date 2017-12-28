package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.zhang.relationshipManager.R;

import butterknife.BindView;

/**
 * Created by zhang on 2017-12-28.
 */

public class ShowRsInSVGActivity extends BaseActivity {
    @BindView(R.id.svg_webView)
    WebView svgWebView;

    private String url;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, ShowRsInSVGActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_show_rs_svg;
    }

    @Override
    protected void initViews() {
        url = getIntent().getStringExtra("url");
        Log.w("Network URL:",url);
        svgWebView.getSettings().setJavaScriptEnabled(true);
        svgWebView.setWebViewClient(new WebViewClient());
        svgWebView.loadUrl(url);
    }
}
