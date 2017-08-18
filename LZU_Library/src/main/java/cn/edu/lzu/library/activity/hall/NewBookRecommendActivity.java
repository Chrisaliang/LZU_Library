package cn.edu.lzu.library.activity.hall;

import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.activity.BaseActivity;
import cn.edu.lzu.library.http.HttpUrl;
import cn.edu.lzu.library.ui.view.ShowPager;
import cn.edu.lzu.library.utils.UIUtils;

public class NewBookRecommendActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected ShowPager.ResultState onSubLoadData() {
        return ShowPager.ResultState.ENUM_STATE_SUCCESS;
    }

    @Override
    protected void logic() {

    }

    @Override
    protected View fillLayout() {
        View view = UIUtils.inflate(R.layout.activity_new_book_recommend);
        webView = (WebView) view.findViewById(R.id.web_view_xstj);
        initWebView();

        webView.loadUrl(HttpUrl.NEWBOOKRECOMMEND);
        return view;
    }

    private void initWebView() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

    /**
     * 设置返回键动作（防止按返回键直接退出程序)
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                // 当webview不是处于第一页面时，返回上一个页面
                webView.goBack();
                return true;
            } else {
                // 当webview处于第一页面时,直接关闭当前页面
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public CharSequence setActivityTitle() {
        String string = getString(R.string.new_book_recommend);
        return string;
    }
}
