package com.example.lookingdynamic.lookingbusy.gameplay;

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

    public int getPercentChanceOfCreation(int currentLevel) {
        return levels[safeLevel(currentLevel)].getPercentChanceOfCreation();
    }

    public int getBallPercentCreated(int currentLevel) {
        return levels[safeLevel(currentLevel)].getBallPercentCreated();
    }

    public int getBallPercentSlow(int currentLevel){
        return levels[safeLevel(currentLevel)].getBallPercentSlow();
    }

    public int getBallPercentMedium(int currentLevel){
        return levels[safeLevel(currentLevel)].getBallPercentMedium();
    }

    public int getBallPercentFast(int currentLevel){
        return levels[safeLevel(currentLevel)].getBallPercentMedium();
    }

    public int getBallPercentSuperFast(int currentLevel){
        return levels[safeLevel(currentLevel)].getBallPercentSuperFast();
    }

    public int getBalloonPercentCreated(int currentLevel) {
        return levels[safeLevel(currentLevel)].getBalloonPercentCreated();
    }

    public int getBalloonPercentSlow(int currentLevel){
        return levels[safeLevel(currentLevel)].getBalloonPercentSlow();
    }

    public int getBalloonPercentMedium(int currentLevel){
        return levels[safeLevel(currentLevel)].getBalloonPercentMedium();
    }

    public int getBalloonPercentFast(int currentLevel){
        return levels[safeLevel(currentLevel)].getBalloonPercentFast();
    }

    public int getBalloonPercentSuperFast(int currentLevel){
        return levels[safeLevel(currentLevel)].getBalloonPercentSuperFast();
    }

    public int getDropletPercentCreated(int currentLevel) {
        return levels[safeLevel(currentLevel)].getDropletPercentCreated();
    }

    public int getDropletPercentSlow(int currentLevel){
        return levels[safeLevel(currentLevel)].getDropletPercentSlow();
    }

    public int getDropletPercentMedium(int currentLevel){
        return levels[safeLevel(currentLevel)].getDropletPercentMedium();
    }

    public int getDropletPercentFast(int currentLevel){
        return levels[safeLevel(currentLevel)].getDropletPercentFast();
    }

    public int getDropletPercentSuperFast(int currentLevel){
        return levels[safeLevel(currentLevel)].getDropletPercentSuperFast();
    }
}
