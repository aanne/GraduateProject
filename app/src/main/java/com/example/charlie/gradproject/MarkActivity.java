package com.example.charlie.gradproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MarkActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    Button A,B,C,D;
    String folderPath;
    List<String> homeworkNames;
    ListView homeworkList;
    SimpleAdapter adapter;
    String finalPath;

    TextView textView;
    ImageView imageView;

    private static final String TAG="MarkActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initWidgets();
        folderPath=getPath();
        File file=new File(folderPath);
        if(file.isDirectory()){
            File[] files=file.listFiles();
            for(int i=0;i<files.length;i++){
                homeworkNames.add(files[i].getName());
            }
            Log.w(TAG,"names:"+homeworkNames);
            adapter=new SimpleAdapter(this,homeworkNames);
            homeworkList.setAdapter(adapter);
        }
    }

    private void initWidgets(){
        homeworkNames=new ArrayList<String>();
        homeworkList=findViewById(R.id.homeworkList);
        homeworkList.setOnItemClickListener(this);
        A=findViewById(R.id.gradeA);
        B=findViewById(R.id.gradeB);
        C=findViewById(R.id.gradeC);
        D=findViewById(R.id.gradeD);
        A.setOnClickListener(this);
        B.setOnClickListener(this);
        C.setOnClickListener(this);
        D.setOnClickListener(this);
        imageView=findViewById(R.id.view_image);
        textView=findViewById(R.id.view_text);
        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    private String getPath(){
        Intent intent = getIntent();
        String path = intent.getStringExtra("folder_path");
        Log.w(TAG,path);
        return path;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String suffix = homeworkNames.get(i).substring(homeworkNames.get(i).lastIndexOf(".") + 1);
        finalPath=folderPath+File.separator+homeworkNames.get(i);
        File f=new File(finalPath);
        switch (suffix){
            case "txt":
                String result=null;
                if(f.exists()){
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"GB2312"));
                        String strLine= reader.readLine();
                        //String temp1 = EncodingUtils.getString(strLine.getBytes(),"GB2312");
                        //String temp2 = EncodingUtils.getString(strLine.getBytes("utf-8"),"utf-8");
                        String temp3 = EncodingUtils.getString(strLine.getBytes(),"utf-8");
                        textView.setText(temp3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "jpg":
            case "jpeg":

                break;

        }
    }
}
