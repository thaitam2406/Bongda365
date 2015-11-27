package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 12/30/2014.
 */
public class ModernNew {
    @SerializedName("ID")
    private int id = 0;
    @SerializedName("name")
    private String name = "";
    @SerializedName("thumbImg")
    private Thumbnail thumbnail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
