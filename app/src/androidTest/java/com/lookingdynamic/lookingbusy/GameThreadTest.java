package com.lookingdynamic.lookingbusy;

import android.test.ActivityTestCase;

/**
 *
 * Created by swu on 9/9/2015.
 */
public class GameThreadTest extends ActivityTestCase {

    public void testCreateNullThreadStartsPaused() {
        GameThread myThread = new GameThread(null);

        assertTrue("Test Failed: Pause Flag should be set during initialization",
                    myThread.isPausedFlagIsSet());
        assertEquals("Test Failed: Thread state should be NEW during initialization",
                    Thread.State.NEW, myThread.getState());
        assertFalse("Test Failed: Thread should already be alive during initialization",
                    myThread.isAlive());
    }

    public void testCreateNullThreadDoesNothing() {
        GameThread myThread = new GameThread(null);
        myThread.start();
        try {
            myThread.join();
        } catch(Exception e){
        }
        assertEquals("Test Failed: Thread should be TERMINATED when run is complete",
                    Thread.State.TERMINATED, myThread.getState());
        assertFalse("Test Failed: Thread should not be alive after termination",
                myThread.isAlive());
    }

    public void testThreadCanBeRun() {
        GameThread myThread = new GameThread(null);
        assertEquals(Thread.State.NEW, myThread.getState());
        myThread.onStart();
        assertFalse("Test Failed: Thread should not be NEW immediately after starting",
                Thread.State.NEW == myThread.getState());
        assertTrue("Test Failed: Thread should not die on a real startup",
                    myThread.isAlive());
    }

    public void testThreadCanStop() {
        GameThread myThread = new GameThread(null);
        assertEquals(Thread.State.NEW, myThread.getState());
        myThread.onStart();
        assertFalse(Thread.State.NEW == myThread.getState());
        assertFalse(Thread.State.TERMINATED == myThread.getState());
        myThread.onStop();
        try {
            myThread.join(20);
        } catch(InterruptedException e){
            // Thread needs time to detect stop
        }
        assertEquals("Test Failed: Thread should be TERMINATED when run is complete",
                Thread.State.TERMINATED, myThread.getState());
        assertFalse("Test Failed: Thread should not be alive after termination",
                myThread.isAlive());
    }

    public void testThreadCanPause() {
        GameThread myThread = new GameThread(null);
        assertEquals(Thread.State.NEW, myThread.getState());
        myThread.onStart();
        assertTrue((Thread.State.RUNNABLE == myThread.getState())
                || (Thread.State.WAITING == myThread.getState()));
        myThread.onPause();
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect paused state
        }
        assertTrue("Test Failed: Pause did not take  effect",
                    myThread.isInPausedState());
    }

    public void testThreadCanResume() {
        GameThread myThread = new GameThread(null);
        assertEquals(Thread.State.NEW, myThread.getState());
        myThread.onStart();

        myThread.onPause();
        assertTrue("Test Failed: Pause Flag did not set",
                    myThread.isPausedFlagIsSet());
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect paused state
        }
        assertTrue("Test Failed: Pause did not take  effect",
                    myThread.isInPausedState());

        myThread.onResume();
        assertFalse("Test Failed: Pause Flag did not turn off",
                myThread.isPausedFlagIsSet());
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect unpause signal
        }
        assertFalse("Test Failed: Resume did not take effect",
                    myThread.isInPausedState());
    }

    public void testThreadCanResumeRepeatedly() {
        GameThread myThread = new GameThread(null);
        assertEquals(Thread.State.NEW, myThread.getState());
        myThread.onStart();

        myThread.onPause();
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect paused state
        }
        assertTrue("Test Failed: Pause did not take effect",
                myThread.isInPausedState());

        myThread.onResume();
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect unpause signal
        }
        assertFalse("Test Failed: Resume did not take effect",
                myThread.isInPausedState());

        myThread.onPause();
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect paused state
        }
        assertTrue("Test Failed: Pause did not take effect",
                    myThread.isInPausedState());

        myThread.onResume();
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect unpause signal
        }
        assertFalse("Test Failed: Resume did not take effect",
                myThread.isInPausedState());

        myThread.onPause();
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect paused state
        }
        assertTrue("Test Failed: Pause did not take effect",
                    myThread.isInPausedState());
        myThread.onResume();
        assertFalse(myThread.isPausedFlagIsSet());
        try {
            myThread.join(100);
        } catch(InterruptedException e){
            // Thread needs time to detect unpause signal
        }
        assertFalse("Test Failed: Resume did not take effect",
                    myThread.isInPausedState());
    }

}
