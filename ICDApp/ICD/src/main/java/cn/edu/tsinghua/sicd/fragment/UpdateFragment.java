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

		btnUpdate=(Button)rootView.findViewById(R.id.btnUpdate);
		btnUpdate.setText("Update Online");


		db_tip=(TextView)rootView.findViewById(R.id.db_tip);
		int disease_count=db_disease.rawQuery("select * from diseaseinjury",null).getCount();
		int cancer_count=db_cancer.rawQuery("select * from cancer",null).getCount();
		int drug_count=db_drug.rawQuery("select * from drug",null).getCount();
		int icd10_count=db_icd10.rawQuery("select * from icd10",null).getCount();
		int land_count=db_land.rawQuery("select * from land",null).getCount();

		String str="Local Data: The disease&injury contains "+disease_count+" records, neoplasm table contains "+cancer_count+" records, land transport accident contains "+land_count+" records, external cause contains "+icd10_count+" records and drugs&chemical contains "+drug_count+" records in the local.";
db_tip.setText(str);

		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				btnUpdate.setEnabled(false);
				btnUpdate.setText("Updating...");

				SharedPreferencesMgr spm=new SharedPreferencesMgr(getActivity(),"remember.data");
				spm.setString("isremember", "false");

				new CancerDownloadHandler().doInBackground();

			}
		});

		return rootView;
	}

	private Handler  hander=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					((TextView)rootView.findViewById(R.id.tv_progress)).setText("Downloading..."+msg.getData().get("lib")+" "+msg.getData().getString("per"));
					break;
				case 1:
					((TextView)rootView.findViewById(R.id.tv_progress)).setText("Finished Download");
					SharedPreferencesMgr spm=new SharedPreferencesMgr(getActivity(),"remember.data");
					spm.setString("isremember", "true");
					btnUpdate.setEnabled(false);
					btnUpdate.setText("Updated");
					break;
				case 2:
					((TextView)rootView.findViewById(R.id.tv_progress)).setText("Error Download");

					break;
			}

			super.handleMessage(msg);
		}
	};

	public class CancerDownloadHandler extends SimpleThreadHandler {


		@Override
		protected String runInBackend() {


			db_cancer.execSQL("delete from cancer");
			db_drug.execSQL("delete from drug");
			db_disease.execSQL("delete from diseaseinjury");
			db_icd10.execSQL("delete from icd10");
			db_land.execSQL("delete from land");

			//DiseaseInjury
		    while(true){

				String data=apnApi.getPage("DiseaseInjury", PageIndex, PageSize);

				if(data==null||data.equals(""))
					break;

				DiseaseInjuryPage page=new Gson().fromJson(data,DiseaseInjuryPage.class);

				if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
					break;


				for(int i=0;i<page.getDataTable().size();i++){
					APNSingleResult model=page.getDataTable().get(i);
					System.out.println(page.getDataTable().get(i).getDiagnosisTerm());
					ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
					cv.put("diagnosisterm",model.getDiagnosisTerm());
					cv.put("general",model.getGeneral());
					cv.put("congenital",model.getCongenital());
					cv.put("perinatalneonatal",model.getPerinatalNeonatal());
					cv.put("Pregnancy",model.getPregnancy());
					cv.put("childbirth",model.getChildbirth());
					cv.put("postpartum", model.getPostpartum());
					cv.put("data", new Gson().toJson(model));
					long ret=db_disease.insert("diseaseinjury", null, cv);//执行插入操作
					System.out.println("insert "+ret);

				}

				System.out.println(PageIndex+","+page.getPageCount());

			//	break;


				Message msg=new Message();
				msg.what=0;
				Bundle bd=new Bundle();

				int per=(int)(PageIndex*1.0/page.getPageCount()*100);
				bd.putString("lib","DiseaseLib");
				bd.putString("per",per+"%");
				msg.setData(bd);
				hander.sendMessage(msg);

				PageIndex++;
				if(PageIndex>page.getPageCount())
					break;


			}

			//cancer
				PageIndex=1;
				while(true){

					String data=apnApi.getPage("CancerTumor", PageIndex, PageSize);

					if(data==null||data.equals(""))
						break;

					CancerPage page=new Gson().fromJson(data,CancerPage.class);

					if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
						break;


					for(int i=0;i<page.getDataTable().size();i++){
						CancerTumorSelectResult model=page.getDataTable().get(i);
						System.out.println(page.getDataTable().get(i).getKeyword());
						ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
						cv.put("keyword",model.getKeyword());
						cv.put("[primary]",model.getPrimary());
						cv.put("secondary",model.getSecondary());
						cv.put("insitu",model.getInsitu());
						cv.put("benign",model.getBenign());
						cv.put("tumor",model.getTumor());
						cv.put("remark", model.getRemark());
						cv.put("data", new Gson().toJson(model));
						long ret=db_cancer.insert("cancer", null, cv);//执行插入操作
						System.out.println("insert "+ret);

					}

					System.out.println(PageIndex+","+page.getPageCount());

					//	break;
					Message msg=new Message();
					msg.what=0;
					Bundle bd=new Bundle();

					int per=(int)(PageIndex*1.0/page.getPageCount()*100);
					bd.putString("lib","TumorLib");
					bd.putString("per",per+"%");
					msg.setData(bd);
					hander.sendMessage(msg);

					PageIndex++;
					if(PageIndex>page.getPageCount())
						break;


			}

			//drug
			PageIndex=1;
			while(true){

				String data=apnApi.getPage("DrugChemical", PageIndex, PageSize);

				if(data==null||data.equals(""))
					break;

				DrugPage page=new Gson().fromJson(data,DrugPage.class);

				if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
					break;


				for(int i=0;i<page.getDataTable().size();i++){
					DrugChemicalSelectResult model=page.getDataTable().get(i);
					System.out.println(page.getDataTable().get(i).getDrugOrChemicalProduct());
					ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
					cv.put("drugorchemicalproduct",model.getDrugOrChemicalProduct());
					cv.put("poisoning",model.getPoisoning());
					cv.put("accident",model.getAccident());
					cv.put("intentionalselfharm",model.getIntentionalSelfHarm());
					cv.put("unknownintent",model.getUnknownIntent());
					cv.put("advesreeffect",model.getAdvesreEffect());
					cv.put("data", new Gson().toJson(model));
					long ret=db_drug.insert("drug", null, cv);//执行插入操作
					System.out.println("insert "+ret);

				}

				System.out.println(PageIndex+","+page.getPageCount());

				//	break;
				Message msg=new Message();
				msg.what=0;
				Bundle bd=new Bundle();

				int per=(int)(PageIndex*1.0/page.getPageCount()*100);
				bd.putString("lib","DrugLib");
				bd.putString("per",per+"%");
				msg.setData(bd);
				hander.sendMessage(msg);

				PageIndex++;
				if(PageIndex>page.getPageCount())
					break;


			}


			//externalcause
			PageIndex=1;
			while(true){

				String data=apnApi.getPage("ExternalCause", PageIndex, PageSize);

				if(data==null||data.equals(""))
					break;

				CausePage page=new Gson().fromJson(data,CausePage.class);

				if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
					break;


				for(int i=0;i<page.getDataTable().size();i++){
					ExternalCauseSelectResult model=page.getDataTable().get(i);
					System.out.println(page.getDataTable().get(i).getCause());
					ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
					cv.put("cause",model.getCause());
					cv.put("icd10",model.getICD10());
					cv.put("data", new Gson().toJson(model));

					long ret=db_icd10.insert("icd10", null, cv);//执行插入操作
					System.out.println("insert "+ret);

				}

				System.out.println(PageIndex+","+page.getPageCount());

				//	break;
				Message msg=new Message();
				msg.what=0;
				Bundle bd=new Bundle();

				int per=(int)(PageIndex*1.0/page.getPageCount()*100);
				bd.putString("lib","ExtCauseLib");
				bd.putString("per",per+"%");
				msg.setData(bd);
				hander.sendMessage(msg);

				PageIndex++;
				if(PageIndex>page.getPageCount())
					break;


			}

			//land
			PageIndex=1;
			while(true){

				String data=apnApi.getPage("LandTransport", PageIndex, PageSize);

				if(data==null||data.equals(""))
					break;

				LandPage page=new Gson().fromJson(data,LandPage.class);

				if(page==null||page.getDataTable()==null||page.getDataTable().size()<=0)
					break;


				for(int i=0;i<page.getDataTable().size();i++){
					LandTransportSelectResult model=page.getDataTable().get(i);
					System.out.println(page.getDataTable().get(i).getCode());
					ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
					cv.put("keyword1",model.getKeyword1());
					cv.put("keyword2",model.getKeyword2());
					cv.put("code",model.getCode());
					cv.put("data", new Gson().toJson(model));

					long ret=db_land.insert("land", null, cv);//执行插入操作
					System.out.println("insert "+ret);

				}

				System.out.println(PageIndex+","+page.getPageCount());

				//	break;
				Message msg=new Message();
				msg.what=0;
				Bundle bd=new Bundle();

				int per=(int)(PageIndex*1.0/page.getPageCount()*100);
				bd.putString("lib","LandTransportLib");
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
