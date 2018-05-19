package com.example.charlie.gradproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class NameAdapter extends BaseAdapter {
    List<String> names;
    Context context;

    public NameAdapter(Context context, List<String> names){
        this.context=context;
        this.names=names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view != null){
            holder = (ViewHolder) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.name_item, null);
            holder = new ViewHolder();
            holder.class_name=view.findViewById(R.id.class_item);
            view.setTag(holder);
        }
        holder.class_name.setText(names.get(i));
        return view;
    }

    public class ViewHolder{
        TextView class_name;
    }
}
