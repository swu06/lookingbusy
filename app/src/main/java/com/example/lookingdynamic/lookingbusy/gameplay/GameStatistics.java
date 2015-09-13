package com.example.lookingdynamic.lookingbusy.gameplay;

/**
 * Created by swu on 9/6/2015.
 */
public class GameStatistics {

    public static final int RELAXING_MODE = 0;
    public static final int CHALLANGING_MODE = 1;
    private int score;
    private int level;
    private int pointsToNextLevel;
    private int mode;

    public GameStatistics(int mode) {
        score = 0;
        level = 1;
        pointsToNextLevel = 50;
        this.mode = mode;
        pointsToNextLevel = 0;
    }

    public void addToScore(int points){
        score = score + points;
        if(mode == CHALLANGING_MODE && pointsToNextLevel > 0) {
            if (pointsToNextLevel - points <= 0) {
                levelUp();
            } else {
                pointsToNextLevel = pointsToNextLevel - points;
            }
        }
    }

    public int getMode(){
        return mode;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public String toString(){
        String scoreString = "" + score;
        if(mode == CHALLANGING_MODE) {
            scoreString = level + ": " + score;
        }
        return scoreString;
    }

    private void levelUp(){
        level++;
        pointsToNextLevel = level*100;
    }

    public int getLevel() {
        int levelToReturn;
        if(mode == CHALLANGING_MODE) {
            levelToReturn = level;
        } else {
            levelToReturn = 0;
        }
        return levelToReturn;
    }
}
