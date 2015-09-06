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
    private boolean isPopped;

    public Balloon(Resources resources, int xCoordinate, int yCoordinate, int velocity) {
        bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.balloon);
        this.xCoordinate = xCoordinate - bitmapImage.getWidth() / 2;
        this.yCoordinate = yCoordinate - bitmapImage.getHeight();
        this.velocity = velocity;
        isPopped = false;
    }

    public void setPoppedImage(Resources resources) {
        xCoordinate = xCoordinate + bitmapImage.getWidth() / 2;
        yCoordinate = yCoordinate + bitmapImage.getHeight();

        bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.popped_balloon);

        xCoordinate = xCoordinate - bitmapImage.getWidth() / 2;
        yCoordinate = yCoordinate - bitmapImage.getHeight();

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
    public boolean isPopped() {
        return isPopped;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmapImage, xCoordinate, yCoordinate, null);
    }

    // Balloons only move vertically
    public void move() {
        yCoordinate = yCoordinate + velocity;
    }

    public boolean handleTouch(int eventX, int eventY) {

        if (eventX >= (xCoordinate - bitmapImage.getWidth() / 2) && eventX <= (xCoordinate + bitmapImage.getWidth()/2)
            && eventY >= (yCoordinate - bitmapImage.getHeight() / 2) && (yCoordinate <= (yCoordinate + bitmapImage.getHeight() / 2))) {
            isPopped = true;
        }

        return isPopped;
    }
}
