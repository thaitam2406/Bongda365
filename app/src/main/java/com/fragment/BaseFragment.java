package com.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

/**
 * Created by Tam Huynh on 12/27/2014.
 */
public abstract class BaseFragment extends Fragment implements AdapterView.OnItemClickListener{
    protected View view;
    protected int xml;
    protected Context mContext;
    public abstract void initUI();
    public abstract void initData();
    public abstract void initListener();

    /**
     * handle Item lv click
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public abstract void onItemLvClick(AdapterView<?> parent, View view, int position, long id);
    public void setXml(int xml){
        this.xml = xml;
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        initListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frame = new FrameLayout(getActivity());
        view = inflater.inflate(xml,null);
        frame.addView(view);
        return frame;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemLvClick(parent, view,  position,  id);
    }
}
