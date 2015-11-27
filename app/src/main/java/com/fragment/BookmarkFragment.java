package com.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import MVP.view.BookmarkView;
import adapter.BookmarkAdapter;
import library.anim.AnimUtil;
import library.database.DatabaseManagement;
import model.Bookmark;

/**
 * Created by Tam Huynh on 1/26/2015.
 */
public class BookmarkFragment extends BaseFragment implements BookmarkView{
    private ListView lv_bookmark;
    private ViewGroup view_group_progressbar;
    private BookmarkAdapter bookmarkAdapter;
    private IOnBookmark onBookmark;

    public interface IOnBookmark{
        public void onItemBookmarkedClick(String idNew);
    }
    @Override
    public void initUI() {
        lv_bookmark = (ListView) view.findViewById(R.id.lv_bookmark);
        view_group_progressbar = (ViewGroup) view.findViewById(R.id.view_group_progressbar);
        showProgressBar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onBookmark = (IOnBookmark) activity;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        new AsyncTask<ArrayList<Bookmark>,Void,ArrayList<Bookmark>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected ArrayList<Bookmark> doInBackground(ArrayList<Bookmark>... params) {
                return new DatabaseManagement(mContext).getBookmarkData();
            }

            @Override
            protected void onPostExecute(ArrayList<Bookmark> bookmarks) {
                super.onPostExecute(bookmarks);
                bookmarkAdapter = new BookmarkAdapter(mContext,bookmarks);
                lv_bookmark.setAdapter(bookmarkAdapter);
                hideProgressBar();
            }
        }.execute();

    }

    @Override
    public void initListener() {
        lv_bookmark.setOnItemClickListener(this);

    }

    @Override
    public void onItemLvClick(AdapterView<?> parent, View view, int position, long id) {
        onBookmark.onItemBookmarkedClick((String) parent.getAdapter().getItem(position));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setXml(R.layout.bookmark_fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void showProgressBar() {
        view_group_progressbar.invalidate();
        view_group_progressbar.setVisibility(View.VISIBLE);
//        AnimUtil.AlphaFadeIn(view_group_progressbar);
    }

    @Override
    public void hideProgressBar() {
//        view_group_progressbar.setVisibility(View.GONE);
        AnimUtil.AlphaFadeOut(view_group_progressbar);
    }
}
