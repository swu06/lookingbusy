package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.test.ActivityTestCase;

/**
 *
 * Created by swu on 9/5/2015.
 */
public class DropletTest extends ActivityTestCase {

    public void testCreateBalloon(){
        Droplet testDroplet = new Droplet(getInstrumentation().getTargetContext().getResources(), 0, 0);

        assertNotNull(testDroplet.bitmapImage);
        assertTrue(1 < testDroplet.bitmapImage.getWidth());
        assertTrue(0 > testDroplet.xCoordinate);
        assertEquals(0, testDroplet.yCoordinate);
        assertEquals(0, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertFalse(testDroplet.offScreen);
    }

    public void testSetPoppedImage() {
        Droplet testDroplet = new Droplet(getInstrumentation().getTargetContext().getResources(), 0, 0);

        Bitmap theImage = testDroplet.bitmapImage;
        testDroplet.setPoppedImage(getInstrumentation().getTargetContext().getResources());

        assertFalse(theImage.equals(testDroplet.bitmapImage));
    }

    public void testMove() {
        Droplet testDroplet = new Droplet(getInstrumentation().getTargetContext().getResources(), 0, 1);

        int oldXCoordinate = testDroplet.xCoordinate;
        testDroplet.move(1000, 1000);

        assertEquals(oldXCoordinate, testDroplet.xCoordinate);
        assertEquals(1, testDroplet.yCoordinate);
        assertEquals(1, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertFalse(testDroplet.offScreen);
    }

    public void testMoveFallOffBottom() {
        Droplet testDroplet = new Droplet(getInstrumentation().getTargetContext().getResources(), 0, 1000);

        int oldXCoordinate = testDroplet.xCoordinate;
        int oldYCoordinate = testDroplet.yCoordinate;
        testDroplet.move(1, 1);

        assertEquals(oldXCoordinate, testDroplet.xCoordinate);
        assertEquals(1000, testDroplet.yCoordinate);
        assertEquals(1000, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertTrue(testDroplet.offScreen);
    }
}
