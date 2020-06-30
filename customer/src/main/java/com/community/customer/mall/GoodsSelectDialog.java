package com.community.customer.mall;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.LoginActivity;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.mall.GoodsEntity;
import com.community.customer.api.mall.GoodsPrice;
import com.community.customer.api.user.CountEntity;
import com.community.customer.api.user.GoodsOrderConfirm;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.api.user.input.CartBody;
import com.community.customer.common.ServerConfig;
import com.community.customer.order.GoodsOrderConfirmActivity;
import com.community.support.component.AutoFlowLayout;
import com.community.support.component.BaseDialog;
import com.community.support.component.FlowAdapter;
import com.community.support.component.FontTextView;
import com.community.support.utils.ReportUtil;
import com.community.support.utils.ToastUtil;
import com.community.support.utils.UserInfoUtil;
import com.zhy.autolayout.utils.AutoUtils;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择物品类型弹框
 */

public class GoodsSelectDialog extends BaseDialog {
    private GoodsEntity goods;
    private String type; //cart：添加到购物车  order：直接购买
    private String selectTypeID;
    private TextView tvNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_goods_select, (ViewGroup) window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

        goods = (GoodsEntity) getArguments().getSerializable("goods");
        type = getArguments().getString("type", "order");
        if (goods == null || goods.prices.size() <= 0) {
            if (goods != null)
                ReportUtil.reportError("商品数据错误：" + goods.code);
            dismiss();
        }

        initView(view);

        return view;
    }

    private void initView(View view) {
        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        final TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvSelectType = view.findViewById(R.id.tvSelectType);

        RelativeLayout rlyClose = view.findViewById(R.id.rlyClose);

        LinearLayout llyType = view.findViewById(R.id.llyType);
        final AutoFlowLayout aflyTypes = view.findViewById(R.id.aflyTypes);

        RelativeLayout rlySub = view.findViewById(R.id.rlySub);
        tvNumber = view.findViewById(R.id.tvNumber);
        RelativeLayout rlyAdd = view.findViewById(R.id.rlyAdd);

        TextView tvSubmit = view.findViewById(R.id.tvSubmit);

        ImageLoader.normal(this.getActivity(), ServerConfig.file_host + goods.icon, R.drawable.default_image_white, ivIcon);
        if (goods.mixPrice == goods.maxPrice) {
            tvPrice.setText("¥ " + goods.mixPrice);
        } else {
            tvPrice.setText("¥ " + goods.mixPrice + " - " + goods.maxPrice);
        }

        if (goods.prices.size() <= 1) {
            tvSelectType.setText("请选择：数量");
            llyType.setVisibility(View.GONE);
        } else {
            llyType.setVisibility(View.VISIBLE);
            tvSelectType.setText("请选择：规格，数量");
            aflyTypes.setMultiChecked(false);
            aflyTypes.setAdapter(new FlowAdapter(goods.prices) {
                @Override
                public View getView(int position) {
                    TextView textView = new FontTextView(GoodsSelectDialog.this.getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AutoUtils.getPercentHeightSize(27));
                    layoutParams.setMargins(AutoUtils.getPercentWidthSize(20), 0, 0, 0);
                    if (!TextUtils.isEmpty(selectTypeID) && selectTypeID.equals(goods.prices.get(position).id)) {
                        textView.setBackgroundResource(R.drawable.shape_stroke_4e_2);
                    } else {
                        textView.setBackgroundResource(R.drawable.shape_rectangle_corner_white_3);
                    }
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(goods.prices.get(position).name);
                    textView.setTextColor(Color.parseColor("#3a3a3a"));
                    textView.setTextSize(13);
                    return textView;
                }
            });
            aflyTypes.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    selectTypeID = goods.prices.get(position).id;
                    tvPrice.setText("¥ " + goods.prices.get(position).price);
                }
            });
        }

        rlyClose.setOnClickListener(clickListener);
        rlySub.setOnClickListener(clickListener);
        rlyAdd.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rlyClose:
                    dismiss();
                    break;

                case R.id.rlySub: {
                    int number = Integer.valueOf(tvNumber.getText().toString().trim());
                    if (number > 0) {
                        number -= 1;
                        tvNumber.setText(number + "");
                    }
                }
                break;

                case R.id.rlyAdd: {
                    int number = Integer.valueOf(tvNumber.getText().toString().trim());
                    number += 1;
                    tvNumber.setText(number + "");
                }
                break;

                case R.id.tvSubmit: {
                    int number = Integer.valueOf(tvNumber.getText().toString().trim());
                    if (goods.prices.size() > 1 && TextUtils.isEmpty(selectTypeID)) {
                        ToastUtil.show(GoodsSelectDialog.this.getActivity(), "请选择商品规格");
                        return;
                    }
                    if (number == 0) {
                        ToastUtil.show(GoodsSelectDialog.this.getActivity(), "请选择商品数量");
                        return;
                    }

                    if (type.equals("cart")) {
                        if (!UserInfoUtil.isLogin()) {
                            startActivity(new Intent(GoodsSelectDialog.this.getActivity(), LoginActivity.class));
                            return;
                        }

                        String typeid = "", typeName = "";
                        if (goods.prices.size() > 1) {
                            for (GoodsPrice goodsPrice : goods.prices) {
                                if (goodsPrice.id.equals(selectTypeID)) {
                                    typeid = selectTypeID;
                                    typeName = goodsPrice.name;
                                    break;
                                }
                            }
                        } else {
                           typeid = goods.prices.get(0).id;
                           typeName = "";
                        }
                        addCart(goods.code, typeid, typeName, number);
                    } else {
                        GoodsOrderConfirm goodsConfirm = new GoodsOrderConfirm();
                        if (goods.prices.size() > 1) {
                            for (GoodsPrice goodsPrice : goods.prices) {
                                if (goodsPrice.id.equals(selectTypeID)) {
                                    goodsConfirm.addItem(goods.code, goods.title, goods.icon, goodsPrice.id, goodsPrice.name,
                                            goodsPrice.price, number);
                                    goodsConfirm.price = goodsPrice.price * number;
                                }
                            }
                        } else {
                            goodsConfirm.addItem(goods.code, goods.title, goods.icon, goods.prices.get(0).id, "",
                                    goods.prices.get(0).price, number);
                            goodsConfirm.price = goods.prices.get(0).price * number;
                        }

                        Intent intent = new Intent(GoodsSelectDialog.this.getActivity(), GoodsOrderConfirmActivity.class);
                        intent.putExtra("goodsConfirm", goodsConfirm);
                        startActivity(intent);

                        dismiss();
                    }
                }
                break;
            }
        }
    };

    private void addCart(String goodsid, String typeid, String typeName, int number) {
        Logger.getLogger().d("加入购物车");

        CartBody body = new CartBody();
        body.goodsID = goodsid;
        body.typeID = typeid;
        body.typeName = typeName;
        body.number = number;

        new UserDataManager().addCart(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CountEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(CountEntity result) {
                        dismiss();
                    }
                });
    }
}
