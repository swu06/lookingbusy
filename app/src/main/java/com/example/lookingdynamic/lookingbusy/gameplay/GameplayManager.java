package com.example.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.widget.Toast;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/6/2015.
 */
public class GameplayManager {
    private static final String LOGGER = GameplayManager.class.getSimpleName();

    protected SettingsManager settings;
    protected GameplayMode[] modes;
    protected int currentHighScore;
    protected int score;
    protected int newHighScore;
    protected int level;
    protected int pointsToNextLevel;
    protected int currentMode;

    public GameplayManager(SettingsManager settings, Resources myResources) {
        this.settings = settings;

        TypedArray items = myResources.obtainTypedArray(R.array.gameplay_modes);
        modes = new GameplayMode[items.length()];

        for(int i=0;i<items.length();i++){
            modes[i] = new GameplayMode(myResources, myResources.obtainTypedArray(items.getResourceId(i,-1)));
        }

        currentMode = settings.getGameplay();
        if(currentMode >= modes.length) {
            currentMode = 0;
        }

        score = 0;
        currentHighScore = settings.getHighScore(currentMode);

        level = 0;
        pointsToNextLevel = modes[currentMode].getPointsToNextLevel(level);

    }

    public boolean addToScore(int points){
        boolean newHighScoreAchieved = false;
        if( currentHighScore > 0                            // There is a High Score to beat
                && newHighScore <= currentHighScore         // We haven't beaten it yet this round
                && score <= currentHighScore                // Just crossed the score
                && score + points > currentHighScore) {
            newHighScoreAchieved = true;
            Log.d(LOGGER, "New High Score Achieved!");
        }
        score = score + points;
        if(score > newHighScore) {
            newHighScore = score;
        }
        if(pointsToNextLevel > 0) {
            if (pointsToNextLevel - points <= 0) {
                levelUp();
            } else {
                pointsToNextLevel = pointsToNextLevel - points;
            }
        }
        return newHighScoreAchieved;
    }

    public String getDisplayString(){
        String scoreString = "";
        if(modes[currentMode].getLevelName(level) != "") {
            scoreString = scoreString + modes[currentMode].getLevelName(level) + ": ";
        }
        scoreString = scoreString + score;
        return scoreString;
    }

    public boolean isHighScore() {
        if(newHighScore > currentHighScore) {
            return true;
        } else {
            return false;
        }
    }
    protected void levelUp(){
        level++;
        pointsToNextLevel = modes[currentMode].getPointsToNextLevel(level);
    }

    public int getLevel() {
        return level;
    }

    public Level getCurrentLevel() {
        return modes[currentMode].getLevel(level);
    }

    public String[] getLabels() {
        String[] labels = new String[modes.length];
        for(int i=0;i < modes.length; i++) {
            labels[i] = modes[i].getName();
        }
        return labels;
    }

    public Integer[] getIconImageIDs() {
        Integer[] icons = new Integer[modes.length];
        for(int i=0;i < modes.length; i++) {
            icons[i] = modes[i].getIconImageId();
        }
        return icons;
    }

    public int getCurrentID() {
        return currentMode;
    }

    public int getCurrentIconID() {
        return modes[currentMode].getIconImageId();
    }

    public void setGameplayMode(int mode) {
        if (mode >= 0 && mode < modes.length && mode != currentMode)  {
            storeHighScore();
            currentMode = mode;
            settings.setGameplay(currentMode);
            score = 0;
            newHighScore = 0;
            currentHighScore = settings.getHighScore(currentMode);
            level = 0;

        }
    }

    public void storeHighScore() {
        if(newHighScore > currentHighScore) {
            settings.setHighScore(currentMode, newHighScore);
        }
    }

    public String[] getHighScores() {
        String[] scoreLabels = new String[modes.length];
        for(int i=0; i<modes.length; i++) {
            scoreLabels[i] = modes[i].getName() + ": " + settings.getHighScore(i);
        }

        return scoreLabels;
    }

    public void clearAllScores() {
        for(int i=0; i<modes.length; i++) {
            settings.setHighScore(i, 0);
        }
    }

}
