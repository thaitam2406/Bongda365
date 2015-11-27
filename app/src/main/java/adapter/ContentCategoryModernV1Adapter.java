package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import library.serviceAPI.loader.ImageLoader;
import library.view.ImageViewRounded;
import library.view.TouchHighlightImageView;
import model.ContentModernPage;

/**
 * Created by Tam Huynh on 1/5/2015.
 */
public class ContentCategoryModernV1Adapter extends BaseAdapter {
    private ArrayList<ContentModernPage> arrayContent = new ArrayList<ContentModernPage>();
    private Context mContext;
    private ImageLoader imageLoader;
    private IOnContentCategoryClick onContentCategoryClick;
    public interface IOnContentCategoryClick {
        public void onClickContent(String contentId, ContentModernPage contentModernPage);
    }
    public ContentCategoryModernV1Adapter(Context context, ArrayList<ContentModernPage> arrayContent){
        this.arrayContent = arrayContent;
        this.mContext = context;
        imageLoader = new ImageLoader(mContext);
        this.onContentCategoryClick = (IOnContentCategoryClick) context;
    }
    @Override
    public int getCount() {
        return arrayContent.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder ;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_content_category_modern_style1,null);
            holder = new Holder();
            initUI(view,holder);

            view.setTag(holder);
        }else {
            view.getTag();
            holder = (Holder) view.getTag();
        }

        setData(arrayContent.get(position),holder);
        setColorItem(holder,position);
        return view;
    }

    private void initUI(View view, Holder holder) {
        holder.thumbImg = (TouchHighlightImageView) view.findViewById(R.id.v1_main_image);
        holder.thumbnailSource = (ImageViewRounded) view.findViewById(R.id.v1_source_icon);
        holder.tvTitle = (TextView) view.findViewById(R.id.v1_title);
        holder.tvSubTitle = (TextView) view.findViewById(R.id.v1_sub_title);
        holder.tvSource_DateTime = (TextView) view.findViewById(R.id.v1_source_name_and_date);
        holder.ll1 = (LinearLayout) view.findViewById(R.id.v1);

        Typeface MyriadPro = Typeface.createFromAsset(mContext.getAssets(), "fonts/MyriadPro-Bold.otf");
        Typeface Roboto = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.tvTitle.setTypeface(MyriadPro);
        holder.tvSource_DateTime.setTypeface(MyriadPro);
        holder.tvSubTitle.setTypeface(Roboto);

    }

    private void setData(final ContentModernPage contentModernPage, Holder holder) {
        holder.tvTitle.setText(contentModernPage.getTitle());
        holder.tvSubTitle.setText("");
        holder.tvSubTitle.setText(contentModernPage.getSubTitle());
        holder.tvSource_DateTime.setText(contentModernPage.getNameSource() + " - "
                +contentModernPage.getDateTime(false));
        imageLoader.DisplayImage(contentModernPage.getThumbImg(), holder.thumbImg);
        imageLoader.DisplayImage(contentModernPage.getThumbnailSource(), holder.thumbnailSource);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContentCategoryClick.onClickContent(contentModernPage.getId(),contentModernPage);
            }
        });
        holder.tvSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContentCategoryClick.onClickContent(contentModernPage.getId(),contentModernPage);
            }
        });
        holder.thumbImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContentCategoryClick.onClickContent(contentModernPage.getId(),contentModernPage);
            }
        });
    }

    public class Holder{
        public TouchHighlightImageView thumbImg;
        public ImageViewRounded thumbnailSource;
        public TextView tvTitle,tvSubTitle,tvSource_DateTime;
        public ViewGroup ll1,ll2,ll_time;
    }

    private void setColorItem(Holder holder, int pos){
        if(pos%2 == 0){
        }else{
        }
    }
}
