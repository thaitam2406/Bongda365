package model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tam Huynh on 1/22/2015.
 */
public class CatVideoFootball implements Serializable {
    @SerializedName("ID")
    private
    String ID;
    @SerializedName("name")
    private
    String name;

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
}

