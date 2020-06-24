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
import com.community.customer.api.EmptyEntity;
import com.community.customer.api.user.Cart;
import com.community.customer.api.user.CountEntity;
import com.community.customer.api.user.UserDataManager;
import com.community.customer.mine.ShoppingCartActivity;
import com.community.support.utils.ReportUtil;

import java.util.ArrayList;
import java.util.Iterator;

import cn.wdcloud.acaeva.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CartAdapter extends BaseAdapter {
    private ShoppingCartActivity activity;
    private ArrayList<Cart> entities = new ArrayList<>();

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

        final Cart cart = entities.get(position);

        ImageLoader.normal(activity, cart.icon, R.drawable.default_image_white, viewHolder.ivIcon);
        viewHolder.tvTitle.setText(cart.title);
        if (TextUtils.isEmpty(cart.typeName)) {
            viewHolder.tvTypeName.setVisibility(View.GONE);
        } else {
            viewHolder.tvTypeName.setVisibility(View.VISIBLE);
            viewHolder.tvTypeName.setText(cart.typeName);
        }
        viewHolder.tvPrice.setText("¥ " + cart.typePrice);
        viewHolder.tvNumber.setText(cart.number + "");

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
                int number = cart.number;
                if (number > 0) {
                    number -= 1;
                    entities.get(position).number = number;
                    viewHolder.tvNumber.setText(number + "");
                    updateCount(entities.get(position).id, number);
                }
                notifyCount();
            }
        });
        viewHolder.rlyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = cart.number;
                number += 1;
                entities.get(position).number = number;
                viewHolder.tvNumber.setText(number + "");
                updateCount(entities.get(position).id, number);

                notifyCount();
            }
        });

        return convertView;
    }

    public void addAll(ArrayList<Cart> entities) {
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

    public ArrayList<Cart> getAll() {
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
        Iterator<Cart> cartIterator = entities.iterator();
        while (cartIterator.hasNext()) {
            Cart cart = cartIterator.next();
            if (cart.isSelect) {
                if (!TextUtils.isEmpty(ids)) {
                    ids += ",";
                }
                ids += cart.id;
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
        for (Cart cart : entities) {
            if (!cart.isSelect) {
                isAllSelect = false;
            } else {
                selectCount += 1;
                price += cart.typePrice * cart.number;
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
                .subscribe(new Observer<CountEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("购物车删除：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CountEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("购物车删除，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("购物车删除, result为空");
                                return;
                            }

                            int count = result.data.getCountInt();
                        }
                    }
                });
    }

    private void updateCount(String id, int number) {
        Logger.getLogger().d("更新购物车商品数量:" + id + "..." + number);
        UserDataManager dataManager = new UserDataManager();
        dataManager.updateCartCount(id, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmptyEntity>() {

                    @Override
                    public void onError(Throwable e) {
                        ReportUtil.reportError(e);
                        Logger.getLogger().e("更新购物车商品数量：" + e.getMessage());
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
                            Logger.getLogger().e("更新购物车商品数量，msgCode：" + result.errCode + "/n" + result.message);
                        }
                    }
                });
    }
}
