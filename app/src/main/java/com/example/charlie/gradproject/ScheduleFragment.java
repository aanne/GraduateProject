package com.example.charlie.gradproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;

public class ScheduleFragment extends Fragment {
	private final static String TAG=ScheduleFragment.class.getSimpleName();
	private SharedPreferences s;
	private boolean i;
	private int cycle;
	private Context context;

	public ScheduleFragment() {
	}

	public static ScheduleFragment newInstance() {
		ScheduleFragment fragment = new ScheduleFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_schedule, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		s=getActivity().getSharedPreferences("info",0);
		i=s.getBoolean("open",false);
		Switch open=(Switch)getActivity().findViewById(R.id.open);
		if(open.isChecked()){
			open();
		}else close();

		open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					open();
				}else close();
			}
		});
		Log.d(TAG, "onActivityCreated");
	}

	private void open(){
		setClock("17:30",5);
	}

	private void close(){

	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context=context;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public static String parseRepeat(int repeat, int flag) {
		String cycle = "";
		String weeks = "";
		if (repeat == 0) {
			repeat = 127;
		}
		if (repeat % 2 == 1) {
			cycle = "周一";
			weeks = "1";
		}
		if (repeat % 4 >= 2) {
			if ("".equals(cycle)) {
				cycle = "周二";
				weeks = "2";
			} else {
				cycle = cycle + "," + "周二";
				weeks = weeks + "," + "2";
			}
		}
		if (repeat % 8 >= 4) {
			if ("".equals(cycle)) {
				cycle = "周三";
				weeks = "3";
			} else {
				cycle = cycle + "," + "周三";
				weeks = weeks + "," + "3";
			}
		}
		if (repeat % 16 >= 8) {
			if ("".equals(cycle)) {
				cycle = "周四";
				weeks = "4";
			} else {
				cycle = cycle + "," + "周四";
				weeks = weeks + "," + "4";
			}
		}
		if (repeat % 32 >= 16) {
			if ("".equals(cycle)) {
				cycle = "周五";
				weeks = "5";
			} else {
				cycle = cycle + "," + "周五";
				weeks = weeks + "," + "5";
			}
		}
		if (repeat % 64 >= 32) {
			if ("".equals(cycle)) {
				cycle = "周六";
				weeks = "6";
			} else {
				cycle = cycle + "," + "周六";
				weeks = weeks + "," + "6";
			}
		}
		if (repeat / 64 == 1) {
			if ("".equals(cycle)) {
				cycle = "周日";
				weeks = "7";
			} else {
				cycle = cycle + "," + "周日";
				weeks = weeks + "," + "7";
			}
		}

		return flag == 0 ? cycle : weeks;
	}

	public void setClock(String t,int d) {
		cycle = setDay(d);//hardcode weekday
		//hardcode time
		String[] times=t.split(":");
		String weeksStr = parseRepeat(cycle, 1);
		String[] weeks = weeksStr.split(",");
		for (int i = 0; i < weeks.length; i++) {
			AlarmManagerUtil.setAlarm(context, 2, Integer.parseInt(times[0]), Integer
					.parseInt(times[1]), i, Integer.parseInt(weeks[i]), "闹钟响了", 0);
		}
	}

	public int setDay(int d){
		int day = 1;
		switch (d){
			case 1:
				day=1;
				break;
			case 2:
				day=2;
				break;
			case 3:
				day=4;
				break;
			case 4:
				day=8;
				break;
			case 5:
				day=16;
				break;
			case 6:
				day=32;
				break;
			case 7:
				day=64;
				break;
		}
		return day;
	}
}
