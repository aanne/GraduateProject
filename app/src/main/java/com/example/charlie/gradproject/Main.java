package com.example.charlie.gradproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends AppCompatActivity {
	private File dir, fle;
	private String a = "a";
	private String b = ".txt";
	private FileWriter writer;
	private final static String TAG = Main.class.getSimpleName();
	private Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setData();
		setFragment();
	}


	private void setData(){
		ActivityCompat.requestPermissions(Main.this, new String[]{android
				.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


		dir = new File(Environment.getExternalStorageDirectory() + File.separator
				+ this.getResources().getString(R.string.folder_name));

		if (!dir.exists()) {
			ActivityCompat.requestPermissions(Main.this, new String[]{android
					.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

			boolean c = dir.mkdirs();
			Log.w(TAG, "result" + " " + c);
			fle = new File(dir.getAbsoluteFile() + File.separator + a + b);
			try {
				fle.createNewFile();
				writer = new FileWriter(fle);
				writer.write("周几,时间,课程号,地点");
				writer.write("\n" + "1,1+2+3,A,6101");
				writer.write("\n" + "1,6+7,B,6101");
				writer.write("\n" + "2,1+2+3,A,7101");
				writer.write("\n" + "2,6+7,B,7101");
				writer.write("\n" + "4,6+7,C,6201");
				writer.write("\n" + "5,1+2,D,7201");
				writer.close();
			} catch (IOException e) {//todo: create new folder for homework csv file
				e.printStackTrace();
			}
			notifySystemToScan(fle);
		}
	}

	public void notifySystemToScan(File filePath){
		intent.setData(Uri.fromFile(filePath));
		sendBroadcast(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 1:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					/*boolean c = dir.mkdirs();
					Log.w(TAG, "result1" + " " + c);*/
				}
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("确认");
		builder.setMessage("确定退出本应用？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Main.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}


	public void setFragment(){
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frameLayout, ScheduleFragment.newInstance());
		fragmentTransaction.commit();
		BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				Fragment fragment = null;
				switch (item.getItemId()) {
					case R.id.action_scheduel:
						fragment = ScheduleFragment.newInstance();
						break;
					case R.id.action_homework:
						fragment = HomeworkFragment.newInstance();
						break;
					case R.id.action_attend:
						fragment = AttendFragment.newInstance();
						break;
				}
				if (fragment != null) {
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.replace(R.id.frameLayout, fragment);
					fragmentTransaction.commit();
				}
				return true;
			}
		});
	}
}
