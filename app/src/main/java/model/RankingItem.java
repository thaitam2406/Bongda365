package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 1/5/2015.
 */
public class RankingItem {
    @SerializedName("played")
    private String numberMatched = "";

    private String ratioGoal = "";

    @SerializedName("point")
    private String point = "";
    @SerializedName("team")
    private Team teamItems;

    @SerializedName("goalAgainst")
    private String goalAgain = "";
    @SerializedName("goal")
    private String goal = "";

    public String getNumberMatched() {
        return numberMatched;
    }

    public void setNumberMatched(String numberMatched) {
        this.numberMatched = numberMatched;
    }

    public String getRatioGoal() {
        return String.valueOf(Integer.parseInt(getGoal()) - Integer.parseInt(getGoalAgain()));
    }

    public void setRatioGoal(String ratioGoal) {
        this.ratioGoal = ratioGoal;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }


    public String getGoalAgain() {
        return goalAgain;
    }

    public void setGoalAgain(String goalAgain) {
        this.goalAgain = goalAgain;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Team getTeamItems() {
        return teamItems;
    }

    public void setTeamItems(Team teamItems) {
        this.teamItems = teamItems;
    }
}
