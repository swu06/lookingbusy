package com.example.lookingdynamic.lookingbusy.model;

/**
 * Created by swu on 9/6/2015.
 */
public class GameStatistics {

    public int score;
    public int level;

    public GameStatistics() {
        score = 0;
        level = 0;
    }

    public void addToScore(int increase){
        score = score + increase;
    }

    public int getScore(){
        return score;
    }

    public String toString(){
        return "" + score;
    }

    public void levelUp(){
        level++;
    }
}
