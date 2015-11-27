package com.fragment;

import android.interfaces.IScheduleMatchedView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bongda365.API;
import com.tamhuynh.bongda365.R;

import java.util.ArrayList;
import java.util.Arrays;

import adapter.ResultMatchesAdapter;
import library.serviceAPI.APIHandler;
import library.serviceAPI.IServiceListener;
import library.serviceAPI.ServiceAction;
import model.ResultMatch;
import model.ServiceResponse;

/**
 * Created by Tam Huynh on 1/5/2015.
 */
public class ResultMatchedFragment extends BaseFragment implements IScheduleMatchedView,IServiceListener {
    private ListView listView;
    private ResultMatchesAdapter adapterSchedule;
    private ArrayList<ResultMatch> array = new ArrayList<ResultMatch>();
    private ViewGroup view_group_progressbar;
    private String leagueID = "1";
    @Override
    public void initUI() {
        listView = (ListView) view.findViewById(R.id.lv_schedule_match);
        view_group_progressbar = (ViewGroup) view.findViewById(R.id.view_group_progressbar);

    }
    String[] arraySelectLeague ;
    @Override
    public void initData() {
        hideProgressBar();
        if(adapterSchedule == null){
            adapterSchedule = new ResultMatchesAdapter(mContext, array);
            listView.setAdapter(adapterSchedule);
            adapterSchedule.notifyDataSetChanged();
        }else {
            adapterSchedule.notifyDataSetChanged();
            listView.setSelection(0);
        }


    }

    @Override
    public void initListener() {

    }

    @Override
    public void onItemLvClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setXml(R.layout.fragment_schedule_match);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    /**
     * call through HomeActivity because get List League first
     * @param leagueIDstr
     */
    public void callAPI(String leagueIDstr){
        showProgressBar();
        new APIHandler(this,mContext).executeAPI(API.getResultMatch.replace(API.leagueStr,leagueIDstr),null,true,false, ServiceAction.ActionGetResultMatch);

    }

    @Override
    public void showProgressBar() {
        view_group_progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        view_group_progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onCompleted(ServiceResponse result) {
        ResultMatch[] scheduleMatches = (ResultMatch[]) result.getData();
        array.clear();
        array.addAll(Arrays.asList(scheduleMatches));
        initData();

    }

    @Override
    public void onFail() {
        hideProgressBar();
    }
}
