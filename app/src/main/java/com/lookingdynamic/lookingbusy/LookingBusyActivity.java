package com.lookingdynamic.lookingbusy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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

import com.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

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
    public static final int FILE_RESULT = 1;

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
        firstRun = getSharedPreferences("BOOT_PREF", MODE_PRIVATE).getBoolean("firstRun", true);

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
     * This message assumes that the Pause Menu was displayed
     * during the onPause process, so the user will use that to
     * resume the game.
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

        if(game.isPaused()) {
            game.resume();
            backButtonCount = 0;
        } else if(backButtonCount >= 1) {
            game.pause();
            game.getGameplayManager().storeHighScore();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
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
        //game = null;
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
                "Swipe the pause button in the top left " +
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

    public void showGameOverMenu() {
        Log.d(LOGGER, "Displaying Game Over Menu");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over!");
        builder.setMessage("Better luck next time.  Click below to start a new game!");
        builder.setPositiveButton("Let's Play Again!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Starting New Game");
                game.startNewGame();
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
                game.clearObjects();
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
                game.setRandomBotImage(null, null);
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
                Log.d(LOGGER, "Camera has returned a file.");
                storeBitmap((Bitmap) data.getExtras().get("data"));
            } else if (requestCode == FILE_RESULT) {
                Log.d(LOGGER, "Gallery has returned a file.");
                try {
                    Bitmap selectedImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    storeBitmap(getCorrectBitmap(selectedImage, data.getData()));
                } catch(Exception e){
                    Log.e(LOGGER, "Cannot load file");
                }
            }
        }
    }

    public Bitmap getCorrectBitmap(Bitmap bitmap, Uri imageUri) {
        ExifInterface ei;
        Bitmap correctBitmap = bitmap;

        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = getContentResolver().query(imageUri, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        correctBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

//        try {
//            ei = new ExifInterface(filePath);
//
//            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL);
//
//            int rotationInDegrees = exifToDegrees(orientation);
//
//            Matrix matrix = new Matrix();
//            if (rotationInDegrees != 0f) {
//                Log.d(LOGGER, "Image is stored rotated.");
//                matrix.preRotate(rotationInDegrees);
//            }
//
//            correctBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return correctBitmap;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    public void storeBitmap(Bitmap existingFile) {
        Log.d(LOGGER, "Scaling file ...");

        final int maxSize = 200;
        int outWidth;
        int outHeight;
        int inWidth = existingFile.getWidth();
        int inHeight = existingFile.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        Bitmap scaledFile = Bitmap.createScaledBitmap(existingFile, outWidth, outHeight, false);

        File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                "LookingBusy"+ System.currentTimeMillis() + ".png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(destination);
            Log.d(LOGGER, "Storing file ...");
            scaledFile.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Log.e(LOGGER, "Exception was thrown:" + e.getMessage());
        }
        Log.d(LOGGER, "RandomBot File ready to use.");

        game.setRandomBotImage(destination.getPath(), scaledFile);
    }


}
