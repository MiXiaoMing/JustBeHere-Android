package com.community.customer.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.library.network.http.AFHttpClient;
import com.appframe.utils.logger.Logger;
import com.community.customer.common.ServerConfig;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.common.Constants;
import com.community.support.utils.DeviceInfo;
import com.community.support.utils.PayCommonUtil;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.community.support.utils.UserInfoUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DepositActivity extends AutoBaseTitleActivity {

    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        //微信初始化
        {
            wxapi = WXAPIFactory.createWXAPI(this, Constants.wx_app_id, true);
            wxapi.registerApp(Constants.wx_app_id);
        }

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
        final String body, detail;
        final String payFee = String.valueOf(200);
        body = "会员充值";
        detail = "200";
        final String tradeID = String.valueOf(System.currentTimeMillis());

        Observable.create(new ObservableOnSubscribe<String>() {

            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

                SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
                String currTime = PayCommonUtil.getCurrTime();
                String strTime = currTime.substring(8, currTime.length());
                String strRandom = PayCommonUtil.buildRandom(4) + "";
                String nonce_str = strTime + strRandom;
                packageParams.put("appid", Constants.wx_app_id);// 公众账号ID
                packageParams.put("mch_id", Constants.wx_mch_id);// 商户号
                packageParams.put("nonce_str", nonce_str);// 随机字符串
                packageParams.put("body", body); //商品描述
                packageParams.put("detail", detail);
                packageParams.put("out_trade_no", tradeID); //商户订单号
                packageParams.put("total_fee", String.valueOf((int) (Float.valueOf(payFee) * 100)));
                packageParams.put("spbill_create_ip", DeviceInfo.getIp());
                packageParams.put("notify_url", ServerConfig.host + "/user/wxpay/wxPayNotify/deposite");
                packageParams.put("trade_type", "APP");
                String sign = PayCommonUtil.createSign("UTF-8", packageParams, Constants.wx_api_key);
                packageParams.put("sign", sign);

                String requestXML = PayCommonUtil.getRequestXml(packageParams);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), requestXML);
                Request request = new Request.Builder().url(url).post(requestBody).build();
                try {
                    Response response = AFHttpClient.getInstance().newCall(request).execute();
                    String responseStr = response.body().string();
                    emitter.onNext(responseStr);
                    emitter.onComplete();
                } catch (IOException e) {
                    ReportUtil.reportError(e);
                    Logger.getLogger().e("请求支付失败:" + e.toString());
                    ToastUtil.show(DepositActivity.this, "支付失败，请稍后重试！");
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {

            public void onSubscribe(Disposable d) {
            }

            public void onNext(String responseStr) {
                Logger.getLogger().d("支付返回结果:" + responseStr);
                HashMap<String, String> map = doXMLParse(responseStr);
                if (map == null) {
                    ReportUtil.reportError("支付失败，map == null");
                    ToastUtil.show(DepositActivity.this, "支付失败，请稍后重试！");
                    return;
                }

                if (map.get("return_code").equalsIgnoreCase("FAIL")) {
                    Logger.getLogger().e("支付失败：" + map.get("return_msg"));
                    ReportUtil.reportError("支付失败：" + map.get("return_msg"));
                    ToastUtil.show(DepositActivity.this, "支付失败，请稍后重试！");
                    return;
                }

                if (map.get("result_code").equalsIgnoreCase("FAIL")) {
                    Logger.getLogger().e("支付失败：" + map.get("err_code") + "..." + map.get("err_code_des"));
                    ReportUtil.reportError("支付失败：" + map.get("err_code") + "..." + map.get("err_code_des"));
                    ToastUtil.show(DepositActivity.this, "支付失败，请稍后重试！");
                    return;
                }

                //开始支付
                String prepayID = map.get("prepay_id");

                SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
                String currTime = PayCommonUtil.getCurrTime();
                String strTime = currTime.substring(8, currTime.length());
                String strRandom = PayCommonUtil.buildRandom(4) + "";
                String nonce_str = strTime + strRandom;
                packageParams.put("appid", Constants.wx_app_id);// 公众账号ID
                packageParams.put("partnerid", Constants.wx_mch_id);// 商户号
                packageParams.put("prepayid", prepayID);
                packageParams.put("noncestr", nonce_str);
                String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
                packageParams.put("timestamp", timeStamp);
                packageParams.put("package", "Sign=WXPay");

                String sign = PayCommonUtil.createSign("UTF-8", packageParams, Constants.wx_api_key);
                packageParams.put("sign", sign);

                final PayReq request = new PayReq();
                request.appId = Constants.wx_app_id;
                request.partnerId = Constants.wx_mch_id;
                request.prepayId = prepayID;
                request.packageValue = "Sign=WXPay";
                request.nonceStr = nonce_str;
                request.timeStamp = timeStamp;
                request.sign = sign;
                wxapi.sendReq(request);
            }

            public void onError(Throwable e) {

            }

            public void onComplete() {
            }
        });
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
}




