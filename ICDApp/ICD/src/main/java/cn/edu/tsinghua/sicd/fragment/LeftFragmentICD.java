package cn.edu.tsinghua.sicd.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.tsinghua.sicd.MainActivity;
import cn.edu.tsinghua.sicd.R;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragmentICD extends Fragment implements OnClickListener{
	private View homeListViewList;
	private View updateView;

	private View quickSearch301ListView;
	private View quickSearchBjListView;
	private View quickSearchWsbListView;

	private View checkPage;

	private ImageView profileView;

	private View rootView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu_icd, null);
		findViews(view);
		this.rootView=view;
		
		return view;
	}
	
	
	public void findViews(View view) {

		homeListViewList = view.findViewById(R.id.tvHome);


		quickSearch301ListView =view.findViewById(R.id.tvQuickSearch301);
		quickSearchBjListView =view.findViewById(R.id.tvQuickSearchBj);
		quickSearchWsbListView =view.findViewById(R.id.tvQuickSearchWsb);

		updateView=view.findViewById(R.id.tvMyUpdate);
		checkPage=view.findViewById(R.id.tvCheck);

		homeListViewList.setOnClickListener(this);

		quickSearch301ListView.setOnClickListener(this);
		quickSearchBjListView.setOnClickListener(this);
		quickSearchWsbListView.setOnClickListener(this);
		checkPage.setOnClickListener(this);

		updateView.setOnClickListener(this);

	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {


		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
			case R.id.tvHome:
				newContent = new HomeFragment();
				title = "主页";
				break;
			case R.id.tvQuickSearch301:
				newContent=new QuickSearch301Fragment();
				title="快捷查询-301";
				break;
			case R.id.tvQuickSearchBj:
				newContent=new QuickSearchBjFragment();
				title="快捷查询-北京市";
				break;
			case R.id.tvQuickSearchWsb:
				newContent=new QuickSearchWsbFragment();
				title="快捷查询-卫生部";
				break;
            case R.id.tvCheck:
                newContent=new ICDCheckFragment();
				title="核对";
				break;
			case R.id.tvMyUpdate:
				newContent=new UpdateFragment();
				title="数据库下载/更新";
				break;

		default:
			break;
		}



		if (newContent != null) {
			switchFragment(newContent, title);
		}

		hightlight(v.getId());
	}

	public void hightlight(int res){
		int[] st=new int[]{R.id.tvHome,R.id.tvQuickSearch301,R.id.tvQuickSearchBj,R.id.tvQuickSearchWsb,R.id.tvCheck};
		for(int i=0;i<st.length;i++){
			if(res==st[i]){

				TextView tv=(TextView)this.rootView.findViewById(st[i]);
				tv.setTextColor(Color.parseColor("#2828FF"));

			}else{
				TextView tv=(TextView)this.rootView.findViewById(st[i]);
				tv.setTextColor(Color.parseColor("#FFFFFF"));
			}
		}
	}



	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}
	
}
