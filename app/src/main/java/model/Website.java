package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bongda365.API;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tam Huynh on 2/7/2015.
 */
public class Website implements Parcelable{
    @SerializedName("name")
    private String nameWebsite;
    @SerializedName("url")
    private String urlWebsite;
    @SerializedName("logo")
    private String logoArticle;

    public String getNameWebsite() {
        return nameWebsite;
    }

    public void setNameWebsite(String nameWebsite) {
        this.nameWebsite = nameWebsite;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }

    public void setUrlWebsite(String urlWebsite) {
        this.urlWebsite = urlWebsite;
    }

    public String getLogoArticle() {
        return API.headerThumbnail + logoArticle;
    }

    public void setLogoArticle(String logoArticle) {
        this.logoArticle = logoArticle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameWebsite);
        dest.writeString(urlWebsite);
        dest.writeString(logoArticle);

    }
    public static Creator CREATOR = new Creator() {
        @Override
        public Website createFromParcel(Parcel source) {
            Website website = new Website();
            website.nameWebsite = source.readString();
            website.urlWebsite = source.readString();
            website.logoArticle = source.readString();
            return website;
        }

        @Override
        public Website[] newArray(int size) {
            return new Website[0];
        }
    };
}
