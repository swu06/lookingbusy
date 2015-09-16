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

        GameplayMode mode = new GameplayMode(myResources, R.array.relaxing_mode);

        assertEquals("Test Failure: Name not properly loaded", "Relaxing Mode", mode.getName());
        assertNotNull("Test Failure: Levels not properly loaded", mode.getLevel(0));
    }

    public void testgetLevels() {
        Resources myResources = getInstrumentation().getTargetContext().getResources();

        GameplayMode mode = new GameplayMode(myResources, R.array.relaxing_mode);

        assertNotNull("Test Failure: getLevel should limit array index", mode.getLevel(100));
    }
}
