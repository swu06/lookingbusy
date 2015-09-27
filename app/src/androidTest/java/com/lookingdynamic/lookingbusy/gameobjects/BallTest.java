package com.lookingdynamic.lookingbusy.gameobjects;

import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.R;
import com.lookingdynamic.lookingbusy.gameobjects.Ball;
import com.lookingdynamic.lookingbusy.gameplay.GameTheme;
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

        assertEquals(0, testBall.xCoordinate);
        assertEquals(0, testBall.yCoordinate);
        assertEquals(0, testBall.xVelocity);
        assertEquals(0, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveTopLeft() {
        Ball testBall = new Ball(1000, 1000, Ball.TOP_LEFT_START, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals(2, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(2, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveTopRight() {
        Ball testBall = new Ball(1000, 1000, Ball.TOP_RIGHT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        testBall.move(theme, 1000, 1000);

        assertEquals(1000 - imageWidth, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(-2, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBottomRight() {
        Ball testBall = new Ball(1000, 1000, Ball.BOTTOM_RIGHT_START, 1);
        int imageWidth = testBall.getImage(theme).getWidth();

        testBall.move(theme, 1000, 1000);

        assertEquals(1000 - imageWidth, testBall.xCoordinate);
        assertEquals(999, testBall.yCoordinate);
        assertEquals(-2, testBall.xVelocity);
        assertEquals(-1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBottomLeft() {
        Ball testBall = new Ball(1000, 1000, Ball.BOTTOM_LEFT_START, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals(2, testBall.xCoordinate);
        assertEquals(999, testBall.yCoordinate);
        assertEquals(2, testBall.xVelocity);
        assertEquals(-1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBounceOffLeft() {
        Ball testBall = new Ball(1000, 1000, Ball.TOP_LEFT_START, 1);

        testBall.xCoordinate = 0;
        testBall.xVelocity = -1;

        testBall.move(theme, 1000, 1000);

        assertEquals(0, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBounceOffRight() {
        Ball testBall = new Ball(100, 0, Ball.TOP_LEFT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        int wallLocation = 100 + imageWidth;

        testBall.xCoordinate = wallLocation;
        testBall.xVelocity = 1;

        testBall.move(theme, wallLocation, imageHeight + 2);

        assertEquals(wallLocation - imageWidth, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(-1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveFallOffTop() {
        Ball testBall = new Ball(100, 0, Ball.TOP_LEFT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        testBall.yCoordinate = 0 - imageHeight;
        testBall.yVelocity = -1;

        testBall.move(theme, imageWidth + 4, imageHeight + 4);

        assertEquals(2, testBall.xCoordinate);
        assertEquals(0 - imageHeight - 1, testBall.yCoordinate);
        assertEquals(2, testBall.xVelocity);
        assertEquals(-1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertTrue(testBall.offScreen);
    }

    public void testMoveFallOffBottom() {
        Ball testBall = new Ball(100, 100, Ball.BOTTOM_LEFT_START, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        testBall.yVelocity = 1;

        testBall.move(theme, imageWidth + 4, 100);

        assertEquals(2, testBall.xCoordinate);
        assertEquals(101, testBall.yCoordinate);
        assertEquals(2, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertTrue(testBall.offScreen);
    }

    public void testIsOffScreen() {
        Ball testBall = new Ball(0, 100, 1, 1);

        testBall.offScreen = true;
        assertTrue(testBall.isOffScreen());
        testBall.offScreen = false;
        assertFalse(testBall.isOffScreen());
    }

    public void testPopped() {
        Ball testBall = new Ball(0, 100, 1, 1);

        testBall.popped = true;
        assertTrue(testBall.isPopped());
        testBall.popped = false;
        assertFalse(testBall.isPopped());
    }

    public void testHandleTouchTopLeft() {
        Ball testBall = new Ball(0, 0, 0, 0);

        testBall.handleTouch(theme, 0, 0);

        assertTrue(testBall.popped);
    }

    public void testHandleTouchTopRight() {
        Ball testBall = new Ball(0, 0, 0, 0);

        int imageWidth = testBall.getImage(theme).getWidth();

        testBall.handleTouch(theme, imageWidth, 0);

        assertTrue(testBall.popped);
    }

    public void testHandleTouchBottomLeft() {
        Ball testBall = new Ball(0, 0, 0, 0);

        int imageHeight = testBall.getImage(theme).getHeight();

        testBall.handleTouch(theme, 0, imageHeight);

        assertTrue(testBall.popped);
    }

    public void testHandleTouchBottomRight() {
        Ball testBall = new Ball(0, 0, 0, 0);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        testBall.handleTouch(theme, imageWidth, imageHeight);

        assertTrue(testBall.popped);
    }

}
