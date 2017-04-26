package cn.edu.tsinghua.sicd.fragment;

import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.models.APNSelectResult;
import cn.edu.tsinghua.sicd.models.APNSingleResult;
import cn.edu.tsinghua.sicd.models.CancerTumorSelectResult;
import cn.edu.tsinghua.sicd.models.ExternalCauseSelectResult;
import cn.edu.tsinghua.sicd.utils.SimpleThreadHandler;
import cn.edu.tsinghua.sicd.webapi.APNApi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 *
 * @author wuwenjie
 * @description 今日
 */
public class ExternalCauseFragment extends Fragment {


	private SearchView sv_apn;
	private ListView lv_apn;
	private ArrayAdapter<String> aadapter;

	private String[] terms;

	private APNApi apnApi;

	private LinearLayout infoLayout;

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
		View view = inflater.inflate(R.layout.frag_externalcauses, null);


		sv_apn = (SearchView) view.findViewById(R.id.sv_apn);

		//sv_apn.setIconified(true);

		lv_apn = (ListView) view.findViewById(R.id.lv_apn);

		lv_apn.setTextFilterEnabled(true);

		lv_apn.setOnItemClickListener(new OnItemClickListenerImpl());

		infoLayout=(LinearLayout)view.findViewById(R.id.info_external_cause);

		sv_apn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				lv_apn.setVisibility(View.VISIBLE);
				infoLayout.setVisibility(View.GONE);
				removeAllRow();

				new SearchForCauseList(newText).doInBackground();

				if(true)return false;

				if (newText.length() != 0) {

					new SearchForCauseList(newText).doInBackground();

				} else {

					//	aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, new String[]{});
					aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_search, new String[]{});

					lv_apn.setAdapter(aadapter);
					aadapter.notifyDataSetChanged();

				}
				return false;
			}
		});

		sv_apn.requestFocus();

		new SearchForCauseList("").doInBackground();


		return view;
	}


	int num=0;

	private void removeAllRow() {

		infoLayout.setVisibility(View.GONE);

	}

	private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			if(terms!=null&&terms.length>0) {

				String title = terms[position];

				//Toast.makeText(getActivity().getApplicationContext(), title, Toast.LENGTH_LONG).show();

				//
				lv_apn.setVisibility(View.GONE);

				infoLayout.setVisibility(View.VISIBLE);


				new SearchForCauseSingle(title).doInBackground();



			}
		}

	}

	public void resetSearchView(String title){

		int myid = sv_apn.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) sv_apn.findViewById(myid);
		textView.setText(title);

	}


	public class SearchForCauseList extends SimpleThreadHandler{
		private String text;

		public SearchForCauseList(String text){
			this.text=text;
		}

		@Override
		protected String runInBackend() {

			String ret=apnApi.getCauseList(this.text);

			return ret;
		}

		@Override
		protected void runInFrontend(String data) {

			terms=new String[]{};

			if(data!=null&&!data.equals("")) {

				Log.d("api", data);


				try{
					Gson gson = new Gson();
					List<ExternalCauseSelectResult> retList = gson.fromJson(data,
							new TypeToken<List<ExternalCauseSelectResult>>() {
							}.getType());
					if(retList!=null&&retList.size()>0) {
						terms=new String[retList.size()];
						for(int i=0;i<retList.size();i++){
							terms[i]=retList.get(i).getCause();
						}
					}

					//若只有一个结果的时候，显示数据
					if(retList!=null&&retList.size()==1){

						lv_apn.setVisibility(View.GONE);

						infoLayout.setVisibility(View.VISIBLE);

						new SearchForCauseSingle(retList.get(0).getCause()).doInBackground();

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


	public class SearchForCauseSingle extends SimpleThreadHandler{
		private String text;

		public SearchForCauseSingle(String text){
			this.text=text;
		}

		@Override
		protected String runInBackend() {

			String ret=apnApi.getCause(this.text);

			return ret;
		}

		@Override
		protected void runInFrontend(String data) {

			terms=new String[]{};

			if(data!=null&&!data.equals("")) {

				Log.d("api", data);


				try{
					Gson gson = new Gson();

					ExternalCauseSelectResult model = gson.fromJson(data,ExternalCauseSelectResult.class);
					if (model!=null){

						//addRow("DiagnosisTerm",model.getDiagnosisTerm());

						((TextView)getActivity().findViewById(R.id.ec_desc)).setText(model.getCause());
						((TextView)getActivity().findViewById(R.id.ec_icd)).setText(model.getICD10());

						//auto fill
						resetSearchView(model.getCause());

					}

				}catch (Exception ex){

				}

			}else {

				Log.d("api","no data");

			}

			aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_search,  terms);

			//aadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, terms);
			lv_apn.setAdapter(aadapter);
			aadapter.notifyDataSetChanged();

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
