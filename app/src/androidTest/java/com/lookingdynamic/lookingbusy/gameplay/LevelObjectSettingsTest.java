package com.lookingdynamic.lookingbusy.gameplay;

import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.gameplay.LevelObjectSettings;

/**
 * Created by swu on 9/14/2015.
 */
public class LevelObjectSettingsTest extends ActivityTestCase{

    public void testCreateLevelObject() {
        LevelObjectSettings los = new LevelObjectSettings();

        assertNotNull("Test Failure: Should create a non-null LevelObjectSettings", los);
        assertEquals("Test Failure: Values should start at 0", 0, los.getPercentCreated());
        assertEquals("Test Failure: Values should start at 0", 0, los.getPercentFast());
        assertEquals("Test Failure: Values should start at 0", 0, los.getPercentMedium());
        assertEquals("Test Failure: Values should start at 0", 0, los.getPercentSlow());
        assertEquals("Test Failure: Values should start at 0", 0, los.getPercentSuperFast());
    }

    public void testPercentCreated() {
        LevelObjectSettings los = new LevelObjectSettings();

        los.setPercentCreated(2);
        assertEquals("Test Failure: Values should be set directly", 2, los.getPercentCreated());
    }

    public void testPercentFast() {
        LevelObjectSettings los = new LevelObjectSettings();

        los.setPercentFast(2);
        assertEquals("Test Failure: Values should be set directly", 2, los.getPercentFast());
    }

    public void testPercentMedium() {
        LevelObjectSettings los = new LevelObjectSettings();

        los.setPercentMedium(2);
        assertEquals("Test Failure: Values should be set directly", 2, los.getPercentMedium());
    }

    public void testPercentSlow() {
        LevelObjectSettings los = new LevelObjectSettings();

        los.setPercentSlow(2);
        assertEquals("Test Failure: Values should be set directly", 2, los.getPercentSlow());
    }

    public void testPercentSuperFast() {
        LevelObjectSettings los = new LevelObjectSettings();

        los.setPercentSuperFast(2);
        assertEquals("Test Failure: Values should be set directly", 2, los.getPercentSuperFast());
    }
}
