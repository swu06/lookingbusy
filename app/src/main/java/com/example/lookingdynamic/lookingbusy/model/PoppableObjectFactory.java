package com.example.lookingdynamic.lookingbusy.model;

import android.content.res.Resources;

import com.example.lookingdynamic.lookingbusy.themes.GameTheme;

import java.util.Random;

/**
 * Created by swu on 9/6/2015.
 */
public class PoppableObjectFactory {

    private static Random rand = new Random();

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
    public static PoppableObject generatePoppableObject(GameTheme theme, int width, int height) {

        PoppableObject toReturn = null;

        int chancOfReturningSomething = rand.nextInt(10);
        if(chancOfReturningSomething != 1 ) return toReturn;

        int randomType = rand.nextInt(10);
        int randomLocation = rand.nextInt(9) + 1;
        int randomSpeed = rand.nextInt(10) + 20;


        if (randomType < 5) {
            toReturn = new Balloon(theme, width * randomLocation / 10, height, -2 * randomSpeed);
        } else if (randomType < 7) {
            toReturn = new Droplet(theme, width * randomLocation / 10, randomSpeed);
        } else if (randomType == 8) {
            randomType = rand.nextInt(4);
            if (randomType == 0) {
                toReturn = new Ball(theme, 0, 0, 2 * randomSpeed, randomSpeed);
            } else if (randomType == 1) {
                toReturn = new Ball(theme, width, 0, -2 * randomSpeed, randomSpeed);
            } else if (randomType == 2) {
                toReturn = new Ball(theme, 0, width, 2 * randomSpeed, -1 * randomSpeed);
            } else if (randomType == 3) {
                toReturn = new Ball(theme, width, height, -2 * randomSpeed, -1 * randomSpeed);
            }
        } // Do nothing 1/10 times

        return toReturn;
    }
}
