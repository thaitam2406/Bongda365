package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 2/17/2015.
 */
public class ContentVideoGoal {
    @SerializedName("type")
    private String type;
    @SerializedName("src")
    private String src;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
