package view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

/**
 * Created by Tam Huynh on 12/22/2014.
 */
public class HeaderMenuYoutube extends android.app.Fragment {
    private ImageView btn_toggle,btn_header_back;
    private TextView tv_title_header_menu;
    View view;

    public void initUI() {
        btn_toggle = (ImageView) view.findViewById(R.id.btn_header_toggle);
        btn_toggle.setVisibility(View.GONE);
        tv_title_header_menu = (TextView)view.findViewById(R.id.tv_title_header_menu);
        btn_header_back = (ImageView) view.findViewById(R.id.btn_header_back);
        btn_header_back.setVisibility(View.VISIBLE);
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
    }



}
