package com.tamhuynh.bongda365.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import library.anim.AnimUtil;
import util.ShareManagement;


/**
 * Created by Tam Huynh on 12/24/2014.
 */
public class VideoViewActivity extends Activity implements MediaPlayer.OnCompletionListener {
    private VideoView mVideoView;
    private LinearLayout layoutBrightness,layoutVolume,layoutTime;
    private RelativeLayout layoutTopBar, layout_next_pre;
    private TextView tvTitle;
    public static String pathKey = "pathUrl";
    private String path ;
    public static String nameVideoKey = "nameVideoKey";
    private String nameVideo;
    private ImageView imgGuide;
    private Handler handler;
    private int Timer_delay = 2000;
    private int numberVideoUrl = 0;
    private ArrayList<String> arrayUrl = new ArrayList<String>();
    public static String arrayUrlKey = "arrayUrlKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return ;
        initBundle();
        initUI();
        initData();
        initListener();
    }

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString(pathKey) != null)
                path = bundle.getString(pathKey);
            else
                path = "http://24h-video.24hstatic.com/upload/4-2014/videoclip/2014-12-23/1419296704-Chelsea_001.mp4?start=0";
            if(bundle.getString(nameVideoKey)!= null)
                nameVideo = bundle.getString(nameVideoKey);
            if(bundle.getStringArrayList(arrayUrlKey) != null)
                arrayUrl = bundle.getStringArrayList(arrayUrlKey);
        }else
            path = "http://24h-video.24hstatic.com/upload/4-2014/videoclip/2014-12-23/1419296704-Chelsea_001.mp4?start=0";
    }

    private void initData() {
        startPlayingVideo();
        tvTitle.setText(nameVideo);
    }
    private void startPlayingVideo() {
        mVideoView.start();
        mVideoView.setOnCompletionListener(this) ;

    }
    private void initListener() {
        if(new ShareManagement(this).getOnOffGuideVideo() && numberVideoUrl == 0){
            handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    AnimUtil.AlphaFadeOut(imgGuide);
                }
            };
            handler.postDelayed(runnable,Timer_delay);
        }else
            imgGuide.setVisibility(View.GONE);
    }
    TextView tvBrightness,tvVolume,tvTime;
    private void initUI() {
        setContentView(R.layout.video_view);
        tvTitle = (TextView)    findViewById(R.id.tv_title_video);
        imgGuide = (ImageView)  findViewById(R.id.imgGuide);
        layoutTopBar = (RelativeLayout) findViewById(R.id.layout_topbar_videoview);
        layout_next_pre = (RelativeLayout) findViewById(R.id.layout_next_pre);
        layoutBrightness = (LinearLayout) findViewById(R.id.layout_video_info_brightness);
        layoutVolume = (LinearLayout) findViewById(R.id.layout_video_info_volume);
        layoutTime = (LinearLayout) findViewById(R.id.layout_video_info_time);
        tvBrightness = (TextView) this.layoutBrightness
                .findViewById(R.id.tv_brightness_percent);
        tvVolume = (TextView) this.layoutVolume
                .findViewById(R.id.tv_volume_percent);
        tvTime = (TextView) this.layoutTime
                .findViewById(R.id.tv_video_time);
        initVideo();

    }
    private void initVideo(){
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVideoView.setLayoutInfo(layoutBrightness, layoutVolume, layoutTime, tvBrightness,tvVolume,tvTime);
        mVideoView.setVideoPath(arrayUrl.get(numberVideoUrl));
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.setMediaController(new MediaController(this),layoutTopBar,layout_next_pre );
        mVideoView.requestFocus();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onClickBack(View view){
        finish();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        numberVideoUrl ++;
        if(numberVideoUrl < arrayUrl.size()){
            initUI();
            initListener();
            startPlayingVideo();
        }else
            finish();

    }
}
