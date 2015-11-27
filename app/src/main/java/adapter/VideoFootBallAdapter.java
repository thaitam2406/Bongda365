package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import library.serviceAPI.loader.ImageLoader;
import library.view.ImageViewCrop;
import model.VideoFootBall;

/**
 * Created by Tam Huynh on 12/27/2014.
 */
public class VideoFootBallAdapter extends BaseAdapter {

    private ArrayList<VideoFootBall> arrayVideoGoal = new ArrayList<VideoFootBall>();
    private Context mContext;
    private ImageLoader imageLoader;
    private TYPE_VIDEO_LIST type_video_list;

    public VideoFootBallAdapter(Context mContext, ArrayList<VideoFootBall> arrayVideoFootBall, TYPE_VIDEO_LIST typeVideoList)
    {
        this.arrayVideoGoal = arrayVideoFootBall;
        this.mContext = mContext;
        imageLoader = new ImageLoader(mContext);
        this.type_video_list = typeVideoList;
    }

    public static enum TYPE_VIDEO_LIST {VIDEO_GOAL, VIDEO_FOOTBALL};


    @Override
    public int getCount() {
        return arrayVideoGoal.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayVideoGoal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public String getUrlAtPos(int pos){
        return arrayVideoGoal.get(pos).getThumbImg();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder ;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_video_football,null);
            holder = new Holder();
            initUI(view,holder);

            view.setTag(holder);
        }else
            holder = (Holder) view.getTag();

        setData(arrayVideoGoal.get(position),holder);
        return view;
    }

    private void initUI(View view, Holder holder) {
        holder.thumbnailIcon = (ImageViewCrop) view.findViewById(R.id.thumbnail_video_goal);
        holder.tv_title = (TextView) view.findViewById(R.id.tv_title_video_list);
    }

    private void setData(VideoFootBall videoFootBall, Holder holder) {
        holder.tv_title.setText(videoFootBall.getName());
        imageLoader.DisplayImage(videoFootBall.getThumbImg(), holder.thumbnailIcon);
    }

    public class Holder{
        public ImageViewCrop thumbnailIcon;
        public TextView tv_title;
    }

}
