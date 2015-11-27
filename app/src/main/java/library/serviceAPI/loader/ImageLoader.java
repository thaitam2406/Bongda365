package library.serviceAPI.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bongda365.BongdaApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import library.view.ImageViewCrop;
import library.view.ImageViewDetailNews;


public class ImageLoader {

	int REQUIRED_SIZE = 0;
	FileCache fileCache;
	ExecutorService executorService;
	Handler handler = new Handler();// handler to display images in UI thread

	MemoryCache memoryCache = new MemoryCache();
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	public ImageLoader(Context context) {
		init(context);
	}

	public ImageLoader(Context context, int requiredSize) {
		REQUIRED_SIZE = requiredSize;
		init(context);
	}

	private void init(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(3);
	}

	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			if (imageView instanceof ImageViewCrop) {
				((ImageViewCrop) imageView).setImageBitmap(bitmap);
			} else
				imageView.setImageBitmap(bitmap);
		else {
            imageView.setImageBitmap(null);
			queuePhoto(url, imageView);
		}
 	}

	public void DisplayImage(String url, ImageView imageView,
			int rotationDegrees) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url + rotationDegrees);
		if (bitmap != null) {
			if (imageView instanceof ImageViewCrop) {
                ((ImageViewCrop) imageView).setImageBitmap(bitmap);
			} else
				imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView, rotationDegrees);
		}
	}

	public void DisplayImage(String url, ImageView imageView,
			boolean roundCorner) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			if (imageView instanceof ImageViewCrop) {
				((ImageViewCrop) imageView).setImageBitmap(bitmap);
			} else
				imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView, roundCorner);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private void queuePhoto(String url, ImageView imageView, int rotationDegrees) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, rotationDegrees);
		executorService.submit(new PhotosLoader(p));
	}

	private void queuePhoto(String url, ImageView imageView, boolean roundCorner) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, roundCorner);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		Bitmap b;
		File f;

		// from SD card
		f = new File(url);
		if (f.exists()) {
			b = decodeFile(f);
			if (b != null) {
				// Log.w("Loader", "getBitmap, from SD cache =" + url);
				return b;
			}
		}

		// disk card
		f = fileCache.getFile(url);

		b = decodeFile(f);

		if (b != null) {
			// Log.w("Loader", "getBitmap, from SD cache =" + url);
			return b;
		}

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(100000);
			conn.setReadTimeout(100000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f);
			// Log.w("Loader", "getBitmap, from web =" + url);

			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	private void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1;
			stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);

			int scale = 1;
			if (REQUIRED_SIZE > 0) {
				// Find the correct scale value. It should be the power of 2.
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			// Log.d("ImageController", "requiredsize=" + REQUIRED_SIZE
			// + ", scale=" + scale + ", bitmap w=" + bitmap.getWidth()
			// + " h=" + bitmap.getHeight());
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public int value = 0;
		public boolean rotate = false;
		public boolean roundCorner = false;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}

		public PhotoToLoad(String u, ImageView i, int value) {
			url = u;
			imageView = i;

			if (value != 0) {
				rotate = true;
				this.value = value;
			}
		}

		public PhotoToLoad(String u, ImageView i, boolean roundCorner) {
			url = u;
			imageView = i;
			this.roundCorner = roundCorner;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {

				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				// round corners or rotate image
				if (photoToLoad.roundCorner) {

				} else if (photoToLoad.rotate) {
					bmp = addShadow(bmp, 4);
					Matrix matrix = new Matrix();
					matrix.postRotate(photoToLoad.value);
					bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
							bmp.getHeight(), matrix, true);
				}

				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {

				memoryCache.put(photoToLoad.url, bitmap);
				if (photoToLoad.imageView instanceof ImageViewCrop) {
					((ImageViewCrop) photoToLoad.imageView).setImageBitmap(bitmap);
				} else if(photoToLoad.imageView instanceof ImageViewDetailNews){
                    float rate = (float) (BongdaApplication.Instance().getWidthBongDa() * 1.0 / bitmap
                            .getWidth());

                    int h_img = (int) (bitmap.getHeight() * rate);

                    android.widget.LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT, h_img);

                    layout.gravity = Gravity.CENTER;

                    photoToLoad.imageView.setLayoutParams(layout);
                    photoToLoad.imageView.setImageBitmap(bitmap);
                }else
                    photoToLoad.imageView.setImageBitmap(bitmap);
				/*
				 * TransitionDrawable td = null; Drawable[] drawables = new
				 * Drawable[2]; drawables[0] = mStubDrawable; drawables[1] = new
				 * BitmapDrawable(res, bitmap); td = new
				 * TransitionDrawable(drawables); td.setCrossFadeEnabled(true);
				 * //important if you have transparent bitmaps
				 * photoToLoad.imageView.setImageDrawable(td); //
				 * photoToLoad.imageView.setImageBitmap(bitm ap); //a little
				 * crossfade td.startTransition(200);
				 */
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static Bitmap setRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	private Bitmap addShadow(Bitmap bitmap, float cornerRadius) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth() + 4,
				bitmap.getHeight() + 4, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		int shadowRadius = 2;
		final Rect imageRect = new Rect(shadowRadius, shadowRadius,
				bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(imageRect);

		// This does not achieve the desired effect
		Paint shadowPaint = new Paint();
		shadowPaint.setAntiAlias(true);
		shadowPaint.setColor(Color.BLACK);
		shadowPaint.setShadowLayer((float) shadowRadius, 1.0f, 1.0f,
				Color.BLACK);
		canvas.drawRect(rectF, shadowPaint);

		canvas.drawARGB(0, 0, 0, 0);
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, imageRect, imageRect, paint);

		return output;
	}

}