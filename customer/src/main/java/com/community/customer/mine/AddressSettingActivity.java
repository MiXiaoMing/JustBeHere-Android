package com.community.customer.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.user.AddressEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.TypefaceHelper;
import com.community.support.utils.GPSUtil;
import com.community.support.utils.PhoneFormatCheckUtils;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddressSettingActivity extends AutoBaseTitleActivity {
    private AddressEntity address;
    private String type;    //A: add,  U: update,  D: delete
    private UserDataManager dataManager = new UserDataManager();

    private TextView tvTitle;
    private EditText etContact, etCellphone, etRegion, etDetail;

    private LocationClient locationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_setting);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        address = (AddressEntity) getIntent().getSerializableExtra("address");

        if (TextUtils.isEmpty(type) || (type.equals("U") && address == null)) {
            type = "A";
        }

        initView();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        LinearLayout llyBack = findViewById(R.id.llyBack);
        TextView tvDelete = findViewById(R.id.tvDelete);

        etContact = findViewById(R.id.etContact);
        etCellphone = findViewById(R.id.etCellphone);
        etRegion = findViewById(R.id.etRegion);
        etDetail = findViewById(R.id.etDetail);

        etContact.setTypeface(TypefaceHelper.get(this));
        etCellphone.setTypeface(TypefaceHelper.get(this));
        etRegion.setTypeface(TypefaceHelper.get(this));
        etDetail.setTypeface(TypefaceHelper.get(this));

        LinearLayout llyCurrentPosition = findViewById(R.id.llyCurrentPosition);
        TextView tvSubmit = findViewById(R.id.tvSubmit);

        llyBack.setOnClickListener(clickListener);
        tvDelete.setOnClickListener(clickListener);
        llyCurrentPosition.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);

        if (type.equals("A")) {
            tvTitle.setText("添加服务地址");
            tvDelete.setVisibility(View.GONE);
        } else if (type.equals("U")) {
            tvTitle.setText("编辑服务地址");
            tvDelete.setVisibility(View.VISIBLE);

            etContact.setText(address.contact);
            etContact.setSelection(address.contact.length());
            etCellphone.setText(address.cellphone);
            etRegion.setText(address.region);
            etDetail.setText(address.detail);
        }
    }

    private void baiduStart() {
        if (GPSUtil.isOPen(this)) {
            if (locationClient == null) {
                locationClient = new LocationClient(this);
                LocationClientOption option = new LocationClientOption();
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
                option.setCoorType("bd0911");// 返回的定位结果是百度经纬度，默认值gcj02
                option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
                option.setIsNeedAddress(true);
                option.setIsNeedLocationDescribe(true);
                option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
                option.setIsNeedLocationPoiList(true);
                option.setOpenGps(true);
                locationClient.setLocOption(option);
                locationClient.registerLocationListener(new LocationListener());
            }
            if (!locationClient.isStarted()) {
                locationClient.start();
            }
        } else {
            ToastUtil.show(this, "定位失败，请打开GPS位置信息");
        }
    }

    public class LocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            Logger.getLogger().e("位置详情：" + location.getLatitude() + "..." + addr + "..." + country + "..." + province + "..." + city + "..." + district + "..." + street);

            if (!TextUtils.isEmpty(addr)) {
                locationClient.stop();
                if (addr.contains("中国")) {
                    addr = addr.substring(2);
                }
                etRegion.setText(addr);
            }
        }
    }

    private void updateAddress(String type) {
        Logger.getLogger().d("服务地址：" + type);

        String contact = etContact.getText().toString().trim();
        String cellphone = etCellphone.getText().toString().trim();
        String region = etRegion.getText().toString().trim();
        String detail = etDetail.getText().toString().trim();

        if (TextUtils.isEmpty(cellphone)) {
            ToastUtil.show(AddressSettingActivity.this, "请输入手机号");
            return;
        } else if (!PhoneFormatCheckUtils.isPhoneLegal(cellphone)) {
            ToastUtil.show(AddressSettingActivity.this, "请输入正确的手机号");
            return;
        } else if (TextUtils.isEmpty(contact)) {
            ToastUtil.show(AddressSettingActivity.this, "请输入联系人");
            return;
        } else if (TextUtils.isEmpty(region)) {
            ToastUtil.show(AddressSettingActivity.this, "请输入服务地址");
            return;
        }

        AddressEntity address = new AddressEntity();
        if (!type.equals("A")) {
            address.id = this.address.id;
        }
        address.contact = contact;
        address.cellphone = cellphone;
        address.region = region;
        address.detail = detail;

        dataManager.address(address, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<EmptyEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(EmptyEntity result) {
                        finish();
                    }
                });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvDelete:
                    updateAddress("D");
                    break;

                case R.id.llyCurrentPosition:
                    baiduStart();
                    break;

                case R.id.tvSubmit:
                    if (type.equals("A")) {
                        updateAddress("A");
                    } else if (type.equals("U")) {
                        updateAddress("U");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
        }
    }
}

