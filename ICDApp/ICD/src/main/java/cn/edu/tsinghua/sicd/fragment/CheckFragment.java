package cn.edu.tsinghua.sicd.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.webapi.APNApi;

/**
 * Created by lxdbu on 2017/4/25.
 */

public class CheckFragment extends Fragment {

    private APNApi apnApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apnApi=new APNApi(getActivity());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_check, null);
        return view;
    }
}
