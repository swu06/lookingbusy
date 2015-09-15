package com.example.lookingdynamic.lookingbusy.gameplay;

import android.content.res.XmlResourceParser;
import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/14/2015.
 */
public class LevelTest extends ActivityTestCase{


    public void testCreateLevelObjectOnlySomeValues() {
        Level lvl = new Level(getInstrumentation().getTargetContext().getResources().getXml(R.xml.level1));

        assertNotNull("Test Failure: Should create a non-null Level", lvl);

        assertEquals("Test Failure: Level 1 Value has changed", "Lvl 1", lvl.getName());
        assertEquals("Test Failure: Level 1 Value has changed", 100, lvl.getPointsToNextLevel());
        assertEquals("Test Failure: Level 1 Value has changed", 20, lvl.getPercentChanceOfCreation());
        assertEquals("Test Failure: Level 1 Value has changed", 100, lvl.getBalloonPercentCreated());
        assertEquals("Test Failure: Level 1 Value has changed", 100, lvl.getBalloonPercentSlow());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBalloonPercentMedium());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBalloonPercentFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBalloonPercentSuperFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBallPercentCreated());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBallPercentSlow());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBallPercentMedium());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBallPercentFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBallPercentSuperFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getDropletPercentCreated());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getDropletPercentSlow());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getDropletPercentMedium());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getDropletPercentFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getDropletPercentSuperFast());
    }

    public void testCreateLevelObjectMoreValues() {
        Level lvl = new Level(getInstrumentation().getTargetContext().getResources().getXml(R.xml.level7));

        assertNotNull("Test Failure: Should create a non-null Level", lvl);

        assertEquals("Test Failure: Level 7 Value has changed", "Lvl 7", lvl.getName());
        assertEquals("Test Failure: Level 7 Value has changed", 700, lvl.getPointsToNextLevel());
        assertEquals("Test Failure: Level 7 Value has changed", 40, lvl.getPercentChanceOfCreation());
        assertEquals("Test Failure: Level 7 Value has changed", 50, lvl.getBalloonPercentCreated());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBalloonPercentSlow());
        assertEquals("Test Failure: Level 7 Value has changed", 100, lvl.getBalloonPercentMedium());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBalloonPercentFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBalloonPercentSuperFast());
        assertEquals("Test Failure: Level 7 Value has changed", 20, lvl.getDropletPercentCreated());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getDropletPercentSlow());
        assertEquals("Test Failure: Level 7 Value has changed", 50, lvl.getDropletPercentMedium());
        assertEquals("Test Failure: Level 7 Value has changed", 50, lvl.getDropletPercentFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getDropletPercentSuperFast());
        assertEquals("Test Failure: Level 7 Value has changed", 30, lvl.getBallPercentCreated());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBallPercentSlow());
        assertEquals("Test Failure: Level 7 Value has changed", 0, lvl.getBallPercentMedium());
        assertEquals("Test Failure: Level 7 Value has changed", 100, lvl.getBallPercentFast());
        assertEquals("Test Failure: No value was specified, but one was found", 0, lvl.getBallPercentSuperFast());
    }
}
