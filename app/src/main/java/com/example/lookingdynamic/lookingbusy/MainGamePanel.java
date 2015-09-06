package com.example.lookingdynamic.lookingbusy;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lookingdynamic.lookingbusy.model.Balloon;

import java.util.Random;
import java.util.Vector;

/**
 * Created by swu on 9/5/2015.
 */
public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String LOGGER = MainGamePanel.class.getSimpleName();

    private MainThread thread;
    private Vector<Balloon> activeBalloons;
    private Vector<Balloon> poppedBalloons;
    private Random rand;

    public MainGamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create holder for balloons
        activeBalloons = new Vector<Balloon>();
        poppedBalloons = new Vector<Balloon>();
        rand = new Random();

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
    public boolean onTouchEvent(MotionEvent event) {

        for(Balloon myBalloon : activeBalloons) {
            if (myBalloon.handleTouch((int) event.getX(), (int) event.getY())) {
                myBalloon.setPoppedImage(getResources());
                break;
            }
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.BLACK);
        for(Balloon myBalloon : activeBalloons) {
            myBalloon.draw(canvas);
        }
        for(Balloon myBalloon : poppedBalloons) {
            myBalloon.draw(canvas);
        }
    }

    public void update() {

        //Handle Dead Balloons
        poppedBalloons.removeAllElements();

        //Move Active Balloons
        for(Balloon myBalloon : activeBalloons) {
            if(myBalloon.isPopped()) {
                poppedBalloons.add(myBalloon);
                activeBalloons.remove(myBalloon);
            } else {
                myBalloon.move();
            }
        }

        //Decide how many balloons to create (0 - 1)
        int balloonChance = rand.nextInt(100);

        if(balloonChance < 10) {
            activeBalloons.add(new Balloon(getResources(), getWidth() * balloonChance / 10 , getHeight(), -3 * balloonChance));
        }

    }

}
