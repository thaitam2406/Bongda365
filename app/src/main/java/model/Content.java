package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 2/9/2015.
 */
public class Content {
    @SerializedName("type")
    private String type;
    @SerializedName("src")
    private String src;
    @SerializedName("alt")
    private String alt;
    @SerializedName("desc")
    private String desc;
    @SerializedName("content")
    private String content;

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

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
