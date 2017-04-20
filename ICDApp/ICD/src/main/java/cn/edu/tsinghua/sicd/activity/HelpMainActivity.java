package cn.edu.tsinghua.sicd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.tsinghua.sicd.R;


/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 主界面
 */
public class HelpMainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		setContentView(R.layout.activity_help_main);

		ImageView btnBack=(ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		TextView btn1=(TextView)findViewById(R.id.helpFirst);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(HelpMainActivity.this, HelpActivity.class);

				startActivity(intent);
			}
		});

		TextView btn2=(TextView)findViewById(R.id.helpSecond);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(HelpMainActivity.this, TotalHelp2Activity.class);
				intent.putExtra("index",0);
				startActivity(intent);
			}
		});



		TextView tvTitle=(TextView)findViewById(R.id.topTv);
		tvTitle.setText("Help Home");

	}



}
