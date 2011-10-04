package com.powerj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class StarsEngine extends WallpaperService {
	private final Handler mHandler = new Handler();


	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new StarEngine();
	}

	class StarEngine extends Engine {
		private final Paint mPaint = new Paint();
		private Stars mStars;

		private final Runnable mDrawStars = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		
		private boolean mVisible;

		StarEngine() {
			final Paint paint = mPaint;
			paint.setAntiAlias(true);
			
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawStars);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDrawStars);
			}
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			int smaller = width < height ? width : height;
			int maxDim = smaller / 7;
			Context c = getApplicationContext();
			
			mStars = new Stars(c, maxDim, mPaint);
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawStars);
		}

		/**
		 * Draw a single animation frame.
		 */
		void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					c.drawColor(Color.BLACK);
					mStars.draw(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			// Reschedule the next redraw
			mHandler.removeCallbacks(mDrawStars);
			if (mVisible) {
				mHandler.postDelayed(mDrawStars, 1000 / 29);
			}
		}
	}
}