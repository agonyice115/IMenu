package com.huassit.imenu.android.ui;


import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.util.StringUtils;
import com.huassit.imenu.android.view.NavigationView;

public class WebViewActivity extends BaseActivity {

    public final static String URL = "URL";
    public static final String NAME = "NAME";

    private WebView webView;
    private NavigationView navigationView;

    @Override
    public int getContentView() {
        return R.layout.webview_activity;
    }

    @Override
    public void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigateItemVisibility(NavigationView.LEFT_IMAGE_VIEW, View.GONE);
        navigationView.setNavigateItemVisibility(NavigationView.FIRST_RIGHT_TEXT_VIEW, View.GONE);
        navigationView.setNavigateItemBackground(NavigationView.SECOND_RIGHT_TEXT_VIEW, R.drawable.close);
        navigationView.setNavigateItemOnClickListener(NavigationView.SECOND_RIGHT_TEXT_VIEW, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView = (WebView) findViewById(R.id.webview);

    }

    @Override
    public void initData() {
        String url = getIntent().getStringExtra(URL);
        String name = getIntent().getStringExtra(NAME);
        if (StringUtils.isBlank(name)) {
            name = getString(R.string.service_terms);
        }
        navigationView.setNavigateItemText(NavigationView.LEFT_TEXT_VIEW, name);
        if (StringUtils.isBlank(url)) {
            webView.loadUrl("file:///android_asset/notice.html");
        } else {
            webView.loadUrl(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setBackgroundColor(Color.parseColor(colorValue));
    }
}
