package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;

import com.lookingdynamic.lookingbusy.R;

/**
 * This class loads the themes that are currently available and provides accessors to that
 * information.  It also keeps track of the currently selected theme including storing that
 * information to the device settings when it changes. Finally, it also holds the information for
 * the randomBot object since that is not related to any particular theme.
 *
 * Created by swu on 9/20/2015.
 */
public class ThemeManager {

    private static final String LOGGER = ThemeManager.class.getSimpleName();
    protected SettingsStorageManager settings;
    protected GameTheme themes[];
    protected int currentTheme;
    protected Bitmap defaultRandomBotImage;
    protected Bitmap randomBot;
    protected Bitmap poppedRandomBot;

    public ThemeManager(SettingsStorageManager settings, Resources myResources) {
        this.settings = settings;
        TypedArray availableThemesArray = myResources.obtainTypedArray(R.array.available_game_themes);

        themes = new GameTheme[availableThemesArray.length()];

        for (int i = 0; i < availableThemesArray.length(); i++) {
            themes[i] = new GameTheme(myResources,
                    myResources.getXml(availableThemesArray.getResourceId(i,-1)));
        }
        availableThemesArray.recycle();

        defaultRandomBotImage = BitmapFactory.decodeResource(myResources, R.drawable.randombot);
        randomBot = BitmapFactory.decodeResource(myResources, R.drawable.randombot);
        poppedRandomBot = BitmapFactory.decodeResource(myResources, R.drawable.randombot_popped);

        tryToLoadFromSettings();

        Log.v(LOGGER, "All (" + themes.length + ") themes loaded.");
    }

    /*
     * This method attempts to load the current theme and RandomBot theme information from the
     * device settings.  If unsuccessful, these settings will use the defaults.
     */
    private void tryToLoadFromSettings() {
        if (settings != null) {
            String randomBotImagePath = settings.getRandomBotLocation();
            if (randomBotImagePath != null) {
                try {
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    randomBot = BitmapFactory.decodeFile(randomBotImagePath, bmOptions);
                } catch (Exception e) {
                    // If we fail to load an object, clear out that location
                    Log.e(LOGGER, "Failed to load file from location: " + randomBotImagePath);
                    settings.setRandomBotLocation(null);
                }
            }
            currentTheme = settings.getTheme();
            if (currentTheme >= themes.length) {
                currentTheme = 0;
            }
        }
    }

    public String[] getAvailableThemeLabels() {
        String[] labels = new String[themes.length];
        for (int i = 0; i < themes.length; i++) {
            labels[i] = themes[i].getName();
        }
        return labels;
    }

    public Integer[] getAvailableThemeIconImageIDs() {
        Integer[] icons = new Integer[themes.length];
        for (int i = 0; i < themes.length; i++) {
            icons[i] = themes[i].getIconImageId();
        }
        return icons;
    }

    public int getCurrentThemeID() {
        return currentTheme;
    }

    public GameTheme getCurrentTheme() {
        return themes[currentTheme];
    }

    public int getCurrentThemeIconID() {
        return getCurrentTheme().getIconImageId();
    }

    /*
     * This method switches between themes.  Whenever a new theme is selected, the old theme
     * should be cleaned up to save memory.  As the images from the new theme are used, they will
     * be loaded into memory.
     */
    public void setTheme(int theme) {
        if (theme >= 0 && theme < themes.length && theme != currentTheme)  {
            getCurrentTheme().unloadImages();
            currentTheme = theme;
            if(settings != null) {
                settings.setTheme(currentTheme);
            }
        }
        Log.v(LOGGER, "Current theme set to item " + currentTheme);
    }

    public void setRandomBotImage(Bitmap randomBot) {
        this.randomBot = randomBot;
        Log.v(LOGGER, "RandomBotImage has been set.");
    }

    public void clearRandomBotImage() {
        randomBot = defaultRandomBotImage;
        settings.setRandomBotLocation(null);
        Log.v(LOGGER, "RandomBotImage has been reset to the default.");
    }

    public Bitmap getRandomBot() {
        return randomBot;
    }

    public Bitmap getPoppedRandomBot() {
        return poppedRandomBot;
    }

    public Bitmap getBall() {
        return getCurrentTheme().getBall();
    }

    public Bitmap getBalloon(int whichOne) {
        return getCurrentTheme().getBalloon(whichOne);
    }

    public Bitmap getDroplet() {
        return getCurrentTheme().getDroplet();
    }

    public Bitmap getBubble() {
        return getCurrentTheme().getBubble();
    }

    public Bitmap getPoppedBall() {
        return getCurrentTheme().getPoppedBall();
    }

    public Bitmap getPoppedBalloon(int whichOne) {
        return getCurrentTheme().getPoppedBalloon(whichOne);
    }

    public Bitmap getPoppedDroplet() {
        return getCurrentTheme().getPoppedDroplet();
    }

    public Bitmap getPauseSign() {
        return getCurrentTheme().getPauseSign();
    }

    public Paint getPainter() {
        return getCurrentTheme().getPainter();
    }
}
