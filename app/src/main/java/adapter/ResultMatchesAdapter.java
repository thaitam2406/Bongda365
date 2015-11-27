package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import library.serviceAPI.loader.ImageLoader;
import library.view.ImageViewCrop;
import model.ResultMatch;

/**
 * Created by Tam Huynh on 1/5/2015.
 */
public class ResultMatchesAdapter extends BaseAdapter {
    private ArrayList<ResultMatch> arraySchedule = new ArrayList<ResultMatch>();
    private Context mContext;
    private ImageLoader imageLoader;

    public ResultMatchesAdapter(Context context, ArrayList<ResultMatch> arraySchedule){
        this.arraySchedule = arraySchedule;
        this.mContext = context;
        imageLoader = new ImageLoader(mContext);
    }
    @Override
    public int getCount() {
        return arraySchedule.size();
    }

    @Override
    public Object getItem(int position) {
        return arraySchedule.get(position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_result_match,null);
            holder = new Holder();
            initUI(view,holder);

            view.setTag(holder);
        }else
            holder = (Holder) view.getTag();

        setData(arraySchedule.get(position),holder);
        setColorItem(holder,position);
        return view;
    }

    private void initUI(View view, Holder holder) {
        holder.thumbnailTeamLeft = (ImageViewCrop) view.findViewById(R.id.img_ic_team_left);
        holder.thumbnailTeamRight = (ImageViewCrop) view.findViewById(R.id.img_ic_team_right);
        holder.tvNameTeamLeft = (TextView) view.findViewById(R.id.tv_name_team_left);
        holder.tvNameTeamRight = (TextView) view.findViewById(R.id.tv_name_team_right);
        holder.tvDateTime = (TextView) view.findViewById(R.id.tv_date_match);
        holder.tvResult = (TextView)    view.findViewById(R.id.tv_result);
        holder.tvTime = (TextView) view.findViewById(R.id.tv_time_match);
        holder.ll1 = (ViewGroup) view.findViewById(R.id.ll1);
        holder.ll2 = (ViewGroup) view.findViewById(R.id.ll2);
        holder.ll_time = (ViewGroup) view.findViewById(R.id.ll_time);

        Typeface MyriadPro = Typeface.createFromAsset(mContext.getAssets(), "fonts/MyriadPro-Bold.otf");
        Typeface Roboto = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.tvNameTeamLeft.setTypeface(Roboto);
        holder.tvNameTeamRight.setTypeface(Roboto);
        holder.tvDateTime.setTypeface(Roboto);
        holder.tvResult.setTypeface(MyriadPro);
        holder.tvTime.setTypeface(Roboto);
    }

    private void setData(ResultMatch scheduleItem, Holder holder) {
        if(checkBigTeam(scheduleItem.getHomeTeam().getName()))
            holder.tvNameTeamLeft.setTextColor(mContext.getResources().getColor(R.color.red));
        if(checkBigTeam(scheduleItem.getAwayTeam().getName()))
            holder.tvNameTeamRight.setTextColor(mContext.getResources().getColor(R.color.red));
        holder.tvNameTeamLeft.setText(scheduleItem.getHomeTeam().getName());
        holder.tvNameTeamRight.setText(scheduleItem.getAwayTeam().getName());
        holder.tvResult.setText(scheduleItem.getResult());
        String[] dateTime = scheduleItem.getDateTime().split(" ");
        String date = dateTime[0];
        String time = dateTime [1];
        holder.tvDateTime.setText(date.split("-")[2] + "/" + date.split("-")[1]);
        holder.tvTime.setText(time.split(":")[0] + ":" + time.split(":")[1]);
        imageLoader.DisplayImage(scheduleItem.getHomeTeam().getThumbnailImg(), holder.thumbnailTeamLeft);
        imageLoader.DisplayImage(scheduleItem.getAwayTeam().getThumbnailImg(), holder.thumbnailTeamRight);
    }

    public class Holder{
        public ImageViewCrop thumbnailTeamLeft,thumbnailTeamRight;
        public TextView tvNameTeamLeft,tvNameTeamRight,tvDateTime,tvTime,tvResult;
        public ViewGroup ll1,ll2,ll_time;
    }
    private String[] bigTeams = {"Man","Ars","Chel","Liv","Paris","Marse","Lyon","Monaco","Real"
            ,"Bar","Ath","Inter","Juven","Liv","Milan","Roma","Munich","Liv","Dort","Liv"};
    private boolean checkBigTeam(String str){
        for(String item : bigTeams){
            if(str.contains(item))
                return true;
        }
        return false;
    }
    private void setColorItem(Holder holder, int pos){
        if(pos%2 == 0){
//            holder.ll1.setBackgroundResource(R.drawable.white_light_container);
//            holder.ll2.setBackgroundResource(R.drawable.gray_light_container);
//            holder.ll_time.setBackgroundResource(R.drawable.gray_dark_container);
        }else{
//            holder.ll2.setBackgroundResource(R.drawable.white_light_container);
//            holder.ll1.setBackgroundResource(R.drawable.gray_light_container);
//            holder.ll_time.setBackgroundResource(R.drawable.gray_light_container);
        }
    }
}
