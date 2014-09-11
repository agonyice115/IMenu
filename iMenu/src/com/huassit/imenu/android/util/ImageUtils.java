package com.huassit.imenu.android.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.media.ExifInterface;

/**
 * Created by Sylar on 14-4-24.
 */
public class ImageUtils {

	// 计算图片的缩放值
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getScaleBitmap(byte[] data, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;

		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getScaleBitmap(String file, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory
					.decodeStream(new FileInputStream(file), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file),
					null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 　　* 进行添加水印图片和文字 　　* 　　* @param src 　　* @param waterMak 　　* @return 　　
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap waterMak,
			String[] title) {
		// if (src =http://blog.csdn.net/developer_jiangqq/article/details/=
		// null)
		// {
		// return src;
		// }
		// 获取原始图片与水印图片的宽与高
		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas mCanvas = new Canvas(newBitmap);
		// 往位图中开始画入src原始图片
		mCanvas.drawBitmap(src, 0, 0, null);
		// // 在src的右下角添加水印
		// Paint paint = new Paint();
		// //paint.setAlpha(100);
		// mCanvas.drawBitmap(waterMak, w - ww - 5, h - wh - 5, paint);
		// 开始加入文字
		if (null != title) {
			Paint textPaint = new Paint();
			textPaint.setColor(Color.argb(0xff, 255, 97, 0x00));
			textPaint.setTextSize(40);
			String familyName = "黑体";
			Typeface typeface = Typeface.create(familyName, Typeface.BOLD);
			textPaint.setTypeface(typeface);
			textPaint.setTextAlign(Align.CENTER);
			if (!StringUtils.isBlank(title[0])) {
				mCanvas.drawText(title[0], w - 210, 50, textPaint);
			}
			if (!StringUtils.isBlank(title[1])) {
				mCanvas.drawText(title[1], w - 200, 100, textPaint);
				mCanvas.drawText(title[2], w - 200, 150, textPaint);
			}

			mCanvas.drawText(title[3], w - 200, 200, textPaint);
		}
		mCanvas.save(Canvas.ALL_SAVE_FLAG);
		mCanvas.restore();
		return newBitmap;
	}

	public static Bitmap getbitmap(String imageUri) {
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			// Log.i(FinalList.DEBUG, "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

}
