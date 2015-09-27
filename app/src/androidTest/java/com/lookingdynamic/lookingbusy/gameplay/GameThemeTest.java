package com.lookingdynamic.lookingbusy.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.R;
import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;

/**
 * Created by swu on 9/13/2015.
 */
public class GameThemeTest extends ActivityTestCase {

    public void testCreateCrayonGameTheme() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertEquals("Test Failure: Crayon Theme Name has changed","Crayon Theme", theme.getName());
        assertEquals("Test Failure: Crayon Theme crayon_icon has changed", R.drawable.crayon_icon, theme.getIconImageId());
        assertEquals("Test Failure: Crayon Theme crayon_ball has changed", R.drawable.crayon_ball, theme.ballImage);
        assertEquals("Test Failure: Crayon Theme crayon_balloon has changed", R.drawable.crayon_balloon1, theme.balloonImages[0]);
        assertEquals("Test Failure: Crayon Theme crayon_droplet has changed", R.drawable.crayon_droplet, theme.dropletImage);
        assertEquals("Test Failure: Crayon Theme crayon_popped_ball has changed", R.drawable.crayon_popped_ball, theme.poppedBallImage);
        assertEquals("Test Failure: Crayon Theme crayon_popped_balloon has changed", R.drawable.crayon_popped_balloon1, theme.poppedBalloonImages[0]);
        assertEquals("Test Failure: Crayon Theme crayon_popped_droplet has changed", R.drawable.crayon_popped_droplet, theme.poppedDropletImage);
        assertEquals("Test Failure: Crayon Theme crayon_pause has changed", R.drawable.pause, theme.pauseSignImage);
    }

    public void testGetBallBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertNotNull("Test Failure: Ball Bitmap does not load properly",
                theme.getBall());
    }

    public void testGetBalloonBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertNotNull("Test Failure: Balloon Bitmap does not load properly",
                theme.getBalloon(0));
    }

    public void testGetDropletBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertNotNull("Test Failure: Droplet Bitmap does not load properly",
                theme.getDroplet());
    }

    public void testGetPoppedBallBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertNotNull("Test Failure: Popped Ball Bitmap does not load properly",
                theme.getPoppedBall());
    }

    public void testGetPoppedBalloonBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertNotNull("Test Failure: Popped Balloon Bitmap does not load properly",
                theme.getPoppedBalloon(0));
    }

    public void testGetPoppedDropletBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertNotNull("Test Failure: Popped Droplet Bitmap does not load properly",
                theme.getPoppedDroplet());
    }

    public void testGetPausedSignBitmap() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.crayon_theme));

        assertNotNull("Test Failure: Pause Bitmap does not load properly",
                theme.getPauseSign());
    }

    public void testCreateBrightGameTheme() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.bright_theme));

        assertEquals("Test Failure: Bright Theme Name has changed", "Bright Theme", theme.getName());
        assertEquals("Test Failure: Bright Theme bright_icon has changed", R.drawable.bright_icon, theme.getIconImageId());
        assertEquals("Test Failure: Bright Theme bright_ball has changed", R.drawable.bright_ball, theme.ballImage);
        assertEquals("Test Failure: Bright Theme bright_balloon has changed", R.drawable.bright_balloon1, theme.balloonImages[0]);
        assertEquals("Test Failure: Bright Theme bright_droplet has changed", R.drawable.bright_droplet, theme.dropletImage);
        assertEquals("Test Failure: Bright Theme bright_popped_ball has changed", R.drawable.bright_popped_ball, theme.poppedBallImage);
        assertEquals("Test Failure: Bright Theme bright_popped_balloon has changed", R.drawable.bright_popped_balloon1, theme.poppedBalloonImages[0]);
        assertEquals("Test Failure: Bright Theme bright_popped_droplet has changed", R.drawable.bright_popped_droplet, theme.poppedDropletImage);
        assertEquals("Test Failure: Bright Theme bright_pause has changed", R.drawable.pause, theme.pauseSignImage);
    }

    public void testCreateThemeBasedOnArrayValue() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        TypedArray availableThemesArray = myResources.obtainTypedArray(R.array.available_game_themes);

        GameTheme[] themes = new GameTheme[availableThemesArray.length()];
        assertEquals("Test Failure: Available Themes have changed", 2, availableThemesArray.length());

        for(int i=0; i < availableThemesArray.length(); i++) {
            themes[i] = new GameTheme(myResources, myResources.getXml(availableThemesArray.getResourceId(i,-1)));
        }

        assertEquals("Test Failure: Theme order has changed", "Crayon Theme", themes[0].name);
        assertEquals("Test Failure: Theme order has changed", "Bright Theme", themes[1].name);
    }

    public void testLazyLoadStartsNull(){
        Resources myResources = getInstrumentation().getTargetContext().getResources();
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.bright_theme));

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
        GameTheme theme = new GameTheme(myResources, myResources.getXml(R.xml.bright_theme));

        //Load all images first
        theme.getBall();
        theme.getBalloon(0);
        theme.getDroplet();
        theme.getPoppedBall();
        theme.getPoppedBalloon(0);
        theme.getPoppedDroplet();
        theme.getPauseSign();
        theme.unloadImages();

        assertNull("Test Failure: ball Image was not dereferenced", theme.ball);
        assertNull("Test Failure: balloon Image was not dereferenced", theme.balloons);
        assertNull("Test Failure: droplet Image was not dereferenced", theme.droplet);
        assertNull("Test Failure: popped_ball Image was not dereferenced", theme.popped_ball);
        assertNull("Test Failure: popped_balloon Image was not dereferenced", theme.popped_balloons);
        assertNull("Test Failure: popped_droplet Image was not dereferenced", theme.popped_droplet);
        assertNull("Test Failure: pause_sign Image was not dereferenced", theme.pause_sign);
    }
}
