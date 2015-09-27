package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.BitmapFactory;
import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.R;
import com.lookingdynamic.lookingbusy.gameobjects.RandomBot;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 * This Test class tests the methods within Ball, and the methods implemented in the
 * PoppableObject abstract parent class.
 * Created by swu on 9/5/2015.
 */
public class RandomBotTest extends ActivityTestCase {

    public ThemeManager theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());
        theme.setRandomBotImage(BitmapFactory.decodeResource(getInstrumentation().getTargetContext().getResources(), R.drawable.bright_ball));
    }

    public void testCreateRandomBot(){
        RandomBot testBot = new RandomBot(200, 200, 1);

        assertTrue("Test Failure: RandomBot did not spawn near the middle.  Instead it spawned at " + testBot.xCoordinate,
                        50 <= testBot.xCoordinate && testBot.xCoordinate <= 150);
        assertTrue("Test Failure: RandomBot did not spawn near the middle.  Instead it spawned at " + testBot.yCoordinate,
                        50 <= testBot.yCoordinate && testBot.yCoordinate <= 150);
        assertFalse("Test Failure: RandomBot spawned in the popped state. This is not expected.", testBot.popped);
        assertFalse("Test Failure: RandomBot spawned in the offScreen state. This is not expected.", testBot.offScreen);
    }

}
