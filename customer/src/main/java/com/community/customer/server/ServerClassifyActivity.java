package com.community.customer.server;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.common.ServerConfig;
import com.community.customer.api.servers.ServerClassify;
import com.community.customer.api.servers.ServerClassifyEntity;
import com.community.customer.api.servers.ServerDataManager;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.ReportUtil;

public class ServerClassifyActivity extends AutoBaseTitleActivity {
    private ServerClassify serverClassify;
    private ServerClassify linkServerClassify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_classify);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        llyBack.setOnClickListener(clickListener);

        initData();
    }

    private void initData() {
        String classify = getIntent().getStringExtra("code");
        if (TextUtils.isEmpty(classify)) {
            Logger.getLogger().e("数据为空：" + classify);
            return;
        }

        ServerDataManager serverDataManager = new ServerDataManager();

        Logger.getLogger().d("获取服务分类详情, code：" + classify);

        serverDataManager.getServerClassify(RequestBody.create(MediaType.parse("text/plain"), classify))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<ServerClassifyEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(ServerClassifyEntity result) {
                        serverClassify = result.data.serviceClassify;
                        linkServerClassify = result.data.linkServiceClassify;

                        initView();
                    }
                });
    }

    private void initView() {
        LinearLayout llyRoot = findViewById(R.id.llyRoot);

        TextView tvEmpty = findViewById(R.id.tvEmpty);
        tvEmpty.setVisibility(View.GONE);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(serverClassify.name);

        ImageView ivCover = findViewById(R.id.ivCover);
        ImageLoader.normal(this, ServerConfig.file_host + serverClassify.image, R.drawable.default_image_white, ivCover);

        ServerCommonView.serverClassify(this, llyRoot, serverClassify);
        ServerCommonView.serverTable(this, llyRoot, serverClassify.servers);
        ServerCommonView.serverClassify(this, llyRoot, linkServerClassify);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;
            }
        }
    };
}
