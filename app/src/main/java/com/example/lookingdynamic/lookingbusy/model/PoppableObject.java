package com.example.lookingdynamic.lookingbusy.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by swu on 9/6/2015.
 */
public abstract class PoppableObject {

    protected Bitmap bitmapImage;
    protected int xCoordinate;
    protected int yCoordinate;
    protected int xVelocity;
    protected int yVelocity;
    protected boolean popped;
    protected boolean offScreen;

    public boolean isPopped() {
        return popped;
    }

    public boolean isOffScreen() {
        return offScreen;
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmapImage, xCoordinate, yCoordinate, null);
    }
    public boolean handleTouch(int eventX, int eventY) {
        if (eventX >= (xCoordinate - bitmapImage.getWidth() / 2) && eventX <= (xCoordinate + bitmapImage.getWidth()/2)
                && eventY >= (yCoordinate - bitmapImage.getHeight() / 2) && (yCoordinate <= (yCoordinate + bitmapImage.getHeight() / 2))) {
            popped = true;
        }

        return popped;
    }

    abstract public void setPoppedImage(Resources resources);
    abstract public void move(int viewWidth, int viewHeight);
    abstract public int getValue();

}
