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

    /*
    Level 1: All Balloons, all slow speed
    Level 2 (50 points):    100% Balloons - 50% at medium speed, 50% slow speed
    Level 3 (100 points):   75% Balloons - 50% at medium speed, 50% slow speed
                            25% Water Droplets - 100% medium speed
    Level 4 (200 points):   75% Balloons - 50% at medium speed, 50% slow speed
                            25% Water Droplets - 50% medium speed, 50% fast speed
    Level 5 (400 points):   50% Balloons - 50% at medium speed, 50% slow speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            20% Bouncy Balls - 100% fast speed
    Level 6 (600 points):   50% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            20% Bouncy Balls - 100% fast speed
    Level 7 (800 points):   50% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            20% Bouncy Balls - 100% fast speed
                            Double the number of items created
    Level 8 (1500 points):  30% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            40% Bouncy Balls - 100% fast speed
                            Double the number of items created
    Level 9 (2000 points):  30% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            40% Bouncy Balls - 100% fast speed
                            Double the number of items created
    Level 10 (3000 points): 30% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            40% Bouncy Balls - 50% fast speed, 50% super-fast speed
                            Double the number of items created
    */
}
