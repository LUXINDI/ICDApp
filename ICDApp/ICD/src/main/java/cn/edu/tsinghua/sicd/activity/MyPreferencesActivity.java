package cn.edu.tsinghua.sicd.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.tsinghua.sicd.R;

/**
* @description 有关首选项preferences的研究
* @author chenzheng_java
* @since 2011/03/29
* 继承了PreferenceActivity我们可以方便的对preference进行操作。
* 例如可以通过getPreferenceManager获取首选项管理器
* 那，我们可不可以不继承PreferenceActivity呢？当然可以，你还记得不记得，实际上Activity类中
* 就有个SharedPreferences getSharedPreferences(String name, int mode)方法呢，我们通过它
* 也可以对preference进行操作。当然了，如果我们不继承PreferenceActivity的话，那么我们就要手动的
* 对数据进行保存了。而不是跟现在一样，会自动的根据你的选择项进行数据保存。
* 那么，preference在这里是怎么样进行自动保存的呢，答案很简单，那就是在addPreferencesFromResource方法的具体实现中！
*/
public class MyPreferencesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //这个是给Settings加自定义Title
        final boolean isCustom = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        if(isCustom){
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_head_back);
        }


        ImageView btnBack=(ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        TextView tvTitle=(TextView)findViewById(R.id.topTv);
        tvTitle.setText("Settings");



        addPreferencesFromResource(R.xml.mylistpreference);
/**
 * getPreferenceManager返回首选项管理器对象
 */
        PreferenceManager manager = getPreferenceManager();
// 根据android:key中指定的名称（相当于id）来获取首选项
        ListPreference listPreference = (ListPreference) manager.findPreference("searchMode");
        Log.i("存储的值为", "" + listPreference.getValue());

        setTitle("Settings");
    }
}