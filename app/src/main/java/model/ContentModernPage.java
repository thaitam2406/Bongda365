package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import util.Util;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class ContentModernPage implements Parcelable{
    @SerializedName("ID")
    private String id = "";
    @SerializedName("title")
    private String title = "";
    @SerializedName("exerpt")
    private String subTitle = "";
    @SerializedName("featureImage")
    private String thumbImg = "";
    @SerializedName("datePost")
    private String dateTime = "";
    @SerializedName("website")
    private Website website;
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(thumbImg);
        dest.writeString(dateTime);
        dest.writeParcelable(website,flags);
    }
    public static Creator CREATOR = new Creator() {
        @Override
        public ContentModernPage createFromParcel(Parcel source) {
            ContentModernPage contentModernPage = new ContentModernPage();
            contentModernPage.id = source.readString();
            contentModernPage.title = source.readString();
            contentModernPage.thumbImg = source.readString();
            contentModernPage.dateTime = source.readString();
            contentModernPage.website = source.readParcelable(Website.class.getClassLoader());
            return contentModernPage;
        }

        @Override
        public ContentModernPage[] newArray(int size) {
            return new ContentModernPage[0];
        }
    };
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getThumbnailSource() {
        if(website ==  null) return null;
        return website.getLogoArticle();
    }

    public void setThumbnailSource(String thumbnailSource) {
        if(website == null) website = new Website();
        this.website.setLogoArticle(thumbnailSource);
    }

    public String getNameSource() {
        if(website == null) return null;
        return website.getNameWebsite();
    }

    public void setNameSource(String nameSource) {
        if(website == null) website = new Website();
        this.website.setNameWebsite(nameSource);
    }

    public String getDateTime(boolean isNormal) {
        if(isNormal)
            return dateTime;
        else
            return Util.parseTime(dateTime);
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
