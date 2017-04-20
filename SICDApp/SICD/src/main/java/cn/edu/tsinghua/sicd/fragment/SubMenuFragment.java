package cn.edu.tsinghua.sicd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.tsinghua.sicd.MainActivity;
import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.activity.HelpActivity;

/**
 * 
 * @author wuwenjie
 * @description 今日
 */
public class SubMenuFragment extends Fragment {
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
		View view = inflater.inflate(R.layout.frag_submenu, null);



		((Button)view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity mainActivity=(MainActivity)getActivity();
				mainActivity.switchConent(new CancerTumorFragment(),"Neoplasm Table");
			}
		});

		((Button)view.findViewById(R.id.btnLand)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity mainActivity=(MainActivity)getActivity();
				mainActivity.switchConent(new LandTransportFragment(),"Table of Land transport Accident");
			}
		});


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
