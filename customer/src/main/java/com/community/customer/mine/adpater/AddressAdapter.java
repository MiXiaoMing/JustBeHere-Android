package com.community.customer.mine.adpater;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.wdcloud.acaeva.R;

import com.community.customer.api.user.AddressEntity;
import com.community.customer.mine.AddressListActivity;
import com.community.customer.mine.AddressSettingActivity;

import java.io.Serializable;
import java.util.ArrayList;


public class AddressAdapter extends BaseAdapter {
    private AddressListActivity activity;
    private ArrayList<AddressEntity> entities = new ArrayList<>();

    public AddressAdapter(AddressListActivity activity) {
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
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_address, null);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvRegion = convertView.findViewById(R.id.tvRegion);
            viewHolder.tvCellphone = convertView.findViewById(R.id.tvCellphone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(entities.get(position).contact);
        viewHolder.tvRegion.setText(entities.get(position).region);
        viewHolder.tvCellphone.setText(entities.get(position).phoneNumber);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddressSettingActivity.class);
                intent.putExtra("type", "U");
                intent.putExtra("address", (Serializable)entities.get(position));
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addAll(ArrayList<AddressEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvName, tvRegion, tvCellphone;
    }
}
