package com.example.charlie.gradproject;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeworkFragment extends Fragment implements View.OnClickListener {
	String TAG="HomeworkFragment";
	Button scan;
	ListView dateList;
	private String baseDirectory;
	File date;
	FolderDisplayAdapter adapter;

	public HomeworkFragment() {
	}

	public static HomeworkFragment newInstance() {
		return new HomeworkFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_homework, container, false);
		scan=view.findViewById(R.id.scan_local);
		dateList=view.findViewById(R.id.date_select);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		baseDirectory= Environment.getExternalStorageDirectory() + File.separator + this.getResources().getString(R.string.folder_name)+File.separator+"homework";
		scan.setOnClickListener(this);
		super.onActivityCreated(savedInstanceState);
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
	public void onClick(View view) {
		if(view==scan){
			File base=new File(baseDirectory);
			if(base.isDirectory()){
				File[] files=base.listFiles();
				List<ItemHomework> fileName=new ArrayList<ItemHomework>();
				for (File file : files) {
					ItemHomework item=new ItemHomework();
					item.className=file.getName();
					//Log.w(TAG,item.className);
					File file2=new File(baseDirectory+File.separator+file.getName());

					if(file2.isDirectory()){
						item.timeList=new ArrayList<String>();
						item.pathList=new ArrayList<String>();
						File[] files2=file2.listFiles();
						for (File aFiles2 : files2) {
							item.timeList.add(aFiles2.getName());
							item.pathList.add(baseDirectory+File.separator+file.getName()+File.separator+aFiles2.getName());
						}
						//Log.w(TAG,item.className+item.timeList);
					}
					fileName.add(item);
				}
				adapter=new FolderDisplayAdapter(fileName,getContext());
				dateList.setAdapter(adapter);
			}
		}
	}

	class ItemHomework{
		List<String> timeList;
		List<String> pathList;
		String className;

	}
}
