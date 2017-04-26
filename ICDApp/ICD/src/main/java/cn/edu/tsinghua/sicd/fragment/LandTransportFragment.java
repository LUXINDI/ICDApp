package cn.edu.tsinghua.sicd.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.adapter.LandAdapter;
import cn.edu.tsinghua.sicd.models.CancerTumorSelectResult;
import cn.edu.tsinghua.sicd.models.LandTransportSelectResult;
import cn.edu.tsinghua.sicd.utils.SimpleThreadHandler;
import cn.edu.tsinghua.sicd.webapi.APNApi;

/**
 *
 * @author wuwenjie
 * @description 今日
 */
public class LandTransportFragment extends Fragment {


	private SearchView sv_apn;
	private ListView lv_apn;
	private ArrayAdapter<String> aadapter;

	private String[] terms;
	private String[] terms1;
	private String[] terms2;

	private APNApi apnApi;

	private ListView lvLand;

	private boolean flag=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		apnApi=new APNApi(getActivity());

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_land_transport, null);


		sv_apn = (SearchView) view.findViewById(R.id.sv_apn);

		//sv_apn.setIconified(true);

		lv_apn = (ListView) view.findViewById(R.id.lv_apn);

		lv_apn.setTextFilterEnabled(true);

		lv_apn.setOnItemClickListener(new OnItemClickListenerImpl());

		lvLand=(ListView)view.findViewById(R.id.land_list);

		sv_apn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {


				if(flag)return false;

				lv_apn.setVisibility(View.VISIBLE);
				lvLand.setVisibility(View.GONE);
				removeAllRow();


				if (newText.length() != 0) {

					new SearchForLand(newText).doInBackground();

				} else {

					//	aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, new String[]{});
					aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_search, new String[]{});

					lv_apn.setAdapter(aadapter);
					aadapter.notifyDataSetChanged();

				}
				return false;
			}
		});

		//ImageView menuButton=(ImageView)getActivity().findViewById(R.id.backButton);
		//menuButton.setVisibility(View.VISIBLE);
		sv_apn.requestFocus();
		return view;
	}


	int num=0;



	private void removeAllRow() {

		List <LandTransportSelectResult>retList =new ArrayList<LandTransportSelectResult>();
		LandAdapter landAdapter =new LandAdapter(getActivity(),retList);

		lvLand.setAdapter(landAdapter);
	}

	private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			if(terms!=null&&terms.length>0) {

				String title = terms[position];

				//Toast.makeText(getActivity().getApplicationContext(), title, Toast.LENGTH_LONG).show();

				//

				flag=true;

				lv_apn.setVisibility(View.GONE);
				lvLand.setVisibility(View.VISIBLE);


				new SearchForLandSingle(title,terms1[position]).doInBackground();



			}
		}

	}

	public void resetSearchView(String title){

		int myid = sv_apn.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) sv_apn.findViewById(myid);
		textView.setText(title);

	}


	public class SearchForLand extends SimpleThreadHandler{
		private String text;

		public SearchForLand(String text){
			this.text=text;
		}

		@Override
		protected String runInBackend() {

			String ret=apnApi.getLandList(this.text);

			return ret;
		}

		@Override
		protected void runInFrontend(String data) {

			terms=new String[]{};
			terms1=new String[]{};
			terms2=new String[]{};

			if(data!=null&&!data.equals("")) {

				Log.d("api", data);


				try{
					Gson gson = new Gson();
					List<LandTransportSelectResult> retList = gson.fromJson(data,
							new TypeToken<List<LandTransportSelectResult>>() {
							}.getType());
					if(retList!=null&&retList.size()>0) {
						terms=new String[retList.size()];
						terms1=new String[retList.size()];
						terms2=new String[retList.size()];
						for(int i=0;i<retList.size();i++){
							terms[i]=retList.get(i).getKeyword1();
							terms1[i]=retList.get(i).getKeyword2();
							terms2[i]=retList.get(i).getKeyword1()+" ("+retList.get(i).getKeyword2()+")";
						}
					}


					//若只有一个结果的时候，显示数据
					if(retList!=null&&retList.size()==1){

						new SearchForLandSingle(retList.get(0).getKeyword1()).doInBackground();

						lv_apn.setVisibility(View.VISIBLE);
						lvLand.setVisibility(View.GONE);


						return;

					}

				}catch (Exception ex){

				}

			}else {

				Log.d("api","no data");

			}
			aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_search, terms);

			//	aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, terms);
			lv_apn.setAdapter(aadapter);

			aadapter.notifyDataSetChanged();

		}
	}


	public class SearchForLandSingle extends SimpleThreadHandler{
		private String keyword1;
		private String keyword2;


		public SearchForLandSingle(String keyword1){
			 this.keyword1=keyword1;

		}

		public SearchForLandSingle(String keyword1,String keyword2){
			this.keyword1=keyword1;
			this.keyword2=keyword2;

		}

		@Override
		protected String runInBackend() {

			String ret=apnApi.getLand(keyword1);

			return ret;
		}

		@Override
		protected void runInFrontend(String data) {

			terms=new String[]{};

			if(data!=null&&!data.equals("")) {

				Log.d("api", data);


				try{
					Gson gson = new Gson();

					List<LandTransportSelectResult> retList = gson.fromJson(data,
							new TypeToken<List<LandTransportSelectResult>>() {
							}.getType());

					if (retList!=null){
						/*
						List<LandTransportSelectResult> newList=new ArrayList<LandTransportSelectResult>();


						if(keyword2!=null){
							for(int i=0;i<retList.size();i++)
								if(retList.get(i).getKeyword2().equals(keyword2)){
									newList.add(retList.get(i))
											;
									break;
								}
						}
*/

						LandAdapter landAdapter =new LandAdapter(getActivity(),retList);

					 	lvLand.setAdapter(landAdapter);

						//auto fill
						resetSearchView(keyword1);

					}else{

						retList=new ArrayList<LandTransportSelectResult>();
						LandAdapter landAdapter =new LandAdapter(getActivity(),retList);

						lvLand.setAdapter(landAdapter);

					}

				}catch (Exception ex){

				}

			}else {

				Log.d("api","no data");

			}


			flag=false;

			//aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_search,  terms);

			//aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, terms);
			//lv_apn.setAdapter(aadapter);
			//aadapter.notifyDataSetChanged();

		}
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
