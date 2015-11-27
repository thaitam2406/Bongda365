package model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import util.Util;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class DetailPage {
    @SerializedName("ID")
    private int mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("excerpt")
    private String subTitle;
    @SerializedName("url")
    private String urlArticle;
    @SerializedName("datePost")
    private String mDate;
    @SerializedName("content")
    private ArrayList<Content> content = new ArrayList<>();
    private String sourceName;
    private String sourceIcon;
    private ArrayList<Integer> idArrayText = new ArrayList<>();
    private ArrayList<Integer> idArrayHint = new ArrayList<>();

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDate(boolean isGetTimeNormal) {
        if(isGetTimeNormal)
            return mDate;
        else
            return Util.parseTime(mDate);
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceIcon() {
        return sourceIcon;
    }

    public void setSourceIcon(String sourceIcon) {
        this.sourceIcon = sourceIcon;
    }

    public ArrayList<Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Content> content) {
        this.content = content;
    }

    public ArrayList<Integer> getIdArrayText() {
        return idArrayText;
    }

    public void setIdArrayText(ArrayList<Integer> idArrayText) {
        this.idArrayText = idArrayText;
    }

    public ArrayList<Integer> getIdArrayHint() {
        return idArrayHint;
    }

    public void setIdArrayHint(ArrayList<Integer> idArrayHint) {
        this.idArrayHint = idArrayHint;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    public void setUrlArticle(String urlArticle) {
        this.urlArticle = urlArticle;
    }
}
