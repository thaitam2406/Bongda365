package library.hearderMenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import model.CatVideoFootball;

/**
 * Created by Tam Huynh on 12/22/2014.
 */
public class HeaderMenuHome extends Fragment implements  View.OnClickListener,
        HeaderMenuView{
    private ImageView btn_toggle,btn_header_back, imgSelector;
    private TextView tv_title_header_menu;
    private Spinner spinnerSelectLeague, spinnerSelectCatVideo;
    private View view;
    private ArrayList<CatVideoFootball> catVideoFootballs ;
    private HeaderMenuPresenter headerMenuPresenter;
    private IOnHeaderMenuInterface onHeaderMenuInterface;
    private ViewGroup rl_btn_left,rl_parent;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == imgSelector.getId()){
            spinnerSelectLeague.performClick();
        }else if(id == btn_header_back.getId()){
            onHeaderMenuInterface.onBackHeaderClick();
        }

    }

    public void setTitleColor(int resByTheme) {
        rl_parent.setBackgroundColor(resByTheme);
    }

    public interface IOnHeaderMenuInterface{
        public void onFinishGetCatVideo(String ItemCat);
        public void onSelectLeague(int leagueID);
        public void onBackHeaderClick();
    }
    ArrayList<String> listCat = new ArrayList<String>();
    @Override
    public void setDataSelectorVideoCat(Object object) {
        CatVideoFootball[] catVideoFootballs = (CatVideoFootball[]) object;
        ArrayList<String> listName = new ArrayList<String>();
        String catStartFootBall = "2";
        for(CatVideoFootball cat : catVideoFootballs) {
            listCat.add(cat.getID());
            listName.add(cat.getName());
            if(cat.getID() == "2")
                catStartFootBall = cat.getID();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_spinner, listName);
        spinnerSelectCatVideo.setAdapter(adapter);
        spinnerSelectCatVideo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onHeaderMenuInterface.onFinishGetCatVideo(listCat.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private String [] arraySelectLeague ;
    @Override
    public void setDataSelectorLeague() {
        resetPositionTitle(false);
        arraySelectLeague = getResources().getStringArray(R.array.array_league_ranking);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_spinner, arraySelectLeague);
        spinnerSelectLeague.setAdapter(adapter);
        spinnerSelectLeague.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onHeaderMenuInterface.onSelectLeague(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void setUISelectLeague(){
        resetPositionTitle(false);
        spinnerSelectLeague.setVisibility(View.VISIBLE);
        setDataSelectorLeague();
    }

    @Override
    public void resetPositionTitle(boolean isNormal) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(isNormal){
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            tv_title_header_menu.setLayoutParams(params);
        }else{
            params.addRule(RelativeLayout.RIGHT_OF,rl_btn_left.getId());
            params.setMargins(30,0,0,0);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            tv_title_header_menu.setLayoutParams(params);
        }

    }

    @Override
    public void setUISelectorCatVideo() {
        resetPositionTitle(false);
        spinnerSelectCatVideo.setVisibility(View.VISIBLE);
        headerMenuPresenter.handleGetCatVideo();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onHeaderMenuInterface = (IOnHeaderMenuInterface) activity;
            headerMenuPresenter = new HeaderMenuPresenterImpl(getActivity(),this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initUI() {
        btn_toggle = (ImageView) view.findViewById(R.id.btn_header_toggle);
        tv_title_header_menu = (TextView)view.findViewById(R.id.tv_title_header_menu);
        btn_header_back = (ImageView) view.findViewById(R.id.btn_header_back);
        btn_header_back.setOnClickListener(this);
        spinnerSelectLeague = (Spinner) view.findViewById(R.id.spinner_select_league);
        spinnerSelectCatVideo = (Spinner) view.findViewById(R.id.spinner_select_cat_video);
        imgSelector = (ImageView)   view.findViewById(R.id.img_click_selector);
        imgSelector.setOnClickListener(this);
        rl_btn_left = (ViewGroup) view.findViewById(R.id.rl_btn_left);
        rl_parent = (ViewGroup) view.findViewById(R.id.rl_parent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.header_menu_home,null);
        initUI();
        return view;
    }

    public void updateButtonToggle()   {
        btn_toggle.setImageResource(R.drawable.slide_menu_ic_white);
    }

    public void setHighlightButtonToggle()   {
        btn_toggle.setImageResource(R.drawable.slide_menu_ic_hl);
    }

    public void setTitleHeaderMenu(String title){
        tv_title_header_menu.setText(title);
        if(!title.equals(getString(R.string.menu_item_ranking)))
            hideUISelectLeague();
        if(!title.equals(getString(R.string.menu_item_video_football)))
            hideUISelectCatVideo();
    }

    /*

    * */
    public void setIconLeftHeaderMenu(boolean isToggle){
        if(isToggle){
            btn_header_back.setVisibility(View.GONE);
            btn_toggle.setVisibility(View.VISIBLE);
        }else{
            btn_header_back.setVisibility(View.VISIBLE);
            btn_toggle.setVisibility(View.GONE);
        }
    }

    public void hideUISelectLeague(){
        spinnerSelectLeague.setVisibility(View.GONE);
        resetPositionTitle(true);
    }
    public void hideUISelectCatVideo(){
        spinnerSelectCatVideo.setVisibility(View.GONE);
        resetPositionTitle(true);
    }
}
