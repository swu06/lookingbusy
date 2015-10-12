package com.lookingdynamic.lookingbusy.gameobjects;

import android.test.ActivityTestCase;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;


/**
 *
 * Created by swu on 9/5/2015.
 */
public class BubbleTest extends ActivityTestCase {

    public ThemeManager theme;

    public void setUp() throws Exception{
        super.setUp();
        theme = new ThemeManager(null, getInstrumentation().getTargetContext().getResources());
    }

    public void testCreateDroplet(){
        Bubble testBubble = new Bubble(0, 0, 0);

        assertNotNull("Test Failure: Balloon object is null", testBubble);
        assertEquals("Test Failure: xCoordinate incorrect on initialization", 0, testBubble.xCoordinate);
        assertEquals("Test Failure: yCoordinate incorrect on initialization", 0, testBubble.yCoordinate);
        assertEquals("Test Failure: yVelocity incorrect on initialization", 0, testBubble.yVelocity);
        assertEquals("Test Failure: xVelocity incorrect on initialization", 0, testBubble.xVelocity);
        assertFalse("Test Failure: offScreen incorrect on initialization", testBubble.offScreen);
        assertFalse("Test Failure: popped incorrect on initialization", testBubble.popped);
        assertFalse("Test Failure: temporarilyPopped incorrect on initialization", testBubble.temporarilyPopped);
        assertEquals("Test Failure: timeToStayPopped incorrect on initialization", 0, testBubble.timeToStayPopped);
        assertEquals("Test Failure: poppedCountsRemaining incorrect on initialization", 0, testBubble.poppedCountsRemaining);
    }

    public void testMove() {
        Bubble testBubble = new Bubble(0, 0, 0);

        testBubble.move(theme, 1000, 1000);

        assertEquals("Test Failure: Moving should not alter an unpopped bubble", 0, testBubble.xCoordinate);
        assertEquals("Test Failure: Moving should not alter an unpopped bubble", 0, testBubble.yCoordinate);
        assertFalse("Test Failure: Moving should not alter an unpopped bubble", testBubble.offScreen);
        assertFalse("Test Failure: Moving should not alter an unpopped bubble", testBubble.popped);
        assertFalse("Test Failure: Moving should not alter an unpopped bubble", testBubble.temporarilyPopped);
        assertEquals("Test Failure: Moving should not alter an unpopped bubble", 0, testBubble.timeToStayPopped);
        assertEquals("Test Failure: Moving should not alter an unpopped bubble", 0, testBubble.poppedCountsRemaining);
    }

    public void testMoveTemporarilyPopped() {
        Bubble testBubble = new Bubble(0, 0, 1);
        testBubble.temporarilyPopped = true;
        testBubble.poppedCountsRemaining = testBubble.timeToStayPopped;

        testBubble.move(theme, 1000, 1000);

        assertTrue("Test Failure: Bubble should stay temporarily popped", testBubble.temporarilyPopped);
        assertEquals("Test Failure: Bubble's timeToStayPopped calculation has changed", 3, testBubble.timeToStayPopped);
        assertEquals("Test Failure: Bubble's poppedCountsRemaining did not decrement", 2, testBubble.poppedCountsRemaining);
    }

    public void testMoveUnpops() {
        Bubble testBubble = new Bubble(0, 0, 1);
        testBubble.temporarilyPopped = true;
        testBubble.poppedCountsRemaining = 1;

        testBubble.move(theme, 1000, 1000);

        assertEquals("Test Failure: Bubble's poppedCountsRemaining did not decrement", 0, testBubble.poppedCountsRemaining);
        assertFalse("Test Failure: Bubble should be unpopped when done", testBubble.temporarilyPopped);
    }

    public void testHandleTouchWhenUnPopped() {
        Bubble testBubble = new Bubble(0, 0, 1);

        testBubble.handleTouch(theme, 0, 0);

        assertFalse("Test Failure: Bubbles should never really pop", testBubble.popped);
        assertTrue("Test Failure: Bubbles should temporarily pop when touched", testBubble.temporarilyPopped);
        assertEquals("Test Failure: Bubbles should have a count down to unpop",
                testBubble.timeToStayPopped, testBubble.poppedCountsRemaining);
    }

    public void testHandleTouchWhenPopped() {
        Bubble testBubble = new Bubble(0, 0, 1);
        testBubble.temporarilyPopped = true;

        testBubble.handleTouch(theme, 0, 0);

        assertFalse("Test Failure: Bubbles should never really pop", testBubble.popped);
        assertTrue("Test Failure: Bubbles should stay popped when touched", testBubble.temporarilyPopped);
        assertEquals("Test Failure: Nothing should change when a popped Bubble is touched",
                0, testBubble.poppedCountsRemaining);
    }

    public void testGetScoreValue() {
        PoppableObject testBubble = new Bubble(0, 0, 1);

        assertEquals("Test Failure: Points not based on constant", Bubble.VALUE, testBubble.getScoreValue());
    }

    public void testGetScoreValueTemporarilyPopped() {
        Bubble testBubble = new Bubble(0, 0, 1);

        testBubble.temporarilyPopped = true;
        testBubble.handleTouch(theme, 0, 0);

        assertEquals("Test Failure: Bubbles have no score value when popped", 0, testBubble.getScoreValue());
    }

    public void testGetScoreValueAfterTemporarilyPopped() {
        Bubble testBubble = new Bubble(0, 0, 1);

        testBubble.handleTouch(theme, 0, 0);

        assertEquals("Test Failure: Bubbles have no score value when popped", Bubble.VALUE, testBubble.getScoreValue());
    }

    public void testGetImage() {
        Bubble testBubble = new Bubble(0, 0, 1);

        assertTrue("Test Failure: Bubble Image is not consistent with theme",
                theme.getBubble().equals(testBubble.getImage(theme)));
    }

    public void testGetImageWhenPopped() {
        Bubble testBubble = new Bubble(0, 0, 1);

        assertTrue("Test Failure: Bubble Image is the same even when popped",
                theme.getBubble().equals(testBubble.getImage(theme)));
    }
}
