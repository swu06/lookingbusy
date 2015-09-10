package com.example.lookingdynamic.lookingbusy;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by swu on 9/5/2015.
 */
public class GameThread extends Thread implements Runnable{

    private static final String LOGGER = GameThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private PopAllTheThingsGame gamePanel;

    // flag to hold game state
    private boolean running;
    private Object pauseLock;
    private boolean pausedFlagIsSet;
    private boolean inPausedState;

    public GameThread(SurfaceHolder surfaceHolder, PopAllTheThingsGame gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        pauseLock = new Object();
        pausedFlagIsSet = true;
        inPausedState = false;
        running = false;
    }

    public synchronized void updateSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public boolean isPausedFlagIsSet() { return pausedFlagIsSet; }
    public boolean isInPausedState() { return inPausedState; }

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

            // This are the two commands run to keep the game moving: update and draw
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if(canvas != null) {
                        this.gamePanel.update();
                        this.gamePanel.onDraw(canvas);
                    }
                }
                sleep(17);
            } catch (InterruptedException e) {
                Log.e(LOGGER, "Sleep interrupted" + e.toString());
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        Log.d(LOGGER, "Completed the game");
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
