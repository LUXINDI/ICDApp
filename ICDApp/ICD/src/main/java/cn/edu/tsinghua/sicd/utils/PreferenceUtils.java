package cn.edu.tsinghua.sicd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

/**
 * Created by douglaschan on 2016/3/23.
 */
public class PreferenceUtils {

    public static String getSearchMode(Context context){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ret=prefs.getString("searchMode","0");
        return ret+"";

    }

    public static void setSearchMode(Context context,String value){
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("searchMode", value);
        editor.commit();

    }

}
