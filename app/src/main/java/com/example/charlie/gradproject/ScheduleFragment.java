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
//	private List<Integer> list;
	private TextView dateDisplay;
	DayList list;

	public ScheduleFragment() {
	}

	public static ScheduleFragment newInstance() {
		ScheduleFragment fragment = new ScheduleFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list=new DayList();
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
		Log.e(TAG,"setAdapter");
		setAdapter();
		schedule.setAdapter(adapter);
		id=2;

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
		setClock("20:03", 1,"test");//hardcode time
		for(int i=0;i<list.size();i++){
			int day=Integer.parseInt(list.get(i).week);
			for(int e=0;e<list.get(i).list.size();e++){
				setClock(setTime(list.get(i).list.get(e).time),day,list.get(i).list.get(e).classn+"即将开始，地点为"
						+list.get(i).list.get(e).location);
				Log.w(TAG,"星期"+day+" "+setTime(list.get(i).list.get(e).time)+"教室:"+list.get(i).list.get(e).location);
			}
		}
	}
	//close alarm
	private void close() {
		for(int i=2;i<id;i++){
			AlarmManagerUtil.cancelAlarm(context,i);
			Log.w(TAG,"id:"+i);
		}
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


		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);

		adapter=new Adapter(context,list);
	}

	private String setTime(String classes){
		String time="";
		switch (classes.substring(0,1)) {
			case "1":
				//time="8:05";
				time="22:12";
				break;
			case "2":
				time="8:55";
				break;
			case "3":
				time="9:45";
				break;
			case "4":
				time="10:00";
				break;
			case "5":
				time="10:50";
				break;
			case "6":
				time="13:30";
				break;
			case "7":
				time="14:20";
				break;
			case "8":
				time="15:10";
				break;
			case "9":
				time="16:00";
				break;
			default:
				time="19:00";
		}
		return time;
	}


	public void setClock(String t, int d,String info) {
		String[] times = t.split(":");
		Log.w(TAG,"id:"+id);
		AlarmManagerUtil.setAlarm(context, 2, Integer.parseInt(times[0]), Integer
				.parseInt(times[1]), id, d, info, 2);
		id++;
	}
}
