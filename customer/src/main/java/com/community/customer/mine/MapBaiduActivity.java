package com.community.customer.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.appframe.utils.logger.Logger;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.community.support.AutoBaseTitleActivity;

import cn.wdcloud.acaeva.R;

//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.CircleOptions;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationConfiguration;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.Stroke;
//import com.baidu.mapapi.model.LatLng;

public class MapBaiduActivity extends AutoBaseTitleActivity {
//    private MapView mapView;
//    private BaiduMap baiduMap;
    public LocationClient mLocationClient = null;
    private boolean isFirstLocation = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_baidu);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        llyBack.setOnClickListener(clickListener);

//        mapView = findViewById(R.id.mapView);
//        baiduMap = mapView.getMap();
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        //设置缩放级别，默认级别为12
//        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(16);
//        baiduMap.setMapStatus(mapstatusUpdate);
//
//        baiduMap.setMyLocationEnabled(true);
//        baiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);

        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd0911");// 返回的定位结果是百度经纬度，默认值gcj02
//        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedLocationPoiList(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new LocationListener());
        mLocationClient.start();
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

//            MyLocationData data = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    .direction(location.getDirection())
//                    .latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            baiduMap.setMyLocationData(data);
//
//            if(isFirstLocation){
//                //获取经纬度
//                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
//                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
//                baiduMap.animateMapStatus(status);//动画的方式到中间
//                isFirstLocation = false;
//            }
        }
    }

//    private BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
//        @Override
//        public void onMapStatusChangeStart(MapStatus mapStatus) {
//
//        }
//
//        @Override
//        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
//
//        }
//
//        @Override
//        public void onMapStatusChange(MapStatus mapStatus) {
//
//        }
//
//        @Override
//        public void onMapStatusChangeFinish(MapStatus mapStatus) {
//
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        mapView.onResume();
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        mapView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mLocationClient.stop();
//        baiduMap.setMyLocationEnabled(false);
//        mapView.onDestroy();
//        mapView = null;
//        super.onDestroy();
//    }
}
