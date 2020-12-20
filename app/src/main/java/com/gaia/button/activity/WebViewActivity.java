package com.gaia.button.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.utils.ConstantUtil;

import java.util.HashMap;
import java.util.Map;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends BaseActivity {
    public static final String URL_KEY = "url_key";
    public static final String TITLE_KEY = "title_key";
    private WebView mWebView;
    private String mUrl = "";
    private String webViewHeaderKey = "token" ;
    private String webViewHeaderValue = "" ;
    private TextView mTvTitle;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = findViewById(R.id.wv_web);
        mUrl = getIntent().getStringExtra(URL_KEY);
        title = getIntent().getStringExtra(TITLE_KEY);

        mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initCookie();
        init(mWebView);
        startLoad(mUrl);

    }
    private void initCookie() {

        AccountInfo accountInfo = PreferenceManager.getInstance().getAccountInfo();
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);

            if (accountInfo != null) {
                if (TextUtils.isEmpty(accountInfo.getToken())) {
//				cookieManager.removeAllCookie();// 移除
                } else {
                    cookieManager.setCookie(".lovetoshare168.com", "token=" + accountInfo.getToken() + ";path=/");
                    cookieManager.flush();
                }
            } else {
                cookieManager.removeAllCookie();// 移除
                cookieManager.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startLoad(String url) {
        if (TextUtils.isEmpty(url) || mWebView == null)
            return;
        CookieManager.getInstance().setCookie(ConstantUtil.NEW_BAPI_NAME_DEFAULT, "token="+PreferenceManager.getInstance().getAccountInfo().getToken());
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
    }

    private void init(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }


        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
