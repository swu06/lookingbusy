package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;

/**
 * Created by swu on 9/15/2015.
 */
public class GameplayMode {

    private static final String LOGGER = Level.class.getSimpleName();

    protected Level[] levels;
    private String name;
    private int icon;

    public GameplayMode(Resources myResources, TypedArray items) {
        //TypedArray items = myResources.obtainTypedArray(gamePlayModeArray);

        if(items.length() > 1) {
            name = myResources.getString(items.getResourceId(0, -1));
            icon = items.getResourceId(1, -1);
            levels = new Level[items.length()-2];
        }for(int i=2;i<items.length();i++){
            levels[i-2] = new Level(myResources.getXml(items.getResourceId(i,-1)));
        }

    }

    public String getName() {
        return name;
    }

    public int getIconImageId() {
        return icon;
    }

    public Level getLevel(int i) {
        return levels[safeLevel(i)];
    }

    public int safeLevel(int inputLevel){
        if(inputLevel >= levels.length) {
            inputLevel = levels.length - 1;
        }
        return inputLevel;
    }

    public String getLevelName(int currentLevel) {
        return levels[safeLevel(currentLevel)].getName();
    }

    public int getPointsToNextLevel(int currentLevel) {
        return levels[safeLevel(currentLevel)].getPointsToNextLevel();
    }

    public int getTimeToNextLevel(int currentLevel) {
        return levels[safeLevel(currentLevel)].getTimeToNextLevel();
    }
}
