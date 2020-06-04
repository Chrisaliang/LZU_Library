package cn.edu.lzu.library.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.edu.lzu.library.R;
import cn.edu.lzu.library.module.dao.history.HistoryDAO;
import cn.edu.lzu.library.module.dao.history.HistorySQLiteOpenHelper;
import cn.edu.lzu.library.utils.UIUtils;
import cn.edu.lzu.library.view.adapter.SearchHistoryAdapter;

/**
 * 搜索界面
 * todo  添加搜索同步查询 完不成的任务 wtf
 */
public class SearchBookActivity extends AppCompatActivity {

    Toolbar tbSearch;
    SearchView svContent;
    WebView wbContent;
    ProgressBar pb;
    ListView lvHistory;

    private HistorySQLiteOpenHelper helper = new HistorySQLiteOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        // 初始化控件
        tbSearch = findViewById(R.id.tb_search);
        svContent = findViewById(R.id.sv_content);
        wbContent = findViewById(R.id.wb_content);
        pb = findViewById(R.id.pb);
        lvHistory = findViewById(R.id.lv_history);
        setSupportActionBar(tbSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbSearch.setNavigationOnClickListener(v -> finish());

        svContent.setSubmitButtonEnabled(false);
        svContent.setIconified(false);
        svContent.setIconifiedByDefault(true);
        svContent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) return false;
                startSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        wbContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        wbContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pb.setProgress(newProgress);//设置进度值
                }
            }
        });

        initHistoryLv();

    }


    /**
     * 加载历史搜索记录
     */
    private void initHistoryLv() {
        lvHistory.setVisibility(View.VISIBLE);
        lvHistory.setAdapter(new SearchHistoryAdapter(helper));
        int count = lvHistory.getCount();
        if (count > 0) {
            // 添加脚布局
            lvHistory.addFooterView(UIUtils.inflate(R.layout.item_search_history_clear));
        }
        lvHistory.setOnItemClickListener((parent, view, position, id) -> {
            // 点击条目事件
            // 点击条目事件进行再次查询
            if (position == lvHistory.getCount() - 1) {
                HistoryDAO.deleteAll(helper);
                // 这里是清空历史记录，所以隐藏历史记录
                lvHistory.setVisibility(View.GONE);
            } else {
                // 这里是条目点击事件
                // view = lvHistory.getChildAt(position);
                String history = ((TextView) view.findViewById(R.id.tv_history))
                        .getText().toString().trim();
                // ToastUtils.show(UIUtils.getContext(),history);
                startSearch(history);
            }
        });
    }


    /**
     * 查询按钮
     *
     * @param view 按钮
     */
    public void search(View view) {
        // 点击按钮开始查询结果，将得到的结果展示在下方
        startSearch();
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
            if (wbContent.canGoBack()) {
                // 当webview不是处于第一页面时，返回上一个页面
                wbContent.goBack();
                return true;
            } else {
                // 当webview处于第一页面时,直接退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 开始查询
     */
    private void startSearch() {
        String local = svContent.getQuery().toString().trim();
        if (TextUtils.isEmpty(local)) {
            return;
        }
        startSearch(local);
    }

    private void startSearch(String query) {
//      startSearch("",history);
//    }
//    private void startSearch(String query,String history) {
        // 去掉文本框的焦点，关闭键盘
        svContent.clearFocus();
        // 关闭历史记录
        lvHistory.setVisibility(View.GONE);

        // 开始查询
        String url = "http://ms.lib.lzu.edu.cn:8080/search?kw=" +
                query + "&xc=3";
//        String url = "http://219.246.191.24:8080/search?kw=" +
//                history + "&xc=5";

        // 储存历史记录
        saveHistory(query);

        wbContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        WebSettings settings = wbContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        wbContent.loadUrl(url);
    }

    private void saveHistory(String history) {
        // 第一步：查询是否已经此url被保存
        boolean hasQueryData = HistoryDAO.queryData(helper, history);
        // 第二步：
        if (hasQueryData) { // 已经有此记录
            // 更新此记录
            HistoryDAO.update(helper, history);
        } else {  // 没有此记录
            // 插入此记录
            HistoryDAO.insert(helper, history);
        }
    }

}
