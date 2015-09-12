package com.example.lookingdynamic.lookingbusy.gameplay;

import com.example.lookingdynamic.lookingbusy.gameobjects.Ball;
import com.example.lookingdynamic.lookingbusy.gameobjects.Balloon;
import com.example.lookingdynamic.lookingbusy.gameobjects.Droplet;
import com.example.lookingdynamic.lookingbusy.gameobjects.PoppableObject;
import com.example.lookingdynamic.lookingbusy.themes.GameTheme;

import java.util.Random;

/**
 * Created by swu on 9/12/2015.
 */
public class LevelingGuide {
    private static Random rand = new Random();
    private static int SLOW_SPEED = 10;
    private static int MEDIUM_SPEED = 20;
    private static int FAST_SPEED = 30;
    private static int SUPER_FAST_SPEED = 40;

    public static boolean shouldCreateObject(int level){
        boolean createObject = false;
        int chanceOfReturningSomething = rand.nextInt(10);
        // Early levels have a 1 in 10 chance of making something new
        // Later levels have a 2in 10 chance of making something new
        if(level < 7 && chanceOfReturningSomething < 1 ||
                level >= 7 && chanceOfReturningSomething <= 1) {
            createObject = true;
        }

        return createObject;
    }

    public static PoppableObject generateNewObject(GameTheme theme, int level, int width, int height){
        PoppableObject toReturn = null;
        int randomType = rand.nextInt(10);
        int randomLocation = rand.nextInt(9) + 1;
        int randomSpeed = rand.nextInt(4);


        if(level == 0) {
        /*
         * Level 0 (Relaxing):  40% Balloons - 50% at medium speed, 25% slow speed, 25% fast speed
         *                      40% Water Droplets - 50% medium speed, 25% slow speed, 25% fast speed
         *                      20% Bouncy Balls - 50% medium speed, 25% slow speed, 25% fast speed
         */
            // Get Speed First: 50% at medium speed, 25% slow speed, 25% fast speed
            int speed = MEDIUM_SPEED;
            switch(randomSpeed) {
                case 0:
                    speed = SLOW_SPEED;
                    break;
                case 1:
                case 2:
                    speed = MEDIUM_SPEED;
                    break;
                case 3:
                    speed = FAST_SPEED;
                    break;
            }

            // The create the random object 30% Balloons, 30% Droplets, 40%
            switch(randomType) {
                case 0:
                case 1:
                case 2:
                case 3:
                    toReturn = new Balloon(theme, width * randomLocation / 10, height, -2 * speed);
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    toReturn = new Droplet(theme, width * randomLocation / 10, speed);
                    break;
                case 8:
                    toReturn = new Ball(theme, 0, 0, 2 * speed, speed);
                    break;
                case 9:
                    toReturn = new Ball(theme, width, 0, -2 * speed, speed);
                    break;
            }
        } else if(level == 1) {
            // Level 1 (100 points):    All Balloons, all slow speed
            toReturn = new Balloon(theme, width * randomLocation / 10, height, -2 * SLOW_SPEED);
        } else if(level == 2) {
            //Level 2 (200 points):    100% Balloons - 50% at medium speed, 50% slow speed
            int speed = MEDIUM_SPEED;
            switch(randomSpeed) {
                case 0:
                case 1:
                    speed = SLOW_SPEED;
                    break;
                case 2:
                case 3:
                    speed = MEDIUM_SPEED;
                    break;
            }
            toReturn = new Balloon(theme, width * randomLocation / 10, height, -2 * speed);
        }

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
