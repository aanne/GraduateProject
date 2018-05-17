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

import java.util.List;

public class AttendFragment extends Fragment implements AdapterView.OnItemClickListener {
	private ListView classAttend;
	Operator operator;
	Context context;
	private DbAdapter adapter;
	private List<Order> orderList;

	public AttendFragment() {
	}

	public static AttendFragment newInstance() {
		return new AttendFragment();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context=context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_attend, container, false);

		classAttend=view.findViewById(R.id.class_attend);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		operator=new Operator(context);
		if (! operator.isDataExist()){
			operator.initTable();
		}
		orderList = operator.getAllDate();
		if (orderList != null){
			adapter = new DbAdapter(context, orderList);
			classAttend.setAdapter(adapter);
			classAttend.setOnItemClickListener(this);
		}

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}
