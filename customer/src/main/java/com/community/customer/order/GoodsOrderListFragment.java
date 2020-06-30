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
import com.community.customer.api.CustomObserver;
import com.community.customer.api.user.GoodsOrderEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.common.Constants;
import com.community.customer.order.adapter.GoodsOrderAdapter;
import com.community.support.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.wdcloud.acaeva.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoodsOrderListFragment extends BaseFragment {
    private View view;
    private GoodsOrderAdapter goodsOrderAdapter;
    private String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order_list, null);

            ListView lvOrders = view.findViewById(R.id.lvOrders);
            TextView tvMore = view.findViewById(R.id.tvMore);

            goodsOrderAdapter = new GoodsOrderAdapter(this.getContext());
            lvOrders.setAdapter(goodsOrderAdapter);

            type = getArguments().getString("type", Constants.goods_order_status_00);
            Logger.getLogger().d("获取未完成商品订单列表: type -> " + type);

            if (type.equals(Constants.goods_order_status_01_02)) {
                tvMore.setOnClickListener(clickListener);
                tvMore.setVisibility(View.VISIBLE);

                getUndoneGoodsOrderList();
            } else {
                tvMore.setVisibility(View.GONE);

                getServerOrderList();
            }
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    private void getUndoneGoodsOrderList() {
        new UserDataManager().getUndoneGoodsOrderList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<GoodsOrderEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(GoodsOrderEntity result) {
                        goodsOrderAdapter.addAll(result.data);
                    }
                });
    }

    private void getServerOrderList() {
        new UserDataManager().getGoodsOrderList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<GoodsOrderEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(GoodsOrderEntity result) {
                        if (type.equals(Constants.server_order_status_00)) {
                            goodsOrderAdapter.addAll(result.data);
                            return;
                        }

                        List<GoodsOrderEntity> goodsOrders = result.data;
                        ArrayList<GoodsOrderEntity> resultOrders = new ArrayList<>();
                        for (GoodsOrderEntity order : goodsOrders) {
                            if (Constants.convertGoodsOrderStatus(order.goodsOrder.status).equals(type)) {
                                resultOrders.add(order);
                            }
                        }
                        goodsOrderAdapter.addAll(resultOrders);
                    }
                });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvMore:
                    startActivity(new Intent(view.getContext(), GoodsOrderListActivity.class));
                    break;
            }
        }
    };
}
