package com.example.lookingdynamic.lookingbusy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.widget.ListAdapter;

import com.example.lookingdynamic.lookingbusy.gameplay.GameStatistics;
import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;
import com.example.lookingdynamic.lookingbusy.gameplay.GameplayMode;

/**
 * Created by swu on 9/10/2015.
 */
public class MenuManager {

    private static final String LOGGER = MenuManager.class.getSimpleName();

    private static final String [] pausedMenuLabels = new String[] {"Themes", "GamePlay"};
    private static final Integer[] pausedMenuIcons = new Integer[] {R.drawable.crayon_icon, R.drawable.ic_insert_emoticon_black_24dp};

    private static final String [] gamePlayMenuLabels = new String[] {"Relaxing", "Challanging"};
    private static final Integer[] gamePlayMenuIcons = new Integer[] {R.drawable.ic_insert_emoticon_black_24dp, R.drawable.ic_verified_user_black_24dp};


    private Context myContext;
    public MenuManager(Context myContext) {
        this.myContext = myContext;
    }

    public void showPauseMenu(final PopAllTheThingsGame game) {
        pausedMenuIcons[0] = game.getCurrentThemeIcon();
        pausedMenuIcons[1] = game.getCurrentGameplayModeIcon();
        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_item, pausedMenuLabels, pausedMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Pause Menu");
        builder.setPositiveButton("Start New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Restart Button Clicked: Starting a new game");
                game.resume();
            }
        });
        builder.setNegativeButton("Continue Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Continue Button Clicked: Continuing with current game");
                game.resume();
            }
        });
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    showThemeMenu(game);
                } else if (item == 1) {
                    showGamePlayMenu(game);
                }
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);

            }
        });
        builder.show();
    }

    public void showGamePlayMenu(final PopAllTheThingsGame game) {
        GameplayMode[] modes =  game.getGameplayModes();
        String [] gameplayMenuLabels = new String[modes.length];
        Integer[] gameplayMenuIcons = new Integer[modes.length];

        for(int i=0; i < modes.length; i++){
            gameplayMenuLabels[i] = modes[i].getName();
            gameplayMenuIcons[i] = modes[i].getIconImageId();
        }
        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_singlechoice, gamePlayMenuLabels, gamePlayMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Game Play Options");
        builder.setPositiveButton("OK", null);
        builder.setSingleChoiceItems(adapter, game.getCurrentGamePlayModeID(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "GamePlay Selected:" + item);
                game.setGameplayMode(item);
            }
        });
        builder.show();

    }

    public void showThemeMenu(final PopAllTheThingsGame game) {
        GameTheme[] themes =  game.getThemes();
        String [] themeMenuLabels = new String[themes.length];
        Integer[] themeMenuIcons = new Integer[themes.length];

        for(int i=0; i < themes.length; i++){
            themeMenuLabels[i] = themes[i].getName();
            themeMenuIcons[i] = themes[i].getIconImageId();
        }

        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_singlechoice, themeMenuLabels, themeMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Themes Options");
        builder.setPositiveButton("OK", null);
        builder.setSingleChoiceItems(adapter, game.getCurrentThemeID(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Theme Selected: " + item);
                game.setTheme(item);
            }
        });

                builder.show();
    }

    public AlertDialog.Builder getDialog(Context myContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        if(Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 23) {
            builder = new AlertDialog.Builder(myContext, android.R.style.Theme_Holo_Light_Dialog);
        }  else if(Build.VERSION.SDK_INT >= 23){
            builder = new AlertDialog.Builder(myContext, android.R.style.Theme_Material_Light_Dialog_Alert);
        }

        return builder;
    }
}
