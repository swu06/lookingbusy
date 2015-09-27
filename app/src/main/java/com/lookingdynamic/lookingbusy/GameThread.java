package com.lookingdynamic.lookingbusy;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by swu on 9/5/2015.
 */
public class GameThread extends Thread implements Runnable{

    private static final String LOGGER = GameThread.class.getSimpleName();
    private static final int SLEEP_TIME_MS = 17;

    private SurfaceHolder surfaceHolder;
    private PopAllTheThingsGame gamePanel;

    // flag to hold game state
    private boolean running;
    private Object pauseLock;
    private Object sleepLock;
    private boolean pausedFlagIsSet;
    private boolean inPausedState;

    public GameThread(PopAllTheThingsGame gamePanel) {
        super();
        this.gamePanel = gamePanel;
        pauseLock = new Object();
        sleepLock = new Object();
        pausedFlagIsSet = true;
        inPausedState = false;
        running = false;
    }

    public void updateSurfaceHolder(SurfaceHolder surfaceHolder) {
        if(this.surfaceHolder != null) {
            synchronized (this.surfaceHolder) {
                this.surfaceHolder = surfaceHolder;
            }
        } else {
            this.surfaceHolder = surfaceHolder;
        }

    }

    public boolean isPausedFlagIsSet() { return pausedFlagIsSet; }
    public boolean isInPausedState() { return inPausedState; }
    public boolean isRunning() { return running; }

    /*
     * This is the main loop for the game. It handles the two main calls:
     * update and draw, while occasionally watching for pauses.  It also
     * has a 17 ms sleep to make the movement more fluid
     */
    @Override
    public void run() {
        Canvas canvas;
        Log.d(LOGGER, "Starting the game");
        while (running) {

            // This synchronized block handles pausing
            checkAndHandlePause();

            // This are the two commands run to keep the game moving: update and draw
            canvas = null;
            try {
                if(surfaceHolder != null) {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        if(canvas != null && gamePanel != null) {
                            gamePanel.update();
                        }
                    }
                }


                checkAndHandlePause();
                if(surfaceHolder != null) {
                    synchronized (surfaceHolder) {
                        if (canvas != null && gamePanel != null) {
                            gamePanel.onDraw(canvas);
                        }
                    }
                }
                waitUnlessNeeded(SLEEP_TIME_MS);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        Log.d(LOGGER, "Completed the game");
    }

    private void checkAndHandlePause() {
        synchronized (pauseLock) {
            while (pausedFlagIsSet) {
                inPausedState = true;
                Log.d(LOGGER, "Game is pausedFlagIsSet");
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                }
                inPausedState = false;
                Log.d(LOGGER, "Game is unpaused");
            }
        }
    }

    private void waitUnlessNeeded(int time) {
        synchronized (sleepLock) {
            try {
                sleepLock.wait(time);
            } catch (InterruptedException e) {
            }
        }
    }

    public void wakeIfSleeping() {
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    public void onPause() {
        synchronized (pauseLock) {
            pausedFlagIsSet = true;
        }
        Log.d(LOGGER, "Game is pausing");
    }

    public void onResume() {
        synchronized (pauseLock) {
            pausedFlagIsSet = false;
            pauseLock.notifyAll();
        }
        Log.d(LOGGER, "Game is unpausing");
    }

    public void onStart() {
        running = true;
        this.start();
        Log.d(LOGGER, "Game is starting");
    }

    public void onStop() {
        running = false;
        Log.d(LOGGER, "Game is stopping");
    }
}
