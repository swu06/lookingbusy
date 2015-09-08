package com.example.lookingdynamic.lookingbusy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by swu on 9/5/2015.
 */

public class LookingBusyActivity extends Activity {
    /** Called when the activity is first created. */

    private static final String LOGGER = LookingBusyActivity.class.getSimpleName();
    private MainGamePanel game = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        game = new MainGamePanel(this);
        setContentView(game);
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
        game.pause();
        super.onStop();

    }

    @Override
    protected void onResume() {
        Log.d(LOGGER, "Resuming...");
        super.onResume();
        //if(game != null) {
            //game.unpause();
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        game.pause();
        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        game.unpause();

    }


}
