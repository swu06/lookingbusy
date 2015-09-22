package com.example.lookingdynamic.lookingbusy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.example.lookingdynamic.lookingbusy.gameplay.ThemeManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    // Called when the activity is first created.
    private static final String LOGGER = LookingBusyActivity.class.getSimpleName();
    private PopAllTheThingsGame game = null;
    private int backButtonCount = 0;
    private boolean firstRun;

    public static final int CAMERA_RESULT = 0;
    public static final int FILE_RESULT = 0;

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
        firstRun = getSharedPreferences("BOOT_PREF", MODE_PRIVATE).getBoolean("firstRun2", true);

        if (firstRun){
            Log.d(LOGGER, "This is the first run of the activity");
            getSharedPreferences("BOOT_PREF", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstRun", false)
                    .commit();
        }
    }

    /*
     * Resume the game when it is ready to be visible.
     * This will be called after onCreate or after onPause
     * Note that this needs to be a safe resume because
     * the playing surface may not yet be available.
     */
    @Override
    protected void onResume() {
        Log.d(LOGGER, "Resuming...");
        super.onResume();

        if(firstRun) {
            Log.d(LOGGER, "This is the first run of the activity");
        } else {
            Log.d(LOGGER, "This activity has run before");

        }

        // Initialize the game as needed, and set it as this activity's content
        if(game == null || game.isStopped()) {
            game = new PopAllTheThingsGame(this, firstRun);
            firstRun = false;
            setContentView(game);
        } else if(game.isPaused()){
            game.resume();
        }

    }

    /*
     * Pause the game if it is not visible for any reason
     * This is called before onStop, and nothing extra needs done in onStop.
     */
    @Override
    protected void onPause() {
        Log.d(LOGGER, "Pausing Activity");
        game.pause();
        super.onPause();
    }

    /*
     * This method forces the back button to behave properly.
     * It is needed to prevent a crash if the user opens up the
     * game immediately after hitting back
     */
    @Override
    public void onBackPressed(){
        if(backButtonCount >= 1)
        {
            game.pause();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    /*
     * This method handles the final phase of life: destruction
     * This game will "never" come back from this state, so we
     * just need to clear up and head home.
     */
    @Override
    protected void onDestroy() {
        Log.d(LOGGER, "Destroying the activity");
        super.onDestroy();
        game.stop();
        game = null;
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

    public void showIntroMenu() {
        Log.d(LOGGER, "Displaying First Run Menu");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public void showPauseMenu() {

        String[] pausedMenuLabels = new String[] {"Themes", "GamePlay", "Picture For RandomBot", "High Scores"};
        Integer[] pausedMenuIcons = new Integer[] {game.getThemeManager().getCurrentIconID(),
                game.getGameplayManager().getCurrentIconID(),
                R.drawable.ic_camera_alt_black_24dp,
                R.drawable.ic_star_black_24dp};

        ListAdapter adapter = new ArrayAdapterWithIcons(this, android.R.layout.select_dialog_item, pausedMenuLabels, pausedMenuIcons);

        AlertDialog.Builder builder = getDialog(this);
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
                    showThemeMenu();
                } else if (item == 1) {
                    showGamePlayMenu();
                } else if (item == 2) {
                    showRandomBotMenu();
                } else if (item == 3) {
                    showHighScoreMenu();
                }
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);

            }
        });
        builder.show();


    }

    public void showGamePlayMenu() {
        final GameplayManager modes = game.getGameplayManager();
        String[] gameplayMenuLabels = modes.getLabels();
        Integer[] gameplayMenuIcons = modes.getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(this, android.R.layout.select_dialog_singlechoice, gameplayMenuLabels, gameplayMenuIcons);

        AlertDialog.Builder builder = getDialog(this);
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

    public void showThemeMenu() {
        final ThemeManager themes = game.getThemeManager();
        String[] themeMenuLabels = themes.getLabels();
        Integer[] themeMenuIcons = themes.getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(this, android.R.layout.select_dialog_singlechoice, themeMenuLabels, themeMenuIcons);

        AlertDialog.Builder builder = getDialog(this);
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

    public void showHighScoreMenu() {
        String[] scoreLabels = game.getGameplayManager().getHighScores();
        Integer[] gameplayMenuIcons = game.getGameplayManager().getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(this, android.R.layout.select_dialog_item, scoreLabels, gameplayMenuIcons);

        AlertDialog.Builder builder = getDialog(this);
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

    public void showRandomBotMenu() {

        String[] pictureOptions = new String[] {"Take a Picture", "Choose Picture from Gallery"};
        Integer[] pictureIcons = new Integer[] {R.drawable.ic_camera_alt_black_24dp, R.drawable.ic_image_black_24dp};

        ListAdapter adapter = new ArrayAdapterWithIcons(this, android.R.layout.select_dialog_item, pictureOptions, pictureIcons);

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Picture for RandomBot");
        builder.setNegativeButton("Clear Image", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "RandomBogImage will be cleared");
                game.setRandomBotImage(null, BitmapFactory.decodeResource(getResources(), R.drawable.bright_balloon1));
            }
        });
        builder.setNeutralButton("Close", null);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_RESULT);
                    dialog.dismiss();
                } else if (item == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_RESULT);
                    dialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_RESULT) {
                storeBitmap((Bitmap) data.getExtras().get("data"));
            } else if (requestCode == FILE_RESULT) {
                Uri selectedImageUri = data.getData();
                storeBitmap(BitmapFactory.decodeFile(selectedImageUri.getPath()));
            }

        }
    }

    public void storeBitmap(Bitmap existingFile) {
        Bitmap scaledFile = Bitmap.createScaledBitmap(existingFile, 100, 100, false);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(destination);
            scaledFile.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {}
        game.setRandomBotImage(destination.getPath(), scaledFile);
    }

}
