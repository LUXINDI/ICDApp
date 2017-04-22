package cn.edu.tsinghua.sicd.fragment;

import android.app.Activity;
import android.content.Intent;
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
import cn.edu.tsinghua.sicd.activity.MyPreferencesActivity;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragmentSICD extends Fragment implements OnClickListener{
	private View apnListView;
	private View cancelTurmorListView;
	private View drugChemicalListView;
	private View externalCauseListView;
	private View homeListViewList;
	private View settingsView;
	private View landTransportViewList;
	private View updateView;

	private View quickSearch301ListView;
	private View quickSearchBjListView;
	private View quickSearchWsbListView;

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
		View view = inflater.inflate(R.layout.layout_menu_sicd, null);
		findViews(view);
		this.rootView=view;
		
		return view;
	}
	
	
	public void findViews(View view) {

		homeListViewList = view.findViewById(R.id.tvHome);
		apnListView = view.findViewById(R.id.tvAPN);
		cancelTurmorListView = view.findViewById(R.id.tvCancerTumor);
		drugChemicalListView = view.findViewById(R.id.tvDrugChemical);
		externalCauseListView = view.findViewById(R.id.tvExternalCause);
		landTransportViewList=view.findViewById(R.id.tvLandTransport);

		settingsView = view.findViewById(R.id.tvMySettings);

		quickSearch301ListView =view.findViewById(R.id.tvQuickSearch301);
		quickSearchBjListView =view.findViewById(R.id.tvQuickSearchBj);
		quickSearchWsbListView =view.findViewById(R.id.tvQuickSearchWsb);

		updateView=view.findViewById(R.id.tvMyUpdate);

		homeListViewList.setOnClickListener(this);
		apnListView.setOnClickListener(this);
		cancelTurmorListView.setOnClickListener(this);
		drugChemicalListView.setOnClickListener(this);
		externalCauseListView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
		landTransportViewList.setOnClickListener(this);

		quickSearch301ListView.setOnClickListener(this);
		quickSearchBjListView.setOnClickListener(this);
		quickSearchWsbListView.setOnClickListener(this);

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
				title = "Home";
				break;
			case R.id.tvAPN:
				newContent = new DiseaseInjuryFragment();
				title = "Diseases and Nature of Injury";
				break;
			case R.id.tvCancerTumor:
				newContent = new CancerTumorFragment();
		//	newContent=new SubMenuFragment();

				title = "Neoplasm Table";
				break;
			case R.id.tvLandTransport:
				newContent=new LandTransportSimpleFragment();

				title = "Table of Land transport Accident";
				break;
			case R.id.tvExternalCause:
				newContent = new ExternalCauseFragment();
				title = "External Causes of Injury";
				break;
			case R.id.tvDrugChemical:
				newContent = new DrugChemicalFragment();
				title = "Tables of Drugs and Chemicals";
				break;
			case R.id.tvQuickSearch301:
				newContent=new QuickSearch301Fragment();
				title="@string/quick_search_301";
				break;
			case R.id.tvQuickSearchBj:
				newContent=new QuickSearch301Fragment();
				title="@string/quick_search_bj";
				break;
			case R.id.tvQuickSearchWsb:
				newContent=new QuickSearch301Fragment();
				title="@string/quick_search_wsb";
				break;
			case R.id.tvMyUpdate:
				newContent=new UpdateFragment();
				title="Download the Latest Data";
				break;


		case R.id.tvMySettings:

		//	newContent = new MySettingsFragment();
		//	title =

			Intent intent=new Intent(getActivity(), MyPreferencesActivity.class);
			startActivity(intent);

			return;

		default:
			break;
		}



		if (newContent != null) {
			switchFragment(newContent, title);
		}

		hightlight(v.getId());
	}

	public void hightlight(int res){
		int[] st=new int[]{R.id.tvHome,R.id.tvAPN,R.id.tvLandTransport,R.id.tvCancerTumor,R.id.tvDrugChemical,R.id.tvExternalCause,R.id.tvMySettings,R.id.tvQuickSearch301,R.id.tvQuickSearchBj,R.id.tvQuickSearchWsb};
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
