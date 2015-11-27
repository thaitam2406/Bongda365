package model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tam Huynh on 12/27/2014.
 */
public class VideoGoalItem {
    @SerializedName("ID")
    private String id;
    @SerializedName("title")
    private String title = "";
    @SerializedName("exerpt")
    private String subtitle = "";
    @SerializedName("featureImage")
    private String thumbnailVideo = "";
    @SerializedName("websiteIcon")
    private String thumbnailCopyRight = "";
    @SerializedName("website")
    private Website website;
    @SerializedName("datePost")
    private String dateTime = "";
    private String viewerCount = "";
    @SerializedName("content")
    private ArrayList<ContentVideoGoal> contentVideoGoal;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getThumbnailVideo() {
        return thumbnailVideo;
    }

    public void setThumbnailVideo(String thumbnailVideo) {
        this.thumbnailVideo = thumbnailVideo;
    }

    public String getThumbnailCopyRight() {
        return thumbnailCopyRight;
    }

    public void setThumbnailCopyRight(String thumbnailCopyRight) {
        this.thumbnailCopyRight = thumbnailCopyRight;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(String viewerCount) {
        this.viewerCount = viewerCount;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public ArrayList<ContentVideoGoal> getContentVideoGoal() {
        return contentVideoGoal;
    }

    public void setContentVideoGoal(ArrayList<ContentVideoGoal> contentVideoGoal) {
        this.contentVideoGoal = contentVideoGoal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
