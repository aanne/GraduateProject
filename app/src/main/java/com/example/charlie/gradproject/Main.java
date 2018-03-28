package com.example.charlie.gradproject;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class Main extends AppCompatActivity {
 private final static String TAG= Main.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
						Log.i(TAG,"scheduel item selected");
						break;
					case R.id.action_homework:
						fragment = HomeworkFragment.newInstance();
						Log.i(TAG,"homework item selected");
						break;
					case R.id.action_attend:
						fragment = AttendFragment.newInstance();
						Log.i(TAG,"attend item selected");
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
