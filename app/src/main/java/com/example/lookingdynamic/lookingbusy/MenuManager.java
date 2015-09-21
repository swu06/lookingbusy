package com.example.lookingdynamic.lookingbusy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;
import com.example.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.example.lookingdynamic.lookingbusy.gameplay.GameplayMode;
import com.example.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 * Created by swu on 9/10/2015.
 */
public class MenuManager {

    private static final String LOGGER = MenuManager.class.getSimpleName();

    private Context myContext;
    public MenuManager(Context myContext) {
        this.myContext = myContext;
    }

    public void showIntroMenu(final PopAllTheThingsGame game) {
        Log.d(LOGGER, "Displaying First Run Menu");

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setTitle("Welcome!");
        builder.setMessage("Thank you for downloading this game.\n" +
                "The goal is simple: Pop everything you see.\n" +
                "Double-Tap the pause button in the top left " +
                "corner to see more options.");
        builder.setPositiveButton("Let's Play!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Starting New Game");
                game.resumeFirstRun();
            }
        });
        builder.show();
    }

    public void showPauseMenu(final PopAllTheThingsGame game) {

        String[] pausedMenuLabels = new String[] {"Themes", "GamePlay", "Picture For RandomBot", "High Scores"};
        Integer[] pausedMenuIcons = new Integer[] {game.getThemeManager().getCurrentIconID(),
                                                    game.getGameplayManager().getCurrentIconID(),
                                                    R.drawable.ic_camera_alt_black_24dp,
                                                    R.drawable.ic_star_black_24dp};

        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_item, pausedMenuLabels, pausedMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Pause Menu");
        builder.setPositiveButton("Start New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Restart Button Clicked: Starting a new game");
                game.startNewGame();
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
                } else if (item == 2) {
                    showRandomBotMenu(game);
                } else if (item == 3) {
                    showHighScoreMenu(game);
                }
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);

            }
        });
        builder.show();


    }

    public void showGamePlayMenu(final PopAllTheThingsGame game) {
        final GameplayManager modes = game.getGameplayManager();
        String[] gameplayMenuLabels = modes.getLabels();
        Integer[] gameplayMenuIcons = modes.getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_singlechoice, gameplayMenuLabels, gameplayMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Game Play Options");
        builder.setPositiveButton("OK", null);
        builder.setSingleChoiceItems(adapter, modes.getCurrentID(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "GamePlay Selected:" + item);
                modes.setGameplayMode(item);
            }
        });
        builder.show();

    }

    public void showThemeMenu(final PopAllTheThingsGame game) {
        final ThemeManager themes = game.getThemeManager();
        String[] themeMenuLabels = themes.getLabels();
        Integer[] themeMenuIcons = themes.getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_singlechoice, themeMenuLabels, themeMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Themes Options");
        builder.setPositiveButton("OK", null);
        builder.setSingleChoiceItems(adapter, themes.getCurrentID(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Theme Selected: " + item);
                themes.setTheme(item);
            }
        });
        builder.show();

    }

    public void showHighScoreMenu(final PopAllTheThingsGame game) {
        String[] scoreLabels = game.getGameplayManager().getHighScores();
        Integer[] gameplayMenuIcons = game.getGameplayManager().getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_item, scoreLabels, gameplayMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("High Scores");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Clear High Scores", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "High Scores will be cleared");
                game.getGameplayManager().clearAllScores();
            }
        });
        builder.setAdapter(adapter, null);
        builder.show();

    }

    public void showRandomBotMenu(final PopAllTheThingsGame game) {

        String[] pictureOptions = new String[] {"Take a Picture", "Choose Picture from Gallery"};
        Integer[] pictureIcons = new Integer[] {R.drawable.ic_camera_alt_black_24dp, R.drawable.ic_image_black_24dp};

        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_singlechoice, pictureOptions, pictureIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Picture for RandomBot");
        builder.setPositiveButton("Close", null);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    //showThemeMenu(game);
                } else if (item == 1) {
                    //showGamePlayMenu(game);
                }
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);

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