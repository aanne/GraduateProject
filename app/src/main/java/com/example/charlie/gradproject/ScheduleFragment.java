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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ScheduleFragment extends Fragment {
	private final static String TAG = ScheduleFragment.class.getSimpleName();
	private SharedPreferences s;
	private boolean i;//oncheck state
	private int id;
	private Context context;
	private ListView schedule;
	private Switch setClockSwitch;
	private Adapter adapter;

	private Map<String,List> scheduleMap;
	private List<Integer> list;
	private TextView dateDisplay;

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

	//inflate view & findviewbyid
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_schedule, container, false);
		schedule=view.findViewById(R.id.schedule);
		dateDisplay=view.findViewById(R.id.dateDisplay);
		setClockSwitch=view.findViewById(R.id.set_clock_switch);
		dateDisplay.setText(getDate());
		return view;
	}

	private String getDate(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日，EEEE");// HH:mm:ss
//获取当前时间
		Date date = new Date(System.currentTimeMillis());
		return "今天是"+simpleDateFormat.format(date);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		s = getActivity().getSharedPreferences("info", 0);
		i = s.getBoolean("open", false);
		setClockSwitch.setChecked(i);
		setAdapter();
		schedule.setAdapter(adapter);
		id=0;

		if(setClockSwitch.isChecked()) {
			open();
		} else close();

		setClockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor=s.edit();
				if (isChecked) {
					editor.putBoolean("open",true);
					open();
				} else{
					editor.putBoolean("open",false);
					close();
				}
				editor.apply();
			}
		});
	}

	//set alarm
	private void open() {
		setClock("17:30", 5);//hardcode time
	}
	//close alarm
	private void close() {
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private void setAdapter(){
		Item class_1,class_2,class_3,class_4,class_5,class_6;
		class_1=new Item("1","1+2+3","A","6101");
		class_2=new Item("1","6+7","A","6101");
		class_3=new Item("2","1+2+3","B","7101");
		class_4=new Item("2","6+7","B","7101");
		class_5=new Item("4","6+7","C","6201");
		class_6=new Item("5","1+2","D","7201");//should be done in reading csv

		DayList.Item2 a,b,c,d;
		a=new DayList.Item2("1",class_1,class_2);//should be classfied automatically
		b=new DayList.Item2("2",class_3,class_4);
		c=new DayList.Item2("4",class_5);
		d=new DayList.Item2("5",class_6);

		DayList list=new DayList();
		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);

		adapter=new Adapter(context,list);
	}


	public void setClock(String t, int d) {
		String[] times = t.split(":");
		AlarmManagerUtil.setAlarm(context, 2, Integer.parseInt(times[0]), Integer
				.parseInt(times[1]), id, d, "闹钟响了", 0);
		id++;//set clock id
	}
}
