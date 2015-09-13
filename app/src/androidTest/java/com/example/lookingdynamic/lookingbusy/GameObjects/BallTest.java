package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.themes.CrayonGameTheme;
import com.example.lookingdynamic.lookingbusy.themes.GameTheme;

/**
 *
 * Created by swu on 9/5/2015.
 */
public class BallTest extends ActivityTestCase {

    public GameTheme theme = new CrayonGameTheme(getInstrumentation().getTargetContext().getResources());

    public void testCreateBall(){
        Ball testBall = new Ball(null, 0, 0, 0, 0);

        assertNull(testBall.getImage(theme));
        assertEquals(0, testBall.xCoordinate);
        assertEquals(0, testBall.yCoordinate);
        assertEquals(0, testBall.xVelocity);
        assertEquals(0, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testCreateBallAwayFromXOrigin(){
        Ball testBall = new Ball(theme, 1, 0, 0, 0);

        assertNotNull(testBall.getImage(theme));
        assertTrue(1 < testBall.getImage(theme).getWidth());
        assertTrue(0 > testBall.xCoordinate);
        assertEquals(0, testBall.yCoordinate);
        assertEquals(0, testBall.xVelocity);
        assertEquals(0, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testCreateBallAwayFromYOrigin(){
        Ball testBall = new Ball(theme, 0, 1, 0, 0);

        assertNotNull(testBall.getImage(theme));
        assertTrue(1 < testBall.getImage(theme).getHeight());
        assertEquals(0, testBall.xCoordinate);
        assertTrue(1 > testBall.yCoordinate);
        assertEquals(0, testBall.xVelocity);
        assertEquals(0, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMove() {
        Ball testBall = new Ball(theme, 0, 0, 1, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals(1, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBounceOffLeft() {
        Ball testBall = new Ball(null, -1, 0, -1, 1);

        testBall.move(theme, 1000, 1000);

        assertEquals(0, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveBounceOffRight() {
        Ball testBall = new Ball(theme, 100, 0, 1, 1);

        int initialLocation = 100 - testBall.getImage(theme).getWidth();

        testBall.move(theme, 1, 1000);

        assertEquals(initialLocation - 1, testBall.xCoordinate);
        assertEquals(1, testBall.yCoordinate);
        assertEquals(-1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertFalse(testBall.offScreen);
    }

    public void testMoveFallOffTop() {
        Ball testBall = new Ball(theme, 0, 0, 1, -1);

        testBall.move(theme, 1000, 1000);

        assertEquals(1, testBall.xCoordinate);
        assertEquals(-1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(-1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertTrue(testBall.offScreen);
    }

    public void testMoveFallOffBottom() {
        Ball testBall = new Ball(theme, 0, 100, 1, 1);

        int initialLocation = 100 - testBall.getImage(theme).getHeight();

        testBall.move(theme, 1000, initialLocation - 1);

        assertEquals(1, testBall.xCoordinate);
        assertEquals(initialLocation + 1, testBall.yCoordinate);
        assertEquals(1, testBall.xVelocity);
        assertEquals(1, testBall.yVelocity);
        assertFalse(testBall.popped);
        assertTrue(testBall.offScreen);
    }

    public void testIsOffScreen() {
        Ball testBall = new Ball(theme, 0, 100, 1, 1);

        testBall.offScreen = true;
        assertTrue(testBall.isOffScreen());
        testBall.offScreen = false;
        assertFalse(testBall.isOffScreen());
    }

    public void testPopped() {
        Ball testBall = new Ball(theme, 0, 100, 1, 1);

        testBall.popped = true;
        assertTrue(testBall.isPopped());
        testBall.popped = false;
        assertFalse(testBall.isPopped());
    }

    public void testHandleTouch() {
        Ball testBall = new Ball(theme, 0, 0, 0, 0);
        testBall.handleTouch(theme, 0, 0);

        assertTrue(testBall.popped);
    }
}
