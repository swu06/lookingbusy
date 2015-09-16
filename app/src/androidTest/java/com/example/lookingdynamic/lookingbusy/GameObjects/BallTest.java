package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.R;
import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;

/**
 * This Test class tests the methods within Ball, and the methods implemented in the
 * PoppableObject abstract parent class.
 * Created by swu on 9/5/2015.
 */
public class BallTest extends ActivityTestCase {

    public GameTheme theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new GameTheme(getInstrumentation().getTargetContext().getResources(),
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.crayon_theme));
    }

    public void testCreateBall(){
        Ball testBall = new Ball(0, 0, 0, 0);

        assertEquals(0, testBall.xCoordinate);
        assertEquals(0, testBall.yCoordinate);
        assertEquals(0, testBall.xVelocity);
        assertEquals(0, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMove() {
        Ball testBall = new Ball(0, 0, 1, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals(1, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBounceOffLeft() {
        Ball testBall = new Ball(-1, 0, -1, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals(0, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBounceOffRight() {
        Ball testBall = new Ball(100, 0, 1, 1);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        int wallLocation = 100 + imageWidth;

        testBall.move(theme, wallLocation, imageHeight + 2);

        assertEquals(wallLocation - imageWidth, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(-1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveFallOffTop() {
        Ball testBall = new Ball(0, 0, 1, -1);

        int imageWidth = testBall.getImage(theme).getWidth();
        int imageHeight = testBall.getImage(theme).getHeight();

        testBall = new Ball(0, 0 - imageHeight, 1, -1);

        testBall.move(theme, imageWidth + 2, imageHeight + 2);

        assertEquals(1, testBall.xCoordinate);
        assertEquals(0 - imageHeight- 1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(-1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertTrue(testBall.offScreen);
    }

    public void testMoveFallOffBottom() {
        Ball testBall = new Ball(0, 100, 1, 1);

        int imageWidth = testBall.getImage(theme).getWidth();

        testBall.move(theme, imageWidth + 2, 100);

        assertEquals(1, testBall.xCoordinate);
        assertEquals(101, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
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
