package com.example.charlie.gradproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.AdapterView;
import android.widget.ListView;

public class AttendFragment extends Fragment implements AdapterView.OnItemClickListener {
	private ListView classAttend;
	private Adapter adapter;

	public AttendFragment() {
		// Required empty public constructor
	}

	public static AttendFragment newInstance() {
		return new AttendFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_attend, container, false);
		//adapter=new Adapter();
		classAttend=view.findViewById(R.id.class_attend);
		classAttend.setAdapter(adapter);
		classAttend.setOnItemClickListener(this);
		return view;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}
