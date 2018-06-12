package com.example.charlie.gradproject;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    Button searchDate;
    EditText inputDate;

    int version;
    String rowDate;
    String changeDate;

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

        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        Date d=new Date(System.currentTimeMillis());
        monthPlusDay=format.format(d);
        buttons=view.findViewById(R.id.buttons);
        buttons.setVisibility(View.INVISIBLE);
        inputDate=view.findViewById(R.id.input_date);
      //  createNew=view.findViewById(R.id.create);
        //createNew.setOnClickListener(this);
        searchDate =view.findViewById(R.id.search_date);
        searchDate.setOnClickListener(this);
        info = view.findViewById(R.id.content);
        classAttend = view.findViewById(R.id.class_attend);
        view_class = view.findViewById(R.id.view_class);
        listHeader=view.findViewById(R.id.header_view);
        listHeader.setVisibility(View.INVISIBLE);
        dateView=view.findViewById(R.id.date_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences s=getActivity().getSharedPreferences("info", 0);
        version=s.getInt("version",1);
        operator = new Operator(context,version);
        //operator.update();
        //operator.initTable();    //rewrite data to hardcode
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
            view_class.setText(selectedClass + "的名单");//点击课程名默认显示今天名单
            rowDate=getDate();

            if(operator.isRowExists(selectedClass,rowDate)){//如果今天名单已存在则显示
                Log.w(TAG,"date exists");
                setContentAdapter();
                //list_adapter.notifyDataSetChanged();
            }else{//升级数据库，保留原始数据并添加列，新列内容""
                Log.w(TAG,"need to add row");
                String[] newColumnArr= new String[1];
                newColumnArr[0]=rowDate;
                operator.addNewColumn(selectedClass,newColumnArr);
//              operator=new Operator(context,3);
                Log.w(TAG,Arrays.toString(operator.getAllColumn(selectedClass)));
                Log.w(TAG,"exists?"+operator.isRowExists(selectedClass,rowDate));
                SharedPreferences.Editor editor=getActivity().getSharedPreferences("info", 0).edit();
                editor.putInt("version",version+1);
                editor.apply();
                name_adapter.notifyDataSetChanged();
                setContentAdapter();
            }


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
                default:
                    status="到课";
                    break;
            }
            operator.changeStatus(selectedClass,changeDate,status, order.id);
            updateDb();
        }
    }

    private void setContentAdapter(){
        dateView.setText(monthPlusDay);
        changeDate=rowDate;
        selectedClassContent = operator.getAllDate(selectedClass,rowDate);  //获得选择班级的数据表中的全部数据
        list_adapter = new DbAdapter(context, selectedClassContent);
        info.setAdapter(list_adapter);
        info.setOnItemClickListener(this);
    }

    // 注意不要直接赋值，如：orderList = ordersDao.getAllDate() 此时相当于重新分配了一个内存 原先的内存没改变 所以界面不会有变化
    // Java中的类是地址传递 基本数据才是值传递
    private void updateDb(){
        selectedClassContent.clear();
        selectedClassContent.addAll(operator.getAllDate(selectedClass,changeDate));
        list_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
       if(view== searchDate){
            String d="date"+inputDate.getText().toString();
            Log.w(TAG,"input date:"+d);
            if(operator.isRowExists(selectedClass,d)){
                changeDate=d;
                dateView.setText(inputDate.getText().toString());
                selectedClassContent.clear();
                selectedClassContent.addAll(operator.getAllDate(selectedClass,d));
                list_adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(context,"没有该日期记录",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return "date"+simpleDateFormat.format(date);
    }
}
