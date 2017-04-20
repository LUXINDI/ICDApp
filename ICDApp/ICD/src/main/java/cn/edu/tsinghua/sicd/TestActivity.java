package cn.edu.tsinghua.sicd;

import android.os.Bundle;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaActivity;


/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 主界面
 */
public class TestActivity extends CordovaActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


        loadUrl("file:///android_asset/www/index.html");

	}



}
