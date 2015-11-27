package util;

import android.content.Context;

import model.FacebookUserInfo;

/**
 * Created by Tam Huynh on 1/9/2015.
 */
public class ShareManagement {
    private Context mContext;
    SharePreference sharePreference;

    public ShareManagement(Context mContext) {
        this.mContext = mContext;
        this.sharePreference = new SharePreference(mContext);
    }

    public String getThemeApp(){
        return  sharePreference.getThemeApp();
    }
    public void setThemApp(String theme){
        sharePreference.setThemeApp(theme);
    }

    public boolean getOnOffGuideVideo() {
        return sharePreference.getOnOffGuideVideo();
    }

    public void setOnOffGuideVideo(boolean on_off){
        sharePreference.setOnOffGuideVideo(on_off);
    }


    public boolean getOnOffPushHotNews() {
        return sharePreference.getOnOffPushHotNews();
    }

    public void setOnOffPushHotNews(boolean on_off){
        sharePreference.setOnOffPushHotNews(on_off);
    }

    public void saveFacebook(FacebookUserInfo facebookUserInfo) {
        sharePreference.saveFacebook(facebookUserInfo);
    }


    public FacebookUserInfo loadFacebook() {
        return sharePreference.loadFacebook();
    }
}
