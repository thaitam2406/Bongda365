package model;

import com.bongda365.API;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 1/20/2015.
 */
public class Team {
    @SerializedName("ID")
    private String ID = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("logoURL")
    private String logoURL = "";

    @SerializedName("nation")
    private String nation = "";

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

    public String getLogoURL() {
        return API.headerThumbnail +logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
