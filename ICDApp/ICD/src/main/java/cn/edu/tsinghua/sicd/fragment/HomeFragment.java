package cn.edu.tsinghua.sicd.fragment;

import cn.edu.tsinghua.sicd.MainActivity;
import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.activity.HelpActivity;
import cn.edu.tsinghua.sicd.activity.HelpMainActivity;
import cn.edu.tsinghua.sicd.utils.AppUpdateHandler;
import cn.edu.tsinghua.sicd.utils.DetectUpdateHandler;
import cn.edu.tsinghua.sicd.utils.PreferenceUtils;
import cn.edu.tsinghua.sicd.utils.SharedPreferencesMgr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * 
 * @author wuwenjie
 * @description 今日
 */
public class HomeFragment extends Fragment {

	private TextView startButton;
	private TextView tvNewVersion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_home, null);

		TextView btnHelp=(TextView)view.findViewById(R.id.btnHelp);
		btnHelp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), HelpMainActivity.class);
				startActivity(intent);
			}
		});

		ImageView menuButton=(ImageView)getActivity().findViewById(R.id.backButton);

		menuButton.setImageResource(R.drawable.icon_settings);

		menuButton.setVisibility(View.VISIBLE);

		menuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setIcon(R.drawable.app_logo_who);
				builder.setTitle("Matching Mode");
				//    指定下拉列表的显示数据
				final String[] cities = {"Forward", "Fuzzy"};
				//    设置一个下拉的列表选择项
				builder.setItems(cities, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PreferenceUtils.setSearchMode(getActivity(), which + ""
						);


						//	Toast.makeText(MainActivity.this, "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
					}
				});
				builder.show();
			}
		});

		startButton=(TextView)view.findViewById(R.id.btnStart);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.Instance.toggleMenu();
			}
		});

		tvNewVersion=(TextView)view.findViewById(R.id.tv_newversion);

		tvNewVersion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AppUpdateHandler(getActivity()).doInBackground();
			}
		});

		new DetectUpdateHandler(getActivity(),tvNewVersion).doInBackground();

		SharedPreferencesMgr spm1=new SharedPreferencesMgr(getActivity(),"remember.data");
		final String hasdownload=spm1.getString("isremember","false");

		if(!hasdownload.equals("true")&&MainActivity.isFirstStart==true
				){

			//开始更新

			MainActivity.isFirstStart=false;

			new AlertDialog.Builder(getActivity()).setTitle("数据库下载/更新")//设置对话框标题

					.setMessage("数据库下载后能够离线使用。您想要下载数据库吗?")//设置显示的内容

					.setPositiveButton("是", new DialogInterface.OnClickListener() {//添加确定按钮

						@Override

						public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

							// TODO Auto-generated method stub
							MainActivity.Instance.switchConent(new UpdateFragment(), "Download the Latest Data");

						}

					}).setNegativeButton("否",new DialogInterface.OnClickListener() {//添加返回按钮

				@Override

				public void onClick(DialogInterface dialog, int which) {//响应事件

				       dialog.dismiss();

				}

			}).show();//在按键响应事件中显示此对话框



		}

		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
