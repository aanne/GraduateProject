package com.example.charlie.gradproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MarkActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button A, B, C, D;
    String folderPath;
    List<String> homeworkNames;
    ListView homeworkList;
    SimpleAdapter adapter;
    String finalPath;
    String filename;

    TextView textView;
    ImageView imageView;
    Bitmap imageBitmap;
    File selectedFile;

    private static final String TAG = "MarkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initWidgets();
        folderPath = getPath();
        File file = new File(folderPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                homeworkNames.add(files[i].getName());
            }
            Log.w(TAG, "names:" + homeworkNames);
            adapter = new SimpleAdapter(this, homeworkNames);
            homeworkList.setAdapter(adapter);
        }
    }

    private void initWidgets() {
        filename = null;
        homeworkNames = new ArrayList<String>();
        homeworkList = findViewById(R.id.homeworkList);
        homeworkList.setOnItemClickListener(this);
        A = findViewById(R.id.gradeA);
        B = findViewById(R.id.gradeB);
        C = findViewById(R.id.gradeC);
        D = findViewById(R.id.gradeD);
        A.setOnClickListener(this);
        B.setOnClickListener(this);
        C.setOnClickListener(this);
        D.setOnClickListener(this);
        imageView = findViewById(R.id.view_image);
        textView = findViewById(R.id.view_text);
        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    private String getPath() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("folder_path");
        Log.w(TAG, path);
        return path;
    }

    @Override
    public void onClick(View view) {
        String grade = "";
        if (view == A) {
            grade = "A";
        } else if (view == B) {
            grade = "B";
        } else if (view == C) {
            grade = "C";
        } else if (view == D) {
            grade = "D";
        }
        if (filename != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("确认");
            builder.setMessage("确定将" +filename+"打分为"+grade+",并将文件删除？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(deleteSelectedFile(selectedFile)){
                        scanFiles();
                        Toast.makeText(MarkActivity.this, "文件"+filename+"已打分并删除", Toast.LENGTH_SHORT).show();
                        selectedFile=null;
                        imageView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                    }
                    dialog.dismiss();
                }
            });
            //设置反面按钮
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MarkActivity.this, "取消打分", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(MarkActivity.this, "没有选择文件", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanFiles(){
        homeworkNames.clear();
        File file = new File(folderPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                homeworkNames.add(files[i].getName());
            }
            Log.w(TAG, "names:" + homeworkNames);
        }
        adapter.notifyDataSetChanged();

    }

    private boolean deleteSelectedFile(File f){
        return f.delete();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        filename = homeworkNames.get(i);
        String suffix = homeworkNames.get(i).substring(filename.lastIndexOf(".") + 1);
        finalPath = folderPath + File.separator + filename;
        selectedFile = new File(finalPath);
        switch (suffix) {
            case "txt":
                if (selectedFile.exists()) {
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), "GB2312"));
                        String strLine = reader.readLine();
                        //String temp1 = EncodingUtils.getString(strLine.getBytes(),"GB2312");
                        //String temp2 = EncodingUtils.getString(strLine.getBytes("utf-8"),"utf-8");
                        String temp3 = EncodingUtils.getString(strLine.getBytes(), "utf-8");
                        textView.setText(temp3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "jpg":
            case "jpeg":
                imageBitmap = BitmapFactory.decodeFile(finalPath);
                imageView.setImageBitmap(imageBitmap);
                textView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                break;

        }
    }
}
