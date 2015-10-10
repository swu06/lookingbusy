package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.BitmapFactory;
import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.R;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 * This Test class tests the methods within Ball, and the methods implemented in the
 * PoppableObject abstract parent class.
 * Created by swu on 9/5/2015.
 */
public class RandomBotTest extends ActivityTestCase {

    public ThemeManager theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());
        theme.setRandomBotImage(BitmapFactory.decodeResource(getInstrumentation().getTargetContext().getResources(), R.drawable.bright_ball));
    }

    public void testCreateRandomBot(){
        RandomBot testBot = new RandomBot(200, 200, 1);

        assertNotNull("Test Failure: RandomBot object is null", testBot);
        assertTrue("Test Failure: RandomBot did not spawn near the middle.  Instead it spawned at " + testBot.xCoordinate,
                50 <= testBot.xCoordinate && testBot.xCoordinate <= 150);
        assertTrue("Test Failure: RandomBot did not spawn near the middle.  Instead it spawned at " + testBot.yCoordinate,
                        50 <= testBot.yCoordinate && testBot.yCoordinate <= 150);
        assertFalse("Test Failure: RandomBot spawned in the popped state. This is not expected.", testBot.popped);
        assertFalse("Test Failure: RandomBot spawned in the offScreen state. This is not expected.", testBot.offScreen);
        assertEquals("Test Failure: RandomBot should spawn ready to move.", 0, testBot.consecutiveMovesRemaining);
        assertEquals("Test Failure: RandomBot speed did not initialize correctly", 1, testBot.speed);
    }

    public void testGetImage() {
        RandomBot testBot = new RandomBot(200, 200, 1);

        assertTrue("Test Failure: RandomBot Image is not consistent with theme",
                theme.getRandomBot().equals(testBot.getImage(theme)));
    }

    public void testGetImageWhenPopped() {
        RandomBot testBot = new RandomBot(200, 200, 1);

        testBot.popped = true;

        assertTrue("Test Failure: Popped Image is not consistent with theme",
                theme.getPoppedRandomBot().equals(testBot.getImage(theme)));
    }

    public void testMovesWithConsecutiveMovesRemaining() {
        RandomBot testBot = new RandomBot(1000, 1000, 1);
        testBot.xVelocity = 1;
        testBot.yVelocity = 1;
        testBot.consecutiveMovesRemaining = 1;

        int oldXCoordinate = testBot.xCoordinate;
        int oldYCoordinate = testBot.yCoordinate;
        testBot.move(theme, 1000, 1000);

        assertEquals("Test Failure: RandomBot should move consistently when there are consecutive moves remaining",
                oldXCoordinate + 1, testBot.xCoordinate);
        assertEquals("Test Failure: RandomBot should move consistently when there are consecutive moves remaining",
                oldYCoordinate + 1, testBot.yCoordinate);
        assertEquals("Test Failure: RandomBot should decrement with each consecutive move",
                0, testBot.consecutiveMovesRemaining);
    }

    public void testMovesWithoutConsecutiveMovesRemaining() {
        RandomBot testBot = new RandomBot(1000, 1000, 1);
        testBot.consecutiveMovesRemaining = 1;

        int oldXCoordinate = testBot.xCoordinate;
        int oldYCoordinate = testBot.yCoordinate;
        testBot.move(theme, 1000, 1000);

        assertTrue("Test Failure: New random xVelocity should be between -1 and 1",
                -1 <= testBot.xVelocity && testBot.xVelocity <= 1);
        assertTrue("Test Failure: New random yVelocity should be between -1 and 1",
                -1 <= testBot.yVelocity && testBot.yVelocity <= 1);
        assertTrue("Test Failure: New random consecutiveMovesRemaining should be between 1 and 49",
                1 <= testBot.consecutiveMovesRemaining && testBot.consecutiveMovesRemaining <= 49);
        assertEquals("Test Failure: RandomBot should move consistently based on velocity",
                oldXCoordinate + testBot.xVelocity, testBot.xCoordinate);
        assertEquals("Test Failure: RandomBot should move consistently based on velocity",
                oldYCoordinate + testBot.yVelocity, testBot.yCoordinate);
    }

    public void testMovesWithZeroSpeed() {
        RandomBot testBot = new RandomBot(1000, 1000, 1);
        testBot.xVelocity = 0;
        testBot.yVelocity = 0;
        testBot.consecutiveMovesRemaining = 1;

        int oldXCoordinate = testBot.xCoordinate;
        int oldYCoordinate = testBot.yCoordinate;
        testBot.move(theme, 1000, 1000);

        assertTrue("Test Failure: New random xVelocity should be between -1 and 1",
                -1 <= testBot.xVelocity && testBot.xVelocity <= 1);
        assertTrue("Test Failure: New random yVelocity should be between -1 and 1",
                -1 <= testBot.yVelocity && testBot.yVelocity <= 1);
        assertTrue("Test Failure: New random consecutiveMovesRemaining should be between 1 and 49",
                1 <= testBot.consecutiveMovesRemaining && testBot.consecutiveMovesRemaining <= 49);
        assertEquals("Test Failure: RandomBot should move consistently based on velocity",
                oldXCoordinate + testBot.xVelocity, testBot.xCoordinate);
        assertEquals("Test Failure: RandomBot should move consistently based on velocity",
                oldYCoordinate + testBot.yVelocity, testBot.yCoordinate);
    }

    public void testMoveBounceOffLeft() {
        RandomBot testBot = new RandomBot(1000, 1000, 1);
        testBot.xVelocity = -1;
        testBot.yVelocity = 1;
        testBot.consecutiveMovesRemaining = 1;

        testBot.xCoordinate = 0;
        int oldYCoordinate = testBot.yCoordinate;

        testBot.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate not in line with a bounce off the wall",
                0, testBot.xCoordinate);
        assertEquals("Test Failure: yCoordinate not in line with a bounce off the wall",
                oldYCoordinate + 1, testBot.yCoordinate);
        assertEquals("Test Failure: xVelocity not in line with a bounce off the wall",
                1, testBot.xVelocity);
        assertEquals("Test Failure: yVelocity not in line with a bounce off the wall",
                1, testBot.yVelocity);
        assertFalse("Test Failure: RandomBot should not pop when it bounces off a wall", testBot.popped);
        assertFalse("Test Failure: RandomBot should not go offscreen when it bounces off a wall", testBot.offScreen);
    }

    public void testMoveBounceOffRight() {
        RandomBot testBot = new RandomBot(1000, 1000, 1);
        testBot.xVelocity = 1;
        testBot.yVelocity = 1;
        testBot.consecutiveMovesRemaining = 1;

        int imageWidth = testBot.getImage(theme).getWidth();

        int wallLocation = 1000 + imageWidth;

        testBot.xCoordinate = wallLocation;
        int oldYCoordinate = testBot.yCoordinate;

        testBot.move(theme, wallLocation, 1000);

        assertEquals("Test Failure: xCoordinate not in line with a bounce off the wall",
                wallLocation - imageWidth, testBot.xCoordinate);
        assertEquals("Test Failure: yCoordinate not in line with a bounce off the wall",
                oldYCoordinate + 1, testBot.yCoordinate);
        assertEquals("Test Failure: xVelocity not in line with a bounce off the wall",
                -1, testBot.xVelocity);
        assertEquals("Test Failure: yVelocity not in line with a bounce off the wall",
                1, testBot.yVelocity);
        assertFalse("Test Failure: RandomBot should not pop when it bounces off a wall", testBot.popped);
        assertFalse("Test Failure: RandomBot should not go offscreen when it bounces off a wall", testBot.offScreen);
    }

    public void testMoveFallOffTop() {
        RandomBot testBot = new RandomBot(1000, 1000, 1);
        testBot.yVelocity = -1;
        testBot.consecutiveMovesRemaining = 1;

        int imageWidth = testBot.getImage(theme).getWidth();
        int imageHeight = testBot.getImage(theme).getHeight();

        testBot.yCoordinate = 0 - imageHeight;

        testBot.move(theme, imageWidth + 4, imageHeight + 4);

        assertFalse("Test Failure: RandomBot should not pop when it falls offscreen", testBot.popped);
        assertTrue("Test Failure: RandomBot is not being set to offscreen correctly", testBot.offScreen);
    }

    public void testMoveFallOffBottom() {
        RandomBot testBot = new RandomBot(1000, 1000, 1);
        testBot.yVelocity = 1;
        testBot.consecutiveMovesRemaining = 1;

        testBot.yCoordinate = 1000;

        int imageWidth = testBot.getImage(theme).getWidth();

        testBot.move(theme, imageWidth + 4, testBot.yCoordinate);

        assertFalse("Test Failure: RandomBot should not pop when it falls offscreen", testBot.popped);
        assertTrue("Test Failure: RandomBot is not being set to offscreen correctly", testBot.offScreen);
    }

}
