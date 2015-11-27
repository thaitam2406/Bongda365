package com.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bongda365.Constant;
import com.tamhuynh.bongda365.R;

import library.view.TextViewSelector;
import util.ShareManagement;

/**
 * Created by Tam Huynh on 1/9/2015.
 */
public class SettingFragment extends BaseFragment implements TextViewSelector.IOnClickSelector, CompoundButton.OnCheckedChangeListener {
    private TextViewSelector textViewSelector;
    private Switch switch1,switch2;
    private IOnRestartApp onRestartApp;
    public interface IOnRestartApp{
        public void restartApp();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onRestartApp = (IOnRestartApp)activity;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initUI() {
        textViewSelector = (TextViewSelector) view.findViewById(R.id.tvSelector);
        textViewSelector.setOnClickSelector(this);
        if(Constant.THEME_BLACK.equals(new ShareManagement(mContext).getThemeApp())){
            textViewSelector.setHightlightBtnRight();
        }else
            textViewSelector.setHightlightBtnLeft();
        switch1 = (Switch)   view.findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);
        switch2= (Switch)   view.findViewById(R.id.switch2);
        switch2.setOnCheckedChangeListener(this);
        if(new ShareManagement(mContext).getOnOffGuideVideo())
            switch1.setChecked(true);
        if(new ShareManagement(mContext).getOnOffPushHotNews())
            switch2.setChecked(true);
    }

    @Override
    public void initData() {

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
        setXml(R.layout.fragment_setting);
    }

    @Override
    public void onLeftClick() {
        new ShareManagement(mContext).setThemApp(Constant.THEME_WHITE);
        showDialog();
    }

    @Override
    public void onRightClick() {
        new ShareManagement(mContext).setThemApp(Constant.THEME_BLACK);
        showDialog();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if(id == switch1.getId() ){
            if(isChecked){
                new ShareManagement(mContext).setOnOffGuideVideo(true);
            }else{
                new ShareManagement(mContext).setOnOffGuideVideo(false);
            }
        }else if(id == switch2.getId() ){
            if(isChecked){
                new ShareManagement(mContext).setOnOffPushHotNews(true);
            }else{
                new ShareManagement(mContext).setOnOffPushHotNews(false);
            }
        }

    }
    private void showDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Cài đặt theme")
                .setMessage("Bạn có muốn thay đổi theme? Ứng dụng sẽ khởi động lại ngay. Bấm hủy để thực hiện ở lần khởi động ứng dụng tiếp theo")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        onRestartApp.restartApp();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
