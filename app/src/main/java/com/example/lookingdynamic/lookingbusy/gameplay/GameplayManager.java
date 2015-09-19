package com.example.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/6/2015.
 */
public class GameplayManager {

    private GameplayMode[] modes;
    private int score;
    private int level;
    private int pointsToNextLevel;
    private int currentMode;

    public GameplayManager(Resources myResources) {

        TypedArray items = myResources.obtainTypedArray(R.array.gameplay_modes);
        modes = new GameplayMode[items.length()];

        for(int i=0;i<items.length();i++){
            modes[i] = new GameplayMode(myResources, myResources.obtainTypedArray(items.getResourceId(i,-1)));
        }

        score = 0;
        currentMode = 0;
        level = 0;
        pointsToNextLevel = modes[currentMode].getPointsToNextLevel(level);

    }

    public void addToScore(int points){
        score = score + points;
        if(pointsToNextLevel > 0) {
            if (pointsToNextLevel - points <= 0) {
                levelUp();
            } else {
                pointsToNextLevel = pointsToNextLevel - points;
            }
        }
    }

    public int getCurrentGameplayMode(){
        return currentMode;
    }

    public GameplayMode[] getGameplayModes(){
        return modes;
    }

    public int getIconImageId(){
        return modes[currentMode].getIconImageId();
    }

    public void setCurrentMode(int currentMode){
        this.currentMode = currentMode;
    }

    public String toString(){
        String scoreString = modes[currentMode].getLevelName(level) + ": " + score;
        return scoreString;
    }

    private void levelUp(){
        level++;
        pointsToNextLevel = modes[currentMode].getPointsToNextLevel(level);
    }

    public int getLevel() {
        return level;
    }

    public Level getCurrentLevel() {
        return modes[currentMode].getLevel(level);
    }
}
