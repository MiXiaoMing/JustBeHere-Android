package com.community.customer.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.community.customer.LoginActivity;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.servers.ServerPrice;
import com.community.customer.api.user.AddressEntity;
import com.community.customer.api.user.ServerOrderConfirm;
import com.community.customer.api.user.UserDataManager;
import com.community.support.common.Constants;
import com.community.customer.event.PayEvent;
import com.community.customer.mine.AddressListActivity;
import com.community.customer.mine.DepositActivity;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.FontTextView;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.UserInfoUtil;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.wdcloud.acaeva.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ServerOrderSelectActivity extends AutoBaseTitleActivity {
    public String code, name, addressID;
    public List<ServerPrice> prices;
    public LinearLayout llyPrice;
    public TextView tvTotal, tvReal, tvDiscount, tvBalance;
    RelativeLayout rlyAddressNone, rlyAddress;
    private TextView tvContact, tvAddress, tvCellphone, tvTime;

    private ServerOrderConfirm orderConfirm = new ServerOrderConfirm();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_order_select);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        name = intent.getStringExtra("name");
        prices = (List<ServerPrice>) intent.getSerializableExtra("price");

        initView();
        getAddressList();

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(PayEvent event) {
        switch (event.getEvent()) {
            case START_PAY:
                finish();
                break;
        }
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(name);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        llyBack.setOnClickListener(clickListener);

        rlyAddressNone = findViewById(R.id.rlyAddressNone);
        rlyAddressNone.setOnClickListener(clickListener);

        rlyAddress = findViewById(R.id.rlyAddress);
        tvContact = findViewById(R.id.tvContact);
        tvAddress = findViewById(R.id.tvAddress);
        tvCellphone = findViewById(R.id.tvCellphone);
        rlyAddress.setOnClickListener(clickListener);

        llyPrice = findViewById(R.id.llyPrice);
        tvTotal = findViewById(R.id.tvTotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvReal = findViewById(R.id.tvReal);
        tvBalance = findViewById(R.id.tvBalance);

        LinearLayout llyDeposit = findViewById(R.id.llyDeposit);
        llyDeposit.setOnClickListener(clickListener);

        tvTime = findViewById(R.id.tvTime);
        tvTime.setOnClickListener(clickListener);

        LinearLayout llyRoot = findViewById(R.id.llyRoot);

        for (int i = prices.size() - 1; i >= 0; --i) {
            final ServerPrice price = prices.get(i);
            RelativeLayout relativeLayout = new RelativeLayout(this);
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(70));
                if (i == 0) {
                    layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(10), 0, 0);
                } else {
                    layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(2), 0, 0);
                }
                relativeLayout.setLayoutParams(layoutParams);
                relativeLayout.setBackgroundColor(Color.WHITE);
                llyRoot.addView(relativeLayout, 1);
            }

            LinearLayout linearLayout = new LinearLayout(this);
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(AutoUtils.getPercentWidthSize(15), 0, 0, 0);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                relativeLayout.addView(linearLayout);
            }
            {
                TextView textView = new FontTextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(price.name);
                textView.setTextColor(Color.parseColor("#3a3a3a"));
                textView.setTextSize(14);
                linearLayout.addView(textView);
            }
            {
                TextView textView = new FontTextView(this);
                RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams3.setMargins(0, AutoUtils.getPercentHeightSize(5), 0, 0);
                textView.setLayoutParams(layoutParams3);
                String priceStr = "单价：" + price.price + "元/" + price.unit;
                if (price.minimum > 1) {
                    priceStr += "，" + price.minimum + price.unit + "起";
                }
                textView.setText(priceStr);
                textView.setTextColor(Color.parseColor("#6a6869"));
                textView.setTextSize(12);
                linearLayout.addView(textView);
            }
            {
                TextView textView3 = new FontTextView(this);
                RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams4.setMargins(0, AutoUtils.getPercentHeightSize(5), 0, 0);
                textView3.setLayoutParams(layoutParams4);
                textView3.setText("会员：" + price.price * price.discount + "元/" + price.unit);
                textView3.setTextColor(Color.parseColor("#da5826"));
                textView3.setTextSize(12);
                linearLayout.addView(textView3);
            }

            LinearLayout linearLayout2 = new LinearLayout(this);
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AutoUtils.getPercentWidthSize(100), AutoUtils.getPercentHeightSize(30));
                layoutParams.setMargins(0, 0, AutoUtils.getPercentWidthSize(15), 0);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                linearLayout2.setLayoutParams(layoutParams);
                linearLayout2.setBackgroundResource(R.drawable.shape_rectangle_corner_white);
                linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
                relativeLayout.addView(linearLayout2);
            }
            {
                RelativeLayout relativeLayout2 = new RelativeLayout(this);
                relativeLayout2.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(30), ViewGroup.LayoutParams.MATCH_PARENT));
                relativeLayout2.setGravity(Gravity.CENTER);
                relativeLayout2.setBackgroundResource(R.drawable.shape_rectangle_corner_white);
                linearLayout2.addView(relativeLayout2);

                View view = new View(this);
                view.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentHeightSize(2)));
                view.setBackgroundColor(this.getResources().getColor(R.color.gray));
                relativeLayout2.addView(view);

                final TextView textView = new FontTextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                textView.setGravity(Gravity.CENTER);
                textView.setText(String.valueOf(price.minimum));
                textView.setTextColor(Color.parseColor("#3d3b3c"));
                textView.setTextSize(15);
                linearLayout2.addView(textView);

                RelativeLayout relativeLayout3 = new RelativeLayout(this);
                relativeLayout3.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(30), ViewGroup.LayoutParams.MATCH_PARENT));
                relativeLayout3.setGravity(Gravity.CENTER);
                relativeLayout3.setBackgroundResource(R.drawable.shape_rectangle_corner_white);
                linearLayout2.addView(relativeLayout3);

                View view3 = new View(this);
                view3.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentHeightSize(2)));
                view3.setBackgroundColor(Color.parseColor("#02bcae"));
                relativeLayout3.addView(view3);

                View view2 = new View(this);
                view2.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentHeightSize(2)));
                view2.setBackgroundColor(Color.parseColor("#02bcae"));
                view2.setRotation(90);
                relativeLayout3.addView(view2);

                price.buyNumber = price.minimum;
                priceChange();

                relativeLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (price.buyNumber > 0 && price.buyNumber > price.minimum) {
                            price.buyNumber -= 1;
                            textView.setText(String.valueOf(price.buyNumber));
                        }
                        priceChange();
                    }
                });

                relativeLayout3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price.buyNumber += 1;
                        textView.setText(String.valueOf(price.buyNumber));
                        priceChange();
                    }
                });
            }
        }
    }

    private void priceChange() {
        float totalPrice = 0, discount = 0, real = 0;
        for (int i = 0; i < prices.size(); ++i) {
            totalPrice += Math.round(prices.get(i).price * prices.get(i).buyNumber * 100 / 100.00);
            discount += Math.round(prices.get(i).price * (1 - prices.get(i).discount) * prices.get(i).buyNumber * 100 / 100.00);
        }

        float balance = UserInfoUtil.getBalance();
        if (balance >= totalPrice) {
            real = totalPrice - discount;
        } else {
            real = totalPrice;
        }

        if (totalPrice > 0) {
            llyPrice.setVisibility(View.VISIBLE);
            tvTotal.setText("总金额：" + totalPrice + "元");
            tvDiscount.setText("会员可优惠：" + discount + "元");
            tvReal.setText("应付额：" + real + "元");
            tvBalance.setText("当前余额：" + balance + "元");

            orderConfirm.totalPrice = totalPrice;
            orderConfirm.discountPrice = discount;
            orderConfirm.payPrice = real;

            showSelectTime();
        } else {
            llyPrice.setVisibility(View.GONE);
            showSelectTime();
        }
    }

    private void showSelectTime() {
        if (llyPrice.getVisibility() == View.VISIBLE && rlyAddress.getVisibility() == View.VISIBLE) {
            tvTime.setBackgroundColor(Color.parseColor("#02bdac"));
            tvTime.setEnabled(true);
        } else {
            tvTime.setBackgroundResource(R.color.gray);
            tvTime.setEnabled(false);
        }
    }

    private void getAddressList() {
        UserDataManager dataManager = new UserDataManager();

        dataManager.getAddressList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<AddressEntity>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onError(String message) {
                        ReportUtil.reportError(message);
                    }

                    @Override
                    public void onSuccess(AddressEntity result) {
                        ArrayList<AddressEntity> addressArrayList = result.data;
                        if (addressArrayList.size() > 0) {
                            addressID = addressArrayList.get(0).id;
                            tvContact.setText(addressArrayList.get(0).contact);
                            tvAddress.setText(addressArrayList.get(0).region);
                            tvCellphone.setText(addressArrayList.get(0).phoneNumber);

                            rlyAddressNone.setVisibility(View.GONE);
                            rlyAddress.setVisibility(View.VISIBLE);

                            showSelectTime();
                        }
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
                case R.id.rlyAddressNone:
                    if (!UserInfoUtil.isLogin()) {
                        startActivity(new Intent(ServerOrderSelectActivity.this, LoginActivity.class));
                    } else {
                        Intent intent = new Intent(ServerOrderSelectActivity.this, AddressListActivity.class);
                        intent.putExtra("type", "edit");
                        intent.putExtra("id", "");
                        startActivityForResult(intent, Constants.req_code_address);
                    }
                    break;
                case R.id.rlyAddress:
                    Intent intent = new Intent(ServerOrderSelectActivity.this, AddressListActivity.class);
                    intent.putExtra("type", "edit");
                    intent.putExtra("id", addressID);
                    startActivityForResult(intent, Constants.req_code_address);
                    break;
                case R.id.llyDeposit:
                    if (!UserInfoUtil.isLogin()) {
                        startActivity(new Intent(ServerOrderSelectActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(ServerOrderSelectActivity.this, DepositActivity.class));
                    }
                    break;
                case R.id.tvTime:
                    orderConfirm.addressid = addressID;
                    orderConfirm.contact = tvContact.getText().toString().trim();
                    orderConfirm.region = tvAddress.getText().toString().trim();
                    orderConfirm.cellphone = tvCellphone.getText().toString().trim();
                    orderConfirm.serverCode = code;
                    orderConfirm.serverName = name;

                    Intent timeIntent = new Intent(ServerOrderSelectActivity.this, ServerOrderSelectTimeActivity.class);
                    timeIntent.putExtra("orderConfirm", orderConfirm);
                    timeIntent.putExtra("price", (Serializable) prices);
                    startActivity(timeIntent);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.req_code_address:
                    if (null != data) {
                        AddressEntity address = (AddressEntity) data.getSerializableExtra("address");
                        if (address != null) {
                            addressID = address.id;

                            tvContact.setText(address.contact);
                            tvAddress.setText(address.region);
                            tvCellphone.setText(address.phoneNumber);

                            rlyAddressNone.setVisibility(View.GONE);
                            rlyAddress.setVisibility(View.VISIBLE);
                        } else {
                            rlyAddressNone.setVisibility(View.VISIBLE);
                            rlyAddress.setVisibility(View.GONE);
                        }
                        showSelectTime();
                    }
                    break;
            }
        }
    }
}
