package com.lookingdynamic.lookingbusy;

import android.graphics.Bitmap;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameobjects.Ball;
import com.lookingdynamic.lookingbusy.gameobjects.Balloon;
import com.lookingdynamic.lookingbusy.gameobjects.Bubble;
import com.lookingdynamic.lookingbusy.gameobjects.Droplet;
import com.lookingdynamic.lookingbusy.gameobjects.PoppableObject;
import com.lookingdynamic.lookingbusy.gameobjects.RandomBot;
import com.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

import java.util.Random;
import java.util.Vector;

/**
 * This class creates all Poppable Objects for use in the game.  It uses Random Number Generators,
 * Level Definitions, and other information to determine if new objects should be created and what
 * those new objects should be.
 * Created by swu on 9/6/2015.
 */
public class PoppableObjectFactory {

    private static final String LOGGER = Bubble.class.getSimpleName();
    private static Random rand = new Random();

    /*
     * This is the entry point of this class. It takes in a set of objects that will help it
     * determine if new objects should be created, creates them if necessary, and returns a
     * vector of these objects, or null.
     */
    public static Vector<PoppableObject> generatePoppableObject(ThemeManager theme, GameplayManager gameplay, int currentObjectCount, int width, int height, boolean randomBotActivated) {
        Vector<PoppableObject> toReturn = null;

        // Bubble Grids are a special case. They make all of their objects once at the beginning.
        if (gameplay.isBubbleGrid() && currentObjectCount == 0) {
            toReturn = newGridOfBubbles(theme, width, height);
            Log.v(LOGGER, "A new Bubble Grid worth of objects were created");
        // Otherwise, a helper method is used to create just one object at a time
        } else if (!gameplay.isBubbleGrid() && shouldCreateObject(gameplay, currentObjectCount)) {
            PoppableObject object = createObject(gameplay, width, height, randomBotActivated);
            if (object != null) {
                toReturn = new Vector<>();
                toReturn.add(object);
                Log.v(LOGGER, "A new object was created");
            }
        }

        if (toReturn != null) {
            gameplay.makeObject();
        }

        return toReturn;
    }

    /*
     * This helper method uses settings from the level definition to determine if a new object
     * should be create or not.  This can come in the form of a random chance of creating a new
     * object and an level maximum/minimum on number of objects to be in play at any time. For
     * example, during a boss level, no new objects should be created until objects from the
     * previous level clear out.
     */
    private static boolean shouldCreateObject(GameplayManager level, int currentObjectCount) {
        boolean createObject = false;
        int chanceOfReturningSomething = rand.nextInt(100);

        if (chanceOfReturningSomething < level.getPercentChanceOfCreation()
                && level.shouldMakeObject(currentObjectCount)) {
            createObject = true;
        }

        return createObject;
    }

    /*
     *  This method creates a full grid of bubbles.  These bubbles are never truly popped, so this
     *  is the only place they are created.
     */
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

        Vector<PoppableObject> toReturn = new Vector<>();
        int yLocation = topMargin + topBorderHeight;
        for (int y=0; y < numberOfBubblesHigh; y++) {
            int xLocation = leftBorderWidth;
            for (int x=0; x < numberOfBubblesWide; x++) {
                toReturn.add(new Bubble(xLocation, yLocation, numberOfBubblesHigh * numberOfBubblesWide));
                xLocation = xLocation + imageWidth;
            }
            yLocation = yLocation + imageHeight;
        }

        return toReturn;
    }

    /*
     * This method creates the objects for all of the other gameplay types.  It uses random numbers
     * to determine which object to create as well as the settings for each object.
     */
    private static PoppableObject createObject(GameplayManager gameplay, int width, int height, boolean randomBotActivated) {
        int randomType = rand.nextInt(100);
        int randomSpeed = rand.nextInt(100);
        int randomWidth = rand.nextInt(width - 200);
        int oneInFour = rand.nextInt(4);

        int speed;
        PoppableObject toReturn;

        if (randomType < gameplay.getBalloonPercentCreated()) {

            if (randomSpeed < gameplay.getBalloonPercentSlow()) {
                speed = getSlowSpeed(height);
            } else if (randomSpeed < gameplay.getBalloonPercentSlow() + gameplay.getBalloonPercentMedium()) {
                speed = getMediumSpeed(height);
            } else if (randomSpeed < gameplay.getBalloonPercentSlow() + gameplay.getBalloonPercentMedium()
                    + gameplay.getBalloonPercentFast()) {
                speed = getFastSpeed(height);
            } else {
                speed = getSuperFastSpeed(height);
            }

            toReturn = new Balloon(randomWidth, height, -1 * speed, oneInFour);
        } else if (randomType < gameplay.getBalloonPercentCreated() + gameplay.getDropletPercentCreated()) {
            if (randomSpeed < gameplay.getDropletPercentSlow()) {
                speed = getSlowSpeed(height);
            } else if (randomSpeed < gameplay.getDropletPercentSlow() + gameplay.getDropletPercentMedium()) {
                speed = getMediumSpeed(height);
            } else if (randomSpeed < gameplay.getDropletPercentSlow() + gameplay.getDropletPercentMedium()
                    + gameplay.getDropletPercentFast()) {
                speed = getFastSpeed(height);
            } else {
                speed = getSuperFastSpeed(height);
            }
            toReturn = new Droplet(randomWidth, speed);
        } else if (randomType < gameplay.getBalloonPercentCreated()
                                + gameplay.getDropletPercentCreated()
                                + gameplay.getBallPercentCreated()){
            if (randomSpeed < gameplay.getBallPercentSlow()) {
                speed = getSlowSpeed(height);
            } else if (randomSpeed < gameplay.getBallPercentSlow() + gameplay.getBallPercentMedium()) {
                speed = getMediumSpeed(height);
            } else if (randomSpeed < gameplay.getBallPercentSlow() + gameplay.getBallPercentMedium()
                    + gameplay.getBallPercentFast()) {
                speed = getFastSpeed(height);
            } else {
                speed = getSuperFastSpeed(height);
            }

            toReturn = new Ball(width, height, oneInFour, speed);
        } else if (randomBotActivated){
            if (randomSpeed < gameplay.getRandomBotPercentSlow()) {
                speed = getSlowSpeed(height);
            } else if (randomSpeed < gameplay.getRandomBotPercentSlow() + gameplay.getRandomBotPercentMedium()) {
                speed = getMediumSpeed(height);
            } else if (randomSpeed < gameplay.getRandomBotPercentSlow() + gameplay.getRandomBotPercentMedium()
                    + gameplay.getRandomBotPercentFast()) {
                speed = getFastSpeed(height);
            } else {
                speed = getSuperFastSpeed(height);
            }

            toReturn = new RandomBot(width, height, speed);
        } else {
            // Default to a Balloon if RandomBot is not active  (but use the speed from the RandomBot)
            if (randomSpeed < gameplay.getRandomBotPercentSlow()) {
                speed = getSlowSpeed(height);
            } else if (randomSpeed < gameplay.getRandomBotPercentSlow() + gameplay.getRandomBotPercentMedium()) {
                speed = getMediumSpeed(height);
            } else if (randomSpeed < gameplay.getRandomBotPercentSlow() + gameplay.getRandomBotPercentMedium()
                    + gameplay.getRandomBotPercentFast()) {
                speed = getFastSpeed(height);
            } else {
                speed = getSuperFastSpeed(height);
            }

            toReturn = new Balloon(randomWidth, height, -1 * speed, oneInFour);
        }

        return toReturn;
    }

    private static int getSlowSpeed(int height) {
        return (int) (height * 0.003);
    }

    private static int getMediumSpeed(int height) {
        return (int) (height * 0.006);
    }

    private static int getFastSpeed(int height) {
        return (int) (height * 0.009);
    }

    private static int getSuperFastSpeed(int height) {
        return (int) (height * 0.011);
    }
}
