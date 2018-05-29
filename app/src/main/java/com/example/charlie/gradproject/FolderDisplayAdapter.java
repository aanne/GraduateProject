package com.example.charlie.gradproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class FolderDisplayAdapter extends BaseAdapter {
    private static final String TAG="FolderDisplayAdapter";
    List<HomeworkFragment.ItemHomework> list;
    Context context;
    LayoutInflater inflater;

    public FolderDisplayAdapter(List<HomeworkFragment.ItemHomework> list, Context context){
        this.list=list;
        this.context=context;
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
        HolderOuter h;
        final int outerPosition=i;

        if (view == null) {
            h=new HolderOuter();
            view= inflater.inflate(R.layout.homework_items, viewGroup, false);
            h.itemText=view.findViewById(R.id.classes);
            h.time=view.findViewById(R.id.dateExists);
            view.setTag(h);
        }else h=(HolderOuter)view.getTag();
        h.itemText.setText(list.get(i).className);
        AdapterInner adapterInner=new AdapterInner(context,list.get(i).timeList,list.get(i).pathList);
        h.time.setAdapter(adapterInner);
        h.time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(context,MarkActivity.class);
                intent.putExtra("folder_path",list.get(outerPosition).pathList.get(i));
                Log.w(TAG,"path selected"+list.get(outerPosition).pathList.get(i));
                context.startActivity(intent);
            }
        });
        return view;
    }


    class HolderOuter{
        TextView itemText;
        ListView time;
    }

    class AdapterInner extends BaseAdapter{

        List<String> timeList;
        List<String> pathList;

        private LayoutInflater inflater;

        public AdapterInner(Context context, List<String> timeList,List<String> pathList){
            this.timeList=timeList;
            this.pathList=pathList;
            this.inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return timeList.size();
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
            HolderInner h;
            if (view == null) {
                h=new HolderInner();
                view= inflater.inflate(R.layout.name_item, viewGroup, false);
                h.dateItem=view.findViewById(R.id.class_item);
                view.setTag(h);
            }else h=(HolderInner)view.getTag();
            h.dateItem.setText(timeList.get(i));
            return view;
        }
    }

    class HolderInner{
        TextView dateItem;
    }
}
