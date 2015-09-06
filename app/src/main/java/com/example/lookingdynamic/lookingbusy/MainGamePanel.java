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

/**
 * Created by swu on 9/5/2015.
 */
public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String LOGGER = MainGamePanel.class.getSimpleName();

    private MainThread thread;
    private Balloon balloon;

    public MainGamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create droid and load bitmap
        balloon = null;

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

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the droid
            balloon.handleActionDown((int)event.getX(), (int)event.getY());

            // check if in the lower part of the screen we exit
            if (event.getY() > getHeight() - 50) {
                thread.setRunning(false);
                ((Activity)getContext()).finish();
            } else {
                Log.d(LOGGER, "Coords: x=" + event.getX() + ",y=" + event.getY());
            }
        } if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // the gestures
            if (balloon.isTouched()) {
                // the droid was picked up and is being dragged
                balloon.setX((int)event.getX());
                balloon.setY((int)event.getY());
            }
        } if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch was released
            if (balloon.isTouched()) {
                balloon.setTouched(false);
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.BLACK);
        balloon.draw(canvas);
    }

    public void update() {

        if(balloon == null) {
            balloon = new Balloon(getResources(), getWidth(), getHeight() / 2, -10);
        }
        else balloon.move();

    }

}
