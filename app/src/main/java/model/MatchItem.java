package model;

import com.bongda365.API;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 1/27/2015.
 */
public class MatchItem {
    @SerializedName("name")
    private
    String name = "";
    @SerializedName("logoURL")
    private
    String thumbnailImg = "";
    @SerializedName("nation")
    private
    String nation = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailImg() {
        return API.headerThumbnail + thumbnailImg;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
