package library.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.Session;
import com.tamhuynh.bongda365.R;

import org.apache.http.protocol.HTTP;

import library.database.DatabaseManagement;
import library.facebookHelper.DialogShareFacebook;
import library.facebookHelper.LoginActivityFacebook;
import model.Bookmark;
import model.Content;
import model.DetailPage;

/**
 * Created by Tam Huynh on 1/25/2015.
 */
public class ToolbarView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private int posArrayFont = 0;
    private String [] arrayFontSize= {"18","20", "22"};
    private IOnToobarListener toobarListener;
    private ImageViewOptimize footerImgShowSetting,share,icon_fb;
    private ViewGroup footerSetting;
    private DetailPage detailPage;
    private DatabaseManagement databaseHelper;
    private String mShareImage, mShareCaption;
    public interface IOnToobarListener{
        public void onFontClick(int size);
        public void onClickWebview();
    }

    public ToolbarView(Context context) {
        super(context);
        this.mContext = context;
        databaseHelper = new DatabaseManagement(context);
        initUI();
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        databaseHelper = new DatabaseManagement(context);
        initUI();
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        databaseHelper = new DatabaseManagement(context);
        initUI();
    }
    public void setOnToolBarListener(IOnToobarListener onToolBarListener){
        this.toobarListener = onToolBarListener;
    }
    private ImageViewOptimize imgFontChange,webview,img_bookmark;
    private View view;
    public void initUI(){
        view = LayoutInflater.from(mContext).inflate(R.layout.toolbar_view,this);
        imgFontChange = (ImageViewOptimize) view.findViewById(R.id.footerFont);
        imgFontChange.setOnClickListener(this);
        webview = (ImageViewOptimize) view.findViewById(R.id.webview);
        webview.setOnClickListener(this);
        footerImgShowSetting = (ImageViewOptimize) view.findViewById(R.id.footerImgShowSetting);
        footerImgShowSetting.setOnClickListener(this);
        footerSetting = (ViewGroup) view.findViewById(R.id.footerSetting);
        share = (ImageViewOptimize) view.findViewById(R.id.share);
        share.setOnClickListener(this);
        img_bookmark = (ImageViewOptimize) view.findViewById(R.id.img_bookmark);
        img_bookmark.setOnClickListener(this);
        icon_fb = (ImageViewOptimize) view.findViewById(R.id.icon_fb);
        icon_fb.setOnClickListener(this);
    }
    public int flagLoginFB = 0;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == imgFontChange.getId()){
            handleChangeFontSizeArticle();
        }else if(id == webview.getId()){
            toobarListener.onClickWebview();
        }else if (id == footerImgShowSetting.getId())
            showSetting();
        else if(id == share.getId()){
            handleMultiShare();
        }else if(id == img_bookmark.getId()){
            handleBookmark();
        }else if(id == icon_fb.getId()){
            Session session = Session.getActiveSession();
            if(session==null){
                // try to restore from cache
                session = Session.openActiveSessionFromCache(mContext);
            }

            if(session!=null && session.isOpened()){
                handleShare();
            }else {
                flagLoginFB = 1;
                mContext.startActivity(new Intent(mContext,
                        LoginActivityFacebook.class));
            }
        }
    }

    public void setShareInfo(String image,String caption){
        this.mShareImage = image;
        this.mShareCaption = caption;
    }
    public void handleShare() {
        flagLoginFB = 0;
        DialogShareFacebook fragment = new DialogShareFacebook();
        Bundle bundle = new Bundle();
        bundle.putString(DialogShareFacebook.TAG_SHARED_TEXT, detailPage.getmTitle() + "<p>" + detailPage.getSubTitle()+"</p>" );
        bundle.putString(DialogShareFacebook.TAG_SHARED_IMAGE,mShareImage);
        bundle.putString(DialogShareFacebook.TAG_SHARED_CAPTION,mShareCaption);
        fragment.setArguments(bundle);

        fragment.show(
                ((FragmentActivity) mContext).getSupportFragmentManager(),
                "Sharing Facebook Dialog");
    }

    private void handleChangeFontSizeArticle() {
        if(posArrayFont == arrayFontSize.length - 1)
            posArrayFont = 0;
        else
            posArrayFont = posArrayFont + 1;
        toobarListener.onFontClick(Integer.parseInt(arrayFontSize[posArrayFont]));
    }

    private void handleMultiShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, detailPage.getmTitle()
                + " : \n " + detailPage.getUrlArticle());
        sendIntent.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain" MIME type

        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(sendIntent);
        }
    }

    private void handleBookmark() {
        if(databaseHelper.checkBookmarkExist(String.valueOf(detailPage.getmId()))){
            //already Bookmark
            img_bookmark.setImageResource(R.drawable.icon_bookmark);
            databaseHelper.deleteBookmark(String.valueOf(detailPage.getmId()));
        }else{
            showToast(getResources().getString(R.string.bookmarked_text));
            img_bookmark.setImageResource(R.drawable.icon_bookmarked);

            databaseHelper.addBookmark(initItemBookmark());
        }
    }

    private Bookmark initItemBookmark() {
        String thumbnailBookmark = "";
        if(detailPage.getContent() != null && detailPage.getContent().size() != 0)
        {
            for(Content content : detailPage.getContent()){
                if(content.getType().equals("img")) {
                    thumbnailBookmark = content.getSrc();
                    break;
                }
            }
        }
        Bookmark object_bookmark = new Bookmark(
                String.valueOf(detailPage.getmId()), "",thumbnailBookmark ,
                detailPage.getmTitle(), "", detailPage.getContent().get(0).getSrc(),
                detailPage.getmDate(true), "", "");
        return object_bookmark;
    }

    private void showToast(String str){
        Toast.makeText(mContext,str,Toast.LENGTH_SHORT).show();
    }

    public boolean footerIsShowing = true;
    public void showSetting() {
        View horizontalScrollView1 = view.findViewById(R.id.horizontalScrollView1);

        horizontalScrollView1.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        int width = horizontalScrollView1.getMeasuredWidth();

        if (!footerIsShowing) {
            footerIsShowing = true;
            horizontalScrollView1.setVisibility(View.VISIBLE);

//            footerImgShowSetting
//                    .setBackgroundResource(R.drawable.bg_press_white);
            footerImgShowSetting.setImageResource(R.drawable.icon_right);

            ObjectAnimator oa = ObjectAnimator.ofFloat(horizontalScrollView1,
                    "translationX", width , 0f);
            oa.setDuration(500);
            oa.start();

        } else {
            footerIsShowing = false;

//            footerImgShowSetting.setBackgroundResource(R.drawable.bg_press_white);
            footerImgShowSetting.setImageResource(R.drawable.icon_left);

            ObjectAnimator oa = ObjectAnimator.ofFloat(horizontalScrollView1,
                    "translationX", 0f, width + 200);
            oa.setDuration(500);
            oa.start();
        }

    }

    public void setData(DetailPage detailPage){
        this.detailPage = detailPage;
        //set Highlight icon bookmark if article is bookmarked
        if(databaseHelper.checkBookmarkExist(String.valueOf(detailPage.getmId())))
            setHighlightBookmark();
    }
    public void setHighlightBookmark(){
        img_bookmark.setImageResource(R.drawable.icon_bookmarked);
    }
}
