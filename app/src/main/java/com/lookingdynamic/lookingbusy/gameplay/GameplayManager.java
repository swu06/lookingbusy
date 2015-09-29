package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import com.lookingdynamic.lookingbusy.R;

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
    protected int currentLevel;
    protected int pointsToNextLevel;
    protected long levelEndTime;
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

        currentHighScore = settings.getHighScore(currentMode);

        score = 0;
        clearCurrentStats();
    }

    public void clearCurrentStats() {
        score = 0;
        currentLevel = 0;
        pointsToNextLevel = modes[currentMode].getPointsToNextLevel(currentLevel);
        setLevelEndTime(modes[currentMode].getTimeToNextLevel(currentLevel));
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

    public String getScoreDisplayString(){
        String scoreString = "";
        if(modes[currentMode].getLevelName(currentLevel) != "") {
            scoreString = scoreString + modes[currentMode].getLevelName(currentLevel) + ": ";
        }
        scoreString = scoreString + score;
        return scoreString;
    }

    public String getTimeRemainingDisplayString() {
        String returnString = null;
        long millisLeft = (levelEndTime - System.currentTimeMillis()) / 1000;
        if(millisLeft > 0) {
            returnString = "Time Left: " + millisLeft;
        } else {
            returnString = "Time is up!";
            levelUp();
        }
        return returnString;
    }

    public boolean isHighScore() {
        if(newHighScore > currentHighScore) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTimedLevel() {
        if(levelEndTime != 0) {
            return true;
        } else {
            return false;
        }
    }
    protected void levelUp(){
        currentLevel++;
        pointsToNextLevel = modes[currentMode].getPointsToNextLevel(currentLevel);
        setLevelEndTime(modes[currentMode].getTimeToNextLevel(currentLevel));
    }
    private void setLevelEndTime(int timeToNextLevel) {
        if(timeToNextLevel == 0){
            levelEndTime = 0;
        } else {
            levelEndTime = System.currentTimeMillis() + (timeToNextLevel * 1000);
        }
    }


    public int getLevel() {
        return currentLevel;
    }

    public Level getCurrentLevel() {
        return modes[currentMode].getLevel(currentLevel);
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
            currentLevel = 0;

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

    public int getPercentChanceOfCreation() {
        return modes[currentMode].getLevel(currentLevel).getPercentChanceOfCreation();
    }

    public int getBallPercentCreated() {
        return modes[currentMode].getLevel(currentLevel).getBallPercentCreated();
    }

    public int getBallPercentSlow(){
        return modes[currentMode].getLevel(currentLevel).getBallPercentSlow();
    }

    public int getBallPercentMedium(){
        return modes[currentMode].getLevel(currentLevel).getBallPercentMedium();
    }

    public int getBallPercentFast(){
        return modes[currentMode].getLevel(currentLevel).getBallPercentMedium();
    }

    public int getBallPercentSuperFast(){
        return modes[currentMode].getLevel(currentLevel).getBallPercentSuperFast();
    }

    public int getBalloonPercentCreated() {
        return modes[currentMode].getLevel(currentLevel).getBalloonPercentCreated();
    }

    public int getBalloonPercentSlow(){
        return modes[currentMode].getLevel(currentLevel).getBalloonPercentSlow();
    }

    public int getBalloonPercentMedium(){
        return modes[currentMode].getLevel(currentLevel).getBalloonPercentMedium();
    }

    public int getBalloonPercentFast(){
        return modes[currentMode].getLevel(currentLevel).getBalloonPercentFast();
    }

    public int getBalloonPercentSuperFast(){
        return modes[currentMode].getLevel(currentLevel).getBalloonPercentSuperFast();
    }

    public int getDropletPercentCreated() {
        return modes[currentMode].getLevel(currentLevel).getDropletPercentCreated();
    }

    public int getDropletPercentSlow(){
        return modes[currentMode].getLevel(currentLevel).getDropletPercentSlow();
    }

    public int getDropletPercentMedium(){
        return modes[currentMode].getLevel(currentLevel).getDropletPercentMedium();
    }

    public int getDropletPercentFast(){
        return modes[currentMode].getLevel(currentLevel).getDropletPercentFast();
    }

    public int getDropletPercentSuperFast(){
        return modes[currentMode].getLevel(currentLevel).getDropletPercentSuperFast();
    }

    public int getRandomBotPercentCreated() {
        return modes[currentMode].getLevel(currentLevel).getRandomBotPercentCreated();
    }

    public int getRandomBotPercentSlow(){
        return modes[currentMode].getLevel(currentLevel).getRandomBotPercentSlow();
    }

    public int getRandomBotPercentMedium(){
        return modes[currentMode].getLevel(currentLevel).getRandomBotPercentMedium();
    }

    public int getRandomBotPercentFast(){
        return modes[currentMode].getLevel(currentLevel).getRandomBotPercentFast();
    }

    public int getRandomBotPercentSuperFast(){
        return modes[currentMode].getLevel(currentLevel).getRandomBotPercentSuperFast();
    }

}
