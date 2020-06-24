package com.community.customer.mall;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.mall.Goods;
import com.community.customer.api.mall.GoodsListEntity;
import com.community.customer.api.mall.MallDataManager;
import com.community.customer.mall.adapter.GoodsAdapter;
import com.community.support.BaseFragment;
import com.community.support.component.RecyclerViewItemDecoration;
import com.community.support.component.RecyclerViewScrollListener;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


public class GoodsListFragment extends BaseFragment {
    private View view;
    private GoodsAdapter goodsAdapter;

    private ArrayList<Goods> dataList = new ArrayList<>();
    private int page = 1;
    private String classify;

    public GoodsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_goods_list, null);

            Bundle bundle = getArguments();
            if (bundle == null) {
                return view;
            }
            classify = bundle.getString("code", "");
            String name = bundle.getString("name", "");
            String desc = bundle.getString("desc", "");

            TextView tvName = view.findViewById(R.id.tvName);
            TextView tvDesc = view.findViewById(R.id.tvDesc);

            tvName.setText(name);
            tvDesc.setText(desc);

            init();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    private void init() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager recyclerViewLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        RecyclerViewItemDecoration itemDecoration = new RecyclerViewItemDecoration(GoodsListFragment.this.getActivity(), AutoUtils.getPercentWidthSize(5));
        recyclerView.addItemDecoration(itemDecoration);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        goodsAdapter = new GoodsAdapter(GoodsListFragment.this.getActivity(), dataList);
        recyclerView.setAdapter(goodsAdapter);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                dataList.clear();
                page = 1;
                getData();

                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollToBottom() {
                goodsAdapter.setLoadState(goodsAdapter.LOADING);
                ++page;
                getData();
            }
        });

        getData();
    }

    private void getData() {
        Logger.getLogger().d("获取商品列表");
        MallDataManager dataManager = new MallDataManager();
        dataManager.getGoodsList(classify, page, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsListEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取商品列表：" + e.getMessage());
                        goodsAdapter.setLoadState(goodsAdapter.LOADING_COMPLETE);
                    }

                    @Override
                    public void onComplete() {
                        goodsAdapter.setLoadState(goodsAdapter.LOADING_COMPLETE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GoodsListEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取商品列表，msgCode：" + result.errCode + "/n" + result.message);
                            ReportUtil.reportError("获取商品列表，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取商品列表, result为空");
                                return;
                            }

                            List<Goods> goodsList = result.data.getGoodsList();
                            if (goodsList == null || goodsList.size() == 0) {
                                goodsAdapter.setLoadState(goodsAdapter.LOADING_END);
                                return;
                            }

                            dataList.addAll(goodsList);
                            goodsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
