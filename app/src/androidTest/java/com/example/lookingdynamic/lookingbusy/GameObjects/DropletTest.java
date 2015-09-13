package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.themes.CrayonGameTheme;
import com.example.lookingdynamic.lookingbusy.themes.GameTheme;

/**
 *
 * Created by swu on 9/5/2015.
 */
public class DropletTest extends ActivityTestCase {

    public GameTheme theme = new CrayonGameTheme(getInstrumentation().getTargetContext().getResources());

    public void testCreateBalloon(){
        Droplet testDroplet = new Droplet(theme, 0, 0);

        assertNotNull(testDroplet.getImage(theme));
        assertTrue(1 < testDroplet.getImage(theme).getWidth());
        assertTrue(0 > testDroplet.xCoordinate);
        assertEquals(0, testDroplet.yCoordinate);
        assertEquals(0, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertFalse(testDroplet.offScreen);
    }

    public void testMove() {
        Droplet testDroplet = new Droplet(theme, 0, 1);

        int oldXCoordinate = testDroplet.xCoordinate;
        testDroplet.move(theme, 1000, 1000);

        assertEquals(oldXCoordinate, testDroplet.xCoordinate);
        assertEquals(1, testDroplet.yCoordinate);
        assertEquals(1, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertFalse(testDroplet.offScreen);
    }

    public void testMoveFallOffBottom() {
        Droplet testDroplet = new Droplet(theme, 0, 1000);

        int oldXCoordinate = testDroplet.xCoordinate;
        testDroplet.move(theme, 1, 1);

        assertEquals(oldXCoordinate, testDroplet.xCoordinate);
        assertEquals(1000, testDroplet.yCoordinate);
        assertEquals(1000, testDroplet.yVelocity);
        assertFalse(testDroplet.popped);
        assertTrue(testDroplet.offScreen);
    }
}
