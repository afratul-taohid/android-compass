package com.louisnard.compass;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Main activity showing device orientation data with a {@link CompassView} for the azimuth and simple text views for the pitch and roll.
 *
 * @author Alexandre Louisnard
 */

public class MainActivity extends AppCompatActivity implements Compass.CompassListener {

    // Tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Constants
    // The minimum difference in degrees with the last orientation value for the CompassListener to be notified
    private static final float MIN_AZIMUTH_DIFFERENCE_BETWEEN_COMPASS_UPDATES = 1;
    private static final float MIN_PITCH_DIFFERENCE_BETWEEN_COMPASS_UPDATES = 2;
    private static final float MIN_ROLL_DIFFERENCE_BETWEEN_COMPASS_UPDATES = 3;

    // Compass
    private Compass mCompass;

    // Views
    private CompassView mCompassView;
    private TextView mPitchTextView;
    private TextView mRollTextView;
    private TextView mScreenRotationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mCompassView = (CompassView) findViewById(R.id.compass_view);
        mPitchTextView = (TextView) findViewById(R.id.pitch_text_view);
        mRollTextView = (TextView) findViewById(R.id.roll_text_view);
        mScreenRotationTextView = (TextView) findViewById(R.id.screen_rotation_text_view);

        // Compass
        mCompass = Compass.newInstance(this, this);
        if (mCompass == null) {
            if (BuildConfig.DEBUG) Log.d(TAG, "The device does not have the required sensors to use a compass.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCompass != null) mCompass.start(MIN_AZIMUTH_DIFFERENCE_BETWEEN_COMPASS_UPDATES, MIN_PITCH_DIFFERENCE_BETWEEN_COMPASS_UPDATES, MIN_ROLL_DIFFERENCE_BETWEEN_COMPASS_UPDATES);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCompass != null) mCompass.stop();
    }

    @Override
    public void onOrientationChanged(float azimuth, float pitch, float roll) {
        mCompassView.updateAzimuth(azimuth);
        mPitchTextView.setText(String.format(getString(R.string.pitch_value), pitch));
        mRollTextView.setText(String.format(getString(R.string.roll_value), roll));

        final int screenRotation = (((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()).getRotation();
        if (screenRotation == Surface.ROTATION_90) {
            mScreenRotationTextView.setText(String.format(getString(R.string.screen_rotation_value), 90));
        } else if (screenRotation == Surface.ROTATION_180) {
            mScreenRotationTextView.setText(String.format(getString(R.string.screen_rotation_value), 180));
        } else if (screenRotation == Surface.ROTATION_270) {
            mScreenRotationTextView.setText(String.format(getString(R.string.screen_rotation_value), 270));
        } else {
            mScreenRotationTextView.setText(String.format(getString(R.string.screen_rotation_value), 0));
        }
    }
}
