package cn.edu.lzu.library.view.fragment;

import android.os.Build;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.module.http.HttpUrl;
import cn.edu.lzu.library.view.ui.view.ShowPager;
import cn.edu.lzu.library.utils.UIUtils;

/**
 * Created by chris on 2017/4/24.
 */

public class ClassificationFragment extends BaseFragment {

    private WebView webView;

    @Override
    protected View onSubCreateSuccessView() {
        LinearLayout view = (LinearLayout) UIUtils.inflate(R.layout.layout_fragment_classification);
        webView = (WebView) view.findViewById(R.id.wb_classification);
        initWebView();

        webView.loadUrl(HttpUrl.CLASSIFICATION);
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
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);//关键点

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
    }

    @Override
    protected ShowPager.ResultState onSubLoadData() {
        return ShowPager.ResultState.ENUM_STATE_SUCCESS;
    }
}
