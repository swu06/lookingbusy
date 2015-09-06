package com.example.lookingdynamic.lookingbusy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lookingdynamic.lookingbusy.model.Ball;
import com.example.lookingdynamic.lookingbusy.model.Balloon;
import com.example.lookingdynamic.lookingbusy.model.Droplet;
import com.example.lookingdynamic.lookingbusy.model.PoppableObject;
import com.example.lookingdynamic.lookingbusy.model.GameStatistics;

import java.util.Random;
import java.util.Vector;

/**
 * Created by swu on 9/5/2015.
 */
public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String LOGGER = MainGamePanel.class.getSimpleName();

    private MainThread thread;
    private Vector<PoppableObject> activePoppableObjects;
    private Vector<PoppableObject> poppedPoppableObjects;
    private GameStatistics stats;
    private Paint whiteFont;
    private Random rand;
    private int counter;


    public MainGamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create holder for balloons
        activePoppableObjects = new Vector<PoppableObject>();
        poppedPoppableObjects = new Vector<PoppableObject>();
        stats = new GameStatistics();
        whiteFont = new Paint();
        whiteFont.setColor(Color.WHITE);
        rand = new Random();
        counter = 0;

        // create the game loop thread
        thread = new MainThread(getHolder(), this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOGGER, "Surface is being destroyed");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        Log.d(LOGGER, "Thread was shut down cleanly");
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {

        for(PoppableObject poppableObject : activePoppableObjects) {
            if (poppableObject.handleTouch((int) event.getX(), (int) event.getY())) {
                poppableObject.setPoppedImage(getResources());
                break;
            }
        }

        return true;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.BLACK);

        canvas.drawText(stats.toString(), 0, 0, whiteFont);
        for(PoppableObject poppableObject : activePoppableObjects) {
            poppableObject.draw(canvas);
        }
        for(PoppableObject poppableObject : poppedPoppableObjects) {
            poppableObject.draw(canvas);
        }
    }

    public synchronized void update() {
        counter++;

        //Handle Dead Balloons
        poppedPoppableObjects.removeAllElements();

        //Move Active Balloons, remove popped balloons
        for(int i=0; i < activePoppableObjects.size(); i++) {
            PoppableObject poppableObject = activePoppableObjects.get(i);

            if(poppableObject.isPopped()) {
                stats.addToScore(poppableObject.getValue());
                poppedPoppableObjects.add(poppableObject);
                activePoppableObjects.remove(i);
            } else if(poppableObject.isOffScreen()) {
                stats.addToScore(-1);
                activePoppableObjects.remove(i);
            } else {
                poppableObject.move(getWidth(), getHeight());
            }
        }

        // Only add new elements every 10th update, or when there is nothing in play
        if(counter % 10 == 1 || activePoppableObjects.size() == 0) {
            //Odds of getting another balloon are 10 in 100
            int randomType = rand.nextInt(10);
            int randomLocation = rand.nextInt(9) +1;
            int randomSpeed = rand.nextInt(10) + 20;

            if (randomType < 5) {
                activePoppableObjects.add(new Balloon(getResources(), getWidth() * randomLocation / 10, getHeight(), -2 * randomSpeed));
            } else if (randomType < 7) {
                activePoppableObjects.add(new Droplet(getResources(), getWidth() * randomLocation / 10, randomSpeed));
            } else if (randomType == 8) {
                randomType = rand.nextInt(4);
                if (randomType == 0) {
                    activePoppableObjects.add(new Ball(getResources(), 0, 0, 2 * randomSpeed, randomSpeed));
                } else if (randomType == 1) {
                    activePoppableObjects.add(new Ball(getResources(), getWidth(), 0, -2 * randomSpeed, randomSpeed));
                } else if (randomType == 2) {
                    activePoppableObjects.add(new Ball(getResources(), 0, getHeight(), 2 * randomSpeed, -1 * randomSpeed));
                } else if (randomType == 3) {
                    activePoppableObjects.add(new Ball(getResources(), getWidth(), getHeight(), -2 * randomSpeed, -1 * randomSpeed));
                }
            } // Do nothing 1/10 times
        }

    }

}
