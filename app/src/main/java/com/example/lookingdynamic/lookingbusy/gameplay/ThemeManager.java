package com.example.lookingdynamic.lookingbusy.gameplay;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lookingdynamic.lookingbusy.R;

import java.io.File;

/**
 * Created by swu on 9/20/2015.
 */
public class ThemeManager {
    private SettingsManager settings;
    private GameTheme themes[];
    private int currentTheme;
    private Bitmap randomBot = null;
    private Bitmap randomBotPopped = null;

    public ThemeManager(SettingsManager settings, Resources myResources) {
        this.settings = settings;
        TypedArray availableThemesArray = myResources.obtainTypedArray(R.array.available_game_themes);

        themes = new GameTheme[availableThemesArray.length()];

        for(int i=0; i < availableThemesArray.length(); i++) {
            themes[i] = new GameTheme(myResources,
                    myResources.getXml(availableThemesArray.getResourceId(i,-1)));
        }

        tryToLoadFromSettings();
    }

    public void tryToLoadFromSettings() {
        if(settings != null) {
            String randomBotImagePath = settings.getRandomBotLocation();
            if (randomBotImagePath != null) {
                try {
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    randomBot = BitmapFactory.decodeFile(randomBotImagePath, bmOptions);
                } catch (Exception E) {
                    settings.setRandomBotLocation(null);
                }
            }
            currentTheme = settings.getTheme();
            if(currentTheme >= themes.length) {
                currentTheme = 0;
            }
        }
    }

    public String[] getLabels() {
        String[] labels = new String[themes.length];
        for(int i=0;i < themes.length; i++) {
            labels[i] = themes[i].getName();
        }
        return labels;
    }

    public Integer[] getIconImageIDs() {
        Integer[] icons = new Integer[themes.length];
        for(int i=0;i < themes.length; i++) {
            icons[i] = themes[i].getIconImageId();
        }
        return icons;
    }

    public int getCurrentID() {
        return currentTheme;
    }

    public int getCurrentIconID() {
        return themes[currentTheme].getIconImageId();
    }

    /*
     * This method switches between themes.  Whenever a new theme is selected, the old theme
     * should be cleaned up to save memory
     */
    public void setTheme(int theme) {
        if(theme >= 0 && theme < themes.length && theme != currentTheme)  {
            themes[currentTheme].unloadImages();
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

    public Bitmap getRandomBotPopped() {
        return themes[currentTheme].getPoppedBalloon(0);
    }

    public Bitmap getBall() {
        return themes[currentTheme].getBall();
    }

    public Bitmap getBalloon(int whichOne) {
        return themes[currentTheme].getBalloon(whichOne);
    }

    public Bitmap getDroplet() {
        return themes[currentTheme].getDroplet();
    }

    public Bitmap getPoppedBall() {
        return themes[currentTheme].getPoppedBall();
    }

    public Bitmap getPoppedBalloon(int whichOne) {
        return themes[currentTheme].getPoppedBalloon(whichOne);
    }


    public Bitmap getPoppedDroplet() {
        return themes[currentTheme].getPoppedDroplet();
    }

    public Bitmap getPauseSign() {
        return themes[currentTheme].getPauseSign();
    }
}
