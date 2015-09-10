package com.example.lookingdynamic.lookingbusy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ListAdapter;

/**
 * Created by swu on 9/10/2015.
 */
public class MenuManager {

    private static final String LOGGER = MenuManager.class.getSimpleName();

    private static final String [] pausedMenuLabels = new String[] {"Themes", "GamePlay", "Restart Game", "Continue Current Game"};
    private static final Integer[] pausedMenuIcons = new Integer[] {R.drawable.popped_balloon, R.drawable.popped_droplet, R.drawable.popped_balloon, R.drawable.popped_droplet};

    private Context myContext;
    public MenuManager(Context myContext) {
        this.myContext = myContext;
    }
    public void showPausedMenu(final PopAllTheThingsGame game) {
        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, pausedMenuLabels, pausedMenuIcons);

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setTitle("Pause Menu");
        builder.setIcon(R.drawable.popped_ball);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);
                game.resume();
            }
        });
        builder.show();
    }
}
