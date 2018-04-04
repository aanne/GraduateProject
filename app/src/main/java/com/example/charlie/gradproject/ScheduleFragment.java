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

public class ScheduleFragment extends Fragment {
	private final static String TAG=ScheduleFragment.class.getSimpleName();
	private SharedPreferences s;
	private boolean i;

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

	}

	private void close(){

	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}
