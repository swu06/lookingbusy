package com.example.lookingdynamic.lookingbusy;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by swu on 9/5/2015.
 */
public class MainThread extends Thread{

    private static final String LOGGER = MainThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private PopAllTheThingsGame gamePanel;

    // flag to hold game state
    private boolean running;
    private Object pauseLock;
    private boolean paused;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, PopAllTheThingsGame gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        pauseLock = new Object();
        paused = true;
        running = false;
    }

    public synchronized void updateSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public void run() {
        Canvas canvas;
        Log.d(LOGGER, "Starting game loop");
        while (running) {

            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

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
        Log.d(LOGGER, "Completed Running Thread");
    }

    public void onPause() {
        synchronized (pauseLock) {
            paused = true;
        }
    }

    public void onResume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }
}
