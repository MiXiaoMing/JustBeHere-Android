package com.community.customer.server;

import android.content.Intent;
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
import com.community.customer.LoginActivity;
import com.community.customer.common.ServerConfig;
import com.community.customer.api.servers.Server;
import com.community.customer.api.servers.ServerDataManager;
import com.community.customer.api.servers.ServerDetail;
import com.community.customer.api.servers.ServerDetailEntity;
import com.community.customer.mine.DepositActivity;
import com.community.customer.order.ServerOrderSelectActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.UserInfoUtil;

import java.io.Serializable;


public class ServerDetailActivity extends AutoBaseTitleActivity {
    public Server server;
    public ServerDetail serverDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_detail);

        initData();
    }

    private void initData() {
        String code = getIntent().getStringExtra("code");
        if (TextUtils.isEmpty(code)) {
            Logger.getLogger().e("数据为空：" + code);
            return;
        }

        ServerDataManager serverDataManager = new ServerDataManager();

        Logger.getLogger().d("获取服务详情, code：" + code);
        serverDataManager.getServerDetail(RequestBody.create(MediaType.parse("text/plain"), code))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerDetailEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取服务分类详情错误：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerDetailEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("服务详情获取失败，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("服务详情数据获取失败, result为空");
                                return;
                            }

                            server = result.data.service;
                            serverDetail = result.data.serviceDetail;

                            initView();
                        }
                    }
                });
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(server.name);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        llyBack.setOnClickListener(clickListener);

        ImageView ivCover = findViewById(R.id.ivCover);
        if (TextUtils.isEmpty(server.cover)) {
            ivCover.setVisibility(View.GONE);
        } else {
            ImageLoader.normal(this, ServerConfig.file_host + server.cover, R.drawable.default_image_white, ivCover);
        }

        LinearLayout llyDeposit = findViewById(R.id.llyDeposit);
        llyDeposit.setOnClickListener(clickListener);

        if (serverDetail.prices != null && serverDetail.prices.size() > 0) {
            TextView tvMinDiscountPrice = findViewById(R.id.tvMinDiscountPrice);
            tvMinDiscountPrice.setText(Math.round(serverDetail.prices.get(0).price * (1 -
                    serverDetail.prices.get(0).discount) * serverDetail.prices.get(0).minimum * 100) / 100.00 + "元优惠");

            TextView tvPrice = findViewById(R.id.tvPrice);
            tvPrice.setText(Math.round(serverDetail.prices.get(0).price * serverDetail.prices.get(0).discount
                    * serverDetail.prices.get(0).minimum * 100) / 100.00 + "");

            TextView tvOrder = findViewById(R.id.tvOrder);
            tvOrder.setOnClickListener(clickListener);

            LinearLayout llyPrice = findViewById(R.id.llyPrice);
            ServerDetailCommonView.prices(this, llyPrice, serverDetail.prices);
            if (!TextUtils.isEmpty(serverDetail.note)) {
                ServerDetailCommonView.note(this, llyPrice, serverDetail.note);
            }
        }

        LinearLayout llyIntroduce = findViewById(R.id.llyIntroduce);
        if (TextUtils.isEmpty(serverDetail.introduce) && TextUtils.isEmpty(serverDetail.refertime)
                && (serverDetail.others == null || serverDetail.others.size() == 0)) {
            llyIntroduce.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(serverDetail.introduce)) {
                ServerDetailCommonView.part(this, llyIntroduce, serverDetail.introduce);
            }
            if (!TextUtils.isEmpty(serverDetail.refertime)) {
                ServerDetailCommonView.refTime(this, llyIntroduce, serverDetail.refertime);
            }
            if (serverDetail.others != null && serverDetail.others.size() > 0) {
                ServerDetailCommonView.others(this, llyIntroduce, serverDetail.others);
            }
        }

        LinearLayout llyScope = findViewById(R.id.llyScope);
        if (TextUtils.isEmpty(serverDetail.scope)) {
            llyScope.setVisibility(View.GONE);
        } else {
            ServerDetailCommonView.part(this, llyScope, serverDetail.scope);
        }

        LinearLayout llyTools = findViewById(R.id.llyTools);
        if (TextUtils.isEmpty(serverDetail.tools)) {
            llyTools.setVisibility(View.GONE);
        } else {
            ServerDetailCommonView.part(this, llyTools, serverDetail.tools);
        }

        LinearLayout llyFlow = findViewById(R.id.llyFlow);
        if (TextUtils.isEmpty(serverDetail.flow)) {
            llyFlow.setVisibility(View.GONE);
        } else {
            ServerDetailCommonView.part(this, llyFlow, serverDetail.flow);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.llyDeposit:
                    if (!UserInfoUtil.isLogin()) {
                        startActivity(new Intent(ServerDetailActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(ServerDetailActivity.this, DepositActivity.class));
                    }
                    break;

                case R.id.tvOrder:
                    Intent intent = new Intent(ServerDetailActivity.this, ServerOrderSelectActivity.class);
                    intent.putExtra("code", server.code);
                    intent.putExtra("name", server.name);
                    intent.putExtra("price", (Serializable) serverDetail.prices);
                    startActivity(intent);
                    break;
            }
        }
    };
}
