package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;

import com.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/20/2015.
 */
public class ThemeManager {

    private static final String LOGGER = ThemeManager.class.getSimpleName();
    private SettingsStorageManager settings;
    private GameTheme themes[];
    private int currentTheme;
    private Bitmap defaultRandomBotImage;
    private Bitmap randomBot;
    private Bitmap randomBotPopped;


    public ThemeManager(SettingsStorageManager settings, Resources myResources) {
        this.settings = settings;
        TypedArray availableThemesArray = myResources.obtainTypedArray(R.array.available_game_themes);

        themes = new GameTheme[availableThemesArray.length()];

        for (int i = 0; i < availableThemesArray.length(); i++) {
            themes[i] = new GameTheme(myResources,
                    myResources.getXml(availableThemesArray.getResourceId(i,-1)));
        }
        availableThemesArray.recycle();

        defaultRandomBotImage = BitmapFactory.decodeResource(myResources, R.mipmap.ic_launcher);
        randomBot = BitmapFactory.decodeResource(myResources, R.mipmap.ic_launcher);
        randomBotPopped = BitmapFactory.decodeResource(myResources, R.drawable.ic_action_notification_adb);
        tryToLoadFromSettings();
    }

    public void tryToLoadFromSettings() {
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

    public String[] getLabels() {
        String[] labels = new String[themes.length];
        for (int i = 0; i < themes.length; i++) {
            labels[i] = themes[i].getName();
        }
        return labels;
    }

    public Integer[] getIconImageIDs() {
        Integer[] icons = new Integer[themes.length];
        for (int i = 0; i < themes.length; i++) {
            icons[i] = themes[i].getIconImageId();
        }
        return icons;
    }

    public int getCurrentID() {
        return currentTheme;
    }

    public GameTheme getCurrentTheme() {
        return themes[currentTheme];
    }

    public int getCurrentIconID() {
        return getCurrentTheme().getIconImageId();
    }

    /*
     * This method switches between themes.  Whenever a new theme is selected, the old theme
     * should be cleaned up to save memory
     */
    public void setTheme(int theme) {
        if (theme >= 0 && theme < themes.length && theme != currentTheme)  {
            getCurrentTheme().unloadImages();
            currentTheme = theme;
            if(settings != null) {
                settings.setTheme(currentTheme);
            }
        }
    }

    public void setRandomBotImage(Bitmap randomBot) {
        this.randomBot = randomBot;
    }

    public Bitmap getRandomBot() {
        return randomBot;
    }

    public Bitmap getPoppedRandomBot() {
        return randomBotPopped;
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

    public void clearRandomBotImage() {
        randomBot = defaultRandomBotImage;
    }
}
