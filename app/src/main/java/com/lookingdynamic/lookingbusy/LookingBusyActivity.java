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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;
import com.lookingdynamic.lookingbusy.purchasing.AppProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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

public class LookingBusyActivity extends Activity
                                implements IabHelper.OnIabSetupFinishedListener,
                                            IabHelper.OnIabPurchaseFinishedListener,
                                            IabHelper.QueryInventoryFinishedListener {
    // Called when the activity is first created.
    private static final String LOGGER = LookingBusyActivity.class.getSimpleName();
    private PopAllTheThingsGame game = null;

    private static final String FIRST_RUN_KEY = "firstRun";
    private boolean firstRun;

    private IabHelper billingHelper;
    private boolean billingHelperReady;
    private static final String RANDOM_BOT_SKU = "OBSCURED_FOR_PUBLIC_REPO";
    private static final String RANDOM_BOT_KEY = "OBSCURED_FOR_PUBLIC_REPO";
    // DEBUG
    // private static final String TEST_SKU = "com.example.product";
    private boolean randomBotPurchased;

    public static final int CAMERA_RESULT = 0;
    public static final int FILE_RESULT = 1;
    public static final int BILLING_RESULT = 2;

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
        firstRun = getSharedPreferences("BOOT_PREF", MODE_PRIVATE).getBoolean(FIRST_RUN_KEY, true);
        randomBotPurchased = getSharedPreferences("BOOT_PREF", MODE_PRIVATE)
                                .getBoolean(RANDOM_BOT_KEY, false);

        if (firstRun) {
            Log.d(LOGGER, "This is the first run of the activity");
            getSharedPreferences("BOOT_PREF", MODE_PRIVATE)
                    .edit().putBoolean(FIRST_RUN_KEY, false).commit();
        }

        billingHelperReady = false;
        setupInAppBilling();
    }

    private void setupInAppBilling() {
        billingHelper = new IabHelper(this, AppProperties.BASE_64_KEY);
        billingHelper.startSetup(this);
    }

    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            Log.d(LOGGER, "In-app Billing set up: " + result);
            billingHelperReady = true;
            checkForPurchases();

        } else {
            Log.e(LOGGER, "Problem setting up In-app Billing: " + result);
        }
    }

    private void checkForPurchases() {
        if(billingHelperReady) {
            List<String> skus = new ArrayList<>();
            skus.add(RANDOM_BOT_SKU);
            billingHelper.queryInventoryAsync(false, skus, this);
        }
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        if (result.isSuccess()) {
            Log.d(LOGGER, "In app purchases query complete" + result);

            boolean randomBotPurchaseCheck = inv.hasPurchase(RANDOM_BOT_SKU);

            if (randomBotPurchaseCheck && !randomBotPurchased) {
                Log.d(LOGGER, "RandomBot Purchase Imported");
                purchaseRandomBot();
            } else if (!randomBotPurchaseCheck && randomBotPurchased) {
                Log.d(LOGGER, "RandomBot Refund Imported");
                refundRandomBot();
            }

            final ThemeManager themes = game.getThemeManager();
            themes.importPurchases(inv);
        } else {
            Log.e(LOGGER, "In app purchases could not be verified.");

        }
    }

    public void purchaseRandomBot() {
        randomBotPurchased = true;
        getSharedPreferences("BOOT_PREF", MODE_PRIVATE).edit().putBoolean(RANDOM_BOT_KEY, true).commit();
        if (game != null) {
            game.purchaseRandomBot();
        }
    }

    public void refundRandomBot() {
        randomBotPurchased = false;
        getSharedPreferences("BOOT_PREF", MODE_PRIVATE).edit().putBoolean(RANDOM_BOT_KEY, false).commit();
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        ThemeManager themes = game.getThemeManager();
        if (result.isFailure()) {
            Log.e(LOGGER, "Purchase failed: " + result);
            Toast.makeText(this, "In-App Purchase Failed or was cancelled.", Toast.LENGTH_LONG)
                    .show();
        } else if (RANDOM_BOT_SKU.equals(info.getSku())) {
            Log.d(LOGGER, "Purchase of RandomBot succeeded.");
            purchaseRandomBot();
        } else if (themes.purchaseTheme(info.getSku())){
            Log.d(LOGGER, "Theme Purchased");
        }
    }

    protected void purchaseItem(String sku) {
        if(billingHelperReady) {
            billingHelper.launchPurchaseFlow(this, sku, BILLING_RESULT, this);
        } else {
            Toast.makeText(this, "In-app Purchasing is currently unavailable, please reconnect and try again",
                    Toast.LENGTH_LONG).show();
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

        if (firstRun) {
            Log.d(LOGGER, "This is the first run of the activity");
        } else {
            Log.d(LOGGER, "This activity has run before");

        }

        // Initialize the game as needed, and set it as this activity's content
        if (game == null || game.isStopped()) {
            game = new PopAllTheThingsGame(this, firstRun, randomBotPurchased);
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

        if (!game.isPaused()) {
            game.pause();
        } else {
            game.resume();
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
        if (billingHelper != null) {
            billingHelper.dispose();
        }
        billingHelper = null;
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
     * the game as an "un-pause" or resume event.
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

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Game Over!");
        builder.setMessage("Better luck next time.  Click below to start a new game!");
        builder.setPositiveButton("Let's Play Again!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Starting New Game");
                game.startNewGame(true);
            }
        });
        builder.show();
    }

    public void showPauseMenu() {

        String[] pausedMenuLabels = new String[7];
        Integer[] pausedMenuIcons = new Integer[7];

        pausedMenuLabels[0] = "GamePlay";
        pausedMenuIcons[0] = game.getGameplayManager().getCurrentIconID();

        pausedMenuLabels[1] = "Themes";
        pausedMenuIcons[1] = game.getThemeManager().getCurrentThemeIconID();

        if(randomBotPurchased) {
            pausedMenuLabels[2] = "Picture For RandomBot";
        } else {
            pausedMenuLabels[2] = "Purchase RandomBot";
        }
        pausedMenuIcons[2] = R.drawable.ic_action_action_android;

        if(game.isMute()) {
            pausedMenuLabels[3] = "Sound: Off";
            pausedMenuIcons[3] = R.drawable.ic_action_av_volume_off;
        } else {
            pausedMenuLabels[3] = "Sound: On";
            pausedMenuIcons[3] = R.drawable.ic_action_av_volume_up;
        }

        if(game.isBlackBackground()) {
            pausedMenuLabels[4] = "Background: Black";
            pausedMenuIcons[4] = R.drawable.ic_action_action_favorite;
        } else {
            pausedMenuLabels[4] = "Background: Clear";
            pausedMenuIcons[4] = R.drawable.ic_action_action_favorite_outline;
        }

        pausedMenuLabels[5] = "High Scores";
        pausedMenuIcons[5] = R.drawable.ic_action_action_grade;

        pausedMenuLabels[6] = "Credits";
        pausedMenuIcons[6] = R.drawable.ic_action_image_palette;

        ListAdapter adapter = new ArrayAdapterWithIcons(this,
                android.R.layout.select_dialog_item,
                pausedMenuLabels,
                pausedMenuIcons);

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Pause Menu");
        builder.setPositiveButton("Start New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Restart Button Clicked: Starting a new game");
                game.startNewGame(true);
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
                    showGameplayMenu();
                } else if (item == 1) {
                    showThemeMenu();
                } else if (item == 2) {
                    if (randomBotPurchased) {
                        showRandomBotMenu();
                    } else {
                        showRandomBotPurchaseMenu();
                    }
                } else if (item == 3) {
                    game.swapMute();
                    dialog.dismiss();
                    game.resume();
                } else if (item == 4) {
                    game.swapBlackBackground();
                    dialog.dismiss();
                    game.resume();
                } else if (item == 5) {
                    showHighScoreMenu();
                } else if (item == 6) {
                    showCreditScreen();
                    dialog.dismiss();
                }
                Log.d(LOGGER, "MenuClick Detected!, item# " + item);

            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                game.resume();

            }
        });
        builder.show();


    }

    public void showGameplayMenu() {
        final GameplayManager modes = game.getGameplayManager();
        String[] gameplayMenuLabels = modes.getLabels();
        Integer[] gameplayMenuIcons = modes.getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(this,
                android.R.layout.select_dialog_singlechoice,
                gameplayMenuLabels,
                gameplayMenuIcons);

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Gameplay Options");
        builder.setPositiveButton("OK", null);
        builder.setSingleChoiceItems(adapter, modes.getCurrentID(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Gameplay Selected:" + item);
                modes.setGameplayMode(item);
                game.startNewGame(false);
            }
        });
        builder.show();

    }

    public void showThemeMenu() {
        final ThemeManager themes = game.getThemeManager();
        String[] themeMenuLabels = themes.getAvailableThemeLabels();
        Integer[] themeMenuIcons = themes.getAvailableThemeIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(this,
                android.R.layout.select_dialog_singlechoice,
                themeMenuLabels,
                themeMenuIcons);

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Themes Options");
        builder.setPositiveButton("OK", null);
        builder.setSingleChoiceItems(adapter, themes.getCurrentThemeID(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Theme Selected: " + item);
                if(themes.isAvailable(item)) {
                    themes.setTheme(item);
                } else {
                    purchaseItem(themes.getSkuForTheme(item));
                }

            }
        });
        builder.show();

    }

    public void showRandomBotMenu() {

        String[] pictureOptions = new String[] {"Take a Picture", "Choose Picture from Gallery"};
        Integer[] pictureIcons = new Integer[] {R.drawable.ic_action_image_photo_camera,
                                                R.drawable.ic_action_editor_insert_photo};

        ListAdapter adapter = new ArrayAdapterWithIcons(this,
                android.R.layout.select_dialog_item,
                pictureOptions,
                pictureIcons);

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Picture for RandomBot");
        builder.setNegativeButton("Clear Image", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "RandomBogImage will be cleared");
                game.clearRandomBotImage();
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

    public void showCreditScreen() {
        Log.d(LOGGER, "Displaying Credit Screen");

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Credits");
        builder.setMessage("Amazing Game Pieces: Colin Kirk\n\n" +
                "Astonishing Sound Effects: Frank Wu, MD, PhD\n\n" +
                "Almost Everything Else: Sarah Wu");
        builder.setPositiveButton("High-Fives To All!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Purchasing RandomBot");
                game.resume();
            }
        });
        builder.show();
    }

    public void showRandomBotPurchaseMenu() {
        Log.d(LOGGER, "Displaying RandomBotPurchase Menu");

        AlertDialog.Builder builder = getDialog(this);
        builder.setTitle("Purchase RandomBot");
        builder.setMessage("RandomBot is an awesome item that is crazy fun to pop!  " +
                "It is worth more points than the other items and you get to pick the image." +
                "\n\nYou could pop a picture of a tree, your boss, your boss in a tree- really the " +
                "options are as endless as the court case you would have if you pinned your boss " +
                "in a tree.\n\nInstead of going to jail and losing your job, just click the " +
                "\"Take My Money\" button below to begin this new amazing journey in your life.");
        builder.setNegativeButton("No: I hate fun.", null);
        builder.setPositiveButton("Yes: Take My Money!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(LOGGER, "Purchasing RandomBot");
                purchaseItem(RANDOM_BOT_SKU);
            }
        });
        builder.show();
    }

    public void showHighScoreMenu() {
        String[] scoreLabels = game.getGameplayManager().getHighScores();
        Integer[] gameplayMenuIcons = game.getGameplayManager().getIconImageIDs();

        ListAdapter adapter = new ArrayAdapterWithIcons(this,
                android.R.layout.select_dialog_item,
                scoreLabels,
                gameplayMenuIcons);

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

    public AlertDialog.Builder getDialog(Context myContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 23) {
            builder = new AlertDialog.Builder(myContext,
                    android.R.style.Theme_Holo_Light_Dialog);
        }  else if (Build.VERSION.SDK_INT >= 23) {
            builder = new AlertDialog.Builder(myContext,
                    android.R.style.Theme_Material_Light_Dialog_Alert);
        }

        return builder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
            Log.d(LOGGER, "Camera has returned a file.");
            storeBitmap((Bitmap) data.getExtras().get("data"));
        } else if (resultCode == RESULT_OK && requestCode == FILE_RESULT) {
            Log.d(LOGGER, "Gallery has returned a file.");
            try {
                Bitmap selectedImage = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(data.getData()));
                storeBitmap(getCorrectBitmap(selectedImage, data.getData()));
            } catch (Exception e){
                Log.e(LOGGER, "Cannot load file");
            }
        } else if (billingHelper != null && requestCode == BILLING_RESULT) {
            billingHelper.handleActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public Bitmap getCorrectBitmap(Bitmap bitmap, Uri imageUri) {
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = getContentResolver().query(imageUri, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }

        try {
            if(cur != null) {
                cur.close();
            }
        } catch(java.lang.NullPointerException e) {
            Log.w(LOGGER, "Cursor became null before it was closed.  This may cause issues");
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public void storeBitmap(Bitmap existingFile) {
        Log.d(LOGGER, "Scaling file ...");

        final int maxSize = game.getWidth() / 3;
        int outWidth;
        int outHeight;
        int inWidth = existingFile.getWidth();
        int inHeight = existingFile.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        Bitmap scaledFile = Bitmap.createScaledBitmap(existingFile, outWidth, outHeight, false);

        File destination = new File(getFilesDir(),
                "RandomBotImage.png");
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
