package library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Tam Huynh on 12/22/2014.
 */

public class ImageViewCrop extends ImageView {
    private static int mCorners = 10;
    private boolean makeRequest;

    public ImageViewCrop(Context context) {
        super(context);
        makeRequest = true;
    }

    public ImageViewCrop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ImageViewCrop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        makeRequest = true;
    }
    public void setCorner(int corners) {
        mCorners = corners;
    }
    @Override
    public void setImageBitmap(Bitmap bm) {
        makeRequest = false;
        super.setImageBitmap(getCroppedBitmap(bm));
        makeRequest = true;
    }
    @Override
    public void setImageResource(int resId) {
        makeRequest = false;
        super.setImageResource(resId);
        makeRequest = true;
    }

    @Override
    public void setImageURI(Uri uri) {
        makeRequest = false;
        super.setImageURI(uri);
        makeRequest = true;
    }
    @Override
    public void setImageDrawable(Drawable drawable) {
        makeRequest = false;
        super.setImageDrawable(drawable);
        makeRequest = true;
    }
    @Override
    public void requestLayout() {
        if (makeRequest)
            super.requestLayout();
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp) {
        try{
            int width = bmp.getWidth(), height = bmp.getHeight();
            Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, width, height);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.parseColor("#BAB399"));
            canvas.drawRoundRect(new RectF(rect), mCorners, mCorners, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bmp, rect, rect, paint);
            return output;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }


}
