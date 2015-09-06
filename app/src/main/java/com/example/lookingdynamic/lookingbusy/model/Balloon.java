package com.example.lookingdynamic.lookingbusy.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.lookingdynamic.lookingbusy.R;

/**
 *
 * This is a balloon model.  It knows everything possible about the balloon
 * but has no brains to do anything about it.  The controller (MainGamePanel)
 * does not know any of this information, but uses it to make the world turn.
 * Created by swu on 9/5/2015.
 */
public class Balloon {

    private static final String LOGGER = Balloon.class.getSimpleName();

    private Bitmap bitmapImage;
    private int xCoordinate;
    private int yCoordinate;
    private int velocity;       // Velocity is the number of pixels that will be moved each loop
    private boolean touched;

    public Balloon(Resources resources, int xCoordinate, int yCoordinate, int velocity) {
        this.bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.balloon);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.velocity = velocity;
    }

    public Bitmap getBitmap() {
        return bitmapImage;
    }
    public int getX() {
        return xCoordinate;
    }
    public int getY() {
        return yCoordinate;
    }
    public int getVelocity() {
        return velocity;
    }

    // Balloons only move horizontally
    public void setX(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setY(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public boolean isTouched() { return touched; }
    public void setTouched(boolean touched) {
        this.touched = touched;
    }



    // This is too intelligent for a model
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmapImage, xCoordinate, yCoordinate, null);
        Log.d(LOGGER, "Drawing balloon here: " + xCoordinate + ", " + yCoordinate);
    }

    public void move() {
        yCoordinate = yCoordinate + velocity;
    }

    /**
     * Handles the {@link MotionEvent.ACTION_DOWN} event. If the event happens on the
     * bitmap surface then the touched state is set to <code>true</code> otherwise to <code>false</code>
     * @param eventX - the event's X coordinate
     * @param eventY - the event's Y coordinate
     */
    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (xCoordinate - bitmapImage.getWidth() / 2) && (eventX <= (xCoordinate + bitmapImage.getWidth()/2))) {
            if (eventY >= (yCoordinate - bitmapImage.getHeight() / 2) && (yCoordinate <= (yCoordinate + bitmapImage.getHeight() / 2))) {
                // droid touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }

    }
}
