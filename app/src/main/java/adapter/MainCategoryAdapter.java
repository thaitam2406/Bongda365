package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fragment.HomeModernFragment;
import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import library.serviceAPI.loader.ImageLoader;
import model.ModernNew;

/**
 * Created by Tam Huynh on 12/30/2014.
 */
public class MainCategoryAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<ModernNew> array = new ArrayList<ModernNew>();
    private ImageLoader imgLoader;
    HomeModernFragment.IOnCategoryChoose interfaceCatChoose;
    public MainCategoryAdapter(Context context, ArrayList<ModernNew> array){
        this.mContext = context;
        this.array = array;
        this.imgLoader = new ImageLoader(context);
        interfaceCatChoose = (HomeModernFragment.IOnCategoryChoose)context;
    }
    @Override
    public int getCount() {
        int i = (array.size() - 5);
        if (i % 6 == 0)
            return i / 6 + 1;
        else
            return i / 6 + 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if(position == 0){
//            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.modern_page_one,null);
                holder = new Holder();
                initUI(view,holder);
//                view.setTag(holder);
//            }else {
//                view.getTag();
//                holder = (Holder) view.getTag();
//            }
        }else{
//            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.modern_page_two,null);
                holder = new Holder();
                initUI(view,holder);
//                view.setTag(holder);
//            }else {
//                view.getTag();
//                holder = (Holder) view.getTag();
//            }
        }
        int index = 0;
        if(position != 0)
            index = 5 + 6 * (position - 1);
        if(position != array.size() - 1)
            initData(array, holder,index);
        return view;
    }

    private void initData(final ArrayList<ModernNew> modernNews,Holder holder,final int index) {
        TextView[] tvs = {holder.tv1,holder.tv2,holder.tv3,holder.tv4,holder.tv5,holder.tv6};
        ImageView[] imgs = {holder.img1,holder.img2,holder.img3,holder.img4,holder.img5,holder.img6};
        for(int i = 0;i < tvs.length; i++) {
            try {
                Typeface MyriadPro = Typeface.createFromAsset(mContext.getAssets(), "fonts/MyriadPro-Bold.otf");
                Typeface Roboto = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
                tvs[i].setTypeface(MyriadPro);
                tvs[i].setText(modernNews.get(i + index).getName());
                imgLoader.DisplayImage(modernNews.get(i + index).getThumbnail().getSrc(), imgs[i]);
                final int j = i + index;
                imgs[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interfaceCatChoose.onClickItemCategory(modernNews.get(j).getId(),modernNews.get(j).getName());
                    }
                });
                tvs[i].setVisibility(View.VISIBLE);
                imgs[i].setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void initUI(View view, Holder holder) {
//        holder.ll_parent = (ViewGroup) view.findViewById(R.id.ll_parent);
//        holder.ll1 = (ViewGroup) view.findViewById(R.id.ll1);
//        holder.ll2 = (ViewGroup) view.findViewById(R.id.ll2);
        holder.tv1  =  (TextView)  view.findViewById(R.id.tv1);
        holder.tv2  =  (TextView)  view.findViewById(R.id.tv2);
        holder.tv3  =  (TextView)  view.findViewById(R.id.tv3);
        holder.tv4  =  (TextView)  view.findViewById(R.id.tv4);
        holder.tv5  =  (TextView)  view.findViewById(R.id.tv5);
        holder.img1 = (ImageView) view.findViewById(R.id.img1);
        holder.img2 = (ImageView) view.findViewById(R.id.img2);
        holder.img3 = (ImageView) view.findViewById(R.id.img3);
        holder.img4 = (ImageView) view.findViewById(R.id.img4);
        holder.img5 = (ImageView) view.findViewById(R.id.img5);
        try {
            holder.img6 = (ImageView) view.findViewById(R.id.img6);
            holder.tv6 = (TextView)  view.findViewById(R.id.tv6);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class Holder {
//        ViewGroup ll1,ll2,ll_parent;
        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        ImageView img1,img2,img3,img4,img5,img6;
    }
}
