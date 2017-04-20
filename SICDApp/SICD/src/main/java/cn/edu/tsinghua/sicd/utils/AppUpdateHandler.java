package cn.edu.tsinghua.sicd.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.tsinghua.sicd.webapi.APNApi;


public class AppUpdateHandler extends SimpleThreadHandler{
	private String keyword1;
		private Context context;
	private String TAG="update";
	private UpdateInfo info;
	public final int UPDATA_CLIENT=0;
	public final int GET_UNDATAINFO_ERROR=-1;
	public final int DOWN_ERROR=-2;

		public AppUpdateHandler(Context context){
			 this.context=context;

		}


	@Override
		protected String runInBackend() {

		   String data="";
			try {

				String path = MetaUtils.getWebRoot(context) + "update.xml";

				data=XMLParser.getXmlFromUrl(path);


				return data;
			}catch (Exception ex){
				ex.printStackTrace();
			}


			return data;
		}

	public UpdateInfo getUpdataInfo(InputStream is) throws Exception{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");//设置解析的数据源
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();//实体
		while(type != XmlPullParser.END_DOCUMENT ){
			switch (type) {
				case XmlPullParser.START_TAG:
					if("version".equals(parser.getName())){
						info.setVersion(parser.nextText()); //获取版本号
					}else if ("url".equals(parser.getName())){
						info.setUrl(parser.nextText()); //获取要升级的APK文件
					}else if ("description".equals(parser.getName())){
						info.setDescription(parser.nextText()); //获取该文件的信息
					}
					break;
			}
			type = parser.next();
		}
		return info;
	}

	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new AlertDialog.Builder(context) ;
		builer.setTitle("Upgrade Information");
		builer.setMessage(info.getDescription());
		//当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk,更新");
				downLoadApk();
			}
		});
		//当点取消按钮时进行登录
		builer.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//  LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}


	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取到文件的大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), "SICD_newest.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				//获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case UPDATA_CLIENT:
					//对话框通知用户升级程序
					showUpdataDialog();
					break;
				case GET_UNDATAINFO_ERROR:
					//服务器超时
					Toast.makeText(context.getApplicationContext(), "Update Information Failed to fetch!", Toast.LENGTH_LONG).show();
					//LoginMain();
					break;
				case DOWN_ERROR:
					//下载apk失败
					Toast.makeText(context.getApplicationContext(), "" +
							"Download Failed!", Toast.LENGTH_LONG).show();
					// LoginMain();
					break;
			}
		}
	};

	/*
 * 从服务器中下载APK
 */
	protected void downLoadApk() {
		final ProgressDialog pd;    //进度条对话框
		pd = new  ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("Downloading the newest package...");
		pd.show();
		new Thread(){
			@Override
			public void run() {
				try {
					File file = getFileFromServer(info.getUrl(), pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); //结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}}.start();
	}

	//安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		//执行动作
		intent.setAction(Intent.ACTION_VIEW);
		//执行的数据类型
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	private String getVersionName() throws Exception{
		//获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		//getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		return packInfo.versionName;
	}

	/**
	 * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
	 * @param version1
	 * @param version2
	 * @return
	 */
	public static int compareVersion(String version1, String version2) throws Exception {
		if (version1 == null || version2 == null) {
			throw new Exception("compareVersion error:illegal params.");
		}
		String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
		int diff = 0;
		while (idx < minLength
				&& (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
			++idx;
		}
		//如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}


	@Override
		protected void runInFrontend(String data) {

			try {
				InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));

				info = getUpdataInfo(is);

				String version = info.getVersion();

				String cur_version=getVersionName();

				if(compareVersion(version,cur_version)>0)
				{
					showUpdataDialog();
					//udpate
				}else {

				//not update
				}


			}catch (Exception ex){
				ex.printStackTrace();
			}

		}
	}