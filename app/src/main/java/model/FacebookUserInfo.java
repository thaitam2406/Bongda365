package model;

import java.io.Serializable;

/**
 * Created by Tam Huynh on 1/31/2015.
 */
public class FacebookUserInfo implements Serializable{
    private String userName;
    private String thumbnail;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
