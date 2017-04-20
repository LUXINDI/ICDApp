package cn.edu.tsinghua.sicd.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.tsinghua.sicd.R;


/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 主界面
 */
public class TestActivity extends Activity {

	private Button btnTest;
	private EditText editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


		setContentView(R.layout.layout_test);

		btnTest=(Button)findViewById(R.id.btnTest);
		editText=(EditText)findViewById(R.id.myEditText);

		btnTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editText.setText("Hello, world!");
			}
		});






	}



}
