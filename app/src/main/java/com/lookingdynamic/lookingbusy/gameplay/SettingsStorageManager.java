package com.lookingdynamic.lookingbusy.gameplay;

import android.app.Activity;
import android.content.Context;

/**
 * This class manages getting and setting the current settings from the device storage.  This
 * includes the user preferences and game statistics like score.
 *
 * Created by swu on 9/20/2015.
 */
public class SettingsStorageManager {
    public static final String CURRENT_THEME_KEY = "currentThemeKey";
    public static final String CURRENT_GAMEPLAY_KEY = "currentGameplayKey";
    public static final String CURRENT_HIGHSCORE_KEY = "currentHighScoreKey";
    public static final String CURRENT_RANDOMBOT_KEY = "currentRandomBotLocation";
    public static final String MUTE_KEY = "volumeIsMuted";
    public static final String BLACK_BACKGROUND_KEY = "backgroundIsBlack";

    private Context myContext;

    public SettingsStorageManager(Context myContext) {
        this.myContext = myContext;
    }

    protected boolean getBooleanValueOrDefault(String key) {
        return myContext.getSharedPreferences("BOOT_PREF",
                Activity.MODE_PRIVATE).getBoolean(key, false);
    }

    protected void setBooleanValue(String key, boolean value) {
        myContext.getSharedPreferences("BOOT_PREF",
                Activity.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    protected int getIntValueOrDefault(String key) {
        return myContext.getSharedPreferences("BOOT_PREF",
                                    Activity.MODE_PRIVATE).getInt(key, 0);
    }

    protected void setIntValue(String key, int value) {
        myContext.getSharedPreferences("BOOT_PREF",
                                    Activity.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    protected String getStringValueOrDefault(String key) {
        return myContext.getSharedPreferences("BOOT_PREF",
                                    Activity.MODE_PRIVATE).getString(key, null);
    }

    protected void setStringValue(String key, String value) {
        myContext.getSharedPreferences("BOOT_PREF",
                                    Activity.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public int getTheme() {
        return getIntValueOrDefault(CURRENT_THEME_KEY);
    }

    public void setTheme(int currentTheme) {
        setIntValue(CURRENT_THEME_KEY, currentTheme);
    }

    public int getGameplay() {
        return getIntValueOrDefault(CURRENT_GAMEPLAY_KEY);
    }

    public void setGameplay(int currentMode) {
        setIntValue(CURRENT_GAMEPLAY_KEY, currentMode);
    }

    public int getHighScore(int currentMode) {
        return getIntValueOrDefault(CURRENT_HIGHSCORE_KEY + currentMode);
    }

    public void setHighScore(int currentMode, int highScore) {
        setIntValue(CURRENT_HIGHSCORE_KEY + currentMode, highScore);
    }

    public String getRandomBotLocation() {
        return getStringValueOrDefault(CURRENT_RANDOMBOT_KEY);
    }

    public void setRandomBotLocation(String path) {
        setStringValue(CURRENT_RANDOMBOT_KEY, path);
    }

    public boolean getPurchased(String sku) {
        return getBooleanValueOrDefault(sku);
    }

    public void setPurchased(String sku, boolean value) {
        setBooleanValue(sku, value);
    }

    public boolean getMute() {
        return getBooleanValueOrDefault(MUTE_KEY);
    }

    public void setMute(boolean value) {
        setBooleanValue(MUTE_KEY, value);
    }

    public boolean getBlackBackground() {
        return getBooleanValueOrDefault(BLACK_BACKGROUND_KEY);
    }

    public void setBlackBackground(boolean value) {
        setBooleanValue(BLACK_BACKGROUND_KEY, value);
    }
}
