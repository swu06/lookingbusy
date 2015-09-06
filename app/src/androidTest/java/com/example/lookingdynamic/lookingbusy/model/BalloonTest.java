package com.example.lookingdynamic.lookingbusy.model;

import android.graphics.Bitmap;
import android.test.ActivityTestCase;

/**
 *
 * Created by swu on 9/5/2015.
 */
public class BalloonTest extends ActivityTestCase {

    public void testCreateBalloon(){
        Balloon testBalloon = new Balloon(getInstrumentation().getTargetContext().getResources(), 0, 0, 0);

        assertNotNull(testBalloon.bitmapImage);
        assertTrue(1 < testBalloon.bitmapImage.getWidth());
        assertTrue(1 < testBalloon.bitmapImage.getHeight());
        assertTrue(0 > testBalloon.xCoordinate);
        assertTrue(0 > testBalloon.yCoordinate);
        assertEquals(0, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertFalse(testBalloon.offScreen);
    }

    public void testSetPoppedImage() {
        Balloon testBalloon = new Balloon(getInstrumentation().getTargetContext().getResources(), 0, 0, 0);

        Bitmap theImage = testBalloon.bitmapImage;
        testBalloon.setPoppedImage(getInstrumentation().getTargetContext().getResources());

        assertFalse(theImage.equals(testBalloon.bitmapImage));
    }

    public void testMove() {
        Balloon testBalloon = new Balloon(getInstrumentation().getTargetContext().getResources(), 1000, 1000, -1);

        int oldXCoordinate = testBalloon.xCoordinate;
        int oldYCoordinate = testBalloon.yCoordinate;
        testBalloon.move(1000, 1000);

        assertEquals(oldXCoordinate, testBalloon.xCoordinate);
        assertEquals(oldYCoordinate - 1, testBalloon.yCoordinate);
        assertEquals(-1, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertFalse(testBalloon.offScreen);
    }

    public void testMoveFallOffTop() {
        Balloon testBalloon = new Balloon(getInstrumentation().getTargetContext().getResources(), 0, 0, -1);

        int oldXCoordinate = testBalloon.xCoordinate;
        int oldYCoordinate = testBalloon.yCoordinate;
        testBalloon.move(1, 1);

        assertEquals(oldXCoordinate, testBalloon.xCoordinate);
        assertEquals(oldYCoordinate - 1, testBalloon.yCoordinate);
        assertEquals(-1, testBalloon.yVelocity);
        assertFalse(testBalloon.popped);
        assertTrue(testBalloon.offScreen);
    }
}
