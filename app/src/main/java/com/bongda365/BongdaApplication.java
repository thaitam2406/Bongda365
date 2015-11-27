package com.bongda365;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Tam Huynh on 12/19/2014.
 */
public class BongdaApplication extends Application {
    private static BongdaApplication _instance;
    private int height, width;
    private boolean _networkState = false;

    public BongdaApplication() {
        super();
        _instance = this;
    }

    public static BongdaApplication Instance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        height = getHeight();
        width = getWidth();
    }

    private int getHeight() {
        Display display = ((WindowManager) BongdaApplication.Instance()
                .getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getHeight();
        return width;
    }

    public int getHeightBongDa() {
        return this.height;
    }

    public int getWidthBongDa() {
        return this.width;
    }

    private int getWidth() {
        Display display = ((WindowManager) BongdaApplication.Instance()
                .getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }

    public boolean is_networkState() {
        return _networkState;
    }

    public void set_networkState(boolean _networkState) {
        this._networkState = _networkState;
    }

}
