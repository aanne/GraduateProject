package com.example.charlie.gradproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DbAdapter extends BaseAdapter{

    private Context context;
    private List<Order> orderList;

    public DbAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }
    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Order order = orderList.get(i);
        if (order == null){
            return null;
        }
        ViewHolder holder = null;
        if (view != null){
            holder = (ViewHolder) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.db_adapter_item, null);
            holder = new ViewHolder();
            holder.Id = (TextView) view.findViewById(R.id.Id);
            holder.Name = (TextView) view.findViewById(R.id.Name);
            holder.Date = (TextView) view.findViewById(R.id.Date);
            view.setTag(holder);
        }
        holder.Id.setText(order.id + "");
        holder.Name.setText(order.name);
        holder.Date.setText(order.date);

        return view;
    }

    public static class ViewHolder{
        public TextView Id;
        public TextView Name;
        public TextView Date;
    }
}
