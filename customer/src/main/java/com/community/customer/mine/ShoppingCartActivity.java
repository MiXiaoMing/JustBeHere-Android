package com.community.customer.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.user.CartListEntity;
import com.community.customer.api.user.GoodsOrderConfirm;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.mine.adpater.CartAdapter;
import com.community.customer.order.GoodsOrderConfirmActivity;
import com.community.support.AutoBaseTitleActivity;

import java.util.ArrayList;

import cn.wdcloud.acaeva.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShoppingCartActivity extends AutoBaseTitleActivity {

    private ImageView ivSelect;
    private TextView tvEdit, tvSelectDesc, tvPrice, tvConfirm;

    private String type = "buy"; //edit: 编辑状态, buy：购买状态
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_cart);

        ListView lvCartList = findViewById(R.id.lvCartList);
        cartAdapter = new CartAdapter(this);
        lvCartList.setAdapter(cartAdapter);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        tvEdit = findViewById(R.id.tvEdit);

        LinearLayout llySelect = findViewById(R.id.llySelect);
        ivSelect = findViewById(R.id.ivSelect);
        tvSelectDesc = findViewById(R.id.tvSelectDesc);

        tvPrice = findViewById(R.id.tvPrice);
        tvConfirm = findViewById(R.id.tvConfirm);

        llyBack.setOnClickListener(clickListener);
        tvEdit.setOnClickListener(clickListener);
        llySelect.setOnClickListener(clickListener);
        tvConfirm.setOnClickListener(clickListener);

        getCartList();
    }

    public void notifyCount(boolean isAllSelect, int selectCount, float price) {
        if (isAllSelect) {
            ivSelect.setImageResource(R.drawable.icon_selected);
            tvSelectDesc.setText("已全选");
        } else {
            ivSelect.setImageResource(R.drawable.icon_unselected);
            tvSelectDesc.setText("全选");
        }
        if (selectCount > 0) {
            if (type.equals("buy")) {
                tvConfirm.setText("去结算（" + selectCount + "）");
            } else {
                tvConfirm.setText("删除所选（" + selectCount + "）");
            }
            if (price > 0) {
                tvConfirm.setBackgroundColor(Color.parseColor("#02bdac"));
                tvConfirm.setEnabled(true);
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText("¥ " + price);
            } else {
                tvConfirm.setBackgroundColor(getResources().getColor(R.color.gray));
                tvConfirm.setEnabled(false);
                tvPrice.setVisibility(View.INVISIBLE);
                tvPrice.setText("");
            }
        } else {
            tvConfirm.setBackgroundColor(getResources().getColor(R.color.gray));
            tvConfirm.setEnabled(false);
            if (type.equals("buy")) {
                tvConfirm.setText("去结算");
            } else {
                tvConfirm.setText("删除所选");
            }
            tvConfirm.setBackgroundColor(getResources().getColor(R.color.gray));
            tvConfirm.setEnabled(false);
            tvPrice.setVisibility(View.INVISIBLE);
            tvPrice.setText("");
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvEdit: {
                    if (type.equals("buy")) {
                        tvEdit.setText("完成");
                        type = "edit";
                        tvConfirm.setText(tvConfirm.getText().toString().trim().replace("去结算", "删除所选"));
                    } else {
                        tvEdit.setText("编辑");
                        type = "buy";
                        tvConfirm.setText(tvConfirm.getText().toString().trim().replace("删除所选", "去结算"));
                    }
                }
                break;

                case R.id.llySelect:
                    String selectDesc = tvSelectDesc.getText().toString().trim();
                    if (selectDesc.equals("全选")) {
                        cartAdapter.selectAll();
                    } else {
                        cartAdapter.selectNone();
                    }
                    break;

                case R.id.tvConfirm:
                    if (type.equals("buy")) {
                        GoodsOrderConfirm goodsConfirm = new GoodsOrderConfirm();

                        ArrayList<CartListEntity> carts = cartAdapter.getAll();
                        for (CartListEntity cart : carts) {
                            goodsConfirm.addItem(cart.goods.code, cart.goods.title, cart.goods.icon, cart.cart.typeID, cart.cart.typeName, cart.price.price, cart.cart.number);
                            goodsConfirm.price += cart.price.price * cart.cart.number;
                        }

                        Intent intent = new Intent(ShoppingCartActivity.this, GoodsOrderConfirmActivity.class);
                        intent.putExtra("goodsConfirm", goodsConfirm);
                        startActivity(intent);
                    } else {
                        cartAdapter.deleteSelect();
                    }
                    break;
            }
        }
    };

    private void getCartList() {
        Logger.getLogger().d("获取购物车列表");
        UserDataManager dataManager = new UserDataManager();

        dataManager.getShoppingCartList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CartListEntity>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(CartListEntity result) {
                        cartAdapter.addAll(result.data);
                    }
                });
    }
}
