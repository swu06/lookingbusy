package com.example.lookingdynamic.lookingbusy.gameobjects;

import com.example.lookingdynamic.lookingbusy.gameplay.Level;

import java.util.Random;

/**
 * Created by swu on 9/6/2015.
 */
public class PoppableObjectFactory {

    private static Random rand = new Random();
    private static int SLOW_SPEED = 2;
    private static int MEDIUM_SPEED = 4;
    private static int FAST_SPEED = 8;
    private static int SUPER_FAST_SPEED = 10;

    public static boolean shouldCreateObject(Level level) {
        boolean createObject = false;
        int chanceOfReturningSomething = rand.nextInt(100);

        if (chanceOfReturningSomething < level.getPercentChanceOfCreation()) {
            createObject = true;
        }

        return createObject;
    }

    /*

    Level 0 (Relaxing):     40% Balloons - 50% at medium speed, 25% slow speed, 25% fast speed
                            40% Water Droplets - 50% medium speed, 25% slow speed, 25% fast speed
                            20% Bouncy Balls - 50% medium speed, 25% slow speed, 25% fast speed
                            Super-fast speed goes away
    Level 1 (100 points):     All Balloons, all slow speed
    Level 2 (200 points):    100% Balloons - 50% at medium speed, 50% slow speed
    Level 3 (300 points):   70% Balloons - 50% at medium speed, 50% slow speed
                            30% Water Droplets - 100% medium speed
    Level 4 (400 points):   70% Balloons - 50% at medium speed, 50% slow speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
    Level 5 (500 points):   50% Balloons - 50% at medium speed, 50% slow speed
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
    public static PoppableObject generatePoppableObject(Level level, int width, int height) {

        PoppableObject toReturn = null;

        boolean createANewObject = true;

        createANewObject = shouldCreateObject(level);

        // If we aren't going to make a new object, then just exit
        if (createANewObject) {
            toReturn = createObject(level, width, height);
        }

        return toReturn;
    }

    public static PoppableObject createObject(Level level, int width, int height) {
        PoppableObject toReturn = null;
        int randomType = rand.nextInt(100);
        int randomSpeed = rand.nextInt(100);
        int randomLocation = rand.nextInt(9) + 1;
        int oneInFour = rand.nextInt(4);

        if(randomType < level.getBalloonPercentCreated()) {
            int speed;
            if(randomSpeed < level.getBalloonPercentSlow()) {
                speed = SLOW_SPEED;
            } else if(randomSpeed < level.getBalloonPercentSlow() + level.getBalloonPercentMedium()) {
                speed = MEDIUM_SPEED;
            } else if(randomSpeed < level.getBalloonPercentSlow() + level.getBalloonPercentMedium()
                                    + level.getBalloonPercentFast()) {
                speed = FAST_SPEED;
            } else {
                speed = SUPER_FAST_SPEED;
            }
            toReturn = new Balloon(width * randomLocation / 10, height, -1 * speed, oneInFour);

        } else if(randomType < level.getBalloonPercentCreated() + level.getDropletPercentCreated()){
            int speed;
            if(randomSpeed < level.getDropletPercentSlow()) {
                speed = SLOW_SPEED;
            } else if(randomSpeed < level.getDropletPercentSlow() + level.getDropletPercentMedium()) {
                speed = MEDIUM_SPEED;
            } else if(randomSpeed < level.getDropletPercentSlow() + level.getDropletPercentMedium()
                    + level.getDropletPercentFast()) {
                speed = FAST_SPEED;
            } else {
                speed = SUPER_FAST_SPEED;
            }
            toReturn = new Droplet(width * randomLocation / 10, speed);
        } else {
            int speed;
            if(randomSpeed < level.getBallPercentSlow()) {
                speed = SLOW_SPEED;
            } else if(randomSpeed < level.getBallPercentSlow() + level.getBallPercentMedium()) {
                speed = MEDIUM_SPEED;
            } else if(randomSpeed < level.getBallPercentSlow() + level.getBallPercentMedium()
                    + level.getBallPercentFast()) {
                speed = FAST_SPEED;
            } else {
                speed = SUPER_FAST_SPEED;
            }


            if(oneInFour == 0) {
                toReturn = new Ball(0, 0, 2 * speed, speed);
            } else if(oneInFour == 1) {
                toReturn = new Ball(width, 0, -2 * speed, speed);
            } else if(oneInFour == 2) {
                toReturn = new Ball(0, height, -2 * speed, -1 * speed);
            } else {
                toReturn = new Ball(width, height, -2 * speed, -1 * speed);
            }
        }

        return toReturn;
    }

}
