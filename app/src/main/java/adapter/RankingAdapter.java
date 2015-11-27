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

import library.view.ImageViewCrop;
import library.serviceAPI.loader.ImageLoader;
import model.RankingItem;

/**
 * Created by Tam Huynh on 1/5/2015.
 */
public class RankingAdapter extends BaseAdapter {
    private ArrayList<RankingItem> arrayRanking = new ArrayList<RankingItem>();
    private Context mContext;
    private ImageLoader imageLoader;

    public RankingAdapter(Context context, ArrayList<RankingItem> arrayRanking){
        this.arrayRanking = arrayRanking;
        this.mContext = context;
        imageLoader = new ImageLoader(mContext);
    }
    @Override
    public int getCount() {
        return arrayRanking.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayRanking.get(position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_ranking,null);
            holder = new Holder();
            initUI(view,holder);

            view.setTag(holder);
        }else
            holder = (Holder) view.getTag();

        setData(arrayRanking.get(position),holder,position);
        setColorItem(holder,position);
        return view;
    }

    private void initUI(View view, Holder holder) {
        holder.thumbnailTeam = (ImageViewCrop) view.findViewById(R.id.img_ic_team);
        holder.tvNameTeam = (TextView) view.findViewById(R.id.tv_name_team_pos);
        holder.tvPositionRank = (TextView) view.findViewById(R.id.tv_position);
        holder.tvNumberMatched = (TextView) view.findViewById(R.id.tv_number_matched);
        holder.tvRatioGoal = (TextView) view.findViewById(R.id.tv_ratio);
        holder.tvPoint = (TextView) view.findViewById(R.id.tv_point);
        holder.parent = (ViewGroup) view.findViewById(R.id.parent_ranking);

        Typeface MyriadPro = Typeface.createFromAsset(mContext.getAssets(), "fonts/MyriadPro-Bold.otf");
        Typeface Roboto = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.tvNameTeam.setTypeface(MyriadPro);
        holder.tvPositionRank.setTypeface(Roboto);
        holder.tvNumberMatched.setTypeface(Roboto);
        holder.tvRatioGoal.setTypeface(Roboto);
        holder.tvPoint.setTypeface(Roboto);
    }

    private void setData(RankingItem rankingItem, Holder holder,int pos) {
        holder.tvNameTeam.setText(rankingItem.getTeamItems().getName());
        holder.tvNumberMatched.setText(rankingItem.getNumberMatched());
        holder.tvPoint.setText(rankingItem.getPoint());
        holder.tvPositionRank.setText(String.valueOf(pos+1));
        holder.tvRatioGoal.setText(rankingItem.getRatioGoal());
        imageLoader.DisplayImage(rankingItem.getTeamItems().getLogoURL(), holder.thumbnailTeam);
    }

    public class Holder{
        public ImageViewCrop thumbnailTeam;
        public TextView tvPositionRank,tvNameTeam,tvNumberMatched,tvRatioGoal,tvPoint;
        public ViewGroup parent;
    }

    private void setColorItem(Holder holder, int pos){
        if(pos%2 == 0){
            holder.parent.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else{
            holder.parent.setBackgroundColor(mContext.getResources().getColor(R.color.gray2));
        }
        if(pos == 1 || pos ==2 || pos == 0){
            holder.tvPositionRank.setBackgroundColor(mContext.getResources().getColor(R.color.blue1));
        }else if(pos ==3)
            holder.tvPositionRank.setBackgroundColor(mContext.getResources().getColor(R.color.blue2));
        else if(pos >=  arrayRanking.size() - 3)
            holder.tvPositionRank.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        else if(pos == arrayRanking.size() - 4)
            holder.tvPositionRank.setBackgroundColor(mContext.getResources().getColor(R.color.red_light));
        else
            holder.tvPositionRank.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
    }
}
