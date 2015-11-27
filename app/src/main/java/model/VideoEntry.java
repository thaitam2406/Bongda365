package model;

/**
 * Created by Tam Huynh on 1/8/2015.
 */
public class VideoEntry {
    private final String text;
    private final String videoId;

    public VideoEntry(String text, String videoId) {
        this.text = text;
        this.videoId = videoId;
    }

    public String getText() {
        return text;
    }

    public String getVideoId() {
        return videoId;
    }
}
