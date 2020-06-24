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

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.servers.ServerPrice;
import com.community.customer.api.user.ServerOrderConfirm;
import com.community.customer.api.user.ServerTimeEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.event.PayEvent;
import com.community.support.AutoBaseTitleActivity;
import com.community.support.component.FontTextView;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerOrderSelectTimeActivity extends AutoBaseTitleActivity {
    private LinearLayout llyDay, llyTimes;

    private ServerOrderConfirm orderConfirm;
    private List<ServerPrice> prices;

    private ArrayList<ServerTimeEntity> serverTimes = new ArrayList<>();
    private ArrayList<DayEntity> dayViews = new ArrayList<>();
    private ArrayList<TextView> timeViews = new ArrayList<>();

    private int clickIndex = 0, clickTimeIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_order_select_time);

        Intent intent = getIntent();
        orderConfirm = (ServerOrderConfirm)intent.getSerializableExtra("orderConfirm");
        prices = (List<ServerPrice>) intent.getSerializableExtra("price");

        initView();
        getTodayTimes();

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
        LinearLayout llyBack = findViewById(R.id.llyBack);
        llyBack.setOnClickListener(clickListener);

        llyDay = findViewById(R.id.llyDay);
        llyTimes = findViewById(R.id.llyTimes);

        TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(clickListener);
    }

    private void initDays() {
        for (int i = 0; i < serverTimes.size(); ++i) {
            DayEntity dayEntity = new DayEntity();
            LinearLayout linearLayout = new LinearLayout(this);
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AutoUtils.getPercentWidthSize(63), AutoUtils.getPercentHeightSize(44));
                if (i == serverTimes.size() - 1) {
                    layoutParams.setMargins(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentHeightSize(9), AutoUtils.getPercentWidthSize(15), 0);
                } else {
                    layoutParams.setMargins(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentHeightSize(9), 0, 0);
                }
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackgroundResource(R.drawable.shape_rectangle_corner_white_2);
                llyDay.addView(linearLayout);
                dayEntity.linearLayout = linearLayout;
            }
            {
                TextView textView = new FontTextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(serverTimes.get(i).dayWeek);
                textView.setTextColor(Color.parseColor("#383838"));
                textView.setTextSize(14);
                linearLayout.addView(textView);
                dayEntity.tvDayWeek = textView;
            }
            {
                TextView textView = new FontTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(5), 0, 0);
                textView.setLayoutParams(layoutParams);
                textView.setText(serverTimes.get(i).dayDesc.substring(5));
                textView.setTextColor(Color.parseColor("#383838"));
                textView.setTextSize(11);
                linearLayout.addView(textView);
                dayEntity.tvDayDesc = textView;
            }
            dayViews.add(dayEntity);

            final int finalI = i;
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickIndex = finalI;
                    changeClick();
                }
            });
        }

        changeClick();
    }

    private void initTimesView(List<ServerTimeEntity.HourTime> hourTimes) {
        llyTimes.removeAllViews();
        clickTimeIndex = -1;
        timeViews.clear();

        if (hourTimes == null || hourTimes.size() <= 0) {
            return;
        }

        for (int i = 0; i < hourTimes.size(); i = i + 4) {
            LinearLayout linearLayout = new LinearLayout(this);
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(35));
                layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(10), 0, 0);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                llyTimes.addView(linearLayout);
            }
            initTime(linearLayout, hourTimes, i);
            initTime(linearLayout, hourTimes, i + 1);
            initTime(linearLayout, hourTimes, i + 2);
            initTime(linearLayout, hourTimes, i + 3);
        }
    }

    private void initTime(LinearLayout linearLayout, List<ServerTimeEntity.HourTime> hourTimes, final int i) {
        {
            TextView textView = new FontTextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            textView.setGravity(Gravity.CENTER);

            textView.setTextSize(14);
            linearLayout.addView(textView);
            if (i > hourTimes.size() - 1) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setText(hourTimes.get(i).timeDesc);
                timeViews.add(textView);
                if (hourTimes.get(i).isTimeActive) {
                    textView.setBackgroundResource(R.drawable.shape_rectangle_corner_white_3);
                    textView.setTextColor(Color.parseColor("#383838"));

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickTimeIndex = i;
                            changeClickTime();
                        }
                    });
                } else {
                    textView.setBackgroundResource(R.drawable.shape_rectangle_corner_white_2);
                    textView.setTextColor(Color.parseColor("#cbcbcb"));
                    textView.setEnabled(false);
                }
            }
        }

        {
            if (i % 4 == 3) {
                return;
            }
            View view = new View(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(8), ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.addView(view);
        }
    }

    private void changeClick() {
        for (int i = 0; i < dayViews.size(); ++i) {
            if (clickIndex == i) {
                dayViews.get(i).linearLayout.setBackgroundResource(R.drawable.shape_stroke_4e_2);
                dayViews.get(i).tvDayWeek.setTextColor(Color.parseColor("#02bcae"));
                dayViews.get(i).tvDayDesc.setTextColor(Color.parseColor("#02bcae"));
            } else {
                dayViews.get(i).linearLayout.setBackgroundResource(R.drawable.shape_rectangle_corner_white_2);
                dayViews.get(i).tvDayWeek.setTextColor(Color.parseColor("#383838"));
                dayViews.get(i).tvDayDesc.setTextColor(Color.parseColor("#383838"));
            }
        }

        initTimesView(serverTimes.get(clickIndex).hourTimes);
    }

    private void changeClickTime() {
        for (int i = 0; i < timeViews.size(); ++i) {
            if (clickTimeIndex != i) {
                if (timeViews.get(i).isEnabled()) {
                    timeViews.get(i).setBackgroundResource(R.drawable.shape_rectangle_corner_white_3);
                    timeViews.get(i).setTextColor(Color.parseColor("#383838"));
                }
            } else {
                timeViews.get(i).setBackgroundResource(R.drawable.shape_stroke_4e_2);
                timeViews.get(i).setTextColor(Color.parseColor("#02bcae"));
            }
        }
    }

    private void getTodayTimes() {
        Logger.getLogger().d("获取今天服务时间列表");

        new UserDataManager().getTodayServerTimeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerTimeEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取服务时间列表：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerTimeEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取服务时间列表，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取服务时间列表, result为空");
                                return;
                            }

                            serverTimes.addAll(result.data);
//                            getTimes();
                            initDays();
                        }
                    }
                });
    }

    private void getTimes() {
        Logger.getLogger().d("获取服务时间列表");
        UserDataManager dataManager = new UserDataManager();

        dataManager.getServerTimeList(orderConfirm.serverCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerTimeEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("获取服务时间列表：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerTimeEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取服务时间列表，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取服务时间列表, result为空");
                                return;
                            }

                            ArrayList<ServerTimeEntity> serverTimeList = result.data;
                            for (ServerTimeEntity serverTime : serverTimeList) {
                                for (ServerTimeEntity serverTime1 : serverTimes) {
                                    if (serverTime1.dayTime == serverTime.dayTime) {
                                        serverTime1.hourTimes = serverTime.hourTimes;
                                    }
                                }
                            }
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

                case R.id.tvSubmit:
                    if (clickTimeIndex == -1) {
                        ToastUtil.show(ServerOrderSelectTimeActivity.this, "请选择服务时间");
                        return;
                    }
                    //下订单
                    orderConfirm.serverTime = serverTimes.get(clickIndex).dayTime + serverTimes.get(clickIndex).hourTimes.get(clickTimeIndex).hourTime;

                    Logger.getLogger().e("服务时间：" + orderConfirm.serverTime);
                    Intent intent = new Intent(ServerOrderSelectTimeActivity.this, ServerOrderConfirmActivity.class);
                    intent.putExtra("orderConfirm", orderConfirm);
                    intent.putExtra("price", (Serializable) prices);
                    startActivity(intent);
                    break;
            }
        }
    };

    private class DayEntity {
        public LinearLayout linearLayout;
        public TextView tvDayWeek, tvDayDesc;
    }
}
