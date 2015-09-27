package com.example.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.test.ActivityTestCase;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/15/2015.
 */
public class GameplayModeTest extends ActivityTestCase {

    public void testCreateGameplayMode() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources, myResources.obtainTypedArray(R.array.relaxing_mode));

        assertEquals("Test Failure: Name not properly loaded", "Relaxing Mode", mode.getName());
        assertNotNull("Test Failure: Icon not properly loaded", mode.getIconImageId());
        assertNotNull("Test Failure: Levels not properly loaded", mode.getLevel(0));
    }

    public void testSafeLevel() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources, myResources.obtainTypedArray(R.array.relaxing_mode));

        assertEquals("Test Failure: SafeLevel should not adjust viable currentLevel values", 0, mode.safeLevel(0));
        assertEquals("Test Failure: SafeLevel should adjust currentLevel values that are too high", 0, mode.safeLevel(1));
    }

    public void testGetLevels() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources, myResources.obtainTypedArray(R.array.challanging_mode));

        assertTrue("Test Failure: multiple levels should be loaded, but only 1 was found",
                    mode.levels.length > 1);
        assertNotNull("Test Failure: getLevel should limit array index", mode.getLevel(100));
    }
}
