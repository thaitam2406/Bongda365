package library.database;

import android.content.Context;

import java.util.ArrayList;

import model.Bookmark;

/**
 * Created by Tam Huynh on 1/26/2015.
 */
public class DatabaseManagement {
    private Context mContext;
    private DatabaseCore databaseCore;

    public DatabaseManagement(Context mContext) {
        this.mContext = mContext;
        databaseCore = new DatabaseCore(mContext);
    }
    public void addBookmark(Bookmark bookmark){
        databaseCore.addBookmark(bookmark);
    }
    public int getTotalBookmark(){
        return databaseCore.GetTotalBookMark();
    }
    public boolean deleteBookmark(String articleID){
        return databaseCore.deleteOneBookamrk(articleID);
    }
    public boolean checkBookmarkExist(String articleID){
        return databaseCore.checkArticalBookmarkExist(articleID);
    }
    public ArrayList<Bookmark> getBookmarkData(){
        return databaseCore.getAllBookMark();
    }
}
