package cn.edu.tsinghua.sicd.webapi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.sicd.db.CancerDatabaseHelper;
import cn.edu.tsinghua.sicd.db.DiseaseInjuryDatabaseHelper;
import cn.edu.tsinghua.sicd.db.DrugDatabaseHelper;
import cn.edu.tsinghua.sicd.db.ICD10DatabaseHelper;
import cn.edu.tsinghua.sicd.db.LandDatabaseHelper;
import cn.edu.tsinghua.sicd.db.QuickSearchHelper;
import cn.edu.tsinghua.sicd.models.APNSingleResult;
import cn.edu.tsinghua.sicd.models.CancerTumorSelectResult;
import cn.edu.tsinghua.sicd.models.DrugChemicalSelectResult;
import cn.edu.tsinghua.sicd.models.ExternalCauseSelectResult;
import cn.edu.tsinghua.sicd.models.ICDCheck_CodenameResult;
import cn.edu.tsinghua.sicd.models.LandTransportSelectResult;
import cn.edu.tsinghua.sicd.models.QuickSearchSelectResult;
import cn.edu.tsinghua.sicd.utils.PreferenceUtils;
import cn.edu.tsinghua.sicd.utils.RawWebApiServer;

/**
 * Created by douglaschan on 2016/3/9.
 */
public class APNApi {

    private RawWebApiServer server;
    private Context context;

    SQLiteDatabase db_disease;
    SQLiteDatabase db_cancer;
    SQLiteDatabase db_drug;
    SQLiteDatabase db_icd10;
    SQLiteDatabase db_land;
    SQLiteDatabase db_quicksearch;

    public APNApi(Context context) {

        this.server = new RawWebApiServer(context);
        this.context = context;

        db_disease = new DiseaseInjuryDatabaseHelper(context).getWritableDatabase();
        db_cancer = new CancerDatabaseHelper(context).getWritableDatabase();
        db_drug = new DrugDatabaseHelper(context).getWritableDatabase();
        db_icd10 = new ICD10DatabaseHelper(context).getWritableDatabase();
        db_land = new LandDatabaseHelper(context).getWritableDatabase();
        db_quicksearch=new QuickSearchHelper(context).getWritableDatabase();

    }

    public String getPage(String model, int PageIndex, int PageSize) {
        return server.call("get", model, new String[]{"PageIndex", "PageSize"}, new String[]{"" + PageIndex, "" + PageSize});
    }

    public String getAutoSearchList(String inputTerm) {

        String ret = "";

        List<APNSingleResult> ulist = new ArrayList<APNSingleResult>();

        Cursor cursor = db_disease.query("diseaseinjury", null, "DiagnosisTerm like ?", new String[]{getTerm(inputTerm)}, null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                APNSingleResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), APNSingleResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() == 0) {

            ret = server.call("get", "DiseaseInjury", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});

        } else {
            return ret;
        }

        return ret;

    }

    String getTerm(String term) {
        String str = PreferenceUtils.getSearchMode(server.getContext());
        if (str.equals("0")) {
            return term + "%";
        } else
            return "%" + term + "%";
    }

    public String getSingle(String inputTerm) {
        APNSingleResult model = null;

        Cursor cursor = db_disease.query("diseaseinjury", null, "DiagnosisTerm = ?", new String[]{"" + inputTerm + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), APNSingleResult.class);
            return new Gson().toJson(model);
        }

        return server.call("post", "DiseaseInjury", new String[]{"Term"}, new String[]{inputTerm});

    }

    public String getCancerList(String inputTerm) {

        String ret = "";

        List<CancerTumorSelectResult> ulist = new ArrayList<CancerTumorSelectResult>();

        Cursor cursor = db_cancer.query("cancer", null, "keyword like ?", new String[]{getTerm(inputTerm)}, null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                CancerTumorSelectResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), CancerTumorSelectResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() <= 0) {

            return server.call("get", "CancerTumor", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});

        } else {
            return ret;
        }
    }

    public String getCancer(String inputTerm) {

        CancerTumorSelectResult model = null;

        Cursor cursor = db_cancer.query("cancer", null, "keyword = ?", new String[]{"" + inputTerm + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), CancerTumorSelectResult.class);
            return new Gson().toJson(model);
        }

        return server.call("post", "CancerTumor", new String[]{"Term"}, new String[]{inputTerm});
    }

    public String getCauseList(String inputTerm) {

        String ret = "";


        List<ExternalCauseSelectResult> ulist = new ArrayList<ExternalCauseSelectResult>();

        Cursor cursor = db_icd10.query("icd10", null, "cause like ?", new String[]{getTerm(inputTerm)}, null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                ExternalCauseSelectResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), ExternalCauseSelectResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() <= 0) {


            return server.call("get", "ExternalCause", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});

        } else {
            return ret;
        }
    }

    public String getCause(String inputTerm) {


        ExternalCauseSelectResult model = null;

        Cursor cursor = db_icd10.query("icd10", null, "cause = ?", new String[]{"" + inputTerm + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), ExternalCauseSelectResult.class);
            return new Gson().toJson(model);
        }


        return server.call("post", "ExternalCause", new String[]{"Term"}, new String[]{inputTerm});
    }


    public String getProductList(String inputTerm) {

        String ret = "";
        List<DrugChemicalSelectResult> ulist = new ArrayList<DrugChemicalSelectResult>();

        Cursor cursor = db_drug.query("drug", null, "drugorchemicalproduct like ?", new String[]{getTerm(inputTerm)}, null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                DrugChemicalSelectResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), DrugChemicalSelectResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() <= 0) {

            return server.call("get", "DrugChemical", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});

        } else {

            return ret;
        }
    }

    public String getProduct(String inputTerm) {
        DrugChemicalSelectResult model = null;

        Cursor cursor = db_drug.query("drug", null, "drugorchemicalproduct = ?", new String[]{"" + inputTerm + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), DrugChemicalSelectResult.class);
            return new Gson().toJson(model);
        }


        return server.call("post", "DrugChemical", new String[]{"Term"}, new String[]{inputTerm});
    }

    public String getLandList(String inputTerm) {

        String ret = "";

        List<LandTransportSelectResult> ulist = new ArrayList<LandTransportSelectResult>();

        Cursor cursor = db_land.query("land", null, "keyword1 like ? or  keyword2 like ?", new String[]{getTerm(inputTerm), getTerm(inputTerm)}, null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                LandTransportSelectResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), LandTransportSelectResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() <= 0) {
            return server.call("get", "LandTransport", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});
        } else {
            return ret;
        }
    }

    public String getLand(String inputTerm1) {

        LandTransportSelectResult model = null;

        Cursor cursor = db_land.query("land", null, "keyword1 = ?", new String[]{"" + inputTerm1 + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), LandTransportSelectResult.class);
            return new Gson().toJson(model);
        }

        return server.call("post", "LandTransport", new String[]{"Term1"}, new String[]{inputTerm1});
    }




    public String getUpdate(){
        return  server.call("get","update.xml",null,null);
    }

    public String getDiagnosis301List(String inputTerm) {
        String ret = "";

        List<QuickSearchSelectResult> ulist = new ArrayList<QuickSearchSelectResult>();

        Cursor cursor = db_quicksearch.query("tbldiagnosis_301", null, "term like ? or py like ?", new String[]{getTerm(inputTerm),getTerm(inputTerm)},null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                QuickSearchSelectResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), QuickSearchSelectResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() <= 0) {

            return server.call("get", "QuickSearch301", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});

        } else {
            return ret;
        }
    }

    public String getDiagnosis301(String inputTerm) {
        QuickSearchSelectResult model = null;


        Cursor cursor = db_quicksearch.query("tbldiagnosis_301", null, "term = ?", new String[]{"" + inputTerm + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), QuickSearchSelectResult.class);
            return new Gson().toJson(model);
        }

        return server.call("post", "QuickSearch301", new String[]{"Term"}, new String[]{inputTerm});
    }

    public String getDiagnosisBjList(String inputTerm) {
        String ret = "";

        List<QuickSearchSelectResult> ulist = new ArrayList<QuickSearchSelectResult>();

        Cursor cursor = db_quicksearch.query("tbldiagnosis_bj", null, "term like ? or py like ?", new String[]{getTerm(inputTerm),getTerm(inputTerm)}, null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                QuickSearchSelectResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), QuickSearchSelectResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() <= 0) {

            return server.call("get", "QuickSearchBj", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});

        } else {
            return ret;
        }
    }

    public String getDiagnosisBj(String inputTerm) {
        QuickSearchSelectResult model = null;


        Cursor cursor = db_quicksearch.query("tbldiagnosis_bj", null, "term = ?", new String[]{"" + inputTerm + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), QuickSearchSelectResult.class);
            return new Gson().toJson(model);
        }

        return server.call("post", "QuickSearchBj", new String[]{"Term"}, new String[]{inputTerm});
    }

    public String getDiagnosisWsbList(String inputTerm) {
        String ret = "";

        List<QuickSearchSelectResult> ulist = new ArrayList<QuickSearchSelectResult>();

        Cursor cursor = db_quicksearch.query("tbldiagnosis_wsb", null, "term like ? or py like ?", new String[]{getTerm(inputTerm),getTerm(inputTerm)}, null, null, null, null);
        if (cursor != null) {


            while (cursor.moveToNext()) {
                QuickSearchSelectResult model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), QuickSearchSelectResult.class);
                if (ulist.size() > 30)
                    break;
                ulist.add(model);
            }
            ret = new Gson().toJson(ulist);
        }

        if (ulist == null || ulist.size() <= 0) {

            return server.call("get", "QuickSearchWsb", new String[]{"Term", "Flag"}, new String[]{inputTerm, PreferenceUtils.getSearchMode(server.getContext())});

        } else {
            return ret;
        }
    }

    public String getDiagnosisWsb(String inputTerm) {
        QuickSearchSelectResult model = null;


        Cursor cursor = db_quicksearch.query("tbldiagnosis_wsb", null, "term = ?", new String[]{"" + inputTerm + ""}, null, null, null, null);
        while (cursor.moveToNext()) {
            model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("data")), QuickSearchSelectResult.class);
            return new Gson().toJson(model);
        }

        return server.call("post", "QuickSearchWsb", new String[]{"Term"}, new String[]{inputTerm});
    }

    //核对
    public String getCodeInfo(String inputTerm){
        ICDCheck_CodenameResult model=null;
        return server.call("get","ICDCheck",new String[]{"icd_code"},new String[]{inputTerm});
    }


}
