package com.community.customer.mine.adpater;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.community.customer.api.CustomObserver;
import com.community.customer.api.mall.GoodsPrice;
import com.community.customer.api.user.CartListEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.api.user.entity.Cart;
import com.community.customer.api.user.input.CartBody;
import com.community.customer.common.ServerConfig;
import com.community.customer.mine.ShoppingCartActivity;

import java.util.ArrayList;
import java.util.Iterator;

import cn.wdcloud.acaeva.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CartAdapter extends BaseAdapter {
    private ShoppingCartActivity activity;
    private ArrayList<CartListEntity> entities = new ArrayList<>();

    public CartAdapter(ShoppingCartActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_cart, null);
            viewHolder.ivSelect = convertView.findViewById(R.id.ivSelect);
            viewHolder.ivIcon = convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvTypeName = convertView.findViewById(R.id.tvTypeName);
            viewHolder.tvPrice = convertView.findViewById(R.id.tvPrice);
            viewHolder.tvNumber = convertView.findViewById(R.id.tvNumber);
            viewHolder.rlySub = convertView.findViewById(R.id.rlySub);
            viewHolder.rlyAdd = convertView.findViewById(R.id.rlyAdd);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CartListEntity cart = entities.get(position);

        if (cart.goods == null || cart.cart == null) {
            return convertView;
        }

        ImageLoader.normal(activity, ServerConfig.file_host + cart.goods.icon, R.drawable.default_image_white, viewHolder.ivIcon);
        viewHolder.tvTitle.setText(cart.goods.title);
        if (TextUtils.isEmpty(cart.cart.typeName)) {
            viewHolder.tvTypeName.setVisibility(View.GONE);
        } else {
            viewHolder.tvTypeName.setVisibility(View.VISIBLE);
            viewHolder.tvTypeName.setText(cart.cart.typeName);
        }
        viewHolder.tvPrice.setText("¥ " + getTypePrice(cart.cart.typeID, cart.goodsPrices));
        viewHolder.tvNumber.setText(cart.cart.number + "");

        if (cart.isSelect) {
            viewHolder.ivSelect.setImageResource(R.drawable.icon_selected);
            entities.get(position).isSelect = true;
        } else {
            viewHolder.ivSelect.setImageResource(R.drawable.icon_unselected);
            entities.get(position).isSelect = false;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.isSelect) {
                    viewHolder.ivSelect.setImageResource(R.drawable.icon_unselected);
                    entities.get(position).isSelect = false;
                } else {
                    viewHolder.ivSelect.setImageResource(R.drawable.icon_selected);
                    entities.get(position).isSelect = true;
                }
                notifyCount();
            }
        });
        viewHolder.rlySub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = cart.cart.number;
                if (number > 0) {
                    number -= 1;
                    entities.get(position).cart.number = number;
                    viewHolder.tvNumber.setText(number + "");
                    updateCount(entities.get(position).cart.id, number);
                }
                notifyCount();
            }
        });
        viewHolder.rlyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = cart.cart.number;
                number += 1;
                entities.get(position).cart.number = number;
                viewHolder.tvNumber.setText(number + "");
                updateCount(entities.get(position).cart.id, number);

                notifyCount();
            }
        });

        return convertView;
    }

    public void addAll(ArrayList<CartListEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (int i = 0; i < entities.size(); ++i) {
            entities.get(i).isSelect = true;
        }
        notifyDataSetChanged();
        notifyCount();
    }

    public ArrayList<CartListEntity> getAll() {
        return entities;
    }

    public void selectNone() {
        for (int i = 0; i < entities.size(); ++i) {
            entities.get(i).isSelect = false;
        }
        notifyDataSetChanged();
        notifyCount();
    }

    public void deleteSelect() {
        String ids = "";
        Iterator<CartListEntity> cartIterator = entities.iterator();
        while (cartIterator.hasNext()) {
            CartListEntity cart = cartIterator.next();
            if (cart.isSelect) {
                if (!TextUtils.isEmpty(ids)) {
                    ids += ",";
                }
                ids += cart.cart.id;
                cartIterator.remove();
            }
        }
        notifyDataSetChanged();
        if (!TextUtils.isEmpty(ids)) {
            deleteCart(ids);
        }
    }

    public void notifyCount() {
        boolean isAllSelect = true;
        int selectCount = 0;
        float price = 0;
        for (CartListEntity cart : entities) {
            if (!cart.isSelect) {
                isAllSelect = false;
            } else {
                selectCount += 1;
                price += getTypePrice(cart.cart.typeID, cart.goodsPrices) * cart.cart.number;
            }
        }
        activity.notifyCount(isAllSelect, selectCount, price);
    }

    private class ViewHolder {
        ImageView ivSelect, ivIcon;
        TextView tvTitle, tvTypeName, tvPrice, tvNumber;
        RelativeLayout rlySub, rlyAdd;
    }

    private void deleteCart(String ids) {
        Logger.getLogger().d("购物车删除:" + ids);
        UserDataManager dataManager = new UserDataManager();
        dataManager.deleteCart(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<Cart>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(Cart result) {

                    }
                });
    }

    private void updateCount(String id, int number) {
        Logger.getLogger().d("更新购物车商品数量:" + id + "..." + number);

        CartBody body = new CartBody();
        body.id = id;
        body.number = number;

        new UserDataManager().updateCartCount(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<Cart>() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(Cart result) {

                    }
                });
    }

    private float getTypePrice(String typeID, ArrayList<GoodsPrice> goodsPrices) {
        float result = 0;
        for (int i = 0; i < goodsPrices.size(); ++i) {
            if (goodsPrices.get(i).id.equals(typeID)) {
                result = goodsPrices.get(i).price;
            }
        }

        return result;
    }
}
