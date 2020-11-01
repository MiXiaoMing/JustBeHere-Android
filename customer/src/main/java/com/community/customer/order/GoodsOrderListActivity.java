package com.community.customer.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.community.customer.common.Constants;
import com.community.support.AutoBaseTitleActivity;

import java.util.ArrayList;

import cn.wdcloud.acaeva.R;

public class GoodsOrderListActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_order_list);

        final ArrayList<Fragment> fragments = new ArrayList<>();
        {
            GoodsOrderListFragment goodsOrderListFragment = new GoodsOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.goods_order_status_00);
            goodsOrderListFragment.setArguments(bundle);
            fragments.add(goodsOrderListFragment);
        }
        {
            GoodsOrderListFragment goodsOrderListFragment = new GoodsOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.goods_order_status_01);
            goodsOrderListFragment.setArguments(bundle);
            fragments.add(goodsOrderListFragment);
        }
        {
            GoodsOrderListFragment goodsOrderListFragment = new GoodsOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.goods_order_status_02);
            goodsOrderListFragment.setArguments(bundle);
            fragments.add(goodsOrderListFragment);
        }
        {
            GoodsOrderListFragment goodsOrderListFragment = new GoodsOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.goods_order_status_03);
            goodsOrderListFragment.setArguments(bundle);
            fragments.add(goodsOrderListFragment);
        }
        {
            GoodsOrderListFragment goodsOrderListFragment = new GoodsOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.goods_order_status_04);
            goodsOrderListFragment.setArguments(bundle);
            fragments.add(goodsOrderListFragment);
        }
        {
            GoodsOrderListFragment goodsOrderListFragment = new GoodsOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.goods_order_status_05);
            goodsOrderListFragment.setArguments(bundle);
            fragments.add(goodsOrderListFragment);
        }

        final ArrayList<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("待付款");
        titles.add("待发货");
        titles.add("待收货");
        titles.add("已收货");
        titles.add("已取消");

        TabLayout tabs = findViewById(R.id.tabs);
//            tabs.setTabTextColors(ContextCompat.getColor(view.getContext(), R.color.gray), ContextCompat.getColor(view.getContext(), R.color.white));
//            tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(view.getContext(), R.color.white));

        ViewPager vpContent =findViewById(R.id.vpContent);
        vpContent.setAdapter(new FragmentStatePagerAdapter(this.getSupportFragmentManager()) {
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

        LinearLayout llyBack = findViewById(R.id.llyBack);

        llyBack.setOnClickListener(clickListener);
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
