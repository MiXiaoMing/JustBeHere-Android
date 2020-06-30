package com.community.customer.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.LoginActivity;
import com.community.customer.api.user.AddOrderEntity;
import com.community.customer.api.user.AddressEntity;
import com.community.customer.api.user.GoodsOrderConfirm;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.common.Constants;
import com.community.customer.common.ServerConfig;
import com.community.customer.event.PayEvent;
import com.community.customer.mine.AddressListActivity;
import com.community.customer.mine.PayActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.FontTextView;
import com.community.support.component.RoundCornerImageView;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.UserInfoUtil;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GoodsOrderConfirmActivity extends AutoBaseTitleActivity {
    private UserDataManager dataManager = new UserDataManager();

    private GoodsOrderConfirm goodsConfirm;

    private TextView tvRemind;
    private RelativeLayout rlyAddressNone, rlyAddress;
    private TextView tvContact, tvAddress, tvCellphone, tvOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_order_confirm);

        Intent intent = getIntent();
        goodsConfirm = (GoodsOrderConfirm) intent.getSerializableExtra("goodsConfirm");

        if (goodsConfirm == null || goodsConfirm.items == null || goodsConfirm.items.size() < 1) {
            ReportUtil.reportError("商品确认页面未收到数据, goodsConfirm == null || goodsConfirm.items == null || goodsConfirm.items.size() < 1");
            Logger.getLogger().e("商品确认页面未收到数据, goodsConfirm == null || goodsConfirm.items == null || goodsConfirm.items.size() < 1");
            finish();
            return;
        }

        initView();
        getAddressList();

        EventBus.getDefault().register(this);
    }

    private void initView() {
        LinearLayout llyBack = findViewById(R.id.llyBack);
        llyBack.setOnClickListener(clickListener);

        rlyAddressNone = findViewById(R.id.rlyAddressNone);
        rlyAddressNone.setOnClickListener(clickListener);

        rlyAddress = findViewById(R.id.rlyAddress);
        tvContact = findViewById(R.id.tvContact);
        tvAddress = findViewById(R.id.tvAddress);
        tvCellphone = findViewById(R.id.tvCellphone);
        rlyAddress.setOnClickListener(clickListener);

        LinearLayout llyItems = findViewById(R.id.llyItems);

        LinearLayout llyRemind = findViewById(R.id.llyRemind);
        tvRemind = findViewById(R.id.tvRemind);
        llyRemind.setOnClickListener(clickListener);

        TextView tvPrice = findViewById(R.id.tvPrice);
        tvPrice.setText("¥ " + goodsConfirm.price);

        tvOrder = findViewById(R.id.tvOrder);
        tvOrder.setBackgroundResource(R.color.gray);
        tvOrder.setEnabled(false);
        tvOrder.setOnClickListener(clickListener);

        initItems(llyItems);

        showOrderButton();
    }

    private void initItems(LinearLayout root) {
        for (int i = 0; i < goodsConfirm.items.size(); ++i) {
            GoodsOrderConfirm.Item item = goodsConfirm.items.get(i);
            if (item.number <= 0) {
                return;
            }
            if (i > 0) {
                View view = new View(this);
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(1)));
                root.addView(view);
            }

            LinearLayout linearLayout = new LinearLayout(this);
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setPadding(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentWidthSize(20), AutoUtils.getPercentWidthSize(15));
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setBackgroundColor(Color.WHITE);
                root.addView(linearLayout);
            }
            {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(65), AutoUtils.getPercentWidthSize(65));
                imageView.setLayoutParams(layoutParams);
                ImageLoader.normal(this, ServerConfig.file_host + item.icon, R.drawable.default_image_white, imageView);
                linearLayout.addView(imageView);
            }
            LinearLayout linearLayout1 = new LinearLayout(this);
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(AutoUtils.getPercentWidthSize(12), 0, 0, 0);
                linearLayout1.setLayoutParams(layoutParams);
                linearLayout1.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(linearLayout1);
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText(item.title);
                textView.setTextColor(Color.parseColor("#343434"));
                textView.setTextSize(14);
                linearLayout1.addView(textView);
            }
            {
                if (!TextUtils.isEmpty(item.typeName)) {
                    TextView textView = new FontTextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(item.typeName);
                    textView.setTextColor(Color.parseColor("#646464"));
                    textView.setTextSize(12);
                    linearLayout1.addView(textView);
                }
            }
            LinearLayout linearLayout2 = new LinearLayout(this);
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(30));
                layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(15), 0, 0);
                linearLayout2.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                linearLayout2.setLayoutParams(layoutParams);
                linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout1.addView(linearLayout2);
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText("" + item.typePrice);
                textView.setTextColor(Color.parseColor("#ff510b"));
                textView.setTextSize(14);
                linearLayout2.addView(textView);
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText(" x" + item.number);
                textView.setTextColor(getResources().getColor(R.color.gray_text));
                textView.setTextSize(12);
                linearLayout2.addView(textView);
            }
        }
    }

    private void showOrderButton() {
        if (goodsConfirm.price > 0 && rlyAddress.getVisibility() == View.VISIBLE) {
            tvOrder.setBackgroundColor(Color.parseColor("#02bdac"));
            tvOrder.setEnabled(true);
        } else {
            tvOrder.setBackgroundResource(R.color.gray);
            tvOrder.setEnabled(false);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.rlyAddressNone: {
                    if (!UserInfoUtil.isLogin()) {
                        startActivity(new Intent(GoodsOrderConfirmActivity.this, LoginActivity.class));
                    } else {
                        Intent intent = new Intent(GoodsOrderConfirmActivity.this, AddressListActivity.class);
                        intent.putExtra("type", "edit");
                        intent.putExtra("id", "");
                        startActivityForResult(intent, Constants.req_code_address);
                    }
                }

                break;
                case R.id.rlyAddress: {
                    Intent intent = new Intent(GoodsOrderConfirmActivity.this, AddressListActivity.class);
                    intent.putExtra("type", "edit");
                    intent.putExtra("id", goodsConfirm.addressid);
                    startActivityForResult(intent, Constants.req_code_address);
                }
                break;

                case R.id.llyRemind: {
                    Intent intent = new Intent(GoodsOrderConfirmActivity.this, RemindActivity.class);
                    String content = tvRemind.getText().toString().trim();
                    intent.putExtra("content", content);
                    startActivityForResult(intent, Constants.req_order_remind);
                }
                break;

                case R.id.tvOrder:
                    confirm();
                    break;
            }
        }
    };

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(PayEvent event) {
        switch (event.getEvent()) {
            case START_PAY:
                finish();
                break;
        }
    }

    private void confirm() {
        Logger.getLogger().d("商品下订单");
        goodsConfirm.remind = tvRemind.getText().toString().trim();

        dataManager.addGoodsOrder(goodsConfirm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddOrderEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("商品下订单：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddOrderEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("商品下订单，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("商品下订单, result为空");
                                return;
                            }

                            Intent intent = new Intent(GoodsOrderConfirmActivity.this, PayActivity.class);
                            intent.putExtra("pay", result.data.payPrice);
                            intent.putExtra("orderID", result.data.id);
                            intent.putExtra("tradeID", result.data.tradeID);
                            intent.putExtra("orderType", Constants.order_type_goods);
                            startActivity(intent);
                        }
                    }
                });
    }


    private void getAddressList() {
        Logger.getLogger().d("获取服务地址列表");
        UserDataManager dataManager = new UserDataManager();

        dataManager.getAddressList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取服务地址列表：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddressEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取服务地址列表，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取服务地址列表, result为空");
                                return;
                            }

                            ArrayList<AddressEntity> addressArrayList = result.data;
                            if (addressArrayList.size() > 0) {
                                goodsConfirm.addressid = addressArrayList.get(0).id;
                                goodsConfirm.contact = addressArrayList.get(0).contact;
                                goodsConfirm.region = addressArrayList.get(0).region;
                                goodsConfirm.cellphone = addressArrayList.get(0).phoneNumber;

                                tvContact.setText(addressArrayList.get(0).contact);
                                tvAddress.setText(addressArrayList.get(0).region);
                                tvCellphone.setText(addressArrayList.get(0).phoneNumber);

                                rlyAddressNone.setVisibility(View.GONE);
                                rlyAddress.setVisibility(View.VISIBLE);

                                showOrderButton();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.req_order_remind:
                    if (null != data) {
                        String remind = data.getStringExtra("remind");
                        if (TextUtils.isEmpty(remind)) {
                            tvRemind.setVisibility(View.GONE);
                            tvRemind.setText("");
                        } else {
                            tvRemind.setVisibility(View.VISIBLE);
                            tvRemind.setText(remind);
                        }
                    }
                    break;
                case Constants.req_code_address:
                    if (null != data) {
                        AddressEntity address = (AddressEntity) data.getSerializableExtra("address");
                        if (address != null) {
                            goodsConfirm.addressid = address.id;
                            goodsConfirm.contact = address.contact;
                            goodsConfirm.region = address.region;
                            goodsConfirm.cellphone = address.phoneNumber;

                            tvContact.setText(address.contact);
                            tvAddress.setText(address.region);
                            tvCellphone.setText(address.phoneNumber);

                            rlyAddressNone.setVisibility(View.GONE);
                            rlyAddress.setVisibility(View.VISIBLE);
                        } else {
                            rlyAddressNone.setVisibility(View.VISIBLE);
                            rlyAddress.setVisibility(View.GONE);
                        }
                        showOrderButton();
                    }
                    break;
            }
        }
    }
}
