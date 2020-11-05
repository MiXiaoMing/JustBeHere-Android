package com.community.customer.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.LoginActivity;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.user.result.ServerOrderListEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.support.common.Constants;
import com.community.customer.order.adapter.ServerOrderAdapter;
import com.community.support.BaseFragment;
import com.community.support.utils.UserInfoUtil;

import java.util.ArrayList;
import java.util.List;

import cn.wdcloud.acaeva.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ServerOrderListFragment extends BaseFragment {
    private View view;
    private ServerOrderAdapter serverOrderAdapter;
    private String type;

    public ServerOrderListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order_list, null);


            // TODO: 2020/6/29 ListView 需要替换 RecycleView，类似彩民：TuijianMatchAdapter


            ListView lvOrders = view.findViewById(R.id.lvOrders);
            TextView tvMore = view.findViewById(R.id.tvMore);

            serverOrderAdapter = new ServerOrderAdapter(this.getContext());
            lvOrders.setAdapter(serverOrderAdapter);

            type = getArguments().getString("type", Constants.server_order_status_00);
            Logger.getLogger().d("获取未完成服务订单列表: type -> " + type);

            if (type.equals(Constants.server_order_status_01_02)) {
                tvMore.setOnClickListener(clickListener);
                tvMore.setVisibility(View.VISIBLE);

                if (UserInfoUtil.isLogin()) {
                    getUndoneServerOrderList();
                } else {
                    startActivity(new Intent(this.getContext(), LoginActivity.class));
                }
            } else {
                tvMore.setVisibility(View.GONE);

                if (UserInfoUtil.isLogin()) {
                    getServerOrderList();
                } else {
                    startActivity(new Intent(this.getContext(), LoginActivity.class));
                }
            }
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    private void getUndoneServerOrderList() {
        UserDataManager dataManager = new UserDataManager();

        dataManager.getUndoneServerOrderList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<ServerOrderListEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(ServerOrderListEntity result) {
                        serverOrderAdapter.addAll(result.data);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    private void getServerOrderList() {
        UserDataManager dataManager = new UserDataManager();

        dataManager.getServerOrderList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<ServerOrderListEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(ServerOrderListEntity result) {
                        if (type.equals(Constants.server_order_status_00)) {
                            serverOrderAdapter.addAll(result.data);
                            return;
                        }

                        List<ServerOrderListEntity> serverOrders = result.data;
                        ArrayList<ServerOrderListEntity> resultOrders = new ArrayList<>();
                        for (ServerOrderListEntity order : serverOrders) {
                            if (Constants.convertServerOrderStatus(order.order.status).equals(type)) {
                                resultOrders.add(order);
                            }
                        }
                        serverOrderAdapter.addAll(resultOrders);
                    }
                });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvMore:
                    startActivity(new Intent(view.getContext(), ServerOrderListActivity.class));
                    break;
            }
        }
    };
}
