package com.lookingdynamic.lookingbusy.gameobjects;

import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.R;
import com.lookingdynamic.lookingbusy.gameobjects.Droplet;
import com.lookingdynamic.lookingbusy.gameplay.GameTheme;
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

    public void testCreateBalloon(){
        Droplet testDroplet = new Droplet(0, 0);

        assertNotNull(testDroplet.getImage(theme));
        assertEquals(0, testDroplet.xCoordinate);
        assertEquals(0, testDroplet.yCoordinate);
        assertEquals(0, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertFalse(testDroplet.offScreen);
    }

    public void testMove() {
        Droplet testDroplet = new Droplet(0, 1);

        int oldXCoordinate = testDroplet.xCoordinate;
        testDroplet.move(theme, 1000, 1000);

        assertEquals(oldXCoordinate, testDroplet.xCoordinate);
        assertEquals(1, testDroplet.yCoordinate);
        assertEquals(1, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertFalse(testDroplet.offScreen);
    }

    public void testMoveFallOffBottom() {
        Droplet testDroplet = new Droplet(0, 1000);

        int oldXCoordinate = testDroplet.xCoordinate;
        testDroplet.move(theme, 1, 1);

        assertEquals(oldXCoordinate, testDroplet.xCoordinate);
        assertEquals(1000, testDroplet.yCoordinate);
        assertEquals(1000, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertTrue(testDroplet.offScreen);
    }
}