package com.powerj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class StarUtils {
	// given a resource, return a bitmap
	public static Bitmap imageResourceToBitmap(Context c, int res, int maxDim) {
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		// compute the smallest size bitmap we need to read
		BitmapFactory.decodeResource(c.getResources(), res, opts);
		int w = opts.outWidth;
		int h = opts.outHeight;
		int s = 1;
		while (true) {
			if ((w / 2 < maxDim) || (h / 2 < maxDim)) {
				break;
			}
			w /= 2;
			h /= 2;
			s++;
		}
		// scale and read the data
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = s;
		bmp = BitmapFactory.decodeResource(c.getResources(), res, opts);
		return bmp;
	}

	// given a resource, return a bitmap with a specified maximum height
	public static Bitmap maxHeightResourceToBitmap(Context c, int res,
			int maxHeight) {
		Bitmap bmp = imageResourceToBitmap(c, res, maxHeight);

		int width = bmp.getWidth();
		int height = bmp.getHeight();

		int newHeight = maxHeight;
		int newWidth = maxHeight / 2;

		// calculate the scale - in this case = 0.4f
		float scaleHeight = ((float) newHeight) / height;
		float scaleWidth = ((float) newWidth) / width;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap and return it
		return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
	}
	
	// given a resource, return a bitmap with a specified maximum height
	public static Bitmap scaleWithRatio(Context c, int res,
			int max) {
		Bitmap bmp = imageResourceToBitmap(c, res, max);

		int width = bmp.getWidth();
		int height = bmp.getHeight();

		// calculate the scale - in this case = 0.4f
		float scaleHeight = ((float) max) / height;
		float scaleWidth = ((float) max) / width;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap and return it
		return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
	}
}
