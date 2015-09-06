package com.example.lookingdynamic.lookingbusy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by swu on 9/5/2015.
 */

public class LookingBusyActivity extends Activity {
    /** Called when the activity is first created. */

    private static final String LOGGER = LookingBusyActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        setContentView(new MainGamePanel(this));
        Log.d(LOGGER, "View added");
    }

    @Override
    protected void onDestroy() {
        Log.d(LOGGER, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(LOGGER, "Stopping...");
        super.onStop();
    }
}
