package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.R;

/**
 *
 * Created by swu on 9/13/2015.
 */
public class GameThemeTest extends ActivityTestCase {

    public void testCreateCrayonGameTheme() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertEquals("Test Failure: Bright Theme Name has changed","Bright Theme", theme.getName());
        assertEquals("Test Failure: Bright Theme icon has changed", R.drawable.bright_icon, theme.getIconImageId());
        assertEquals("Test Failure: Bright Theme ball has changed", R.drawable.bright_ball, theme.ballImage);
        assertEquals("Test Failure: Bright Theme balloon has changed", R.drawable.bright_balloon1, theme.balloonImages[0]);
        assertEquals("Test Failure: Bright Theme droplet has changed", R.drawable.bright_droplet, theme.dropletImage);
        assertEquals("Test Failure: Bright Theme popped_ball has changed", R.drawable.bright_popped_ball, theme.poppedBallImage);
        assertEquals("Test Failure: Bright Theme popped_balloon has changed", R.drawable.bright_popped_balloon1, theme.poppedBalloonImages[0]);
        assertEquals("Test Failure: Bright Theme popped_droplet has changed", R.drawable.bright_popped_droplet, theme.poppedDropletImage);
        assertEquals("Test Failure: Bright Theme pause has changed", R.drawable.pause, theme.pauseSignImage);
    }

    public void testGetBallBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNotNull("Test Failure: Ball Bitmap does not load properly",
                theme.getBall());
    }

    public void testGetBalloonBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNotNull("Test Failure: Balloon Bitmap does not load properly",
                theme.getBalloon(0));
    }

    public void testGetDropletBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNotNull("Test Failure: Droplet Bitmap does not load properly",
                theme.getDroplet());
    }

    public void testGetPoppedBallBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNotNull("Test Failure: Popped Ball Bitmap does not load properly",
                theme.getPoppedBall());
    }

    public void testGetPoppedBalloonBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNotNull("Test Failure: Popped Balloon Bitmap does not load properly",
                theme.getPoppedBalloon(0));
    }

    public void testGetPoppedDropletBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNotNull("Test Failure: Popped Droplet Bitmap does not load properly",
                theme.getPoppedDroplet());
    }

    public void testGetPausedSignBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNotNull("Test Failure: Pause Bitmap does not load properly",
                theme.getPauseSign());
    }

    public void testCreateSweetGameTheme() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_subtle));

        assertEquals("Test Failure: Subtle Theme Name has changed", "Subtle Theme", theme.getName());
        assertEquals("Test Failure: Subtle Theme icon has changed", R.drawable.business_icon, theme.getIconImageId());
        assertEquals("Test Failure: Subtle Theme ball has changed", R.drawable.business_power, theme.ballImage);
        assertEquals("Test Failure: Subtle Theme balloon has changed", R.drawable.business_wifi, theme.balloonImages[0]);
        assertEquals("Test Failure: Subtle Theme droplet has changed", R.drawable.business_battery, theme.dropletImage);
        assertEquals("Test Failure: Subtle Theme popped_ball has changed", R.drawable.business_popped_power, theme.poppedBallImage);
        assertEquals("Test Failure: Subtle Theme popped_balloon has changed", R.drawable.business_popped_wifi, theme.poppedBalloonImages[0]);
        assertEquals("Test Failure: Subtle Theme popped_droplet has changed", R.drawable.business_popped_battery, theme.poppedDropletImage);
        assertEquals("Test Failure: Subtle Theme pause has changed", R.drawable.pause, theme.pauseSignImage);
    }

    public void testCreateThemeBasedOnArrayValue() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        TypedArray availableThemesArray = myResources.obtainTypedArray(R.array.available_game_themes);

        GameTheme[] themes = new GameTheme[availableThemesArray.length()];
        assertEquals("Test Failure: Available Themes have changed", 3, availableThemesArray.length());

        for(int i=0; i < availableThemesArray.length(); i++) {
            themes[i] = new GameTheme(myResources, myResources.getXml(availableThemesArray.getResourceId(i,-1)));
        }

        assertEquals("Test Failure: Theme order has changed", "Bright Theme", themes[0].name);
        assertEquals("Test Failure: Theme order has changed", "Subtle Theme", themes[1].name);
        assertEquals("Test Failure: Theme order has changed", "Sweet Theme", themes[2].name);
    }

    public void testLazyLoadStartsNull(){
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        assertNull("Test Failure: ball Image was pre-loaded", theme.ball);
        assertNull("Test Failure: balloon Image was pre-loaded", theme.balloons);
        assertNull("Test Failure: droplet Image was pre-loaded", theme.droplet);
        assertNull("Test Failure: popped_ball Image was pre-loaded", theme.popped_ball);
        assertNull("Test Failure: popped_balloon Image was pre-loaded", theme.popped_balloons);
        assertNull("Test Failure: popped_droplet Image was pre-loaded", theme.popped_droplet);
        assertNull("Test Failure: pause_sign Image was pre-loaded", theme.pause_sign);
    }

    public void testunloadReturnsToNull(){
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.theme_bright));

        //Load all images first
        theme.getBall();
        theme.getBalloon(0);
        theme.getDroplet();
        theme.getPoppedBall();
        theme.getPoppedBalloon(0);
        theme.getPoppedDroplet();
        theme.getPauseSign();
        theme.unloadImages();

        assertNull("Test Failure: ball Image was not de-referenced", theme.ball);
        assertNull("Test Failure: balloon Image was not de-referenced", theme.balloons);
        assertNull("Test Failure: droplet Image was not de-referenced", theme.droplet);
        assertNull("Test Failure: popped_ball Image was not de-referenced", theme.popped_ball);
        assertNull("Test Failure: popped_balloon Image was not de-referenced", theme.popped_balloons);
        assertNull("Test Failure: popped_droplet Image was not de-referenced", theme.popped_droplet);
        assertNull("Test Failure: pause_sign Image was not de-referenced", theme.pause_sign);
    }
}
