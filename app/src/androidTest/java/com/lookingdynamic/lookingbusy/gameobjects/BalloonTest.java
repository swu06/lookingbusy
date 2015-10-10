package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;


/**
 *
 * Created by swu on 9/5/2015.
 */
public class BalloonTest extends ActivityTestCase {

    public ThemeManager theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());
    }

    public void testCreateBalloon(){
        Balloon testBalloon = new Balloon(0, 0, 0, 0);

        assertNotNull(testBalloon.getImage(theme));
        assertTrue(1 < testBalloon.getImage(theme).getWidth());
        assertTrue(1 < testBalloon.getImage(theme).getHeight());
        assertEquals(0, testBalloon.xCoordinate);
        assertEquals(0, testBalloon.yCoordinate);
        assertEquals(0, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertFalse(testBalloon.offScreen);

    }

    public void testMove() {
        Balloon testBalloon = new Balloon(600, 600, -1, 0);

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
        Balloon dummyBalloon = new Balloon(0, 0, -1, 0);

        Bitmap image = dummyBalloon.getImage(theme);
        Balloon testBalloon = new Balloon(0, -1 * image.getHeight(), -1, 0);


        testBalloon.move(theme, 1, 1);

        assertEquals(0, testBalloon.xCoordinate);
        assertEquals((-1 * image.getHeight()) - 1, testBalloon.yCoordinate);
        assertEquals(-1, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertTrue(testBalloon.offScreen);
    }

    public void testGettingSameImage() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        Bitmap originalImage = testBalloon.getImage(theme);
        testBalloon.move(theme, 1, 1);
        Bitmap nextImage = testBalloon.getImage(theme);
        assertEquals("Images should not change for objects", originalImage, nextImage);

        testBalloon.move(theme, 1, 1);
        nextImage = testBalloon.getImage(theme);
        assertEquals("Images should not change for objects", originalImage, nextImage);

        testBalloon.move(theme, 1, 1);
        nextImage = testBalloon.getImage(theme);
        assertEquals("Images should not change for objects", originalImage, nextImage);

        testBalloon.move(theme, 1, 1);
        nextImage = testBalloon.getImage(theme);
        assertEquals("Images should not change for objects", originalImage, nextImage);
    }

    public void testGetScoreValue() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        assertEquals("Test Failure: Points not based on constant", Balloon.VALUE, testBalloon.getScoreValue());
    }

    public void testGetImage() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        assertTrue("Test Failure: Ball Image is not consistent with theme",
                theme.getBalloon(0).equals(testBalloon.getImage(theme)));
    }

    public void testGetImageWhenPopped() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        testBalloon.popped = true;

        assertTrue("Test Failure: Popped Image is not consistent with theme",
                theme.getPoppedBalloon(0).equals(testBalloon.getImage(theme)));
    }
}
