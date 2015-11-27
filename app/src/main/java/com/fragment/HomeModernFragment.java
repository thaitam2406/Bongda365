package com.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import MVP.presenter.ModernCatPresenter;
import MVP.presenter.ModernCatPresenterImpl;
import MVP.view.ModernStyleCategoryView;
import adapter.MainCategoryAdapter;
import library.anim.AnimUtil;
import model.ModernNew;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * Created by Tam Huynh on 12/29/2014.
 */
public class HomeModernFragment extends BaseFragment implements
        FlipView.OnOverFlipListener, FlipView.OnFlipListener, View.OnClickListener, ModernStyleCategoryView {

    private FlipView flipView;
    private MainCategoryAdapter adapterFlipview;
    private ModernCatPresenter modernCatPresenter;
    private ViewGroup rlProgressBar;
    private TextView tv_page_number,tv_footer_line;
    private String totalPage = "";

    @Override
    public void setAdapter(ArrayList<ModernNew> modernNews) {
        adapterFlipview = new MainCategoryAdapter(mContext, modernNews);
        flipView.setAdapter(adapterFlipview);
        flipView.peakNext(true);
        flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        flipView.setOnFlipListener(this);
        flipView.setOnClickListener(this);
        flipView.setOnOverFlipListener(this);
    }

    public void showProgress() {
        rlProgressBar.setVisibility(View.VISIBLE);
//        AnimUtil.AlphaFadeIn(rlProgressBar);
    }
    @Override
    public void hideProgress() {
//        rlProgressBar.setVisibility(View.GONE);
        AnimUtil.AlphaFadeOut(rlProgressBar);
        showFooterLine();
    }

    @Override
    public void setPageNumber(String number) {
        tv_page_number.setText(getString(R.string.pageText) + " " + number + "/" + this.totalPage);
    }

    @Override
    public void setTotalPage(String total) {
        this.totalPage = total;
    }

    @Override
    public void hideFooterLine() {
        tv_footer_line.setVisibility(View.GONE);
    }

    @Override
    public void showFooterLine() {
        tv_footer_line.setVisibility(View.VISIBLE);
    }

    public interface IOnCategoryChoose {
        public void onClickItemCategory(int ID, String nameCat);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            modernCatPresenter = new ModernCatPresenterImpl(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initUI() {
        flipView = (FlipView) view.findViewById(R.id.flip_view);
        rlProgressBar = (ViewGroup) view.findViewById(R.id.progressBar);
        tv_page_number = (TextView) view.findViewById(R.id.tv_page_number);
        tv_footer_line = (TextView) view.findViewById(R.id.v1_footer);

    }

    @Override
    public void initData() {
        modernCatPresenter.getDataCatModern(getActivity());
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setXml(R.layout.fragment_modern_style);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onItemLvClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {
        setPageNumber(String.valueOf(position+1));
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }
}
