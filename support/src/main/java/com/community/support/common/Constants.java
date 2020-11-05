package com.community.support.common;

import android.graphics.Color;

import java.util.ArrayList;

public class Constants {

    //微信
    public final static String wx_app_id = "wx0d83ef9bd8190fe0";
    public final static String wx_app_secret = "803324f7bc78c6491527f833df4a98ef";
    public final static String wx_mch_id = "1501887481";
    public final static String wx_api_key = "q2eu8pxycp2018CP0418Irt1650BjLxh";

    //数据分类
    public static final String data_server_classify = "01";
    public static final String data_server = "02";
    public static final String data_goods_classify = "03";
    public static final String data_goods = "04";
    public static final String data_others = "05";

    //标签颜色
    public static final ArrayList<String> tagsColor = new ArrayList<String>() {{
        add("#81a2f3"); add("#fe808b"); add("#f7853e");  add("#89bfff");
    }};

    //数据分类对应页面
    public static final String activity_service_classify = "com.community.customer.server.ServerClassifyActivity";
    public static final String activity_service_detail = "com.community.customer.server.ServerDetailActivity";
    public static final String activity_goods_detail = "com.community.customer.mall.GoodsDetailActivity";

    public static final String activity_deposit = "com.community.customer.mine.DepositActivity";

    //订单类型
    public static final String order_type_service = "服务订单";
    public static final String order_type_goods = "商品订单";

    //服务订单状态
    public static final String server_order_status_00 = "全部";
    public static final String server_order_status_01 = "待支付";
    public static final String server_order_status_02 = "待服务";
    public static final String server_order_status_03 = "服务中";
    public static final String server_order_status_04 = "已完成";
    public static final String server_order_status_05 = "已取消";
    public static final String server_order_status_01_02 = "待支付+待服务";

    public static String convertServerOrderStatus(String status) {
        if (status.equals("01")) {
            return server_order_status_01;
        } else if (status.equals("02")) {
            return server_order_status_02;
        } else if (status.equals("03")) {
            return server_order_status_03;
        } else if (status.equals("04")) {
            return server_order_status_04;
        }

        return server_order_status_05;
    }

    public static int getServerStatusColor(String status) {
        if (status.equals("01")) {
            return Color.parseColor("#da5826");
        } else if (status.equals("02")) {
            return Color.parseColor("#13cd3f");
        } else if (status.equals("03")) {
            return Color.parseColor("#0cb7b1");
        } else if (status.equals("04")) {
            return Color.parseColor("#313A43");
        }

        return Color.parseColor("#8a8a8a");
    }

    //商品订单状态
    public static final String goods_order_status_00 = "全部";
    public static final String goods_order_status_01 = "待支付";
    public static final String goods_order_status_02 = "待发货";
    public static final String goods_order_status_03 = "待收货";
    public static final String goods_order_status_04 = "已收货";
    public static final String goods_order_status_05 = "已取消";
    public static final String goods_order_status_01_02 = "待支付+待发货";

    public static String convertGoodsOrderStatus(String status) {
        if (status.equals("01")) {
            return goods_order_status_01;
        } else if (status.equals("02")) {
            return goods_order_status_02;
        } else if (status.equals("03")) {
            return goods_order_status_03;
        } else if (status.equals("04")) {
            return goods_order_status_04;
        }

        return goods_order_status_05;
    }

    public static int getGoodsStatusColor(String status) {
        if (status.equals("01")) {
            return Color.parseColor("#da5826");
        } else if (status.equals("02")) {
            return Color.parseColor("#13cd3f");
        } else if (status.equals("03")) {
            return Color.parseColor("#0cb7b1");
        } else if (status.equals("04")) {
            return Color.parseColor("#313A43");
        }

        return Color.parseColor("#8a8a8a");
    }

    //页面间数据传递
    public static final int req_code_address = 1001;
    public static final int req_order_remind = 1002;
    public static final int req_setting_name = 1003;

    //友盟
    public static final String umeng_app_key = "5c7e56e020365709b20000d0";

    //版本标签
    public static final String tag = "gray";
//    public static final String tag = "normal";


    public static final String phone_server_number = "10086";
}
