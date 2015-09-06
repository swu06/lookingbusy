package com.example.lookingdynamic.lookingbusy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lookingdynamic.lookingbusy.model.Ball;
import com.example.lookingdynamic.lookingbusy.model.Balloon;
import com.example.lookingdynamic.lookingbusy.model.Droplet;
import com.example.lookingdynamic.lookingbusy.model.GameObject;

import java.util.Random;
import java.util.Vector;

/**
 * Created by swu on 9/5/2015.
 */
public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String LOGGER = MainGamePanel.class.getSimpleName();

    private MainThread thread;
    private Vector<GameObject> activeGameObjects;
    private Vector<GameObject> poppedGameObjects;
    private Random rand;
    private int counter;


    public MainGamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create holder for balloons
        activeGameObjects = new Vector<GameObject>();
        poppedGameObjects = new Vector<GameObject>();
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

        for(GameObject gameObject : activeGameObjects) {
            if (gameObject.handleTouch((int) event.getX(), (int) event.getY())) {
                gameObject.setPoppedImage(getResources());
                break;
            }
        }

        return true;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.BLACK);
        for(GameObject gameObject : activeGameObjects) {
            gameObject.draw(canvas);
        }
        for(GameObject gameObject : poppedGameObjects) {
            gameObject.draw(canvas);
        }
    }

    public synchronized void update() {
        counter++;

        //Handle Dead Balloons
        poppedGameObjects.removeAllElements();

        //Move Active Balloons, remove popped balloons
        for(int i=0; i < activeGameObjects.size(); i++) {
            GameObject gameObject = activeGameObjects.get(i);

            if(gameObject.isPopped() || gameObject.isOffScreen()) {
                poppedGameObjects.add(gameObject);
                activeGameObjects.remove(i);
            } else {
                gameObject.move(getWidth(), getHeight());
            }
        }

        // Only add new elements every 10th update
        if(counter % 10 == 1 || activeGameObjects.size() == 0) {
            //Odds of getting another balloon are 10 in 100
            int randomSeed = rand.nextInt(100) + 1;

            if (randomSeed < 10) {
                activeGameObjects.add(new Balloon(getResources(), getWidth() * randomSeed / 10, getHeight(), -2 * randomSeed));
            } else if (randomSeed < 19) {
                activeGameObjects.add(new Droplet(getResources(), getWidth() * (randomSeed - 9) / 10, 0, randomSeed));
            } else if (randomSeed < 29) {
                activeGameObjects.add(new Ball(getResources(), getWidth(), 0, randomSeed, 2 * randomSeed));
            }

            Log.d(LOGGER, "Total number of objects is now:" + activeGameObjects.size());
        }

    }

}
