package cn.edu.tsinghua.sicd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import java.util.ArrayList;

import cn.edu.tsinghua.sicd.MainActivity;
import cn.edu.tsinghua.sicd.R;


/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 主界面
 */
public class TotalHelpActivity extends Activity {
	private TextView startButton;
	ViewPager viewPager;
	TextView tvTitle;
	ArrayList<View> views;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		setContentView(R.layout.activity_help_total);

		int index=0;

		if(getIntent()!=null){
			Intent intent=getIntent();
			index=intent.getIntExtra("index",0);
		}

		ImageView btnBack=(ImageView)findViewById(R.id.btnBack);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		 tvTitle=(TextView)findViewById(R.id.topTv);
		tvTitle.setText(titles[index]);

		initViewPager();

	 	viewPager.setCurrentItem(index);



	}

	String[] titles=new String[]{


			"Help<1>-1",

	"Help<1>-2",

			"Help<1>-3",

			"Help<1>-4",

			"Help<1>-5",

			"Help<1>-6",

			"Help<1>-7"


};


	private void initViewPager(){
		  viewPager = (ViewPager)findViewById(R.id.viewPager);

		View view1 = LayoutInflater.from(this).inflate(R.layout.activity_help1, null);
		View view2 = LayoutInflater.from(this).inflate(R.layout.activity_help2, null);
		View view3 = LayoutInflater.from(this).inflate(R.layout.activity_help3, null);
		View view4 = LayoutInflater.from(this).inflate(R.layout.activity_help4, null);
		View view5 = LayoutInflater.from(this).inflate(R.layout.activity_help5, null);
		View view6 = LayoutInflater.from(this).inflate(R.layout.activity_help6, null);
		View view7 = LayoutInflater.from(this).inflate(R.layout.activity_help7, null);

		startButton=(TextView)view7.findViewById(R.id.btnStart);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(TotalHelpActivity.this, MainActivity.class);
				intent.putExtra("openmenu",true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				TotalHelpActivity.this.startActivity(intent);

				//MainActivity.Instance.toggleMenu();
			}
		});


		 views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		views.add(view6);
		views.add(view7);

		MYViewPagerAdapter adapter = new MYViewPagerAdapter();
		adapter.setViews(views);

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				tvTitle.setText(titles[position]);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});


		viewPager.setAdapter(adapter);
	}

	public class MYViewPagerAdapter extends PagerAdapter {
		private ArrayList<View> views;

		public void setViews(ArrayList<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return super.getPageTitle(position);
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {

			Log.d("main", "" + position);
			((ViewPager) container).addView(views.get(position));

			return views.get(position);
		}
	}



}
