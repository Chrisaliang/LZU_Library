package cn.edu.lzu.library.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.lzu.library.R;

public class SearchHtmlActivity extends AppCompatActivity {

    @BindView(R.id.wb_search)
    WebView wbSearch;
    @BindView(R.id.tb_search)
    Toolbar tbSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_html);
        ButterKnife.bind(this);

        setSupportActionBar(tbSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbSearch.setNavigationOnClickListener(v -> finish());

        wbSearch.setWebViewClient(new WebViewClient());
        WebSettings settings = wbSearch.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);

    }
}
