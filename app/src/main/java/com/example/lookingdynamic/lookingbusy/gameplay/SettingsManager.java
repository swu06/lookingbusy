package com.example.lookingdynamic.lookingbusy.gameplay;

import android.app.Activity;
import android.content.Context;
import android.media.audiofx.BassBoost;

/**
 * Created by swu on 9/20/2015.
 */
public class SettingsManager {
    public static final String CURRENT_THEME_KEY = "currentThemeKey";
    public static final String CURRENT_GAMEPLAY_KEY = "currentGameplayKey";
    public static final String CURRENT_HIGHSCORE_KEY = "currentHighScoreKey";

    private Context myContext;

    public SettingsManager(Context myContext) {
        this.myContext = myContext;
    }

    private int getIntValueOrDefault(String key) {
        return myContext.getSharedPreferences("BOOT_PREF", Activity.MODE_PRIVATE).getInt(key, 0);
    }

    private void setIntValue(String key, int value) {
        myContext.getSharedPreferences("BOOT_PREF", Activity.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .commit();
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
}
