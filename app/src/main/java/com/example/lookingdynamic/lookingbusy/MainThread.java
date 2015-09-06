package com.example.lookingdynamic.lookingbusy;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by swu on 9/5/2015.
 */
public class MainThread extends Thread{

    private static final String LOGGER = MainThread.class.getSimpleName();

    // Surface holder that can access the physical surface
    private SurfaceHolder surfaceHolder;
    // The actual view that handles inputs
    // and draws to the surface
    private MainGamePanel gamePanel;

    // flag to hold game state
    private boolean running;
    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        Canvas canvas;
        Log.d(LOGGER, "Starting game loop");
        while (running) {
            canvas = null;
            // try locking the canvas for exclusive pixel editing
            // in the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // update game state
                    // render state to the screen
                    // draws the canvas on the panel
                    this.gamePanel.update();
                    this.gamePanel.onDraw(canvas);
                    //sleep(17);
                }
            //} catch (InterruptedException e) {
              //  Log.e(LOGGER, e.toString());
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }	// end finally
        }
    }
}
