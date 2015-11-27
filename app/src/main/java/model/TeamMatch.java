package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 1/27/2015.
 */
public class TeamMatch {

    @SerializedName("name")
    private String name = "";

    @SerializedName("logoURL")
    private String logoURL = "";

    @SerializedName("nation")
    private String nation = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoURL() {
        return logoURL;
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
