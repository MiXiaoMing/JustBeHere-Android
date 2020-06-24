package com.community.customer.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.community.customer.common.Constants;
import com.community.support.AutoBaseTitleActivity;

import java.util.ArrayList;

import cn.wdcloud.acaeva.R;

public class ServerOrderListActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_order_list);

        final ArrayList<Fragment> fragments = new ArrayList<>();
        {
            ServerOrderListFragment serverOrderListFragment = new ServerOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.server_order_status_00);
            serverOrderListFragment.setArguments(bundle);
            fragments.add(serverOrderListFragment);
        }
        {
            ServerOrderListFragment serverOrderListFragment = new ServerOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.server_order_status_01);
            serverOrderListFragment.setArguments(bundle);
            fragments.add(serverOrderListFragment);
        }
        {
            ServerOrderListFragment serverOrderListFragment = new ServerOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.server_order_status_02);
            serverOrderListFragment.setArguments(bundle);
            fragments.add(serverOrderListFragment);
        }
        {
            ServerOrderListFragment serverOrderListFragment = new ServerOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.server_order_status_03);
            serverOrderListFragment.setArguments(bundle);
            fragments.add(serverOrderListFragment);
        }
        {
            ServerOrderListFragment serverOrderListFragment = new ServerOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.server_order_status_04);
            serverOrderListFragment.setArguments(bundle);
            fragments.add(serverOrderListFragment);
        }
        {
            ServerOrderListFragment serverOrderListFragment = new ServerOrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.server_order_status_05);
            serverOrderListFragment.setArguments(bundle);
            fragments.add(serverOrderListFragment);
        }

        final ArrayList<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("待付款");
        titles.add("待服务");
        titles.add("服务中");
        titles.add("已完成");
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

    }
}
