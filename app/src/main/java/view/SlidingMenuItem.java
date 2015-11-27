package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tamhuynh.bongda365.R;

/**
 * Created by Tam Huynh on 12/22/2014.
 */
public class SlidingMenuItem extends RelativeLayout implements View.OnClickListener {
    /**
     * Tag fragment for find & management by Home
     */
    public static String MENU_ITEM_VIDEO_GOAL = "MENU_ITEM_VIDEO_GOAL" ;
    public static String MENU_ITEM_SCHEDULE = "MENU_ITEM_SCHEDULE" ;
    public static String MENU_ITEM_RESULT_MATCH = "MENU_ITEM_RESULT_MATCH" ;
    public static String MENU_ITEM_SCHEDULE_TODAY = "MENU_ITEM_SCHEDULE_TODAY" ;
    public static String MODERN_STYLE_PAGE = "MODERN_STYLE_PAGE" ;
    public static String MENU_ITEM_RANKING = "MENU_ITEM_RANKING" ;
    public static String MENU_ITEM_HOME = "MENU_ITEM_HOME" ;
    public static String MENU_ITEM_CONTENT_CAT_MODERN = "MENU_ITEM_CONTENT_CAT_MODERN" ;
    public static String MENU_ITEM_DETAIL_NEWS = "MENU_ITEM_DETAIL_NEWS" ;
    public static String MENU_ITEM_VIDEO_FOOTBALL = "MENU_ITEM_VIDEO_FOOTBALL" ;
    public static String MENU_ITEM_SETTING = "MENU_ITEM_SETTING" ;
    public static String MENU_ITEM_BOOKMARK = "MENU_ITEM_BOOKMARK" ;

    Context mContext ;
    ViewGroup ll_first_item,ll_second_item,ll_third_item,ll_four_item,ll_home_item,ll_five_item,
            ll_setting_item,ll_bookmark_item,ll_result_item;
    IMenuItemClick listener;

    public interface IMenuItemClick{
        public void onMenuItemClick(String tag);
    }

    public SlidingMenuItem(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SlidingMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SlidingMenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        listener = (IMenuItemClick) mContext;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        layoutInflater.inflate(R.layout.sliding_menu_item,this);
        ll_first_item = (ViewGroup) findViewById(R.id.first_item);
        ll_first_item.setOnClickListener(this);
        ll_second_item = (ViewGroup) findViewById(R.id.second_item);
        ll_second_item.setOnClickListener(this);
        ll_third_item = (ViewGroup) findViewById(R.id.third_item);
        ll_third_item.setOnClickListener(this);
        ll_four_item = (ViewGroup) findViewById(R.id.four_item);
        ll_four_item.setOnClickListener(this);
        ll_home_item = (ViewGroup) findViewById(R.id.home_item);
        ll_home_item.setOnClickListener(this);
        ll_five_item = (ViewGroup) findViewById(R.id.five_item);
        ll_five_item.setOnClickListener(this);
        ll_setting_item = (ViewGroup) findViewById(R.id.setting_item);
        ll_setting_item.setOnClickListener(this);
        ll_bookmark_item = (ViewGroup) findViewById(R.id.bookmark_item);
        ll_bookmark_item.setOnClickListener(this);
        ll_result_item = (ViewGroup)    findViewById(R.id.result_item);
        ll_result_item.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == ll_first_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_VIDEO_GOAL);
        }else if(id == ll_second_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_SCHEDULE);
        }else if(id == ll_third_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_SCHEDULE_TODAY);
        }else if(id == ll_four_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_RANKING);
        }else if(id == ll_home_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_HOME);
        }else if(id == ll_five_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_VIDEO_FOOTBALL);
        }else if(id == ll_setting_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_SETTING);
        }else if(id == ll_bookmark_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_BOOKMARK);
        }else if(id == ll_result_item.getId()){
            listener.onMenuItemClick(MENU_ITEM_RESULT_MATCH);
        }
    }
}
