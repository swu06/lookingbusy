package com.lookingdynamic.lookingbusy.gameobjects;

import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 * This Test class tests the methods within Ball, and the methods implemented in the
 * PoppableObject abstract parent class.
 * Created by swu on 9/5/2015.
 */
public class BallTest extends ActivityTestCase {

    public ThemeManager theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());
    }

    public void testCreateBall(){
        Ball testBall = new Ball(0, 0, Ball.TOP_LEFT_START, 0);

        assertNotNull("Test Failure: Ball object is null", testBall);
        assertEquals("Test Failure: xCoordinate incorrect on initialization", 0, testBall.xCoordinate);
        assertEquals("Test Failure: yCoordinate incorrect on initialization", 0, testBall.yCoordinate);
        assertEquals("Test Failure: xVelocity incorrect on initialization", 0, testBall.xVelocity);
        assertEquals("Test Failure: yVelocity incorrect on initialization", 0, testBall.yVelocity);
        assertFalse("Test Failure: Ball should not be popped on initialization", testBall.popped);
        assertFalse("Test Failure: Ball should not be offscreen on initialization", testBall.offScreen);
    }

    public void testMoveTopLeft() {
        Ball testBall = new Ball(1000, 1000, Ball.TOP_LEFT_START, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate does not match expected value after move", 2, testBall.xCoordinate);
        assertEquals("Test Failure: yCoordinate does not match expected value after move", 1, testBall.yCoordinate);
        assertEquals("Test Failure: xVelocity does not match expected value after move", 2, testBall.xVelocity);
        assertEquals("Test Failure: yVelocity does not match expected value after move", 1, testBall.yVelocity);
        assertFalse("Test Failure: Ball should not pop when it moves", testBall.popped);
        assertFalse("Test Failure: Ball should not go offscreen when it moves", testBall.offScreen);
    }

    public void testMoveTopRight() {
        Ball testBall = new Ball(1000, 1000, Ball.TOP_RIGHT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        testBall.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate does not match expected value after move", 1000 - imageWidth, testBall.xCoordinate);
        assertEquals("Test Failure: yCoordinate does not match expected value after move", 1, testBall.yCoordinate);
        assertEquals("Test Failure: xVelocity does not match expected value after move", -2, testBall.xVelocity);
        assertEquals("Test Failure: yVelocity does not match expected value after move", 1, testBall.yVelocity);
        assertFalse("Test Failure: Ball should not pop when it moves", testBall.popped);
        assertFalse("Test Failure: Ball should not go offscreen when it moves", testBall.offScreen);
    }

    public void testMoveBottomRight() {
        Ball testBall = new Ball(1000, 1000, Ball.BOTTOM_RIGHT_START, 1);
        int imageWidth = testBall.getImage(theme).getWidth();

        testBall.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate does not match expected value after move", 1000 - imageWidth, testBall.xCoordinate);
        assertEquals("Test Failure: yCoordinate does not match expected value after move", 999, testBall.yCoordinate);
        assertEquals("Test Failure: xVelocity does not match expected value after move", -2, testBall.xVelocity);
        assertEquals("Test Failure: yVelocity does not match expected value after move", -1, testBall.yVelocity);
        assertFalse("Test Failure: Ball should not pop when it moves", testBall.popped);
        assertFalse("Test Failure: Ball should not go offscreen when it moves", testBall.offScreen);
    }

    public void testMoveBottomLeft() {
        Ball testBall = new Ball(1000, 1000, Ball.BOTTOM_LEFT_START, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate does not match expected value after move", 2, testBall.xCoordinate);
        assertEquals("Test Failure: yCoordinate does not match expected value after move", 999, testBall.yCoordinate);
        assertEquals("Test Failure: xVelocity does not match expected value after move", 2, testBall.xVelocity);
        assertEquals("Test Failure: yVelocity does not match expected value after move", -1, testBall.yVelocity);
        assertFalse("Test Failure: Ball should not pop when it moves", testBall.popped);
        assertFalse("Test Failure: Ball should not go offscreen when it moves", testBall.offScreen);
    }

    public void testMoveBounceOffLeft() {
        Ball testBall = new Ball(1000, 1000, Ball.TOP_LEFT_START, 1);

        testBall.xCoordinate = 0;
        testBall.xVelocity = -1;

        testBall.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate not in line with a bounce off the wall",
                0, testBall.xCoordinate);
        assertEquals("Test Failure: yCoordinate not in line with a bounce off the wall",
                1, testBall.yCoordinate);
        assertEquals("Test Failure: xVelocity not in line with a bounce off the wall",
                1, testBall.xVelocity);
        assertEquals("Test Failure: yVelocity not in line with a bounce off the wall",
                1, testBall.yVelocity);
        assertFalse("Test Failure: Ball should not pop when it bounces off a wall", testBall.popped);
        assertFalse("Test Failure: Ball should not go offscreen when it bounces off a wall", testBall.offScreen);
    }

    public void testMoveBounceOffRight() {
        Ball testBall = new Ball(100, 0, Ball.TOP_LEFT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        int wallLocation = 100 + imageWidth;

        testBall.xCoordinate = wallLocation;
        testBall.xVelocity = 1;

        testBall.move(theme, wallLocation, imageHeight + 2);

        assertEquals("Test Failure: xCoordinate not in line with a bounce off the wall",
                        wallLocation - imageWidth, testBall.xCoordinate);
        assertEquals("Test Failure: yCoordinate not in line with a bounce off the wall",
                        1, testBall.yCoordinate);
        assertEquals("Test Failure: xVelocity not in line with a bounce off the wall",
                        -1, testBall.xVelocity);
        assertEquals("Test Failure: yVelocity not in line with a bounce off the wall",
                        1, testBall.yVelocity);
        assertFalse("Test Failure: Ball should not pop when it bounces off a wall", testBall.popped);
        assertFalse("Test Failure: Ball should not go offscreen when it bounces off a wall", testBall.offScreen);
    }

    public void testMoveFallOffTop() {
        Ball testBall = new Ball(100, 0, Ball.TOP_LEFT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        testBall.yCoordinate = 0 - imageHeight;
        testBall.yVelocity = -1;

        testBall.move(theme, imageWidth + 4, imageHeight + 4);

        assertFalse("Test Failure: Ball should not pop when it falls offscreen", testBall.popped);
        assertTrue("Test Failure: Ball is not being set to offscreen correctly", testBall.offScreen);
    }

    public void testMoveFallOffBottom() {
        Ball testBall = new Ball(100, 100, Ball.BOTTOM_LEFT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        testBall.yVelocity = 1;

        testBall.move(theme, imageWidth + 4, 100);

        assertFalse("Test Failure: Ball should not pop when it falls offscreen", testBall.popped);
        assertTrue("Test Failure: Ball is not being set to offscreen correctly", testBall.offScreen);
    }

    public void testIsOffScreen() {
        Ball testBall = new Ball(0, 100, 1, 1);

        testBall.offScreen = true;
        assertTrue("Test Failure: Offscreen Accessor is not in sync in variable", testBall.isOffScreen());
        testBall.offScreen = false;
        assertFalse("Test Failure: Offscreen Accessor is not in sync in variable", testBall.isOffScreen());
    }

    public void testIsPopped() {
        Ball testBall = new Ball(0, 100, 1, 1);

        testBall.popped = true;
        assertTrue("Test Failure: Popped Accessor is not in sync in variable", testBall.isPopped());
        testBall.popped = false;
        assertFalse("Test Failure: Popped Accessor is not in sync in variable", testBall.isPopped());
    }

    public void testHandleTouchTopLeft() {
        Ball testBall = new Ball(0, 0, 0, 0);

        testBall.handleTouch(theme, 0, 0);

        assertTrue("Test Failure: Ball does not pop when touched", testBall.popped);
    }

    public void testHandleTouchWithBuffer() {
        Ball testBall = new Ball(10, 10, 0, 0);

        testBall.handleTouch(theme, 0, 0);

        assertTrue("Test Failure: Ball does not pop when touched", testBall.popped);
    }

    public void testHandleTouchTopRight() {
        Ball testBall = new Ball(0, 0, 0, 0);

        int imageWidth = testBall.getImage(theme).getWidth();

        testBall.handleTouch(theme, imageWidth, 0);

        assertTrue("Test Failure: Ball does not pop when touched", testBall.popped);
    }

    public void testHandleTouchBottomLeft() {
        Ball testBall = new Ball(0, 0, 0, 0);

        int imageHeight = testBall.getImage(theme).getHeight();

        testBall.handleTouch(theme, 0, imageHeight);

        assertTrue("Test Failure: Ball does not pop when touched", testBall.popped);
    }

    public void testHandleTouchBottomRight() {
        Ball testBall = new Ball(0, 0, 0, 0);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        testBall.handleTouch(theme, imageWidth, imageHeight);

        assertTrue("Test Failure: Ball does not pop when touched", testBall.popped);
    }

    public void testGetImage() {
        Ball testBall = new Ball(0, 0, 0, 0);

        assertTrue("Test Failure: Ball Image is not consistent with theme",
                theme.getBall().equals(testBall.getImage(theme)));
    }

    public void testGetImageWhenPopped() {
        Ball testBall = new Ball(0, 0, 0, 0);

        testBall.popped = true;

        assertTrue("Test Failure: Popped Image is not consistent with theme",
                theme.getPoppedBall().equals(testBall.getImage(theme)));
    }

    public void testGetScoreValue() {
        Ball testBall = new Ball(0, 0, 0, 0);

        assertEquals("Test Failure: Points not based on constant", Ball.VALUE, testBall.getScoreValue());
    }
}
