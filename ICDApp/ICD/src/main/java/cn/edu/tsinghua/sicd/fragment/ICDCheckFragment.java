package cn.edu.tsinghua.sicd.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.edu.tsinghua.sicd.MainActivity;
import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.models.ICDCheck_CodenameResult;
import cn.edu.tsinghua.sicd.models.QuickSearchSelectResult;
import cn.edu.tsinghua.sicd.utils.SimpleThreadHandler;
import cn.edu.tsinghua.sicd.webapi.APNApi;

/**
 * Created by lxdbu on 2017/4/25.
 */

public class ICDCheckFragment extends Fragment {

    private APNApi apnApi;
    private TableLayout tableLayout;
    private SearchView sv_check;
    private Button btn_check;
    private String icd_code;
    private Boolean found=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apnApi = new APNApi(getActivity());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_check, null);

        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        sv_check = (SearchView) view.findViewById(R.id.sv_check);
        btn_check = (Button) view.findViewById(R.id.btn_check);

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllRow();
                found=false;
                icd_code=sv_check.getQuery().toString();
                new ICDCheckFragment.CheckCodeInfo(icd_code).doInBackground();
            }
        });

        /*sv_check.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                removeAllRow();
                found=false;
                icd_code=query;
                new ICDCheckFragment.CheckCodeInfo(query).doInBackground();
                diag(found);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        return view;
    }

    public void diag(Boolean found){
        if (found==false){
            AlertDialog.Builder dialog = new AlertDialog.Builder
                    (getActivity());
            dialog.setTitle("ICD 系统");
            dialog.setMessage("未找到编码.");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.
                    OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
    }

    public class CheckCodeInfo extends SimpleThreadHandler {
        private String text;

        public CheckCodeInfo(String text) {
            this.text = text;
        }

        @Override
        protected String runInBackend() {

            String ret=apnApi.getCodeInfo(text);
            if (ret!=null&&!ret.equals("")){
                found=true;
            }
            diag(found);
            return ret;
        }
        ;
        @Override
        protected void runInFrontend(String data) {

            icd_code = "";

            if (data != null && !data.equals("")) {

                Log.d("api", data);


                try {
                    Gson gson = new Gson();

                    ICDCheck_CodenameResult model = gson.fromJson(data, ICDCheck_CodenameResult.class);
                    if (model != null) {
                        removeAllRow();

                        //addRow("DiagnosisTerm",model.getDiagnosisTerm());
                        addRow("Name", model.getName());

                        addRow("Code Type", model.getCodeType());
                        addRow("Page", model.getPage());
                        //	addRow("Remark",model.getRemark());

                    }

                } catch (Exception ex) {

                }

            } else {

                Log.d("api", "no data");

            }
        }
    }

        private void addRow(String key, String value) {
            TableRow tableRow = new TableRow(this.getActivity());

            if (!key.equals(""))
                tableRow.setBackgroundColor(Color.parseColor("#E0E0E0"));


            TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
            lp.setMargins(0, 10, 0, 0);

            tableRow.setLayoutParams(lp);


            TextView keyView = new TextView(this.getActivity());
            TextView valueView = new TextView(this.getActivity());


            keyView.setText(key);
            keyView.setTextSize(15);
            keyView.setTextColor(Color.rgb(30, 144, 255));
            valueView.setText(value);
            valueView.setTextSize(15);
            valueView.setWidth(600);
            valueView.setPadding(10,0,5,0);

            tableRow.addView(keyView);
            tableRow.addView(valueView);

            tableLayout.addView(tableRow);

        }

        private void removeAllRow() {

            tableLayout.removeAllViews();

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
