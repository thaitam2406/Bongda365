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

import adapter.RankingAdapter;
import library.anim.AnimUtil;
import library.serviceAPI.APIHandler;
import library.serviceAPI.IServiceListener;
import library.serviceAPI.ResultCode;
import library.serviceAPI.ServiceAction;
import model.RankingItem;
import model.ServiceResponse;

/**
 * Created by Tam Huynh on 1/5/2015.
 */
public class RankingFragment extends BaseFragment implements IScheduleMatchedView,IServiceListener {
    private ListView listView;
    private RankingAdapter adapterSchedule;
    private ArrayList<RankingItem> array = new ArrayList<RankingItem>();
    private ViewGroup view_group_progressbar;
    private int leagueID = 1;
    @Override
    public void initUI() {
        listView = (ListView) view.findViewById(R.id.lv_ranking);
        view_group_progressbar = (ViewGroup) view.findViewById(R.id.view_group_progressbar);
    }

    @Override
    public void initData() {

        if(adapterSchedule == null) {
            adapterSchedule = new RankingAdapter(mContext, array);
            listView.setAdapter(adapterSchedule);
            adapterSchedule.notifyDataSetChanged();
        }else{
            adapterSchedule.notifyDataSetChanged();
            listView.setSelection(0);
        }
        hideProgressBar();
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
        setXml(R.layout.fragment_ranking);
    }

    @Override
    public void showProgressBar() {
        view_group_progressbar.setVisibility(View.VISIBLE);
//        AnimUtil.AlphaFadeIn(view_group_progressbar);
    }

    @Override
    public void hideProgressBar() {
//        view_group_progressbar.setVisibility(View.GONE);
        AnimUtil.AlphaFadeOut(view_group_progressbar);
    }

    /**
     * call through HomeActivity because get List League first
     * @param leagueID
     */
    public void callAPI(int leagueID){
        showProgressBar();
        new APIHandler(RankingFragment.this,getActivity()).executeAPI(API.getRakingFootball.replace(API.leagueStr,String.valueOf(leagueID)), null, true,false,
                ServiceAction.ActionGetRankingFootball);
    }

    @Override
    public void onCompleted(ServiceResponse result) {
        if(result.getAction() == ServiceAction.ActionGetRankingFootball && result.getCode() == ResultCode.Success){
            RankingItem[] rankingItems = (RankingItem[]) result.getData();
            array.clear();
            array.addAll(Arrays.asList(rankingItems));
            initData();
        }
    }

    @Override
    public void onFail() {
        hideProgressBar();

    }
}
