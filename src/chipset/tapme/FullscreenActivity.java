package chipset.tapme;

import java.util.Random;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import chipset.tapme.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
@SuppressLint("CutPasteId")
public class FullscreenActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		final View controlsView = findViewById(R.id.dont_tap_me);
		final View contentView = findViewById(R.id.dont_tap_me);
		final FrameLayout act = (FrameLayout) findViewById(R.id.rL);
		final VideoView videoView = (VideoView) findViewById(R.id.video);
		final TextView showText = (TextView) findViewById(R.id.showText);
		final Button tapMe = (Button) findViewById(R.id.tap_me);
/*
		Typeface robotoLight = Typeface.createFromAsset(getApplicationContext()
				.getAssets(), "fonts/light.ttf");
		Typeface robotoThin = Typeface.createFromAsset(getApplicationContext()
				.getAssets(), "fonts/thin.ttf");
		showText.setTypeface(robotoThin);
		tapMe.setTypeface(robotoLight);
*/
		tapMe.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Random r = new Random();
				int i = r.nextInt(6);

				switch (i) {
				case 0: {

					act.setBackgroundColor(getResources().getColor(R.color.bg));
					tapMe.setBackgroundColor(getResources()
							.getColor(R.color.bg));

					showText.setText("");

					Uri uri = Uri.parse("android.resource://"
							+ getPackageName() + "/" + R.raw.video);

					videoView.setVideoURI(uri);
					videoView.start();

					videoView.setVisibility(View.VISIBLE);
					break;
				}
				case 1: {
					act.setBackgroundColor(getResources().getColor(R.color.bg));
					tapMe.setBackgroundColor(getResources()
							.getColor(R.color.bg));

					videoView.setVisibility(View.GONE);

					showText.setText("TeeHee!");

					MediaPlayer mediaPlayer = MediaPlayer.create(
							getApplicationContext(), R.raw.teehee);
					mediaPlayer.start(); // mediaPlayer.release();
					mediaPlayer = null;
					break;
				}
				case 2: {
					act.setBackgroundColor(getResources().getColor(R.color.bg));
					tapMe.setBackgroundColor(getResources()
							.getColor(R.color.bg));

					videoView.setVisibility(View.GONE);
					showText.setText("");

					Toast toast = Toast.makeText(getApplicationContext(),
							"HOW CUTE! :D", Toast.LENGTH_SHORT);
					toast.show();
					MediaPlayer mediaPlayer = MediaPlayer.create(
							getApplicationContext(), R.raw.sound);
					mediaPlayer.start(); // mediaPlayer.release();
					mediaPlayer = null;
					break;

				}
				case 3: {
					showText.setText("");
					act.setBackgroundColor(Color.BLACK);
					tapMe.setBackgroundColor(Color.BLACK);

					videoView.setVisibility(View.GONE);
					break;
				}
				case 4: {
					act.setBackgroundColor(getResources().getColor(R.color.bg));
					tapMe.setBackgroundColor(getResources()
							.getColor(R.color.bg));

					videoView.setVisibility(View.GONE);
					showText.setText("");

					Toast toast = Toast.makeText(getApplicationContext(),
							"THIS SHALL MOVE", Toast.LENGTH_SHORT);
					toast.show();

					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

					vib.vibrate(1000);
					break;
				}
				default: {
					act.setBackgroundColor(getResources().getColor(R.color.bg));
					tapMe.setBackgroundColor(getResources()
							.getColor(R.color.bg));

					videoView.setVisibility(View.GONE);
					showText.setText("I have to put something here :P");
					break;
				}
				}
			}
		});

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.show();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dont_tap_me).setOnTouchListener(
				mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}