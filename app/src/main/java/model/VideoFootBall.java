package model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tam Huynh on 1/10/2015.
 */
public class VideoFootBall implements Serializable {
    @SerializedName("ID")
    private String ID = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("thumbImg")
    private String thumbImg = "";
    @SerializedName("url")
    private String url = "";
    @SerializedName("createDate")
    private String createDate = "";
    @SerializedName("isHot")
    private String isHot = "";
    @SerializedName("category")
    private
    CatVideoFootball catVideoFootball;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public CatVideoFootball getCatVideoFootball() {
        return catVideoFootball;
    }

    public void setCatVideoFootball(CatVideoFootball catVideoFootball) {
        this.catVideoFootball = catVideoFootball;
    }
}
