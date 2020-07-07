package com.community.customer.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;

import com.community.customer.api.user.result.ServerOrderListEntity;
import com.community.customer.common.Constants;
import com.community.customer.order.ServerOrderDetailActivity;

import java.util.ArrayList;


public class ServerOrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ServerOrderListEntity> entities = new ArrayList<>();

    public ServerOrderAdapter(Context context) {
        this.context = context;
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
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_server_order, null);
            viewHolder.tvServerName = convertView.findViewById(R.id.tvServerName);
            viewHolder.tvStatus = convertView.findViewById(R.id.tvStatus);
            viewHolder.tvServerTime = convertView.findViewById(R.id.tvServerTime);
            viewHolder.tvAddress = convertView.findViewById(R.id.tvAddress);
            viewHolder.llyPayPrice = convertView.findViewById(R.id.llyPayPrice);
            viewHolder.tvPayPrice = convertView.findViewById(R.id.tvPayPrice);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvServerName.setText(entities.get(position).serviceOrder.serviceName);
        viewHolder.tvServerTime.setText(entities.get(position).serviceOrder.serviceTime);
        viewHolder.tvAddress.setText(entities.get(position).deliveryAddress.region);

        String status = entities.get(position).order.status;
        viewHolder.tvStatus.setText(Constants.convertServerOrderStatus(status));
        viewHolder.tvStatus.setTextColor(Constants.getServerStatusColor(status));
        if (status.equals("01")) {
            viewHolder.llyPayPrice.setVisibility(View.VISIBLE);
            viewHolder.tvPayPrice.setText(entities.get(position).serviceOrder.payPrice + "元");
        } else {
            viewHolder.llyPayPrice.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServerOrderDetailActivity.class);
                intent.putExtra("orderID", entities.get(position).serviceOrder.id);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addAll(ArrayList<ServerOrderListEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvServerName, tvStatus, tvServerTime, tvAddress, tvPayPrice;
        LinearLayout llyPayPrice;
    }
}
