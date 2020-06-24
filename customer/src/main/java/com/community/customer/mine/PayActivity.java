package com.community.customer.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.LoginActivity;
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.user.FloatEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.common.Constants;
import com.community.customer.event.PayEvent;
import com.community.customer.order.GoodsOrderDetailActivity;
import com.community.customer.order.ServerOrderDetailActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.community.support.utils.UserInfoUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PayActivity extends AutoBaseTitleActivity {
    private float price;
    private String orderID, tradeID, orderType; // server：服务订单，goods:商品订单
    private int payType = 1; // 0: 余额支付  1：微信支付

    private TextView tvTime, tvBalance;
    private ImageView ivMemberSelect, ivWXSelect;

    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        price = getIntent().getFloatExtra("pay", 0);
        orderID = getIntent().getStringExtra("orderID");
        tradeID = getIntent().getStringExtra("tradeID");
        orderType = getIntent().getStringExtra("orderType");

        // TODO: 2020/6/23 不应该注释掉
//        if (price == 0) {
//            ReportUtil.reportError("支付金额为 0");
//            finish();
//            return;
//        }
        if (TextUtils.isEmpty(orderID) || TextUtils.isEmpty(tradeID) || TextUtils.isEmpty(orderType)) {
            ReportUtil.reportError("orderID || tradeID || orderType为空");
            finish();
            return;
        }

        //微信初始化
        {
            wxapi = WXAPIFactory.createWXAPI(this, Constants.wx_app_id, true);
            wxapi.registerApp(Constants.wx_app_id);
        }

        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new PayEvent(PayEvent.Event.START_PAY));

        initView();
        countDownTimer.start();
    }

    private void initView() {
        RelativeLayout llyBack = findViewById(R.id.llyBack);

        TextView tvPayPrice = findViewById(R.id.tvPayPrice);
        tvTime = findViewById(R.id.tvTime);

        LinearLayout llyMember = findViewById(R.id.llyMember);
        tvBalance = findViewById(R.id.tvBalance);
        LinearLayout llyDeposit = findViewById(R.id.llyDeposit);
        LinearLayout llyWX = findViewById(R.id.llyWX);
        ivMemberSelect = findViewById(R.id.ivMemberSelect);
        ivWXSelect = findViewById(R.id.ivWXSelect);

        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvPay = findViewById(R.id.tvPay);

        tvPayPrice.setText(price + "元");
        tvPrice.setText("¥ " + price);
        tvBalance.setText("余额：" + UserInfoUtil.getBalance() + "元");

        llyBack.setOnClickListener(clickListener);
        llyDeposit.setOnClickListener(clickListener);
        tvPay.setOnClickListener(clickListener);
        llyMember.setOnClickListener(clickListener);
        llyWX.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvBalance.setText("余额：" + UserInfoUtil.getBalance() + "元");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxapi != null) {
            wxapi.detach();
            wxapi = null;
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    showSureDialog();
                    break;

                case R.id.llyDeposit:
                    if (!UserInfoUtil.isLogin()) {
                        startActivity(new Intent(PayActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(PayActivity.this, DepositActivity.class));
                    }
                    break;

                case R.id.tvPay:
                    if (payType == 1) {
                        unifiedOrder();
                    } else {
                        balancePay();
                    }
                    break;

                case R.id.llyMember:
                    payType = 0;
                    ivMemberSelect.setImageResource(R.drawable.icon_selected);
                    ivWXSelect.setImageResource(R.drawable.icon_unselected);
                    break;

                case R.id.llyWX:
                    payType = 1;
                    ivMemberSelect.setImageResource(R.drawable.icon_unselected);
                    ivWXSelect.setImageResource(R.drawable.icon_selected);
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showSureDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private CountDownTimer countDownTimer = new CountDownTimer(15 * 60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished > 60 * 1000) {
                tvTime.setText(millisUntilFinished / 1000 / 60 + "分" + millisUntilFinished / 1000 % 60 + "秒");
            } else {
                tvTime.setText(millisUntilFinished / 1000 + "秒");
            }
        }

        @Override
        public void onFinish() {
            tvTime.setText("支付超时");

            changeOrderCancel();
        }
    };

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(PayEvent event) {
        switch (event.getEvent()) {
            case WX_PAY_SUCCESS:
                Logger.getLogger().d("支付成功");
                startOrderDetail();
                break;

            case WX_PAY_FAIL:
                startOrderDetail();
                break;

            case WX_PAY_CANCEL:
                startOrderDetail();
                break;
        }
    }

    private AlertDialog alertDialog;

    private void showSureDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付提示：");
        builder.setMessage("当前订单还未完成支付，是否确认退出？");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startOrderDetail();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void startOrderDetail() {
        if (orderType.equals(Constants.order_type_service)) {
            Intent intent = new Intent(this, ServerOrderDetailActivity.class);
            intent.putExtra("orderID", orderID);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, GoodsOrderDetailActivity.class);
            intent.putExtra("orderID", orderID);
            startActivity(intent);
        }

        finish();
    }

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

        // TODO: 2020/5/22 不应该注释
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

    private HashMap<String, String> doXMLParse(String strxml) {
        HashMap<String, String> list = new HashMap<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputStream inputStream = new ByteArrayInputStream(strxml.getBytes());

            org.w3c.dom.Document document2 = db.parse(inputStream);

            DOMReader domReader = new DOMReader();
            Document document3 = domReader.read(document2);
            Element root = document3.getRootElement();

            Iterator<Element> elementIterator = root.elementIterator();
            while (elementIterator.hasNext()) {
                Element element = elementIterator.next();
                list.put(element.getName(), element.getTextTrim());
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            Logger.getLogger().e("解析微信返回值失败：" + e.toString());
            return null;
        }

        return list;
    }

    private void balancePay() {
        float balance = UserInfoUtil.getBalance();
        if (balance < price) {
            ToastUtil.show(this, "余额不足，请充值");
            return;
        }
        Logger.getLogger().d("余额支付");

        UserDataManager dataManager = new UserDataManager();
        dataManager.balancePay(orderID, orderType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FloatEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("余额支付：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FloatEntity result) {
                        if (!result.success) {
                            ReportUtil.reportError("余额支付，msgCode：" + result.errCode + "/n" + result.message);
                            Logger.getLogger().e("余额支付，msgCode：" + result.errCode + "/n" + result.message);
                            if (result.data != null) {
                                UserInfoUtil.setBalance(result.data.getBalance());
                            }
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("余额支付, result为空");
                                return;
                            }
                            UserInfoUtil.setBalance(result.data.getBalance());
                            startOrderDetail();
                        }
                    }
                });
    }

    private void changeOrderCancel() {
        Logger.getLogger().d("支付超时，更新状态");

        UserDataManager dataManager = new UserDataManager();
        dataManager.changeServerOrderStatus(orderID, "05", "客户原因(15分钟超时未支付，系统自动取消)")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmptyEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("支付超时：" + e.getMessage());
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
                            ReportUtil.reportError("支付超时，更新状态，msgCode：" + result.errCode + "/n" + result.message);
                            Logger.getLogger().e("支付超时，更新状态，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            startOrderDetail();
                        }
                    }
                });
    }
}
