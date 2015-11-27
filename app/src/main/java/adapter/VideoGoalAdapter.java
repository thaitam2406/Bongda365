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
import library.view.ImageViewRounded;
import model.VideoGoalItem;

/**
 * Created by Tam Huynh on 12/27/2014.
 */
public class VideoGoalAdapter extends BaseAdapter {

    private ArrayList<VideoGoalItem> arrayVideoGoal = new ArrayList<VideoGoalItem>();
    private Context mContext;
    private ImageLoader imageLoader;
    private VideoFootBallAdapter.TYPE_VIDEO_LIST type_video_list;

    public VideoGoalAdapter(Context context, ArrayList<VideoGoalItem> array, VideoFootBallAdapter.TYPE_VIDEO_LIST type_video_list){
        this.arrayVideoGoal = array;
        this.mContext = context;
        imageLoader = new ImageLoader(mContext);
        this.type_video_list = type_video_list;

    }

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
//    public String getUrlAtPos(int pos){
//        return arrayVideoGoal.get(pos).getContentVideoGoal().get);
//    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder ;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_video_goal,null);
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
        holder.thumbnailCopyRight = (ImageViewRounded) view.findViewById(R.id.thumbnailCopyRight);
        holder.tv_title = (TextView) view.findViewById(R.id.tv_title_video_list);
        holder.tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle_video_list);
        holder.tv_dateTime = (TextView) view.findViewById(R.id.tv_date_time);
        holder.tv_countViewer = (TextView) view.findViewById(R.id.tv_count_viewer);
        holder.tvSourceName = (TextView) view.findViewById(R.id.tv_name_source);
        if(this.type_video_list == VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_FOOTBALL){
            holder.thumbnailCopyRight.setVisibility(View.GONE);
            holder.tv_subtitle.setVisibility(View.GONE);
            holder.tv_countViewer.setVisibility(View.GONE);
            holder.tv_dateTime.setVisibility(View.GONE);
            holder.tvSourceName.setVisibility(View.GONE);
        }
    }

    private void setData(VideoGoalItem videoGoalItem, Holder holder) {
        holder.tv_title.setText(videoGoalItem.getTitle());
        imageLoader.DisplayImage(videoGoalItem.getThumbnailVideo(), holder.thumbnailIcon);
        if(this.type_video_list == VideoFootBallAdapter.TYPE_VIDEO_LIST.VIDEO_GOAL){
            imageLoader.DisplayImage(videoGoalItem.getWebsite().getLogoArticle(),holder.thumbnailCopyRight);
            holder.tv_subtitle.setText(videoGoalItem.getSubtitle());
            holder.tv_countViewer.setText(videoGoalItem.getViewerCount().equals("")?" 5":" "+videoGoalItem.getViewerCount());
            holder.tv_dateTime.setText(videoGoalItem.getDateTime().split(" ")[0]);
            holder.tvSourceName.setText(videoGoalItem.getWebsite().getNameWebsite()+ "-");
        }
    }

    public class Holder{
        public ImageViewRounded thumbnailCopyRight;
        public ImageViewCrop thumbnailIcon;
        public TextView tv_title,tv_subtitle,tv_dateTime,tv_countViewer,tvSourceName;
    }

}
