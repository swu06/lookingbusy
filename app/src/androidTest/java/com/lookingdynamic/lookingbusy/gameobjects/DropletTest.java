package com.lookingdynamic.lookingbusy.gameobjects;

import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 *
 * Created by swu on 9/5/2015.
 */
public class DropletTest extends ActivityTestCase {

    public ThemeManager theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());
    }

    public void testCreateDroplet(){
        Droplet testDroplet = new Droplet(0, 0);

        assertNotNull("Test Failure: Droplet object is null", testDroplet);
        assertEquals("Test Failure: xCoordinate incorrect on initialization", 0, testDroplet.xCoordinate);
        assertEquals("Test Failure: yCoordinate incorrect on initialization", 0, testDroplet.yCoordinate);
        assertEquals("Test Failure: yVelocity incorrect on initialization", 0, testDroplet.yVelocity);
        assertEquals("Test Failure: xVelocity incorrect on initialization", 0, testDroplet.xVelocity);
        assertFalse("Test Failure: popped incorrect on initialization", testDroplet.popped);
        assertFalse("Test Failure: offScreen incorrect on initialization", testDroplet.offScreen);
    }

    public void testMove() {
        Droplet testDroplet = new Droplet(0, 1);

        testDroplet.move(theme, 1000, 1000);

        assertEquals("Test Failure: xCoordinate does not match expected value after move",
                0, testDroplet.xCoordinate);
        assertEquals("Test Failure: yCoordinate does not match expected value after move",
                1, testDroplet.yCoordinate);
        assertEquals("Test Failure: yVelocity does not match expected value after move", 1, testDroplet.yVelocity);
        assertFalse("Test Failure: Balloon should not pop when moved", testDroplet.popped);
        assertFalse("Test Failure: Balloon should not go offscreen when moved", testDroplet.offScreen);
    }

    public void testMoveFallOffBottom() {
        Droplet testDroplet = new Droplet(0, 1000);

        testDroplet.move(theme, 1, 1);


        assertFalse("Test Failure: Balloon should not pop when floating off top", testDroplet.popped);
        assertTrue("Test Failure: Balloon should show as offscreen when it floats too high", testDroplet.offScreen);
    }

    public void testGetImage() {
        Droplet testDroplet = new Droplet(0, 0);

        assertTrue("Test Failure: Droplet Image is not consistent with theme",
                theme.getDroplet().equals(testDroplet.getImage(theme)));
    }

    public void testGetImageWhenPopped() {
        Droplet testDroplet = new Droplet(0, 0);

        testDroplet.popped = true;

        assertTrue("Test Failure: Popped Image is not consistent with theme",
                theme.getPoppedDroplet().equals(testDroplet.getImage(theme)));
    }
}
