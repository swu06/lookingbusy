package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;

/**
 *
 * Created by swu on 9/5/2015.
 */
public class BalloonTest extends ActivityTestCase {

    public GameTheme theme = new GameTheme(getInstrumentation().getTargetContext().getResources(), "crayon_theme");

    public void testCreateBalloon(){
        Balloon testBalloon = new Balloon(0, 0, 0);

        assertNotNull(testBalloon.getImage(theme));
        assertTrue(1 < testBalloon.getImage(theme).getWidth());
        assertTrue(1 < testBalloon.getImage(theme).getHeight());
        assertTrue(0 > testBalloon.xCoordinate);
        assertTrue(0 > testBalloon.yCoordinate);
        assertEquals(0, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertFalse(testBalloon.offScreen);
    }

    public void testMove() {
        Balloon testBalloon = new Balloon(1000, 1000, -1);

        int oldXCoordinate = testBalloon.xCoordinate;
        int oldYCoordinate = testBalloon.yCoordinate;
        testBalloon.move(theme, 1000, 1000);

        assertEquals(oldXCoordinate, testBalloon.xCoordinate);
        assertEquals(oldYCoordinate - 1, testBalloon.yCoordinate);
        assertEquals(-1, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertFalse(testBalloon.offScreen);
    }

    public void testMoveFallOffTop() {
        Balloon testBalloon = new Balloon(0, 0, -1);

        int oldXCoordinate = testBalloon.xCoordinate;
        int oldYCoordinate = testBalloon.yCoordinate;
        testBalloon.move(theme, 1, 1);

        assertEquals(oldXCoordinate, testBalloon.xCoordinate);
        assertEquals(oldYCoordinate - 1, testBalloon.yCoordinate);
        assertEquals(-1, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertTrue(testBalloon.offScreen);
    }
}
