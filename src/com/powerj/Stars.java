package com.powerj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class Stars extends WallpaperService {

	private static final String SHARED_PREFS_NAME = "Stars In Space Preferences";
	private final Handler mHandler = new Handler();
	private static int maxWidth;

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences prefs = Stars.this.getSharedPreferences(
				SHARED_PREFS_NAME, 0);
		// Boolean b = prefs.getBoolean("Example Setting", false); 2nd
		// arg->default value if attribute doesn't exist

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
		// private long mStartTime;

		private boolean hasSetup = false;
		private Starfield small;
		private Starfield medium;
		private Starfield large;

		private final Runnable mDrawStars = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		private boolean mVisible;

		StarEngine() {
			final Paint paint = mPaint;
			paint.setAntiAlias(false);

			// setup star fields
			ArrayList<Bitmap> smallStars = new ArrayList<Bitmap>();
			ArrayList<Bitmap> mediumStars = new ArrayList<Bitmap>();
			ArrayList<Bitmap> largeStars = new ArrayList<Bitmap>();
			// small fields
			smallStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.small_stars));
			smallStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.small_stars2));
			smallStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.small_stars3));
			smallStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.small_stars4));
			// medium fields
			mediumStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.medium_stars));
			mediumStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.medium_stars2));
			mediumStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.medium_stars3));
			mediumStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.medium_stars4));
			// large fields
			largeStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.large_stars));
			largeStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.large_stars2));
			largeStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.large_stars3));
			largeStars.add(BitmapFactory.decodeResource(getResources(),
					R.drawable.large_stars4));

			small = new Starfield(smallStars, Starfield.STARFIELD_SMALL);
			medium = new Starfield(mediumStars, Starfield.STARFIELD_MEDIUM);
			large = new Starfield(largeStars, Starfield.STARFIELD_LARGE);
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
					if (!hasSetup) {
						maxWidth = c.getWidth();
						hasSetup = true;
					}
					// draw something
					c.drawColor(Color.BLACK);
					small.draw(c);
					medium.draw(c);
					large.draw(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			// Reschedule the next redraw
			mHandler.removeCallbacks(mDrawStars);
			if (mVisible) {
				// approx 90 fps
				mHandler.postDelayed(mDrawStars, 1000 / 90);
			}
		}

		class Starfield {
			Bitmap currentStars;
			Bitmap nextStars;

			float x = 0f;
			final float y = 0f;
			final int type;
			final int numStarsImages;

			private static final int STARFIELD_SMALL = 0;
			private static final int STARFIELD_MEDIUM = 1;
			private static final int STARFIELD_LARGE = 2;
			final ArrayList<Bitmap> stars;

			Starfield(final ArrayList<Bitmap> stars, final int type) {
				// initialize Starfield
				this.stars = stars;
				this.type = type;
				numStarsImages = stars.size();
				currentStars = stars.get(new Random().nextInt(numStarsImages));
				nextStars = stars.get(new Random().nextInt(numStarsImages));
				x = maxWidth;
			}

			public void draw(Canvas c) {
				// default large: 20, medium: 10, small: 5
				if (type == STARFIELD_LARGE)
					x -= 20f;
				if (type == STARFIELD_MEDIUM)
					x -= 15f;
				else
					x -= 10f;

				if (x < (-maxWidth)) {
					x = 0;

					// randomly choose new set of stars
					currentStars = nextStars;
					nextStars = stars.get(new Random().nextInt(numStarsImages));
				}

				c.drawBitmap(currentStars, x, y, mPaint);
				c.drawBitmap(nextStars, maxWidth + x, y, mPaint);

			}
		}
	}
}