package cn.edu.tsinghua.sicd.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.db.CancerDatabaseHelper;
import cn.edu.tsinghua.sicd.db.DiseaseInjuryDatabaseHelper;
import cn.edu.tsinghua.sicd.db.DrugDatabaseHelper;
import cn.edu.tsinghua.sicd.db.ICD10DatabaseHelper;
import cn.edu.tsinghua.sicd.db.LandDatabaseHelper;
import cn.edu.tsinghua.sicd.db.QuickSearchHelper;
import cn.edu.tsinghua.sicd.models.APNSingleResult;
import cn.edu.tsinghua.sicd.models.CancerPage;
import cn.edu.tsinghua.sicd.models.CancerTumorSelectResult;
import cn.edu.tsinghua.sicd.models.CausePage;
import cn.edu.tsinghua.sicd.models.DiseaseInjuryPage;
import cn.edu.tsinghua.sicd.models.DrugChemicalSelectResult;
import cn.edu.tsinghua.sicd.models.DrugPage;
import cn.edu.tsinghua.sicd.models.ExternalCauseSelectResult;
import cn.edu.tsinghua.sicd.models.LandPage;
import cn.edu.tsinghua.sicd.models.LandTransportSelectResult;
import cn.edu.tsinghua.sicd.models.QuickSearchPage;
import cn.edu.tsinghua.sicd.models.QuickSearchSelectResult;
import cn.edu.tsinghua.sicd.utils.SharedPreferencesMgr;
import cn.edu.tsinghua.sicd.utils.SimpleThreadHandler;
import cn.edu.tsinghua.sicd.webapi.APNApi;

/**
 * 
 * @author wuwenjie
 * @description 今日
 */
public class UpdateFragment extends Fragment {

	private View rootView;
	private Button btn;

	int PageIndex;
	int PageSize;

	APNApi apnApi=null;

	SQLiteDatabase db_disease;
	SQLiteDatabase db_cancer;
	SQLiteDatabase db_drug;
	SQLiteDatabase db_icd10;
	SQLiteDatabase db_land;
	SQLiteDatabase db_quicksearch;

	private Button btnUpdate;
	TextView db_tip;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_update, null);

		PageIndex=1;
		PageSize=10;



		apnApi=new APNApi(getActivity());

		db_disease=new DiseaseInjuryDatabaseHelper(getActivity()).getWritableDatabase();
		db_cancer=new CancerDatabaseHelper(getActivity()).getWritableDatabase();
		db_drug=new DrugDatabaseHelper(getActivity()).getWritableDatabase();
		db_icd10=new ICD10DatabaseHelper(getActivity()).getWritableDatabase();
		db_land=new LandDatabaseHelper(getActivity()).getWritableDatabase();
		db_quicksearch=new QuickSearchHelper(getActivity()).getWritableDatabase();

		btnUpdate=(Button)rootView.findViewById(R.id.btnUpdate);
		btnUpdate.setText(String.format(getResources().getString(R.string.onlineUpdate)));


		db_tip=(TextView)rootView.findViewById(R.id.db_tip);
		int disease_count=db_disease.rawQuery("select * from diseaseinjury",null).getCount();
		int cancer_count=db_cancer.rawQuery("select * from cancer",null).getCount();
		int drug_count=db_drug.rawQuery("select * from drug",null).getCount();
		int icd10_count=db_icd10.rawQuery("select * from icd10",null).getCount();
		int land_count=db_land.rawQuery("select * from land",null).getCount();
		int diagnosis301_count=db_quicksearch.rawQuery("select * from tbldiagnosis_301",null).getCount();
		int diagnosisWsb_count=db_quicksearch.rawQuery("select * from tbldiagnosis_wsb",null).getCount();
		int diagnosisBj_count=db_quicksearch.rawQuery("select * from tbldiagnosis_bj",null).getCount();

		//String str="Local Data: The disease&injury contains "+disease_count+" records, neoplasm table contains "+cancer_count+" records, land transport accident contains "+land_count+" records, external cause contains "+icd10_count+" records and drugs&chemical contains "+drug_count+" records in the local.";

		String str="本地数据: tbldiagnosis_301包含 "+diagnosis301_count+" 条记录; tbldiagnosis_wsb包含 "+diagnosisWsb_count+" 条记录; tbldiagnosis_bj包含 "+diagnosisBj_count+" 条记录。";
		db_tip.setText(str);

		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				btnUpdate.setEnabled(false);
				btnUpdate.setText("Updating...");

				SharedPreferencesMgr spm=new SharedPreferencesMgr(getActivity(),"remember.data");
				spm.setString("isremember", "false");

				new TableDownloadHandler().doInBackground();

			}
		});

		return rootView;
	}

	private Handler  hander=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					((TextView)rootView.findViewById(R.id.tv_progress)).setText("下载中..."+msg.getData().get("lib")+" "+msg.getData().getString("per"));
					break;
				case 1:
					((TextView)rootView.findViewById(R.id.tv_progress)).setText(String.format(getResources().getString(R.string.FinishDownload)));
					SharedPreferencesMgr spm=new SharedPreferencesMgr(getActivity(),"remember.data");
					spm.setString("isremember", "true");
					btnUpdate.setEnabled(false);
					btnUpdate.setText(String.format(getResources().getString(R.string.updated)));
					break;
				case 2:
					((TextView)rootView.findViewById(R.id.tv_progress)).setText(String.format(getResources().getString(R.string.errorDownload)));

					break;
			}

			super.handleMessage(msg);
		}
	};

	public class TableDownloadHandler extends SimpleThreadHandler {


		@Override
		protected String runInBackend() {


			db_cancer.execSQL("delete from cancer");
			db_drug.execSQL("delete from drug");
			db_disease.execSQL("delete from diseaseinjury");
			db_icd10.execSQL("delete from icd10");
			db_land.execSQL("delete from land");
			db_quicksearch.execSQL("delete from tbldiagnosis_301");
			//db_quicksearch.execSQL("delete from tbldiagnosis_bj");
			//db_quicksearch.execSQL("delete from tbldiagnosis_wsb");


			//diagnosis_301
			PageIndex=1;
			while(true){

				String data=apnApi.getPage("QuickSearch301", PageIndex, PageSize);

				if(data==null||data.equals(""))
					break;

				QuickSearchPage page=new Gson().fromJson(data,QuickSearchPage.class);

				if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
					break;


				for(int i=0;i<page.getDataTable().size();i++){
					QuickSearchSelectResult model=page.getDataTable().get(i);
					System.out.println(page.getDataTable().get(i).getKeyword());
					ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
					cv.put("term",model.getKeyword());
					cv.put("icd_code",model.getICDcode());
					cv.put("Star_code",model.getStarCode());
					cv.put("py",model.getPy());
					cv.put("data", new Gson().toJson(model));

					long ret=db_quicksearch.insert("tbldiagnosis_301", null, cv);//执行插入操作
					System.out.println("insert "+ret);

				}

				System.out.println(PageIndex+","+page.getPageCount());

				//	break;
				Message msg=new Message();
				msg.what=0;
				Bundle bd=new Bundle();

				int per=(int)(PageIndex*1.0/page.getPageCount()*100);
				bd.putString("lib","Diagnosis301Lib");
				bd.putString("per",per+"%");
				msg.setData(bd);
				hander.sendMessage(msg);

				PageIndex++;
				if(PageIndex>page.getPageCount())
					break;


			}

			//diagnosis_bj
			PageIndex=1;
			while(true){

				String data=apnApi.getPage("QuickSearchBj", PageIndex, PageSize);

				if(data==null||data.equals(""))
					break;

				QuickSearchPage page=new Gson().fromJson(data,QuickSearchPage.class);

				if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
					break;


				for(int i=0;i<page.getDataTable().size();i++){
					QuickSearchSelectResult model=page.getDataTable().get(i);
					System.out.println(page.getDataTable().get(i).getKeyword());
					ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
					cv.put("term",model.getKeyword());
					cv.put("icd_code",model.getICDcode());
					cv.put("Star_code",model.getStarCode());
					cv.put("py",model.getPy());
					cv.put("data", new Gson().toJson(model));

					long ret=db_quicksearch.insert("tbldiagnosis_bj", null, cv);//执行插入操作
					System.out.println("insert "+ret);

				}

				System.out.println(PageIndex+","+page.getPageCount());

				//	break;
				Message msg=new Message();
				msg.what=0;
				Bundle bd=new Bundle();

				int per=(int)(PageIndex*1.0/page.getPageCount()*100);
				bd.putString("lib","DiagnosisBjLib");
				bd.putString("per",per+"%");
				msg.setData(bd);
				hander.sendMessage(msg);

				PageIndex++;
				if(PageIndex>page.getPageCount())
					break;


			}

			//diagnosis_wsb
			PageIndex=1;
			while(true){

				String data=apnApi.getPage("QuickSearchWsb", PageIndex, PageSize);

				if(data==null||data.equals(""))
					break;

				QuickSearchPage page=new Gson().fromJson(data,QuickSearchPage.class);

				if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
					break;


				for(int i=0;i<page.getDataTable().size();i++){
					QuickSearchSelectResult model=page.getDataTable().get(i);
					System.out.println(page.getDataTable().get(i).getKeyword());
					ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
					cv.put("term",model.getKeyword());
					cv.put("icd_code",model.getICDcode());
					cv.put("Star_code",model.getStarCode());
					cv.put("py",model.getPy());
					cv.put("data", new Gson().toJson(model));

					long ret=db_quicksearch.insert("tbldiagnosis_wsb", null, cv);//执行插入操作
					System.out.println("insert "+ret);

				}

				System.out.println(PageIndex+","+page.getPageCount());

				//	break;
				Message msg=new Message();
				msg.what=0;
				Bundle bd=new Bundle();

				int per=(int)(PageIndex*1.0/page.getPageCount()*100);
				bd.putString("lib","DiagnosisWsbLib");
				bd.putString("per",per+"%");
				msg.setData(bd);
				hander.sendMessage(msg);

				PageIndex++;
				if(PageIndex>page.getPageCount())
					break;

			}



			Message msg=new Message();
			msg.what=1;
			Bundle bd=new Bundle();


			msg.setData(bd);
			hander.sendMessage(msg);


			return "";

		}

		@Override
		protected void runInFrontend(String data) {



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
