package com.community.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.support.common.Constants;
import com.community.customer.mall.MallFragment;
import com.community.customer.mine.MineFragment;
import com.community.customer.order.OrderFragment;
import com.community.customer.server.ServerFragment;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.LocationUtil;
import com.community.support.utils.PushUtil;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.taobao.sophix.SophixManager;

import java.util.ArrayList;
import java.util.List;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AutoBaseTitleActivity {

    private TextView tvServer, tvMall, tvOrder, tvMine;
    private ImageView ivServer, ivMall, ivOrder, ivMine;
    private FragmentManager fragmentManager;
    private Fragment serverFragment, mallFragment, orderFragment, mineFragment, currentFragment;
    public LocationClient locationClient = null;

    private UserDataManager dataManager = new UserDataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //阿里云 热修复 获取补丁
        SophixManager.getInstance().queryAndLoadNewPatch();
        List<String> tags = new ArrayList<>();
        tags.add(Constants.tag);
        SophixManager.getInstance().setTags(tags);

        setContentView(R.layout.activity_main);

        LinearLayout llyServer = findViewById(R.id.llyServer);
        LinearLayout llyMall = findViewById(R.id.llyMall);
        LinearLayout llyOrder = findViewById(R.id.llyOrder);
        LinearLayout llyMine = findViewById(R.id.llyMine);

        tvServer = findViewById(R.id.tvServer);
        tvMall = findViewById(R.id.tvMall);
        tvOrder = findViewById(R.id.tvOrder);
        tvMine = findViewById(R.id.tvMine);

        ivServer = findViewById(R.id.ivServer);
        ivMall = findViewById(R.id.ivMall);
        ivOrder = findViewById(R.id.ivOrder);
        ivMine = findViewById(R.id.ivMine);

        llyServer.setOnClickListener(clickListener);
        llyMall.setOnClickListener(clickListener);
        llyOrder.setOnClickListener(clickListener);
        llyMine.setOnClickListener(clickListener);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        serverFragment = new ServerFragment();
        fragmentTransaction.add(R.id.flyContainer, serverFragment).commit();
        currentFragment = serverFragment;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyServer:
                    if (currentFragment != serverFragment) {
                        ivServer.setImageResource(R.drawable.icon_server_click);
                        tvServer.setTextColor(getResources().getColor(R.color.blue_text));
                        ivMall.setImageResource(R.drawable.icon_mall);
                        tvMall.setTextColor(getResources().getColor(R.color.gray_text));
                        ivOrder.setImageResource(R.drawable.icon_order);
                        tvOrder.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMine.setImageResource(R.drawable.icon_mine);
                        tvMine.setTextColor(getResources().getColor(R.color.gray_text));

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(currentFragment).show(serverFragment).commit();
                        currentFragment = serverFragment;
                    }
                    break;
                case R.id.llyMall:
                    if (currentFragment != mallFragment) {
                        ivServer.setImageResource(R.drawable.icon_server);
                        tvServer.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMall.setImageResource(R.drawable.icon_mall_click);
                        tvMall.setTextColor(getResources().getColor(R.color.blue_text));
                        ivOrder.setImageResource(R.drawable.icon_order);
                        tvOrder.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMine.setImageResource(R.drawable.icon_mine);
                        tvMine.setTextColor(getResources().getColor(R.color.gray_text));

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (mallFragment == null) {
                            mallFragment = new MallFragment();
                            fragmentTransaction.hide(currentFragment).add(R.id.flyContainer, mallFragment).commit();
                        } else {
                            fragmentTransaction.hide(currentFragment).show(mallFragment).commit();
                        }
                        currentFragment = mallFragment;
                    }
                    break;
                case R.id.llyOrder:
                    if (currentFragment != orderFragment) {
                        ivServer.setImageResource(R.drawable.icon_server);
                        tvServer.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMall.setImageResource(R.drawable.icon_mall);
                        tvMall.setTextColor(getResources().getColor(R.color.gray_text));
                        ivOrder.setImageResource(R.drawable.icon_order_click);
                        tvOrder.setTextColor(getResources().getColor(R.color.blue_text));
                        ivMine.setImageResource(R.drawable.icon_mine);
                        tvMine.setTextColor(getResources().getColor(R.color.gray_text));

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (orderFragment == null) {
                            orderFragment = new OrderFragment();
                            fragmentTransaction.hide(currentFragment).add(R.id.flyContainer, orderFragment).commit();
                        } else {
                            fragmentTransaction.hide(currentFragment).show(orderFragment).commit();
                        }
                        currentFragment = orderFragment;
                    }
                    break;
                case R.id.llyMine:
                    if (currentFragment != mineFragment) {
                        ivServer.setImageResource(R.drawable.icon_server);
                        tvServer.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMall.setImageResource(R.drawable.icon_mall);
                        tvMall.setTextColor(getResources().getColor(R.color.gray_text));
                        ivOrder.setImageResource(R.drawable.icon_order);
                        tvOrder.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMine.setImageResource(R.drawable.icon_mine_click);
                        tvMine.setTextColor(getResources().getColor(R.color.blue_text));

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (mineFragment == null) {
                            mineFragment = new MineFragment();
                            fragmentTransaction.hide(currentFragment).add(R.id.flyContainer, mineFragment).commit();
                        } else {
                            fragmentTransaction.hide(currentFragment).show(mineFragment).commit();
                        }
                        currentFragment = mineFragment;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initBDLocation();
    }

    private void initBDLocation() {
        locationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd0911");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(true);
        option.setScanSpan(10 * 60 * 1000);
        option.setIsNeedLocationDescribe(true);
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedLocationPoiList(true);
        option.setOpenGps(true);
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new LocationListener());
        locationClient.start();
    }

    private class LocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            Logger.getLogger().e("位置详情：" + location.getLocType() + "..." + location.getLatitude() + "..." + addr + "..." + country + "..." + province + "..." + city + "..." + district + "..." + street);

            if (!TextUtils.isEmpty(addr)) {
                locationClient.stop();

                String maxLocation = LocationUtil.setLocation(addr);
                if (!TextUtils.isEmpty(maxLocation)) {
                    updateDevice(addr);
                }
            }
        }
    }

    public void updateDevice(String address) {
        Logger.getLogger().d("更新设备位置");
        String cid = PushUtil.getCID();
        if (TextUtils.isEmpty(cid)) {
            Logger.getLogger().e("cid为空，暂时不上传数据");
            return;
        }

        dataManager.updateDevice(cid, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmptyEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        Logger.getLogger().e("更新设备位置：" + e.getMessage());
                        ReportUtil.reportError(e);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmptyEntity result) {
                        if (!result.success) {
                            String str = "更新设备位置，msgCode：" + result.errCode + "/n" + result.message;
                            Logger.getLogger().e(str);
                            ReportUtil.reportError(str);
                        }
                    }
                });
    }


    // 退出登录
    private long clickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - clickTime < 2000) {
                finish();
            } else {
                ToastUtil.show(MainActivity.this, "再点一次将退出程序");
                clickTime = secondTime;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
