package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 1/27/2015.
 */
public class ScheduleMatch {
    @SerializedName("ID")
    private String id;
    @SerializedName("homeTeam")
    private MatchItem homeTeam;
    @SerializedName("awayTeam")
    private MatchItem awayTeam;
    @SerializedName("stadium")
    private String stadium;
    @SerializedName("dateTime")
    private String dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MatchItem getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(MatchItem homeTeam) {
        this.homeTeam = homeTeam;
    }

    public MatchItem getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(MatchItem awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
