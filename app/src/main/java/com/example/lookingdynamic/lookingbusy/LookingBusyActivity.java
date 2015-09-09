package com.example.lookingdynamic.lookingbusy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

/**
 * Welcome to The Entry Point for this game! The LookingBusyActivity is the main
 * class for this little Android project. It handles all the big calls from the
 * operating system and passes on the needed information to the real Controller:
 * MainGamePanel.  The PopAllTheThingsGame then controls the view/panel/surface
 * and everything to do with the actual game being played, which can be
 * described as "Pop all the things!"
 *
 * Created by swu on 9/5/2015.
 */

public class LookingBusyActivity extends Activity {
    /** Called when the activity is first created. */

    private static final String LOGGER = LookingBusyActivity.class.getSimpleName();
    private PopAllTheThingsGame game = null;

    /*
     * This method is for a brand new activity
     * It has no history, no memory
     * Nothing to worry about but starting fresh
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOGGER, "Creating a new activity");
        super.onCreate(savedInstanceState);
        // Removing the title to save previous screen space
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Initialize the game, and set it as this activity's content
        game = new PopAllTheThingsGame(this);
        setContentView(game);
    }

    /*
     * This method handles the final phase of life: destruction
     * This game will never come back from this state, so we
     * just need to clear up and head home.
     */
    @Override
    protected void onDestroy() {
        Log.d(LOGGER, "Destroying the activity");
        super.onDestroy();
        game = null;
    }

    /*
     * Pause the game if it is not visible for any reason
     */
    @Override
    protected void onPause() {
        Log.d(LOGGER, "Pausing Activity");
        game.pause();
        super.onPause();
    }

    /*
     * Resume the game when it is ready to be visible again
     * Note that this needs to be a safe resume,  because
     * the playing surface may note yet be available.
     */
    @Override
    protected void onResume() {
        Log.d(LOGGER, "Resuming...");
        super.onResume();
        game.resume();
    }

    /*
     * This method handles a user pushing the "menu" button
     * available on older and awesome devices.  This game
     * has a unified pause/pause-menu behavior, so we
     * are going to delegate the work to the game for this.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        game.pause();
        return true;
    }

    /*
     * There are some cases when our pause/pause-menu can
     * be secretly closed.  This reveals that secret to
     * the game as an "unpause" or resume event.
     */
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        game.resume();
    }


}
