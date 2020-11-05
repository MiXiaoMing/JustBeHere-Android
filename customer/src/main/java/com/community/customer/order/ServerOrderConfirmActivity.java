package com.community.customer.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.LoginActivity;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.servers.ServerPrice;
import com.community.customer.api.user.AddOrderEntity;
import com.community.customer.api.user.ServerOrderConfirm;
import com.community.customer.api.user.UserDataManager;
import com.community.support.common.Constants;
import com.community.customer.event.PayEvent;
import com.community.customer.mine.DepositActivity;
import com.community.customer.mine.PayActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.FontTextView;
import com.community.support.utils.TimeUtils;
import com.community.support.utils.UserInfoUtil;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import cn.wdcloud.acaeva.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ServerOrderConfirmActivity extends AutoBaseTitleActivity {
    private UserDataManager dataManager = new UserDataManager();

    private ServerOrderConfirm orderConfirm;
    private List<ServerPrice> prices;

    private TextView tvRemind, tvBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_order_confirm);

        Intent intent = getIntent();
        orderConfirm = (ServerOrderConfirm) intent.getSerializableExtra("orderConfirm");
        prices = (List<ServerPrice>) intent.getSerializableExtra("price");

        initView();

        EventBus.getDefault().register(this);
    }

    private void initView() {
        LinearLayout llyBack = findViewById(R.id.llyBack);

        TextView tvContact = findViewById(R.id.tvContact);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvCellphone = findViewById(R.id.tvCellphone);

        TextView tvServerTime = findViewById(R.id.tvServerTime);

        TextView tvServerName = findViewById(R.id.tvServerName);
        LinearLayout llyItems = findViewById(R.id.llyItems);

        LinearLayout llyRemind = findViewById(R.id.llyRemind);
        tvRemind = findViewById(R.id.tvRemind);

        TextView tvTotal = findViewById(R.id.tvTotal);
        TextView tvDiscount = findViewById(R.id.tvDiscount);
        TextView tvPay = findViewById(R.id.tvPay);

        LinearLayout llyDeposit = findViewById(R.id.llyDeposit);
        tvBalance = findViewById(R.id.tvBalance);

        TextView tvOrder = findViewById(R.id.tvOrder);


        tvContact.setText(orderConfirm.contact);
        tvAddress.setText(orderConfirm.region);
        tvCellphone.setText(orderConfirm.cellphone);
        tvServerTime.setText(TimeUtils.dateFormat(new Date(orderConfirm.serverTime)));
        tvServerName.setText(orderConfirm.serverName);
        tvTotal.setText("    总金额：" + orderConfirm.totalPrice + "元");
        tvDiscount.setText("会员可优惠：" + orderConfirm.discountPrice + "元");
        tvPay.setText("    实付额：" + orderConfirm.payPrice + "元");
        tvBalance.setText(UserInfoUtil.getBalance() + "元");

        llyBack.setOnClickListener(clickListener);
        llyRemind.setOnClickListener(clickListener);
        llyDeposit.setOnClickListener(clickListener);
        tvOrder.setOnClickListener(clickListener);

        initItems(llyItems);
    }

    private void initItems(LinearLayout root) {
        for (int i = 0; i < prices.size(); ++i) {
            if (prices.get(i).buyNumber <= 0) {
                return;
            }

            LinearLayout linearLayout = new LinearLayout(this);
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(32));
                layoutParams.setMargins(AutoUtils.getPercentWidthSize(15), 0, AutoUtils.getPercentWidthSize(15), 0);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setBackgroundColor(Color.WHITE);
                root.addView(linearLayout);
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(AutoUtils.getPercentWidthSize(20), 0, 0, 0);
                textView.setLayoutParams(layoutParams);
                textView.setText(prices.get(i).name);
                textView.setTextColor(Color.parseColor("#3a3a3a"));
                textView.setTextSize(13);
                linearLayout.addView(textView);
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText("(" + prices.get(i).price + "元/" + prices.get(i).unit + ")");
                textView.setTextColor(getResources().getColor(R.color.gray_text));
                textView.setTextSize(12);
                linearLayout.addView(textView);
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, AutoUtils.getPercentWidthSize(15), 0);
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.RIGHT);
                textView.setText(prices.get(i).buyNumber + prices.get(i).unit);
                textView.setTextColor(Color.parseColor("#3a3a3a"));
                textView.setTextSize(12);
                linearLayout.addView(textView);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvBalance.setText(UserInfoUtil.getBalance() + "元");
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.llyRemind:
                    Intent intent = new Intent(ServerOrderConfirmActivity.this, RemindActivity.class);
                    String content = tvRemind.getText().toString().trim();
                    intent.putExtra("content", content);
                    startActivityForResult(intent, Constants.req_order_remind);
                    break;

                case R.id.llyDeposit:
                    if (!UserInfoUtil.isLogin()) {
                        startActivity(new Intent(ServerOrderConfirmActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(ServerOrderConfirmActivity.this, DepositActivity.class));
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
        Logger.getLogger().d("下订单");
        orderConfirm.remind = tvRemind.getText().toString().trim();
        dataManager.addServerOrder(orderConfirm, prices)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<AddOrderEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(AddOrderEntity result) {
                        Intent intent = new Intent(ServerOrderConfirmActivity.this, PayActivity.class);
                        intent.putExtra("pay", result.data.payPrice);
                        intent.putExtra("orderID", result.data.id);
                        intent.putExtra("tradeID", result.data.tradeID);
                        intent.putExtra("orderType", Constants.order_type_service);
                        startActivity(intent);

                        finish();
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
            }
        }
    }
}
