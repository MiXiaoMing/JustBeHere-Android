package com.community.customer.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.community.customer.common.Constants;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.DeviceInfo;
import com.community.support.utils.PayCommonUtil;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.community.support.utils.UserInfoUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DepositActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        initView();
    }

    private void initView() {
        LinearLayout llyBack = findViewById(R.id.llyBack);

        TextView tvCellphone = findViewById(R.id.tvCellphone);
        TextView tvDeposit = findViewById(R.id.tvDeposit);

        llyBack.setOnClickListener(clickListener);
        tvDeposit.setOnClickListener(clickListener);

        tvCellphone.setText(UserInfoUtil.getCellphone());
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvDeposit:
                    unifiedOrder();
                    break;
            }
        }
    };



    private void unifiedOrder() {
//        final String body, detail;
//        final String payFee = String.valueOf(price);
//        if (orderType.equals(Constants.order_type_server)) {
//            body = "倾心-服务订单";
//            detail = "服务";
//        } else {
//            body = "倾心-商品订单";
//            detail = "商品";
//        }
        final String body = "伟东测评-100灵石";
        final String payFee = "0.01";
        final String detail = "100灵石";

        // TODO: 2020/5/22 不应该注释掉
//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//
//                SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
//                String currTime = PayCommonUtil.getCurrTime();
//                String strTime = currTime.substring(8, currTime.length());
//                String strRandom = PayCommonUtil.buildRandom(4) + "";
//                String nonce_str = strTime + strRandom;
//                packageParams.put("appid", Constants.wx_app_id);// 公众账号ID
//                packageParams.put("mch_id", Constants.wx_mch_id);// 商户号
//                packageParams.put("nonce_str", nonce_str);// 随机字符串
//                packageParams.put("body", body); //商品描述
//                packageParams.put("detail", detail);
//                packageParams.put("out_trade_no", tradeID); //商户订单号
//                packageParams.put("total_fee", String.valueOf((int) (Float.valueOf(payFee) * 100)));
//                packageParams.put("spbill_create_ip", DeviceInfo.getIp());
//                if (orderType.equals(Constants.order_type_server)) {
//                    packageParams.put("notify_url", ServerConfig.getInstance().getServerUrl("MobileServer") + "/user/wxpay/wxPayNotify/server");
//                } else {
//                    packageParams.put("notify_url", ServerConfig.getInstance().getServerUrl("MobileServer") + "/user/wxpay/wxPayNotify/goods");
//                }
//                packageParams.put("trade_type", "APP");
//                String sign = PayCommonUtil.createSign("UTF-8", packageParams, Constants.wx_api_key);
//                packageParams.put("sign", sign);
//
//                String requestXML = PayCommonUtil.getRequestXml(packageParams);
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), requestXML);
//                Request request = new Request.Builder().url(url).post(requestBody).build();
//                try {
//                    Response response = AFHttpClient.getHttpClientSpecify().newCall(request).execute();
//                    String responseStr = response.body().string();
//                    subscriber.onNext(responseStr);
//                    subscriber.onCompleted();
//                } catch (IOException e) {
//                    ReportUtil.reportError(e);
//                    Logger.getLogger().e("请求支付失败:" + e.toString());
//                    ToastUtil.show(PayActivity.this, "支付失败，请稍后重试！");
//                }
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Logger.getLogger().e("支付失败：" + throwable.getMessage());
//                        ReportUtil.reportError(throwable);
//                        ToastUtil.show(PayActivity.this, "支付失败，请稍后重试！");
//                    }
//
//                    @Override
//                    public void onNext(String json) {
//                        Logger.getLogger().d("支付返回结果:" + json);
//                        HashMap<String, String> map = doXMLParse(json);
//                        if (map == null) {
//                            ReportUtil.reportError("支付失败，map == null");
//                            ToastUtil.show(PayActivity.this, "支付失败，请稍后重试！");
//                            return;
//                        }
//
//                        if (map.get("return_code").equalsIgnoreCase("FAIL")) {
//                            Logger.getLogger().e("支付失败：" + map.get("return_msg"));
//                            ReportUtil.reportError("支付失败：" + map.get("return_msg"));
//                            ToastUtil.show(PayActivity.this, "支付失败，请稍后重试！");
//                            return;
//                        }
//
//                        if (map.get("result_code").equalsIgnoreCase("FAIL")) {
//                            Logger.getLogger().e("支付失败：" + map.get("err_code") + "..." + map.get("err_code_des"));
//                            ReportUtil.reportError("支付失败：" + map.get("err_code") + "..." + map.get("err_code_des"));
//                            ToastUtil.show(PayActivity.this, "支付失败，请稍后重试！");
//                            return;
//                        }
//
//                        //开始支付
//                        String prepayID = map.get("prepay_id");
//
//                        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
//                        String currTime = PayCommonUtil.getCurrTime();
//                        String strTime = currTime.substring(8, currTime.length());
//                        String strRandom = PayCommonUtil.buildRandom(4) + "";
//                        String nonce_str = strTime + strRandom;
//                        packageParams.put("appid", Constants.wx_app_id);// 公众账号ID
//                        packageParams.put("partnerid",Constants.wx_mch_id);// 商户号
//                        packageParams.put("prepayid", prepayID);
//                        packageParams.put("noncestr", nonce_str);
//                        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
//                        packageParams.put("timestamp", timeStamp);
//                        packageParams.put("package", "Sign=WXPay");
//
//                        String sign = PayCommonUtil.createSign("UTF-8", packageParams, Constants.wx_api_key);
//                        packageParams.put("sign", sign);
//
//                        final PayReq request = new PayReq();
//                        request.appId = Constants.wx_app_id;
//                        request.partnerId = Constants.wx_mch_id;
//                        request.prepayId = prepayID;
//                        request.packageValue = "Sign=WXPay";
//                        request.nonceStr = nonce_str;
//                        request.timeStamp = timeStamp;
//                        request.sign = sign;
//                        wxapi.sendReq(request);
//                    }
//                });
    }
}




