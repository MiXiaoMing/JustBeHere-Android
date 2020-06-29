package com.community.customer.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.mall.GoodsClassifyListEntity;
import com.community.customer.api.mall.MallDataManager;
import com.community.customer.api.user.CountEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.mine.ShoppingCartActivity;
import com.community.support.BaseFragment;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.UserInfoUtil;

import java.util.ArrayList;

public class MallFragment extends BaseFragment {
    private View view;
    private TextView tvCartCount;
    private RelativeLayout rlyCart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mall, null);

            tvCartCount = view.findViewById(R.id.tvCartCount);
            rlyCart = view.findViewById(R.id.rlyCart);

            rlyCart.setOnClickListener(clickListener);

            getTabs();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfoUtil.isLogin()) {
            rlyCart.setVisibility(View.VISIBLE);

            shoppingCartCount();
        } else {
            rlyCart.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rlyCart:
                    startActivity(new Intent(MallFragment.this.getActivity(), ShoppingCartActivity.class));
                    break;
            }
        }
    };

    private void getTabs() {
        Logger.getLogger().d("获取商品分类数据");
        MallDataManager dataManager = new MallDataManager();

        dataManager.getGoodsClassifyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsClassifyListEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取商品分类数据：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GoodsClassifyListEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取商品分类数据，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取商品分类数据, result为空");
                                return;
                            }

                            ArrayList<GoodsClassifyListEntity> goodsClassifies = result.data;

                            final ArrayList<Fragment> fragments = new ArrayList<>();
                            final ArrayList<String> titles = new ArrayList<>();

                            for (GoodsClassifyListEntity goodsClassify : goodsClassifies) {
                                GoodsListFragment listFragment = new GoodsListFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("code", goodsClassify.code);
                                bundle.putString("name", goodsClassify.name);
                                bundle.putString("desc", goodsClassify.desc);
                                listFragment.setArguments(bundle);
                                fragments.add(listFragment);

                                titles.add(goodsClassify.title);
                            }

                            TabLayout tabs = view.findViewById(R.id.tabs);
//            tabs.setTabTextColors(ContextCompat.getColor(view.getContext(), R.color.gray), ContextCompat.getColor(view.getContext(), R.color.white));
//            tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(view.getContext(), R.color.white));

                            ViewPager vpContent = view.findViewById(R.id.vpContent);
                            vpContent.setAdapter(new FragmentStatePagerAdapter(MallFragment.this.getActivity().getSupportFragmentManager()) {
                                @Override
                                public Fragment getItem(int position) {
                                    return fragments.get(position);
                                }

                                @Override
                                public int getCount() {
                                    return fragments.size();
                                }

                                @Override
                                public void destroyItem(ViewGroup container, int position, Object object) {
                                    super.destroyItem(container, position, object);
                                }

                                @Nullable
                                @Override
                                public CharSequence getPageTitle(int position) {
                                    return titles.get(position);
                                }
                            });

                            tabs.setupWithViewPager(vpContent);
                        }
                    }
                });
    }

    private void shoppingCartCount() {
        Logger.getLogger().d("获取购物车商品数量");

        UserDataManager dataManager = new UserDataManager();
        dataManager.getShoppingCartCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CountEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(CountEntity result) {
                        int count = result.data.count;
                        if (count > 0) {
                            tvCartCount.setVisibility(View.VISIBLE);
                            tvCartCount.setText(count + "");
                        } else {
                            tvCartCount.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
