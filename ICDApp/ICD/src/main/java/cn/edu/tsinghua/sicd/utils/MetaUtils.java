package cn.edu.tsinghua.sicd.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class MetaUtils {

	public static String getValue(Context context, String key) {
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			String v = appInfo.metaData.getString(key);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getWebRoot(Context context){

		String apiUrl=getValue(context,"WebApiRoot");
		return apiUrl.substring(0,apiUrl.length()-4);
	}

}
