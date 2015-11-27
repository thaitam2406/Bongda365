package library.photoView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamhuynh.bongda365.R;

import java.util.ArrayList;

import library.serviceAPI.loader.ImageLoader;

/**
 * Created by Tam Huynh on 2/23/2015.
 */
public class PhotoViewerActivity extends Activity {
    public static String keyArrayImage = "keyArrayImage";
    public static String keyArrayHint = "keyArrayHint";
    public static String keyPositionImage = "keyPositionImage";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewer);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayHint = new ArrayList<>();
        int posImage = 0;
        if(bundle != null){
            arrayList = bundle.getStringArrayList(keyArrayImage);
            arrayHint = bundle.getStringArrayList(keyArrayHint);
            posImage = bundle.getInt(keyPositionImage);
        }
        ImageAdapter adapter = new ImageAdapter(this,arrayList,arrayHint);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(posImage);
    }


    public class ImageAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<String> arrayImage = new ArrayList<>();
        private ArrayList<String> arrayHintImgs = new ArrayList<>();
        private ImageLoader imageLoader;
        ImageAdapter(Context context, ArrayList<String> arrayImage, ArrayList<String> arrayHintImgs){
            this.context=context;
            this.arrayImage = arrayImage;
            this.imageLoader = new ImageLoader(context);
            this.arrayHintImgs = arrayHintImgs;
        }
        @Override
        public int getCount() {
            return arrayImage.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_photo_viewer,null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageCenter = (ImageViewTouch) view.findViewById(R.id.img_center);
            viewHolder.tv_photo_number  = (TextView) view.findViewById(R.id.tv_photo_number);
            viewHolder.tv_hint_photo_view  = (TextView) view.findViewById(R.id.tv_hint_photo_view);
            viewHolder.imageCenter.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            viewHolder.imageCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context , PhotoSingleViewActivity.class);
                    it.putExtra(PhotoSingleViewActivity.keyUrlImg, arrayImage.get(position));
                    context.startActivity(it);
                }
            });


            viewHolder.tv_photo_number.setText(String.valueOf(position+1) + "/" + String.valueOf(arrayImage.size()));
            viewHolder.tv_hint_photo_view.setText(Html.fromHtml(arrayHintImgs.get(position)));
            imageLoader.DisplayImage(arrayImage.get(position), viewHolder.imageCenter);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        public class ViewHolder {
            ImageViewTouch imageCenter;
            TextView tv_hint_photo_view, tv_photo_number;

        }
    }
    public void onClickBack(View view){
        finish();
    }
}
