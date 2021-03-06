package library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Tam Huynh on 12/22/2014.
 */

public class ImageViewRounded extends ImageView {
    private static int mCorners = 10;
    private boolean makeRequest;

    public ImageViewRounded(Context context) {
        super(context);
        makeRequest = true;
    }

    public ImageViewRounded(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ImageViewRounded(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        makeRequest = true;
    }
    public void setCorner(int corners) {
        mCorners = corners;
    }
    @Override
    public void setImageBitmap(Bitmap bm) {
        makeRequest = false;
        super.setImageBitmap(bm);
        makeRequest = true;
    }
    @Override
    public void setImageResource(int resId) {
        makeRequest = false;
        super.setImageResource(resId);
        makeRequest = true;
    }
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        try {
            Bitmap b =  ((BitmapDrawable)drawable).getBitmap() ;
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

            int w = getWidth(), h = getHeight();


            Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);
            canvas.drawBitmap(roundBitmap, 0,0, null);
        } catch (Exception e) {
            // TODO: handle exception
        }


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

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.1f, sbmp.getHeight() / 2 + 0.1f,
                sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);


        return output;
    }


}
