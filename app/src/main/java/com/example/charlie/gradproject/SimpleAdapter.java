package com.example.charlie.gradproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SimpleAdapter extends BaseAdapter {
    List<String> list;
    LayoutInflater inflater;

    public SimpleAdapter(Context context, List<String> list){
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder h;
        if (view == null) {
            h=new Holder();
            view = inflater.inflate(R.layout.name_item, viewGroup, false);
            h.textDisplay=view.findViewById(R.id.class_item);
            view.setTag(h);
        }else h=(Holder)view.getTag();
        h.textDisplay.setText(list.get(i));
        return view;
    }

    class Holder{
        TextView textDisplay;
    }
}
