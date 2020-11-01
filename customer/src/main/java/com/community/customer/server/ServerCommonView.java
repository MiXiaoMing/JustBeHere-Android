package com.community.customer.server;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.utils.app.ActivityUtil;
import com.appframe.utils.logger.Logger;
import com.community.customer.common.ServerConfig;
import com.community.customer.api.servers.Server;
import com.community.customer.api.servers.ServerClassify;
import com.community.customer.common.Constants;
import com.community.support.component.FontTextView;
import com.community.support.utils.BgUtil;
import com.community.support.utils.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import cn.wdcloud.acaeva.R;

public class ServerCommonView {

    public static void serverClassify(final Activity activity, LinearLayout root, ServerClassify serverClassify) {
        if (!ActivityUtil.isAvailable(activity)) {
            return;
        }

        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(10), 0, 0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackgroundColor(Color.WHITE);
        root.addView(linearLayout);

        LinearLayout linearLayout1 = new LinearLayout(activity);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(45)));
        linearLayout.addView(linearLayout1);

        TextView textView = new FontTextView(activity);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(AutoUtils.getPercentWidthSize(15), 0, 0, 0);
        textView.setLayoutParams(layoutParams1);
        textView.setText(serverClassify.name);
        textView.setTextColor(Color.parseColor("#f8a710"));
        textView.setTextSize(15);
        linearLayout1.addView(textView);

        TextView textView1 = new FontTextView(activity);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(AutoUtils.getPercentWidthSize(25), 0, 0, 0);
        textView1.setLayoutParams(layoutParams2);
        textView1.setText(serverClassify.desc);
        textView1.setTextColor(Color.parseColor("#b5b2ad"));
        textView1.setTextSize(12);
        linearLayout1.addView(textView1);

        LinearLayout linearLayout2 = new LinearLayout(activity);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.addView(linearLayout2);
        for (int j = 0; j < serverClassify.servers.size(); ++j) {
            final Server server = serverClassify.servers.get(j);

            LinearLayout linearLayout3 = new LinearLayout(activity);
            linearLayout3.setOrientation(LinearLayout.VERTICAL);
            linearLayout3.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            layoutParams5.setMargins(AutoUtils.getPercentWidthSize(5), 0, AutoUtils.getPercentWidthSize(5), 0);
            linearLayout3.setLayoutParams(layoutParams5);
            linearLayout2.addView(linearLayout3);

            linearLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toClass(activity, Constants.data_server, server.code);
                }
            });

            ImageView imageView = new ImageView(activity);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(100), AutoUtils.getPercentWidthSize(100)));
            BgUtil.loadImageCenterInside(activity, ServerConfig.file_host + server.bigicon, R.drawable.default_image_white, imageView);
            linearLayout3.addView(imageView);

            TextView textView2 = new FontTextView(activity);
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams3.setMargins(0, AutoUtils.getPercentHeightSize(12), 0, 0);
            textView2.setLayoutParams(layoutParams3);
            textView2.setText(server.name);
            textView2.setTextColor(Color.parseColor("#474747"));
            textView2.setTextSize(14);
            linearLayout3.addView(textView2);

            TextView textView3 = new FontTextView(activity);
            LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams4.setMargins(0, AutoUtils.getPercentHeightSize(7), 0, AutoUtils.getPercentHeightSize(15));
            textView3.setLayoutParams(layoutParams4);
            textView3.setText(server.desc);
            textView3.setTextColor(Color.parseColor("#b5b2ad"));
            textView3.setTextSize(12);
            linearLayout3.addView(textView3);
        }
    }

    public static void toClass(Activity activity, String classify, String code) {
        if (TextUtils.isEmpty(classify) || TextUtils.isEmpty(code)) {
            Logger.getLogger().e("数据是空的：" + classify + " ... " + code);
            return;
        }
        try {
            if (classify.equals(Constants.data_server_classify)) {
                Intent intent = new Intent(activity, Class.forName(Constants.activity_service_classify));
                intent.putExtra("code", code);
                activity.startActivity(intent);
            } else if (classify.equals(Constants.data_server)) {
                Intent intent = new Intent(activity, Class.forName(Constants.activity_service_detail));
                intent.putExtra("code", code);
                activity.startActivity(intent);
            }  else if (classify.equals(Constants.data_goods)) {
                Intent intent = new Intent(activity, Class.forName(Constants.activity_goods_detail));
                intent.putExtra("goodsid", code);
                activity.startActivity(intent);
            } else {
                ToastUtil.show(activity, "数据错误：" + classify);
            }
        } catch (ClassNotFoundException e) {
            Logger.getLogger().e("类未找到：" + classify + " ... " + code);
        }
    }

    public static void serverTable(Activity activity, LinearLayout root, List<Server> servers) {
        LinearLayout linearLayout = new LinearLayout(activity);

        ArrayList<Server> servers0 = new ArrayList<>();
        for (int i = 0; i < servers.size(); ++i) {
            if (!servers.get(i).level.equals("1")) {
                servers0.add(servers.get(i));
            }
        }

        for (int i = 0; i < servers0.size(); ++i) {
            if (i % 2 == 0) {
                View view = new View(activity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(1));
                if (i == 0) {
                    layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(10), 0, 0);
                }
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.parseColor("#f2f2f2"));
                root.addView(view);

                LinearLayout linearLayout1 = new LinearLayout(activity);
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout1.setBackgroundColor(Color.WHITE);
                linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(70)));
                root.addView(linearLayout1);

                linearLayout = linearLayout1;
            } else {
                View view = new View(activity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentHeightSize(1), ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.parseColor("#f2f2f2"));
                linearLayout.addView(view);
            }
            serverTableItem(activity, linearLayout, servers0.get(i));
        }
    }

    private static void serverTableItem(final Activity activity, LinearLayout root, final Server server) {
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
        relativeLayout.setPadding(AutoUtils.getPercentHeightSize(10), 0, AutoUtils.getPercentHeightSize(10), 0);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        root.addView(relativeLayout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toClass(activity, Constants.data_server, server.code);
            }
        });

        LinearLayout linearLayout2 = new LinearLayout(activity);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams4.setMargins(0, AutoUtils.getPercentHeightSize(10), 0, 0);
        linearLayout2.setLayoutParams(layoutParams4);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        relativeLayout.addView(linearLayout2);

        TextView textView = new FontTextView(activity);
        textView.setText(server.name);
        textView.setSingleLine();
        textView.setMaxEms(7);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.parseColor("#222222"));
        textView.setTextSize(16);
        linearLayout2.addView(textView);

        TextView textView1 = new FontTextView(activity);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams3.setMargins(0, AutoUtils.getPercentHeightSize(8), 0, 0);
        textView1.setLayoutParams(layoutParams3);
        textView1.setText(server.shortdesc);
        textView1.setTextColor(Color.parseColor("#b5b2ad"));
        textView1.setTextSize(11);
        linearLayout2.addView(textView1);

        ImageView imageView = new ImageView(activity);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(AutoUtils.getPercentHeightSize(45), AutoUtils.getPercentHeightSize(45));
        layoutParams2.setMargins(0, AutoUtils.getPercentHeightSize(4), 0, 0);
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageView.setLayoutParams(layoutParams2);
        BgUtil.loadImageCircle(activity, ServerConfig.file_host + server.bigicon, R.drawable.default_image_white, imageView);
        relativeLayout.addView(imageView);
    }
}
