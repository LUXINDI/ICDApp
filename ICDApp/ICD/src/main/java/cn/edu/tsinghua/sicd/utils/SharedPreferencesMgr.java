
package cn.edu.tsinghua.sicd.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesMgr {

	private  Context context;
	private  SharedPreferences sPrefs;

	public SharedPreferencesMgr(Context context, String fileName) {
		this.context = context;
		// 初始化一个SharedPreferences对象，可以读写。
		sPrefs = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
	}



	/**
	 * 从Preferences缓存得到一个整形
	 *
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public  int getInt(String key, int defaultValue) {
		return sPrefs.getInt(key, defaultValue);
	}

	/**
	 * 保存一个整形到Preferences中
	 *
	 * @param key
	 * @param value
	 */
	public  void setInt(String key, int value) {
		sPrefs.edit().putInt(key, value).commit();
	}

	public  boolean getBoolean(String key, boolean defaultValue) {
		return sPrefs.getBoolean(key, defaultValue);
	}

	public  void setBoolean(String key, boolean value) {
		sPrefs.edit().putBoolean(key, value).commit();
	}

	public  String getString(String key, String defaultValue) {
		if (sPrefs == null)
			return null;
		return sPrefs.getString(key, defaultValue);
	}

	public  void setString(String key, String value) {
		if (sPrefs == null)
			return;
		sPrefs.edit().putString(key, value).commit();
	}
}