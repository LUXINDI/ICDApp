package cn.edu.tsinghua.sicd.fragment;

import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.models.APNSelectResult;
import cn.edu.tsinghua.sicd.models.APNSingleResult;
import cn.edu.tsinghua.sicd.utils.PreferenceUtils;
import cn.edu.tsinghua.sicd.utils.SimpleThreadHandler;
import cn.edu.tsinghua.sicd.webapi.APNApi;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 
 * @author wuwenjie
 * @description 今日
 */
public class DiseaseInjuryFragment extends Fragment {


	private SearchView sv_apn;
	private ListView lv_apn;
	private ArrayAdapter<String> aadapter;

	private String[] terms;

	private APNApi apnApi;

	private TableLayout tableLayout;

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
		View view = inflater.inflate(R.layout.frag_disease_injury, null);




		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		sv_apn = (SearchView) view.findViewById(R.id.sv_apn);

		//sv_apn.setIconified(true);

		lv_apn = (ListView) view.findViewById(R.id.lv_apn);

		lv_apn.setTextFilterEnabled(true);

		lv_apn.setOnItemClickListener(new OnItemClickListenerImpl());

		tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);

		sv_apn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				lv_apn.setVisibility(View.VISIBLE);
				tableLayout.setVisibility(View.GONE);
				removeAllRow();

				new SearchForAPN(newText).doInBackground();

				if(true)
				return false;

				if (newText.length() != 0) {

					new SearchForAPN(newText).doInBackground();

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


		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(sv_apn.getRootView(),InputMethodManager.SHOW_FORCED);


		new SearchForAPN("").doInBackground();


		//	Toast.makeText(getActivity(), PreferenceUtils.getSearchMode(getActivity()), Toast.LENGTH_SHORT).show();
		return view;
	}


	int num=0;

	private void addRow(String key,String value)
	{
		TableRow tableRow = new TableRow(this.getActivity());

		if(!key.equals(""))
		tableRow.setBackgroundColor(Color.parseColor("#E0E0E0"));


		TableLayout.LayoutParams lp=new TableLayout.LayoutParams();
		lp.setMargins(0, 10, 0, 0);

		tableRow.setLayoutParams(lp);


		TextView keyView = new TextView(this.getActivity());
		TextView valueView = new TextView(this.getActivity());


		keyView.setText(key);
		keyView.setTextSize(20);
		keyView.setTextColor(Color.rgb(30,144,255));
		valueView.setText(value);
		valueView.setTextSize(20);
		valueView.setPadding(0,0,5,0);

		if(!key.equals("")){

			//add tag


		}


		tableRow.addView(keyView);
		tableRow.addView(valueView);

		tableLayout.addView(tableRow);

	}

	private void removeAllRow() {

		tableLayout.removeAllViews();

	}

private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			if(terms!=null&&terms.length>0) {

				String title = terms[position];

				//Toast.makeText(getActivity().getApplicationContext(), title, Toast.LENGTH_LONG).show();

				//
				lv_apn.setVisibility(View.GONE);
				tableLayout.setVisibility(View.VISIBLE);


				new SearchForAPNSingle(title).doInBackground();



			}
		}

	}

	public void resetSearchView(String title){

		int myid = sv_apn.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) sv_apn.findViewById(myid);
		textView.setText(title);
		textView.requestFocus();

	}


public class SearchForAPN extends SimpleThreadHandler{
		private String text;

		public SearchForAPN(String text){
			this.text=text;
		}

		@Override
		protected String runInBackend() {

			String ret=apnApi.getAutoSearchList(this.text);

			return ret;
		}

		@Override
		protected void runInFrontend(String data) {

			terms=new String[]{};

			if(data!=null&&!data.equals("")) {

				Log.d("api", data);


				try{
					Gson gson = new Gson();
					List<APNSelectResult> retList = gson.fromJson(data,
							new TypeToken<List<APNSelectResult>>() {
							}.getType());
					if(retList!=null&&retList.size()>0) {
						terms=new String[retList.size()];
						for(int i=0;i<retList.size();i++){
							terms[i]=retList.get(i).getDiagnosisTerm();
						}
					}

					//若只有一个结果的时候，显示数据
					if(retList!=null&&retList.size()==1){

						lv_apn.setVisibility(View.GONE);
						tableLayout.setVisibility(View.VISIBLE);

						new SearchForAPNSingle(retList.get(0).getDiagnosisTerm()).doInBackground();

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


	public class SearchForAPNSingle extends SimpleThreadHandler{
		private String text;

		public SearchForAPNSingle(String text){
			this.text=text;
		}

		@Override
		protected String runInBackend() {

			String ret=apnApi.getSingle(this.text);

			return ret;
		}

		@Override
		protected void runInFrontend(String data) {

			terms=new String[]{};

			if(data!=null&&!data.equals("")) {

				Log.d("api", data);


				try{
					Gson gson = new Gson();

					APNSingleResult model = gson.fromJson(data,APNSingleResult.class);
					if (model!=null){

					 	//addRow("DiagnosisTerm",model.getDiagnosisTerm());
						addRow("General", model.getGeneral());
						addRow("", "");

						addRow("Congenital",model.getCongenital());
						addRow("Perinatal Neonatal",model.getPerinatalNeonatal());
						addRow("", "");

						addRow("Pregnancy",model.getPregnancy());
						addRow("Childbirth",model.getChildbirth());
						addRow("Postpartum",model.getPostpartum());


						//auto fill
						resetSearchView(model.getDiagnosisTerm());

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
