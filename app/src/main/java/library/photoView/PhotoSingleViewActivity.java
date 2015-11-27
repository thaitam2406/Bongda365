package library.photoView;

import android.app.Activity;
import android.os.Bundle;

import com.tamhuynh.bongda365.R;

import library.serviceAPI.loader.ImageLoader;

/**
 * Created by Tam Huynh on 2/23/2015.
 */
public class PhotoSingleViewActivity  extends Activity{
    private ImageLoader imageLoader;
    public static String keyUrlImg = "keyUrlImg";
    private String urlImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_photo);
        imageLoader = new ImageLoader(getApplicationContext());
        ImageViewTouch touchImageView = (ImageViewTouch)findViewById(R.id.img_photo_view);
        touchImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        Bundle bundle = getIntent().getExtras();
        urlImg = bundle.getString(keyUrlImg);
        imageLoader.DisplayImage(urlImg, touchImageView);

    }
}
