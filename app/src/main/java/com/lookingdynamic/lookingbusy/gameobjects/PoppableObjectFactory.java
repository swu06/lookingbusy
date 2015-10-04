package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.lookingdynamic.lookingbusy.gameplay.GameplayMode;
import com.lookingdynamic.lookingbusy.gameplay.Level;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

import java.util.Random;
import java.util.Vector;

/**
 * Created by swu on 9/6/2015.
 */
public class PoppableObjectFactory {

    private static final String LOGGER = Bubble.class.getSimpleName();
    private static Random rand = new Random();
    private static int SLOW_SPEED = 2;
    private static int MEDIUM_SPEED = 4;
    private static int FAST_SPEED = 8;
    private static int SUPER_FAST_SPEED = 9;

    public static boolean shouldCreateObject(GameplayManager level, int currentObjectCount) {
        boolean createObject = false;
        int chanceOfReturningSomething = rand.nextInt(100);

        if (chanceOfReturningSomething < level.getPercentChanceOfCreation()
                && level.shouldMakeObject(currentObjectCount)) {
            createObject = true;
        }

        return createObject;
    }

    public static Vector<PoppableObject> generatePoppableObject(ThemeManager theme, GameplayManager gameplay, int currentObjectCount, int width, int height, boolean randomBotActivated) {

        Vector<PoppableObject> toReturn = null;

        if(gameplay.isBubbleGrid() && currentObjectCount == 0) {
            toReturn = newGridOfBubbles(theme, width, height);
        }
        else if (!gameplay.isBubbleGrid() && shouldCreateObject(gameplay, currentObjectCount)) {
            PoppableObject object = createObject(gameplay, width, height, randomBotActivated);
            if(object != null) {
                toReturn = new Vector<PoppableObject>();
                toReturn.add(object);
            }
        }

        if(toReturn != null) {
            gameplay.makeObject();
        }

        return toReturn;
    }

    private static Vector<PoppableObject> newGridOfBubbles(ThemeManager theme, int screenWidth, int screenHeight) {
        Bitmap bubbleImage = theme.getBubble();
        int topMargin = 0;
        int remainingScreenHeight = screenHeight - topMargin;
        int imageWidth = bubbleImage.getWidth();
        int imageHeight = bubbleImage.getHeight();

        int numberOfBubblesWide = screenWidth / imageWidth;
        int leftBorderWidth = (screenWidth - (numberOfBubblesWide * imageWidth)) / 2;

        int numberOfBubblesHigh = remainingScreenHeight / imageHeight;
        int topBorderHeight = (remainingScreenHeight - (numberOfBubblesHigh * imageHeight)) / 2;

        Vector<PoppableObject> toReturn = new Vector<PoppableObject>();
        int yLocation = topMargin + topBorderHeight;
        for(int y=0; y < numberOfBubblesHigh; y++) {
            int xLocation = leftBorderWidth;
            for(int x=0; x < numberOfBubblesWide; x++ ) {
                toReturn.add(new Bubble(xLocation, yLocation, numberOfBubblesHigh * numberOfBubblesWide));
                xLocation = xLocation + imageWidth;
            }
            yLocation = yLocation + imageHeight;
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
