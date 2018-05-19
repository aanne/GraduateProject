package com.example.charlie.gradproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttendFragment extends Fragment implements AdapterView.OnItemClickListener ,View.OnClickListener{
    final static String TAG = "AttendFragment";

    Operator operator;
    Context context;
    private DbAdapter list_adapter;
    NameAdapter name_adapter;
    private List<Order> orderList;
    String monthPlusDay;
    String selectedClass;
    List<Order> selectedClassContent;

    private ListView classAttend;
    ListView info;
    TextView view_class;
    LinearLayout listHeader,buttons;
    TextView dateView;
    Button createNew, searchDate;
    EditText inputDate;


    public AttendFragment() {
    }

    public static AttendFragment newInstance() {
        return new AttendFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attend, container, false);

        SimpleDateFormat format=new SimpleDateFormat("yyyy_MM_dd");
        Date d=new Date(System.currentTimeMillis());
        monthPlusDay=format.format(d);

        buttons=view.findViewById(R.id.buttons);
        buttons.setVisibility(View.INVISIBLE);
        inputDate=view.findViewById(R.id.input_date);
        createNew=view.findViewById(R.id.create);
        createNew.setOnClickListener(this);
        searchDate =view.findViewById(R.id.search_date);
        searchDate.setOnClickListener(this);
        info = view.findViewById(R.id.content);
        classAttend = view.findViewById(R.id.class_attend);
        view_class = view.findViewById(R.id.view_class);
        listHeader=view.findViewById(R.id.header_view);
        listHeader.setVisibility(View.INVISIBLE);
        dateView=view.findViewById(R.id.date_view);
        dateView.setText(monthPlusDay);
  //      dateView=view.findViewById(R.id.date_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        operator = new Operator(context);
        operator.update();
        operator.initTable();    //rewrite data to hardcode
        name_adapter = new NameAdapter(context, operator.getName());
        classAttend.setAdapter(name_adapter);
        classAttend.setOnItemClickListener(this);


		/*if (! operator.isDataExist()){
			operator.initTable();
		}
		*/

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.w(TAG, view + "");
        if (parent.getId() == R.id.class_attend) {
            Log.w(TAG, "touched class name");
            listHeader.setVisibility(View.VISIBLE);
            buttons.setVisibility(View.VISIBLE);
            selectedClass = operator.getName().get(position);
            view_class.setText(selectedClass + "的名单");
            selectedClassContent = operator.getAllDate(selectedClass);  //获得选择班级的数据表中的全部数据
            list_adapter = new DbAdapter(context, selectedClassContent);
            info.setAdapter(list_adapter);
            info.setOnItemClickListener(this);
            list_adapter.notifyDataSetChanged();

        } else if (parent.getId() == R.id.content) {
            Log.w(TAG, "touched content");
            Order order = selectedClassContent.get(position);
            String status=order.date;
            switch (status){
                case "到课":
                    status="请假";
                    break;
                case "请假":
                    status="缺席";
                    break;
                case "缺席":
                    status="到课";
                    break;
            }
            operator.changeStatus(selectedClass, status, order.id);
            updateDb();
        }
    }


    private void updateDb(){
        // 注意：千万不要直接赋值，如：orderList = ordersDao.getAllDate() 此时相当于重新分配了一个内存 原先的内存没改变 所以界面不会有变化
        // Java中的类是地址传递 基本数据才是值传递
        selectedClassContent.clear();
        selectedClassContent.addAll(operator.getAllDate(selectedClass));
        list_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if(view==createNew){
            operator.addNewColumn(selectedClass,"date"+monthPlusDay);
            //operator.update();
        }else if(view== searchDate){
            String d=inputDate.getText().toString();
            Log.w(TAG,"input date:"+d);
            List<Order> a=operator.searchColumn(selectedClass,d);
            if(a!=null){
                selectedClassContent.clear();
                selectedClassContent.addAll(a);
                list_adapter.notifyDataSetChanged();
            }
        }
    }
}
