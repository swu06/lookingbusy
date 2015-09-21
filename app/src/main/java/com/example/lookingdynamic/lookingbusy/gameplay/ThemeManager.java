package com.example.lookingdynamic.lookingbusy.gameplay;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/20/2015.
 */
public class ThemeManager {
    private SettingsManager settings;
    private GameTheme themes[];
    private int currentTheme;

    public ThemeManager(SettingsManager settings, Resources myResources) {
        this.settings = settings;
        TypedArray availableThemesArray = myResources.obtainTypedArray(R.array.available_game_themes);

        themes = new GameTheme[availableThemesArray.length()];

        for(int i=0; i < availableThemesArray.length(); i++) {
            themes[i] = new GameTheme(myResources, myResources.getXml(availableThemesArray.getResourceId(i,-1)));
        }
        
        currentTheme = settings.getTheme();
        if(currentTheme >= themes.length) {
            currentTheme = 0;
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

    public GameTheme getCurrentTheme() {
        return themes[currentTheme];
    }

    /*
     * This method switches between themes.  Whenever a new theme is selected, the old theme
     * should be cleaned up to save memory
     */
    public void setTheme(int theme) {
        if(theme >= 0 && theme < themes.length && theme != currentTheme)  {
            themes[currentTheme].unloadImages();
            currentTheme = theme;
            settings.setTheme(currentTheme);
        }

    }

}
