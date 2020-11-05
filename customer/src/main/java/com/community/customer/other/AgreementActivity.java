package com.community.customer.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.community.support.AutoBaseTitleActivity;

import cn.wdcloud.acaeva.R;

public class AgreementActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_agree);

        findViewById(R.id.llyBack).setOnClickListener(clickListener);

        initData();
    }

    private void initData() {
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(url);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.llyBack) {
                finish();
            }
        }
    };
}
