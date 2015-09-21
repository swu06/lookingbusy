package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.R;
import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;

/**
 *
 * Created by swu on 9/5/2015.
 */
public class BalloonTest extends ActivityTestCase {

    public GameTheme theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new GameTheme(getInstrumentation().getTargetContext().getResources(),
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.crayon_theme));
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
        Balloon testBalloon = new Balloon(1000, 1000, -1, 0);

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
}
