package com.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import MVP.presenter.ModernContentPresenter;
import MVP.presenter.ModernContentPresenterImpl;
import MVP.view.ModernContentView;
import adapter.ContentCategoryModernV1Adapter;
import library.anim.AnimUtil;
import model.ContentModernPage;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * Created by Tam Huynh on 12/29/2014.
 */
public class ListArticleModern extends BaseFragment implements
        FlipView.OnOverFlipListener,FlipView. OnFlipListener, View.OnClickListener,
        ModernContentView{

    private FlipView flipView;
    private ContentCategoryModernV1Adapter adapterFlipview;
    private ViewGroup rlProgressBar;
    private ModernContentPresenter modernContentPresenter;
    public static String contentIdKey = "contentIdKey";
    private String contentID;

    public interface IOnContentItemClick{
        public void onContentItemClick(int id);
    }
    private void getBundle(){
        Bundle bundle = getArguments();
        contentID = bundle.getString(contentIdKey);
    }
    @Override
    public void initUI() {
        flipView = (FlipView) view.findViewById(R.id.flip_view);
        rlProgressBar = (ViewGroup) view.findViewById(R.id.progressBar);
        getBundle();
        /*ContentModernPage contentModernPage = new ContentModernPage();
        contentModernPage.setTitle("title");
        contentModernPage.setSubTitle("SubTitle");
        contentModernPage.setNameSource("TT");
        contentModernPage.setDateTime("2h trước");
        contentModernPage.setThumbnailSource("http://www.slate.com/content/dam/slate/blogs/the_spot/2014/07/13/argentina_germany_2014_world_cup_lionel_messi_is_sad/452110974-argentinas-forward-and-captain-lionel-messi-reacts.jpg.CROP.promo-mediumlarge.jpg");
        for(int i = 0; i < 7; i++)
            arrayContent.add(contentModernPage);
        adapterFlipview = new ContentCategoryModernV1Adapter(mContext, arrayContent);
        flipView.setAdapter(adapterFlipview);
        flipView.peakNext(true);
        flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        flipView.setOnFlipListener(this);
        flipView.setOnOverFlipListener(this);*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.modernContentPresenter = new ModernContentPresenterImpl(this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        this.modernContentPresenter.getDataContentModern(mContext, contentID);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setXml(R.layout.fragment_modern_content);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onItemLvClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {
        /**
         * get more data when near last item 5 index
         */
        if(position == this.contentModernPages.size() - 5){
            initData();
        }
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }
    private ArrayList<ContentModernPage> contentModernPages = new ArrayList<>();
    @Override
    public void setAdapter(ArrayList<ContentModernPage> contentModernPages) {
        if(adapterFlipview == null){
            this.contentModernPages = contentModernPages;
            adapterFlipview = new ContentCategoryModernV1Adapter(mContext, contentModernPages);
            adapterFlipview.notifyDataSetChanged();
            flipView.setAdapter(adapterFlipview);
            flipView.peakNext(true);
            flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
            flipView.setOnFlipListener(this);
            flipView.setOnClickListener(this);
            flipView.setOnOverFlipListener(this);
        }else {
            this.contentModernPages.addAll(contentModernPages);
            adapterFlipview.notifyDataSetChanged();
        }

    }

    @Override
    public void showProgress() {
        rlProgressBar.setVisibility(View.VISIBLE);
//        AnimUtil.AlphaFadeIn(rlProgressBar);
    }
    @Override
    public void hideProgress() {
//        rlProgressBar.setVisibility(View.GONE);
        AnimUtil.AlphaFadeOut(rlProgressBar);
    }
}
