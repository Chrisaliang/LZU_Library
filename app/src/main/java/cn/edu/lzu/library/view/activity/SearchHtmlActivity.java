package cn.edu.lzu.library.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.edu.lzu.library.R;

public class SearchHtmlActivity extends AppCompatActivity {

    WebView wbSearch;
    Toolbar tbSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_html);
        wbSearch = findViewById(R.id.wb_search);
        tbSearch = findViewById(R.id.tb_search);

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
