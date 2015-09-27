package com.example.lookingdynamic.lookingbusy.gameobjects;

import com.example.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.example.lookingdynamic.lookingbusy.gameplay.GameplayMode;
import com.example.lookingdynamic.lookingbusy.gameplay.Level;
import com.example.lookingdynamic.lookingbusy.gameplay.ThemeManager;

import java.util.Random;

/**
 * Created by swu on 9/6/2015.
 */
public class PoppableObjectFactory {

    private static Random rand = new Random();
    private static int SLOW_SPEED = 2;
    private static int MEDIUM_SPEED = 4;
    private static int FAST_SPEED = 8;
    private static int SUPER_FAST_SPEED = 9;

    public static boolean shouldCreateObject(GameplayManager level) {
        boolean createObject = false;
        int chanceOfReturningSomething = rand.nextInt(100);

        if (chanceOfReturningSomething < level.getPercentChanceOfCreation()) {
            createObject = true;
        }

        return createObject;
    }

    public static PoppableObject generatePoppableObject(GameplayManager gameplay, int width, int height, boolean randomBotActivated) {

        PoppableObject toReturn = null;

        // If we aren't going to make a new object, then just exit
        if (shouldCreateObject(gameplay)) {
            toReturn = createObject(gameplay, width, height, randomBotActivated);
        }

        return toReturn;
    }

    public static PoppableObject createObject(GameplayManager gameplay, int width, int height, boolean randomBotActivated) {
        PoppableObject toReturn = null;
        int randomType = rand.nextInt(100);
        int randomSpeed = rand.nextInt(100);
        int randomLocation = rand.nextInt(9) + 1;
        int oneInFour = rand.nextInt(4);

        if(randomType < gameplay.getBalloonPercentCreated()) {
            int speed;
            if(randomSpeed < gameplay.getBalloonPercentSlow()) {
                speed = SLOW_SPEED;
            } else if(randomSpeed < gameplay.getBalloonPercentSlow() + gameplay.getBalloonPercentMedium()) {
                speed = MEDIUM_SPEED;
            } else if(randomSpeed < gameplay.getBalloonPercentSlow() + gameplay.getBalloonPercentMedium()
                                    + gameplay.getBalloonPercentFast()) {
                speed = FAST_SPEED;
            } else {
                speed = SUPER_FAST_SPEED;
            }
            toReturn = new Balloon(width * randomLocation / 10, height, -1 * speed, oneInFour);

        } else if(randomType < gameplay.getBalloonPercentCreated() + gameplay.getDropletPercentCreated()){
            int speed;
            if(randomSpeed < gameplay.getDropletPercentSlow()) {
                speed = SLOW_SPEED;
            } else if(randomSpeed < gameplay.getDropletPercentSlow() + gameplay.getDropletPercentMedium()) {
                speed = MEDIUM_SPEED;
            } else if(randomSpeed < gameplay.getDropletPercentSlow() + gameplay.getDropletPercentMedium()
                    + gameplay.getDropletPercentFast()) {
                speed = FAST_SPEED;
            } else {
                speed = SUPER_FAST_SPEED;
            }
            toReturn = new Droplet(width * randomLocation / 10, speed);
        } else if(randomType < gameplay.getBalloonPercentCreated()
                                + gameplay.getDropletPercentCreated()
                                + gameplay.getBallPercentCreated()){
            int speed;
            if(randomSpeed < gameplay.getBallPercentSlow()) {
                speed = SLOW_SPEED;
            } else if(randomSpeed < gameplay.getBallPercentSlow() + gameplay.getBallPercentMedium()) {
                speed = MEDIUM_SPEED;
            } else if(randomSpeed < gameplay.getBallPercentSlow() + gameplay.getBallPercentMedium()
                    + gameplay.getBallPercentFast()) {
                speed = FAST_SPEED;
            } else {
                speed = SUPER_FAST_SPEED;
            }

            toReturn = new Ball(width, height, oneInFour, speed);
        } else if(randomBotActivated){
            int speed;
            if(randomSpeed < gameplay.getRandomBotPercentSlow()) {
                speed = SLOW_SPEED;
            } else if(randomSpeed < gameplay.getRandomBotPercentSlow() + gameplay.getRandomBotPercentMedium()) {
                speed = MEDIUM_SPEED;
            } else if(randomSpeed < gameplay.getRandomBotPercentSlow() + gameplay.getRandomBotPercentMedium()
                    + gameplay.getRandomBotPercentFast()) {
                speed = FAST_SPEED;
            } else {
                speed = SUPER_FAST_SPEED;
            }

            toReturn = new RandomBot(width, height, speed);
        }

        return toReturn;
    }

}
