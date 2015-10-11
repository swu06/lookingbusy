package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.R;

/**
 *
 * Created by swu on 9/15/2015.
 */
public class GameplayModeTest extends ActivityTestCase {

    public void testCreateGameplayMode() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_relaxing));

        assertEquals("Test Failure: Name not properly loaded", "Relaxing Mode", mode.getName());
        assertNotNull("Test Failure: Icon not properly loaded", mode.getIconImageId());
        assertEquals("Test Failure: Lives allows not properly defaulted to 0", 0, mode.getLivesAllowed());
        assertNotNull("Test Failure: Levels not properly loaded", mode.getLevel(0));
        assertEquals("Test Failure: Levels not properly loaded", 1, mode.levels.length);
    }

    public void testGetLevels() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_campaign));

        assertTrue("Test Failure: multiple levels should be loaded, but only 1 was found",
                    mode.levels.length > 1);
        assertNotNull("Test Failure: getLevel should limit array index", mode.getLevel(100));
    }

    public void testGetLevelName() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_campaign));

        assertEquals("Test Failure: Level Name incorrect", "Lvl 1", mode.getLevelName(0));
    }

    public void testGetPointsToNextLevel() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_campaign));

        assertEquals("Test Failure: Level settings incorrect", 250, mode.getPointsToNextLevel(0));
    }

    public void testGetTimeToNextLevel() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_campaign));

        assertEquals("Test Failure: Level settings incorrect", 0, mode.getTimeToNextLevel(0));
    }

    public void testGetTotalObjectsToCreate() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_classic));

        assertEquals("Test Failure: Level settings incorrect", 50, mode.getTotalObjectsToCreate(3));
    }

    public void testIsBubbleGrid() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_grid));

        assertTrue("Test Failure: Level settings incorrect", mode.isBubbleGrid(0));
    }

    public void testGetLivesAllowed() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources,
                getInstrumentation().getTargetContext().getResources().getXml(R.xml.mode_classic));

        assertEquals("Test Failure: Level settings incorrect", 5, mode.getLivesAllowed());
    }
}
