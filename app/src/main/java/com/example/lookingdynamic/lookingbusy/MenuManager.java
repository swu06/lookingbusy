package com.example.lookingdynamic.lookingbusy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.widget.ListAdapter;

/**
 * Created by swu on 9/10/2015.
 */
public class MenuManager {

    private static final String LOGGER = MenuManager.class.getSimpleName();

    private static final String [] pausedMenuLabels = new String[] {"Themes", "GamePlay", };
    private static final Integer[] pausedMenuIcons = new Integer[] {R.drawable.icon, R.drawable.ic_insert_emoticon_black_24dp};

    private static final String [] gamePlayMenuLabels = new String[] {"Relaxing", "Challanging"};
    private static final Integer[] gamePlayMenuIcons = new Integer[] {R.drawable.ic_insert_emoticon_black_24dp, R.drawable.ic_verified_user_black_24dp};

    private static final String [] themeMenuLabels = new String[] {"Crayon", "Other Thing"};
    private static final Integer[] themeMenuIcons = new Integer[] {R.drawable.icon, R.drawable.icon2};


    private Context myContext;
    public MenuManager(Context myContext) {
        this.myContext = myContext;
    }

    public void showPausedMenu(final PopAllTheThingsGame game) {
        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_item, pausedMenuLabels, pausedMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Pause Menu");
        builder.setIcon(R.drawable.ic_pause_black_24dp);
        builder.setPositiveButton("Start New Game", null);
        builder.setNegativeButton("Continue Game", null);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    showThemeMenu(game);
                } else if (item == 1) {
                    showGamePlayMenu(game);
                } else if (item == 2) {
                    dialog.dismiss();
                    game.resume();
                } else if (item == 3) {
                    dialog.dismiss();
                    game.resume();
                }
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);

            }
        });
        builder.show();
    }

    public void showGamePlayMenu(final PopAllTheThingsGame game) {
        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_singlechoice, gamePlayMenuLabels, gamePlayMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Game Play Options");
        builder.setIcon(R.drawable.ic_toys_black_24dp);
        builder.setPositiveButton("OK", null);
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);
                if(item == 2){
                    dialog.dismiss();
                    showPausedMenu(game);
                }
            }
        });
        builder.show();
    }

    public void showThemeMenu(final PopAllTheThingsGame game) {
        ListAdapter adapter = new ArrayAdapterWithIcons(myContext, android.R.layout.select_dialog_singlechoice, themeMenuLabels, themeMenuIcons);

        AlertDialog.Builder builder = getDialog(myContext);
        builder.setTitle("Themes Options");
        builder.setIcon(R.drawable.ic_insert_emoticon_black_24dp);
        builder.setPositiveButton("OK",null);
        builder.setSingleChoiceItems(adapter, 1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);
                if(item == 2){
                    dialog.dismiss();
                    showPausedMenu(game);
                }
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
