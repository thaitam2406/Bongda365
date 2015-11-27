package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bongda365.Constant;

import model.FacebookUserInfo;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class SharePreference {

    private String TAG = "SharePreferenceManager";
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String THEME_APP = "THEME_APP-enable";
    private static final String ON_OFF_GUIDE_VIDEO = "ON_OFF_GUIDE_VIDEO";
    private static final String ON_OFF_PUSH_HOT_NEW = "ON_OFF_PUSH_HOT_NEW";
    private static final String FACEBOOK_USER_NAME = "FACEBOOK_USER_NAME";
    private static final String FACEBOOK_THUMBNAIL = "FACEBOOK_THUMBNAIL";


    public SharePreference(Context context) {
        mContext = context;
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        editor = sharedPreferences.edit();
    }

    public String getThemeApp() {
        return sharedPreferences.getString(THEME_APP, Constant.THEME_DEFAULT);
    }

    public void setThemeApp(String theme){
        editor.putString(THEME_APP,theme).commit();
    }

    public boolean getOnOffGuideVideo() {
        return sharedPreferences.getBoolean(ON_OFF_GUIDE_VIDEO,true);
    }

    public void setOnOffGuideVideo(boolean on_off){
        editor.putBoolean(ON_OFF_GUIDE_VIDEO,on_off).commit();
    }

    public boolean getOnOffPushHotNews() {
        return sharedPreferences.getBoolean(ON_OFF_PUSH_HOT_NEW,true);
    }

    public void setOnOffPushHotNews(boolean on_off){
        editor.putBoolean(ON_OFF_PUSH_HOT_NEW,on_off).commit();
    }

    public void saveFacebook(FacebookUserInfo facebookUserInfo) {
        editor.putString(FACEBOOK_USER_NAME, facebookUserInfo.getUserName());
        editor.putString(FACEBOOK_THUMBNAIL, facebookUserInfo.getThumbnail());
        editor.commit();
    }

    public FacebookUserInfo loadFacebook() {
        FacebookUserInfo facebookUserInfo = new FacebookUserInfo();
        facebookUserInfo.setUserName(sharedPreferences.getString(FACEBOOK_USER_NAME,""));
        facebookUserInfo.setThumbnail(sharedPreferences.getString(FACEBOOK_THUMBNAIL,""));
        return facebookUserInfo;

    }


}
