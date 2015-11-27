/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package library.youtube;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.bongda365.Constant;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.tamhuynh.bongda365.R;

import java.util.ArrayList;
import java.util.Random;

import adapter.VideoFootBallAdapter;
import model.VideoFootBall;
import util.ShareManagement;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A sample showing how to use the ActionBar as an overlay when the video is playing in fullscreen.
 *
 * The ActionBar is the only view allowed to overlay the player, so it is a useful place to put
 * custom application controls when the video is in fullscreen. The ActionBar can not change back
 * and forth between normal mode and overlay mode, so to make sure our application's content
 * is not covered by the ActionBar we want to pad our root view when we are not in fullscreen.
 */
@TargetApi(11)
public class YoutubePlayerActivity extends YouTubeFailureRecoveryActivity implements
YouTubePlayer.OnFullscreenListener, AdapterView.OnItemClickListener {

	private ActionBarPaddedFrameLayout viewContainer;
	private YouTubePlayerFragment playerFragment;
	private ActionBar actionBar;
    private YouTubePlayer player;
    private VideoFootBall videoFootBall;
    private ListView listView;
    private VideoFootBallAdapter adapter;
    private ArrayList<VideoFootBall> array = new ArrayList<VideoFootBall>();
    private Context mContext;
//    private HeaderMenuYoutube headerMenuHome;
    public static String objectVideoFootBallKey = "objectVideoFootBallKey";
    public static String arrayVideoFootBallKey = "arrayVideoFootBallKey";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init();
        initUI();
        initData();
        initListener();
        
		// Action bar background is transparent by default.

	}

    private void init() {
        mContext = this;
        setTheme(getResources().getIdentifier(new ShareManagement(this).getThemeApp(), "style", getPackageName()));
    }

    private void initListener() {
        listView.setOnItemClickListener(this);

    }

    private void initData() {
        try {
            videoFootBall = (VideoFootBall) getIntent().getSerializableExtra(objectVideoFootBallKey);
            array = (ArrayList<VideoFootBall>) getIntent().getSerializableExtra(arrayVideoFootBallKey);
        }catch (Exception e){
            e.printStackTrace();
        }

        new AsyncTask<String,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                showProgressBar();
            }

            @Override
            protected String doInBackground(String... params) {

                return null;
            }

            @Override
            protected void onPostExecute(String str) {
                super.onPostExecute(str);
//                hideProgressBar();
                adapter = new VideoFootBallAdapter(mContext,array, VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_FOOTBALL);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setSelection(new Random().nextInt(array.size()));
            }
        }.execute();
    }
    

    private void initUI() {
        setContentView(R.layout.activity_youtube_player);

        viewContainer = (ActionBarPaddedFrameLayout) findViewById(R.id.view_container);
        playerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.player_fragment);
        playerFragment.initialize(Constant.YOUTUBE_DEVELOPER_KEY, this);
        viewContainer.setActionBar(getActionBar());

        listView = (ListView) findViewById(R.id.lv_related_video);
//        headerMenuHome = (HeaderMenuYoutube) getFragmentManager().findFragmentById(R.id.frag_header_menu);
//        headerMenuHome.setTitleHeaderMenu(getResources().getString(R.string.app_name));
    }

    @Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		this.player = player;
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		
		if (!wasRestored) {
			player.cueVideo(videoFootBall.getUrl().split("v=")[1]);
		}
	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.player_fragment);
	}

	@Override
	public void onFullscreen(boolean fullscreen) {
		viewContainer.setEnablePadding(!fullscreen);

		ViewGroup.LayoutParams playerParams = playerFragment.getView().getLayoutParams();
		if (fullscreen) {
			playerParams.width = MATCH_PARENT;
			playerParams.height = MATCH_PARENT;
		} else {
			playerParams.width = WRAP_CONTENT;
			playerParams.height = WRAP_CONTENT;
		}
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setVideoYoutube(array.get(position).getUrl().split("v=")[1]);

    }

    private void setVideoYoutube(String id){
        player.cueVideo(id);
    }


    /**
	 * This is a FrameLayout which adds top-padding equal to the height of the ActionBar unless
	 * disabled by {@link #setEnablePadding(boolean)}.
	 */
	public static final class ActionBarPaddedFrameLayout extends FrameLayout {

		private ActionBar actionBar;
		private boolean paddingEnabled;

		public ActionBarPaddedFrameLayout(Context context) {
			this(context, null);
		}

		public ActionBarPaddedFrameLayout(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public ActionBarPaddedFrameLayout(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			paddingEnabled = true;
		}

		public void setActionBar(ActionBar actionBar) {
			this.actionBar = actionBar;
			requestLayout();
		}

		public void setEnablePadding(boolean enable) {
			paddingEnabled = enable;
			requestLayout();
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int topPadding =
					paddingEnabled && actionBar != null && actionBar.isShowing() ? actionBar.getHeight() : 0;
					setPadding(0, topPadding, 0, 0);

					super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

	}

    public void onclickBackHeaderMenu(View view){
        finish();
    }
    public void onclickToggle(View view){

    }
}
