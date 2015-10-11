package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import com.lookingdynamic.lookingbusy.R;

/**
 * The GameplayManager contains all of the necessary information to control the gameplay. This
 * include the mode of the gameplay, the levels associated with that mode, and the user's statistics
 * for the game so far.
 * Created by swu on 9/6/2015.
 */
public class GameplayManager {
    private static final String LOGGER = GameplayManager.class.getSimpleName();

    protected SettingsStorageManager settings;
    protected GameplayMode[] modes;
    protected int currentHighScore;
    protected int score;
    protected int newHighScore;
    protected int currentLevel;
    protected int pointsToNextLevel;
    protected long levelEndTime;
    protected int objectsLeftInLevel;
    protected int livesLeft;
    protected int currentMode;

    public GameplayManager(SettingsStorageManager settings, Resources myResources) {
        this.settings = settings;

        TypedArray gameplayModes = myResources.obtainTypedArray(R.array.gameplay_modes);
        modes = new GameplayMode[gameplayModes.length()];

        for (int i = 0; i < gameplayModes.length(); i++){
            modes[i] = new GameplayMode(myResources,
                                        myResources.getXml(gameplayModes.getResourceId(i, -1)));
        }
        gameplayModes.recycle();

        currentMode = settings.getGameplay();
        if (currentMode >= modes.length) {
            currentMode = 0;
        }

        currentHighScore = settings.getHighScore(currentMode);

        clearCurrentStats();
    }

    public void clearCurrentStats() {
        score = 0;
        currentLevel = 0;
        livesLeft = modes[currentMode].getLivesAllowed();
        setLevelSettings();
    }

    public boolean addToScore(int points){
        boolean newHighScoreAchieved = false;
        if (currentHighScore > 0                            // There is a High Score to beat
                && newHighScore <= currentHighScore         // We haven't beaten it yet this round
                && score <= currentHighScore                // Just crossed the score
                && score + points > currentHighScore) {
            newHighScoreAchieved = true;
            Log.d(LOGGER, "New High Score Achieved!");
        }
        score = score + points;
        if (score > newHighScore) {
            newHighScore = score;
        }
        if (pointsToNextLevel > 0) {
            if (pointsToNextLevel - points <= 0) {
                levelUp();
                Log.d(LOGGER, "Levelling up after normal level");
            } else {
                pointsToNextLevel = pointsToNextLevel - points;
            }
        }
        if (points < 0 && modes[currentMode].getLivesAllowed() > 0) {
            livesLeft--;
        }
        return newHighScoreAchieved;
    }

    public boolean missedObject(int points){
        boolean stillAlive = true;

        score = score - points;

        if (isLifeRestrictedMode()) {
            livesLeft--;
            if (livesLeft <= 0) {
                stillAlive = false;
                storeHighScore();
            }
        }
        return stillAlive;
    }

    public String getScoreDisplayString(){
        String scoreString = "";
        if (!modes[currentMode].getLevelName(currentLevel).equals("")) {
            scoreString = scoreString + modes[currentMode].getLevelName(currentLevel) + ": ";
        }
        scoreString = scoreString + score;
        return scoreString;
    }


    public String getTimeRemainingDisplayString() {
        String returnString;
        long millisLeft = (levelEndTime - System.currentTimeMillis()) / 1000;
        if (millisLeft > 0) {
            returnString = "Time Left: " + millisLeft;
        } else {
            returnString = "Time is up!";
            Log.d(LOGGER,"Levelling up after loot level");
            levelUp();
        }
        return returnString;
    }

    public String getLivesRemainingString() {
        String returnString = null;
        if (isLifeRestrictedMode()) {
            returnString = "Lives Left: " + livesLeft;
        }
        return returnString;
    }

    public boolean isHighScore() {
        return (newHighScore > currentHighScore);
    }

    protected void levelUp(){
        storeHighScore();
        currentLevel++;
        setLevelSettings();
    }

    protected void setLevelSettings() {
        pointsToNextLevel = modes[currentMode].getPointsToNextLevel(currentLevel);
        setLevelEndTime(modes[currentMode].getTimeToNextLevel(currentLevel));
        objectsLeftInLevel = modes[currentMode].getTotalObjectsToCreate(currentLevel);
    }

    private void setLevelEndTime(int timeToNextLevel) {
        if (timeToNextLevel == 0) {
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

    public GameplayMode getCurrentMode() {
        return modes[currentMode];
    }

    public String[] getLabels() {
        String[] labels = new String[modes.length];
        for (int i = 0; i < modes.length; i++) {
            labels[i] = modes[i].getName();
        }
        return labels;
    }

    public Integer[] getIconImageIDs() {
        Integer[] icons = new Integer[modes.length];
        for (int i = 0; i < modes.length; i++) {
            icons[i] = modes[i].getIconImageId();
        }
        return icons;
    }

    public int getCurrentID() {
        return currentMode;
    }

    public int getCurrentIconID() {
        return getCurrentMode().getIconImageId();
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
            setLevelSettings();
        }
    }

    public void storeHighScore() {
        if (newHighScore > currentHighScore) {
            settings.setHighScore(currentMode, newHighScore);
        }
    }

    public int getNewHighScore() {
        return newHighScore;
    }

    public String[] getHighScores() {
        String[] scoreLabels = new String[modes.length];
        for(int i = 0; i < modes.length; i++) {
            scoreLabels[i] = modes[i].getName() + ": " + settings.getHighScore(i);
        }

        return scoreLabels;
    }

    public void clearAllScores() {
        for (int i = 0; i < modes.length; i++) {
            settings.setHighScore(i, 0);
        }
        currentHighScore = 0;
    }

    public boolean shouldMakeObject(int objectCount) {
        boolean returnValue = true;

        // If this is a boss level
        if (isBossLevel()) {

            // and there are objects left from the previous level, wait
            if (objectCount > 0
                    && getCurrentMode().getTotalObjectsToCreate(currentLevel) == objectsLeftInLevel) {
                returnValue = false;
            }
            // or there are no more objects left, don't make any more
            else if (objectsLeftInLevel <= 0) {
                returnValue = false;
                // Also, If we have popped all of the objects, level up
                if (objectCount == 0) {
                    levelUp();
                    Log.d(LOGGER,"Levelling up after boss level");
                }
            }
            else {
                returnValue = true;
                // If this is the first object we are creating for the level, set the points to
                // the next level for it
                if (getCurrentMode().getTotalObjectsToCreate(currentLevel) == objectsLeftInLevel) {
                    pointsToNextLevel = getCurrentMode().getPointsToNextLevel(currentLevel);
                }
            }

        }

        return returnValue;
    }

    public void makeObject() {
        if (objectsLeftInLevel > 0) {
            objectsLeftInLevel--;
        }
    }

    public int getPercentChanceOfCreation() {
        return getCurrentLevel().getPercentChanceOfCreation();
    }

    public int getBallPercentCreated() {
        return getCurrentLevel().getBallPercentCreated();
    }

    public int getBallPercentSlow(){
        return getCurrentLevel().getBallPercentSlow();
    }

    public int getBallPercentMedium(){
        return getCurrentLevel().getBallPercentMedium();
    }

    public int getBallPercentFast(){
        return getCurrentLevel().getBallPercentMedium();
    }

    public int getBallPercentSuperFast(){
        return getCurrentLevel().getBallPercentSuperFast();
    }

    public int getBalloonPercentCreated() {
        return getCurrentLevel().getBalloonPercentCreated();
    }

    public int getBalloonPercentSlow(){
        return getCurrentLevel().getBalloonPercentSlow();
    }

    public int getBalloonPercentMedium(){
        return getCurrentLevel().getBalloonPercentMedium();
    }

    public int getBalloonPercentFast(){
        return getCurrentLevel().getBalloonPercentFast();
    }

    public int getBalloonPercentSuperFast(){
        return getCurrentLevel().getBalloonPercentSuperFast();
    }

    public int getDropletPercentCreated() {
        return getCurrentLevel().getDropletPercentCreated();
    }

    public int getDropletPercentSlow(){
        return getCurrentLevel().getDropletPercentSlow();
    }

    public int getDropletPercentMedium(){
        return getCurrentLevel().getDropletPercentMedium();
    }

    public int getDropletPercentFast(){
        return getCurrentLevel().getDropletPercentFast();
    }

    public int getDropletPercentSuperFast(){
        return getCurrentLevel().getDropletPercentSuperFast();
    }

    public int getRandomBotPercentCreated() {
        return getCurrentLevel().getRandomBotPercentCreated();
    }

    public int getRandomBotPercentSlow(){
        return getCurrentLevel().getRandomBotPercentSlow();
    }

    public int getRandomBotPercentMedium(){
        return getCurrentLevel().getRandomBotPercentMedium();
    }

    public int getRandomBotPercentFast(){
        return getCurrentLevel().getRandomBotPercentFast();
    }

    public int getRandomBotPercentSuperFast(){
        return getCurrentLevel().getRandomBotPercentSuperFast();
    }

    public boolean isLifeRestrictedMode() {
        return getCurrentMode().getLivesAllowed() > 0;
    }

    public boolean isBossLevel() {
        return getCurrentMode().getTotalObjectsToCreate(currentLevel) > 0;
    }

    public boolean isTimedLevel() {
        return getCurrentMode().getTimeToNextLevel(currentLevel) > 0;
    }

    public boolean isBubbleGrid() {
        return getCurrentMode().isBubbleGrid(currentLevel);
    }

    public boolean isGameOver() {
        return (isLifeRestrictedMode() && livesLeft <= 0);
    }
}
