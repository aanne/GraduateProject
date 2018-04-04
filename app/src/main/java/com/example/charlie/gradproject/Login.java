package com.example.charlie.gradproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class Login extends AppCompatActivity {

	private Button login;
	private CheckBox rm;
	private EditText ac;
	private EditText ps;
	private SharedPreferences a;
	private SharedPreferences.Editor e;
	private boolean i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login=findViewById(R.id.login);
		rm=findViewById(R.id.remember);
		ac=findViewById(R.id.ac);
		ps=findViewById(R.id.ps);
		setValue();
	}

	@Override
	protected void onPause() {
		super.onPause();
		e.putString("ac",ac.getText().toString());
		if(i) e.putString("ps",ps.getText().toString());
		e.putBoolean("i",i);
		e.commit();
	}

	private void setValue(){
		a=getSharedPreferences("info",0);
		e=a.edit();
		ac.setText(a.getString("ac",null));
		ps.setText(a.getString("ps",null));
		rm.setChecked(a.getBoolean("i",false));
		i = rm.isChecked();
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Login.this,Main.class);
				startActivity(i);
				finish();
			}
		});
		rm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					i=true;
				}else{
					e.putString("ps",null);
					i=false;
				}
			}
		});
	}
}
