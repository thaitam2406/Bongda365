package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bongda365.API;
import com.tamhuynh.bongda365.Activity.VideoViewActivity;
import com.tamhuynh.bongda365.R;

import java.util.ArrayList;
import java.util.Arrays;

import MVP.view.IVideoGoalListView;
import adapter.VideoFootBallAdapter;
import adapter.VideoGoalAdapter;
import library.anim.AnimUtil;
import library.serviceAPI.APIHandler;
import library.serviceAPI.IServiceListener;
import library.serviceAPI.ResultCode;
import library.serviceAPI.ServiceAction;
import library.youtube.YoutubePlayerActivity;
import model.ContentVideoGoal;
import model.ServiceResponse;
import model.VideoFootBall;
import model.VideoGoalItem;

/**
 * Created by Tam Huynh on 12/27/2014.
 */
public class VideoGoalListFragment extends BaseFragment implements IVideoGoalListView, IServiceListener, AbsListView.OnScrollListener {
    private ListView listView;
    private VideoGoalAdapter videoGoalAdapter = null;
    private VideoFootBallAdapter videoFootBallAdapter;
    private ArrayList<VideoGoalItem> arrayVideoGoal = new ArrayList<VideoGoalItem>();
    private ArrayList<VideoFootBall> arrayVideoFootBall = new ArrayList<VideoFootBall>();
    private ViewGroup view_group_progressbar;
    private String typeAdapter;
    public static String typeAdapterKey = "typeAdapterKey";
    public static String typeAdapterGoalVideo = "typeAdapterGoalVideo";
    public static String typeAdapterFootballVideo = "typeAdapterFootballVideo";
    private VideoFootBallAdapter.TYPE_VIDEO_LIST typeVideoList;
    private int pagenumber = 1;
    private boolean flagCallAPI = true;
    @Override
    public void initUI() {
        listView = (ListView) view.findViewById(R.id.lv_video_goal_list);
        view_group_progressbar = (ViewGroup) view.findViewById(R.id.view_group_progressbar);
        showProgressBar();
        if(typeAdapter == null)
            initBundle();
        if(typeVideoList == VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_GOAL)
            callAPI(pagenumber,typeAdapter);

    }
    private void initBundle(){
        Bundle bundle = getArguments();
        if(bundle != null){
            try{
                typeAdapter = bundle.getString(typeAdapterKey);
                if(typeAdapter.equals(typeAdapterFootballVideo)){
                    typeVideoList = VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_FOOTBALL;
                }else if(typeAdapter.equals(typeAdapterGoalVideo)){
                    typeVideoList = VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_GOAL;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initData() {
        if(typeAdapter.equals(typeAdapterGoalVideo) && videoGoalAdapter == null ) {
            videoGoalAdapter = new VideoGoalAdapter(mContext, arrayVideoGoal, typeVideoList);
            listView.setAdapter(videoGoalAdapter);
            videoGoalAdapter.notifyDataSetChanged();
        }
        else if(typeAdapter.equals(typeAdapterFootballVideo) && videoFootBallAdapter == null) {
            videoFootBallAdapter = new VideoFootBallAdapter(mContext, arrayVideoFootBall, typeVideoList);
            listView.setAdapter(videoFootBallAdapter);
            videoFootBallAdapter.notifyDataSetChanged();
        }
        hideProgressBar();
    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    @Override
    public void onItemLvClick(AdapterView<?> parent, View view, int position, long id) {
        if(typeVideoList == VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_FOOTBALL){
            Intent it = new Intent(getActivity(), YoutubePlayerActivity.class);
            it.putExtra(YoutubePlayerActivity.objectVideoFootBallKey, arrayVideoFootBall.get(position));
            it.putExtra(YoutubePlayerActivity.arrayVideoFootBallKey, arrayVideoFootBall);
            startActivity(it);
        }else if(typeVideoList == VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_GOAL){
            Intent it = new Intent(getActivity(), VideoViewActivity.class);
            if(arrayVideoGoal.get(position).getContentVideoGoal() != null ) {
                ArrayList<String> a = parseVideo(arrayVideoGoal.get(position).getContentVideoGoal());
                it.putStringArrayListExtra(VideoViewActivity.arrayUrlKey,a);
                startActivity(it);
            }
            else
                Toast.makeText(mContext,"This video not avaiable now",Toast.LENGTH_LONG).show();
        }
    }
    private ArrayList<String> parseVideo(ArrayList<ContentVideoGoal> contentVideoGoals){
        ArrayList<String> array = new ArrayList<String>();
        for(int i = 0; i < contentVideoGoals.size(); i++)
            array.add(contentVideoGoals.get(i).getSrc());
        return array;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setXml(R.layout.video_goal);
    }

    @Override
    public void showProgressBar() {
        view_group_progressbar.invalidate();
        view_group_progressbar.setVisibility(View.VISIBLE);
//        AnimUtil.AlphaFadeIn(view_group_progressbar);
    }

    @Override
    public void hideProgressBar() {
//        view_group_progressbar.setVisibility(View.GONE);
        AnimUtil.AlphaFadeOut(view_group_progressbar);
    }

    @Override
    public void onCompleted(ServiceResponse result) {
        if(result.getAction() == ServiceAction.ActionGetListVideoGoal && result.getCode() == ResultCode.Success){
            flagCallAPI = true;
            VideoGoalItem[] videoGoalItems = (VideoGoalItem[]) result.getData();
            arrayVideoGoal.addAll(Arrays.asList(videoGoalItems));
            initData();
            initParam(videoGoalItems[videoGoalItems.length-1].getId(),
                    videoGoalItems[videoGoalItems.length-1].getDateTime().replace(" ","_"));
            videoGoalAdapter.notifyDataSetChanged();
        }else if(result.getAction() == ServiceAction.ActionGetVideoFootball && result.getCode() == ResultCode.Success) {
            flagCallAPI = true;
            VideoFootBall[] videoGoalItems = (VideoFootBall[]) result.getData();
            arrayVideoFootBall.addAll(Arrays.asList(videoGoalItems));
            initData();
            videoFootBallAdapter.notifyDataSetChanged();
        }
    }
    private String lastTime = "";
    private String lastID = "";
    public void initParam(String lastID, String lastTime){
        this.lastID = lastID;
        this.lastTime = lastTime;
    }


    @Override
    public void onFail() {
        flagCallAPI = false;
        hideProgressBar();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (flagCallAPI && (firstVisibleItem+visibleItemCount) == totalItemCount  && totalItemCount != 0){
            flagCallAPI = false;
            pagenumber = pagenumber + 1;
            callAPI(pagenumber,typeAdapter);
        }
    }
    private void callAPI(int pageNumber,String typeAdapter){
        showProgressBar();
        if(typeAdapter.equals(typeAdapterGoalVideo))
            new APIHandler(VideoGoalListFragment.this,getActivity()).executeAPI(API.getListVideoGoal
                .replace(API.lastTime, lastTime).replace(API.lastID,lastID), null, true,false,
                ServiceAction.ActionGetListVideoGoal);
        else
            new APIHandler(VideoGoalListFragment.this,getActivity()).executeAPI(API.getVideoFootBall.
                            replace(API.catVideo,catID).replace(API.pageNumber,String.valueOf(pageNumber)) , null, true,false,
                    ServiceAction.ActionGetVideoFootball);
    }
    private String catID;

    /**
     * This function just call from HeaderMenu when select Category Video,coming here through
     * Home Activity. reset numberPage is 1
     * @param cat
     */
    public void callAPIByCat(String cat){
        arrayVideoFootBall.clear();
        this.catID = cat;
        this.pagenumber = 1;
        callAPI(this.pagenumber,typeAdapterFootballVideo);
    }
}
