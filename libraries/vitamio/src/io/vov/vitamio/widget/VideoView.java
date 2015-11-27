/*
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (C) 2013 YIXIA.COM
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

package io.vov.vitamio.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnTimedTextListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.MediaPlayer.TrackInfo;
import io.vov.vitamio.Metadata;
import io.vov.vitamio.R;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.utils.StringUtils;




/**
 * Displays a video file. The VideoView class can load images from various
 * sources (such as resources or content providers), takes care of computing its
 * measurement from the video so that it can be used in any layout manager, and
 * provides various display options such as scaling and tinting.
 * <p/>
 * VideoView also provide many wrapper methods for
 * {@link io.vov.vitamio.MediaPlayer}, such as {@link #getVideoWidth()},
 * {@link #setTimedTextShown(boolean)}
 */
public class VideoView extends SurfaceView implements
		MediaController.MediaPlayerControl {
	public static final int VIDEO_LAYOUT_ORIGIN = 0;
	public static final int VIDEO_LAYOUT_SCALE = 1;
	public static final int VIDEO_LAYOUT_STRETCH = 2;
	public static final int VIDEO_LAYOUT_ZOOM = 3;
	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;
	private static final int STATE_SUSPEND = 6;
	private static final int STATE_RESUME = 7;
	private static final int STATE_SUSPEND_UNSUPPORTED = 8;
	OnVideoSizeChangedListener mSizeChangedListener = new OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			Log.d("onVideoSizeChanged: (%dx%d)", width, height);
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			mVideoAspectRatio = mp.getVideoAspectRatio();
			if (mVideoWidth != 0 && mVideoHeight != 0)
				setVideoLayout(mVideoLayout, mAspectRatio);
		}
	};
	OnPreparedListener mPreparedListener = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			Log.d("onPrepared");
			mCurrentState = STATE_PREPARED;
			mTargetState = STATE_PLAYING;

			// Get the capabilities of the player for this stream
			Metadata data = mp.getMetadata();

			if (data != null) {
				mCanPause = !data.has(Metadata.PAUSE_AVAILABLE)
						|| data.getBoolean(Metadata.PAUSE_AVAILABLE);
				mCanSeekBack = !data.has(Metadata.SEEK_BACKWARD_AVAILABLE)
						|| data.getBoolean(Metadata.SEEK_BACKWARD_AVAILABLE);
				mCanSeekForward = !data.has(Metadata.SEEK_FORWARD_AVAILABLE)
						|| data.getBoolean(Metadata.SEEK_FORWARD_AVAILABLE);
			} else {
				mCanPause = mCanSeekBack = mCanSeekForward = true;
			}

			if (mOnPreparedListener != null)
				mOnPreparedListener.onPrepared(mMediaPlayer);
			if (mMediaController != null)
				mMediaController.setEnabled(true);
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			mVideoAspectRatio = mp.getVideoAspectRatio();

			long seekToPosition = mSeekWhenPrepared;
			System.out.println("loadingdialog.dismiss at onPrepared");

			if (seekToPosition != 0)
				seekTo(seekToPosition);
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				setVideoLayout(mVideoLayout, mAspectRatio);
				if (mSurfaceWidth == mVideoWidth
						&& mSurfaceHeight == mVideoHeight) {
					if (mTargetState == STATE_PLAYING) {
						start();
						if (mMediaController != null)
							mMediaController.show();
					} else if (!isPlaying()
							&& (seekToPosition != 0 || getCurrentPosition() > 0)) {
						if (mMediaController != null)
							mMediaController.show(0);
					}
				}
			} else if (mTargetState == STATE_PLAYING) {
				start();
			}
		}
	};
	SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			mSurfaceWidth = w;
			mSurfaceHeight = h;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
			if (mMediaPlayer != null && isValidState && hasValidSize) {
				if (mSeekWhenPrepared != 0)
					seekTo(mSeekWhenPrepared);
				start();
				if (mMediaController != null) {
					if (mMediaController.isShowing())
						mMediaController.hide();
					mMediaController.show();
				}
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceHolder = holder;
			if (mMediaPlayer != null && mCurrentState == STATE_SUSPEND
					&& mTargetState == STATE_RESUME) {
				mMediaPlayer.setDisplay(mSurfaceHolder);
				resume();
			} else {
				openVideo();
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			mSurfaceHolder = null;
			if (mMediaController != null)
				mMediaController.hide();
			if (mCurrentState != STATE_SUSPEND)
				release(true);
		}
	};
	private Uri mUri;
	private long mDuration;
	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;
	private float mAspectRatio = 0;
	private int mVideoLayout = VIDEO_LAYOUT_SCALE;
	private SurfaceHolder mSurfaceHolder = null;
	private MediaPlayer mMediaPlayer = null;
	private Dialog loadingDialog;
	private int mVideoWidth;
	private int mVideoHeight;
	private float mVideoAspectRatio;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	private MediaController mMediaController;
	private OnCompletionListener mOnCompletionListener;
	private OnPreparedListener mOnPreparedListener;
	private OnErrorListener mOnErrorListener;
	private OnSeekCompleteListener mOnSeekCompleteListener;
	private OnTimedTextListener mOnTimedTextListener;
	private OnInfoListener mOnInfoListener;
	private OnBufferingUpdateListener mOnBufferingUpdateListener;
	private int mCurrentBufferPercentage;
	private long mSeekWhenPrepared; // recording the seek position while
									// preparing
	TextView tvBrightness;
	TextView tvVolume;
	TextView tvTime;
	LinearLayout layoutBrightness;
	LinearLayout layoutVolume;
	LinearLayout layoutTime;
	private boolean mCanPause;
	private boolean mCanSeekBack;
	private boolean mCanSeekForward;
	private Context mContext;
	private OnCompletionListener mCompletionListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			Log.d("onCompletion");
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;
			if (mMediaController != null)
				mMediaController.hide();
			if (mOnCompletionListener != null)
				mOnCompletionListener.onCompletion(mMediaPlayer);
		}
	};
	private OnErrorListener mErrorListener = new OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			Log.d("Error: %d, %d", framework_err, impl_err);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			if (mMediaController != null)
				mMediaController.hide();
			if (loadingDialog != null)
				loadingDialog.dismiss();

			if (mOnErrorListener != null) {
				if (mOnErrorListener.onError(mMediaPlayer, framework_err,
						impl_err))
					return true;
			}

			if (getWindowToken() != null) {
				int message = framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ? R.string.VideoView_error_text_invalid_progressive_playback
						: R.string.VideoView_error_text_unknown;

				new AlertDialog.Builder(mContext)
						.setTitle(R.string.VideoView_error_title)
						.setMessage(message)
						.setPositiveButton(R.string.VideoView_error_button,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										if (mOnCompletionListener != null)
											mOnCompletionListener
													.onCompletion(mMediaPlayer);
									}
								}).setCancelable(false).show();
			}
			return true;
		}
	};
	private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
			// hide media controller after 5s from film is played
			if (percent >= 98) {
				mMediaController.show(5000);
				mMediaController.hideBufferingInfo(1000);
			}
			if (mOnBufferingUpdateListener != null)
				mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
		}
	};
	private OnInfoListener mInfoListener = new OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			Log.d("onInfo: (%d, %d)", what, extra);
			if (mOnInfoListener != null) {
				mOnInfoListener.onInfo(mp, what, extra);
			} else if (mMediaPlayer != null) {
				if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
					mMediaPlayer.pause();
				else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
					mMediaPlayer.start();
			}

			return true;
		}
	};
	private OnSeekCompleteListener mSeekCompleteListener = new OnSeekCompleteListener() {
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			Log.d("onSeekComplete");
			if (mOnSeekCompleteListener != null)
				mOnSeekCompleteListener.onSeekComplete(mp);
		}
	};
	private OnTimedTextListener mTimedTextListener = new OnTimedTextListener() {
		@Override
		public void onTimedTextUpdate(byte[] pixels, int width, int height) {
			Log.i("onSubtitleUpdate: bitmap subtitle, %dx%d", width, height);
			if (mOnTimedTextListener != null)
				mOnTimedTextListener.onTimedTextUpdate(pixels, width, height);
		}

		@Override
		public void onTimedText(String text) {
			Log.i("onSubtitleUpdate: %s", text);
			if (mOnTimedTextListener != null)
				mOnTimedTextListener.onTimedText(text);
		}
	};

	public VideoView(Context context) {
		super(context);
		initVideoView(context);
	}

	public VideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initVideoView(context);
	}

	public VideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initVideoView(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	/**
	 * Set the display options
	 * 
	 * @param layout
	 *            <ul>
	 *            <li>{@link #VIDEO_LAYOUT_ORIGIN}
	 *            <li>{@link #VIDEO_LAYOUT_SCALE}
	 *            <li>{@link #VIDEO_LAYOUT_STRETCH}
	 *            <li>{@link #VIDEO_LAYOUT_ZOOM}
	 *            </ul>
	 * @param aspectRatio
	 *            video aspect ratio, will audo detect if 0.
	 */
	public void setVideoLayout(int layout, float aspectRatio) {
		LayoutParams lp = getLayoutParams();
		DisplayMetrics disp = mContext.getResources().getDisplayMetrics();
		int windowWidth = disp.widthPixels, windowHeight = disp.heightPixels;
		float windowRatio = windowWidth / (float) windowHeight;
		float videoRatio = aspectRatio <= 0.01f ? mVideoAspectRatio
				: aspectRatio;
		mSurfaceHeight = mVideoHeight;
		mSurfaceWidth = mVideoWidth;
		if (VIDEO_LAYOUT_ORIGIN == layout && mSurfaceWidth < windowWidth
				&& mSurfaceHeight < windowHeight) {
			lp.width = (int) (mSurfaceHeight * videoRatio);
			lp.height = mSurfaceHeight;
		} else if (layout == VIDEO_LAYOUT_ZOOM) {
			lp.width = windowRatio > videoRatio ? windowWidth
					: (int) (videoRatio * windowHeight);
			lp.height = windowRatio < videoRatio ? windowHeight
					: (int) (windowWidth / videoRatio);
		} else {
			boolean full = layout == VIDEO_LAYOUT_STRETCH;
			lp.width = (full || windowRatio < videoRatio) ? windowWidth
					: (int) (videoRatio * windowHeight);
			lp.height = (full || windowRatio > videoRatio) ? windowHeight
					: (int) (windowWidth / videoRatio);
		}
		setLayoutParams(lp);
		getHolder().setFixedSize(mSurfaceWidth, mSurfaceHeight);
		Log.d("VIDEO: %dx%dx%f, Surface: %dx%d, LP: %dx%d, Window: %dx%dx%f",
				mVideoWidth, mVideoHeight, mVideoAspectRatio, mSurfaceWidth,
				mSurfaceHeight, lp.width, lp.height, windowWidth, windowHeight,
				windowRatio);
		mVideoLayout = layout;
		mAspectRatio = aspectRatio;
	}

	private void initVideoView(Context ctx) {
		mContext = ctx;
		setScreenResolution(ctx);
		audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);

		mVideoWidth = 0;
		mVideoHeight = 0;
		getHolder().setFormat(PixelFormat.RGBA_8888); // PixelFormat.RGB_565
		getHolder().addCallback(mSHCallback);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
		if (ctx instanceof Activity)
			((Activity) ctx).setVolumeControlStream(AudioManager.STREAM_MUSIC);
		loadingDialog = new Dialog(mContext);
		loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loadingDialog.setContentView(R.layout.waitingdialog);
		loadingDialog.setCancelable(false);
	}

	public boolean isValid() {
		return (mSurfaceHolder != null && mSurfaceHolder.getSurface().isValid());
	}

	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
	}

	public void setVideoURI(Uri uri) {
		mUri = uri;
		mSeekWhenPrepared = 0;
		openVideo();
		requestLayout();
		invalidate();
	}

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
		}
	}

	private void openVideo() {
		if (mUri == null || mSurfaceHolder == null
				|| !Vitamio.isInitialized(mContext))
			return;

		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		mContext.sendBroadcast(i);

		release(false);
		loadingDialog.show();
		try {
			mDuration = -1;
			mCurrentBufferPercentage = 0;
			mMediaPlayer = new MediaPlayer(mContext);
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			mMediaPlayer.setOnInfoListener(mInfoListener);
			mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
			mMediaPlayer.setOnTimedTextListener(mTimedTextListener);
			mMediaPlayer.setDataSource(mContext, mUri);
			mMediaPlayer.setDisplay(mSurfaceHolder);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.prepareAsync();
			mCurrentState = STATE_PREPARING;
			attachMediaController();
		} catch (IOException ex) {
			Log.e("Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} catch (IllegalArgumentException ex) {
			Log.e("Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		}
	}

	public void setMediaController(MediaController controller, RelativeLayout layoutTopBar,RelativeLayout NextPre) {
		mMediaController = controller;
		mMediaController.setTopBar(layoutTopBar);
		mMediaController.setNextPre(NextPre);
		if (mMediaController != null) {
			mMediaController.hide();
			layoutTopBar.setVisibility(View.GONE);
			NextPre.setVisibility(View.GONE);
		}
		attachMediaController();
	}

	private void attachMediaController() {
		if (mMediaPlayer != null && mMediaController != null) {
			mMediaController.setMediaPlayer(this);
			View anchorView = this.getParent() instanceof View ? (View) this
					.getParent() : this;
			mMediaController.setAnchorView(anchorView);
			mMediaController.setEnabled(isInPlaybackState());

			if (mUri != null) {
				// List<String> paths = mUri.getPathSegments();
				// String name = paths == null || paths.isEmpty() ? "null" :
				// paths
				// .get(paths.size() - 1);
				// mMediaController.setFileName(name);
				mMediaController.setFileName("");
			}
		}
	}

	public void setOnPreparedListener(OnPreparedListener l) {
		mOnPreparedListener = l;
	}

	public void setOnCompletionListener(OnCompletionListener l) {
		mOnCompletionListener = l;
	}

	public void setOnErrorListener(OnErrorListener l) {
		mOnErrorListener = l;
	}

	public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
		mOnBufferingUpdateListener = l;
	}

	public void setOnSeekCompleteListener(OnSeekCompleteListener l) {
		mOnSeekCompleteListener = l;
	}

	public void setOnTimedTextListener(OnTimedTextListener l) {
		mOnTimedTextListener = l;
	}

	public void setOnInfoListener(OnInfoListener l) {
		mOnInfoListener = l;
	}

	private void release(boolean cleartargetstate) {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			if (cleartargetstate)
				mTargetState = STATE_IDLE;
		}
	}

	public void setLayoutInfo(LinearLayout layoutBrightness,
			LinearLayout layoutVolume, LinearLayout layoutTime, TextView... tv ) {
		this.layoutBrightness = layoutBrightness;
		this.layoutVolume = layoutVolume;
		this.layoutTime = layoutTime;
		this.tvBrightness = tv[0];
        this.tvVolume = tv[1];
		this.tvTime = tv[2];
	}

	public void setLayoutInfoVisibility(String TOUCH_STATE) {
		if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START
				&& !layoutBrightness.isShown())
			layoutBrightness.setVisibility(View.VISIBLE);
		else if (TOUCH_STATE == ENHANCE_VOLUME_START && !layoutVolume.isShown())
			layoutVolume.setVisibility(View.VISIBLE);
		else if (TOUCH_STATE == ENHANCE_MEDIA_POSITION && !layoutTime.isShown())
			layoutTime.setVisibility(View.VISIBLE);
		else if (TOUCH_STATE == ENHANCE_NONE) {
			layoutBrightness.setVisibility(View.INVISIBLE);
			layoutVolume.setVisibility(View.INVISIBLE);
			layoutTime.setVisibility(View.INVISIBLE);
		}
	}

	public void setLayoutTextInfo(String TOUCH_STATE, String mText) {
		if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START)
			tvBrightness.setText(mText + "%");
		else if (TOUCH_STATE == ENHANCE_VOLUME_START)
			tvVolume.setText(mText + "%");
		else if (TOUCH_STATE == ENHANCE_MEDIA_POSITION)
			tvTime.setText(mText);
	}

	public void setScreenResolution(Context ctx) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		screenWidth = displaymetrics.widthPixels;
		screenHeight = displaymetrics.heightPixels;
		numberPixelPerPercent = screenHeight / 100;
		numberPixelPerSecond = screenWidth / 360;
		System.out.println("ScreenWidth=" + screenWidth + "\n ScreenHeight="
				+ screenHeight + "\n numberPixelPerPercent="
				+ numberPixelPerPercent);
	}

	private void setBrightness(float brightness) {
		// float brightness = (curBrightnessValue + value) / (float) 255;
		WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
				.getAttributes();
		lp.screenBrightness = brightness;
		((Activity) mContext).getWindow().setAttributes(lp);
	}

	private void setBrightness(int brightness) {
		android.provider.Settings.System.putInt(mContext.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		android.provider.Settings.System.putInt(mContext.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
	}

	private Runnable mRunnableSeeker = new Runnable() {

		@Override
		public void run() {
			System.out.println("change time positoin");
			mMediaPlayer.seekTo(mCurrentChangedTime);
			start();
			mediaPositionChanged = true;
		}
	};

	// For Volume and brightness. 100% = screen height
	private int numberPixelPerPercent = 20;
	// For media time position. 6min = screen width
	private int numberPixelPerSecond = 20;
	private int iX, iY;
	private int screenWidth;
	private int screenHeight;
	private String TOUCH_STATE = "";
	final private String ENHANCE_NONE = "ENHANCE_NONE";
	final private String ENHANCE_VOLUME_IDLE = "ENHANCE_VOLUME_IDLE";
	final private String ENHANCE_VOLUME_START = "ENHANCE_VOLUME_START";
	final private String ENHANCE_BRIGHTNESS_IDLE = "ENHANCE_BRIGHTNESS_IDLE";
	final private String ENHANCE_BRIGHTNESS_START = "ENHANCE_BRIGHTNESS_START";
	final private String ENHANCE_MEDIA_POSITION = "ENHANCE_MEDIA_POSITION";
	final private int MAX_CLICK_DURATION = 400;
	final private int MAX_CLICK_DISTANCE = 10;
	private AudioManager audioManager;
	private int maxVolume;
	private int curVolume;
	private int curVolumeInPercent;
	private int curBrightnessInt = 0;
	private int updatedBrightnessInt = 0;
	private long startClickTime;
	private long mCurrentTime;
	private long mCurrentChangedTime;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("ACTION_DOWN");
			System.out.println("x=" + ev.getX() + ", y=" + ev.getY());
			startClickTime = Calendar.getInstance().getTimeInMillis();
			TOUCH_STATE = ENHANCE_NONE;
			iX = (int) ev.getX();
			iY = (int) ev.getY();

			if (iY < (screenHeight * 4 / 5) && iY > (screenHeight * 1 / 5)) {
				if (iX < (screenWidth / 10)) {
					TOUCH_STATE = ENHANCE_BRIGHTNESS_IDLE;
				} else if (iX > (screenWidth * 9 / 10)) {
					TOUCH_STATE = ENHANCE_VOLUME_IDLE;
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START
					|| TOUCH_STATE == ENHANCE_VOLUME_START
					|| TOUCH_STATE == ENHANCE_MEDIA_POSITION) {
				touchEventHandler(TOUCH_STATE, iX, iY, ev);
			} else {
				if (TOUCH_STATE == ENHANCE_BRIGHTNESS_IDLE
						&& Math.abs(iY - ev.getY()) > 50
						&& Math.abs(iX - ev.getX()) < 50) {
					TOUCH_STATE = ENHANCE_BRIGHTNESS_START;
					try {
						curBrightnessInt = android.provider.Settings.System
								.getInt(mContext.getContentResolver(),
										android.provider.Settings.System.SCREEN_BRIGHTNESS);
					} catch (SettingNotFoundException e) {
						e.printStackTrace();
					}

				} else if (TOUCH_STATE == ENHANCE_VOLUME_IDLE
						&& Math.abs(iY - ev.getY()) > 50
						&& Math.abs(iX - ev.getX()) < 50) {
					TOUCH_STATE = ENHANCE_VOLUME_START;
					maxVolume = audioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					curVolume = audioManager
							.getStreamVolume(AudioManager.STREAM_MUSIC);
					curVolumeInPercent = curVolume * 100 / maxVolume;

				} else if (Math.abs(iX - ev.getX()) > 50) {
					TOUCH_STATE = ENHANCE_MEDIA_POSITION;
					if (mediaPositionChanged)
						mCurrentTime = getCurrentPosition();
					else
						mCurrentTime = mCurrentChangedTime;
					pause();
				}
				setLayoutInfoVisibility(TOUCH_STATE);
			}
			break;

		case MotionEvent.ACTION_UP:
			// check click event occurred
			long clickDuration = Calendar.getInstance().getTimeInMillis()
					- startClickTime;
			int dx = Math.abs((int) (ev.getX() - iX));
			int dy = Math.abs((int) (ev.getY() - iY));
			if (clickDuration < MAX_CLICK_DURATION && dx < MAX_CLICK_DISTANCE
					&& dy < MAX_CLICK_DISTANCE) {
				if (isInPlaybackState() && mMediaController != null)
					toggleMediaControlsVisiblity();
			}

			// update system brightness
			if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START) {
				setBrightness(updatedBrightnessInt);
			}
			if (TOUCH_STATE == ENHANCE_MEDIA_POSITION) {
				System.out.println("ACTION_UP, show loading video progressbar");
				mMediaController.show(360000);
				mMediaController.showBufferingInfo();
			}

			TOUCH_STATE = ENHANCE_NONE;
			setLayoutInfoVisibility(TOUCH_STATE);
			break;

		default:
			break;
		}
		return true;

	}

	private boolean mediaPositionChanged = true;

	private void touchEventHandler(String TOUCH_STATE, int iX, int iY,
			MotionEvent ev) {
		// System.out.println("TouchEventHandler");
		if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START) {
			if (Math.abs(iX - ev.getX()) < 100) {
				int changedPercent = (int) ((iY - ev.getY()) / numberPixelPerPercent);
				int brightnessInt = curBrightnessInt + changedPercent;
				int brightnessInPercent = brightnessInt * 100 / 255;
				if (brightnessInt > 255) {
					brightnessInt = 255;
					brightnessInPercent = 100;
				} else if (brightnessInt < 12) {
					brightnessInt = 11;
					brightnessInPercent = 5;
				}
				updatedBrightnessInt = brightnessInt;

				// update window brightness
				float brightnessFloat = (brightnessInt) / (float) 255;
				setBrightness(brightnessFloat);
				setLayoutTextInfo(TOUCH_STATE,
						String.valueOf(brightnessInPercent));
				System.out.println("percentOfChanged=" + changedPercent
						+ "\n curBrightness=" + brightnessInt);
			}
		} else if (TOUCH_STATE == ENHANCE_VOLUME_START) {
			if (Math.abs(iX - ev.getX()) < 100) {
				int changedPercent = (int) ((iY - ev.getY()) / numberPixelPerPercent);
				int volumeInPercent = curVolumeInPercent + changedPercent;
				int volume = volumeInPercent * maxVolume / 100;
				if (volume > maxVolume) {
					volume = maxVolume;
					volumeInPercent = 100;
				} else if (volume < 1) {
					volume = 0;
					volumeInPercent = 0;
				}

				setLayoutTextInfo(TOUCH_STATE, String.valueOf(volumeInPercent));
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
						0);
				System.out.println("volume=" + volume + "\n percentOfChanged="
						+ changedPercent + "\n curVolumeInPercent="
						+ curVolumeInPercent);
			}
		} else if (TOUCH_STATE == ENHANCE_MEDIA_POSITION) {
			// in millisecond
			long changedSecond = (long) ((ev.getX() - iX) / numberPixelPerSecond) * 100;
			long mTime = mCurrentTime + changedSecond;
			if (mTime >= getDuration())
				mTime = getDuration();
			else if (mTime < 1)
				mTime = 0;
			mCurrentChangedTime = mTime;

			setLayoutTextInfo(TOUCH_STATE, StringUtils.generateTime(mTime));
			mediaPositionChanged = false;
			removeCallbacks(mRunnableSeeker);
			postDelayed(mRunnableSeeker, 700);
			System.out.println("changed second=" + changedSecond
					+ "\nchanged second="
					+ StringUtils.generateTime(changedSecond)
					+ "\ncurrent position=" + mTime + "\n curren position="
					+ StringUtils.generateTime(mTime));
		}
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		if (isInPlaybackState() && mMediaController != null)
			toggleMediaControlsVisiblity();
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK
				&& keyCode != KeyEvent.KEYCODE_VOLUME_UP
				&& keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
				&& keyCode != KeyEvent.KEYCODE_MENU
				&& keyCode != KeyEvent.KEYCODE_CALL
				&& keyCode != KeyEvent.KEYCODE_ENDCALL;
		if (isInPlaybackState() && isKeyCodeSupported
				&& mMediaController != null) {
			if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
					|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
					|| keyCode == KeyEvent.KEYCODE_SPACE) {
				if (mMediaPlayer.isPlaying()) {
					pause();
					mMediaController.show();
				} else {
					start();
					mMediaController.hide();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
				if (!mMediaPlayer.isPlaying()) {
					start();
					mMediaController.hide();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
					|| keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
				if (mMediaPlayer.isPlaying()) {
					pause();
					mMediaController.show();
				}
				return true;
			} else {
				toggleMediaControlsVisiblity();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private void toggleMediaControlsVisiblity() {
		if (mMediaController.isShowing()) {
			mMediaController.hide();
		} else {
			mMediaController.show();
		}
	}

	public void start() {
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
		}
		mTargetState = STATE_PLAYING;
		if (loadingDialog != null) {
			loadingDialog.dismiss();
		}
	}

	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
			}
		}
		mTargetState = STATE_PAUSED;
	}

	public void suspend() {
		if (isInPlaybackState()) {
			release(false);
			mCurrentState = STATE_SUSPEND_UNSUPPORTED;
			Log.d("Unable to suspend video. Release MediaPlayer.");
		}
	}

	public void resume() {
		if (mSurfaceHolder == null && mCurrentState == STATE_SUSPEND) {
			mTargetState = STATE_RESUME;
		} else if (mCurrentState == STATE_SUSPEND_UNSUPPORTED) {
			openVideo();
		}
	}

	public long getDuration() {
		if (isInPlaybackState()) {
			if (mDuration > 0)
				return mDuration;
			mDuration = mMediaPlayer.getDuration();
			return mDuration;
		}
		mDuration = -1;
		return mDuration;
	}

	public long getCurrentPosition() {
		if (isInPlaybackState())
			return mMediaPlayer.getCurrentPosition();
		return 0;
	}

	public void seekTo(long msec) {
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
			mSeekWhenPrepared = 0;
		} else {
			mSeekWhenPrepared = msec;
		}
	}

	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	public int getBufferPercentage() {
		if (mMediaPlayer != null)
			return mCurrentBufferPercentage;
		return 0;
	}

	public void setVolume(float leftVolume, float rightVolume) {
		if (mMediaPlayer != null)
			mMediaPlayer.setVolume(leftVolume, rightVolume);
	}

	public int getVideoWidth() {
		return mVideoWidth;
	}

	public int getVideoHeight() {
		return mVideoHeight;
	}

	public float getVideoAspectRatio() {
		return mVideoAspectRatio;
	}

	public void setVideoQuality(int quality) {
		if (mMediaPlayer != null)
			mMediaPlayer.setVideoQuality(quality);
	}

	public void setVideoChroma(int chroma) {
		if (mMediaPlayer != null)
			mMediaPlayer.setVideoChroma(chroma);
	}

	public void setBufferSize(int bufSize) {
		if (mMediaPlayer != null)
			mMediaPlayer.setBufferSize(bufSize);
	}

	public boolean isBuffering() {
		if (mMediaPlayer != null)
			return mMediaPlayer.isBuffering();
		return false;
	}

	public String getMetaEncoding() {
		if (mMediaPlayer != null)
			return mMediaPlayer.getMetaEncoding();
		return null;
	}

	public void setMetaEncoding(String encoding) {
		if (mMediaPlayer != null)
			mMediaPlayer.setMetaEncoding(encoding);
	}

	public SparseArray<String> getAudioTrackMap(String encoding) {
		if (mMediaPlayer != null)
			return mMediaPlayer.findTrackFromTrackInfo(
					TrackInfo.MEDIA_TRACK_TYPE_AUDIO,
					mMediaPlayer.getTrackInfo(encoding));
		return null;
	}

	public int getAudioTrack() {
		if (mMediaPlayer != null)
			return mMediaPlayer.getAudioTrack();
		return -1;
	}

	public void setAudioTrack(int audioIndex) {
		if (mMediaPlayer != null)
			mMediaPlayer.selectTrack(audioIndex);
	}

	public void setTimedTextShown(boolean shown) {
		if (mMediaPlayer != null)
			mMediaPlayer.setTimedTextShown(shown);
	}

	public void setTimedTextEncoding(String encoding) {
		if (mMediaPlayer != null)
			mMediaPlayer.setTimedTextEncoding(encoding);
	}

	public int getTimedTextLocation() {
		if (mMediaPlayer != null)
			return mMediaPlayer.getTimedTextLocation();
		return -1;
	}

	public void addTimedTextSource(String subPath) {
		if (mMediaPlayer != null)
			mMediaPlayer.addTimedTextSource(subPath);
	}

	public String getTimedTextPath() {
		if (mMediaPlayer != null)
			return mMediaPlayer.getTimedTextPath();
		return null;
	}

	public void setSubTrack(int trackId) {
		if (mMediaPlayer != null)
			mMediaPlayer.selectTrack(trackId);
	}

	public int getTimedTextTrack() {
		if (mMediaPlayer != null)
			return mMediaPlayer.getTimedTextTrack();
		return -1;
	}

	public SparseArray<String> getSubTrackMap(String encoding) {
		if (mMediaPlayer != null)
			return mMediaPlayer.findTrackFromTrackInfo(
					TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT,
					mMediaPlayer.getTrackInfo(encoding));
		return null;
	}

	protected boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}

	public boolean canPause() {
		return mCanPause;
	}

	public boolean canSeekBackward() {
		return mCanSeekBack;
	}

	public boolean canSeekForward() {
		return mCanSeekForward;
	}
}
