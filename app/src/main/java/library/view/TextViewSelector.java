package library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tamhuynh.bongda365.R;

/**
 * Created by Tam Huynh on 1/9/2015.
 */
public class TextViewSelector extends LinearLayout implements View.OnClickListener{
    private  Context mContext;
    private TouchHighlightTextView tv1,tv2;
    private IOnClickSelector onClickSelector;
    public interface IOnClickSelector{
        public void onLeftClick();
        public void onRightClick();
    }
    public TextViewSelector(Context context) {
        super(context);
        mContext = context;
        initUI();
    }

    public void setOnClickSelector(IOnClickSelector listener){
        onClickSelector = listener;
    }

    public TextViewSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUI();
    }

    public TextViewSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initUI();
    }

    private void initUI(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.customize_select_view,this);
        tv1 = (TouchHighlightTextView) findViewById(R.id.tv1);
        tv2 = (TouchHighlightTextView) findViewById(R.id.tv2);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);

    }
    public void setText(String left, String right){
        if(tv1 != null && tv2 != null){
            tv1.setText(left);
            tv2.setText(right);
        }
    }
    public void setHightlightBtnLeft(){
        tv1.setBackgroundColor(mContext.getResources().getColor(R.color.color_blue_default));
        tv2.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
    }
    public void setHightlightBtnRight(){
        tv2.setBackgroundColor(mContext.getResources().getColor(R.color.color_blue_default));
        tv1.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == tv1.getId()){
            setHightlightBtnLeft();
            if(onClickSelector != null)
                onClickSelector.onLeftClick();
        }else if(id == tv2.getId()){
            setHightlightBtnRight();
            if(onClickSelector != null)
                onClickSelector.onRightClick();
        }
    }
}
