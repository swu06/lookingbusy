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

        assertNotNull("Test Failure: Balloon object is null", testBalloon);
        assertNotNull("Test Failure: whichBalloon incorrect on initialization", testBalloon.whichBalloon);
        assertEquals("Test Failure: xCoordinate incorrect on initialization", 0, testBalloon.xCoordinate);
        assertEquals("Test Failure: yCoordinate incorrect on initialization", 0, testBalloon.yCoordinate);
        assertEquals("Test Failure: yVelocity incorrect on initialization", 0, testBalloon.yVelocity);
        assertEquals("Test Failure: xVelocity incorrect on initialization", 0, testBalloon.xVelocity);
        assertFalse("Test Failure: popped incorrect on initialization", testBalloon.popped);
        assertFalse("Test Failure: offScreen incorrect on initialization", testBalloon.offScreen);
    }

    public void testMove() {
        Balloon testBalloon = new Balloon(600, 600, -1, 0);

        int oldXCoordinate = testBalloon.xCoordinate;
        int oldYCoordinate = testBalloon.yCoordinate;
        testBalloon.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate does not match expected value after move",
                    oldXCoordinate, testBalloon.xCoordinate);
        assertEquals("Test Failure: yCoordinate does not match expected value after move",
                    oldYCoordinate - 1, testBalloon.yCoordinate);
        assertEquals("Test Failure: yVelocity does not match expected value after move", -1, testBalloon.yVelocity);
        assertFalse("Test Failure: Balloon should not pop when moved", testBalloon.popped);
        assertFalse("Test Failure: Balloon should not go offscreen when moved", testBalloon.offScreen);
    }

    public void testMoveFallOffTop() {
        Balloon dummyBalloon = new Balloon(0, 0, -1, 0);

        Bitmap image = dummyBalloon.getImage(theme);
        Balloon testBalloon = new Balloon(0, -1 * image.getHeight(), -1, 0);

        testBalloon.move(theme, 1, 1);

        assertFalse("Test Failure: Balloon should not pop when floating off top", testBalloon.popped);
        assertTrue("Test Failure: Balloon should show as offscreen when it floats too high", testBalloon.offScreen);
    }

    public void testGettingSameImage() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        Bitmap originalImage = testBalloon.getImage(theme);
        testBalloon.move(theme, 1, 1);
        Bitmap nextImage = testBalloon.getImage(theme);
        assertEquals("Test Failure: Images should not change for poppable objects", originalImage, nextImage);

        testBalloon.move(theme, 1, 1);
        nextImage = testBalloon.getImage(theme);
        assertEquals("Test Failure: Images should not change for poppable objects", originalImage, nextImage);

        testBalloon.move(theme, 1, 1);
        nextImage = testBalloon.getImage(theme);
        assertEquals("Test Failure: Images should not change for poppable objects", originalImage, nextImage);

        testBalloon.move(theme, 1, 1);
        nextImage = testBalloon.getImage(theme);
        assertEquals("Test Failure: Images should not change for poppable objects", originalImage, nextImage);
    }

    public void testGetScoreValue() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        assertEquals("Test Failure: Points not based on constant", Balloon.VALUE, testBalloon.getScoreValue());
    }

    public void testGetImage() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        assertTrue("Test Failure: Balloon Image is not consistent with theme",
                theme.getBalloon(0).equals(testBalloon.getImage(theme)));
    }

    public void testGetImageWhenPopped() {
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

        testBalloon.popped = true;

        assertTrue("Test Failure: Popped Image is not consistent with theme",
                theme.getPoppedBalloon(0).equals(testBalloon.getImage(theme)));
    }
}
