package com.community.customer.server;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.common.ServerConfig;
import com.community.customer.api.servers.ServerDetail;
import com.community.customer.api.servers.ServerPrice;
import com.community.support.component.FontTextView;
import com.community.support.utils.BgUtil;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


public class ServerDetailCommonView {

    public static void prices(final Activity activity, LinearLayout root, final List<ServerPrice> prices) {
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(15), 0, 0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        root.addView(linearLayout);
        {
            LinearLayout linearLayout1 = new LinearLayout(activity);
            linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(30)));
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setBackgroundColor(Color.parseColor("#fbf6f3"));
            linearLayout.addView(linearLayout1);
            ArrayList<String> items = new ArrayList<>();
            items.add("项目名称");
            items.add("非会员价格");
            items.add("会员价格");
            for (int j = 0; j < items.size(); ++j) {
                TextView textView = new FontTextView(activity);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                textView.setGravity(Gravity.CENTER);
                textView.setText(items.get(j));
                textView.setTextColor(Color.parseColor("#7a756f"));
                textView.setTextSize(13);
                linearLayout1.addView(textView);
            }
        }

        for (int i = 0; i < prices.size(); ++i) {
            ServerPrice item = prices.get(i);

            LinearLayout linearLayout1 = new LinearLayout(activity);
            linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(30)));
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(linearLayout1);
            ArrayList<String> items = new ArrayList<>();
            items.add(item.name);
            items.add(item.price + "元/" + item.unit);
            items.add(item.price * item.discount + "元/" + item.unit);
            for (int j = 0; j < items.size(); ++j) {
                TextView textView = new FontTextView(activity);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                textView.setGravity(Gravity.CENTER);
                textView.setText(items.get(j));
                textView.setTextColor(Color.parseColor("#050505"));
                textView.setBackgroundResource(R.drawable.shape_rectangle_corner_white);
                textView.setTextSize(13);
                linearLayout1.addView(textView);
            }
        }
    }

    public static void note(final Activity activity, LinearLayout root, final String note) {
        if (activity == null || root == null || TextUtils.isEmpty(note)) {
            return;
        }
        TextView textView = new FontTextView(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(15), 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setText(note);
        textView.setTextColor(activity.getResources().getColor(R.color.gray_text_10));
        textView.setTextSize(11);
        root.addView(textView);
    }

    public static void part(final Activity activity, LinearLayout root, final String value) {
        Logger.getLogger().d("解析数据：" + value);
        Gson gson = new Gson();
        Part part = gson.fromJson(value, Part.class);

        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        root.addView(linearLayout);

        TextView textView = new FontTextView(activity);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, AutoUtils.getPercentHeightSize(18), 0, 0);
        textView.setLayoutParams(layoutParams1);
        textView.setGravity(Gravity.CENTER);
        textView.setText(part.name);
        textView.setTextColor(Color.parseColor("#363636"));
        textView.setTextSize(16);
        linearLayout.addView(textView);

        if (!TextUtils.isEmpty(part.desc)) {
            TextView textView1 = new FontTextView(activity);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(0, AutoUtils.getPercentHeightSize(5), 0, 0);
            textView1.setLayoutParams(layoutParams2);
            textView1.setGravity(Gravity.CENTER);
            textView1.setText(part.desc);
            textView1.setTextColor(Color.parseColor("#5c5c5c"));
            textView1.setTextSize(11);
            linearLayout.addView(textView1);
        }

        fillPartBody(activity, linearLayout, part);

        if (!TextUtils.isEmpty(part.content)) {
            TextView textView1 = new FontTextView(activity);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView1.setLayoutParams(layoutParams2);
            textView1.setText(part.content);
            textView1.setTextColor(Color.parseColor("#363636"));
            textView1.setTextSize(14);
            linearLayout.addView(textView1);
        }

        if (part.subs != null && part.subs.size() > 0) {
            for (int i = 0; i < part.subs.size(); ++i) {
                Part subPart = part.subs.get(i);
                addSubPartTitle(activity, linearLayout, subPart.name, subPart.desc);
                fillPartBody(activity, linearLayout, subPart);
                note(activity, linearLayout, subPart.note);
            }
        }

        if (!TextUtils.isEmpty(part.note)) {
            note(activity, root, part.note);
        }
    }

    private static void fillPartBody(Activity activity, LinearLayout root, Part part) {
        if (TextUtils.isEmpty(part.type)) {
            return;
        }
        if (part.type.equals("01")) {
            int imageHeight = 120;
            if (!TextUtils.isEmpty(part.imageheight)) {
                imageHeight = Integer.valueOf(part.imageheight);
            }
            ImageView imageView = new ImageView(activity);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(imageHeight));
            layoutParams2.setMargins(0, AutoUtils.getPercentHeightSize(13), 0, 0);
            imageView.setLayoutParams(layoutParams2);
            BgUtil.loadImageCenterInside(activity, ServerConfig.file_host + part.image, R.drawable.default_image_white, imageView);
            root.addView(imageView);
        } else if (part.type.equals("03")) {
            if (part.subs != null && part.subs.size() > 0) {
                LinearLayout linearLayout = new LinearLayout(activity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(AutoUtils.getPercentWidthSize(-10), AutoUtils.getPercentHeightSize(12), AutoUtils.getPercentWidthSize(-10), 0);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                root.addView(linearLayout);

                for (int i = 0; i < part.subs.size(); ++i) {
                    Part item = part.subs.get(i);

                    LinearLayout linearLayout2 = new LinearLayout(activity);
                    linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    linearLayout2.setOrientation(LinearLayout.VERTICAL);
                    linearLayout2.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearLayout.addView(linearLayout2);

                    int imageHeight = 70, imageWidth = 40;
                    if (!TextUtils.isEmpty(item.imageheight)) {
                        imageHeight = Integer.valueOf(item.imageheight);
                    }
                    if (!TextUtils.isEmpty(item.imagewidth)) {
                        imageWidth = Integer.valueOf(item.imagewidth);
                    }
                    ImageView imageView = new ImageView(activity);
                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(AutoUtils.getPercentHeightSize(imageWidth), AutoUtils.getPercentHeightSize(imageHeight)));
                    ImageLoader.normal(activity, ServerConfig.file_host + item.image, R.drawable.default_image_white, imageView);
                    linearLayout2.addView(imageView);

                    TextView textView2 = new FontTextView(activity);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams2.setMargins(0, AutoUtils.getPercentHeightSize(8), 0, 0);
                    textView2.setLayoutParams(layoutParams2);
                    textView2.setText(item.name);
                    textView2.setTextColor(Color.parseColor("#292929"));
                    textView2.setTextSize(12);
                    linearLayout2.addView(textView2);

                    TextView textView3 = new FontTextView(activity);
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams3.setMargins(0, AutoUtils.getPercentHeightSize(3), 0, 0);
                    textView3.setLayoutParams(layoutParams3);
                    textView3.setText(item.desc);
                    textView3.setTextColor(activity.getResources().getColor(R.color.gray_text_10));
                    textView3.setTextSize(10);
                    linearLayout2.addView(textView3);
                }
            }
        }
    }

    public static void refTime(final Activity activity, LinearLayout root, final String time) {
        Gson gson = new Gson();
        Part part = gson.fromJson(time, Part.class);

        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(linearLayout);

        addSubPartTitle(activity, linearLayout, part.name, part.desc);

        if (part.times != null && part.times.size() > 0) {
            LinearLayout linearLayout2 = new LinearLayout(activity);
            LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams4.setMargins(AutoUtils.getPercentWidthSize(-10), AutoUtils.getPercentHeightSize(12), AutoUtils.getPercentWidthSize(-10), 0);
            linearLayout2.setLayoutParams(layoutParams4);
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(linearLayout2);

            for (int i = 0; i < part.times.size(); ++i) {
                Time item = part.times.get(i);

                LinearLayout linearLayout3 = new LinearLayout(activity);
                linearLayout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                linearLayout3.setOrientation(LinearLayout.VERTICAL);
                linearLayout3.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout2.addView(linearLayout3);

                TextView textView2 = new FontTextView(activity);
                textView2.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentHeightSize(50), AutoUtils.getPercentHeightSize(50)));
                textView2.setBackgroundResource(R.drawable.shape_oval_25beac);
                textView2.setGravity(Gravity.CENTER);
                textView2.setText(item.content);
                textView2.setTextColor(Color.parseColor("#25beac"));
                textView2.setTextSize(12);
                linearLayout3.addView(textView2);

                TextView textView3 = new FontTextView(activity);
                LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams5.setMargins(0, AutoUtils.getPercentHeightSize(8), 0, 0);
                textView3.setLayoutParams(layoutParams5);
                textView3.setText(item.time);
                textView3.setTextColor(Color.parseColor("#484848"));
                textView3.setTextSize(10);
                linearLayout3.addView(textView3);
            }
        }

        note(activity, root, part.note);
    }

    public static void others(final Activity activity, LinearLayout root, final ArrayList<ServerDetail.Other> others) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(linearLayout);

        addSubPartTitle(activity, linearLayout, "其他服务推荐", "");


        for (int i = 0; i < others.size(); ++i) {
            final ServerDetail.Other item = others.get(i);

            RelativeLayout relativeLayout = new RelativeLayout(activity);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(0, AutoUtils.getPercentHeightSize(15), 0, 0);
            relativeLayout.setLayoutParams(layoutParams2);
            relativeLayout.setBackgroundResource(R.drawable.shape_rectangle_corner_white);
            linearLayout.addView(relativeLayout);

            ImageView imageView = new ImageView(activity);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(85)));
            ImageLoader.normal(activity, ServerConfig.file_host + item.cover, R.drawable.default_image_white, imageView);
            relativeLayout.addView(imageView);

            TextView textView = new FontTextView(activity);
            textView.setMinWidth(AutoUtils.getPercentWidthSize(70));
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AutoUtils.getPercentHeightSize(25));
            layoutParams3.setMargins(AutoUtils.getPercentWidthSize(10), 0, 0, 0);
            textView.setLayoutParams(layoutParams3);
            textView.setPadding(AutoUtils.getPercentWidthSize(5), 0, AutoUtils.getPercentWidthSize(5), 0);
            textView.setBackgroundColor(Color.parseColor("#05beb0"));
            textView.setGravity(Gravity.CENTER);
            textView.setText(item.name);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(12);
            relativeLayout.addView(textView);

            TextView textView2 = new FontTextView(activity);
            textView2.setMinHeight(AutoUtils.getPercentHeightSize(40));
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AutoUtils.getPercentHeightSize(25));
            layoutParams4.setMargins(0, AutoUtils.getPercentHeightSize(85), AutoUtils.getPercentWidthSize(85), 0);
            layoutParams4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textView2.setLayoutParams(layoutParams4);
            textView2.setPadding(AutoUtils.getPercentWidthSize(8), AutoUtils.getPercentHeightSize(8), AutoUtils.getPercentWidthSize(8), AutoUtils.getPercentHeightSize(8));
            textView2.setMinHeight(AutoUtils.getPercentHeightSize(40));
            textView2.setGravity(Gravity.CENTER);
            textView2.setText(item.desc);
            textView2.setTextColor(Color.parseColor("#5f5e5c"));
            textView2.setTextSize(10);
            relativeLayout.addView(textView2);

            TextView textView3 = new FontTextView(activity);
            RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(AutoUtils.getPercentWidthSize(70), AutoUtils.getPercentHeightSize(22));
            layoutParams5.setMargins(0, AutoUtils.getPercentHeightSize(90), AutoUtils.getPercentWidthSize(10), AutoUtils.getPercentHeightSize(5));
            layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textView3.setLayoutParams(layoutParams5);
            textView3.setBackgroundResource(R.drawable.shape_rectangle_corner_50b79c);
            textView3.setGravity(Gravity.CENTER);
            textView3.setText("立即预约");
            textView3.setTextColor(Color.parseColor("#50b79c"));
            textView3.setTextSize(12);
            relativeLayout.addView(textView3);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ServerCommonView.toClass(activity, "02", item.code);
                }
            });
        }
    }

    private static void addSubPartTitle(Activity activity, LinearLayout root, String name, String desc) {
        LinearLayout linearLayout1 = new LinearLayout(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, AutoUtils.getPercentHeightSize(16), 0, 0);
        linearLayout1.setLayoutParams(layoutParams);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(linearLayout1);

        View view = new View(activity);
        view.setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentHeightSize(1)));
        view.setBackgroundColor(activity.getResources().getColor(R.color.gray_text_10));
        linearLayout1.addView(view);

        TextView textView = new FontTextView(activity);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(AutoUtils.getPercentWidthSize(8), 0, 0, 0);
        textView.setLayoutParams(layoutParams2);
        textView.setText(name);
        textView.setTextColor(activity.getResources().getColor(R.color.black_text));
        textView.setTextSize(14);
        linearLayout1.addView(textView);

        View view2 = new View(activity);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentHeightSize(1));
        layoutParams3.setMargins(AutoUtils.getPercentWidthSize(8), 0, 0, 0);
        view2.setLayoutParams(layoutParams3);
        view2.setBackgroundColor(activity.getResources().getColor(R.color.gray_text_10));
        linearLayout1.addView(view2);

        if (!TextUtils.isEmpty(desc)) {
            TextView textView2 = new FontTextView(activity);
            LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams4.setMargins(0, AutoUtils.getPercentHeightSize(5), 0, 0);
            textView2.setLayoutParams(layoutParams4);
            textView2.setText(desc);
            textView2.setTextColor(Color.parseColor("#8f8f8f"));
            textView2.setTextSize(10);
            root.addView(textView2);
        }
    }

    private class Part {
        public String name, desc, type;
        public String image, imageheight, imagewidth;
        public String content;
        public String note;
        public ArrayList<Time> times;
        public ArrayList<Part> subs;
    }

    private class Time {
        public String content, time;
    }
}
