package com.example.charlie.gradproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Adapter extends BaseAdapter {
	private LayoutInflater inflater;
	private DayList list;
	private Context mcontext;

	public Adapter(Context context,DayList list){
		mcontext=context;
		this.inflater=LayoutInflater.from(context);
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h;
		if (convertView == null) {
			h=new Holder();
			convertView = inflater.inflate(R.layout.weekday_item, parent, false);
			h.day=convertView.findViewById(R.id.day);
			h.weekDay=convertView.findViewById(R.id.class_per_weekday);
			convertView.setTag(h);
		} else h = (Holder) convertView.getTag();
		Adapter2 adapter=new Adapter2(mcontext,list.get(position).list);
		h.day.setText(getDay(Integer.parseInt(list.get(position).week)));
		h.weekDay.setAdapter(adapter);
		return convertView;
	}

	private String getDay(int d){
		String day = null;
		switch (d){
			case 1:
				day="星期一";
				break;
			case 2:
				day="星期二";
				break;

			case 3:
				day="星期三";
				break;

			case 4:
				day="星期四";
				break;

			case 5:
				day="星期五";
				break;

			case 6:
				day="星期六";
				break;

			case 7:
				day="星期天";
				break;
		}
		return day;
	}

	static class Holder{
		ListView weekDay;
		TextView day;
	}

	class Adapter2 extends BaseAdapter{
		List<Item> mlist;
		private LayoutInflater inflater;
		public Adapter2(Context context, List<Item> list){
			this.mlist=list;
			this.inflater=LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder h;
			if (convertView == null) {
				h=new Holder();
				convertView = inflater.inflate(R.layout.schedule_item, parent, false);
				h.classtime=convertView.findViewById(R.id.classtime);
				h.classname=convertView.findViewById(R.id.classname);
				h.classlocation=convertView.findViewById(R.id.classlocation);
				convertView.setTag(h);
			} else h = (Holder) convertView.getTag();
			h.classtime.setText(mlist.get(position).time);
			h.classname.setText(mlist.get(position).classn);
			h.classlocation.setText(mlist.get(position).location);
			return convertView;
		}

		class Holder{
			TextView classtime,classlocation,classname;
		}
	}
}
