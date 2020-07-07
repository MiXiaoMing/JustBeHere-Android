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

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.user.GoodsOrderDetailEntity;
import com.community.customer.api.user.GoodsOrderEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.api.user.input.OrderBody;
import com.community.customer.common.Constants;
import com.community.customer.common.ServerConfig;
import com.community.customer.mine.PayActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.FontTextView;
import com.community.support.component.RoundCornerImageView;
import com.community.support.utils.ReportUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GoodsOrderDetailActivity extends AutoBaseTitleActivity {
    private TextView tvTime;
    private String orderID, tradeID;
    private float price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_order_detail);

        orderID = getIntent().getStringExtra("orderID");

        orderDetail();
    }

    private void initView(GoodsOrderDetailEntity order) {
        if (order == null) {
            finish();
            return;
        }
        LinearLayout llyBack = findViewById(R.id.llyBack);

        TextView tvStatus = findViewById(R.id.tvStatus);

        TextView tvContact = findViewById(R.id.tvContact);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvCellphone = findViewById(R.id.tvCellphone);

        LinearLayout llyCancel = findViewById(R.id.llyCancel);
        TextView tvUpdateTime = findViewById(R.id.tvUpdateTime);
        TextView tvCancelMessage = findViewById(R.id.tvCancelMessage);

        TextView tvItemCount = findViewById(R.id.tvItemCount);
        TextView tvPrice = findViewById(R.id.tvPrice);
        LinearLayout llyItems = findViewById(R.id.llyItems);

        LinearLayout llyRemind = findViewById(R.id.llyRemind);
        TextView tvRemind = findViewById(R.id.tvRemind);

        TextView tvOrderID = findViewById(R.id.tvOrderID);
        TextView tvCreateTime = findViewById(R.id.tvCreateTime);

        LinearLayout llyBottom = findViewById(R.id.llyBottom);
        tvTime = findViewById(R.id.tvTime);
        TextView tvCancel = findViewById(R.id.tvCancel);
        TextView tvPay = findViewById(R.id.tvPay);

        llyBack.setOnClickListener(clickListener);
        tvCancel.setOnClickListener(clickListener);
        tvPay.setOnClickListener(clickListener);

        tvStatus.setText(Constants.convertServerOrderStatus(order.order.status));
        tvStatus.setTextColor(Constants.getServerStatusColor(order.order.status));
        tvContact.setText(order.deliveryAddress.contact);
        tvAddress.setText(order.deliveryAddress.region);
        tvCellphone.setText(order.deliveryAddress.phoneNumber);
        if (order.order.status.equals("05")) {
            llyCancel.setVisibility(View.VISIBLE);
            tvUpdateTime.setText(order.order.updateTime);
            if (TextUtils.isEmpty(order.order.content)) {
                tvCancelMessage.setVisibility(View.GONE);
            } else {
                tvCancelMessage.setVisibility(View.VISIBLE);
                tvCancelMessage.setText(order.order.content);
            }
        } else {
            llyCancel.setVisibility(View.GONE);
        }
        tvItemCount.setText("共" + order.items.size() + "件商品，共计");
        tvPrice.setText(order.goodsOrder.price + "");
        initItems(llyItems, order.items);
        if (TextUtils.isEmpty(order.goodsOrder.remind)) {
            llyRemind.setVisibility(View.GONE);
        } else {
            llyRemind.setVisibility(View.VISIBLE);
            tvRemind.setText(order.goodsOrder.remind);
        }

        tvOrderID.setText(order.goodsOrder.id);
        tvCreateTime.setText(order.order.createTime);

        price = order.goodsOrder.price;
        tradeID = order.order.tradeID;

        if (order.order.status.equals("01")) {
            llyBottom.setVisibility(View.VISIBLE);

            Logger.getLogger().d("倒计时：" + order.order.remainTime);

            new CountDownTimer(order.order.remainTime, 1000) {
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
            llyBottom.setVisibility(View.GONE);
        }
    }

    private void initItems(LinearLayout root, ArrayList<GoodsOrderDetailEntity.Item> items) {
        for (int i = 0; i < items.size(); ++i) {
            GoodsOrderDetailEntity.Item item = items.get(i);
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
                linearLayout.setPadding(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentWidthSize(15));
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setBackgroundColor(Color.WHITE);
                root.addView(linearLayout);
            }
            {
                RoundCornerImageView imageView = new RoundCornerImageView(this, AutoUtils.getPercentWidthSize(10));
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
                textView.setText("¥ " + item.typePrice);
                textView.setTextColor(Color.parseColor("#ff510b"));
                textView.setTextSize(14);
                linearLayout2.addView(textView);
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText(" x " + item.number);
                textView.setTextColor(getResources().getColor(R.color.gray_text));
                textView.setTextSize(12);
                linearLayout2.addView(textView);
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
                    Intent intent = new Intent(GoodsOrderDetailActivity.this, PayActivity.class);
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
        dataManager.getGoodsOrderDetail(orderID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<GoodsOrderDetailEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(GoodsOrderDetailEntity result) {
                        initView(result.data);
                    }
                });
    }

    private void cancelOrder(String content) {
        Logger.getLogger().d("取消订单，更新状态：" + content);

        OrderBody body = new OrderBody();
        body.id = orderID;
        body.status = "05";
        body.content = content;

        new UserDataManager().changeGoodsOrderStatus(body)
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
