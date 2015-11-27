package com.tamhuynh.bongda365.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Session;
import com.fragment.BookmarkFragment;
import com.fragment.DetailArticcleFragment;
import com.fragment.HomeModernFragment;
import com.fragment.ListArticleModern;
import com.fragment.RankingFragment;
import com.fragment.ResultMatchedFragment;
import com.fragment.ScheduleMatchedFragment;
import com.fragment.SettingFragment;
import com.fragment.VideoGoalListFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import adapter.ContentCategoryModernV1Adapter;
import library.hearderMenu.HeaderMenuHome;
import model.ContentModernPage;
import util.ShareManagement;
import view.SlidingMenuItem;


public class HomeActivity extends SlidingFragmentActivity implements FragmentManager.OnBackStackChangedListener,
        SlidingMenuItem.IMenuItemClick,HomeModernFragment.IOnCategoryChoose
        , HeaderMenuHome.IOnHeaderMenuInterface,SettingFragment.IOnRestartApp,
        ContentCategoryModernV1Adapter.IOnContentCategoryClick,BookmarkFragment.IOnBookmark{
    private static final String TAG  = "HomeActivity";
    private HeaderMenuHome headerMenuHome;
    private android.support.v4.app.FragmentManager fm;
    private ArrayList<String> reUseFragment = new ArrayList<String>();
    private String CURRENT_FRAGMENT;
    private String nameCategory = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getResources().getIdentifier(new ShareManagement(this).getThemeApp(), "style", getPackageName()));
        setContentView(R.layout.activity_home);
        setBehindContentView(R.layout.menu_frame_sliding);
        initSlidingmenu();
        initUI();
        initreUserFragment();

        addFragment(SlidingMenuItem.MENU_ITEM_HOME, null, false);

    }

    private void initreUserFragment() {
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_HOME);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_VIDEO_GOAL);
        reUseFragment.add(SlidingMenuItem.MODERN_STYLE_PAGE);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_RANKING);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_SETTING);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_SCHEDULE);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_VIDEO_FOOTBALL);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_CONTENT_CAT_MODERN);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_SCHEDULE_TODAY);
        reUseFragment.add(SlidingMenuItem.MENU_ITEM_BOOKMARK);

    }
    private FrameLayout frameContent ;
    private void initUI() {
        fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(this);
        headerMenuHome = (HeaderMenuHome) fm.findFragmentById(R.id.frag_header_menu);
        frameContent = (FrameLayout) findViewById(R.id.content_frame);
    }
    private void setPosHeaderMenu(boolean isFull){
        if(isFull){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameContent.setLayoutParams(layoutParams);
            headerMenuHome.setTitleColor(getResources().getColor(R.color.transparent_80_percent_alpha));
        }else{
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW,headerMenuHome.getId());
            frameContent.setLayoutParams(layoutParams);
            headerMenuHome.setTitleColor(getResources().getColor(R.color.gray));
        }

    }

    private void initSlidingmenu() {

        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setShadowDrawable(R.drawable.sliding_menu_shadow);
        sm.setOnCloseListener(mCloseListener);
        sm.setOnOpenListener(mOpenListener);
    }

    private SlidingMenu.OnCloseListener mCloseListener = new SlidingMenu.OnCloseListener() {
        @Override
        public void onClose() {
            headerMenuHome.updateButtonToggle();
        }
    };

    private SlidingMenu.OnOpenListener mOpenListener = new SlidingMenu.OnOpenListener() {
        @Override
        public void onOpen() {
            headerMenuHome.setHighlightButtonToggle();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addFragment(String tag, Object object, boolean toggle) {
        //check if it is current item
        if (tag.equals(CURRENT_FRAGMENT)) {
            if (toggle)
                toggle();
            return;
        }
        // check if the fragment added
        Fragment fragMain = fm.findFragmentByTag(tag);
        if (fragMain != null) {
            //revert fragment already added before,remove all fragment added after
            fm.popBackStack(tag, 0);
            if (toggle)
                toggle();
            return;
        }
        if (tag.equals(SlidingMenuItem.MENU_ITEM_VIDEO_GOAL)) {
            fragMain = new VideoGoalListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VideoGoalListFragment.typeAdapterKey,VideoGoalListFragment.typeAdapterGoalVideo);
            fragMain.setArguments(bundle);
        } else if (tag.equals(SlidingMenuItem.MENU_ITEM_HOME)) {
            fragMain = new HomeModernFragment();
        } else if (tag.equals(SlidingMenuItem.MENU_ITEM_SCHEDULE)) {
            fragMain = new ScheduleMatchedFragment();
        } else if (tag.equals(SlidingMenuItem.MENU_ITEM_SCHEDULE_TODAY)) {

        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_RANKING)) {
            fragMain = new RankingFragment();
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_CONTENT_CAT_MODERN)) {
            fragMain = new ListArticleModern();
            fragMain.setArguments((Bundle) object);
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_VIDEO_FOOTBALL)) {
            fragMain = new VideoGoalListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VideoGoalListFragment.typeAdapterKey,VideoGoalListFragment.typeAdapterFootballVideo);
            fragMain.setArguments(bundle);
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_SETTING)) {
            fragMain = new SettingFragment();
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_DETAIL_NEWS)) {
            fragMain = new DetailArticcleFragment();
            fragMain.setArguments((Bundle) object);
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_BOOKMARK)) {
            fragMain = new BookmarkFragment();
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_RESULT_MATCH)){
            fragMain = new ResultMatchedFragment();
        }



        if (fragMain != null) {
            if (!reUseFragment.contains(CURRENT_FRAGMENT))
                getSupportFragmentManager().popBackStack();

            FragmentTransaction mTransaction = fm.beginTransaction();

            mTransaction.add(R.id.content_frame, fragMain, tag);

            mTransaction.addToBackStack(tag);
            mTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            mTransaction.commit();
        }

        if (toggle)
            toggle();
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fm = getSupportFragmentManager();
        try {
            String temp = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1)
                    .getName();
            if(temp.equals(SlidingMenuItem.MENU_ITEM_CONTENT_CAT_MODERN)||temp.equals(SlidingMenuItem.MENU_ITEM_HOME))
                setPosHeaderMenu(true);
            else
                setPosHeaderMenu(false);
            Log.w(TAG, "onBackStackChanged, current fragment=" + temp);
            /** update header state **/

            setCurrentFragment(temp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
            finish();
        }
    }
    private void setCurrentFragment(String temp) {
        CURRENT_FRAGMENT = temp;
        setHeaderMenuHome(temp);
    }

    /**
     * first item in Menu Item Left onClick
     *
     * @param view
     */
    public void firstItem(View view) {
        Toast.makeText(this, "onlick", Toast.LENGTH_LONG).show();
    }

    /**
     * toggle icon onclick
     *
     * @param view
     */
    public void onclickToggle(View view) {
        getSlidingMenu().showMenu(true);
        headerMenuHome.setHighlightButtonToggle();
    }

    /**
     * back icon onclick
     *
     * @param view
     */
    public void onclickBackHeaderMenu(View view) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }

    @Override
    public void onMenuItemClick(String tag) {
        if (tag.equals(SlidingMenuItem.MENU_ITEM_VIDEO_GOAL)) {
            addFragment(SlidingMenuItem.MENU_ITEM_VIDEO_GOAL, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_SCHEDULE)){
            addFragment(SlidingMenuItem.MENU_ITEM_SCHEDULE, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_SCHEDULE_TODAY)){
            addFragment(SlidingMenuItem.MENU_ITEM_SCHEDULE_TODAY, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_RANKING)){
            addFragment(SlidingMenuItem.MENU_ITEM_RANKING, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_HOME)){
            addFragment(SlidingMenuItem.MENU_ITEM_HOME, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_VIDEO_FOOTBALL)){
            addFragment(SlidingMenuItem.MENU_ITEM_VIDEO_FOOTBALL, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_SETTING)){
            addFragment(SlidingMenuItem.MENU_ITEM_SETTING, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_BOOKMARK)){
            addFragment(SlidingMenuItem.MENU_ITEM_BOOKMARK, null, true);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_RESULT_MATCH)){
            addFragment(SlidingMenuItem.MENU_ITEM_RESULT_MATCH, null, true);
        }
    }

    private void setHeaderMenuHome(String tag) {
        if (tag.equals(SlidingMenuItem.MENU_ITEM_HOME)) {
            headerMenuHome.setTitleHeaderMenu(getStringResource(R.string.app_name));
            headerMenuHome.setIconLeftHeaderMenu(true);
        } else if (tag.equals(SlidingMenuItem.MENU_ITEM_VIDEO_GOAL)) {
            headerMenuHome.setTitleHeaderMenu(getString(R.string.goal_video_list));
            headerMenuHome.setIconLeftHeaderMenu(true);
        } else if (tag.equals(SlidingMenuItem.MENU_ITEM_SCHEDULE_TODAY)) {
            headerMenuHome.setTitleHeaderMenu(getString(R.string.menu_item_schedule_today));
            headerMenuHome.setIconLeftHeaderMenu(true);
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_RANKING)) {
            headerMenuHome.setTitleHeaderMenu(getString(R.string.menu_item_ranking));
            headerMenuHome.setIconLeftHeaderMenu(true);
            headerMenuHome.setUISelectLeague();
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_VIDEO_FOOTBALL)) {
            headerMenuHome.setTitleHeaderMenu(getString(R.string.menu_item_video_football));
            headerMenuHome.setIconLeftHeaderMenu(true);
            headerMenuHome.setUISelectorCatVideo();
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_SETTING)) {
            headerMenuHome.setTitleHeaderMenu(getString(R.string.menu_item_setting));
            headerMenuHome.setIconLeftHeaderMenu(true);
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_CONTENT_CAT_MODERN)) {
            headerMenuHome.setTitleHeaderMenu(this.nameCategory);
            headerMenuHome.setIconLeftHeaderMenu(false);
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_DETAIL_NEWS)) {
            headerMenuHome.setTitleHeaderMenu(this.nameCategory);
            headerMenuHome.setIconLeftHeaderMenu(false);
        }else if(tag.equals(SlidingMenuItem.MENU_ITEM_BOOKMARK)){
            headerMenuHome.setTitleHeaderMenu(getString(R.string.menu_item_bookmark));
            headerMenuHome.setIconLeftHeaderMenu(false);
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_SCHEDULE)) {
            headerMenuHome.setTitleHeaderMenu(getString(R.string.menu_item_schedule));
            headerMenuHome.setIconLeftHeaderMenu(true);
            headerMenuHome.setUISelectLeague();
        }else if (tag.equals(SlidingMenuItem.MENU_ITEM_RESULT_MATCH)) {
            headerMenuHome.setTitleHeaderMenu(getString(R.string.menu_item_result_matched));
            headerMenuHome.setIconLeftHeaderMenu(true);
            headerMenuHome.setUISelectLeague();
        }
    }

    private String getStringResource(int a){
        return getString(a);
    }

    @Override
    public void onClickItemCategory(int ID,String nameCat) {
        this.nameCategory = nameCat;
        Bundle bundle = new Bundle();
        bundle.putString(ListArticleModern.contentIdKey,String.valueOf(ID));
        addFragment(SlidingMenuItem.MENU_ITEM_CONTENT_CAT_MODERN, bundle, false);
    }

    @Override
    public void onSelectLeague(int leagueID) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(SlidingMenuItem.MENU_ITEM_RANKING);
        if(frag != null && frag instanceof RankingFragment && CURRENT_FRAGMENT.equals(SlidingMenuItem.MENU_ITEM_RANKING)){
            ((RankingFragment) frag).callAPI(leagueID);
        }else {
            frag = getSupportFragmentManager().findFragmentByTag(SlidingMenuItem.MENU_ITEM_SCHEDULE);
            if(frag != null && frag instanceof ScheduleMatchedFragment&& CURRENT_FRAGMENT.equals(SlidingMenuItem.MENU_ITEM_SCHEDULE)){
                ((ScheduleMatchedFragment) frag).callAPI(String.valueOf(leagueID));
            }else{
                frag = getSupportFragmentManager().findFragmentByTag(SlidingMenuItem.MENU_ITEM_RESULT_MATCH);
                if(frag != null && frag instanceof ResultMatchedFragment&& CURRENT_FRAGMENT.equals(SlidingMenuItem.MENU_ITEM_RESULT_MATCH)){
                    ((ResultMatchedFragment) frag).callAPI(String.valueOf(leagueID));
                }
            }
        }
    }

    @Override
    public void onBackHeaderClick() {
        onBackPressed();
    }

    @Override
    public void onFinishGetCatVideo(String itemCatId) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(SlidingMenuItem.MENU_ITEM_VIDEO_FOOTBALL);
        if(frag != null && frag instanceof VideoGoalListFragment){
            ((VideoGoalListFragment)frag).callAPIByCat(itemCatId);
        }
    }

    @Override
    public void restartApp() {
        recreate();
    }

    @Override
    public void onClickContent(String contentId, ContentModernPage contentModernPage) {
        Bundle bundle = new Bundle();
        bundle.putString(ListArticleModern.contentIdKey,contentId);
        bundle.putParcelable(DetailArticcleFragment.contentObjectKey, contentModernPage);
        addFragment(SlidingMenuItem.MENU_ITEM_DETAIL_NEWS, bundle, false);
    }

    @Override
    public void onItemBookmarkedClick(String idNew) {
        //go to Home before add detail news fragment
        fm.popBackStack(SlidingMenuItem.MODERN_STYLE_PAGE, 0);
        Bundle bundle = new Bundle();
        bundle.putString(ListArticleModern.contentIdKey,idNew);
        addFragment(SlidingMenuItem.MENU_ITEM_DETAIL_NEWS, bundle, false);
    }
}
