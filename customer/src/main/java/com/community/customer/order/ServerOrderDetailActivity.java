package com.community.customer.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.servers.ServerPrice;
import com.community.customer.api.user.input.ServiceOrderBody;
import com.community.customer.api.user.result.ServerOrderEntity;
import com.community.customer.api.user.result.ServerOrderListEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.common.Constants;
import com.community.customer.mine.PayActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.FontTextView;
import com.community.support.utils.ReportUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ServerOrderDetailActivity extends AutoBaseTitleActivity {
    private TextView tvTime;
    private String orderID, tradeID;
    private float price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_order_detail);

        orderID = getIntent().getStringExtra("orderID");

        orderDetail();
    }

    private void initView(ServerOrderEntity order) {
        if (order == null) {
            finish();
            return;
        }
        LinearLayout llyBack = findViewById(R.id.llyBack);

        TextView tvServerName = findViewById(R.id.tvServerName);
        TextView tvStatus = findViewById(R.id.tvStatus);

        TextView tvContact = findViewById(R.id.tvContact);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvCellphone = findViewById(R.id.tvCellphone);

        TextView tvServerTime = findViewById(R.id.tvServerTime);

        LinearLayout llyCancel = findViewById(R.id.llyCancel);
        TextView tvUpdateTime = findViewById(R.id.tvUpdateTime);
        TextView tvCancelMessage = findViewById(R.id.tvCancelMessage);

        LinearLayout llyItems = findViewById(R.id.llyItems);

        LinearLayout llyRemind = findViewById(R.id.llyRemind);
        TextView tvRemind = findViewById(R.id.tvRemind);

        TextView tvTotal = findViewById(R.id.tvTotal);
        TextView tvDiscount = findViewById(R.id.tvDiscount);
        TextView tvPayPrice = findViewById(R.id.tvPayPrice);

        TextView tvOrderID = findViewById(R.id.tvOrderID);
        TextView tvCreateTime = findViewById(R.id.tvCreateTime);

        LinearLayout llyBottom = findViewById(R.id.llyBottom);
        tvTime = findViewById(R.id.tvTime);
        TextView tvCancel = findViewById(R.id.tvCancel);
        TextView tvPay = findViewById(R.id.tvPay);

        llyBack.setOnClickListener(clickListener);
        tvCancel.setOnClickListener(clickListener);
        tvPay.setOnClickListener(clickListener);

        tvServerName.setText(order.serviceOrder.serviceName);
        tvStatus.setText(Constants.convertServerOrderStatus(order.serviceOrder.status));
        tvStatus.setTextColor(Constants.getServerStatusColor(order.serviceOrder.status));
        tvContact.setText(order.deliveryAddress.contact);
        tvAddress.setText(order.deliveryAddress.region);
        tvCellphone.setText(order.deliveryAddress.phoneNumber);
        tvServerTime.setText(order.serviceOrder.serviceTime);
        if (order.serviceOrder.status.equals("05")) {
            llyCancel.setVisibility(View.VISIBLE);
            tvUpdateTime.setText(order.serviceOrder.updateTime);
            if (TextUtils.isEmpty(order.serviceOrder.content)) {
                tvCancelMessage.setVisibility(View.GONE);
            } else {
                tvCancelMessage.setVisibility(View.VISIBLE);
                tvCancelMessage.setText(order.serviceOrder.content);
            }
        } else {
            llyCancel.setVisibility(View.GONE);
        }
        initItems(llyItems, order.prices);
        if (TextUtils.isEmpty(order.serviceOrder.remind)) {
            llyRemind.setVisibility(View.GONE);
        } else {
            llyRemind.setVisibility(View.VISIBLE);
            tvRemind.setText(order.serviceOrder.remind);
        }
        tvTotal.setText("    总金额：" + order.serviceOrder.totalPrice + "元");
        if (order.serviceOrder.discountPrice != 0) {
            tvDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText("会员已优惠：" + order.serviceOrder.discountPrice + "元");
        } else {
            tvDiscount.setVisibility(View.GONE);
        }

        tvOrderID.setText(order.serviceOrder.id);
        tvCreateTime.setText(order.serviceOrder.createTime);

        price = order.serviceOrder.payPrice;
        tradeID = order.serviceOrder.tradeID;

        if (order.serviceOrder.status.equals("01")) {
            llyBottom.setVisibility(View.VISIBLE);
            tvPayPrice.setText("  还需付额：" + order.serviceOrder.payPrice + "元");

            Logger.getLogger().d("倒计时：" + order.serviceOrder.remainTime);

            new CountDownTimer(order.serviceOrder.remainTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished > 60 * 1000) {
                        tvTime.setText(millisUntilFinished / 1000 / 60 + "分" + millisUntilFinished / 1000 % 60 + "秒内完成支付");
                    } else {
                        tvTime.setText(millisUntilFinished / 1000 + "秒内完成支付");
                    }
                }

                @Override
                public void onFinish() {
                    tvTime.setText("支付超时");
                    cancelOrder("客户原因（15分钟超时未支付，系统自动取消）");
                }
            }.start();
        } else {
            if (order.serviceOrder.status.equals("05")) {
                llyBottom.setVisibility(View.GONE);
                tvPayPrice.setText("  未付金额：" + order.serviceOrder.payPrice + "元");
            } else {
                llyBottom.setVisibility(View.GONE);
                tvPayPrice.setText("  已付金额：" + order.serviceOrder.payPrice + "元");
            }
        }
    }

    private void initItems(LinearLayout root, ArrayList<ServerPrice> prices) {
        root.removeAllViews();

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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvCancel:
                    showSureDialog();
                    break;

                case R.id.tvPay:
                    Intent intent = new Intent(ServerOrderDetailActivity.this, PayActivity.class);
                    intent.putExtra("pay", price);
                    intent.putExtra("orderID", orderID);
                    intent.putExtra("tradeID", tradeID);
                    intent.putExtra("orderType", Constants.order_type_service);
                    startActivity(intent);

                    finish();
                    break;
            }
        }
    };

    private AlertDialog alertDialog;

    private void showSureDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("订单提示：");
        builder.setMessage("当前订单还未完成支付，是否确认取消？");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelOrder("客户原因(客户手动操作)");
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

    private void orderDetail() {
        Logger.getLogger().d("订单详情, orderID -> " + orderID);

        UserDataManager dataManager = new UserDataManager();
        dataManager.getServerOrderDetail(RequestBody.create(MediaType.parse("text/plain"), orderID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<ServerOrderEntity>() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(ServerOrderEntity result) {
                        ServerOrderEntity serverOrder = result.data;
                        initView(serverOrder);
                    }
                });
    }

    private void cancelOrder(String content) {
        Logger.getLogger().d("取消订单，更新状态：" + content);

        ServiceOrderBody body = new ServiceOrderBody();
        body.id = orderID;
        body.status = "05";
        body.content = content;

        new UserDataManager().changeServerOrderStatus(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmptyEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("取消订单：" + e.getMessage());
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
                            ReportUtil.reportError("取消订单，更新状态，msgCode：" + result.errCode + "/n" + result.message);
                            Logger.getLogger().e("取消订单，更新状态，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            orderDetail();
                        }
                    }
                });
    }
}
