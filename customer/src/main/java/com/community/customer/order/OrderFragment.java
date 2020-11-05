package com.community.customer.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.wdcloud.acaeva.R;
import com.community.support.common.Constants;
import com.community.support.BaseFragment;

import java.util.ArrayList;

public class OrderFragment extends BaseFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order, null);

            final ArrayList<Fragment> fragments = new ArrayList<>();
            {
                ServerOrderListFragment serverOrderListFragment = new ServerOrderListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", Constants.server_order_status_01_02);
                serverOrderListFragment.setArguments(bundle);
                fragments.add(serverOrderListFragment);
            }
            {
                GoodsOrderListFragment goodsOrderListFragment = new GoodsOrderListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", Constants.goods_order_status_01_02);
                goodsOrderListFragment.setArguments(bundle);
                fragments.add(goodsOrderListFragment);
            }

            final ArrayList<String> titles = new ArrayList<>();
            titles.add("服务订单");
            titles.add("商品订单");

            TabLayout tabs = view.findViewById(R.id.orderTabs);
//            tabs.setTabTextColors(ContextCompat.getColor(view.getContext(), R.color.gray), ContextCompat.getColor(view.getContext(), R.color.white));
//            tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(view.getContext(), R.color.white));

            ViewPager vpContent =view.findViewById(R.id.vpOrderContent);
            vpContent.setAdapter(new FragmentStatePagerAdapter(OrderFragment.this.getActivity().getSupportFragmentManager()) {
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
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }
}
