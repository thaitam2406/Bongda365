package com.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import MVP.presenter.DetailNewsPresenter;
import MVP.presenter.DetailPresenterImpl;
import MVP.view.ModernDetailView;
import library.anim.AnimUtil;
import library.photoView.PhotoViewerActivity;
import library.serviceAPI.loader.ImageLoader;
import library.view.ImageViewDetailNews;
import library.view.ImageViewRounded;
import library.view.ScrollViewExt;
import library.view.ToolbarView;
import model.Content;
import model.ContentModernPage;
import model.DetailPage;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;
import util.ShareManagement;

/**
 * Created by Tam Huynh on 12/29/2014.
 */
public class DetailArticcleFragment extends BaseFragment implements
        FlipView.OnOverFlipListener,FlipView. OnFlipListener, View.OnClickListener
        ,ModernDetailView, ToolbarView.IOnToobarListener, ScrollViewExt.ScrollViewListener {

    private String detailNewsId;
    private ViewGroup rlProgressBar;
    private DetailNewsPresenter detailNewsPresenter;
    private TextView tvTitle,tvSourceName,tvDate,tvSubtitle;
    private ImageViewRounded imgSourceIcon;
    private ImageLoader imageLoader;
    private ToolbarView toolbarView;
    private WebView webView;
    private DetailPage detailPage;
    private ViewGroup ll_detail;
    private ScrollViewExt scrollViewExt;
    public static String contentObjectKey = "contentObjectKey";
    private ContentModernPage contentModernPage;
    @Override
    public void initUI() {
        rlProgressBar = (ViewGroup) view.findViewById(R.id.view_group_progressbar);
//        showProgress();
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvSourceName = (TextView) view.findViewById(R.id.tvSourceName);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        imgSourceIcon = (ImageViewRounded) view.findViewById(R.id.imgSourceIcon);
        toolbarView = (ToolbarView) view.findViewById(R.id.toolbarView);
        webView = (WebView) view.findViewById(R.id.webView1);
        ll_detail = (ViewGroup) view.findViewById(R.id.llDetails);
        scrollViewExt = (ScrollViewExt) view.findViewById(R.id.scrollView1);
        tvSubtitle = (TextView) view.findViewById(R.id.tv_subTitle);

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            detailNewsPresenter = new DetailPresenterImpl(this);
            imageLoader = new ImageLoader(mContext);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getBundle(){
        Bundle bundle = getArguments();
        detailNewsId = bundle.getString(ListArticleModern.contentIdKey);
        try {
            contentModernPage = bundle.getParcelable(contentObjectKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void initData() {
        getBundle();
        if(contentModernPage != null)
            initFirstUI();
        else
            showProgress();
        this.detailNewsPresenter.getDataDetailNews(mContext,detailNewsId);

    }

    private void initFirstUI() {
        tvTitle.setText(contentModernPage.getTitle());
        tvSourceName.setText(contentModernPage.getNameSource());
        tvDate.setText("- " + contentModernPage.getDateTime(false));
        imageLoader.DisplayImage(contentModernPage.getThumbnailSource(), imgSourceIcon);
        tvSubtitle.setText(contentModernPage.getSubTitle());

        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Bold.otf");
        tvTitle.setTypeface(fontRegular);
        tvDate.setTypeface(fontRegular);
        tvSourceName.setTypeface(fontRegular);
        tvSubtitle.setTypeface(fontRegular);
    }

    @Override
    public void initListener() {
        toolbarView.setOnToolBarListener(this);
        scrollViewExt.setScrollViewListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setXml(R.layout.fragment_detail_modern_style);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent it = new Intent(getActivity(), PhotoViewerActivity.class);
        int posImageClick = 0;
        for(int i = 0;i < arrayImage.size() ; i++){
            if(i == id - 1000)
                posImageClick = i;

        }
        it.putStringArrayListExtra(PhotoViewerActivity.keyArrayImage,arrayImage);
        it.putExtra(PhotoViewerActivity.keyPositionImage, posImageClick);
        it.putExtra(PhotoViewerActivity.keyArrayHint, arrayHint);
        mContext.startActivity(it);

    }
    private ArrayList<String> arrayImage = new ArrayList<>();
    private ArrayList<String> arrayHint = new ArrayList<>();
    @Override
    public void setDataUI(final DetailPage detailPage) {
        /**
         * collect images list
         */
        for(Content content : detailPage.getContent()){
            if(content.getType().equals("img")){
                arrayImage.add(content.getSrc());
                arrayHint.add(content.getAlt());
            }
        }
        if(arrayImage.size() > 0)
            toolbarView.setShareInfo(arrayImage.get(0), arrayHint.get(0));
        hideProgress();
        if(contentModernPage == null){
            tvTitle.setText(detailPage.getmTitle());
//            tvSourceName.setText(contentModernPage.getNameSource());
            tvDate.setText("- " + detailPage.getmDate(false));
//            imageLoader.DisplayImage(detailPage.getSourceIcon(), imgSourceIcon);
            tvSubtitle.setText(detailPage.getSubTitle());
        }

        this.detailPage = detailPage;
        toolbarView.setData(detailPage);

        /**
         * get color by theme
         */
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.headerTitle, typedValue, true);
        /**
         * get color by theme
         */

        ArrayList<Integer> tvContentsId = new ArrayList<>();
        ArrayList<Integer> tvHintContentsId = new ArrayList<>();
        int indexImg = 0;
        for (int i = 0; i < detailPage.getContent().size(); i++) {
            if(detailPage.getContent().get(i).getType().equals("p")){
                TextView tv1 = new TextView(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                /**
                 * set more space for image
                 */
                boolean isNearImage = false;
                //except the last item, get more space at bottom
                if(i != detailPage.getContent().size() - 1 && detailPage.getContent().get(i + 1).getType().equals("img")) {
                    isNearImage = true;
                    tv1.setPadding(0, 20, 0, 60);
                }
                //except the first item, get more space at top
                if(i != 0 && detailPage.getContent().get(i - 1).getType().equals("img")) {
                    isNearImage = true;
                    tv1.setPadding(0, 60, 0, 20);
                }
                if(!isNearImage)
                    tv1.setPadding(0,20,0,20);
                /**
                 * set more space for image
                 */
                tv1.setId(i + 1000);
                tvContentsId.add(i + 1000);
                tv1.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_item_detail));
                tv1.setLayoutParams(lp);
                tv1.setText(Html.fromHtml(detailPage.getContent().get(i).getContent()));
                Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
                Typeface MyriadPro = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Bold.otf");
                tv1.setTypeface(fontRegular);
                tv1.setTextColor(typedValue.data);
                /**
                 *  add text into view
                 */
                ll_detail.addView(tv1);
            }else{
                final ImageViewDetailNews image = new ImageViewDetailNews(mContext);
                image.setId(indexImg + 1000);
                indexImg ++ ;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                image.setLayoutParams(params);
                image.setOnClickListener(this);
                imageLoader.DisplayImage(detailPage.getContent().get(i).getSrc(), image);
                /*
                 *   add image into view
                 */
                ll_detail.addView(image);


                /**
                 * check hint text image not null
                 */
                if(!detailPage.getContent().get(i).getAlt().equals("")){
                    TextView tvHint = new TextView(mContext);

                    tvHint.setId(i + 10000);
                    tvHintContentsId.add(i + 10000);
                    tvHint.setTextColor(typedValue.data);
                    tvHint.setBackgroundColor(getResources().getColor(R.color.gray));
                    tvHint.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_item_detail) - 2);
                    params.setMargins(0,-15,0,0);
                    tvHint.setLayoutParams(params);
                    tvHint.setText(Html.fromHtml(detailPage.getContent().get(i).getAlt()));
                    tvHint.setPadding(50,20,50,20);
                    tvHint.setTypeface(null, Typeface.ITALIC);
                    Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");
                    tvHint.setTypeface(fontRegular);
                    /**
                     * add text hint below image
                     */
                    ll_detail.addView(tvHint);
                }
            }


        }
        /**
         * collect ID textView for reset size
         */
        detailPage.setIdArrayText(tvContentsId);
        detailPage.setIdArrayHint(tvHintContentsId);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(toolbarView != null && toolbarView.flagLoginFB == 1 && !new ShareManagement(mContext).loadFacebook().getUserName().equals("")){
            toolbarView.handleShare();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showProgress() {
        rlProgressBar.invalidate();
        rlProgressBar.setVisibility(View.VISIBLE);
//        AnimUtil.AlphaFadeIn(rlProgressBar);
    }
    @Override
    public void hideProgress() {
//        rlProgressBar.setVisibility(View.GONE);
        if(rlProgressBar.isShown())
            AnimUtil.AlphaFadeOut(rlProgressBar);
    }

    @Override
    public void hideToolbar() {
        toolbarView.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbarView.setVisibility(View.VISIBLE);
//        AnimUtil.showViewFromRightSide(toolbarView);
    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {
    }
    @Override
    public void onItemLvClick(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onStart() {
        super.onStart();
//        initData();
    }

    @Override
    public void onFontClick(int size) {
//        tvTitle.setTextSize(size);
//        tvSourceName.setTextSize(size - 3);
//        tvDate.setTextSize(size - 3);

        for (int i = 0; i < detailPage.getIdArrayText().size(); i++)
            ((TextView) view.findViewById(detailPage.getIdArrayText().get(i)))
                    .setTextSize(size);

        for (int i = 0; i < detailPage.getIdArrayHint().size(); i++)
            ((TextView) view.findViewById(detailPage.getIdArrayHint().get(i)))
                    .setTextSize(size - 3);
    }
    private boolean isShowWebview = false;
    @Override
    public void onClickWebview() {
        if(!isShowWebview) {
            startWebView(detailPage.getUrlArticle());
        }
        else {
            hideProgress();
            isShowWebview = false;
            webView.setVisibility(View.GONE);
        }
    }
    @JavascriptInterface
    private void startWebView(String url) {
        webView.setVisibility(View.VISIBLE);
        isShowWebview = true;
//        showProgress();
        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setGeolocationDatabasePath(mContext.getCacheDir().getPath());
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        class JsObject {
            @JavascriptInterface
            public String toString() { return "injectedObject"; }
        }
        webView.addJavascriptInterface(new JsObject(), "injectedObject");
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("http://www.foodserviceconsultant.org/find-a-member-app/");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("webKit",consoleMessage.toString());
                return super.onConsoleMessage(consoleMessage);
            }
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message,           JsResult result) {
                //Required functionality here
                return super.onJsAlert(view, url, message, result);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgress();
            }
        });
        //        webView.loadUrl(url);

//        try {
//            String html = readAssetFile("www/index.html");
//            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
//        } catch (IOException e) {
//        }
//        webView.loadDataWithBaseURL("http://www.foodserviceconsultant.org/find-a-member-app/", null, "text/html", "UTF-8", null);
    }
    private String readAssetFile(String filePath) throws IOException {
        StringBuilder buffer = new StringBuilder();
        InputStream fileInputStream = mContext.getAssets().open(filePath);
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
        String str;

        while ((str=bufferReader.readLine()) != null) {
            buffer.append(str);
        }
        fileInputStream.close();

        return buffer.toString();
    }
    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
        View view = scrollView
                .getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                .getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff < 500) {
            // do stuff
            // Toast.makeText(context, "down", 400).show();
            if (toolbarView.footerIsShowing) {
                toolbarView.showSetting();
            }
        } else if (diff > 500 && !toolbarView.footerIsShowing) {
            toolbarView.showSetting();
        }
    }
}
