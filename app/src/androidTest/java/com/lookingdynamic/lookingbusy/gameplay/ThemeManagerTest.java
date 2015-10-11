package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.R;

/**
 *
 * Created by swu on 9/20/2015.
 */
public class ThemeManagerTest extends ActivityTestCase{

    public void testCreateThemeManager() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        assertNotNull("Test Failure: ThemeManager creation failed", themes);
        assertNull("Test Failure: Settings should be null for this test", themes.settings);
        assertNotNull("Test Failure: Default Values not set right", themes.randomBot);
        assertNotNull("Test Failure: Default Values not set right", themes.poppedRandomBot);
        assertNotNull("Test Failure: Default Values not set right", themes.defaultRandomBotImage);
        assertNotNull("Test Failure: Default Values not set right", themes.themes);
        assertEquals("Test Failure: Default Values not set right", 0, themes.currentTheme);
        assertTrue("Test Failure: Default Values not set right", themes.themes.length > 0);

        for (GameTheme theme : themes.themes) {
            assertNotNull("Test Failure: All these should be non-null", theme);
        }
    }

    public void testAvailableThemeLabels() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        String[] labels = themes.getAvailableThemeLabels();

        assertNotNull("Test Failure: labels should not be a null array", labels);
        for (String label : labels) {
            assertNotNull("Test Failure: no label should be null", label);
        }

        assertTrue("Test Failure: There should be at least one label at any time", labels.length > 0);
    }

    public void testAvailableThemeIconImageIDs() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        Integer[] ids = themes.getAvailableThemeIconImageIDs();

        assertNotNull("Test Failure: icons should not be a null array", ids);
        for (Integer id : ids) {
            assertNotNull("Test Failure: no icon should be null", id);
        }

        assertTrue("Test Failure: There should be at least one icon at any time", ids.length > 0);
    }

    public void testIconsAndLabelsSameLength() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        Integer[] ids = themes.getAvailableThemeIconImageIDs();
        String[] labels = themes.getAvailableThemeLabels();

        assertEquals("Test Failure: There should be the same number of labels as icons", ids.length, labels.length);
    }

    public void testGetSetCurrentThemeID() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        themes.setTheme(1);
        assertEquals("Test Failure: CurrentTheme should be 1", 1, themes.getCurrentThemeID());
    }

    public void testGetSetInvalidCurrentThemeID() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        themes.setTheme(1);
        themes.setTheme(1000);
        assertEquals("Test Failure: CurrentTheme should be 1", 1, themes.getCurrentThemeID());
    }

    public void testGetCurrentTheme() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        assertNotNull("Test Failure: CurrentTheme should be a valid object", themes.getCurrentTheme());
    }

    public void testGetCurrentThemeIconID() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        assertNotNull("Test Failure: CurrentTheme Icon should be a valid object", themes.getCurrentThemeIconID());
    }

    public void testGetSetRandomBot() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        ThemeManager themes = new ThemeManager(null, myResources);
        Bitmap testBitmap = BitmapFactory.decodeResource(myResources, R.drawable.bright_ball);

        themes.setRandomBotImage(testBitmap);

        assertEquals("Test Failure: RandomBot Image did not set properly", testBitmap, themes.getRandomBot());
    }

    public void testClearRandomBot() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        ThemeManager themes = new ThemeManager(null, myResources);
        Bitmap testBitmap = BitmapFactory.decodeResource(myResources, R.drawable.bright_ball);

        themes.setRandomBotImage(testBitmap);
        themes.clearRandomBotImage();

        assertEquals("Test Failure: RandomBot Image did not set properly",
                themes.defaultRandomBotImage.getWidth(), themes.randomBot.getWidth());
        assertEquals("Test Failure: RandomBot Image did not set properly",
                themes.defaultRandomBotImage.getHeight(), themes.randomBot.getHeight());
    }

    public void testGetPoppedRandomBot() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        ThemeManager themes = new ThemeManager(null, myResources);
        Bitmap poppedImage = BitmapFactory.decodeResource(myResources, R.drawable.ic_action_notification_adb);

        assertEquals("Test Failure: RandomBot Image did not set properly",
                poppedImage.getWidth(), themes.getPoppedRandomBot().getWidth());
        assertEquals("Test Failure: RandomBot Image did not set properly",
                poppedImage.getHeight(), themes.getPoppedRandomBot().getHeight());
    }

    public void testThemeAccessors() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        ThemeManager themes = new ThemeManager(null, myResources);
        themes.setTheme(0);

        assertEquals("Test Failure: Crayon Theme crayon_ball has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_ball).getWidth(),
                themes.getBall().getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_ball has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_ball).getHeight(),
                themes.getBall().getHeight());
        assertEquals("Test Failure: Crayon Theme crayon_balloon has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_balloon1).getWidth(),
                themes.getBalloon(0).getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_balloon has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_balloon1).getHeight(),
                themes.getBalloon(0).getHeight());
        assertEquals("Test Failure: Crayon Theme crayon_droplet has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_droplet).getWidth(),
                themes.getDroplet().getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_droplet has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_droplet).getHeight(),
                themes.getDroplet().getHeight());
        assertEquals("Test Failure: Crayon Theme crayon_droplet has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.bubble).getWidth(),
                themes.getBubble().getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_droplet has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.bubble).getHeight(),
                themes.getBubble().getHeight());
        assertEquals("Test Failure: Crayon Theme crayon_popped_ball has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_popped_ball).getWidth(),
                themes.getPoppedBall().getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_popped_ball has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_popped_ball).getHeight(),
                themes.getPoppedBall().getHeight());
        assertEquals("Test Failure: Crayon Theme crayon_popped_balloon has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_popped_balloon1).getWidth(),
                themes.getPoppedBalloon(0).getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_popped_balloon has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_popped_balloon1).getHeight(),
                themes.getPoppedBalloon(0).getHeight());
        assertEquals("Test Failure: Crayon Theme crayon_popped_droplet has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_popped_droplet).getWidth(),
                themes.getPoppedDroplet().getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_popped_droplet has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.crayon_popped_droplet).getHeight(),
                themes.getPoppedDroplet().getHeight());
        assertEquals("Test Failure: Crayon Theme crayon_pause has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.pause).getWidth(),
                themes.getPauseSign().getWidth());
        assertEquals("Test Failure: Crayon Theme crayon_pause has changed",
                BitmapFactory.decodeResource(myResources, R.drawable.pause).getHeight(),
                themes.getPauseSign().getHeight());
    }

    public void testPainters() {
        ThemeManager themes = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());

        for (int i = 0; i < themes.themes.length; i++) {
            themes.setTheme(i);
            assertNotNull("Test Failure: All themes should return a valid painter", themes.getPainter());
        }
    }


}
